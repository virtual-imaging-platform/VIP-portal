/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
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
//    public Timepoint createAndAddTimepoint(SimulationObjectModel objectModel, Calendar startingDate);
//    public void setObjecLayerResolution(ObjectLayer objectLayer, Resolution newResolution);
//    public Instant createAndAddInstant(Timepoint timePoint, String duration);
//    public ObjectLayer createObjectLayerAndAddLayerPart(SimulationObjectModel objectModel, int timePointIndex, int instantIndex, Resolution layerResolution, ObjectLayerPart objectLayerPart);
//    public void addObjectLayerPart(ObjectLayer objectLayer, ObjectLayerPart objectLayerPart);
//    public ObjectLayerPart createObjectLayerPart(String objectName, ArrayList<String> fileNames, int objectLabel, Format fileFormat, int objectPriority);
//    public void addPhysicalParametersLayerToInstant(PhysicalParametersLayer physicalParametersLayer, Instant instant);
//    public void addPhysicalParametersLayerToObjectLayer(PhysicalParametersLayer physicalParametersLayer, ObjectLayer objectLayer);
//    public PhysicalParametersLayer createPhysicalParametersLayer(PhysicalParameterType physicalParametersType, ArrayList<String> fileNames, double b0);
//    public PhysicalParameter createPhysicalParameter(PhysicalParameterType physicalParameterType, ArrayList<String> fileNames);
//others to be added on request

    //public ObjectType getObjectType(String objectName);

}
