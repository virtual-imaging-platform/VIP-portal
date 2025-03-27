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
package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by abonnet on 7/19/16.
 */
public class PlatformProperties {

    @NotNull
    private String platformName;
    @JsonProperty("APIErrorCodesAndMessages")
    private List<ErrorCodeAndMessage> APIErrorCodesAndMessages;
    @NotNull
    private List<SupportedTransferProtocol> supportedTransferProtocols;
    @NotNull
    private List<Module> supportedModules;
    @NotNull
    private Long defaultLimitListExecutions;
    private String email;
    private String platformDescription;
    private Long minAuthorizedExecutionTimeout;
    private Long maxAuthorizedExecutionTimeout;
    @NotNull
    private List<String> unsupportedMethods;
    private Long maxSizeDirectTransfer;
    private String defaultStudy;
    @NotNull
    private String supportedAPIVersion;

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public List<ErrorCodeAndMessage> getAPIErrorCodesAndMessages() {
        return APIErrorCodesAndMessages;
    }

    public void setAPIErrorCodesAndMessages(List<ErrorCodeAndMessage> APIErrorCodesAndMessages) {
        this.APIErrorCodesAndMessages = APIErrorCodesAndMessages;
    }

    public List<SupportedTransferProtocol> getSupportedTransferProtocols() {
        return supportedTransferProtocols;
    }

    public void setSupportedTransferProtocols(List<SupportedTransferProtocol> supportedTransferProtocols) {
        this.supportedTransferProtocols = supportedTransferProtocols;
    }

    public List<Module> getSupportedModules() {
        return supportedModules;
    }

    public void setSupportedModules(List<Module> supportedModules) {
        this.supportedModules = supportedModules;
    }

    public Long getDefaultLimitListExecutions() {
        return defaultLimitListExecutions;
    }

    public void setDefaultLimitListExecutions(Long defaultLimitListExecutions) {
        this.defaultLimitListExecutions = defaultLimitListExecutions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlatformDescription() {
        return platformDescription;
    }

    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    public Long getMinAuthorizedExecutionTimeout() {
        return minAuthorizedExecutionTimeout;
    }

    public void setMinAuthorizedExecutionTimeout(Long minAuthorizedExecutionTimeout) {
        this.minAuthorizedExecutionTimeout = minAuthorizedExecutionTimeout;
    }

    public Long getMaxAuthorizedExecutionTimeout() {
        return maxAuthorizedExecutionTimeout;
    }

    public void setMaxAuthorizedExecutionTimeout(Long maxAuthorizedExecutionTimeout) {
        this.maxAuthorizedExecutionTimeout = maxAuthorizedExecutionTimeout;
    }

    public List<String> getUnsupportedMethods() {
        return unsupportedMethods;
    }

    public void setUnsupportedMethods(List<String> unsupportedMethods) {
        this.unsupportedMethods = unsupportedMethods;
    }

    public Long getMaxSizeDirectTransfer() {
        return maxSizeDirectTransfer;
    }

    public void setMaxSizeDirectTransfer(Long maxSizeDirectTransfer) {
        this.maxSizeDirectTransfer = maxSizeDirectTransfer;
    }

    public String getDefaultStudy() {
        return defaultStudy;
    }

    public void setDefaultStudy(String defaultStudy) {
        this.defaultStudy = defaultStudy;
    }

    public String getSupportedAPIVersion() {
        return supportedAPIVersion;
    }

    public void setSupportedAPIVersion(String supportedAPIVersion) {
        this.supportedAPIVersion = supportedAPIVersion;
    }
}
