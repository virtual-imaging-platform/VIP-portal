package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.WebServiceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

@Service
@Scope("prototype")
@Profile("local")
@Primary
public class WebServiceEngineLocal extends WebServiceEngine {

    @Autowired
    private LocalBashEngine localBashEngine;

    private String addressWS;
    private String settings;
    private List<ParameterSweep> inputs;
    private File workflow;

    @Override
    public String getAddressWS() {
        return addressWS;
    }

    @Override
    public void setAddressWS(String addressWS) {
        this.addressWS = addressWS;
    }

    @Override
    public String getSettings() {
        return settings;
    }

    @Override
    public void setSettings(String settings) {
        this.settings = settings;
    }

    @Override
    public String getWorkflow() {
        return workflow.toString();
    }

    @Override
    public void setWorkflow(File workflow) {
        this.workflow = workflow;
    }

    @Override
    public String getInput() {
        return inputs.toString();
    }

    @Override
    public void setInput(List<ParameterSweep> parameters) {
        this.inputs = parameters;
    }

    @Override
    public String launch(String proxyFileName, String userDN) throws RemoteException, ServiceException {
        return localBashEngine.launch(workflow, inputs);
    }

    @Override
    public String getSimulationId(String launchID) {
        return launchID;
    }

    @Override
    public void kill(String workflowID) throws RemoteException, ServiceException {
        localBashEngine.kill(workflowID);
    }

    @Override
    public SimulationStatus getStatus(String workflowID) throws RemoteException, ServiceException {
        return localBashEngine.getStatus(workflowID);
    }
}
