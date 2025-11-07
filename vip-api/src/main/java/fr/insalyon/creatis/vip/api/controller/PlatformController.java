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

import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;
import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;
import fr.insalyon.creatis.vip.api.model.PlatformProperties;
import fr.insalyon.creatis.vip.core.server.security.oidc.OidcLoginConfig;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.VipException.VipError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abonnet on 7/13/16.
 */
@RestController
@RequestMapping("/platform")
public class PlatformController extends ApiController{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OidcLoginConfig oidcLoginConfig;

    @Autowired
    public PlatformController(OidcLoginConfig oidcLoginConfig) {
        this.oidcLoginConfig = oidcLoginConfig;
    }

    @RequestMapping
    public PlatformProperties getPlatformProperties() throws ApiException {
        logMethodInvocation(logger, "getPlatformProperties");
        PlatformProperties platformProperties = new PlatformProperties();
        platformProperties.setPlatformName(server.getCarminPlatformName());
        platformProperties.setPlatformDescription(server.getCarminPlatformDescription());
        platformProperties.setSupportedTransferProtocols(Arrays.asList(server.getCarminSupportedTransferProtocols()));
        platformProperties.setSupportedModules(Arrays.asList(server.getCarminSupportedModules()));
        platformProperties.setDefaultLimitListExecutions(server.getCarminDefaultLimitListExecution());
        platformProperties.setUnsupportedMethods(Arrays.asList(server.getCarminUnsupportedMethods()));
        platformProperties.setSupportedAPIVersion(server.getCarminSupportedApiVersion());
        platformProperties.setEmail(server.getCarminPlatformEmail());
        platformProperties.setAPIErrorCodesAndMessages(getErrorCodesAndMessages());
        platformProperties.setMaxSizeDirectTransfer(server.getCarminApiDataTransfertMaxSize());
        platformProperties.setOidcLoginProviders(oidcLoginConfig.getLoginProviders());
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
                .orElse("The error message for '" + errorName + "' cannot be known in advance");
        return new ErrorCodeAndMessage(vipError.getCode(), message);
    }
}
