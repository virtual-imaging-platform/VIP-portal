package fr.insalyon.creatis.vip.core.client;

public enum DefaultError implements VipError {

    // we kept 8xxx codes since VipAPI was using it and we want it to factorize
    // without affecting return codes
    GENERIC_ERROR(8000, "An error has been encountered on the VIP API", 0),
    BAD_CREDENTIALS(8002, "Bad credentials", 0, 401),
    // mean that Spring do not consider the request enought authenticated (like a token missing or something else)
    INSUFFICIENT_AUTH(8003, "Insufficient authentication", 0),
    AUTHENTICATION_ERROR(8004, "Failed authentication", 0),
    BAD_INPUT_FIELD(8009, "Input field '{}' is not valid. Cause : {}", 2),

    // new errors can use the 1xxx appropriated error code (see VipError)
    NOT_FOUND(1000, "Item {} not found", 1),
    ACCESS_DENIED(1001, "You do not have the right to do that!", 0);

    private final String message;
    private final Integer code;
    private final Integer expectedParams;
    private final Integer httpCode;

    private DefaultError(Integer code, String message, Integer expectedParams, Integer httpCode) {
        this.message = message;
        this.code = code;
        this.expectedParams = expectedParams;
        this.httpCode = httpCode;
    }

    private DefaultError(Integer code, String message, Integer expectedParams) {
        this(code, message, expectedParams, 400);
    }

    @Override public Integer getCode() { return code; }
    @Override public String getMessage() { return message; }
    @Override public Integer getExpectedParameters() { return expectedParams; }
    @Override public Integer getHttpCode() { return httpCode; }
}
