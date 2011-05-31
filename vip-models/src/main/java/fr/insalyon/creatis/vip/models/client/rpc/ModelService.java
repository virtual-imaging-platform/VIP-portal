/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface ModelService extends RemoteService {

    public static final String SERVICE_URI = "/modelservice";

     public static class Util {

        public static ModelServiceAsync getInstance() {

           ModelServiceAsync instance = (ModelServiceAsync) GWT.create(ModelService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public List<String> getFiles(String modelZipFile);
    public SimulationObjectModel createModel(String modelName);
    public List<SimulationObjectModelLight> listAllModels();
    public SimulationObjectModel getADAM();
    public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri);
    public SimulationObjectModel rebuildObjectModelFromAnnotationFile(String fileName);
    public void completeModel(SimulationObjectModel som);
    public SimulationObjectModel setStorageUrl(SimulationObjectModel som, String url);
    public void removeObjectModelFromTripleStore(String uri);
    public void deleteAllModelsInTheTripleStore();
}
