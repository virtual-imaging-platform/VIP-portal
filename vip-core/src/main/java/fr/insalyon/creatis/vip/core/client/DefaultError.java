package fr.insalyon.creatis.vip.core.client;

public enum DefaultError implements VipError {

    // we kept 8xxx codes since VipAPI was using it and we want it to factorize
    // without affecting return codes
    GENERIC_ERROR(8000, "An error has been encountered on the VIP API", 0),
    BAD_CREDENTIALS(8002, "Bad credentials", 0),
    INSUFFICIENT_AUTH(8003, "Insufficient authentication", 0),
    AUTHENTICATION_ERROR(8004, "Failed authentication", 0),
    BAD_INPUT_FIELD(8009, "Input field '{}' is not valid. Cause : {}", 2),

    // new errors can use the 1xxx appropriated error code (see VipError)
    NOT_FOUND(1000, "Item {} not found", 1);

    private final String message;
    private final Integer code;
    private final Integer expectedParams;

    private DefaultError(Integer code, String message, Integer expectedParams) {
        this.message = message;
        this.code = code;
        this.expectedParams = expectedParams;
    }

    @Override public Integer getCode() { return code; }
    @Override public String getMessage() { return message; }
    @Override public Integer getExpectedParameters() { return expectedParams; }
}