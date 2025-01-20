package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.WorkflowExecutionBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.util.FileUtil;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * local version to override WebServiceEngine and make all executions stuff
 * by LocalBashEngine
 */

@Service
@Profile("local")
@Primary
public class LocalWorkflowExecutionBusiness extends WorkflowExecutionBusiness {

    private final LocalBashEngine localBashEngine;

    @Autowired
    public LocalWorkflowExecutionBusiness(LocalBashEngine localBashEngine) {
        super(null, null);
        this.localBashEngine = localBashEngine;
    }

    @Override
    public Workflow launch(String engineEndpoint, AppVersion appVersion, User user, String simulationName, String workflowPath, 
            List<ParameterSweep> parameters, String settings, String executorConfig) throws BusinessException {
        String workflowContent;
        try {
            workflowContent = FileUtil.read(new File(workflowPath));
            String workflowId = localBashEngine.launch(workflowContent, parameters);
            return new Workflow(workflowId, user.getFullName(), WorkflowStatus.Running, new Date(), null, simulationName, appVersion.getApplicationName(), appVersion.getVersion(), "", engineEndpoint, null);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void kill(String addressWS, String workflowID) {
        localBashEngine.kill(workflowID);

    }

    @Override
    public SimulationStatus getStatus(String addressWS, String workflowID) {
        return localBashEngine.getStatus(workflowID);
    }

}
