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
package fr.insalyon.creatis.vip.models.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelQueryer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectSearcher;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Instant;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectMatch;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Tristan Glatard
 */
public class ModelServiceImpl extends RemoteServiceServlet implements ModelService {

    public List<String> getFiles(String modelZipFile) {
        System.out.println("Calling getFiles " + modelZipFile);
        if (modelZipFile.lastIndexOf(".zip") == -1) {
            this.doUnexpectedFailure(new Exception("This is not a zip file"));
        }


        String rootDirectory = ServerConfiguration.getInstance().getDataManagerPath() + "/uploads/";

        ArrayList<String> files = new ArrayList<String>();
        byte[] buf = new byte[1024];
        try {

            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(rootDirectory + modelZipFile));
            zipentry = zipinputstream.getNextEntry();
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
        } catch (IOException ex) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return files;
    }

    public SimulationObjectModel createModel(String modelName) {
        return SimulationObjectModelFactory.createModel(modelName);
    }

    public SimulationObjectModel.ObjectType getObjectType(String objectName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SimulationObjectModelLight> listAllModels() {
        return SimulationObjectModelQueryer.getAllModels();
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

    public void removeObjectModelFromTripleStore(String uri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteAllModelsInTheTripleStore() {
        SimulationObjectModelFactory.deleteAllModelsInTheTripleStore();
    }

    public List<SimulationObjectModelLight> searchModels(String query) {
        boolean[] scope = new boolean[]{true, true, true, true, true};
        ArrayList<SimulationObjectModelLight> result = new ArrayList<SimulationObjectModelLight>();

        List<SimulationObjectModelLight> somls = listAllModels();
        for (SimulationObjectModelLight soml : somls) {
            SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(soml.getURI());
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
        }

        return result;
    }

    private boolean matches(String target, String query) {
        return ((target.toLowerCase().indexOf(query.toLowerCase()) != -1) || (query.toLowerCase().indexOf(target.toLowerCase()) != -1));
    }
}
