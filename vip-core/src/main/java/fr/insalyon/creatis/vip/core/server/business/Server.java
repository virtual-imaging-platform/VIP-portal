package fr.insalyon.creatis.vip.core.server.business;

import org.apache.commons.configuration.ConfigurationException;

import java.util.HashMap;
import java.util.List;

public interface Server {

    String VIP_DIR = "/.vip/";
    String CONF_FILE = "vip.conf";

    String getDatabaseServerHost();

    int getDatabaseServerPort();

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

    String getMoteurServer();

    String getGRIDAHost();

    int getGRIDAPort();

    String getN4uGridaHost();

    int getN4uGridaPort();

    String getWorkflowsHost();

    int getWorkflowsPort();

    String getWorkflowsPath();

    String getWorflowsExecMode();

    String getApacheHost();

    int getApacheSSLPort();

    String getSMAHost();

    int getSMAPort();

    String getDataManagerPath();

    String getDataManagerLFCHost();

    int getDataManagerLFCPort();

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

    String getAdminPhone();

    String getCasURL();

    String getSshPublicKey();

    String getSamlTrustedCertificate(String issuer);

    String getSAMLAccountType(String issuer);

    String getApplicationImporterFileRepository();

    String getApplicationImporterRootFolder();

    List<String> getApplicationImporterRequirements();

    String getDeleteFilesAfterUpload();

    List<String> getAppletGateLabClasses();

    void setAppletGateLabClasses(List<String> appletGateLabClasses);

    List<String> getAppletGateLabTestClasses();

    void setAppletGateLabTestClasses(List<String> appletGateLabTestClasses);

    HashMap<String, Integer> getReservedClasses();

    List<String> getUndesiredMailDomains();

    List<String> getUndesiredCountries();

    int getMaxPlatformRunningSimulations();

    int getNumberMonthsToTestLastPublicationUpdates();

    String getPublicationCommandLine();

    void setMaxPlatformRunningSimulations(int maxPlatformRunningSimulations) throws ConfigurationException;

    float getGirderTokenDurationInDays();
}
