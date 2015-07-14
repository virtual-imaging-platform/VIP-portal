/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Server {

    // Constants
    private static final Logger logger = Logger.getLogger(Server.class);
    private static Server instance;
    private final String CONF_FILE = "vip.conf";
    private final String VIP_DIR = "/.vip/";
    private final String PROXIES_DIR = "proxies/";
    // Portal
    private String configurationFolder;
    private String serverProxy;
    // Database
    private String databaseServerHost;
    private int databaseServerPort;
    // Admin
    private String adminFirstName;
    private String adminLastName;
    private String adminEmail;
    private String adminInstitution;
    private String adminPhone;
    private String adminPassword;
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
    private String dataManagerLFCHost;
    private int dataManagerLFCPort;
    // Moteur
    private String moteurServer;
    private String truststoreFile;
    private String truststorePass;
    // Simulations
    private int beginnerMaxRunningSimulations;
    private int advancedMaxRunningSimulations;
    private String workflowsPath;
    private String workflowsDB;
    private String workflowsHost;
    private int workflowsPort;
    private String workflowsExecuctionMode;
    // Apache
    private String apacheHost = "localhost";
    private int apacheSSLPort = 80;
    // Provenance
    private String provenanceDBUser = "vip";
    private String provenanceDBPass = "";
    private String provenanceDBURL = "jdbc:mysql://localhost:3306/SDB2";
    //Simulated data
    private String simulatedDataDBUser = "vip";
    private String simulatedDataDBPass = "vip2011";
    private String simulatedDataDBURL = "jdbc:mysql://localhost:3306vip_simulated_data";
    //cas
    private String casURL;
    private String SAMLDefaultAccountType;
    //ssh
    private String sshPublicKey;
    
    //third-party auth
    private String samlTrustedCertificate;
    private String mozillaPersonaValidationURL;
    //treeQuery
    private String queryTree;
    //N4U
    private String applicationN4uClasse;
    private String applicationFilesRepository;
    private String deleteFilesAfterUpload;

   
    
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private Server() {

        try {
            // Directories
            configurationFolder = setPath(System.getenv("HOME") + VIP_DIR);
            serverProxy = setPath(configurationFolder + PROXIES_DIR) + "/x509up_server";

            // Configuration File
            String confFilePath = configurationFolder + CONF_FILE;
            PropertiesConfiguration config = new PropertiesConfiguration(confFilePath);
            logger.info("Loading config file: "+confFilePath);

            databaseServerHost = config.getString(CoreConstants.LAB_DB_HOST, "localhost");
            databaseServerPort = config.getInt(CoreConstants.LAB_DB_PORT, 9092);

            adminFirstName = config.getString(CoreConstants.LAB_ADMIN_FIRST_NAME, "Administrator");
            adminLastName = config.getString(CoreConstants.LAB_ADMIN_LAST_NAME, "");
            adminEmail = config.getString(CoreConstants.LAB_ADMIN_EMAIL, "admin@vip.creatis.insa-lyon.fr");
            adminInstitution = config.getString(CoreConstants.LAB_ADMIN_INSTITUTION, "");
            adminPhone = config.getString(CoreConstants.LAB_ADMIN_PHONE, "");
            adminPassword = config.getString(CoreConstants.LAB_ADMIN_PASS, "admin");

            myProxyHost = config.getString(CoreConstants.LAB_MYPROXY_HOST, "localhost");
            myProxyPort = config.getInt(CoreConstants.LAB_MYPROXY_PORT, 7211);
            myProxyUser = config.getString(CoreConstants.LAB_MYPROXY_USER, "");
            myProxyPass = config.getString(CoreConstants.LAB_MYPROXY_PASS, "");
            myProxyLifeTime = config.getString(CoreConstants.LAB_MYPROXY_LIFETIME, "86400");
            myProxyMinHours = config.getInt(CoreConstants.LAB_MYPROXY_MIN_HOURS, 12);

            SMAHost = config.getString(CoreConstants.LAB_SMA_HOST, "localhost");
            SMAPort = config.getInt(CoreConstants.LAB_SMA_PORT, 8082);

            gridaHost = config.getString(CoreConstants.LAB_GRIDA_HOST, "localhost");
            gridaPort = config.getInt(CoreConstants.LAB_GRIDA_PORT, 9006);

            dataManagerUsersHome = config.getString(CoreConstants.LAB_DATA_USERS_HOME, "/users");
            dataManagerGroupsHome = config.getString(CoreConstants.LAB_DATA_GROUPS_HOME, "/groups");
            dataManagerPath = config.getString(CoreConstants.LAB_DATA_PATH, "/tmp");
            dataManagerLFCHost = config.getString(CoreConstants.LAB_DATA_LFC_HOST, "lfc-biomed.in2p3.fr");
            dataManagerLFCPort = config.getInt(CoreConstants.LAB_DATA_LFC_PORT, 5010);

            moteurServer = config.getString(CoreConstants.LAB_MOTEUR_HOST, "https://localhost:443/cgi-bin/moteurServer/moteur_server");
            truststoreFile = config.getString(CoreConstants.LAB_TRUSTSTORE_FILE, "/usr/local/apache-tomcat-6.0.29/conf/truststore.jks");
            truststorePass = config.getString(CoreConstants.LAB_TRUSTSTORE_PASS, "");

            beginnerMaxRunningSimulations = config.getInt(CoreConstants.LAB_SIMULATION_BEGINNER_MAX, 1);
            advancedMaxRunningSimulations = config.getInt(CoreConstants.LAB_SIMULATION_ADVANCED_MAX, Integer.MAX_VALUE);
            workflowsPath = config.getString(CoreConstants.LAB_SIMULATION_FOLDER, "/var/www/html/workflows");
            workflowsDB = config.getString(CoreConstants.LAB_SIMULATION_DB_NAME, "/var/www/workflows.db");
            workflowsHost = config.getString(CoreConstants.LAB_SIMULATION_DB_HOST, "localhost");
            workflowsPort = config.getInt(CoreConstants.LAB_SIMULATION_DB_PORT, 1527);
            workflowsExecuctionMode = config.getString(CoreConstants.LAB_SIMULATION_EXEC_MODE, "ws");

            apacheHost = config.getString("apache.host", apacheHost);
            apacheSSLPort = config.getInt("apache.ssl.port", apacheSSLPort);

            provenanceDBUser = config.getString("provenance.db.user", provenanceDBUser);
            provenanceDBPass = config.getString("provenance.db.pass", provenanceDBPass);
            provenanceDBURL = config.getString("provenance.db.url", provenanceDBURL);

            simulatedDataDBUser = config.getString(CoreConstants.LAB_SIMULATED_DATA_DB_USER, simulatedDataDBUser);
            simulatedDataDBPass = config.getString(CoreConstants.LAB_SIMULATED_DATA_DB_PASSWORD, simulatedDataDBPass);
            simulatedDataDBURL = config.getString(CoreConstants.LAB_SIMULATED_DATA_DB_URL, simulatedDataDBURL);

            casURL = config.getString(CoreConstants.LAB_CAS_URL, "https://ng-cas.maatg.fr/pandora-gateway-sl-cas");
            SAMLDefaultAccountType = config.getString(CoreConstants.LAB_SAML_ACCOUNT_TYPE, "Neuroimaging");

            sshPublicKey = config.getString(CoreConstants.SSH_PUBLIC_KEY, "ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAuNjIXlgjuBR+WfjGtkieecZfe/ZL6EyNJTbL14bn3/Soof0kFSshDJvFgSH1hNwMMU1hynLbzcEbLTyVMoGQKfQkq7mJPajy9g8878WCKxCRbXv3W1/HT9iab/qqt2dcRYnDEruHwgyELBhQuMAe2W2/mgjd7Y5PxE01bwDcenYl3cU3iJk1sAOHao6P+3xU6Ov+TD8K9aC0LzZpM+rzAmS9HOZ9nvzERExd7k4TUpyffQV9Dpb5jEnEViF3VHqplB8AbWDdcJbiVkUBUe4hQb7nmWP0kHl1+v5SQJ1B4mWCZ+35Rc/9b1GsmPnXg3qqhjeKbrim/NbcUwKr9NPWjQ== vip-services@kingkong.grid.creatis.insa-lyon.fr");
            samlTrustedCertificate = config.getString(CoreConstants.SAML_TRUSTED_CERTIFICATE, System.getProperty( "user.home" )+File.separator+".vip"+File.separator+"trusted_saml_cert.pem");
            mozillaPersonaValidationURL = config.getString(CoreConstants.MOZILLA_PERSONA_VALIDATION_URL,"https://verifier.login.persona.org/verify");
                    
             //queryTree
            queryTree=config.getString(CoreConstants.TreeQuery,"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> select * from <http://e-ginseng.org/graph/ontology/semEHR> where {?x a rdfs:Class . ?x rdfs:label ?label}");
            
            //N4U_model
            applicationN4uClasse=config.getString(CoreConstants.APP_CLASSE,"Test");
            applicationFilesRepository=config.getString(CoreConstants.APPLICATION_FILES_REPOSITORY,"/home/boujelben");
            deleteFilesAfterUpload=config.getString(CoreConstants.APP_DELETE_FILES_AFTER_UPLOAD,"yes");
          
             
            
            config.setProperty(CoreConstants.LAB_DB_HOST, databaseServerHost);
            config.setProperty(CoreConstants.LAB_DB_PORT, databaseServerPort);
            config.setProperty(CoreConstants.LAB_ADMIN_FIRST_NAME, adminFirstName);
            config.setProperty(CoreConstants.LAB_ADMIN_LAST_NAME, adminLastName);
            config.setProperty(CoreConstants.LAB_ADMIN_EMAIL, adminEmail);
            config.setProperty(CoreConstants.LAB_ADMIN_INSTITUTION, adminInstitution);
            config.setProperty(CoreConstants.LAB_ADMIN_PHONE, adminPhone);
            config.setProperty(CoreConstants.LAB_ADMIN_PASS, adminPassword);
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
            config.setProperty(CoreConstants.LAB_DATA_LFC_HOST, dataManagerLFCHost);
            config.setProperty(CoreConstants.LAB_DATA_LFC_PORT, dataManagerLFCPort);
            config.setProperty(CoreConstants.LAB_MOTEUR_HOST, moteurServer);
            config.setProperty(CoreConstants.LAB_TRUSTSTORE_FILE, truststoreFile);
            config.setProperty(CoreConstants.LAB_TRUSTSTORE_PASS, truststorePass);
            config.setProperty(CoreConstants.LAB_SIMULATION_BEGINNER_MAX, beginnerMaxRunningSimulations);
            config.setProperty(CoreConstants.LAB_SIMULATION_ADVANCED_MAX, advancedMaxRunningSimulations);
            config.setProperty(CoreConstants.LAB_SIMULATION_FOLDER, workflowsPath);
            config.setProperty(CoreConstants.LAB_SIMULATION_DB_NAME, workflowsDB);
            config.setProperty(CoreConstants.LAB_SIMULATION_DB_HOST, workflowsHost);
            config.setProperty(CoreConstants.LAB_SIMULATION_DB_PORT, workflowsPort);
            config.setProperty(CoreConstants.LAB_SIMULATION_EXEC_MODE, workflowsExecuctionMode);
            config.setProperty("apache.host", apacheHost);
            config.setProperty("apache.ssl.port", apacheSSLPort);
            config.setProperty("provenance.db.user", provenanceDBUser);
            config.setProperty("provenance.db.pass", provenanceDBPass);
            config.setProperty("provenance.db.url", provenanceDBURL);
            config.setProperty(CoreConstants.LAB_SIMULATED_DATA_DB_URL, simulatedDataDBUser);
            config.setProperty(CoreConstants.LAB_SIMULATED_DATA_DB_PASSWORD, simulatedDataDBPass);
            config.setProperty(CoreConstants.LAB_SIMULATED_DATA_DB_URL, simulatedDataDBURL);
            config.setProperty(CoreConstants.LAB_CAS_URL, casURL);
            config.setProperty(CoreConstants.LAB_SAML_ACCOUNT_TYPE, SAMLDefaultAccountType);
            config.setProperty(CoreConstants.SSH_PUBLIC_KEY, sshPublicKey);
            config.setProperty(CoreConstants.SAML_TRUSTED_CERTIFICATE, samlTrustedCertificate);
            config.setProperty(CoreConstants.MOZILLA_PERSONA_VALIDATION_URL,mozillaPersonaValidationURL);
            config.setProperty(CoreConstants.TreeQuery, queryTree);
            
            config.setProperty(CoreConstants.APP_CLASSE, applicationN4uClasse);
            config.setProperty(CoreConstants.APPLICATION_FILES_REPOSITORY,applicationFilesRepository);
            config.setProperty(CoreConstants.APP_DELETE_FILES_AFTER_UPLOAD, deleteFilesAfterUpload);
            config.save();

        } catch (ConfigurationException ex) {
            logger.error(ex);
        }
    }

    private String setPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public String getDatabaseServerHost() {
        return databaseServerHost;
    }

    public int getDatabaseServerPort() {
        return databaseServerPort;
    }

    public String getConfigurationFolder() {
        return configurationFolder;
    }

    public String getServerProxy() {
        return serverProxy;
    }

    public String getMyProxyHost() {
        return myProxyHost;
    }

    public int getMyProxyPort() {
        return myProxyPort;
    }

    public String getMyProxyPass() {
        return myProxyPass;
    }

    public String getMyProxyUser() {
        return myProxyUser;
    }

    public String getMyProxyLifeTime() {
        return myProxyLifeTime;
    }

    public int getMyProxyMinHours() {
        return myProxyMinHours;
    }

    public String getMoteurServer() {
        return moteurServer;
    }

    public String getGRIDAHost() {
        return gridaHost;
    }

    public int getGRIDAPort() {
        return gridaPort;
    }

    public String getWorkflowsDB() {
        return workflowsDB;
    }

    public String getWorkflowsHost() {
        return workflowsHost;
    }

    public int getWorkflowsPort() {
        return workflowsPort;
    }

    public String getWorkflowsPath() {
        return workflowsPath;
    }

    public String getWorflowsExecMode() {
        return workflowsExecuctionMode;
    }

    public String getApacheHost() {
        return apacheHost;
    }

    public int getApacheSSLPort() {
        return apacheSSLPort;
    }

    public String getSMAHost() {
        return SMAHost;
    }

    public int getSMAPort() {
        return SMAPort;
    }

    public String getDataManagerPath() {
        return dataManagerPath;
    }

    public String getDataManagerLFCHost() {
        return dataManagerLFCHost;
    }

    public int getDataManagerLFCPort() {
        return dataManagerLFCPort;
    }

    public String getDataManagerUsersHome() {
        return dataManagerUsersHome;
    }

    public String getDataManagerGroupsHome() {
        return dataManagerGroupsHome;
    }

    public String getProvenanceDBPass() {
        return provenanceDBPass;
    }

    public String getProvenanceDBURL() {
        return provenanceDBURL;
    }

    public String getProvenanceDBUser() {
        return provenanceDBUser;
    }

    public String getTruststoreFile() {
        return truststoreFile;
    }

    public String getTruststorePass() {
        return truststorePass;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminFirstName() {
        return adminFirstName;
    }

    public String getAdminInstitution() {
        return adminInstitution;
    }

    public String getAdminLastName() {
        return adminLastName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public String getCasURL() {
        return casURL;
    }

    public String getSAMLDefaultAccountType() {
        return SAMLDefaultAccountType;
    }

    public String getSimulatedDataDBPass() {
        return simulatedDataDBPass;
    }

    public String getSimulatedDataDBURL() {
        return simulatedDataDBURL;
    }

    public String getSimulatedDataDBUser() {
        return simulatedDataDBUser;
    }

    public String getSshPublicKey() {
        return sshPublicKey;
    }

    public String getSamlTrustedCertificate() {
        return samlTrustedCertificate;
    }

    public String getMozillaPersonaValidationURL() {
        return mozillaPersonaValidationURL;
    }
    
     public String getQueryTree() {
        return queryTree;
    }
     
     
    public String getApplicationN4uClasse() {
        return applicationN4uClasse;
    }

    public String getApplicationFilesRepository() {
        return applicationFilesRepository;
    }

    public String getDeleteFilesAfterUpload() {
        return deleteFilesAfterUpload;
    }
  
}
