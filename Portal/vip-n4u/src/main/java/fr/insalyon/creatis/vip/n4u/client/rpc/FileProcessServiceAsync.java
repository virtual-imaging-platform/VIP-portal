/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public interface FileProcessServiceAsync {
    

    public void fileTraitement(String expresstxt, AsyncCallback<List<List<String>>> callback);
}
