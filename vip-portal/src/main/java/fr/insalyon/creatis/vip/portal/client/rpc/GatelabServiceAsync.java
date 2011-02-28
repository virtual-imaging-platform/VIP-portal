/**
 *
 * @author Ibrahim Kallel
 */

package fr.insalyon.creatis.vip.portal.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Map;



public interface GatelabServiceAsync {

    public void getGatelabWorkflowInputs(String workflowID, AsyncCallback<Map<String, String>> asyncCallback);

    public void getNumberParticles(String workflowID,AsyncCallback<java.lang.Long> asyncCallback);

    public void StopWorkflowSimulation( String workflowID, AsyncCallback<Void> asyncCallback);


}
