package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Configuration
@Profile("spring-config-server")
public class SpringPropertiesConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SamlTokenValidator.class);

    @Autowired
    public SpringPropertiesConfiguration(
            Resource vipConfigFolder,
            ConfigurableApplicationContext configurableApplicationContext) throws IOException {
        ConfigurableEnvironment env = configurableApplicationContext.getEnvironment();
        Resource configFileResource = new FileSystemResource(vipConfigFolder + Server.CONF_FILE);
        env.getPropertySources().addLast(
                new ResourcePropertySource(configFileResource)
        );
    }

    @Component
    @Profile("spring-config-server")
    public static class SpringConfigServer implements Server {

        @Value("${"+ CoreConstants.LAB_ADMIN_FIRST_NAME + ":Administrator}")
        private String adminFirstName;
        @Value("${"+ CoreConstants.LAB_ADMIN_LAST_NAME + ":}")
        private String adminLastName;
        @Value("${"+ CoreConstants.LAB_ADMIN_EMAIL + ":admin@vip.creatis.insa-lyon.fr}")
        private String adminEmail;
        @Value("${"+ CoreConstants.LAB_ADMIN_INSTITUTION + ":}")
        private String adminInstitution;
        @Value("${"+ CoreConstants.LAB_ADMIN_PHONE + ":}")
        private String adminPhone;
        @Value("${"+ CoreConstants.LAB_ADMIN_PASS + ":admin}")
        private String adminPassword;
        @Value("${"+ CoreConstants.VO_NAME + ":biomed}")
        private String voName;
        @Value("${"+ CoreConstants.VO_ROOT + ":/grid/biomed}")
        private String voRoot;
        @Value("${"+ CoreConstants.LAB_MYPROXY_HOST + ":localhost}")
        private String myProxyHost;
        @Value("${"+ CoreConstants.LAB_MYPROXY_PORT + ":7211}")
        private Integer myProxyPort;
        @Value("${"+ CoreConstants.LAB_MYPROXY_USER + ":}")
        private String myProxyUser;
        @Value("${"+ CoreConstants.LAB_MYPROXY_PASS + ":}")
        private String myProxyPass;
        @Value("${"+ CoreConstants.LAB_MYPROXY_LIFETIME + ":86400}")
        private String myProxyLifeTime;
        @Value("${"+ CoreConstants.LAB_MYPROXY_MIN_HOURS + ":12}")
        private Integer myProxyMinHours;
        @Value("${"+ CoreConstants.LAB_SMA_HOST + ":kingkong.grid.creatis.insa-lyon.fr}")
        private String SMAHost;
        @Value("${"+ CoreConstants.LAB_SMA_PORT + ":8082}")
        private Integer SMAPort;
        @Value("${"+ CoreConstants.LAB_GRIDA_HOST + ":localhost}")
        private String gridaHost;
        @Value("${"+ CoreConstants.LAB_GRIDA_PORT + ":9006}")
        private Integer gridaPort;
        @Value("${"+ CoreConstants.LAB_DATA_USERS_HOME + ":/users}")
        private String dataManagerUsersHome;
        @Value("${"+ CoreConstants.LAB_DATA_GROUPS_HOME + ":/groups}")
        private String dataManagerGroupsHome;
        @Value("${"+ CoreConstants.LAB_DATA_PATH + ":/tmp}")
        private String dataManagerPath;
        @Value("${"+ CoreConstants.LAB_DATA_ALT_USERS_HOME + ":}")
        private String altDataManagerUsersHome;
        @Value("${"+ CoreConstants.LAB_DATA_ALT_GROUPS_HOME + ":}")
        private String altDataManagerGroupsHome;
        @Value("${"+ CoreConstants.LAB_TRUSTSTORE_FILE + ":/usr/local/apache-tomcat-6.0.29/conf/truststore.jks}")
        private String truststoreFile;
        @Value("${"+ CoreConstants.LAB_TRUSTSTORE_PASS + ":}")
        private String truststorePass;

        @Value("${"+ CoreConstants.LAB_SIMULATION_PLATFORM_MAX + ":"+Integer.MAX_VALUE+"}")
        private Integer maxPlatformRunningSimulations;
        @Value("${"+ CoreConstants.LAB_SIMULATION_FOLDER + ":/var/www/html/workflows}")
        private String workflowsPath;
        @Value("${"+ CoreConstants.LAB_SIMULATION_DB_HOST + ":localhost}")
        private String workflowsHost;

        @Value("${"+ CoreConstants.LAB_APACHE_HOST + ":localhost}")
        private String apacheHost;
        @Value("${"+ CoreConstants.LAB_APACHE_SSL_PORT + ":80}")
        private Integer apacheSSLPort;

        @Value("${"+ CoreConstants.LAB_CAS_URL + ":https://ng-cas.maatg.fr/pandora-gateway-sl-cas}")
        private String casURL;
        @Value("${"+ CoreConstants.SSH_PUBLIC_KEY + ":ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAuNjIXlgjuBR+WfjGtkieecZfe/ZL6EyNJTbL14bn3/Soof0kFSshDJvFgSH1hNwMMU1hynLbzcEbLTyVMoGQKfQkq7mJPajy9g8878WCKxCRbXv3W1/HT9iab/qqt2dcRYnDEruHwgyELBhQuMAe2W2/mgjd7Y5PxE01bwDcenYl3cU3iJk1sAOHao6P+3xU6Ov+TD8K9aC0LzZpM+rzAmS9HOZ9nvzERExd7k4TUpyffQV9Dpb5jEnEViF3VHqplB8AbWDdcJbiVkUBUe4hQb7nmWP0kHl1+v5SQJ1B4mWCZ+35Rc/9b1GsmPnXg3qqhjeKbrim/NbcUwKr9NPWjQ== vip-services@kingkong.grid.creatis.insa-lyon.fr}")
        private String sshPublicKey;

        @Value("${"+ CoreConstants.UNDESIRED_MAIL_DOMAINS + ":}#{T(java.util.Collections).emptyList()}")
        private List<String> undesiredMailDomains;
        @Value("${"+ CoreConstants.UNDESIRED_COUNTRIES + ":}#{T(java.util.Collections).emptyList()}")
        private List<String> undesiredCountries;

        @Value("${"+ CoreConstants.APPLET_GATELAB_CLASSES + ":GateLab}")
        private List<String> gatelabClasses;
        @Value("${"+ CoreConstants.APPLET_GATELABTEST_CLASSES + ":GateLab Test}")
        private List<String> gatelabTestClasses;

        @Value("${"+ CoreConstants.APPLICATION_FILES_REPOSITORY + ":/tmp/boutiques-cache}")
        private String applicationImporterFileRepository;
        @Value("${"+ CoreConstants.APP_IMPORTER_ROOT_FOLDER + ":/biomed/user/c/creatis/vip/data/groups/Applications}")
        private String applicationImporterRootFolder;
        @Value("${"+ CoreConstants.APP_REQUIREMENTS + ":}#{T(java.util.Collections).emptyList()}")
        private List<String> applicationImporterRequirements;

        @Value("${"+ CoreConstants.PUB_MONTHS_UPDATES + ":6}")
        private Integer numberMonthsToTestLastPublicationUpdates;

        @Value("${"+ CoreConstants.PUBLICATION_SYSTEM_COMMAND + ":bosh publish --sandbox --no-int $FILE}")
        private String publicationCommandLine;

        @Value("${"+ CoreConstants.GIRDER_TOKEN_DURATION_IN_DAYS + ":1.0}")
        private Float girderTokenDurationInDays;

        private Environment environment;

        private File vipConfigFolder;
        private File proxyFolder;
        private HashMap<String, Integer> reservedClasses;

        @Autowired
        public SpringConfigServer(
                Environment environment, Resource vipConfigFolder)
                throws IOException {
            this.environment = environment;
            this.vipConfigFolder = vipConfigFolder.getFile();
            this.proxyFolder = vipConfigFolder.createRelative(PROXIES_DIR).getFile();
            createFolderIfNeeded(proxyFolder);
            buildReservedClasses();
        }

        private void createFolderIfNeeded(File folder) {
            if ( ! folder.exists() &&
                    ! folder.mkdir()) {
                logger.error("Cannot create VIP config folder : {}", folder);
                throw new BeanInitializationException("Cannot create VIP config folder");
            }
        }

        private void buildReservedClasses() {
            reservedClasses = new HashMap<>();
            gatelabClasses.forEach(className -> reservedClasses.put(className,0));
            gatelabTestClasses.forEach(className -> reservedClasses.put(className,1));
        }

        @Override
        public String getConfigurationFolder() {
            return vipConfigFolder.getAbsolutePath();
        }

        @Override
        public String getVoName() {
            return voName;
        }

        @Override
        public String getVoRoot() {
            return voRoot;
        }

        @Override
        public String getServerProxy() {
            return vipConfigFolder.toPath().resolve(PROXY_FILENAME).toString();
        }

        @Override
        public String getServerProxy(String vo) {
            return Paths.get(getServerProxyFolder(vo)).resolve(PROXY_FILENAME).toString();
        }

        @Override
        public String getServerProxyFolder(String vo) {
            File voProxyFolder = this.proxyFolder.toPath().resolve(vo).toFile();
            createFolderIfNeeded(voProxyFolder);
            return proxyFolder.getAbsolutePath();
        }

        @Override
        public String getMyProxyHost() {
            return myProxyHost;
        }

        @Override
        public int getMyProxyPort() {
            return myProxyPort;
        }

        @Override
        public String getMyProxyPass() {
            return myProxyPass;
        }

        @Override
        public String getMyProxyUser() {
            return myProxyUser;
        }

        @Override
        public String getMyProxyLifeTime() {
            return myProxyLifeTime;
        }

        @Override
        public int getMyProxyMinHours() {
            return myProxyMinHours;
        }

        @Override
        public String getGRIDAHost() {
            return gridaHost;
        }

        @Override
        public int getGRIDAPort() {
            return gridaPort;
        }

        @Override
        public String getWorkflowsHost() {
            return workflowsHost;
        }

        @Override
        public String getWorkflowsPath() {
            return workflowsPath;
        }

        @Override
        public String getApacheHost() {
            return apacheHost;
        }

        @Override
        public int getApacheSSLPort() {
            return apacheSSLPort;
        }

        @Override
        public String getSMAHost() {
            return SMAHost;
        }

        @Override
        public int getSMAPort() {
            return SMAPort;
        }

        @Override
        public String getDataManagerPath() {
            return dataManagerPath;
        }

        @Override
        public String getDataManagerUsersHome() {
            return dataManagerUsersHome;
        }

        @Override
        public String getDataManagerGroupsHome() {
            return dataManagerGroupsHome;
        }

        @Override
        public String getAltDataManagerUsersHome() {
            return altDataManagerUsersHome;
        }

        @Override
        public String getAltDataManagerGroupsHome() {
            return altDataManagerGroupsHome;
        }

        @Override
        public String getTruststoreFile() {
            return truststoreFile;
        }

        @Override
        public String getTruststorePass() {
            return truststorePass;
        }

        @Override
        public String getAdminEmail() {
            return adminEmail;
        }

        @Override
        public String getAdminFirstName() {
            return adminFirstName;
        }

        @Override
        public String getAdminInstitution() {
            return adminInstitution;
        }

        @Override
        public String getAdminLastName() {
            return adminLastName;
        }

        @Override
        public String getAdminPassword() {
            return adminPassword;
        }

        @Override
        public String getAdminPhone() {
            return adminPhone;
        }

        @Override
        public String getCasURL() {
            return casURL;
        }

        @Override
        public String getSshPublicKey() {
            return sshPublicKey;
        }

        @Override
        public String getSamlTrustedCertificate(String issuer) {
            logger.info("Getting trusted certificate for issuer {}", issuer);
            return environment.getProperty(CoreConstants.SAML_TRUSTED_CERTIFICATE+"."+issuer);
        }

        @Override
        public String getSAMLAccountType(String issuer) {
            logger.info("Getting account type for issuer "+issuer);
            return environment.getProperty(CoreConstants.SAML_ACCOUNT_TYPE+"."+issuer);
        }

        @Override
        public String getApplicationImporterFileRepository() {
            return applicationImporterFileRepository;
        }

        @Override
        public String getApplicationImporterRootFolder() {
            return applicationImporterRootFolder;
        }

        @Override
        public List<String> getApplicationImporterRequirements() {
            return applicationImporterRequirements;
        }

        @Override
        public HashMap<String, Integer> getReservedClasses() {
            return reservedClasses;
        }

        @Override
        public List<String> getUndesiredMailDomains() {
            return undesiredMailDomains;
        }

        @Override
        public List<String> getUndesiredCountries() {
            return undesiredCountries;
        }

        @Override
        public int getMaxPlatformRunningSimulations() {
            return maxPlatformRunningSimulations;
        }

        @Override
        public int getNumberMonthsToTestLastPublicationUpdates() {
            return numberMonthsToTestLastPublicationUpdates;
        }

        @Override
        public String getPublicationCommandLine() {
            return publicationCommandLine;
        }

        @Override
        public void setMaxPlatformRunningSimulations(int maxPlatformRunningSimulations) throws ConfigurationException {
            throw new ConfigurationException("Not possible to change conf value with spring");
        }

        @Override
        public float getGirderTokenDurationInDays() {
            return girderTokenDurationInDays;
        }
    }

}
