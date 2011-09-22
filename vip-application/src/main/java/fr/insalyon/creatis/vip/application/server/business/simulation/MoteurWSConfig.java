package fr.insalyon.creatis.vip.application.server.business.simulation;

import java.rmi.RemoteException;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import java.io.InputStream;
import java.util.List;
import javax.xml.rpc.ServiceException;
import localhost.moteur_service_wsdl.Moteur_ServiceLocator;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

/**
 *
 * @author Ibrahim kallel
 */
public class MoteurWSConfig extends WorkflowMoteurConfig {
// URI adress of the Moteur Web service */

    private String addressWS;

    public String getAddressWS() {
        return addressWS;
    }

    public void setAddressWS(String addressWS) {
        this.addressWS = addressWS;
    }

    public MoteurWSConfig() {
    }

    public MoteurWSConfig(String workflowPath, List<ParameterSweep> parameters) {
        super(workflowPath, parameters);
    }

    /**
     *  Call the WS that is going to run the workflow and return the HTTP
     *  link that can be used to monitor the workflow status.
     * @return the HTTP link that shows the workflow current status
     * @throws ServiceException
     * @throws RemoteException
     * @throws VlException
     */
    public String launch(String proxyFileName, String userDN) throws RemoteException, ServiceException {

        System.setProperty("javax.net.ssl.trustStore", ServerConfiguration.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", ServerConfiguration.getInstance().getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        // String settings = "This is going to contain settings...";
        // Get Proxy from current User's context
        String strProxy = ProxyUtil.readAsString(proxyFileName);

        //configure WS with the specific wsdd file (fix pb of conflict with grid service)
        String resourcename = "moteur-client-config.wsdd";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcename);
        EngineConfiguration engineConfig = new FileProvider(is);
        Moteur_ServiceLocator wfS = new Moteur_ServiceLocator(addressWS, engineConfig);

        return wfS.getmoteur_service().workflowSubmit(contentXMLworkflow, contentXMLInput.toString(), strProxy, _settings);
    }

    /**
     *
     * @param workflowID
     * @throws RemoteException
     * @throws ServiceException
     */
    public void kill(String workflowID) throws RemoteException, ServiceException {

        System.setProperty("javax.net.ssl.trustStore", ServerConfiguration.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", ServerConfiguration.getInstance().getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String resourcename = "moteur-client-config.wsdd";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcename);
        EngineConfiguration engineConfig = new FileProvider(is);
        Moteur_ServiceLocator wfS = new Moteur_ServiceLocator(addressWS, engineConfig);

        wfS.getmoteur_service().killWorkflow(workflowID);
    }

    /**
     *
     * @param workflowID
     * @throws RemoteException
     * @throws ServiceException
     */
    public String getStatus(String workflowID) throws RemoteException, ServiceException {

        System.setProperty("javax.net.ssl.trustStore", ServerConfiguration.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", ServerConfiguration.getInstance().getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String resourcename = "moteur-client-config.wsdd";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcename);
        EngineConfiguration engineConfig = new FileProvider(is);
        Moteur_ServiceLocator wfS = new Moteur_ServiceLocator(addressWS, engineConfig);

        return wfS.getmoteur_service().getWorkflowStatus(workflowID);
    }
}
