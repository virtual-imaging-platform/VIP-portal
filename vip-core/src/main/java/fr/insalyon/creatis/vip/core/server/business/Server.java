package fr.insalyon.creatis.vip.core.server.business;

import java.util.List;

import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;

/*
    interface for the server configuration to allow several implementations
 */
public interface Server {

    String VIP_DIR = "/.vip/";
    String CONF_FILE = "vip.conf";
    String PROXIES_DIR = "proxies/";
    String PROXY_FILENAME = "x509up_server";

    String getConfigurationFolder();

    String getVoName();

    String getVoRoot();

    String getServerProxy();

    String getServerProxy(String vo);

    String getServerProxyFolder(String vo);

    String getMyProxyHost();

    int getMyProxyPort();

    String getMyProxyPass();

    String getMyProxyUser();

    String getMyProxyLifeTime();

    int getMyProxyMinHours();

	boolean getMyProxyEnabled();

    String getGRIDAHost();

    int getGRIDAPort();

    String getWorkflowsHost();

    String getWorkflowsPath();

    String getApacheHost();

    int getApacheSSLPort();

    boolean useSMA();

    String getSMAHost();

    int getSMAPort();

    String getDataManagerPath();

    String getDataManagerUsersHome();

    String getDataManagerGroupsHome();

    String getAltDataManagerUsersHome();

    String getAltDataManagerGroupsHome();

    String getTruststoreFile();

    String getTruststorePass();

    String getAdminEmail();

    String getAdminFirstName();

    String getAdminInstitution();

    String getAdminLastName();

    String getAdminPassword();

    String getCasURL();

    String getSamlTrustedCertificate(String issuer);

    String getSAMLDefaultGroup(String issuer);

    List<String> getUndesiredMailDomains();

    List<String> getUndesiredCountries();

    int getMaxPlatformRunningSimulations();

    int getNumberMonthsToTestLastPublicationUpdates();

    String getPublicationCommandLine();

    float getGirderTokenDurationInDays();

    boolean useLocalFilesInInputs();

    int getApiParallelDownloadNb();

    String getReproVIPRootDir();

    String getMoteurServerPassword();

    String getHostURL();

    String getApikeyHeaderName();

    boolean getKeycloakActivated();

    String[] getCarminCorsAuthorizedDomains();

    String getCarminPlatformName();

    String getCarminPlatformDescription();

    String getCarminPlatformEmail();

    long getCarminDefaultLimitListExecution();

    String getCarminSupportedApiVersion();

    boolean getCarminApikeyGenerateNewEachTime();

    String getCarminApiDirectoryMimeType();

    String getCarminApiDefaultMimeType();

    int getCarminApiDownloadRetryInSeconds();

    int getCarminApiDownloadTimeoutInSeconds();

    long getCarminApiDataTransfertMaxSize();

    SupportedTransferProtocol[] getCarminSupportedTransferProtocols();

    Module[] getCarminSupportedModules();

    String[] getCarminUnsupportedMethods();

    String[] getCarminApiPipelineWhiteList();

    class OidcLoginProviderConfig {
        public String clientId;
        public String clientSecret;
        public String redirectUri;
        public String authorizationUri;
        public String tokenUri;
        public String userInfoUri;
        public String jwkSetUri;
    }
    OidcLoginProviderConfig getOidcConfigEGI();
    OidcLoginProviderConfig getOidcConfigLSLOGIN();

    boolean isDevMode();
}
