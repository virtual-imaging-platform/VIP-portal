package fr.insalyon.creatis.vip.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.model.PlatformProperties;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.VipException.VipError;
import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;
import fr.insalyon.creatis.vip.core.server.security.oidc.OidcLoginConfig;

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
