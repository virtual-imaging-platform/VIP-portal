package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationInfo;

import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AuthenticationInfoTestUtils {

    public static final Map<String,Function> authenticationInfoSuppliers;

    static {
        authenticationInfoSuppliers = getAuthenticationInfoSuppliers();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Function> getAuthenticationInfoSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList("httpHeader", "httpHeaderValue"),
                AuthenticationInfo::getHttpHeader,
                AuthenticationInfo::getHttpHeaderValue);
    }

    public static Matcher<Map<String, ?>> jsonCorrespondsToAuthenticationInfo(
            String header, String headerValue) {
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.setHttpHeader(header);
        authenticationInfo.setHttpHeaderValue(headerValue);
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return JsonCustomObjectMatcher.jsonCorrespondsTo(
                authenticationInfo,
                authenticationInfoSuppliers,
                suppliersRegistry);
    }
}
