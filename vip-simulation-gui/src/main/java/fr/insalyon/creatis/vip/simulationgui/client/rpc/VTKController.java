
package fr.insalyon.creatis.vip.simulationgui.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import java.util.List;


/**
 *
 * @author moulin
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

	public Data3D[][] downloadAndUnzipExample(String path);
        public Data3D[][] downloadAndUnzipModel(String url, String proxyFileName ,String user);
      
}
