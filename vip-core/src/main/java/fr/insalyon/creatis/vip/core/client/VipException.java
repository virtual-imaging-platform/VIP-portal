package fr.insalyon.creatis.vip.core.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VipException extends Exception implements IsSerializable {

    /*
        to override to return internal error code
        reserved codes for vip-modules:
        - 1xxx : vip-core
        - 2xxx : vip-application
        - 3xxx : vip-application-importer
        - 4xxx : vip-data-management
        - 5xxx : vip-gatelab
        - 6xxx : vip-social
        - 7xxx : vip-visualization
        - 8xxx : vip-api
     */

    /* to extends as an enum */
    public interface VipError {
        Integer getCode();
    }

    private static class VipErrorMessage {
        String messageFormat;
        Integer paramsNb;

        private VipErrorMessage(String messageFormat, Integer paramsNb) {
            this.messageFormat = messageFormat;
            this.paramsNb = paramsNb;
        }
    }

    private static final Map<VipError, VipErrorMessage> apiErrors = new HashMap<>();

    protected static void addMessage(VipError vipError, String messageFormat, Integer paramsNb) {
        apiErrors.put(
                vipError,
                new VipErrorMessage(messageFormat, paramsNb)
        );
    }

    public static Optional<String> getRawMessage(VipError vipError) {
        return Optional.ofNullable(apiErrors.get(vipError)).map(
                vipErrorMessage -> vipErrorMessage.messageFormat
        );
    }

    protected static String formatMessage(
            VipError vipError,
            Object ...params )
    {
        Optional<VipErrorMessage> vipErrorMessage =
                Optional.ofNullable( apiErrors.get(vipError) );

        String message;
        if ( ! vipErrorMessage.isPresent()) {
            // cannot use slf4j as we are in a gwt client class
            System.err.println(format("Wrong use of vip exception with code {}, No message " +
                            "configured. Ignoring and using a general message",
                    vipError.getCode()));
            message = "VIP Internal error";
        } else if (params.length != vipErrorMessage.get().paramsNb) {
            System.err.println(format("Wrong parameters list for a vip exception message." +
                    "expected {}, got {}. Exception code : {}." +
                    " Ignoring and using non-formatted message",
                    vipErrorMessage.get().paramsNb, params.length,
                    vipError.getCode()));
            message = vipErrorMessage.get().messageFormat;
        } else {
            message = vipErrorMessage
                    .map( vem -> format(vem.messageFormat, params) )
                    .get();
        }
        return message + " (Error code " + vipError.getCode() + ")";
    }

    /*
     need to re-implement a String.format like method as GWT does not support it
     */
    private static String format(String format, Object... args) {
        String[] split = format.split("\\{\\}", -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length - 1; i += 1) {
            sb.append(split[i]);
            sb.append(args[i].toString());
        }
        sb.append(split[split.length - 1]);
        return sb.toString();
    }

    private VipError vipError = null;

    protected Optional<VipError> getVipError() {
        return Optional.ofNullable(vipError);
    }
    public Optional<Integer> getVipErrorCode() {
        return getVipError().map(VipError::getCode);
    }

    // Allow all exception constructors to be used
    public VipException() {
    }

    public VipException(String message) {
        super(message);
    }

    public VipException(String message, Throwable cause) {
        super(message, cause);
    }

    public VipException(Throwable cause) {
        super(cause);
    }

    // VIP error constructors
    public VipException(VipError vipError, Object ...params) {
        super(formatMessage(vipError, params));
        this.vipError = vipError;
    }

    public VipException(VipError vipError, Throwable cause, Object ...params) {
        super(formatMessage(vipError, params), cause);
        this.vipError = vipError;
    }

    public VipException(String message, VipError vipError) {
        super(message);
        this.vipError = vipError;
    }

}
