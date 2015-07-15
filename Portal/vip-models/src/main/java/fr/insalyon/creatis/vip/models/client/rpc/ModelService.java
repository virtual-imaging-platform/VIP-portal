/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.models.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.PhysicalParametersLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.models.client.ParserLUT.GenericParameter;
import fr.insalyon.creatis.vip.models.client.ParserLUT.PhysicalParameterLUT;
import fr.insalyon.creatis.vip.models.client.view.ModelException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tristan Glatard, Rafael Silva
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

    public List<String> getFiles(String modelZipFile, String modelFullPath, boolean bUpload) throws ModelException;

    public SimulationObjectModel createModel(String modelName, String user) throws ModelException;

    public SimulationObjectModel createEmptyModel() throws ModelException;

    public List<SimulationObjectModelLight> listAllModels(boolean test) throws ModelException;

    public SimulationObjectModel getADAM() throws ModelException;

    public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri, boolean test) throws ModelException;

    public SimulationObjectModel rebuildObjectModelFromAnnotationFile(String fileName) throws ModelException;

    public void completeModel(SimulationObjectModel som, boolean test) throws ModelException;

    public SimulationObjectModel setStorageUrl(SimulationObjectModel som, String url) throws ModelException;

    public void deleteAllModelsInTheTripleStore(boolean test) throws ModelException;

    public List<SimulationObjectModelLight> searchModels(String query, String[] types, String[] time, boolean test) throws ModelException;

    public void deleteModel(String uri, boolean test) throws ModelException;

    public String getStorageURL(String uri, boolean test) throws ModelException;

    public SimulationObjectModel addTimePoint(SimulationObjectModel som, Date d) throws ModelException;

    public SimulationObjectModel addInstant(SimulationObjectModel som, int timepoint) throws ModelException;

    public SimulationObjectModel setInstantDuration(SimulationObjectModel objectModel, int tp, int ins, String duration)throws ModelException;
    
    public SimulationObjectModel duplicateTimePoint(SimulationObjectModel objectModel, int tp) throws ModelException;
    
    public SimulationObjectModel duplicateInstant(SimulationObjectModel objectModel, int tp, int ins) throws ModelException;
    
    public List<String[]> searchWithScope(String query, boolean[] scope) throws ModelException;

    public SimulationObjectModel addObject(SimulationObjectModel model, String ontoName, List<String> objName, int tp, int ins, int type, int label) throws ModelException;

    public SimulationObjectModel addLUT(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, ArrayList<String> objects, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int type) throws ModelException;
    
    public SimulationObjectModel removeTimePoint(SimulationObjectModel model, int tp) throws ModelException;

    public SimulationObjectModel removeInstant(SimulationObjectModel model, int tp, int ins) throws ModelException;

    public SimulationObjectModel removeObjectLayer(SimulationObjectModel model, int tp, int ins, String layer) throws ModelException;

    public SimulationObjectModel removeObject(SimulationObjectModel model, int tp, int ins, String layer, String name) throws ModelException;
    
    public SimulationObjectModel removeMap(SimulationObjectModel model, int tp, int ins, String layer, PhysicalParametersLayer.PhysicalParameterType type) throws ModelException;
    
    public SimulationObjectModel removeMapAll(SimulationObjectModel model, int tp, int ins,  PhysicalParametersLayer.PhysicalParameterType type) throws ModelException;

    public SimulationObjectModel removePhysical(SimulationObjectModel objectModel, int tp, int ins, String layer, PhysicalParametersLayer.PhysicalParameterType type) throws ModelException;
    
    public SimulationObjectModel removeObjects(SimulationObjectModel model, int tp, int ins, String layer) throws ModelException;
    
    public SimulationObjectModel removePhysicals(SimulationObjectModel model, int tp, int ins, String layer) throws ModelException;
    
    public SimulationObjectModel recordAddedFiles(String zipName, List<String> addFiles, 
                            SimulationObjectModel model, String lfn,  String zipFullPath, boolean bUpload, boolean test) throws ModelException;
    
    public SimulationObjectModel renameTimepoint(SimulationObjectModel model, int tp, Date starting) throws ModelException;
    
    public SimulationObjectModel renameInstant(SimulationObjectModel model, int tp, int ins, String duration)throws ModelException;
    
    public String extractRaw(String name, String zipname, String modelFullPath, boolean bUpload) throws ModelException ;
    
   public PhysicalParameterLUT extractLUT(String name, String zipname, String modelFullPath, boolean bUpload) throws ModelException ;
    
    public SimulationObjectModel setDescription(SimulationObjectModel model, String description)throws ModelException;
    
    public SimulationObjectModel addMap(SimulationObjectModel model, ArrayList<String> objects, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int b0, String externalAgent, String unitOfMeasure)throws ModelException;
    
    public void checkRDFEncoding(String files, String modelFullPath, boolean bUpload)throws ModelException;
    
    public String getURLFromURI(String uri, boolean test);
    
    public String readLicense(String file) throws ModelException;
    
    public SimulationObjectModel setModelName(String name, SimulationObjectModel model);
     
    public SimulationObjectModel addObjectLayer(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, int tp, int ins) throws ModelException;
         
    public String copyZipFile(String zippath);
    
    public SimulationObjectModel addMathematicalLUT(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, HashMap<String, GenericParameter> parameters, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype) throws ModelException;
    
    public String getRdfDump(boolean test) throws ModelException;
}
