/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.portal.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface ModelServiceAsync {
    public void getFiles(String modelZipFile, AsyncCallback<List<String>> asyncCallback);

}
