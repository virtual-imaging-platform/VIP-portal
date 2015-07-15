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
package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import localhost.moteur_service_wsdl.Moteur_ServiceLocator;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

/**
 *
 * @author Rafael Ferreira da Silva, Ibrahim kallel
 */
public class WebServiceEngine extends WorkflowEngineInstantiator {

    // URI address of the Moteur Web service
    private String addressWS;
    // settings to send to the web service.
    private String settings;

    public String getAddressWS() {
        return addressWS;
    }

    public void setAddressWS(String addressWS) {
        this.addressWS = addressWS;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public WebServiceEngine() {
        this(null, null);
    }

    WebServiceEngine(File workflow, List<ParameterSweep> parameters) {
        super(workflow, parameters);
        mode = "ws";
    }

    /**
     * Call the WS that is going to run the workflow and return the HTTP link
     * that can be used to monitor the workflow status.
     *
     * @return the HTTP link that shows the workflow current status
     * @throws ServiceException
     * @throws RemoteException
     * @throws VlException
     */
    @Override
    public String launch(String proxyFileName, String userDN)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        System.setProperty("javax.net.ssl.trustStore", Server.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", Server.getInstance().getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        // String settings = "This is going to contain settings...";
        // Get Proxy from current User's context
        String strProxy = ProxyUtil.readAsString(proxyFileName);

        //configure WS with the specific wsdd file (fix pb of conflict with grid service)
        String resourcename = "moteur-client-config.wsdd";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcename);
        EngineConfiguration engineConfig = new FileProvider(is);
        Moteur_ServiceLocator wfS = new Moteur_ServiceLocator(addressWS, engineConfig);

        return wfS.getmoteur_service().workflowSubmit(workflow, input, strProxy, settings);
    }

    /**
     *
     * @param workflowID
     * @throws RemoteException
     * @throws ServiceException
     */
    @Override
    public void kill(String workflowID)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        System.setProperty("javax.net.ssl.trustStore", Server.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", Server.getInstance().getTruststorePass());
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
    @Override
    public SimulationStatus getStatus(String workflowID)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        System.setProperty("javax.net.ssl.trustStore", Server.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", Server.getInstance().getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String resourcename = "moteur-client-config.wsdd";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcename);
        EngineConfiguration engineConfig = new FileProvider(is);
        Moteur_ServiceLocator wfS = new Moteur_ServiceLocator(addressWS, engineConfig);

        String workflowStatus = wfS.getmoteur_service().getWorkflowStatus(workflowID);
        MoteurStatus moteurStatus = MoteurStatus.valueOf(workflowStatus);
        switch (moteurStatus) {

            case RUNNING:
                return SimulationStatus.Running;
            case COMPLETE:
                return SimulationStatus.Completed;
            case TERMINATED:
                return SimulationStatus.Killed;
            case UNKNOWN:
                return SimulationStatus.Unknown;
            default:
                return SimulationStatus.Unknown;
        }
    }

    static enum MoteurStatus {

        RUNNING, COMPLETE, TERMINATED, UNKNOWN
    };
}
