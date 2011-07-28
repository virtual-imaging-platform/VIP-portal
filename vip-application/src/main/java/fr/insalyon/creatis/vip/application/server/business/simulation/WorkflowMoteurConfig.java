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

import fr.insalyon.creatis.vip.application.server.business.util.FileUtil;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;
import javax.xml.rpc.ServiceException;
import localhost.moteur_service_wsdl.Moteur_ServiceLocator;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

/**
 *
 * @author Rafael Silva
 * @author kboulebiar
 */
public class WorkflowMoteurConfig {

    // content of the xml file that describe the workflow (read on a file) */
    String contentXMLworkflow;
    // content of the input for the workflow (generated depending of the user)*/
    StringBuffer contentXMLInput;
    // URI adress of the Moteur Web service */
    String addressWS;
    // settings to send to the web service.
    String _settings = "";

    /**
     * 
     * @param URIaddrWS URI Adress of the web service
     */
    public WorkflowMoteurConfig(String URIaddrWS) {
        addressWS = URIaddrWS;
    }
    /**
     * 
     * @param URIaddrWS URI Adress of the web service
     * @param workflowPath Path of the xml worflow descriptor file
     * @param parameters List of parameters
     */
    public WorkflowMoteurConfig(String URIaddrWS, String workflowPath, List<ParameterSweep> parameters) {

        addressWS = URIaddrWS;

        //read the workflow config file
        contentXMLworkflow = FileUtil.read(workflowPath);

        //generate the xml input file according to the user input on the GUI
        contentXMLInput = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        contentXMLInput.append("<d:inputdata>\n");

        for (ParameterSweep p : parameters) {
            contentXMLInput.append("\t<d:source name=\"" + p.getParameterName() + "\"  tag=\"Group\">\n");

            for (String value : p.getValues()) {
                contentXMLInput.append("\t\t<d:item>" + value + "</d:item>\n");
            }
            contentXMLInput.append("\t</d:source>\n");
        }
        contentXMLInput.append("</d:inputdata>\n");
    }

    /**
     *  Call the WS that is going to run the workflow and return the HTTP
     *  link that can be used to monitor the workflow status.
     * @return the HTTP link that shows the workflow current status
     * @throws ServiceException
     * @throws RemoteException
     * @throws VlException
     */
    public String launch(String proxyFileName) throws RemoteException, ServiceException {
        
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

    public String getContentXMLworkflow() {
        return contentXMLworkflow;
    }

    public void setContentXMLworkflow(String contentXMLworkflow) {
        this.contentXMLworkflow = contentXMLworkflow;
    }

    public String getContentXMLInput() {
        return contentXMLInput.toString();
    }

    public void setContentXMLInput(String contentXMLInput) {
        this.contentXMLInput = new StringBuffer(contentXMLInput);
    }

    public String getAddressWS() {
        return addressWS;
    }

    public void setAddressWS(String addressWS) {
        this.addressWS = addressWS;
    }

    public String getSettings() {
        return _settings;
    }

    public void setSettings(String settings) {
        _settings = settings;
    }
}
