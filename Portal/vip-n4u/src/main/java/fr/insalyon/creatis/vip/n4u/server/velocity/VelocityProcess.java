/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nouha boujelben
 */
public interface VelocityProcess {

    public void gaswFile(HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir, String date, String sandbox, String environementFile, List<String> extensionFileValues, String executableSandbox) throws VelocityException;

    public void wrapperScriptFile(HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String scriptFile, String applicationLocation, String environementFile, String dir, String date, List<String> mandatoryDir) throws VelocityException;

    public String gwendiaFile(HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String description, String applicationLocation, String dir, String date, List<String> mandatoryDir) throws VelocityException;
}
