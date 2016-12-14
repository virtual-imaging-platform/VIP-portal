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
package fr.insalyon.creatis.vip.api.tools.spring;

import org.hamcrest.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;

/**
 * Created by abonnet on 8/3/16
 *
 * // TODO verify generic types of suppliers
 */
public class JsonCustomObjectMatcher<T> extends TypeSafeDiagnosingMatcher<Map<String,?>> {

    private final T expectedBean;
    private final Matcher<Integer> nonNullPropertiesCountMatcher;
    private final List<Matcher<?>> propertyMatchers;


    public JsonCustomObjectMatcher(T expectedBean,
                                   Map<String, Function> suppliers) {
        this(expectedBean, suppliers, new HashMap<>());
    }

    public JsonCustomObjectMatcher(T expectedBean,
                                   Map<String, Function> suppliers,
                                   Map<Class,Map<String,Function>> suppliersRegistry) {
        super(Map.class);
        this.expectedBean = expectedBean;
        propertyMatchers = new ArrayList<>(suppliers.size());
        suppliersRegistry.put(expectedBean.getClass(), suppliers);
        for (Entry<String,Function> supplierEntry : suppliers.entrySet()) {
            String property = supplierEntry.getKey();
            Object expectedValue = supplierEntry.getValue().apply(expectedBean);
            if (expectedValue != null) {
                Matcher expectedValueMatcher =
                        getGenericMatcher(expectedValue, suppliersRegistry);
                Matcher<Map> entryMatcher =
                        Matchers.hasEntry(equalTo(property),
                                expectedValueMatcher);
                propertyMatchers.add(
                        entryMatcher);
            }
        }
        nonNullPropertiesCountMatcher = equalTo(propertyMatchers.size());
    }

    @Override
    public boolean matchesSafely(Map<String,?> map, Description mismatch) {
        if (!nonNullPropertiesCountMatcher.matches(countNonNullValue(map))) {
            return false;
        }
        for (Matcher<?> propertyMatcher : propertyMatchers) {
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

    public int countNonNullValue(Map<String,?> map) {
        int counter = 0;
        for (Entry<String,?> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                counter++;
            }
        }
        return counter;
    }

    private static class JsonMapMatcher extends TypeSafeDiagnosingMatcher<Map<String,?>> {

        private final Matcher<Integer> sizeMatcher;
        private final List<Matcher<?>> mapEntriesMatchers = new ArrayList<>();

        private JsonMapMatcher(
                Map<?,?> expectedMap,
                Map<Class, Map<String, Function>> suppliersRegistry) {
            sizeMatcher = equalTo(expectedMap.size());
            for (Map.Entry<?, ?> expectedEntry : expectedMap.entrySet()) {
                String expectedKey = expectedEntry.getKey().toString();
                Object expectedValue = expectedEntry.getValue();
                Matcher<String> keyMatcher = equalTo(expectedKey);
                Matcher valueMatcher = getGenericMatcher(expectedValue, suppliersRegistry);
                Matcher<Map> entryMatcher = hasEntry(keyMatcher, valueMatcher);
                mapEntriesMatchers.add(entryMatcher);
            }
        }

        @Override
        protected boolean matchesSafely(Map<String, ?> item, Description mismatchDescription) {
            if (!sizeMatcher.matches(item.size())) {
                mismatchDescription.appendText("map size is different");
                sizeMatcher.describeMismatch(item, mismatchDescription);
                return false;
            }
            for (Matcher<?> entryMatcher : mapEntriesMatchers) {
                if (!entryMatcher.matches(item)) {
                    entryMatcher.describeMismatch(item, mismatchDescription);
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("same entries as")
                    .appendList(" [", ", ", "]", mapEntriesMatchers);;
        }
    }

    private static Matcher<?> getGenericMatcher(
            Object expectedValue,
            Map<Class, Map<String, Function>> suppliersRegistry) {
        // if null, the actual value must be null
        if (expectedValue == null) {
            return nullValue();
        } else {
            // else it all depends of the value type
            Class<?> expectedValueType = expectedValue.getClass();
            // if its a primitive,the actual value must be equal
            if (expectedValueType.equals(String.class) || isPrimitiveOrWrapper(expectedValueType)) {
                return equalTo(expectedValue);
            } else if (Iterable.class.isAssignableFrom(expectedValueType)) {
                // if it's iterable, the actual collection should contain in any order the same elements
                Iterable iterable = (Iterable) expectedValue;
                if (iterable.iterator().hasNext()) {
                    List<Matcher<? super Object>> collectionItemMatchers = new ArrayList<>();
                    for (Object o : iterable) {
                        Matcher matcher = getGenericMatcher(o, suppliersRegistry);
                        collectionItemMatchers.add(matcher);
                    }
                    return containsInAnyOrder(collectionItemMatchers);
                } else {
                    // except if it's empty, when the actual collection should also be empty
                    return empty(); // or null ??? anyOf(empty(), nullValue());
                }
            } else if (Enum.class.isAssignableFrom(expectedValueType)) {
                // if its an enum, the actual value should be equal
                return equalTo(expectedValue.toString()); // tostring to avoid type difference
            } else if (Map.class.isAssignableFrom(expectedValueType)) {
                Map<?,?> map = (Map) expectedValue;
                return new JsonMapMatcher(map, suppliersRegistry);
            } else {
                return getCustomObjectMatcherFromRegistry(expectedValue, suppliersRegistry);
            }
        }
    }

    private static Matcher<Map<String, ?>> getCustomObjectMatcherFromRegistry(
            Object o,
            Map<Class, Map<String, Function>> suppliersRegistry) {
        for (Entry<Class, Map<String,Function>> supplierEntry : suppliersRegistry.entrySet()) {
            if (supplierEntry.getKey().isAssignableFrom(o.getClass())) {
                return jsonCorrespondsTo(o, supplierEntry.getValue(), suppliersRegistry);
            }
        }
        throw new RuntimeException("cant find supplier for type " + o.getClass().getSimpleName());
    }

    @Factory
    public static <T> Matcher<Map<String,?>> jsonCorrespondsTo(
            T expectedBean,
            Map<String, Function> suppliers) {
        return new JsonCustomObjectMatcher<T>(expectedBean, suppliers);
    }

    @Factory
    public static <T> Matcher<Map<String,?>> jsonCorrespondsTo(
            T expectedBean,
            Map<String, Function> suppliers,
            Map<Class, Map<String, Function>> suppliersRegistry) {
        return new JsonCustomObjectMatcher<T>(expectedBean, suppliers, suppliersRegistry);
    }

    public static <T> Map<String, Function> formatSuppliers(
            List<String> mapKeys, Function<T,?>... suppliers) {
        if (mapKeys.size() != suppliers.length) {
            throw new IllegalArgumentException("wrong suppliers number given");
        }
        Map<String, Function> suppliersMap = new HashMap<>();
        for (int it=0; it<suppliers.length; it++) {
            suppliersMap.put(mapKeys.get(it), suppliers[it]);
        }
        return suppliersMap;
    }

}
