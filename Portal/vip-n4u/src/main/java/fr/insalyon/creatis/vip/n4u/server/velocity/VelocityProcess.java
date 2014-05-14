/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import fr.insalyon.creatis.vip.n4u.client.rpc.N4uException;
import java.util.ArrayList;

/**
 *
 * @author Nouha boujelben
 */
public interface VelocityProcess {
    public void gassFile(ArrayList listInput, ArrayList listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir) throws VelocityException;

    public void wrapperScriptFile(ArrayList listInput, ArrayList listOutput, String applicationName, String scriptFile, String applicationLocation, String dir) throws VelocityException;

    public String gwendiaFile(ArrayList listInput, ArrayList listOutput, String applicationName, String description, String applicationLocation, String dir) throws VelocityException;
}
