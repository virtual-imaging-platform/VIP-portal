/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataException;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface SimulatedDataService extends RemoteService {

    public static final String SERVICE_URI = "/simulateddataservice";

    public static class Util {

        public static SimulatedDataServiceAsync getInstance() {

            SimulatedDataServiceAsync instance = (SimulatedDataServiceAsync) GWT.create(SimulatedDataService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    
    public List<SimulatedData> getSimulatedData() throws SimulatedDataException;
    
}
