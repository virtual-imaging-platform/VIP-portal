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
package fr.insalyon.creatis.vip.api.rest.controller;

import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.model.ErrorCodeAndMessage;
import fr.insalyon.creatis.vip.core.client.VipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

/**
 * Created by abonnet on 7/28/16.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ErrorCodeAndMessage handleApiException(ApiException e) {
        // No need to log, VIP errors are logged when they are created

        // to find the error message : look for an error code in the vip
        // ancestor exceptions and use that exception message
        return fetchErrorInException(e);
    }

    private ErrorCodeAndMessage fetchErrorInException(Throwable throwable) {

        // cast to vipException
        VipException vipException =  Optional.ofNullable(throwable)
                .filter(VipException.class::isInstance)
                .map(VipException.class::cast)
                // stop recursion if no exception or if not a VipException
                .orElse( new ApiException(ApiError.GENERIC_API_ERROR));

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
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Internal spring exception catched", ex);
        ErrorCodeAndMessage codeAndmessage =
                new ErrorCodeAndMessage(
                        status.value()*100,
                        ex.getMessage());
        return new ResponseEntity<Object>(codeAndmessage, headers, status);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorCodeAndMessage handleAllException(Exception e) {
        logger.error("Unexpected exception catched", e);
        return new ErrorCodeAndMessage(50000,
                "Internal Error");
    }
}
