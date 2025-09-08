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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

@Service
public class WorkflowExecutionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private WorkflowEngineInstantiator engine;


    @Autowired
    public WorkflowExecutionBusiness(Server server, WorkflowEngineInstantiator engine) {
        this.server = server;
        this.engine = engine;
    }

    public Workflow launch(String engineEndpoint, AppVersion appVersion, User user, String simulationName,
            List<ParameterSweep> parameters, String executorConfig) throws BusinessException {

        try {
            String workflowContent = appVersion.getDescriptor();
            String inputs = (parameters != null) ? getParametersAsXMLInput(parameters) : null;
            String proxyFileName = server.getServerProxy(server.getVoName());
            String settingsJSON = new ObjectMapper().writeValueAsString(appVersion.getSettings());
            System.err.println(settingsJSON);
            String workflowID = engine.launch(engineEndpoint, workflowContent, inputs, settingsJSON, executorConfig, proxyFileName);
            return new Workflow(workflowID, user.getFullName(),
                    WorkflowStatus.Running, new Date(), null, simulationName, 
                    appVersion.getApplicationName(), appVersion.getVersion(), "",
                    engineEndpoint, null);

        } catch (ServiceException | RemoteException | JsonProcessingException ex) {
            logger.error("Error launching simulation {} ({}/{})",
                    simulationName, appVersion.getApplicationName(), appVersion.getVersion(), ex);
            throw new BusinessException(ex);
        }
    }

    public SimulationStatus getStatus(String engineEndpoint, String simulationID) throws BusinessException {
        SimulationStatus status = SimulationStatus.Unknown;
        try {
            status = engine.getStatus(engineEndpoint, simulationID);
        } catch (RemoteException | ServiceException e) {
            logger.error("Error getting status of simulation {} on engine {}", simulationID, engineEndpoint, e);
            throw new BusinessException(e);
        }

        return status;
    }

    public void kill(String engineEndpoint, String simulationID) throws BusinessException {
        try {
            engine.kill(engineEndpoint, simulationID);
        } catch (RemoteException | ServiceException e) {
            logger.error("Error killing simulation {} on engine {}", simulationID, engineEndpoint, e);
            throw new BusinessException(e);
        }
    }

    public String getParametersAsXMLInput(List<ParameterSweep> parameters) {

        //generate the xml input file according to the user input on the GUI
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<inputdata>\n");

        for (ParameterSweep parameter : parameters) {

            xml.append("\t<source name=\"")
                    .append(parameter.getParameterName())
                    .append("\"  type=\"String\">\n")
                    .append("<array>\n");

            int counter = 0;
            for (String value : parameter.getValues()) {


                xml.append("\t\t<item>")
                        .append("<tag name=\"Group\" value=\"")
                        .append(counter)
                        .append("\"/>")
                        .append(value)
                        .append("</item>\n");

                counter++;
            }

            xml.append("</array>\n");
            xml.append("\t</source>\n");
        }

        xml.append("</inputdata>\n");

        return xml.toString();
    }
}
