/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Nouha boujelben
 */
public interface VelocityProcess {

    public void gaswFile(Map<Integer, Map> listInput, List<Map> listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir, String date, String sandbox, String environementFile, String extensionFileValue, String executableSandbox) throws VelocityException;

    public void wrapperScriptFile(Map<Integer, Map> listInput, List<Map> listOutput, String applicationName, String scriptFile, String applicationLocation, String environementFile, String dir, String date, String mandatoryDir) throws VelocityException;

    public String gwendiaFile(Map<Integer, Map> listInput, List<Map> listOutput, String applicationName, String description, String applicationLocation, String dir, String date, String mandatoryDir) throws VelocityException;
}
