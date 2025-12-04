package fr.insalyon.creatis.vip.application.server.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.Server;

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
            Map<String, List<String>> parameters, String executorConfig) throws VipException, Exception {

        try {
            String workflowContent = appVersion.getDescriptor();
            String inputs = (parameters != null) ? getParametersAsJSONInput(parameters) : null;
            String proxyFileName = server.getServerProxy(server.getVoName());
            String settingsJSON = new ObjectMapper().writeValueAsString(appVersion.getSettings());
            String id = engine.launch(engineEndpoint, workflowContent, inputs, settingsJSON, executorConfig, proxyFileName);

            return new Workflow(id, user.getFullName(),
                    WorkflowStatus.Running, new Date(), null, simulationName,
                    appVersion.getApplicationName(), appVersion.getVersion(), "",
                    engineEndpoint, null);

        } catch (JsonProcessingException ex) {
            logger.error("Error launching simulation {} ({}/{})",
                    simulationName, appVersion.getApplicationName(), appVersion.getVersion(), ex);
            throw new VipException(ex);
        }
    }

    public SimulationStatus getStatus(String engineEndpoint, String simulationID) throws VipException {
        return engine.getStatus(engineEndpoint, simulationID);
    }

    public void kill(String engineEndpoint, String simulationID) throws VipException {
        engine.kill(engineEndpoint, simulationID);
    }

    public String getParametersAsJSONInput(Map<String, List<String>> parameters) throws VipException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            logger.error("Failed ot convert execution parameters to JSON string!");
            throw new VipException(e);
        }
    }
}
