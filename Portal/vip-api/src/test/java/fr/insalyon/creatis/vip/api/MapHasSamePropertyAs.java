/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api;

import org.hamcrest.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;

/**
 * Created by abonnet on 8/3/16.
 * <p>
 * TODO see license (largery inspired from {@link org.hamcrest.beans.SamePropertyValuesAs})
 * TODO add object registry to test nested objects
 */
public class MapHasSamePropertyAs<T> extends TypeSafeDiagnosingMatcher<Map<String,?>> {

    private final T expectedBean;
    private final List<PropertyMatcher<T>> propertyMatchers;


    public MapHasSamePropertyAs(T expectedBean,
                                Map<String, Function<T,?>> suppliers) {
        this(expectedBean, suppliers, null);
    }

    public MapHasSamePropertyAs(T expectedBean,
                                Map<String, Function<T,?>> suppliers,
                                Map<Class<?>,Map<String,Function<Object,?>>> suppliersRegistry) {
        super(Map.class);
        this.expectedBean = expectedBean;
        propertyMatchers = new ArrayList<>(suppliers.size());
        for (Entry<String,Function<T,?>> supplierEntry : suppliers.entrySet()) {
            propertyMatchers.add(new PropertyMatcher<T>(expectedBean,
                    supplierEntry.getKey(), supplierEntry.getValue(), suppliersRegistry));
        }
    }

    @Override
    public boolean matchesSafely(Map<String,?> map, Description mismatch) {
        for (PropertyMatcher propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(map)) {
                propertyMatcher.describeMismatch(map, mismatch);
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same property values as :")
                .appendValue(expectedBean.getClass().getSimpleName())
                .appendList(" [", ", ", "]", propertyMatchers);
    }

    public static class PropertyMatcher<T> extends TypeSafeDiagnosingMatcher<Map<String,Object>> {
        private final Matcher<?> matcher;
        private final String key;

        public PropertyMatcher(T expectedBean,
                               String key, Function<T, ?> supplier,
                               Map<Class<?>, Map<String, Function<Object, ?>>> suppliersRegistry) {
            this.key = key;
            Object value = supplier.apply(expectedBean);
            if (value == null) {
                matcher = nullValue();
            } else {
                Class<?> propertyType = value.getClass();
                if (propertyType.equals(String.class) || isPrimitiveOrWrapper(propertyType)) {
                    matcher = equalTo(value);
                } else if (Iterable.class.isAssignableFrom(propertyType)) {
                    Iterable iterable = (Iterable) value;
                    if (iterable.iterator().hasNext()) {
                        List<Matcher<? super Map<String,?>>> collectionItemMatchers = new ArrayList<>();
                        for (Object o : iterable) {
                            collectionItemMatchers.add(getMatcherFromRegistry(o, suppliersRegistry));
                        }
                        matcher = Matchers.containsInAnyOrder(collectionItemMatchers);
                    } else {
                        matcher = anyOf(empty(), nullValue());
                    }
                } else if (Enum.class.isAssignableFrom(propertyType)) {
                    matcher = equalTo(value.toString());
                } else if (Map.class.isAssignableFrom(propertyType)) {
                    Map map = (Map) value;
                    matcher = equalTo(map);
                } else {
                    throw new RuntimeException("nested object not implemented yet");
                }
            }
        }

        private Matcher<Map<String, ?>> getMatcherFromRegistry(Object o, Map<Class<?>, Map<String, Function<Object, ?>>> suppliersRegistry) {
            for (Entry<Class<?>, Map<String,Function<Object,?>>> supplierEntry : suppliersRegistry.entrySet()) {
                if (supplierEntry.getKey().isAssignableFrom(o.getClass())) {
                    return mapHasSamePropertyAs(o, supplierEntry.getValue(), suppliersRegistry);
                }
            }
            throw new RuntimeException("cant find supplier for type " + o.getClass().getSimpleName());
        }

        @Override
        public boolean matchesSafely(Map<String,Object> map, Description mismatch) {
            final Object actualValue = map.get(key);
            if (!matcher.matches(actualValue)) {
                mismatch.appendText(key + " ");
                matcher.describeMismatch(actualValue, mismatch);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(key + ": ").appendDescriptionOf(matcher);
        }
    }

    @Factory
    public static <T> Matcher<Map<String,?>> mapHasSamePropertyAs(T expectedBean,
                                                                  Map<String, Function<T,?>> suppliers) {
        return new MapHasSamePropertyAs<T>(expectedBean, suppliers);
    }

    @Factory
    public static <T> Matcher<Map<String,?>> mapHasSamePropertyAs(T expectedBean,
                                                                  Map<String, Function<T,?>> suppliers,
                                                                  Map<Class<?>, Map<String, Function<Object, ?>>> suppliersRegistry) {
        return new MapHasSamePropertyAs<T>(expectedBean, suppliers, suppliersRegistry);
    }

    public static <T> Map<String, Function<T,?>> formatSuppliers(
            List<String> mapKeys, Function<T,?>... suppliers) {
        if (mapKeys.size() != suppliers.length) {
            throw new IllegalArgumentException("wrong supplliers number given");
        }
        Map<String, Function<T,?>> suppliersMap = new HashMap<>();
        for (int it=0; it<suppliers.length; it++) {
            suppliersMap.put(mapKeys.get(it), suppliers[it]);
        }
        return suppliersMap;
    }

}
