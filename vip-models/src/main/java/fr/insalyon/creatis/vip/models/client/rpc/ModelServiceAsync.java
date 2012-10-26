/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.models.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.PhysicalParametersLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.models.client.view.ModelException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public interface ModelServiceAsync {

    public void getFiles(String modelZipFile, String modelFullPath, boolean bUpload, AsyncCallback<List<String>> asyncCallback);

    public void createModel(String modelName, String user, AsyncCallback<SimulationObjectModel> asyncCallback);

    public void createEmptyModel(AsyncCallback<SimulationObjectModel> asyncCallback);
    
    public void listAllModels(boolean test, AsyncCallback<List<SimulationObjectModelLight>> asyncCallback);

    public void getADAM(AsyncCallback<SimulationObjectModel> asyncCallback);

    public void completeModel(SimulationObjectModel som, boolean test, AsyncCallback<Void> asyncCallback);

    public void rebuildObjectModelFromTripleStore(String uri, boolean test, AsyncCallback<SimulationObjectModel> callback);

    public void rebuildObjectModelFromAnnotationFile(String fileName, AsyncCallback<SimulationObjectModel> asyncCallback);

    public void setStorageUrl(SimulationObjectModel som, String url, AsyncCallback<SimulationObjectModel> asyncCallback);

    public void deleteAllModelsInTheTripleStore(boolean test, AsyncCallback<Void> asyncCallback);
    
    public void searchModels(String query, String[] types, String[] time, boolean test, AsyncCallback<List<SimulationObjectModelLight>> asyncCallback);
    
    public void deleteModel(String uri, boolean test, AsyncCallback<Void> asyncCallback);
    
     public void getStorageURL(String uri, boolean test, AsyncCallback<String> asyncCallback);
     
     public void addTimePoint(SimulationObjectModel som, Date d, AsyncCallback<SimulationObjectModel> asyncCallback);
     
     public void addInstant(SimulationObjectModel som, int timepoint, AsyncCallback<SimulationObjectModel> asyncCallback);
     
     public void setInstantDuration(SimulationObjectModel objectModel, int tp, int ins, String duration, AsyncCallback<SimulationObjectModel> asyncCallback);
     
     public void duplicateTimePoint(SimulationObjectModel objectModel, int tp, AsyncCallback<SimulationObjectModel> asyncCallback);
     
     public void duplicateInstant(SimulationObjectModel objectModel, int tp, int ins,  AsyncCallback<SimulationObjectModel> asyncCallback);
     
     public void searchWithScope(String query, boolean[] scope, AsyncCallback<List<String[]>> asyncCallback);
     
     public void addObject(SimulationObjectModel model, String ontoName, List<String> objName, int tp, int ins, int type, int label, AsyncCallback<SimulationObjectModel> asyncCallback);
   
     public void addLUT(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, String name, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int type, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void removeTimePoint(SimulationObjectModel model, int tp, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void removeInstant(SimulationObjectModel model, int tp, int ins,AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void removeObjectLayer(SimulationObjectModel model, int tp, int ins, String layer,AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void removeObjects(SimulationObjectModel model, int tp, int ins, String layer, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void removePhysical(SimulationObjectModel objectModel, int tp, int ins, String layer, PhysicalParametersLayer.PhysicalParameterType type, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void removePhysicals(SimulationObjectModel model, int tp, int ins, String layer, AsyncCallback<SimulationObjectModel> asyncCallback);
      
       public void removeObject(SimulationObjectModel model, int tp, int ins, String layer, String name,AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void recordAddedFiles(String zipName, List<String> addFiles ,SimulationObjectModel model, String lfn, String nwName,String zipFullPath, boolean bUpload, boolean test, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void renameTimepoint(SimulationObjectModel model, int tp, Date start, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void renameInstant(SimulationObjectModel model, int tp, int ins, String duration,  AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void extractRaw(String name, String zipname, String modelFullPath, boolean bUpload, AsyncCallback< String > asyncCallback);
      
      public void setDescription(SimulationObjectModel model,  String description,  AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void addMap(SimulationObjectModel model, String name, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int b0, String externalAgent, String unitOfMeasure, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void checkRDFEncoding(String files,String modelFullPath, boolean bUpload, AsyncCallback<SimulationObjectModel> asyncCallback);
      
      public void getURLFromURI(String files, boolean test,AsyncCallback<String> asyncCallback);
      
      public void readLicense(String file, AsyncCallback<String> asyncCallback);
}

