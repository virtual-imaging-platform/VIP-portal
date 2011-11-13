/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.models.server.rpc;

import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.models.client.view.ModelException;
import fr.insalyon.creatis.vip.models.server.business.ModelBusiness;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard, Rafael Silva
 */
public class ModelServiceImpl extends AbstractRemoteServiceServlet implements ModelService {

    private static final Logger logger = Logger.getLogger(ModelServiceImpl.class);
    private ModelBusiness modelBusiness;

    public ModelServiceImpl() {

        modelBusiness = new ModelBusiness();
    }

    public List<String> getFiles(String modelZipFile) throws ModelException {

        try {
            return modelBusiness.getFiles(modelZipFile);

        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel createModel(String modelName) throws ModelException {

        try {
            trace(logger, "Creating model: " + modelName);
            return modelBusiness.createModel(modelName);

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel.ObjectType getObjectType(String objectName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SimulationObjectModelLight> listAllModels() throws ModelException {

        try {
            return modelBusiness.listAllModels();

        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public SimulationObjectModel getADAM() {
        return modelBusiness.getADAM();
    }

    public void completeModel(SimulationObjectModel som) {
        modelBusiness.completeModel(som);
    }

    public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri) {
        return modelBusiness.rebuildObjectModelFromTripleStore(uri);
    }

    public SimulationObjectModel rebuildObjectModelFromAnnotationFile(String fileName) {
        return modelBusiness.rebuildObjectModelFromAnnotationFile(fileName);
    }

    public SimulationObjectModel setStorageUrl(SimulationObjectModel som, String url) {
        return modelBusiness.setStorageUrl(som, url);
    }

  
    public void deleteAllModelsInTheTripleStore() throws ModelException {

        try {
            trace(logger, "Removing all models in the triple store.");
            modelBusiness.deleteAllModelsInTheTripleStore();

        } catch (CoreException ex) {
            throw new ModelException(ex);
        }
    }

    public List<SimulationObjectModelLight> searchModels(String query, 
            String[] types, String[] time) throws ModelException {

        try {
            return modelBusiness.searchModels(query, types, time);

        } catch (BusinessException ex) {
            throw new ModelException(ex);
        }
    }

    public void deleteModel(String uri) throws ModelException {
        try{
            try {
                trace(logger, "Removing object model: " + uri);
            } catch (CoreException ex) {
                java.util.logging.Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        modelBusiness.deleteModel(uri);
        }catch (BusinessException ex){
            throw new ModelException(ex);
        }
    }
}
