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
package fr.insalyon.creatis.vip.simulationgui.server.rpc;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3Dij;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKController;
import fr.insalyon.creatis.vip.simulationgui.client.view.SimulationGUIException;
import fr.insalyon.creatis.vip.simulationgui.server.business.DownloadService;
import fr.insalyon.creatis.vip.simulationgui.server.business.ObjectFactory;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin Moulin, Rafael Silva
 */
public class VTKControllerImpl extends AbstractRemoteServiceServlet implements VTKController {

    private static Logger logger = Logger.getLogger(VTKControllerImpl.class);
    private Data3D[][] object = null;

    @Override
    public void configure() throws SimulationGUIException {

        addClass(SimulationGUIConstants.CLASS_CT);
        addClass(SimulationGUIConstants.CLASS_MRI);
        addClass(SimulationGUIConstants.CLASS_PET);
        addClass(SimulationGUIConstants.CLASS_US);
    }

    @Override
    public Data3D[][] downloadAndUnzipExample(String path) {

        String[][] entry = new String[3][3];

        entry[0][0] = "[external_thorax.vtp]";
        entry[0][1] = "[internal_thorax.vtp]";
        entry[0][2] = "[HeartLungsThorax00.mhd, blabla.zraw]";

        entry[1] = new String[5];
        entry[1][0] = "[spine.vtp]";
        entry[1][1] = "[spinal_cord.vtp]";
        entry[1][2] = "[right_lung.vtp]";
        entry[1][3] = "[left_lung.vtp]";
        entry[1][4] = "[HeartLungsThorax00.mhd, blabla.zraw]";

        entry[2] = new String[7];
        entry[2][0] = "[myocardium.vtp]";
        entry[2][1] = "[aorta.vtp]";
        entry[2][2] = "[left_ventricle.vtp]";
        entry[2][3] = "[right_ventricle.vtp]";
        entry[2][4] = "[right_auricle.vtp]";
        entry[2][5] = "[left_auricle.vtp]";
        entry[2][6] = "[HeartLungsThorax00.mhd, blabla.zraw]";

        String type[] = new String[]{"anatomical1", "anatomical2", "anatomical3"};
        Data3D[][] object = ObjectFactory.buildMulti(path, entry, type);

        // Data3D object =ObjectFactory.build(path+"/myocardium.vtp");
        // String s="";
        // for(int i: object[0][1].getSupIndices())s+=" "+i+" ";
        // System.out.println(s);
        return object;

        // list tous les modeles 
        //             ModelServiceAsync ms = ModelService.Util.getInstance();
        //        ms.listAllModels(callback);
        //peupler  le combo
        // check
        //à la selection d'un élément du combo
        //ModelServiceAsync ms = ModelService.Util.getInstance();  
        //ms.rebuildObjectModelFromTripleStore(uri, callback);
        //donne un SimulationObjectModel
        // url = SimulationObjectModel.getStorageURL

        //get storage url

        // telechargement 
//               VletAgentClient client = new VletAgentClient(
//                    ServerConfiguration.getInstance().getVletagentHost(),
//                    ServerConfiguration.getInstance().getVletagentPort(),
//                    proxyFileName);
//
//            String workflowPath = client.getRemoteFile(url.getPath(),
//                    System.getenv("HOME") + "/.platform/models/" + 
//                    url.getPath().substring(0, url.getPath().lastIndexOf("/")));

        //dezipper
        // voir fr.insalyon.creatis.objectpreparationworkflowsbeanshells.CopyAndUnzip

        //afficher 
        //lister les fichiers du premier instant du premier timepoint
        //voir public ModelTreeGrid(SimulationObjectModel model) 
        //les afficher (bounding box pour les mhd + tous les vtp)

    }

    @Override
    public Data3D[][] downloadAndUnzipModel(String url) throws Exception {

        DownloadService service = new DownloadService(url, getSessionUser());
        Data3D[][] object = service.rebuildObject();


        System.out.println("object got and garbage done " + object.length);
        return object;
        //return service.getObject();
    }

    @Override
    public int[][] UnzipModel(String url) throws Exception {

        DownloadService service = new DownloadService(url, getSessionUser());
        object = service.rebuildObject();

        System.out.println("object got and garbage done " + object.length);

        int[][] result = new int[object.length][1];
        for (int i = 0; i < object.length; i++) {
            result[i] = new int[object[i].length];
            System.out.println("for " + i + object[i].length + " objets");
        }
        return result;
        //return service.getObject();
    }

    @Override
    public Data3Dij downloadModel(int i, int j) throws Exception {

        Data3Dij data = new Data3Dij();
        data.data = object[i][j];
        data.i = i;
        data.j = j;
        System.out.println("Download " + i + " et " + j + " size :" + data.data.getItemSizeVertex());
        return data;
    }

    private void addClass(String className) throws SimulationGUIException {

        try {
            ApplicationDAOFactory.getDAOFactory().getClassDAO().add(new AppClass(className, new ArrayList<String>()));

        } catch (DAOException ex) {
            if (!ex.getMessage().contains("A class named \"" + className + "\" already exists")) {
                logger.error(ex);
                throw new SimulationGUIException(ex);
            }
        }
    }
}
