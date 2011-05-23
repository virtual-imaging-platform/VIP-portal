/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.server.business;

import fr.insalyon.creatis.vip.models.client.bean.Instant;
import fr.insalyon.creatis.vip.models.client.bean.ObjectLayer;
import fr.insalyon.creatis.vip.models.client.bean.ObjectLayer.Resolution;
import fr.insalyon.creatis.vip.models.client.bean.PhysicalParametersLayer;
import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel.ObjectType;
import fr.insalyon.creatis.vip.models.client.bean.Timepoint;
import java.util.Calendar;

/**
 * Factory used for conversions between Neusemstore and VIP Portal Beans
 * @author glatard
 */
public class BeanFactory {

    public static SimulationObjectModel getVIPSimulationObjectModel(fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.SimulationObjectModel om){
        SimulationObjectModel somf = new SimulationObjectModel();
        somf.setModelName(om.getModelName());
        somf.setURI(om.getURI());
        //timepoints should be added
        return somf;
    
    }
    
    public static fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.SimulationObjectModel getNeuSemStoreSimulationObjectModel(fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel vipSimulationObjectModel) {

        fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.SimulationObjectModel result = new fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.SimulationObjectModel();
        
        result.setModelName(vipSimulationObjectModel.getModelName());
        result.setURI(vipSimulationObjectModel.getURI());
        for(Timepoint t : vipSimulationObjectModel.getTimepoints()){
            result.addTimepoint(BeanFactory.getNeuSemStoreTimepoint(t));
        }
        return result;
    }

    private static fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.Timepoint getNeuSemStoreTimepoint(Timepoint t) {
        fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.Timepoint result = new fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.Timepoint();
        
        result.setURI(t.getURI());
        //TODO fix the calendar
        result.setStartingDate(Calendar.getInstance());
        for(Instant i : t.getInstants()){
            result.addInstant(BeanFactory.getNeuSemStoreInstant(i));          
        }
        return result;
    }

    private static fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.Instant getNeuSemStoreInstant(Instant i) {
        fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.Instant result = new fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.Instant();
        result.setDuration(i.getDuration());
        result.setURI(i.getURI());
//        for(ObjectLayer ol : i.getObjectLayers())
//            result.addObjectLayer(BeanFactory.getNeuSemStoreObjectLayer(ol));
//        for(PhysicalParametersLayer ppl : i.getPhysicalParametersLayers())
//            result.addPhysicalParametersLayer(getNeuSemStorePhysicalParametersLayer(ppl));
        return result;    
    }

    private static fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.ObjectLayer getNeuSemStoreObjectLayer(ObjectLayer ol) {
//        fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.ObjectLayer result = new fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.ObjectLayer(getNeuSemStoreObjectType(ol.getType()));
//        
//        result.setURI(ol.getURI());
//        result.setResolution(BeanFactory.getNeuSemStoreResolution(ol.getResolution()));
        throw new UnsupportedOperationException("Not yet implemented");
        
     //   return result;
    }

    private static fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.bean.PhysicalParametersLayer getNeuSemStorePhysicalParametersLayer(PhysicalParametersLayer ppl) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static ObjectType getNeuSemStoreObjectType(SimulationObjectModel.ObjectType objectType) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static Resolution getNeuSemStoreResolution(ObjectLayer.Resolution resolution) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
