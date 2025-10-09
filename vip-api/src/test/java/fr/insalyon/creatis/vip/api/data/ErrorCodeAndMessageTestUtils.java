package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.VipException.VipError;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ErrorCodeAndMessageTestUtils {

    public static final Map<String,Function> errorCodeAndMessageSuppliers;

    static {
        errorCodeAndMessageSuppliers = getErrorCodeAndMessageSuppliers();
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Function> getErrorCodeAndMessageSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList("errorCode", "errorMessage"),
                ErrorCodeAndMessage::getErrorCode,
                ErrorCodeAndMessage::getErrorMessage);
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToErrorCodeAndMessage(
            ErrorCodeAndMessage errorCodeAndMessage
    ) {
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return JsonCustomObjectMatcher.jsonCorrespondsTo(errorCodeAndMessage, errorCodeAndMessageSuppliers, suppliersRegistry);
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToErrorCodeAndMessage(
            Integer code, String message
    ) {
        ErrorCodeAndMessage errorCodeAndMessage = new ErrorCodeAndMessage(
                code, message);
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return JsonCustomObjectMatcher.jsonCorrespondsTo(errorCodeAndMessage, errorCodeAndMessageSuppliers, suppliersRegistry);
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToErrorCodeAndMessage(
            VipError vipError
    ) {
        ErrorCodeAndMessage errorCodeAndMessage = new ErrorCodeAndMessage(
                vipError.getCode(),
                VipException.getRawMessage(vipError).get()
        );
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return JsonCustomObjectMatcher.jsonCorrespondsTo(errorCodeAndMessage, errorCodeAndMessageSuppliers, suppliersRegistry);
    }
}
