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
package fr.insalyon.creatis.vip.models.server.rpc;

import fr.cnrs.i3s.neusemstore.persistence.RDFManagerFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.PhysicalParametersLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.models.client.ParserLUT.GenericParameter;
import fr.insalyon.creatis.vip.models.client.ParserLUT.PhysicalParameterLUT;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.view.ModelException;
import fr.insalyon.creatis.vip.models.server.business.ModelBusiness;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard, Rafael Ferreira da Silva
 */
public class ModelServiceImpl extends AbstractRemoteServiceServlet implements ModelService {

    private static final Logger logger = Logger.getLogger(ModelServiceImpl.class);
    private ModelBusiness modelBusiness;

    public ModelServiceImpl() {

        modelBusiness = new ModelBusiness();
    }

    public List<String> getFiles(String modelZipFile, String modelFullPath, boolean bUpload) throws ModelException {

        try {
            try {
                try {
                    return modelBusiness.getFiles(modelZipFile, getSessionUser(), modelFullPath, bUpload);
                } catch (CoreException ex) {
                    throw new ModelException(ex);
                }
            } catch (DataManagerException ex) {
                throw new ModelException(ex);
            }

        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel createModel(String modelName, String user) throws ModelException {

        try {
            trace(logger, "Creating model: " + modelName);
            return modelBusiness.createModel(modelName, getSessionUser().getFullName());

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel.ObjectType getObjectType(String objectName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SimulationObjectModelLight> listAllModels(boolean test) throws ModelException {

        try {
            return modelBusiness.listAllModels(test);

        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel getADAM() {
        return modelBusiness.getADAM();
    }

    public void completeModel(SimulationObjectModel som, boolean test) {
        modelBusiness.completeModel(som, test);
    }

    public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri, boolean test) {
        return modelBusiness.rebuildObjectModelFromTripleStore(uri,test);
    }

    public SimulationObjectModel rebuildObjectModelFromAnnotationFile(String fileName) throws ModelException {
        try {
            return modelBusiness.rebuildObjectModelFromAnnotationFile(fileName, getSessionUser().getFullName());
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel setStorageUrl(SimulationObjectModel som, String url) throws ModelException {
        return modelBusiness.setStorageUrl(som, url);
    }

    public void deleteAllModelsInTheTripleStore(boolean test) throws ModelException {

        try {
            trace(logger, "Removing all models in the triple store.");
            modelBusiness.deleteAllModelsInTheTripleStore(test);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public List<SimulationObjectModelLight> searchModels(String query,
            String[] types, String[] time,boolean test) throws ModelException {

        try {
            return modelBusiness.searchModels(query, types, time,test);

        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public String getStorageURL(String uri, boolean test) throws ModelException {
        try {
            try {
                trace(logger, "Removing object model: " + uri);
            } catch (CoreException ex) {
                java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return modelBusiness.getStorageURL(uri,test);
        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public void deleteModel(String uri, boolean test) throws ModelException {
        try {
            try {
                trace(logger, "Removing object model: " + uri);
            } catch (CoreException ex) {
                java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            modelBusiness.deleteModel(uri,test);
        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel createEmptyModel() throws ModelException {
        try {
            trace(logger, "Creating an empty model ");
            return modelBusiness.createModel("Empty_Model", getSessionUser().getFullName());

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel addTimePoint(SimulationObjectModel som, Date d) throws ModelException {
        try {
            trace(logger, "Add a TimePoint");
            return modelBusiness.addTimePoint(som, d);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel addInstant(SimulationObjectModel som, int tp) throws ModelException {
        try {
            trace(logger, "Add an Instant");
            return modelBusiness.addInstant(som, tp);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel setInstantDuration(SimulationObjectModel objectModel, int tp, int ins, String duration) throws ModelException {
        try {
            trace(logger, "set Instant Duration");
            return modelBusiness.setInstantDuration(objectModel, tp, ins, duration);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel duplicateTimePoint(SimulationObjectModel objectModel, int tp) throws ModelException {
        try {
            trace(logger, "duplicate timepoint ");
            return modelBusiness.duplicateTimePoint(objectModel, tp);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel duplicateInstant(SimulationObjectModel objectModel, int tp, int ins) throws ModelException {
        try {
            trace(logger, "duplicate Instant ");
            return modelBusiness.duplicateInstant(objectModel, tp, ins);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public List<String[]> searchWithScope(String query, boolean[] scope) throws ModelException {
        try {
            trace(logger, "search through ontology");
            return modelBusiness.searchWithScope(query, scope);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }
     public SimulationObjectModel addObjectLayer(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, int tp, int ins) throws ModelException {
        try {
            trace(logger, "add LUT");
            return modelBusiness.addObjectLayer(model, layer,  tp, ins);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel addObject(SimulationObjectModel model, String ontoName, List<String> objName, int tp, int ins, int type, int label) throws ModelException {
        try {
            trace(logger, "add Object");
            return modelBusiness.addObject(model, ontoName, objName, tp, ins, type, label);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel addLUT(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, ArrayList<String> objects, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int type) throws ModelException {
        try {
            trace(logger, "add LUT");
            return modelBusiness.addLUT(model, layer, objects, tp, ins, pptype, type);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel removeTimePoint(SimulationObjectModel model, int tp) throws ModelException {

        try {
            trace(logger, "remove timepoint");
            return modelBusiness.removeTimePoint(model, tp);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel removeInstant(SimulationObjectModel model, int tp, int ins) throws ModelException {

        try {
            trace(logger, "remove instant: " + ins);
            return modelBusiness.removeInstant(model, tp, ins);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel recordAddedFiles(String zipName, List<String> addfiles,
            SimulationObjectModel model, String lfn,  String zipFullPath, boolean bUpload, boolean test) throws ModelException {

        try {
            trace(logger, "add files to zip");
            try {
                return modelBusiness.recordAddedFiles(zipName, addfiles, model, lfn,
                        getSessionUser(),  zipFullPath, bUpload,test);
            } catch (DataManagerException ex) {
                throw new ModelException(ex);
            }
        } catch (CoreException ex) {
            throw new ModelException(ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel removeObjectLayer(SimulationObjectModel model, int tp, int ins, String layer) throws ModelException {
        try {
            trace(logger, "remove object layer: " + layer);
            return modelBusiness.removeLayer(model, tp, ins, layer);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }

    }

    public SimulationObjectModel removeMap(SimulationObjectModel model, int tp, int ins, String layer, PhysicalParametersLayer.PhysicalParameterType type) throws ModelException {
             return modelBusiness.removeMap(model, tp, ins, layer, type);
         
    }
     public SimulationObjectModel removeMapAll(SimulationObjectModel model, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType type) throws ModelException {
            return modelBusiness.removeMapAll(model, tp, ins, type);
      
    }

      public SimulationObjectModel removeObject(SimulationObjectModel model, int tp, int ins, String layer, String name) throws ModelException {
       
          try {
            trace(logger, "remove object: " + name);
            return modelBusiness.removeObject(model, tp, ins, layer, name);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }
    public SimulationObjectModel removePhysical(SimulationObjectModel objectModel, int tp, int ins, String layer, PhysicalParametersLayer.PhysicalParameterType type) throws ModelException {
        try {
            trace(logger, "remove physical paramters " + type.toString());
            return modelBusiness.removePhysical(objectModel, tp, ins, layer, type);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel removeObjects(SimulationObjectModel model, int tp, int ins, String layer) throws ModelException {
        try {
            trace(logger, "remove objects from " + layer);
            return modelBusiness.removeObjects(model, tp, ins, layer);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel removePhysicals(SimulationObjectModel model, int tp, int ins, String layer) throws ModelException {
        try {
            trace(logger, "remove all physical paramters from " + layer);
            return modelBusiness.removePhysicals(model, tp, ins, layer);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel renameTimepoint(SimulationObjectModel model, int tp, Date starting) throws ModelException {
        try {
            trace(logger, "rename timepoint: " + tp);
            return modelBusiness.renameTimepoint(model, tp, starting);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel renameInstant(SimulationObjectModel model, int tp, int ins, String duration) throws ModelException {
        try {
            trace(logger, "rename instant: " + ins);
            return modelBusiness.renameInstant(model, tp, ins, duration);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public String extractRaw(String name, String zipname, String modelFullPath, boolean bUpload) throws ModelException {
        try {
            try {
                return modelBusiness.extractRaw(name, zipname, getSessionUser(), modelFullPath, bUpload);
            } catch (DataManagerException ex) {
                throw new ModelException(ex);
            }
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public PhysicalParameterLUT extractLUT(String name, String zipname, String modelFullPath, boolean bUpload) throws ModelException {
        try {
            try {
                return modelBusiness.extractLUT(name, zipname, getSessionUser(), modelFullPath, bUpload);
            } catch (DataManagerException ex) {
                throw new ModelException(ex);
            }
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }
    
    public SimulationObjectModel setDescription(SimulationObjectModel model, String description) throws ModelException {
        try {
            trace(logger, "set description " + description);
            return modelBusiness.setDescription(model, description);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel addMap(SimulationObjectModel model, ArrayList<String> objects,
            int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int b0, String externalAgent, String unitOfMeasure) throws ModelException {
        try {
            trace(logger, "add map of type " + pptype.toString());
            return modelBusiness.addMap(model, objects, tp, ins, pptype, b0, externalAgent, unitOfMeasure);
        } catch (CoreException ex) {
            throw new ModelException(ex);
        }

    }

    public void checkRDFEncoding(String files, String modelFullPath, boolean bUpload) throws ModelException {
        try {
            try {
                try {
                    modelBusiness.checkRDF(files, getSessionUser(), modelFullPath, bUpload);
                } catch (CoreException ex) {
                    java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (DataManagerException ex) {
                java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getURLFromURI(String uri, boolean test) {
        return modelBusiness.getURLFromURI(uri, test);

    }

    public String readLicense(String file) throws ModelException {
        try {
            return modelBusiness.readLicense(file);
        } catch (FileNotFoundException ex) {
            throw new ModelException(ex);
        } catch (IOException ex) {
            throw new ModelException(ex);
        }


    }
    
      public SimulationObjectModel setModelName(String name, SimulationObjectModel model)
      {
           return modelBusiness.setModelName(name, model);
      }
      
       public String copyZipFile(String zippath)
       {
           String result ="";
            try {
            try {
                try {
                    result = modelBusiness.copyZipFile(getSessionUser(),zippath);
                } catch (CoreException ex) {
                    java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (DataManagerException ex) {
                java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            return result;
       }
       
        public SimulationObjectModel addMathematicalLUT(SimulationObjectModel model, 
                SimulationObjectModel.ObjectType layer, HashMap<String, GenericParameter> parameters, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype) throws ModelException
        {
            return modelBusiness.addMathematicalLUT(model,layer, parameters, tp, ins, pptype);
        }

    @Override
    public String getRdfDump(boolean test) throws ModelException {
          logger.trace("Getting RDF dump of model repository");
        String subdir = "tmp";
        File f = null;
        try {
            File dir = new File(Server.getInstance().getWorkflowsPath()+File.separator+subdir+File.separator);
            if(!dir.exists())
                dir.mkdir();
            f = File.createTempFile("dump-", ".rdf", dir);
            SimulationObjectModelFactory.getRDFManager(test).dumpToFile(f);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ModelException(ex);
        }
        return File.separator+subdir+File.separator+f.getName();
    }
}
