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
package fr.insalyon.creatis.vip.application.server.business.simulation;

import java.rmi.RemoteException;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
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

        return wfS.getmoteur_service().workflowSubmit(contentXMLworkflow, contentXMLInput.toString(), strProxy, _settings);
    }

    /**
     *
     * @param workflowID
     * @throws RemoteException
     * @throws ServiceException
     */
    public void kill(String workflowID) throws RemoteException, ServiceException {

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
    public String getStatus(String workflowID) throws RemoteException, ServiceException {

        System.setProperty("javax.net.ssl.trustStore", Server.getInstance().getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", Server.getInstance().getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String resourcename = "moteur-client-config.wsdd";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcename);
        EngineConfiguration engineConfig = new FileProvider(is);
        Moteur_ServiceLocator wfS = new Moteur_ServiceLocator(addressWS, engineConfig);

        return wfS.getmoteur_service().getWorkflowStatus(workflowID);
    }
}
