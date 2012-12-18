/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface SimulatedDataServiceAsync {
    public void getSimulatedData(AsyncCallback<List<SimulatedData>> asyncCallback);
    public void getRdfDump(AsyncCallback<String> asyncCallback);
}
