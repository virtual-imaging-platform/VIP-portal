/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nouha boujelben
 */
public interface VelocityProcess {
    

    public void gaswFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir, String date, String sandbox, String environementFile, List<String> extensionFileValues, String executableSandbox) throws VelocityException;

    public void wrapperScriptFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String scriptFile, String applicationLocation, String environementFile, String dir, String date, List<String> mandatoryDir,String dockerImage,String commandLine) throws VelocityException;

    public String gwendiaFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String description, String applicationLocation, String dir, File theDirApp, String date, List<String> mandatoryDir,String vo) throws VelocityException;
}
