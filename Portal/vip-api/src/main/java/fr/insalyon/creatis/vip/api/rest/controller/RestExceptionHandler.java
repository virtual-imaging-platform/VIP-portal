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
import fr.insalyon.creatis.vip.api.rest.RestErrorCodes;
import fr.insalyon.creatis.vip.api.rest.model.ErrorCodesAndMessage;
import org.apache.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by abonnet on 7/28/16.
 */
@ControllerAdvice
public class RestExceptionHandler {

    public static final Logger logger = Logger.getLogger(RestExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ErrorCodesAndMessage handleApiException(ApiException e) {
        logger.error(e);
        return new ErrorCodesAndMessage(RestErrorCodes.API_ERROR.getCode(),
                RestErrorCodes.API_ERROR.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public void methodNotSupportedHandler(Exception e)
            throws Exception {
        logger.info("HttpRequestMethodNotSupportedException", e);
            throw e;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorCodesAndMessage defaultErrorHandler(HttpServletRequest req, Exception e)
            throws Exception {
        logger.info("Unexpected exception", e);
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        return new ErrorCodesAndMessage(RestErrorCodes.UNEXPECTED_ERROR.getCode(),
                RestErrorCodes.UNEXPECTED_ERROR.getMessage());
    }

}
