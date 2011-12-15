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
package fr.insalyon.creatis.vip.core.server.business;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
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
    // Admin
    private String adminFirstName = "Administrator";
    private String adminLastName = "";
    private String adminEmail = "admin@vip.creatis.insa-lyon.fr";
    private String adminInstitution = "";
    private String adminPhone = "";
    private String adminPassword = "admin";
    // MyProxy
    private String myProxyHost = "localhost";
    private int myProxyPort = 7211;
    private String myProxyUser = "";
    private String myProxyPass = "";
    // Mail
    private String mailHost = "";
    private String mailTransportProtocol = "";
    private String mailFrom = "";
    // GRIDA server
    private String gridaHost = "localhost";
    private int gridaPort = 9006;
    // Data Manager
    private String dataManagerUsersHome = "/users";
    private String dataManagerGroupsHome = "/groups";
    // Workflows
    private String workflowsPath = "/var/www/html/workflows";
    private String workflowsDB = "/var/www/workflows.db";
    private String workflowsHost = "localhost";
    private int workflowsPort = 1527;
    // Moteur
    private String moteurServer = "https://localhost:443/cgi-bin/moteurServer/moteur_server";
    private int moteurMaxWorkflows = 5;
    private String truststoreFile = "/usr/local/apache-tomcat-6.0.29/conf/truststore.jks";
    private String truststorePass = "";
    // Apache
    private String apacheHost = "localhost";
    private int apacheSSLPort = 80;
    // News
    private String[] newsRecipients = new String[]{};
    // Data Manager
    private String dataManagerPath = "/tmp";
    private String dataManagerLFCHost = "lfc-biomed.in2p3.fr";
    private int dataManagerLFCPort = 5010;
    // Provenance
    private String provenanceDBUser = "vip";
    private String provenanceDBPass = "";
    private String provenanceDBURL = "jdbc:mysql://localhost:3306/SDB2";

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

            adminFirstName = config.getString("admin.first.name", adminFirstName);
            adminLastName = config.getString("admin.last.name", adminLastName);
            adminEmail = config.getString("admin.email", adminEmail);
            adminInstitution = config.getString("admin.institution", adminInstitution);
            adminPhone = config.getString("admin.phone", adminPhone);
            adminPassword = config.getString("admin.pass", adminPassword);

            myProxyHost = config.getString("myproxy.host", myProxyHost);
            myProxyPort = config.getInt("myproxy.port", myProxyPort);
            myProxyUser = config.getString("myproxy.user", myProxyUser);
            myProxyPass = config.getString("myproxy.pass", myProxyPass);

            mailHost = config.getString("mail.host", mailHost);
            mailTransportProtocol = config.getString("mail.transport.protocol", mailTransportProtocol);
            mailFrom = config.getString("mail.from", mailFrom);

            gridaHost = config.getString("grida.server.host", gridaHost);
            gridaPort = config.getInt("grida.server.port", gridaPort);

            dataManagerUsersHome = config.getString("datamanager.users.home", dataManagerUsersHome);
            dataManagerGroupsHome = config.getString("datamanager.groups.home", dataManagerGroupsHome);

            moteurServer = config.getString("moteur.host", moteurServer);
            moteurMaxWorkflows = config.getInt("moteur.max.workflows", moteurMaxWorkflows);
            truststoreFile = config.getString("truststore.file", truststoreFile);
            truststorePass = config.getString("truststore.password", truststorePass);

            workflowsPath = config.getString("workflows.directory", workflowsPath);
            workflowsDB = config.getString("workflows.db.name", workflowsDB);
            workflowsHost = config.getString("workflows.db.host", workflowsHost);
            workflowsPort = config.getInt("workflows.db.port", workflowsPort);

            apacheHost = config.getString("apache.host", apacheHost);
            apacheSSLPort = config.getInt("apache.ssl.port", apacheSSLPort);

            newsRecipients = config.getStringArray("news.recipients");

            dataManagerPath = config.getString("datamanager.path", dataManagerPath);
            dataManagerLFCHost = config.getString("datamanager.lfc.host", dataManagerLFCHost);
            dataManagerLFCPort = config.getInt("datamanager.lfc.port", dataManagerLFCPort);

            provenanceDBUser = config.getString("provenance.db.user", provenanceDBUser);
            provenanceDBPass = config.getString("provenance.db.pass", provenanceDBPass);
            provenanceDBURL = config.getString("provenance.db.url", provenanceDBURL);

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

    public String getApacheHost() {
        return apacheHost;
    }

    public int getApacheSSLPort() {
        return apacheSSLPort;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailHost() {
        return mailHost;
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

    public String getTruststoreFile() {
        return truststoreFile;
    }

    public String getTruststorePass() {
        return truststorePass;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
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

    public int getMoteurMaxWorkflows() {
        return moteurMaxWorkflows;
    }
}
