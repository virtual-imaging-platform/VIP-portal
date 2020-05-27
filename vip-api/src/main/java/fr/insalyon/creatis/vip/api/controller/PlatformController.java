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
package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.model.Module;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.ErrorCodeAndMessage;
import fr.insalyon.creatis.vip.api.model.PlatformProperties;
import fr.insalyon.creatis.vip.api.model.SupportedTransferProtocol;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.VipException.VipError;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Supplier;

import static fr.insalyon.creatis.vip.api.CarminProperties.*;

/**
 * Created by abonnet on 7/13/16.
 */
@RestController
@RequestMapping("/platform")
public class PlatformController extends ApiController{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment env;

    @Autowired
    public PlatformController(Supplier<User> currentUserSupplier, Environment env) {
        super(currentUserSupplier);
        this.env = env;
    }

    @RequestMapping
    public PlatformProperties getPlatformProperties() throws ApiException {
        logMethodInvocation(logger, "getPlatformProperties");
        PlatformProperties platformProperties = new PlatformProperties();
        platformProperties.setPlatformName(env.getProperty(PLATFORM_NAME));
        platformProperties.setPlatformDescription(env.getProperty(PLATFORM_DESCRIPTION));
        platformProperties.setSupportedTransferProtocols(Arrays.asList(
                env.getRequiredProperty(SUPPORTED_TRANSFER_PROTOCOLS, SupportedTransferProtocol[].class)
        ));
        platformProperties.setSupportedModules(Arrays.asList(
                env.getRequiredProperty(SUPPORTED_MODULES, Module[].class)
        ));
        platformProperties.setDefaultLimitListExecutions(
                env.getProperty(DEFAULT_LIMIT_LIST_EXECUTION, Long.class));
        platformProperties.setUnsupportedMethods(Arrays.asList(
                env.getRequiredProperty(UNSUPPORTED_METHODS, String[].class)
        ));
        platformProperties.setSupportedAPIVersion(env.getProperty(SUPPORTED_API_VERSION));
        platformProperties.setEmail(env.getProperty(PLATFORM_EMAIL));
        platformProperties.setAPIErrorCodesAndMessages(getErrorCodesAndMessages());
        platformProperties.setMaxSizeDirectTransfer(env.getProperty(API_DATA_TRANSFERT_MAX_SIZE, Long.class));
        return platformProperties;
    }

    private List<ErrorCodeAndMessage> getErrorCodesAndMessages() {
        List<ErrorCodeAndMessage> res = new ArrayList<>();
        res.addAll(getErrorCodesAndMessages(ApiError.class));
        res.addAll(getErrorCodesAndMessages(ApplicationError.class));
        return res;
    }

    private List<ErrorCodeAndMessage> getErrorCodesAndMessages(Class<? extends VipError> errorEnumClass) {
        List<ErrorCodeAndMessage> res = new ArrayList<>();
        VipError[] enumConstants = errorEnumClass.getEnumConstants();
        if (enumConstants == null) {
            return res;
        }
        for (VipError vipError : enumConstants) {
            String enumName = ((Enum) vipError).name();
            res.add(getErrorCodeAndMessage(vipError, enumName));
        }
        return res;
    }

    private ErrorCodeAndMessage getErrorCodeAndMessage(VipError vipError, String errorName) {
        errorName = errorName.replace('_', ' ').toLowerCase();
        String message = VipException.getRawMessage(vipError)
                .orElse("The error message for '"
                        + errorName + "' cannot be known in advance");
        return new ErrorCodeAndMessage(
                vipError.getCode(),
                message
        );
    }
}
