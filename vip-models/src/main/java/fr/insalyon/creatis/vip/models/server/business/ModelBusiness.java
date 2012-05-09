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
package fr.insalyon.creatis.vip.models.server.business;

import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelQueryer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Instant;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.server.rpc.DataManagerServiceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard, Rafael Silva
 */
public class ModelBusiness {

    private static final Logger logger = Logger.getLogger(ModelBusiness.class);

    /**
     * 
     * @param modelZipFile
     * @return
     * @throws BusinessException 
     */
    public List<String> getFiles(String modelZipFile) throws BusinessException {

        logger.info("Calling getFiles " + modelZipFile);
        if (modelZipFile.lastIndexOf(".zip") == -1) {
            throw new BusinessException("This is not a zip file");
        }

        try {
            String rootDirectory = Server.getInstance().getDataManagerPath() + "/uploads/";
            ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(rootDirectory + modelZipFile));
            ZipEntry zipentry = zipinputstream.getNextEntry();

            ArrayList<String> files = new ArrayList<String>();
            byte[] buf = new byte[1024];

            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                if (entryName.endsWith(".rdf")) { //extract only the annotations.
                    FileOutputStream fileoutputstream;
                    File newFile = new File(entryName);
                    String directory = newFile.getParent();

                    if (directory == null) {
                        if (newFile.isDirectory()) {
                            break;
                        }
                    }
                    int n;
                    fileoutputstream = new FileOutputStream(
                            rootDirectory + entryName);

                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                        fileoutputstream.write(buf, 0, n);
                    }

                    fileoutputstream.close();
                    zipinputstream.closeEntry();
                }
                files.add(rootDirectory + entryName);
                zipentry = zipinputstream.getNextEntry();
            }
            zipinputstream.close();

            return files;

        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
    }

    public SimulationObjectModel createModel(String modelName) {
        return SimulationObjectModelFactory.createModel(modelName);
    }

    public SimulationObjectModel.ObjectType getObjectType(String objectName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SimulationObjectModelLight> listAllModels() throws BusinessException {
        
        try {
            return SimulationObjectModelQueryer.getAllModels();
            
        } catch (Exception ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public SimulationObjectModel getADAM() {
        return SimulationObjectModelFactory.rebuildObjectModelFromAnnotationFile("/tmp/adam.rdf", false);

    }

    public void completeModel(SimulationObjectModel som) {
        SimulationObjectModelFactory.inferModelSemanticAxes(som);
        SimulationObjectModelFactory.completeModel(som);
    }

    public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri) {
        return SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(uri);
    }

    public SimulationObjectModel rebuildObjectModelFromAnnotationFile(String fileName) {
        return SimulationObjectModelFactory.rebuildObjectModelFromAnnotationFile(fileName, true);
    }

    public SimulationObjectModel setStorageUrl(SimulationObjectModel som, String url) {
        SimulationObjectModelFactory.setStorageURL(som, url);
        return som;
    }

  
    public void deleteAllModelsInTheTripleStore() {
        SimulationObjectModelFactory.deleteAllModelsInPersistentStore();
    }

    public void deleteModel(String uri) throws BusinessException{
        try {
            System.out.println("Deleting model with uri "+uri);
            SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(uri);
            SimulationObjectModelFactory.deleteModelInPersistentStore(som);
                            } catch (Exception e) {
                                e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    
    public List<SimulationObjectModelLight> searchModels(String query, 
            String[] types, String[] time) throws BusinessException {

//        System.out.println("Model part: " + query + " ; types: ");
//        for (int i = 0; i < types.length; i++) {
//            System.out.print(types[i].toString());
//        }
//        System.out.print(" time: ");
//        for (int i = 0; i < time.length; i++) {
//            System.out.print(time[i].toString());
//        }
//        System.out.println("");

        boolean anat = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("anatomical")) {
                anat = true;
            }
        }

        boolean path = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("pathological")) {
                path = true;
            }
        }

        boolean foreign = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("foreign object")) {
                foreign = true;
            }
        }

        boolean external = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("external agent")) {
                external = true;
            }
        }

        boolean longi = false;
        for (int i = 0; i < time.length; i++) {
            if (time[i].toLowerCase().equals("longitudinal follow-up")) {
                longi = true;
            }
        }

        boolean move = false;
        for (int i = 0; i < time.length; i++) {
            if (time[i].toLowerCase().equals("movement")) {
                move = true;
            }
        }

        boolean geom = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("geometrical")) {
                geom = true;
            }
        }
        ArrayList<SimulationObjectModelLight> result = new ArrayList<SimulationObjectModelLight>();

        List<SimulationObjectModelLight> somls = listAllModels();
        for (SimulationObjectModelLight soml : somls) {
            SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(soml.getURI());

//            System.out.println("looking at SOML " + soml.getModelName());
//
//            if (soml.isLongitudinal()) {
//                System.out.println("SOML is longitudinal");
//            }
//            if (soml.isMoving()) {
//                System.out.println("SOML is moving");
//            }
//
//            if (soml.getSemanticAxes()[0]) {
//                System.out.println("SOML is anatomical");
//            }
//
//            if (soml.getSemanticAxes()[1]) {
//                System.out.println("SOML is pathological");
//            }
//            if (soml.getSemanticAxes()[2]) {
//                System.out.println("SOML is geometricaal");
//            }
//            if (soml.getSemanticAxes()[3]) {
//                System.out.println("SOML is foreign");
//            }
//
//            if (soml.getSemanticAxes()[4]) {
//                System.out.println("SOML is external");
//            }

            if ((soml.isLongitudinal() || !longi)) {

                if (soml.isMoving() || !move) {
                    if (soml.getSemanticAxes()[0] || !anat) {
                        if (soml.getSemanticAxes()[1] || !path) {
                            if (soml.getSemanticAxes()[3] || !foreign) {
                                if (soml.getSemanticAxes()[2] || !geom) {
                                    if (soml.getSemanticAxes()[4] || !external) {

                                        for (Timepoint t : som.getTimepoints()) {
                                            for (Instant it : t.getInstants()) {
                                                for (ObjectLayer ol : it.getObjectLayers()) {
                                                    for (ObjectLayerPart olp : ol.getLayerParts()) {
                                                        if (matches(olp.getReferredObject().getObjectName().replace("_", " ") + " (" + olp.getFormat() + ": ", query)) {
                                                            if (!result.contains(soml)) {
                                                                result.add(soml);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        // System.out.println("Don't include " + soml.getModelName() + " because it has no exernal agent");
                                    }
                                } else {
                                    // System.out.println("Don't include " + soml.getModelName() + " because it has no geometrical object");
                                }
                            } else {
                                // System.out.println("Don't include " + soml.getModelName() + " because it has no foreign body");
                            }
                        } else {
                            // System.out.println("Don't include " + soml.getModelName() + " because it's has not pathology");
                        }
                    } else {
                        // System.out.println("Don't include " + soml.getModelName() + " because it has no anatomy");
                    }
                } else {
                    //System.out.println("Don't include " + soml.getModelName() + " because it's not moving");
                }

            } else {
                // System.out.println("Don't include " + soml.getModelName() + " because it's not longitudinal");
            }
        }
        return result;
    }

    private boolean matches(String target, String query) {
        return ((target.toLowerCase().indexOf(query.toLowerCase()) != -1) || (query.toLowerCase().indexOf(target.toLowerCase()) != -1));
    }

    public String getStorageURL(String uri) throws BusinessException {
        try{
            return SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(uri).getStorageURL();
        }catch (Exception e){
            throw new BusinessException(e);
        }
        }
    
   }
