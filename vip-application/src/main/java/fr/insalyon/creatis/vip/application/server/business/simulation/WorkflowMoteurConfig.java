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
import java.rmi.RemoteException;
import java.util.List;
import javax.xml.rpc.ServiceException;

/**
 *
 * @author Rafael Silva
 * @author kboulebiar
 */
public abstract class WorkflowMoteurConfig {

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

    public WorkflowMoteurConfig() {
    }

    /**
     * 
     * @param URIaddrWS URI Adress of the web service
     * @param workflowPath Path of the xml worflow descriptor file
     * @param parameters List of parameters
     */
    public WorkflowMoteurConfig(String workflowPath, List<ParameterSweep> parameters) {

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

    public abstract String launch(String proxyFileName, String userDN) throws RemoteException, ServiceException;

    public abstract void kill(String workflowID) throws RemoteException, ServiceException;

    public abstract String getStatus(String workflowID) throws RemoteException, ServiceException;

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

    public String getSettings() {
        return _settings;
    }

    public void setSettings(String settings) {
        _settings = settings;
    }
}
