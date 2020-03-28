/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.ParameterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tristan Glatard
 */
public class ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);
    
    public static void methodInvocationLog(String methodName, Object... parameters) {
        String message = "Calling API method " + methodName + "(";
        boolean first = true;
        for (Object o : parameters) {
            if (!first) {
                message += ", ";
            }
            first = false;
            if (o == null) {
                message += "null";
            } else {
                message += "\""+o.toString()+"\"";
            }
        }
        message += ")";
        logger.info(message);
    }

    public static void methodInvocationLog(String methodName, String email, Object... parameters) {
        String message = "("+ email +") Calling API method " + methodName + "(";
        boolean first = true;
        for (Object o : parameters) {
            if (!first) {
                message += ", ";
            }
            first = false;
            if (o == null) {
                message += "null";
            } else {
                message += "\""+o.toString()+"\"";
            }
        }
        message += ")";
        logger.info(message);
    }

    public static void throwIfNull(Object parameter, String name) throws ApiException {
        if (parameter == null) {
            logger.error("parameter {} cannot be empty", name);
            throw new ApiException(name + " cannot be empty.");
        }
    }

    public static String getMessage(ApiContext apiContext) {
        String message;
        if (apiContext.getWarnings().isEmpty()) {
            message = "OK";
        } else {
            message = "Warning: ";
            for (String warning : apiContext.getWarnings()) {
                message += warning + " ";
            }
        }
        return message;
    }

    public static String getPipelineIdentifier(String applicationName, String applicationVersion) {
        return applicationName + "/" + applicationVersion;
    }
    
    public static String getApplicationVersion(String pipelineIdentifier) throws ApiException {
        checkIfValidPipelineIdentifier(pipelineIdentifier);
        return pipelineIdentifier.substring(pipelineIdentifier.lastIndexOf("/") + 1);
    }

    public static String getApplicationName(String pipelineIdentifier) throws ApiException {
        checkIfValidPipelineIdentifier(pipelineIdentifier);
        return pipelineIdentifier.substring(0, pipelineIdentifier.lastIndexOf("/"));
    }

    public static ParameterType getCarminType(String vipType) {
        return vipType.equals("URI") ? ParameterType.File : ParameterType.String;
    }

    public static void checkIfValidPipelineIdentifier(String identifier) throws ApiException {
        if (!identifier.contains("/")) {
            logger.error("Invalid pipeline identifier {} : missing /", identifier);
            throw new ApiException("Invalid pipeline identifier: " + identifier);
        }
    }
}
