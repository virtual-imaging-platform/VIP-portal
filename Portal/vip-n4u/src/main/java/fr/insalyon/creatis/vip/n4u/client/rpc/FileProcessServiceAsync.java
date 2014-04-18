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
    public void fileTraitement(String expressFile, AsyncCallback<List<List<String>>> callback);
    public void fileJobTraitement(String jobFile,String expressFile, AsyncCallback <int[]> callback) ;

    public void generateScriptFile(ArrayList listInput,ArrayList listOutput,String wrapperScriptPath,String scriptFile,String applicationName,String applicationLocation,String description,AsyncCallback <Void> callback) ;
    public void generateGwendiaFile(ArrayList listInput,ArrayList listOutput,String wrapperScriptPath,String scriptFile,String applicationName,String applicationLocation,String description,AsyncCallback <String> callback) ;
    public void generateGaswFile(ArrayList listInput,ArrayList listOutput,String wrapperScriptPath,String scriptFile,String applicationName,String applicationLocation,String description,AsyncCallback <Void> callback) ;
    public void  getApplicationClasse(AsyncCallback <String> callback) ;
}
