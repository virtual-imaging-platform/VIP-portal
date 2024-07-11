package fr.insalyon.creatis.vip.core.server.business;

import java.util.HashMap;
import java.util.List;

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

    String getGRIDAHost();

    int getGRIDAPort();

    String getWorkflowsHost();

    String getWorkflowsPath();

    String getApacheHost();

    int getApacheSSLPort();

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

    String getSshPublicKey();

    String getSamlTrustedCertificate(String issuer);

    String getSAMLDefaultGroup(String issuer);

    String getApplicationImporterFileRepository();

    String getApplicationImporterRootFolder();

    List<String> getApplicationImporterRequirements();

    HashMap<String, Integer> getReservedClasses();

    List<String> getUndesiredMailDomains();

    List<String> getUndesiredCountries();

    int getMaxPlatformRunningSimulations();

    int getNumberMonthsToTestLastPublicationUpdates();

    String getPublicationCommandLine();

    float getGirderTokenDurationInDays();

    boolean useLocalFilesInInputs();

    int getApiParallelDownloadNb();

    String getReproVIPRootDir();

    boolean useMoteurlite();
}
