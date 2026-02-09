package fr.insalyon.creatis.vip.core.server;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(VipException.class)
    public ResponseEntity<Object> handleVipException(VipException e) {
        // No need to log, VIP errors are logged when they are created

        // to find the error message : look for an error code in the vip
        // ancestor exceptions and use that exception message
        ErrorCodeAndMessage codeAndMessage = fetchErrorInException(e);
        // we are now using specific return codes inside the response itself
        // like 8xxx codes
        HttpStatus status = HttpStatus.resolve(e.getVipError()
                .map(VipError::getHttpCode)
                .orElse(400));

        return new ResponseEntity<>(codeAndMessage, status);
    }

    private ErrorCodeAndMessage fetchErrorInException(Throwable throwable) {
        // cast to vipException
        VipException vipException =  Optional.ofNullable(throwable)
                .filter(VipException.class::isInstance)
                .map(VipException.class::cast)
                // stop recursion if no exception or if not a VipException
                .orElse(new VipException(DefaultError.GENERIC_ERROR));

        // return code and message if present otherwise call parent
        return vipException.getVipErrorCode()
                .map( errorCode -> new ErrorCodeAndMessage(
                        errorCode,
                        cleanExceptionMessage(vipException) )
                )
                .orElseGet( () ->
                        fetchErrorInException( vipException.getCause() )
                );
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
        logger.error("Internal spring exception catched", ex);
        return new ResponseEntity<>(
                fetchErrorInException(new VipException(DefaultError.GENERIC_ERROR)), headers, status);
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

            return new ResponseEntity<>(fetchErrorInException(
                    new VipException(DefaultError.BAD_INPUT_FIELD, fieldError.getField(),
                            fieldError.getDefaultMessage())
            ), headers, status);
        }
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorCodeAndMessage handleAllException(Exception e) {
        logger.error("Unexpected exception catched", e);
        return fetchErrorInException(e);
    }
}
