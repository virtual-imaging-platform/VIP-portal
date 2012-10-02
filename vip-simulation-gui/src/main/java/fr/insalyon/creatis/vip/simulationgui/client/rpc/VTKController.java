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
package fr.insalyon.creatis.vip.simulationgui.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3Dij;
import fr.insalyon.creatis.vip.simulationgui.client.view.SimulationGUIException;
import java.util.List;

/**
 *
 * @author Kevin Moulin, Rafael Silva
 */
@RemoteServiceRelativePath("vtk")
public interface VTKController extends RemoteService {

    public static final String SERVICE_URI = "/vtkcontroller";

    public static class Util {

        public static VTKControllerAsync getInstance() {

            VTKControllerAsync instance = (VTKControllerAsync) GWT.create(VTKController.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }
    
    public void configure() throws SimulationGUIException;

    public Data3D[][] downloadAndUnzipExample(String path) throws SimulationGUIException;

    public Data3D[][] downloadAndUnzipModel(String url) throws Exception;
    
    public int[][] UnzipModel(String url) throws Exception;
    
    public Data3Dij downloadModel(int i, int j) throws Exception;
    
     public List<SimulationObjectModelLight> listAllModels() throws Exception;
     
     public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri);
}
