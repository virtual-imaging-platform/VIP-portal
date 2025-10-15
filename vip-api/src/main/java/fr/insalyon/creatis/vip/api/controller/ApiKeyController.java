package fr.insalyon.creatis.vip.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.datamanager.client.bean.UserApiKey;
import fr.insalyon.creatis.vip.datamanager.server.business.ApiKeyBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/externalKeys")
public class ApiKeyController extends ApiController{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiKeyBusiness apiKeyBusiness;
    private final ExternalPlatformBusiness externalPlatformBusiness;

    @Autowired
    public ApiKeyController(
            ApiKeyBusiness apiKeyBusiness,
            ExternalPlatformBusiness externalPlatformBusiness) {
        this.apiKeyBusiness = apiKeyBusiness;
        this.externalPlatformBusiness = externalPlatformBusiness;
    }

    @GetMapping
    public List<UserApiKey> listUserApiKeys() throws VipException {
        logMethodInvocation(logger, "listUserApiKeys");
        return apiKeyBusiness.apiKeysFor(currentUser().getEmail());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addOrUpdateApiKey(@RequestBody @Valid KeyInfo keyInfo)
            throws VipException {
        logMethodInvocation(logger, "addOrUpdateApiKey");
        if (externalPlatformBusiness.listAll().stream()
                .noneMatch(ep -> ep.getIdentifier()
                        .equals(keyInfo.storageIdentifier))) {
            logger.error("Storage does not exist: {}", keyInfo.storageIdentifier);
            throw new VipException(
                    "Storage does not exist: " + keyInfo.storageIdentifier);
        }

        apiKeyBusiness.addOrUpdateApiKey(
                keyInfo.storageIdentifier,
                currentUser().getEmail(),
                keyInfo.apiKey);
    }

    @RequestMapping(value = "/{storageIdentifier}",
                    method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApiKey(@PathVariable String storageIdentifier)
            throws VipException {
        logMethodInvocation(logger, "deleteApiKey");
        apiKeyBusiness.deleteApiKey(
                storageIdentifier, currentUser().getEmail());
    }

    public static class KeyInfo {
        @NotNull
        private String storageIdentifier;
        @NotNull
        private String apiKey;

        public String getStorageIdentifier() {
            return storageIdentifier;
        }

        public void setStorageIdentifier(String storageIdentifier) {
            this.storageIdentifier = storageIdentifier;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
}
