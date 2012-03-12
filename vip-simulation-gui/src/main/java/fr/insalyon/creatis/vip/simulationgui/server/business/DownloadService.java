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
package fr.insalyon.creatis.vip.simulationgui.server.business;

import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Instant;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.view.SimulationGUIException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin Moulin, Rafael Silva
 */
public class DownloadService {

    private static Logger logger = Logger.getLogger(DownloadService.class);
    private String zipPath = "null";
    private String finalPath = "null";
    private String rdfPath = "null";
    private String returnPath = "null";
    private static Data3D[][] object;

    public DownloadService(String url, String user) throws URISyntaxException  {

        zipPath = "null";
        finalPath = "null";
        File fb = new File(Server.getInstance().getConfigurationFolder() + "/models");
        fb.mkdirs();
        download(url, user);
    }

    public String getPath() {
        return returnPath;
    }

    public Data3D[][] getObject() {
        return object;
    }

    private void download(String url, String user) throws URISyntaxException {
        try {
            zipPath = vletAgentConnexion(url, user);

        } catch (DataManagerException ex) {
            logger.error(ex);
            zipPath = "null";
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            zipPath = "null";
        }

        try {
            finalPath = unzip(zipPath);

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            finalPath = "null2";
        } catch (IOException ex) {
            logger.error(ex);
            finalPath = "null2";
        }
    }

    private String vletAgentConnexion(String url, String user) throws GRIDAClientException, URISyntaxException, DataManagerException {

        URI URL = new URI(url);
        String path = "null2";
        path = CoreUtil.getGRIDAClient().getRemoteFile(
                DataManagerUtil.parseBaseDir(user, URL.getPath()),
                Server.getInstance().getConfigurationFolder() + "/models");
        
        return path;
    }

    private String unzip(String path) throws FileNotFoundException, IOException, URISyntaxException {

        System.out.println(path);
        String parent = new File(path).getParent();
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(new FileInputStream(path));
        zipentry = zipinputstream.getNextEntry();
        FileOutputStream fileoutputstream;

        String entryName = zipentry.getName();
        File newFile = new File(entryName);
        String directory = newFile.getParent();
        if (!entryName.endsWith("/")) {
            parent = parent + "/" + entryName.substring(0, (entryName.length() - 4));
            new File(parent).mkdir();
        }

        byte[] buf = new byte[1024];
        while (zipentry != null) {
//                for each entry to be extracted
            entryName = zipentry.getName();
            newFile = new File(entryName);
            directory = newFile.getParent();

            if (directory != null) {
                new File(parent + "/" + directory + "/").mkdir();

            }
            if (!entryName.endsWith("/")) {

                int n;
                fileoutputstream = new FileOutputStream(parent + "/" + entryName);


                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }
                fileoutputstream.close();
                zipinputstream.closeEntry();

            }
            if (entryName.endsWith(".rdf")) {
                rdfPath = parent + "/" + entryName;
            }
            zipentry = zipinputstream.getNextEntry();
        }

        return parent;
    }

    public Data3D[][] rebuildObject() throws SimulationGUIException, Exception
    {
         return rebuildObject(finalPath, rdfPath);
    }
    private Data3D[][] rebuildObject(String path, String rdfPath) throws SimulationGUIException, Exception {
        
        SimulationObjectModel inModel = SimulationObjectModelFactory.rebuildObjectModelFromAnnotationFile(rdfPath, true);
        int index = 0;

        Timepoint tp = inModel.getTimepoints().get(0);
        Instant it = tp.getInstants().get(0);
        String[][] entry = new String[it.getObjectLayers().size()][1];
        String[] type = new String[it.getObjectLayers().size()];
        
        for (ObjectLayer ol : it.getObjectLayers()) {
            entry[index] = new String[ol.getLayerParts().size()];
            type[index] = ol.getType().toString();
            int i = 0;
            for (ObjectLayerPart olp : ol.getLayerParts()) {
                entry[index][i] = olp.getFileNames().toString();
                i++;
            }
            index++;
        }
//        returnPath = " 0 ";
//    
//        returnPath += "  objectListSize :     " + index + " compare to " + it.getObjectLayers().size();
//        System.out.println(returnPath);
//       

        //ObjectFactoryOld objFact=ObjectFactoryOld.getInstance();
        return ObjectFactory.buildMulti(path, entry, type);
    }
}
