/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import java.util.HashMap;
import java.util.List;

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

    int[] fileJobProcess(String jobFile, String expressFile) throws ApplicationImporterException;

    List<String[]> parseXmlFile(String xmlFile) throws ApplicationImporterException;
    
    List<List<HashMap<String,String>>> parseJsonFile(String jsonFile) throws ApplicationImporterException;

    void generateScriptFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String environementFile, String description,String dockerImage,String commandLine) throws ApplicationImporterException;

    String generateGwendiaFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description,String vo) throws ApplicationImporterException;

    void generateGaswFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String sandboxFile, String environementFile, String extensionFile) throws ApplicationImporterException;
}
