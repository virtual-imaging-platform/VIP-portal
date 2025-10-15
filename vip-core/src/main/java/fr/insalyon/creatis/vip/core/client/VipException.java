package fr.insalyon.creatis.vip.core.client;

import java.util.Optional;

import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPCheckRemoval;

public class VipException extends Exception implements IsSerializable {

    private final VipError error;

    @VIPCheckRemoval
    public VipException() {
        this.error = null;
    }

    public VipException(String message) {
        super(message);
        this.error = null;
    }

    public VipException(String message, Throwable cause) {
        super(message, cause);
        this.error = null;
    }

    public VipException(Throwable cause) {
        super(cause);
        this.error = null;
    }

    public VipException(VipError error, Object ...params) {
        super(format(error, params));
        this.error = error;
    }

    public VipException(VipError error, Throwable cause, Object ...params) {
        super(format(error, params), cause);
        this.error = error;
    }

    public VipException(String message, VipError error) {
        super(message);
        this.error = error;
    }

    public Optional<VipError> getVipError() {
        return Optional.ofNullable(error);
    }

    public Optional<Integer> getVipErrorCode() {
        return getVipError().map(VipError::getCode);
    }

    // two methods below can be simplified after GWT migration
    @VIPCheckRemoval
    private static String format(VipError error, Object... params) {
        if (params.length != error.getExpectedParameters()) {
            System.err.println(formatGWT("Wrong parameters list for a vip exception message." +
                    "expected {}, got {}. Exception code : {}." +
                    " Ignoring and using non-formatted message",
                    error.getExpectedParameters(), params.length,
                    error.getCode()));
            return error.getMessage() + " (Error code " + error.getCode() + ")";
        } else {
            return formatGWT(error.getMessage(), params)
                    + " (Error code " + error.getCode() + ")";
        }
    }

    // GWT does not support String.format()
    @VIPCheckRemoval
    private static String formatGWT(String data, Object... args) {
        String[] split = data.split("\\{\\}", -1);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < split.length - 1; i += 1) {
            sb.append(split[i]);
            sb.append(args[i].toString());
        }
        sb.append(split[split.length - 1]);
        return sb.toString();
    }
}
