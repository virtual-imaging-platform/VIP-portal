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
import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.api.exception.SQLRuntimeException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.UserApiKey;
import fr.insalyon.creatis.vip.datamanager.server.business.ApiKeyBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.Valid;

@RestController
@RequestMapping("/user/externalKeys")
public class ApiKeyController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApiKeyBusiness apiKeyBusiness;
    private ExternalPlatformBusiness externalPlatformBusiness;
    private Supplier<Connection> connectionSupplier;

    @Autowired
    public ApiKeyController(
        ApiKeyBusiness apiKeyBusiness,
        ExternalPlatformBusiness externalPlatformBusiness,
        Supplier<Connection> connectionSupplier) {
        this.apiKeyBusiness = apiKeyBusiness;
        this.externalPlatformBusiness = externalPlatformBusiness;
        this.connectionSupplier = connectionSupplier;
    }

    @GetMapping
    public List<UserApiKey> listUserApiKeys() throws ApiException {
        ApiUtils.methodInvocationLog("listUserApiKeys");
        String userEmail = getCurrentUserEmail();
        try(Connection connection = connectionSupplier.get()) {
            return apiKeyBusiness.apiKeysFor(userEmail, connection);
        } catch (BusinessException | SQLException | SQLRuntimeException e) {
            throw new ApiException(e);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addOrUpdateApiKey(@RequestBody @Valid KeyInfo keyInfo)
        throws ApiException {
        ApiUtils.methodInvocationLog("addOrUpdateApiKey");
        String userEmail = getCurrentUserEmail();
        try(Connection connection = connectionSupplier.get()) {
            if (!externalPlatformBusiness.listAll(connection).stream()
                .anyMatch(ep -> ep.getIdentifier()
                          .equals(keyInfo.storageIdentifier))) {
                logger.error("Storage does not exist: {}", keyInfo.storageIdentifier);
                throw new ApiException(
                    "Storage does not exist: " + keyInfo.storageIdentifier);
            }

            apiKeyBusiness.addOrUpdateApiKey(
                keyInfo.storageIdentifier,
                userEmail,
                keyInfo.apiKey,
                connection);
        } catch (BusinessException | SQLException | SQLRuntimeException e) {
            throw new ApiException(e);
        }
    }

    @RequestMapping(value = "/{storageIdentifier}",
                    method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApiKey(@PathVariable String storageIdentifier)
        throws ApiException {
        ApiUtils.methodInvocationLog("deleteApiKey");
        String userEmail = getCurrentUserEmail();
        try(Connection connection = connectionSupplier.get()) {
            apiKeyBusiness.deleteApiKey(
                storageIdentifier, userEmail, connection);
        } catch (BusinessException | SQLException | SQLRuntimeException e) {
            throw new ApiException(e);
        }
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private static class KeyInfo {
        private String storageIdentifier;
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
