package fr.insalyon.creatis.vip.application.server.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;

@Service
public class WorkflowExecutionBusiness extends CommonBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private WorkflowEngineInstantiator engine;

    @Autowired
    public WorkflowExecutionBusiness(Server server, WorkflowEngineInstantiator engine) {
        this.server = server;
        this.engine = engine;
    }

    public String launch(String workflowName, Resource resource, Engine engine, AppVersion appVersion,
                           List<Map<String, List<String>>> parameters, List<Resource> orderedResources) throws VipException {

        try {
            String workflowContent = appVersion.getDescriptor();
            String inputs = (parameters != null) ? getParametersAsJSONInput(parameters) : null;
            String proxyFileName = server.getServerProxy(server.getVoName());
            Map<String,String> settings = new HashMap<>(appVersion.getSettings());
            settings.put(ApplicationConstants.DEFAULT_EXECUTOR_GASW, resource.getType().toString());

            // Add ordered resource list for fallback mechanism (types only, comma-separated)
            String resourcesCSV = orderedResources.stream()
                .map(r -> r.getType().toString())
                .collect(Collectors.joining(","));
            settings.put(ApplicationConstants.FALLBACK_RESOURCES_GASW, resourcesCSV);

            String settingsJSON = new ObjectMapper().writeValueAsString(settings);
            logger.info("Launching workflow of [{}/{}]", appVersion.getApplicationName(), appVersion.getVersion());
            logger.info("Launching on engine [{}] and resource [{}] (type : [{}])", engine.getName(), resource.getName(), resource.getType());
            logger.info("Fallback resources: {}", resourcesCSV);
            logger.info("inputs : {}", inputs);
            logger.info("settings: {}", settingsJSON);
            return this.engine.launch(engine.getEndpoint(), workflowContent, inputs, settingsJSON, resource.getConfiguration(), proxyFileName);

        } catch (JsonProcessingException ex) {
            logger.error("Error launching simulation {} ({}/{})",
                    workflowName, appVersion.getApplicationName(), appVersion.getVersion(), ex);
            throw new VipException(ex);
        }
    }

    public WorkflowStatus getStatus(String engineEndpoint, String simulationID) throws VipException {
        return engine.getStatus(engineEndpoint, simulationID);
    }

    public void kill(String engineEndpoint, String simulationID) throws VipException {
        engine.kill(engineEndpoint, simulationID);
    }

    public String getParametersAsJSONInput(List<Map<String, List<String>>> parameters) throws VipException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            logger.error("Failed ot convert execution parameters to JSON string!");
            throw new VipException(e);
        }
    }
}
