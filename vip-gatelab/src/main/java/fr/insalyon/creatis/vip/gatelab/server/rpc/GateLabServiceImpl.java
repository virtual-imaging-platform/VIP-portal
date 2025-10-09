package fr.insalyon.creatis.vip.gatelab.server.rpc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import fr.insalyon.creatis.vip.gatelab.client.view.GateLabException;
import fr.insalyon.creatis.vip.gatelab.server.business.GateLabBusiness;
import jakarta.servlet.ServletException;

public class GateLabServiceImpl extends AbstractRemoteServiceServlet implements GateLabService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private GateLabBusiness gatelabBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        gatelabBusiness = getBean(GateLabBusiness.class);
    }

    @Override
    public Map<String, String> getGatelabWorkflowInputs(String simulationID) throws GateLabException {
        try {
            return gatelabBusiness.getGatelabWorkflowInputs(simulationID);
        } catch (VipException ex) {
            throw new GateLabException(ex);
        }
    }

    @Override
    public long getNumberParticles(String simulationID) throws GateLabException {

        try {
            return gatelabBusiness.getNumberParticles(simulationID);

        } catch (VipException ex) {
            throw new GateLabException(ex);
        }
    }

    @Override
    public void StopWorkflowSimulation(String simulationID) throws GateLabException {

        try {
            trace(logger, "Stopping GateLab simulation: " + simulationID);
            gatelabBusiness.StopWorkflowSimulation(simulationID);

        } catch (VipException ex) {
            throw new GateLabException(ex);
        }
    }

    @Override
    public void reportProblem(String message) throws GateLabException {
        try {
            trace(logger, "Reporting simulation launch problem.");
            gatelabBusiness.reportProblem(getSessionUser().getEmail(), message);

        } catch (VipException ex) {
            throw new GateLabException(ex);
        }
    }
}
