package fr.insalyon.creatis.vip.core.server;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.RequestRejectedHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;

/**
 * New behavior for exception :
 * - if VipException : return code and message for it
 *      - if no code, take generic one and the exception message
 * - if not VipException : log fetch one is causes (if possible) and return code and message for it
 * - if still not VipException in causes, return generic code and root exception message
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler implements RequestRejectedHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper mapper;

    @Autowired
    public RestExceptionHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @ExceptionHandler(VipException.class)
    public ResponseEntity<Object> handleVipException(VipException vipException) {
        // No need to log, VIP errors are logged when they are created
        HttpStatus status = HttpStatus.resolve(vipException.getVipError().getHttpCode());
        return ResponseEntity.status(status).body(getErrorCodeAndMessage(vipException));
    }

    private ErrorCodeAndMessage getErrorCodeAndMessage(VipException vipException) {
        return new ErrorCodeAndMessage(
                vipException.getVipErrorCode(),
                cleanExceptionMessage(vipException)
        );
    }

    private Optional<VipException> fetchVipException(Throwable throwable) {
        if (throwable == null) {
            return Optional.empty();
        }
        Optional<VipException> vipException = Optional.of(throwable)
                .filter(VipException.class::isInstance)
                .map(VipException.class::cast);

        if (vipException.isEmpty()) {
            return fetchVipException(throwable.getCause());
        } else {
            return vipException;
        }
    }

    private String cleanExceptionMessage(VipException vipException) {
        // remove html newline made for vip portal
        return vipException.getMessage().replaceAll("<br */>", "");
    }


    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode status, @NonNull WebRequest request) {
        logger.error("Internal spring exception caught", ex);
        return new ResponseEntity<>(getErrorCodeAndMessage(new VipException(ex)), headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        // only handle field error
        if (ex.getBindingResult().getFieldError() != null) {
            // only take the first one
            FieldError fieldError = ex.getBindingResult().getFieldError();
            logger.error("Spring validation error catched", ex);

            return new ResponseEntity<>(getErrorCodeAndMessage(
                    new VipException(DefaultError.BAD_INPUT_FIELD, fieldError.getField(),
                            fieldError.getDefaultMessage())
            ), headers, status);
        }
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        logger.error("Spring/Jackson serialization error caught", ex);

        return new ResponseEntity<>(getErrorCodeAndMessage(
                new VipException(DefaultError.BAD_INPUT,ex.getMessage())
        ), headers, status);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorCodeAndMessage handleAllException(Exception e) {
        logger.error("Unexpected exception caught", e);
        return getErrorCodeAndMessage(
                fetchVipException(e).orElse(new VipException(e)));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, RequestRejectedException requestRejectedException) throws IOException, ServletException {
        logger.debug("Rejecting request due to: {}", requestRejectedException.getMessage(), requestRejectedException);
        ErrorCodeAndMessage errorCodeAndMessage = getErrorCodeAndMessage(new VipException(requestRejectedException));
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        mapper.writeValue(response.getWriter(), errorCodeAndMessage);
    }
}
