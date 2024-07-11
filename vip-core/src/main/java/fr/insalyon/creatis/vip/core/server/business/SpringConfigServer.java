package fr.insalyon.creatis.vip.core.server.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

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

        assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_HOST);
        assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_PORT);
        assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_USER);
        assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_PASS);
        assertPropertyIsPresent(CoreConstants.LAB_MYPROXY_LIFETIME);
        assertPropertyIsNotEmpty(CoreConstants.LAB_MYPROXY_MIN_HOURS, Integer.class);

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
        assertPropertyIsPresent(CoreConstants.SSH_PUBLIC_KEY);

        assertPropertyIsPresent(CoreConstants.UNDESIRED_MAIL_DOMAINS, List.class);
        assertPropertyIsPresent(CoreConstants.UNDESIRED_COUNTRIES, List.class);

        assertPropertyIsPresent(CoreConstants.APPLET_GATELAB_CLASSES, List.class);
        assertPropertyIsPresent(CoreConstants.APPLET_GATELABTEST_CLASSES, List.class);

        assertPropertyIsNotEmpty(CoreConstants.APPLICATION_FILES_REPOSITORY);
        assertPropertyIsNotEmpty(CoreConstants.APP_IMPORTER_ROOT_FOLDER);
        assertPropertyIsPresent(CoreConstants.APP_REQUIREMENTS, List.class);
        assertPropertyIsNotEmpty(CoreConstants.PUBLICATION_SYSTEM_COMMAND);

        assertPropertyIsNotEmpty(CoreConstants.GIRDER_TOKEN_DURATION_IN_DAYS, Float.class);
        assertPropertyIsNotEmpty(CoreConstants.LAB_SIMULATION_PLATFORM_MAX, Integer.class);

        assertOptionalPropertyType(CoreConstants.USE_LOCAL_FILES_AS_INPUTS, Boolean.class);

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
    public String getSshPublicKey() {
        return env.getRequiredProperty(CoreConstants.SSH_PUBLIC_KEY);
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
    public String getApplicationImporterFileRepository() {
        return env.getRequiredProperty(CoreConstants.APPLICATION_FILES_REPOSITORY);
    }

    @Override
    public String getApplicationImporterRootFolder() {
        return env.getRequiredProperty(CoreConstants.APP_IMPORTER_ROOT_FOLDER);
    }

    @Override
    public List<String> getApplicationImporterRequirements() {
        return Arrays.asList(env.getRequiredProperty(CoreConstants.APP_REQUIREMENTS, String[].class));
    }

    @Override
    public HashMap<String, Integer> getReservedClasses() {
        HashMap<String, Integer> reservedClasses = new HashMap<>();
        Stream.of(
                env.getRequiredProperty(
                        CoreConstants.APPLET_GATELAB_CLASSES,
                        String[].class))
                .forEach(className -> reservedClasses.put(className,0));
        Stream.of(
                env.getRequiredProperty(
                        CoreConstants.APPLET_GATELABTEST_CLASSES,
                        String[].class))
                .forEach(className -> reservedClasses.put(className,0));
        return reservedClasses;
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
    public boolean useMoteurlite() {
        return env.getProperty(CoreConstants.USE_MOTEURLITE, Boolean.class, false);
    }
}
