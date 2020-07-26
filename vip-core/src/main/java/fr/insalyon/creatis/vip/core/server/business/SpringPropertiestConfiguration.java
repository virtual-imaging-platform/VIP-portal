package fr.insalyon.creatis.vip.core.server.business;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Configuration
@Profile("spring-config-server")
public class SpringPropertiestConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SamlTokenValidator.class);

    @Autowired
    public SpringPropertiestConfiguration(
            ConfigurableApplicationContext configurableApplicationContext) throws IOException {
        ConfigurableEnvironment env = configurableApplicationContext.getEnvironment();
        // look for configLocation in environment
        String configFolder = env.getProperty("vipConfigFolder");
        // if present, look for file
        if (configFolder != null) {
            logger.info("found vipConfigFolder property : {}", configFolder);
        } else {
            // if not, look in user home folder
            configFolder = env.getProperty("user.home")  + Server.VIP_DIR;
            logger.info("vipConfigFolder property not found, using user-home : {}", configFolder);
        }
        if ( ! configFolder.endsWith("/")) {
            configFolder += "/";
        }
        Resource configFileResource = new FileSystemResource(configFolder + Server.CONF_FILE);
        env.getPropertySources().addLast(
                new ResourcePropertySource(configFileResource)
        );
    }

    @Component
    public static class SpringConfigServer implements Server {


        @Override
        public String getDatabaseServerHost() {
            return null;
        }

        @Override
        public int getDatabaseServerPort() {
            return 0;
        }

        @Override
        public String getConfigurationFolder() {
            return null;
        }

        @Override
        public String getVoName() {
            return null;
        }

        @Override
        public String getVoRoot() {
            return null;
        }

        @Override
        public String getServerProxy() {
            return null;
        }

        @Override
        public String getServerProxy(String vo) {
            return null;
        }

        @Override
        public String getServerProxyFolder(String vo) {
            return null;
        }

        @Override
        public String getMyProxyHost() {
            return null;
        }

        @Override
        public int getMyProxyPort() {
            return 0;
        }

        @Override
        public String getMyProxyPass() {
            return null;
        }

        @Override
        public String getMyProxyUser() {
            return null;
        }

        @Override
        public String getMyProxyLifeTime() {
            return null;
        }

        @Override
        public int getMyProxyMinHours() {
            return 0;
        }

        @Override
        public String getMoteurServer() {
            return null;
        }

        @Override
        public String getGRIDAHost() {
            return null;
        }

        @Override
        public int getGRIDAPort() {
            return 0;
        }

        @Override
        public String getN4uGridaHost() {
            return null;
        }

        @Override
        public int getN4uGridaPort() {
            return 0;
        }

        @Override
        public String getWorkflowsHost() {
            return null;
        }

        @Override
        public int getWorkflowsPort() {
            return 0;
        }

        @Override
        public String getWorkflowsPath() {
            return null;
        }

        @Override
        public String getWorflowsExecMode() {
            return null;
        }

        @Override
        public String getApacheHost() {
            return null;
        }

        @Override
        public int getApacheSSLPort() {
            return 0;
        }

        @Override
        public String getSMAHost() {
            return null;
        }

        @Override
        public int getSMAPort() {
            return 0;
        }

        @Override
        public String getDataManagerPath() {
            return null;
        }

        @Override
        public String getDataManagerLFCHost() {
            return null;
        }

        @Override
        public int getDataManagerLFCPort() {
            return 0;
        }

        @Override
        public String getDataManagerUsersHome() {
            return null;
        }

        @Override
        public String getDataManagerGroupsHome() {
            return null;
        }

        @Override
        public String getAltDataManagerUsersHome() {
            return null;
        }

        @Override
        public String getAltDataManagerGroupsHome() {
            return null;
        }

        @Override
        public String getTruststoreFile() {
            return null;
        }

        @Override
        public String getTruststorePass() {
            return null;
        }

        @Override
        public String getAdminEmail() {
            return null;
        }

        @Override
        public String getAdminFirstName() {
            return null;
        }

        @Override
        public String getAdminInstitution() {
            return null;
        }

        @Override
        public String getAdminLastName() {
            return null;
        }

        @Override
        public String getAdminPassword() {
            return null;
        }

        @Override
        public String getAdminPhone() {
            return null;
        }

        @Override
        public String getCasURL() {
            return null;
        }

        @Override
        public String getSshPublicKey() {
            return null;
        }

        @Override
        public String getSamlTrustedCertificate(String issuer) {
            return null;
        }

        @Override
        public String getSAMLAccountType(String issuer) {
            return null;
        }

        @Override
        public String getApplicationImporterFileRepository() {
            return null;
        }

        @Override
        public String getApplicationImporterRootFolder() {
            return null;
        }

        @Override
        public List<String> getApplicationImporterRequirements() {
            return null;
        }

        @Override
        public String getDeleteFilesAfterUpload() {
            return null;
        }

        @Override
        public List<String> getAppletGateLabClasses() {
            return null;
        }

        @Override
        public void setAppletGateLabClasses(List<String> appletGateLabClasses) {

        }

        @Override
        public List<String> getAppletGateLabTestClasses() {
            return null;
        }

        @Override
        public void setAppletGateLabTestClasses(List<String> appletGateLabTestClasses) {

        }

        @Override
        public HashMap<String, Integer> getReservedClasses() {
            return null;
        }

        @Override
        public List<String> getUndesiredMailDomains() {
            return null;
        }

        @Override
        public List<String> getUndesiredCountries() {
            return null;
        }

        @Override
        public int getMaxPlatformRunningSimulations() {
            return 0;
        }

        @Override
        public int getNumberMonthsToTestLastPublicationUpdates() {
            return 0;
        }

        @Override
        public String getPublicationCommandLine() {
            return null;
        }

        @Override
        public void setMaxPlatformRunningSimulations(int maxPlatformRunningSimulations) throws ConfigurationException {

        }

        @Override
        public float getGirderTokenDurationInDays() {
            return 0;
        }
    }

}
