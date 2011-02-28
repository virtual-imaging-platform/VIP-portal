/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Rafael Silva
 */
public class ServerConfiguration {

    private static ServerConfiguration instance;
    private final String CONF_FILE = "settings.conf";
    private final String PROXIES_DIR = "proxies/";
    private String confDirPath = "";
    private String workflowsPath = "/var/www/html/workflows";
    private String workflowsDB = "/var/www/workflows.db";
    private String adminDN = "/O=GRID-FR/C=FR/O=CNRS/OU=CREATIS/CN=Rafael Silva";
    private String proxiesDir;
    private String quickStartURL = "http://www.creatis.insa-lyon.fr/~glatard/grid/index.html";
    // Vlet Agent
    private String vletagentHost = "kingkong.grid.creatis.insa-lyon.fr";
    private int vletagentPort = 9006;
    // Moteur
    private String moteurServer = "https://kingkong.grid.creatis.insa-lyon.fr:9010/cgi-bin/moteurServer/moteur_server";
    // Apache
    private String apacheHost = "kingkong.grid.creatis.insa-lyon.fr";
    private int apacheSSLPort = 80;
    // MyProxy
    private String myProxyHost = "kingkong.grid.creatis.insa-lyon.fr";
    private int myProxyPort = 9011;
    // KeyStore
    private String keystoreFile = "/usr/local/apache-tomcat-6.0.29/conf/keystore.jks";
    private String keystorePass = "";
    private String truststoreFile = "/usr/local/apache-tomcat-6.0.29/conf/truststore.jks";
    private String truststorePass = "";

    public static ServerConfiguration getInstance() {
        if (instance == null) {
            instance = new ServerConfiguration();
        }
        return instance;
    }

    private ServerConfiguration() {

        // Configuration Directory
        confDirPath = System.getenv("HOME") + "/.platform/";
        File cDir = new File(confDirPath);
        if (!cDir.exists()) {
            cDir.mkdir();
        }
        String confFilePath = confDirPath + CONF_FILE;

        // Proxies Directory
        proxiesDir = confDirPath + PROXIES_DIR;
        File pDir = new File(proxiesDir);
        if (!pDir.exists()) {
            pDir.mkdir();
        }

        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(confFilePath));

            workflowsPath = prop.getProperty("workflows.directory", workflowsPath);
            workflowsDB = prop.getProperty("workflows.database", workflowsDB);
            vletagentHost = prop.getProperty("vletagent.host", vletagentHost);
            vletagentPort = new Integer(prop.getProperty("vletagent.port", vletagentPort + ""));
            moteurServer = prop.getProperty("moteur.host", moteurServer);
            adminDN = prop.getProperty("admin.dn", adminDN);
            adminDN = prop.getProperty("admin.dn", adminDN);
            myProxyHost = prop.getProperty("myproxy.host", myProxyHost);
            myProxyPort = new Integer(prop.getProperty("myproxy.port", myProxyPort + ""));
            keystoreFile = prop.getProperty("keystore.file", keystoreFile);
            keystorePass = prop.getProperty("keystore.password", keystorePass);
            truststoreFile = prop.getProperty("truststore.file", truststoreFile);
            truststorePass = prop.getProperty("truststore.password", truststorePass);
            apacheHost = prop.getProperty("apache.host", apacheHost);
            apacheSSLPort = new Integer(prop.getProperty("apache.ssl.port", apacheSSLPort + ""));
            quickStartURL = prop.getProperty("quickstart.url", quickStartURL);

        } catch (IOException e) {

            try {
                prop.setProperty("workflows.directory", workflowsPath);
                prop.setProperty("workflows.database", workflowsDB);
                prop.setProperty("vletagent.host", vletagentHost);
                prop.setProperty("vletagent.port", vletagentPort + "");
                prop.setProperty("moteur.host", moteurServer);
                prop.setProperty("admin.dn", adminDN);
                prop.setProperty("myproxy.host", myProxyHost);
                prop.setProperty("myproxy.port", myProxyPort + "");
                prop.setProperty("keystore.file", keystoreFile);
                prop.setProperty("keystore.password", keystorePass);
                prop.setProperty("truststore.file", truststoreFile);
                prop.setProperty("truststore.password", truststorePass);
                prop.setProperty("apache.host", apacheHost);
                prop.setProperty("apache.ssl.port", apacheSSLPort + "");
                prop.setProperty("quickstart.url", quickStartURL);

                prop.store(new FileOutputStream(confFilePath), "");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

    public String getQuickStartURL() {
        return quickStartURL;
    }
}
