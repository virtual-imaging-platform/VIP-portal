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
        NOT_COMPATIBLE_WITH_BOUTIQUES(8016),
        INPUT_FIELD_MISSING(8017);

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
            addMessage(ApiError.NOT_COMPATIBLE_WITH_BOUTIQUES, "The pipeline is not compatible with boutiques : {}", 1);
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
