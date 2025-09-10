package fr.insalyon.creatis.vip.gatelab.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.gatelab.client.view.GateLabException;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva, Ibrahim Kallel
 */
public interface GateLabService extends RemoteService {

    public static final String SERVICE_URI = "/gatelabservice";

    public static class Util {

        public static GateLabServiceAsync getInstance() {

            GateLabServiceAsync instance = (GateLabServiceAsync) GWT.create(GateLabService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public Map<String, String> getGatelabWorkflowInputs(String workflowID) throws GateLabException;

    public long getNumberParticles(String workflowID) throws GateLabException;

    public void StopWorkflowSimulation(String workflowID) throws GateLabException;
    
    public void reportProblem(String message) throws GateLabException;
}
