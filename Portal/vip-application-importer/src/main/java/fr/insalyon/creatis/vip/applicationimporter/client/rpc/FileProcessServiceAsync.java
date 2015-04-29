/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public interface FileProcessServiceAsync {

    public void fileJobProcess(String jobFile, String expressFile, AsyncCallback<int[]> callback);

    public void parseXmlFile(String xmlFile, AsyncCallback<List<String[]>> callback);
    
    public void parseJsonFile(String jsonFile, AsyncCallback<List<List<HashMap<String,String>>>> callback);

    public void generateScriptFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String environementFile, String description,String dockerImage,String commandLine, AsyncCallback<Void> callback);

    public void generateGwendiaFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description,String vo, AsyncCallback<String> callback);

    public void generateGaswFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String sandboxFile, String environementFile, String extensionFile, AsyncCallback<Void> callback);


}
