package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;
import jakarta.validation.constraints.NotNull;
import java.util.List;

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
    @NotNull
    private List<String> oidcLoginProviders;

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

    public List<String> getOidcLoginProviders() {
        return oidcLoginProviders;
    }

    public void setOidcLoginProviders(List<String> oidcLoginProviders) {
        this.oidcLoginProviders = oidcLoginProviders;
    }
}
