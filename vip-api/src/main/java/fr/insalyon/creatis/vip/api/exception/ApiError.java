package fr.insalyon.creatis.vip.api.exception;

import fr.insalyon.creatis.vip.core.client.VipError;

public enum ApiError implements VipError {

    // some old codes have been moved to vip-core/DefaultError
    // due to refactoring. We wanted to mutualize some error codes
    // between public and new internal api.

    NOT_IMPLEMENTED(8001, "The {} method is not implemented in the VIP API", 1),
    INVALID_PIPELINE_IDENTIFIER(8005, "The {} pipeline identifier is not valid", 1),
    APPLICATION_NOT_FOUND(8006, "The {} application does not exists or cannot be used", 1),
    PIPELINE_NOT_FOUND(8007, "The {} pipeline does not exists or cannot be used", 1),
    NOT_ALLOWED_TO_USE_PIPELINE(8008, "Not allowed to access pipeline {}", 1),
    WRONG_DATE_FORMAT(8010, "The date {} have a wrong format (needed : {})", 1),
    WRONG_STAT_SERVICE(8011, "The service {} is unknown, only 'vip' is possible", 1),
    COUNTRY_UNKNOWN(8012, "Country unknown : {}", 1),
    UNAUTHORIZED_DATA_ACCESS(8013, "Unauthorized data access to : {}", 1),
    WRONG_OIDC_LOGIN(8014, "The login process encountered an error", 0),
    INVALID_EXECUTION_ID(8015, "No execution available with this id : {}", 1),
    INVALID_EXAMPLE_ID(8016, "There is no example with the id : {}", 1),
    NOT_COMPATIBLE_WITH_BOUTIQUES(8017, "The pipeline is not compatible with boutiques : {}", 1),
    INPUT_FIELD_MISSING(8018, "Input field '{}' is missing", 1),
    INVALID_EXECUTION_NAME(8019, "The execution name is not valid : Cause {}", 1),
    INVALID_EXECUTION_INIT(8020, "The execution could not be submitted : Cause {}", 1);

    private final String message;
    private final Integer code;
    private final Integer expectedParams;
    private final Integer httpCode;

    private ApiError(Integer code, String message, Integer expectedParams) {
        this.message = message;
        this.code = code;
        this.expectedParams = expectedParams;
        this.httpCode = 400;
    }

    @Override public Integer getCode() { return code; }
    @Override public String getMessage() { return message; }
    @Override public Integer getExpectedParameters() { return expectedParams; }
    @Override public Integer getHttpCode() { return httpCode; }
}
