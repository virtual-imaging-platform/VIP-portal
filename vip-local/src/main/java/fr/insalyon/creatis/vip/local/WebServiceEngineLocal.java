package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.RestServiceEngine;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

/**
 * local version to override WebServiceEngine and make all executions stuff
 * by LocalBashEngine
 */

@Service
@Profile("local")
@Primary
public class LocalWorkflowEngineInstantiator extends WorkflowEngineInstantiator {

    @Autowired
    private LocalBashEngine localBashEngine;

    @Override
    public String launch(String addressWS, String workflowContent, String inputs, String settings, String proxyFileName) throws RemoteException, ServiceException, BusinessException {
        return localBashEngine.launch(workflowContent, inputs);
    }

    @Override
    public void kill(String addressWS, String workflowID) throws RemoteException, ServiceException {
        localBashEngine.kill(workflowID);

    }

    @Override
    public SimulationStatus getStatus(String addressWS, String workflowID) throws RemoteException, ServiceException {
        return localBashEngine.getStatus(workflowID);
    }

}
