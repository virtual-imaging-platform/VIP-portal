/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import fr.insalyon.creatis.vip.cardiac.client.view.CardiacException;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface CardiacService extends RemoteService{

    public static final String SERVICE_URI = "/cardiacservice";

    public static class Util {

        public static CardiacServiceAsync getInstance() {

            CardiacServiceAsync instance = (CardiacServiceAsync) GWT.create(CardiacService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public void addSimulation(Simulation s) throws CardiacException;
    
    public void deleteSimulation(Simulation s) throws CardiacException;
    
    public void updateSimulation(Simulation s) throws CardiacException;
//    
//    public void addFileToSimulation(File f, Simulation s) throws CardiacException;
//    
//    public void deleteFileFromSimulation(File f, Simulation s) throws CardiacException;
    
    public List<Simulation> getSimulations() throws CardiacException;
}
