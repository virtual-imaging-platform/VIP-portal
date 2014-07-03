/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nouha Boujelben
 */
public interface FileProcessServiceAsync {

    public void fileJobProcess(String jobFile, String expressFile, AsyncCallback<int[]> callback);

    public void parseXmlFile(String xmlFile, AsyncCallback<List<String[]>> callback);

    public void generateScriptFile(Map<Integer, Map> listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String environementFile, String description, AsyncCallback<Void> callback);

    public void generateGwendiaFile(Map<Integer, Map> listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, AsyncCallback<String> callback);

    public void generateGaswFile(Map<Integer, Map> listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String sandboxFile, String environementFile, AsyncCallback<Void> callback);

    public void getApplicationClass(AsyncCallback<String> callback);
}
