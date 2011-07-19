/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
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
package fr.insalyon.creatis.vip.common.server;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class ServerConfiguration {

    private static Logger logger = Logger.getLogger(ServerConfiguration.class);
    private static ServerConfiguration instance;
    private final String CONF_FILE = "settings.conf";
    private final String PROXIES_DIR = "proxies/";
    private String confDirPath = "";
    private String adminDN = "/O=GRID-FR/C=FR/O=CNRS/OU=CREATIS/CN=Rafael Silva";
    private String proxiesDir;
    // Workflows
    private String workflowsPath = "/var/www/html/workflows";
    private String workflowsDB = "/var/www/workflows.db";
    private String workflowsHost = "localhost";
    private int workflowsPort = 1527;
    // Vlet Agent
    private String vletagentHost = "localhost";
    private int vletagentPort = 9006;
    // Moteur
    private String moteurServer = "https://localhost:443/cgi-bin/moteurServer/moteur_server";
    // Apache
    private String apacheHost = "localhost";
    private int apacheSSLPort = 80;
    // MyProxy
    private String myProxyHost = "localhost";
    private int myProxyPort = 7211;
    // KeyStore
    private String keystoreFile = "/usr/local/apache-tomcat-6.0.29/conf/keystore.jks";
    private String keystorePass = "";
    private String truststoreFile = "/usr/local/apache-tomcat-6.0.29/conf/truststore.jks";
    private String truststorePass = "";
    // Mail
    private String mailSmtpHost = "";
    private String mailFrom = "";
    private String[] newsRecipients = new String[]{};
    // Data Management
    private String dataManagerPath = "/tmp";
    private String dataManagerLFCHost = "lfc-biomed.in2p3.fr";
    private int dataManagerLFCPort = 5010;
    private String dataManagerUsersHome = "/grid/biomed/creatis/vip/data/users";
    private String dataManagerGroupsHome = "/grid/biomed/creatis/vip/data/groups";
    // Provenance
    private String provenanceDBUser = "vip";
    private String provenanceDBPass = "";
    private String provenanceDBURL = "jdbc:mysql://localhost:3306/SDB2";

    public static ServerConfiguration getInstance() {
        if (instance == null) {
            instance = new ServerConfiguration();
        }
        return instance;
    }

    private ServerConfiguration() {

        try {
            // Proxies Directory
            confDirPath = System.getenv("HOME") + "/.platform/";
            proxiesDir = confDirPath + PROXIES_DIR;
            File pDir = new File(proxiesDir);
            if (!pDir.exists()) {
                pDir.mkdir();
            }

            String confFilePath = confDirPath + CONF_FILE;
            PropertiesConfiguration config = new PropertiesConfiguration(confFilePath);

            workflowsPath = config.getString("workflows.directory", workflowsPath);
            workflowsDB = config.getString("workflows.db.name", workflowsDB);
            workflowsHost = config.getString("workflows.db.host", workflowsHost);
            workflowsPort = config.getInt("workflows.db.port", workflowsPort);
            vletagentHost = config.getString("vletagent.host", vletagentHost);
            vletagentPort = config.getInt("vletagent.port", vletagentPort);
            moteurServer = config.getString("moteur.host", moteurServer);
            adminDN = config.getString("admin.dn", adminDN);
            myProxyHost = config.getString("myproxy.host", myProxyHost);
            myProxyPort = config.getInt("myproxy.port", myProxyPort);
            keystoreFile = config.getString("keystore.file", keystoreFile);
            keystorePass = config.getString("keystore.password", keystorePass);
            truststoreFile = config.getString("truststore.file", truststoreFile);
            truststorePass = config.getString("truststore.password", truststorePass);
            apacheHost = config.getString("apache.host", apacheHost);
            apacheSSLPort = config.getInt("apache.ssl.port", apacheSSLPort);
            mailSmtpHost = config.getString("mail.smtp.host", mailSmtpHost);
            mailFrom = config.getString("mail.from", mailFrom);
            newsRecipients = config.getStringArray("news.recipients");
            dataManagerPath = config.getString("datamanager.path", dataManagerPath);
            dataManagerLFCHost = config.getString("datamanager.lfc.host", dataManagerLFCHost);
            dataManagerLFCPort = config.getInt("datamanager.lfc.port", dataManagerLFCPort);
            dataManagerUsersHome = config.getString("datamanager.users.home", dataManagerUsersHome);
            dataManagerGroupsHome = config.getString("datamanager.groups.home", dataManagerGroupsHome);
            provenanceDBUser = config.getString("provenance.db.user", provenanceDBUser);
            provenanceDBPass = config.getString("provenance.db.pass", provenanceDBPass);
            provenanceDBURL = config.getString("provenance.db.url", provenanceDBURL);
            
            config.save();

        } catch (ConfigurationException ex) {
            logger.error(ex);
        }
    }

    public String getConfDirPath() {
        return confDirPath;
    }

    public String getMoteurServer() {
        return moteurServer;
    }

    public String getVletagentHost() {
        return vletagentHost;
    }

    public int getVletagentPort() {
        return vletagentPort;
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

    public String getAdminDN() {
        return adminDN;
    }

    public String getMyProxyHost() {
        return myProxyHost;
    }

    public int getMyProxyPort() {
        return myProxyPort;
    }

    public String getKeystoreFile() {
        return keystoreFile;
    }

    public String getKeystorePass() {
        return keystorePass;
    }

    public String getProxiesDir() {
        return proxiesDir;
    }

    public String getApacheHost() {
        return apacheHost;
    }

    public int getApacheSSLPort() {
        return apacheSSLPort;
    }

    public String getTruststoreFile() {
        return truststoreFile;
    }

    public String getTruststorePass() {
        return truststorePass;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public String[] getNewsRecipients() {
        return newsRecipients;
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
}
