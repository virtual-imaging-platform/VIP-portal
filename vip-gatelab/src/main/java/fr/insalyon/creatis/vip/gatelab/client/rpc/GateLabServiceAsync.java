package fr.insalyon.creatis.vip.gatelab.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva, Ibrahim Kallel
 */
public interface GateLabServiceAsync {
   
    public void getGatelabWorkflowInputs(String workflowID, AsyncCallback<Map<String, String>> asyncCallback);

    public void getNumberParticles(String workflowID, AsyncCallback<java.lang.Long> asyncCallback);

    public void StopWorkflowSimulation(String workflowID, AsyncCallback<Void> asyncCallback);
    
    public void reportProblem(String message, AsyncCallback<Void> asyncCallback);
}
