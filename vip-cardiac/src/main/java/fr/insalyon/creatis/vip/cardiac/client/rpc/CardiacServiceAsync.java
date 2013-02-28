/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import fr.insalyon.creatis.vip.cardiac.client.view.CardiacException;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface CardiacServiceAsync {
    
     public void addSimulation(Simulation s, AsyncCallback<Void> asyncCallback) throws CardiacException;
    
    public void deleteSimulation(Simulation s, AsyncCallback<Void> asyncCallback) throws CardiacException;
    
    public void updateSimulation(Simulation s, AsyncCallback<Void> asyncCallback) throws CardiacException;
//    
//    public void addFileToSimulation(File f, Simulation s, AsyncCallback<Void> asyncCallback) throws CardiacException;
//    
//    public void deleteFileFromSimulation(File f, Simulation s, AsyncCallback<Void> asyncCallback) throws CardiacException;
//    
    public void getSimulations(AsyncCallback<List<Simulation>> asyncCallback) throws CardiacException;
    
}
