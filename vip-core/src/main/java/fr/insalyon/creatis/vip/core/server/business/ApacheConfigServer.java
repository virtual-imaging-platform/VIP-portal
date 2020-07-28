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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 *
 * @author Rafael Ferreira da Silva
 */
@Component
// to avoid bean being created in tests
// although the vif.conf file does not exist and causes an (silent error)
@Profile({"default", "prod", "apache-config-server"})
public class ApacheConfigServer implements Server {
    // Configuration File
    private PropertiesConfiguration config;
    // Constants
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // Portal
    private String configurationFolder;
    private String serverProxy;
    // Admin
    private String adminFirstName;
    private String adminLastName;
    private String adminEmail;
    private String adminInstitution;
    private String adminPhone;
    private String adminPassword;
    // VO
    private String voName;
    private String voRoot;
    // MyProxy
    private String myProxyHost;
    private int myProxyPort;
    private String myProxyUser;
    private String myProxyPass;
    private String myProxyLifeTime;
    private int myProxyMinHours;
    // Mail
    private String SMAHost;
    private int SMAPort;
    // GRIDA server
    private String gridaHost;
    private int gridaPort;
    // Data Manager
    private String dataManagerUsersHome;
    private String dataManagerGroupsHome;
    private String dataManagerPath;
    // old lfn directories prefixes on the LFC
    private String altDataManagerUsersHome;
    private String altDataManagerGroupsHome;
    // Moteur
    private String truststoreFile;
    private String truststorePass;
    // Simulations
    private int maxPlatformRunningSimulations;
    private String workflowsPath;
    private String workflowsHost;
    // Apache
    private String apacheHost = "localhost";
    private int apacheSSLPort = 80;
    //cas
    private String casURL;
    //ssh
    private String sshPublicKey;
    //application,GateLab
    private List<String> appletGateLabClasses;
    private List<String> appletGateLabTestClasses;
    //Integer=0 for GateLab Prod Class and Integer=1 for GateLab Prod Class
    private HashMap<String, Integer> reservedClasses;
    //undesired email domains and countries
    private List<String> undesiredMailDomains;
    private List<String> undesiredCountries;
    // Application importer
    private String applicationImporterFileRepository;
    private String applicationImporterRootFolder;
    private List<String> applicationImporterRequirements;
    //Publication
    private int numberMonthsToTestLastPublicationUpdates;
    //Zenodo publication
    private String publicationCommandLine;

    // External storage, girder.
    private float girderTokenDurationInDays;

    @SuppressWarnings("unchecked")
    public ApacheConfigServer() {

        try {
            // Directories
            configurationFolder = setPath(System.getenv("HOME") + VIP_DIR);
            serverProxy = setPath(configurationFolder + PROXIES_DIR) + "/x509up_server";

            // Configuration File
            String confFilePath = configurationFolder + CONF_FILE;
            config = new PropertiesConfiguration(confFilePath);
            logger.info("Loading config file: " + confFilePath);

            adminFirstName = config.getString(CoreConstants.LAB_ADMIN_FIRST_NAME, "Administrator");
            adminLastName = config.getString(CoreConstants.LAB_ADMIN_LAST_NAME, "");
            adminEmail = config.getString(CoreConstants.LAB_ADMIN_EMAIL, "admin@vip.creatis.insa-lyon.fr");
            adminInstitution = config.getString(CoreConstants.LAB_ADMIN_INSTITUTION, "");
            adminPhone = config.getString(CoreConstants.LAB_ADMIN_PHONE, "");
            adminPassword = config.getString(CoreConstants.LAB_ADMIN_PASS, "admin");

            voName = config.getString(CoreConstants.VO_NAME, "biomed");
            voRoot = config.getString(CoreConstants.VO_ROOT, "/grid/biomed");

            myProxyHost = config.getString(CoreConstants.LAB_MYPROXY_HOST, "localhost");
            myProxyPort = config.getInt(CoreConstants.LAB_MYPROXY_PORT, 7211);
            myProxyUser = config.getString(CoreConstants.LAB_MYPROXY_USER, "");
            myProxyPass = config.getString(CoreConstants.LAB_MYPROXY_PASS, "");
            myProxyLifeTime = config.getString(CoreConstants.LAB_MYPROXY_LIFETIME, "86400");
            myProxyMinHours = config.getInt(CoreConstants.LAB_MYPROXY_MIN_HOURS, 12);

            SMAHost = config.getString(CoreConstants.LAB_SMA_HOST, "kingkong.grid.creatis.insa-lyon.fr");
            SMAPort = config.getInt(CoreConstants.LAB_SMA_PORT, 8082);

            gridaHost = config.getString(CoreConstants.LAB_GRIDA_HOST, "localhost");
            gridaPort = config.getInt(CoreConstants.LAB_GRIDA_PORT, 9006);

            dataManagerUsersHome = config.getString(CoreConstants.LAB_DATA_USERS_HOME, "/users");
            dataManagerGroupsHome = config.getString(CoreConstants.LAB_DATA_GROUPS_HOME, "/groups");
            dataManagerPath = config.getString(CoreConstants.LAB_DATA_PATH, "/tmp");

            altDataManagerUsersHome = config.getString(CoreConstants.LAB_DATA_ALT_USERS_HOME, "");
            altDataManagerGroupsHome = config.getString(CoreConstants.LAB_DATA_ALT_GROUPS_HOME, "");

            truststoreFile = config.getString(CoreConstants.LAB_TRUSTSTORE_FILE, "/usr/local/apache-tomcat-6.0.29/conf/truststore.jks");
            truststorePass = config.getString(CoreConstants.LAB_TRUSTSTORE_PASS, "");

            maxPlatformRunningSimulations=config.getInt(CoreConstants.LAB_SIMULATION_PLATFORM_MAX, Integer.MAX_VALUE);
            workflowsPath = config.getString(CoreConstants.LAB_SIMULATION_FOLDER, "/var/www/html/workflows");
            workflowsHost = config.getString(CoreConstants.LAB_SIMULATION_DB_HOST, "localhost");

            apacheHost = config.getString(CoreConstants.LAB_APACHE_HOST, apacheHost);
            apacheSSLPort = config.getInt(CoreConstants.LAB_APACHE_SSL_PORT, apacheSSLPort);

            casURL = config.getString(CoreConstants.LAB_CAS_URL, "https://ng-cas.maatg.fr/pandora-gateway-sl-cas");

            sshPublicKey = config.getString(CoreConstants.SSH_PUBLIC_KEY, "ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAuNjIXlgjuBR+WfjGtkieecZfe/ZL6EyNJTbL14bn3/Soof0kFSshDJvFgSH1hNwMMU1hynLbzcEbLTyVMoGQKfQkq7mJPajy9g8878WCKxCRbXv3W1/HT9iab/qqt2dcRYnDEruHwgyELBhQuMAe2W2/mgjd7Y5PxE01bwDcenYl3cU3iJk1sAOHao6P+3xU6Ov+TD8K9aC0LzZpM+rzAmS9HOZ9nvzERExd7k4TUpyffQV9Dpb5jEnEViF3VHqplB8AbWDdcJbiVkUBUe4hQb7nmWP0kHl1+v5SQJ1B4mWCZ+35Rc/9b1GsmPnXg3qqhjeKbrim/NbcUwKr9NPWjQ== vip-services@kingkong.grid.creatis.insa-lyon.fr");

            List<String> appletGateLabCl = new ArrayList<String>();
            appletGateLabCl.add("GateLab");
            appletGateLabClasses = config.getList(CoreConstants.APPLET_GATELAB_CLASSES, appletGateLabCl);

            List<String> appletGateLabTestCl = new ArrayList<String>();
            appletGateLabTestCl.add("GateLab Test");
            appletGateLabTestClasses = config.getList(CoreConstants.APPLET_GATELABTEST_CLASSES, appletGateLabTestCl);

            reservedClasses = new HashMap<String, Integer>();
            for (final String gateClass : appletGateLabClasses) {
                this.reservedClasses.put(gateClass, 0);
            }
            for (final String gateTestClass : appletGateLabTestClasses) {
                this.reservedClasses.put(gateTestClass, 1);
            }

            //undesired Mail Domains
            List<String> listUndesiredEmailDomains = new ArrayList<>();
            undesiredMailDomains = config.getList(CoreConstants.UNDESIRED_MAIL_DOMAINS, listUndesiredEmailDomains);

            //undesired countries
            List<String> listUndesiredCountries = new ArrayList<>();
            undesiredCountries = config.getList(CoreConstants.UNDESIRED_COUNTRIES, listUndesiredCountries);

            //Applicatoin importer
            applicationImporterFileRepository = config.getString(CoreConstants.APPLICATION_FILES_REPOSITORY, "/tmp/boutiques-cache");
            applicationImporterRootFolder = config.getString(CoreConstants.APP_IMPORTER_ROOT_FOLDER, "/biomed/user/c/creatis/vip/data/groups/Applications");
            applicationImporterRequirements = config.getList(CoreConstants.APP_REQUIREMENTS, new ArrayList<>());

            //Publication
            numberMonthsToTestLastPublicationUpdates = config.getInt(CoreConstants.PUB_MONTHS_UPDATES, 6);

            //Zenodo publication
            publicationCommandLine = config.getString(CoreConstants.PUBLICATION_SYSTEM_COMMAND, "bosh publish --sandbox --no-int $FILE");

            // External storage, girder.
            // 1.0 is for one day.  A half day, for example, can be set with the
            // value 0.5.
            girderTokenDurationInDays = config.getFloat(
                CoreConstants.GIRDER_TOKEN_DURATION_IN_DAYS, 1.0f);

            config.setProperty(CoreConstants.LAB_ADMIN_FIRST_NAME, adminFirstName);
            config.setProperty(CoreConstants.LAB_ADMIN_LAST_NAME, adminLastName);
            config.setProperty(CoreConstants.LAB_ADMIN_EMAIL, adminEmail);
            config.setProperty(CoreConstants.LAB_ADMIN_INSTITUTION, adminInstitution);
            config.setProperty(CoreConstants.LAB_ADMIN_PHONE, adminPhone);
            config.setProperty(CoreConstants.LAB_ADMIN_PASS, adminPassword);
            config.setProperty(CoreConstants.VO_NAME, voName);
            config.setProperty(CoreConstants.VO_ROOT, voRoot);
            config.setProperty(CoreConstants.LAB_MYPROXY_HOST, myProxyHost);
            config.setProperty(CoreConstants.LAB_MYPROXY_PORT, myProxyPort);
            config.setProperty(CoreConstants.LAB_MYPROXY_USER, myProxyUser);
            config.setProperty(CoreConstants.LAB_MYPROXY_PASS, myProxyPass);
            config.setProperty(CoreConstants.LAB_MYPROXY_LIFETIME, myProxyLifeTime);
            config.setProperty(CoreConstants.LAB_MYPROXY_MIN_HOURS, myProxyMinHours);
            config.setProperty(CoreConstants.LAB_SMA_HOST, SMAHost);
            config.setProperty(CoreConstants.LAB_SMA_PORT, SMAPort);
            config.setProperty(CoreConstants.LAB_GRIDA_HOST, gridaHost);
            config.setProperty(CoreConstants.LAB_GRIDA_PORT, gridaPort);
            config.setProperty(CoreConstants.LAB_DATA_USERS_HOME, dataManagerUsersHome);
            config.setProperty(CoreConstants.LAB_DATA_GROUPS_HOME, dataManagerGroupsHome);
            config.setProperty(CoreConstants.LAB_DATA_PATH, dataManagerPath);
            config.setProperty(CoreConstants.LAB_DATA_ALT_USERS_HOME, altDataManagerUsersHome);
            config.setProperty(CoreConstants.LAB_DATA_ALT_GROUPS_HOME, altDataManagerGroupsHome);
            config.setProperty(CoreConstants.LAB_TRUSTSTORE_FILE, truststoreFile);
            config.setProperty(CoreConstants.LAB_TRUSTSTORE_PASS, truststorePass);
            config.setProperty(CoreConstants.LAB_SIMULATION_PLATFORM_MAX, maxPlatformRunningSimulations);

            config.setProperty(CoreConstants.LAB_SIMULATION_FOLDER, workflowsPath);
            config.setProperty(CoreConstants.LAB_SIMULATION_DB_HOST, workflowsHost);
            config.setProperty(CoreConstants.LAB_APACHE_HOST, apacheHost);
            config.setProperty(CoreConstants.LAB_APACHE_SSL_PORT, apacheSSLPort);
            config.setProperty(CoreConstants.LAB_CAS_URL, casURL);
            config.setProperty(CoreConstants.SSH_PUBLIC_KEY, sshPublicKey);

            config.setProperty(CoreConstants.APPLICATION_FILES_REPOSITORY, applicationImporterFileRepository);
            config.setProperty(CoreConstants.APP_IMPORTER_ROOT_FOLDER, applicationImporterRootFolder);
            config.setProperty(CoreConstants.APP_REQUIREMENTS, applicationImporterRequirements);
            config.setProperty(CoreConstants.APPLET_GATELAB_CLASSES, appletGateLabClasses);
            config.setProperty(CoreConstants.APPLET_GATELABTEST_CLASSES, appletGateLabTestClasses);
            config.setProperty(CoreConstants.UNDESIRED_MAIL_DOMAINS, undesiredMailDomains);
            config.setProperty(CoreConstants.UNDESIRED_COUNTRIES, undesiredCountries);
            config.setProperty(CoreConstants.PUB_MONTHS_UPDATES, numberMonthsToTestLastPublicationUpdates);
            config.setProperty(CoreConstants.PUBLICATION_SYSTEM_COMMAND, publicationCommandLine);
            config.setProperty(
                CoreConstants.GIRDER_TOKEN_DURATION_IN_DAYS,
                girderTokenDurationInDays);

            config.save();

        } catch (ConfigurationException ex) {
            logger.error("Error configuring server", ex);
        }
    }

    private String setPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    @Override
    public String getConfigurationFolder() {
        return configurationFolder;
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
        return serverProxy;
    }

    @Override
    public String getServerProxy(String vo) {
        return getServerProxyFolder(vo) + "x509up_server";
    }

    @Override
    public String getServerProxyFolder(String vo) {
        return setPath(configurationFolder + PROXIES_DIR + vo + "/");
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
        logger.info("Getting trusted certificate for issuer "+issuer);
        String result = config.getString(CoreConstants.SAML_TRUSTED_CERTIFICATE+"."+issuer);
        logger.info("Returning "+result);
        return result;
    }

    @Override
    public String getSAMLAccountType(String issuer) {
        logger.info("Getting account type for issuer "+issuer);
        String result = config.getString(CoreConstants.SAML_ACCOUNT_TYPE+"."+issuer);
        logger.info("Returning "+result);
        return result;
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
        this.maxPlatformRunningSimulations = maxPlatformRunningSimulations;
        config.setProperty(CoreConstants.LAB_SIMULATION_PLATFORM_MAX, maxPlatformRunningSimulations);
        config.save();
    }

    @Override
    public float getGirderTokenDurationInDays() {
        return girderTokenDurationInDays;
    }
}
