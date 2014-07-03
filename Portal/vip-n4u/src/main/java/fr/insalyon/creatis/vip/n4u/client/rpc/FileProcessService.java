/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nouha Boujelben
 */
public interface FileProcessService extends RemoteService {

    public static final String SERVICE_URI = "/fileProcessService";

    public static class Util {

        public static FileProcessServiceAsync getInstance() {

            FileProcessServiceAsync instance = (FileProcessServiceAsync) GWT.create(FileProcessService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    int[] fileJobProcess(String jobFile, String expressFile) throws N4uException;

    List<String[]> parseXmlFile(String xmlFile) throws N4uException;

    void generateScriptFile(Map<Integer,Map> listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String environementFile, String description) throws N4uException;

    String generateGwendiaFile(Map<Integer,Map> listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) throws N4uException;

    void generateGaswFile(Map<Integer,Map> listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String sandboxFile, String environementFile) throws N4uException;

    String getApplicationClass() throws N4uException;
}
