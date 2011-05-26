/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface ModelServiceAsync {
    public void getFiles(String modelZipFile, AsyncCallback<List<String>> asyncCallback);
    public void  createModel(String modelName, AsyncCallback<SimulationObjectModel> asyncCallback);
}
