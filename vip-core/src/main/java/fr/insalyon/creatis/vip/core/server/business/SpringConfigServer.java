package fr.insalyon.creatis.vip.core.server.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;
import jakarta.annotation.PostConstruct;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.CarminProperties;

/**
 * Reads the vip.conf property file from the configured vifConfigFolder
 * (see {@link fr.insalyon.creatis.vip.core.server.SpringCoreConfig})
 * It is readonly.
 * This is based on apache PropertiesConfiguration to allow for automatic
 * reloading.
 * A default vip.conf file is available in vip-portal resources
 */
@Component
@Profile({"default", "prod", "config-file"})
public class SpringConfigServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(SamlTokenValidator.class);

    /**
     * using apache config to have a reloadable config file
     * from https://www.baeldung.com/spring-reloading-properties
     */
    public static class ReloadablePropertySource extends PropertySource {

        private PropertiesConfiguration propertiesConfiguration;

        public ReloadablePropertySource(String name, File configFile)
                throws ConfigurationException {
            super(name);
            this.propertiesConfiguration = new PropertiesConfiguration(configFile);
            this.propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
        }

        @Override
        public Object getProperty(String s) {
            return propertiesConfiguration.getProperty(s);
        }
    }

    private Environment env;
    private File vipConfigFolder;
    private File proxyFolder;

    @Autowired
    public SpringConfigServer(
            Resource vipConfigFolder,
            ConfigurableEnvironment env) throws IOException, ConfigurationException {
        File configFile = vipConfigFolder.getFile().toPath().resolve(Server.CONF_FILE).toFile();

        if( ! configFile.exists()) {
            throw new FileNotFoundException(configFile.toString());
        }

        ReloadablePropertySource vipConf = new ReloadablePropertySource("vipMainConfigFile", configFile);
        env.getPropertySources().addLast(vipConf);
        this.env = env;
        this.vipConfigFolder = vipConfigFolder.getFile();
        this.proxyFolder = vipConfigFolder.getFile().toPath().resolve(PROXIES_DIR).toFile();
        createFolderIfNeeded(proxyFolder);
    }

    private void createFolderIfNeeded(File folder) {
        if ( ! folder.exists() &&
                ! folder.mkdir()) {
            logger.error("Cannot create VIP config folder : {}", folder);
            throw new BeanInitializationException("Cannot create VIP config folder");
        }
    }

    @PostConstruct
    public void verify() {
        assertPropertyIsPresent(CoreConstants.LAB_ADMIN_FIRST_NAME);
        assertPropertyIsPresent(CoreConstants.LAB_ADMIN_LAST_NAME);
        assertPropertyIsNotEmpty(CoreConstants.LAB_ADMIN_EMAIL);
        assertPropertyIsPresent(CoreConstants.LAB_ADMIN_INSTITUTION);
        assertPropertyIsPresent(CoreConstants.LAB_ADMIN_PASS);

        assertPropertyIsNotEmpty(CoreConstants.VO_NAME);
        assertPropertyIsNotEmpty(CoreConstants.VO_ROOT);

        assertOptionalPropertyType(CoreConstants.LAB_MYPROXY_ENABLED, Boolean.class);
        if (getMyProxyEnabled()) {
            assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_HOST);
            assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_PORT);
            assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_USER);
            assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_PASS);
            assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_LIFETIME);
            assertPropertyIsNotEmpty(CoreConstants.LAB_MYPROXY_MIN_HOURS, Integer.class);
        }

        assertPropertyIsNotEmpty(CoreConstants.LAB_SMA_HOST);
        assertPropertyIsNotEmpty(CoreConstants.LAB_SMA_PORT, Integer.class);

        assertPropertyIsNotEmpty(CoreConstants.LAB_GRIDA_HOST);
        assertPropertyIsNotEmpty(CoreConstants.LAB_GRIDA_PORT, Integer.class);

        assertPropertyIsNotEmpty(CoreConstants.LAB_DATA_PATH);
        assertPropertyIsNotEmpty(CoreConstants.LAB_DATA_USERS_HOME);
        assertPropertyIsNotEmpty(CoreConstants.LAB_DATA_GROUPS_HOME);
        assertPropertyIsPresent(CoreConstants.LAB_DATA_ALT_USERS_HOME);
        assertPropertyIsPresent(CoreConstants.LAB_DATA_ALT_GROUPS_HOME);

        assertPropertyIsNotEmpty(CoreConstants.LAB_TRUSTSTORE_FILE);
        assertPropertyIsNotEmpty(CoreConstants.LAB_TRUSTSTORE_PASS);

        assertPropertyIsNotEmpty(CoreConstants.LAB_SIMULATION_FOLDER);
        assertPropertyIsNotEmpty(CoreConstants.LAB_SIMULATION_DB_HOST);

        assertPropertyIsPresent(CoreConstants.LAB_APACHE_HOST);
        assertPropertyIsPresent(CoreConstants.LAB_APACHE_SSL_PORT);
        assertPropertyIsPresent(CoreConstants.LAB_CAS_URL);

        assertPropertyIsPresent(CoreConstants.UNDESIRED_MAIL_DOMAINS, List.class);
        assertPropertyIsPresent(CoreConstants.UNDESIRED_COUNTRIES, List.class);

        assertPropertyIsPresent(CoreConstants.APPLET_GATELAB_CLASSES, List.class);
        assertPropertyIsPresent(CoreConstants.APPLET_GATELABTEST_CLASSES, List.class);

        assertPropertyIsNotEmpty(CoreConstants.PUBLICATION_SYSTEM_COMMAND);

        assertPropertyIsNotEmpty(CoreConstants.GIRDER_TOKEN_DURATION_IN_DAYS, Float.class);
        assertPropertyIsNotEmpty(CoreConstants.LAB_SIMULATION_PLATFORM_MAX, Integer.class);

        assertOptionalPropertyType(CoreConstants.USE_LOCAL_FILES_AS_INPUTS, Boolean.class);
        verifyApiProperties();
    }

    private void verifyApiProperties() {
        verifyPropertyNotNull(CarminProperties.CORS_AUTHORIZED_DOMAINS, String[].class);
        verifyPropertyNotNull(CarminProperties.PLATFORM_NAME, String.class);
        verifyPropertyNotNull(CarminProperties.PLATFORM_DESCRIPTION, String.class);
        verifyPropertyNotNull(CarminProperties.PLATFORM_EMAIL, String.class);
        verifyPropertyNotNull(CarminProperties.DEFAULT_LIMIT_LIST_EXECUTION, Long.class);
        verifyPropertyNotNull(CarminProperties.SUPPORTED_API_VERSION, String.class);
        verifyPropertyNotNull(CarminProperties.APIKEY_HEADER_NAME, String.class);
        verifyPropertyNotNull(CarminProperties.APIKEY_GENERATE_NEW_EACH_TIME, Boolean.class);
        verifyPropertyNotNull(CarminProperties.API_DIRECTORY_MIME_TYPE, String.class);
        verifyPropertyNotNull(CarminProperties.API_DEFAULT_MIME_TYPE, String.class);
        verifyPropertyNotNull(CarminProperties.API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class);
        verifyPropertyNotNull(CarminProperties.API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class);
        verifyPropertyNotNull(CarminProperties.API_DATA_TRANSFERT_MAX_SIZE, Long.class);

        if (getKeycloakActivated()) {
            logger.info("Keycloak/OIDC activated");
        } else {
            logger.info("Keycloak/OIDC NOT active");
        }

        // due to arrays and generics, this verification aren't easy to factorize
        Assert.notEmpty(getCarminSupportedTransferProtocols(),
                CarminProperties.SUPPORTED_TRANSFER_PROTOCOLS + " required in api conf file");
        Assert.notEmpty(getCarminSupportedModules(),
                CarminProperties.SUPPORTED_MODULES + " required in api conf file");
        Assert.isInstanceOf(String[].class, getCarminUnsupportedMethods(),
                CarminProperties.UNSUPPORTED_METHODS + " required in api conf file");
        Assert.isInstanceOf(String[].class, getCarminApiPipelineWhiteList(),
                CarminProperties.API_PIPELINE_WHITE_LIST + " required in api conf file");
    }

    private void verifyPropertyNotNull(String propertyKey, Class<?> targetType) {
        Assert.notNull(env.getProperty(propertyKey, targetType),
                propertyKey + " required in api conf file");
    }

    private void assertPropertyIsPresent(String property) {
        assertPropertyIsPresent(property, String.class);
    }

    private void assertPropertyIsPresent(String property, Class<?> type) {
        Assert.notNull(env.getProperty(property, type), property + " should be present");
    }

    private void assertOptionalPropertyType(String property, Class<?> type) {
        Object val = env.getProperty(property, type);
        if (val != null) {
            Assert.isInstanceOf(type, val, val + " must be of type " + type);
        }
    }

    private void assertPropertyIsNotEmpty(String property) {
        assertPropertyIsNotEmpty(property, String.class);
    }

    private void assertPropertyIsNotEmpty(String property, Class<?> type) {
        if (String.class.equals(type)) {
            Assert.hasText(env.getProperty(property), property + " should not be empty");
        } else if (List.class.equals(type)) {
            Assert.notEmpty(env.getProperty(property, List.class), property + " should not be empty");
        } else {
            Assert.notNull(env.getProperty(property, type), property + " should not be empty");
        }
    }

    @Override
    public String getConfigurationFolder() {
        return vipConfigFolder.getAbsolutePath() + "/";
    }

    @Override
    public String getVoName() {
        return env.getRequiredProperty(CoreConstants.VO_NAME);
    }

    @Override
    public String getVoRoot() {
        return env.getRequiredProperty(CoreConstants.VO_ROOT);
    }

    @Override
    public String getServerProxy() {
        return proxyFolder.toPath().resolve(PROXY_FILENAME).toString();
    }

    @Override
    public String getServerProxy(String vo) {
        return Paths.get(getServerProxyFolder(vo)).resolve(PROXY_FILENAME).toString();
    }

    @Override
    public String getServerProxyFolder(String vo) {
        File voProxyFolder = this.proxyFolder.toPath().resolve(vo).toFile();
        createFolderIfNeeded(voProxyFolder);
        return voProxyFolder.getAbsolutePath();
    }

    @Override
    public String getMyProxyHost() {
        return env.getRequiredProperty(CoreConstants.LAB_MYPROXY_HOST);
    }

    @Override
    public int getMyProxyPort() {
        return env.getRequiredProperty(CoreConstants.LAB_MYPROXY_PORT, Integer.class);
    }

    @Override
    public String getMyProxyPass() {
        return env.getRequiredProperty(CoreConstants.LAB_MYPROXY_PASS);
    }

    @Override
    public String getMyProxyUser() {
        return env.getRequiredProperty(CoreConstants.LAB_MYPROXY_USER);
    }

    @Override
    public String getMyProxyLifeTime() {
        return env.getRequiredProperty(CoreConstants.LAB_MYPROXY_LIFETIME);
    }

    @Override
    public int getMyProxyMinHours() {
        return env.getRequiredProperty(CoreConstants.LAB_MYPROXY_MIN_HOURS, Integer.class);
    }

    @Override
    public boolean getMyProxyEnabled() {
        return env.getProperty(CoreConstants.LAB_MYPROXY_ENABLED, Boolean.class, true);
    }

    @Override
    public String getGRIDAHost() {
        return env.getRequiredProperty(CoreConstants.LAB_GRIDA_HOST);
    }

    @Override
    public int getGRIDAPort() {
        return env.getRequiredProperty(CoreConstants.LAB_GRIDA_PORT, Integer.class);
    }

    @Override
    public String getWorkflowsHost() {
        return env.getRequiredProperty(CoreConstants.LAB_SIMULATION_DB_HOST);
    }

    @Override
    public String getWorkflowsPath() {
        return env.getRequiredProperty(CoreConstants.LAB_SIMULATION_FOLDER);
    }

    @Override
    public String getApacheHost() {
        return env.getRequiredProperty(CoreConstants.LAB_APACHE_HOST);
    }

    @Override
    public int getApacheSSLPort() {
        return env.getRequiredProperty(CoreConstants.LAB_APACHE_SSL_PORT, Integer.class);
    }

    @Override
    public boolean useSMA() {
        return env.getProperty(CoreConstants.LAB_SMA_ENABLED, Boolean.class, true);
    }

    @Override
    public String getSMAHost() {
        return env.getRequiredProperty(CoreConstants.LAB_SMA_HOST);
    }

    @Override
    public int getSMAPort() {
        return env.getRequiredProperty(CoreConstants.LAB_SMA_PORT, Integer.class);
    }

    @Override
    public String getDataManagerPath() {
        return env.getRequiredProperty(CoreConstants.LAB_DATA_PATH);
    }

    @Override
    public String getDataManagerUsersHome() {
        return env.getRequiredProperty(CoreConstants.LAB_DATA_USERS_HOME);
    }

    @Override
    public String getDataManagerGroupsHome() {
        return env.getRequiredProperty(CoreConstants.LAB_DATA_GROUPS_HOME);
    }

    @Override
    public String getAltDataManagerUsersHome() {
        return env.getRequiredProperty(CoreConstants.LAB_DATA_ALT_USERS_HOME);
    }

    @Override
    public String getAltDataManagerGroupsHome() {
        return env.getRequiredProperty(CoreConstants.LAB_DATA_ALT_GROUPS_HOME);
    }

    @Override
    public String getTruststoreFile() {
        return env.getRequiredProperty(CoreConstants.LAB_TRUSTSTORE_FILE);
    }

    @Override
    public String getTruststorePass() {
        return env.getRequiredProperty(CoreConstants.LAB_TRUSTSTORE_PASS);
    }

    @Override
    public String getAdminEmail() {
        return env.getRequiredProperty(CoreConstants.LAB_ADMIN_EMAIL);
    }

    @Override
    public String getAdminFirstName() {
        return env.getRequiredProperty(CoreConstants.LAB_ADMIN_FIRST_NAME);
    }

    @Override
    public String getAdminInstitution() {
        return env.getRequiredProperty(CoreConstants.LAB_ADMIN_INSTITUTION);
    }

    @Override
    public String getAdminLastName() {
        return env.getRequiredProperty(CoreConstants.LAB_ADMIN_LAST_NAME);
    }

    @Override
    public String getAdminPassword() {
        return env.getRequiredProperty(CoreConstants.LAB_ADMIN_PASS);
    }

    @Override
    public String getCasURL() {
        return env.getRequiredProperty(CoreConstants.LAB_CAS_URL);
    }

    @Override
    public String getSamlTrustedCertificate(String issuer) {
        logger.info("Getting trusted certificate for issuer {}", issuer);
        return env.getRequiredProperty(CoreConstants.SAML_TRUSTED_CERTIFICATE+"."+issuer);
    }

    @Override
    public String getSAMLDefaultGroup(String issuer) {
        logger.info("Getting default group for issuer "+issuer);
        return env.getRequiredProperty(CoreConstants.SAML_DEFAULT_GROUP +"."+issuer);
    }

    @Override
    public List<String> getUndesiredMailDomains() {
        return Arrays.asList(env.getRequiredProperty(CoreConstants.UNDESIRED_MAIL_DOMAINS, String[].class));
    }

    @Override
    public List<String> getUndesiredCountries() {
        return Arrays.asList(env.getRequiredProperty(CoreConstants.UNDESIRED_COUNTRIES, String[].class));
    }

    @Override
    public int getMaxPlatformRunningSimulations() {
        return env.getRequiredProperty(CoreConstants.LAB_SIMULATION_PLATFORM_MAX, Integer.class);
    }

    @Override
    public int getNumberMonthsToTestLastPublicationUpdates() {
        return env.getRequiredProperty(CoreConstants.PUB_MONTHS_UPDATES, Integer.class);
    }

    @Override
    public String getPublicationCommandLine() {
        return env.getRequiredProperty(CoreConstants.PUBLICATION_SYSTEM_COMMAND);
    }

    @Override
    public float getGirderTokenDurationInDays() {
        return env.getRequiredProperty(CoreConstants.GIRDER_TOKEN_DURATION_IN_DAYS, Float.class);
    }

    @Override
    public boolean useLocalFilesInInputs() {
        return env.getProperty(CoreConstants.USE_LOCAL_FILES_AS_INPUTS, Boolean.class, false);
    }

    @Override
    public int getApiParallelDownloadNb() {
        return env.getProperty(CoreConstants.API_PARALLEL_DOWNLOAD_NB, Integer.class, 20);
    }

    @Override
    public String getReproVIPRootDir() {
        return env.getProperty(CoreConstants.REPROVIP_ROOT_DIR, "/vip/ReproVip/");
    }

    @Override
    public String getMoteurServerPassword() {
        return env.getRequiredProperty(CoreConstants.MOTEUR_REST_PASSWORD);
    }

    @Override
    public String getHostURL() {
        return env.getRequiredProperty(CoreConstants.HOST_URL);
    }

    // vip-core level settings: these are CarminProperties constants, but with a wider scope than the /rest API:
    // don't prefix their getters with "Carmin"

    @Override
    public String getApikeyHeaderName() { return env.getRequiredProperty(CarminProperties.APIKEY_HEADER_NAME); }

    @Override
    public boolean getKeycloakActivated() { return env.getProperty(CarminProperties.KEYCLOAK_ACTIVATED, Boolean.class, Boolean.FALSE); }

    private OidcLoginProviderConfig makeOidcConfig(
            String clientIdKey, String clientSecretKey, String redirectUriKey,
            String authorizationUriKey, String tokenUriKey, String userInfoUriKey, String jwkSetUriKey) {
        String clientId = env.getProperty(clientIdKey);
        if (clientId == null || clientId.isEmpty()) {
            return null;
        }
        OidcLoginProviderConfig conf = new OidcLoginProviderConfig();
        conf.clientId = clientId;
        conf.clientSecret = env.getProperty(clientSecretKey);
        conf.redirectUri = env.getProperty(redirectUriKey);
        conf.authorizationUri = env.getProperty(authorizationUriKey);
        conf.tokenUri = env.getProperty(tokenUriKey);
        conf.userInfoUri = env.getProperty(userInfoUriKey);;
        conf.jwkSetUri = env.getProperty(jwkSetUriKey);
        return conf;
    }

    @Override
    public OidcLoginProviderConfig getOidcConfigEGI() {
        return makeOidcConfig(
                CarminProperties.EGI_CLIENT_ID,
                CarminProperties.EGI_CLIENT_SECRET,
                CarminProperties.EGI_REDIRECT_URI,
                CarminProperties.EGI_AUTHORIZATION_URI,
                CarminProperties.EGI_TOKEN_URI,
                CarminProperties.EGI_USER_INFO_URI,
                CarminProperties.EGI_JWK_SET_URI
        );
    }

    @Override
    public OidcLoginProviderConfig getOidcConfigLSLOGIN() {
        return makeOidcConfig(
                CarminProperties.LSLOGIN_CLIENT_ID,
                CarminProperties.LSLOGIN_CLIENT_SECRET,
                CarminProperties.LSLOGIN_REDIRECT_URI,
                CarminProperties.LSLOGIN_AUTHORIZATION_URI,
                CarminProperties.LSLOGIN_TOKEN_URI,
                CarminProperties.LSLOGIN_USER_INFO_URI,
                CarminProperties.LSLOGIN_JWK_SET_URI
        );
    }

    // vip-api level settings: these only apply to the /rest API, prefix them with "Carmin"

    @Override
    public String[] getCarminCorsAuthorizedDomains() { return env.getRequiredProperty(CarminProperties.CORS_AUTHORIZED_DOMAINS, String[].class); }

    @Override
    public String getCarminPlatformName() { return env.getRequiredProperty(CarminProperties.PLATFORM_NAME); }

    @Override
    public String getCarminPlatformDescription() { return env.getRequiredProperty(CarminProperties.PLATFORM_DESCRIPTION); }

    @Override
    public String getCarminPlatformEmail() { return env.getRequiredProperty(CarminProperties.PLATFORM_EMAIL); }

    @Override
    public long getCarminDefaultLimitListExecution() { return env.getRequiredProperty(CarminProperties.DEFAULT_LIMIT_LIST_EXECUTION, Long.class); }

    @Override
    public String getCarminSupportedApiVersion() { return env.getRequiredProperty(CarminProperties.SUPPORTED_API_VERSION); }

    @Override
    public boolean getCarminApikeyGenerateNewEachTime() { return env.getRequiredProperty(CarminProperties.APIKEY_GENERATE_NEW_EACH_TIME, Boolean.class); }

    @Override
    public String getCarminApiDirectoryMimeType() { return env.getRequiredProperty(CarminProperties.API_DIRECTORY_MIME_TYPE); }

    @Override
    public String getCarminApiDefaultMimeType() { return env.getRequiredProperty(CarminProperties.API_DEFAULT_MIME_TYPE); }

    @Override
    public int getCarminApiDownloadRetryInSeconds() { return env.getRequiredProperty(CarminProperties.API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class); }

    @Override
    public int getCarminApiDownloadTimeoutInSeconds() { return env.getRequiredProperty(CarminProperties.API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class); }

    @Override
    public long getCarminApiDataTransfertMaxSize() { return env.getRequiredProperty(CarminProperties.API_DATA_TRANSFERT_MAX_SIZE, Long.class); }

    @Override
    public SupportedTransferProtocol[] getCarminSupportedTransferProtocols() { return env.getProperty(CarminProperties.SUPPORTED_TRANSFER_PROTOCOLS, SupportedTransferProtocol[].class); }

    @Override
    public Module[] getCarminSupportedModules() { return env.getProperty(CarminProperties.SUPPORTED_MODULES, Module[].class); }

    @Override
    public String[] getCarminUnsupportedMethods() { return env.getProperty(CarminProperties.UNSUPPORTED_METHODS, String[].class); }

    @Override
    public String[] getCarminApiPipelineWhiteList() { return env.getProperty(CarminProperties.API_PIPELINE_WHITE_LIST, String[].class); }
}
