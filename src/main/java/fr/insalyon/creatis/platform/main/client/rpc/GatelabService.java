/**
 *
 * @author Ibrahim Kallel
 */
package fr.insalyon.creatis.platform.main.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import java.util.Map;


public interface GatelabService extends RemoteService {

    public static final String SERVICE_URI = "/gatelabservice";

    public static class Util {

        public static GatelabServiceAsync getInstance() {

            GatelabServiceAsync instance = (GatelabServiceAsync) GWT.create(GatelabService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

     public Map<String, String>  getGatelabWorkflowInputs(String workflowID);

     public long getNumberParticles(String workflowID);
     
     public void StopWorkflowSimulation (String workflowID);

}
