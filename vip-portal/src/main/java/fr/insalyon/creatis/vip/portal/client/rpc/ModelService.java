/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.portal.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import java.io.File;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface ModelService extends RemoteService {

    public static final String SERVICE_URI = "/modelservice";

     public static class Util {

        public static ModelServiceAsync getInstance() {

           ModelServiceAsync instance = (ModelServiceAsync) GWT.create(ModelService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

     public List<String> getFiles(String modelZipFile);

}
