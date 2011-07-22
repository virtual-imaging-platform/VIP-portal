package fr.insalyon.creatis.vip.simulationgui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import java.util.List;

/**
 *
 * @author moulin
 */

public interface VTKControllerAsync {

        public void downloadAndUnzipExample(String path ,AsyncCallback <Data3D[][]> callback);
        public void downloadAndUnzipModel(String url, String proxyFileName ,String user,AsyncCallback <Data3D[][]> callback);
        public void linkerSimulator (String modality, AsyncCallback <List <String>> callback);

}