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

import fr.insalyon.creatis.vip.api.bean.Module;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.api.rest.RestApiBusiness;
import fr.insalyon.creatis.vip.api.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

import static fr.insalyon.creatis.vip.api.CarminProperties.*;

/**
 * Created by abonnet on 7/13/16.
 */
@RestController
@RequestMapping("/platform")
public class PlatformController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    // although the controller is a singleton, these are proxies that always point on the current request
    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping
    public PlatformProperties getPlatformProperties() throws ApiException {
        ApiUtils.methodInvocationLog("getPlatformProperties");
        PlatformProperties platformProperties = new PlatformProperties();
        platformProperties.setPlatformName(env.getProperty(PLATFORM_NAME));
        platformProperties.setPlatformDescription(env.getProperty(PLATFORM_DESCRIPTION));
        platformProperties.setSupportedTransferProtocols(Arrays.asList(
                env.getProperty(SUPPORTED_TRANSFER_PROTOCOLS, SupportedTransferProtocol[].class)
        ));
        platformProperties.setSupportedModules(Arrays.asList(
                env.getProperty(SUPPORTED_MODULES, Module[].class)
        ));
        platformProperties.setDefaultLimitListExecutions(
                env.getProperty(DEFAULT_LIMIT_LIST_EXECUTION, Long.class));
        platformProperties.setUnsupportedMethods(Arrays.asList(
                env.getProperty(UNSUPPORTED_METHODS, String[].class)
        ));
        platformProperties.setSupportedAPIVersion(env.getProperty(SUPPORTED_API_VERSION));
        platformProperties.setEmail(env.getProperty(PLATFORM_EMAIL));
        platformProperties.setAPIErrorCodesAndMessages(getErrorCodesAndMessages());
        platformProperties.setMaxSizeDirectTransfer(env.getProperty(API_DATA_TRANSFERT_MAX_SIZE, Long.class));
        return platformProperties;
    }

    private List<ErrorCodeAndMessage> getErrorCodesAndMessages() throws ApiException {
        List<ErrorCodeAndMessage> res = new ArrayList<>();
        String[] codesAndMessagesAsStrings = env.getProperty(
                PLATFORM_ERROR_CODES_AND_MESSAGES, String[].class);
        for (String codeAndMessageAsString : codesAndMessagesAsStrings) {
            String[] parts = codeAndMessageAsString.split(":");
            if (parts.length != 2) {
                logger.error("Malformed api code and message in properties: {}",
                        codeAndMessageAsString);
                throw new ApiException("Malformed api code and message in properties");
            }
            Integer code = Integer.parseInt(parts[0]);
            res.add(new ErrorCodeAndMessage(code, parts[1]));
        }
        return res;
    }

}
