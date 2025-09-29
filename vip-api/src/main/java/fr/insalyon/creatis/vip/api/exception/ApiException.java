package fr.insalyon.creatis.vip.api.exception;

import fr.insalyon.creatis.vip.core.client.VipException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 *
 * @author Tristan Glatard
 */
public class ApiException extends VipException {

    /* Reserved codes : 8xxx : vip-api */
    public enum ApiError implements VipError {
        GENERIC_API_ERROR(8000),
        NOT_IMPLEMENTED(8001),
        BAD_CREDENTIALS(8002, HttpStatus.UNAUTHORIZED.value()),
        INSUFFICIENT_AUTH(8003),
        AUTHENTICATION_ERROR(8004),
        INVALID_PIPELINE_IDENTIFIER(8005),
        APPLICATION_NOT_FOUND(8006),
        PIPELINE_NOT_FOUND(8007),
        NOT_ALLOWED_TO_USE_PIPELINE(8008),
        INPUT_FIELD_NOT_VALID(8009),
        WRONG_DATE_FORMAT(8010),
        WRONG_STAT_SERVICE(8011),
        COUNTRY_UNKNOWN(8012),
        UNAUTHORIZED_DATA_ACCESS(8013),
        WRONG_OIDC_LOGIN(8014),
        INVALID_EXECUTION_ID(8015),
        INVALID_EXAMPLE_ID(8016),
        NOT_COMPATIBLE_WITH_BOUTIQUES(8017),
        INPUT_FIELD_MISSING(8018),
        INVALID_EXECUTION_NAME(8019),
        INVALID_EXECUTION_INIT(8020);

        private final Integer code;
        private final Integer httpStatus;

        ApiError(Integer code) {
            this(code, null);
        }

        ApiError(Integer code, Integer httpStatus) {
            this.code = code;
            this.httpStatus = httpStatus;
        }

        @Override
        public Integer getCode() { return code; }

        public Integer getHttpStatus() {return httpStatus; }

        static private final String GENERIC_ERROR_MESSAGE = "An error has been encountered on the VIP API";

        static {
            addMessage(ApiError.GENERIC_API_ERROR, GENERIC_ERROR_MESSAGE, 0);
            addMessage(ApiError.NOT_IMPLEMENTED, "The {} method is not implemented in the VIP API", 1);
            addMessage(ApiError.BAD_CREDENTIALS, "Bad credentials", 0);
            addMessage(ApiError.INVALID_PIPELINE_IDENTIFIER, "The {} pipeline identifier is not valid", 1);
            addMessage(ApiError.PIPELINE_NOT_FOUND, "The {} pipeline does not exists or cannot be used", 1);
            addMessage(ApiError.NOT_ALLOWED_TO_USE_PIPELINE, "Not allowed to access pipeline {}", 1);
            addMessage(ApiError.INPUT_FIELD_NOT_VALID, "Input field '{}' is not valid. Cause : {}", 2);
            addMessage(ApiError.INPUT_FIELD_MISSING, "Input field '{}' is missing", 1);
            addMessage(ApiError.WRONG_DATE_FORMAT, "The date {} have a wrong format (needed : {})", 2);
            addMessage(ApiError.WRONG_STAT_SERVICE, "The service {} is unknown, only 'vip' is possible", 1);
            addMessage(ApiError.COUNTRY_UNKNOWN, "Country unknown : {}", 1);
            addMessage(ApiError.UNAUTHORIZED_DATA_ACCESS, "Unauthorized data access to : {}", 1);
            addMessage(ApiError.WRONG_OIDC_LOGIN, "The login process encountered an error", 0);
            addMessage(ApiError.INVALID_EXECUTION_ID, "No execution available with this id : {}", 1);
            addMessage(ApiError.INVALID_EXAMPLE_ID, "There is no example with the id : {}", 1);
            addMessage(ApiError.NOT_COMPATIBLE_WITH_BOUTIQUES, "The pipeline is not compatible with boutiques : {}", 1);
            addMessage(ApiError.INVALID_EXECUTION_NAME, "The execution name is not valid : Cause {}", 1);
            addMessage(ApiError.INVALID_EXECUTION_INIT, "The execution could not be submitted : Cause {}", 1);
        }

        public Optional<String> getMessage() {
            return getRawMessage(this);
        }

        public static String getGenericErrorMessage() {
            return GENERIC_ERROR_MESSAGE;
        }

        public String formatMessage(Object ...params) {
            return VipException.formatMessage(this, params);
        }
    }

    public Optional<Integer> getHttpStatus() {
        return getVipError()
                .filter(ApiError.class::isInstance)
                .map(ApiError.class::cast)
                .map(ApiError::getHttpStatus);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(ApiError apiError, Object... params) {
        super(apiError, params);
    }

    public ApiException(ApiError apiError, Throwable cause, Object... params) {
        super(apiError, cause, params);
    }
}
