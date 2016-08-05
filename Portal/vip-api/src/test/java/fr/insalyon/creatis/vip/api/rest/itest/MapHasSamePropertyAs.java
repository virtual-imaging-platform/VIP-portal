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
package fr.insalyon.creatis.vip.api.rest.itest;

import org.hamcrest.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.beans.PropertyUtil.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by abonnet on 8/3/16.
 * <p>
 * TODO see license (largery inspired from {@link org.hamcrest.beans.SamePropertyValuesAs})
 * TODO extraProperties are tested but not the missing properties
 */
public class MapHasSamePropertyAs<T> extends TypeSafeDiagnosingMatcher<Map<String,Object>> {

    private final T expectedBean;
    private final Set<String> propertyNames;
    private final List<PropertyMatcher> propertyMatchers;


    public MapHasSamePropertyAs(T expectedBean) {
        super(Map.class);
        PropertyDescriptor[] descriptors = propertyDescriptorsFor(expectedBean, Object.class);
        this.expectedBean = expectedBean;
        this.propertyNames = propertyNamesFrom(descriptors);
        this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors);
    }

    @Override
    public boolean matchesSafely(Map<String,Object> bean, Description mismatch) {
        return hasNoExtraProperties(bean, mismatch)
                && hasMatchingValues(bean, mismatch);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same property values as " + expectedBean.getClass().getSimpleName())
                .appendList(" [", ", ", "]", propertyMatchers);
    }

    private boolean hasNoExtraProperties(Map<String,Object> item, Description mismatchDescription) {
        Set<String> actualPropertyNames = new HashSet<>(item.keySet());
        actualPropertyNames.removeAll(propertyNames);
        if (!actualPropertyNames.isEmpty()) {
            mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
            return false;
        }
        return true;
    }

    private boolean hasMatchingValues(Map<String,Object> item, Description mismatchDescription) {
        for (PropertyMatcher propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(item)) {
                propertyMatcher.describeMismatch(item, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    private static <T> List<PropertyMatcher> propertyMatchersFor(T bean, PropertyDescriptor[] descriptors) {
        List<PropertyMatcher> result = new ArrayList<>(descriptors.length);
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            result.add(new PropertyMatcher(propertyDescriptor, bean));
        }
        return result;
    }

    private static Set<String> propertyNamesFrom(PropertyDescriptor[] descriptors) {
        HashSet<String> result = new HashSet<String>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            result.add(propertyDescriptor.getDisplayName());
        }
        return result;
    }

    public static class PropertyMatcher extends TypeSafeDiagnosingMatcher<Map<String,Object>> {
        private final Matcher<?> matcher;
        private final String propertyName;

        public PropertyMatcher(PropertyDescriptor descriptor, Object expectedObject) {
            this.propertyName = descriptor.getDisplayName();
            Method readMethod = descriptor.getReadMethod();
            Class<?> propertyType = descriptor.getPropertyType();
            Object value = readProperty(readMethod, expectedObject);
            if (propertyType.equals(String.class) || propertyType.isPrimitive()) {
                this.matcher = equalTo(readProperty(readMethod, expectedObject));
            } else if (Iterable.class.isAssignableFrom(propertyType)) {
                Iterable iterable = (Iterable) value;
                if (iterable.iterator().hasNext()) {
                    matcher = equalTo(value);
                } else {
                    matcher = nullValue();
                }
            } else {
                matcher = mapHasSamePropertyAs(value);
            }
        }

        @Override
        public boolean matchesSafely(Map<String,Object> map, Description mismatch) {
            final Object actualValue = map.get(propertyName);
            if (!matcher.matches(actualValue)) {
                mismatch.appendText(propertyName + " ");
                matcher.describeMismatch(actualValue, mismatch);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(propertyName + ": ").appendDescriptionOf(matcher);
        }
    }

    private static Object readProperty(Method method, Object target) {
        try {
            return method.invoke(target, NO_ARGUMENTS);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
        }
    }

    /**
     * Creates a matcher that matches when the examined object has values for all of
     * its JavaBean properties that are equal to the corresponding values of the
     * specified bean.
     * <p/>
     * For example:
     * <pre>assertThat(myBean, samePropertyValuesAs(myExpectedBean))</pre>
     *
     * @param expectedBean the bean against which examined beans are compared
     */
    @Factory
    public static <T> Matcher<Map<String,Object>> mapHasSamePropertyAs(T expectedBean) {
        return new MapHasSamePropertyAs<T>(expectedBean);
    }

}
