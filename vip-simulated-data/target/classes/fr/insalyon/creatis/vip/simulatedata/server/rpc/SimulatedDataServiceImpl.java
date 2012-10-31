/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.server.rpc;

import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataException;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glatard
 */
public class SimulatedDataServiceImpl extends AbstractRemoteServiceServlet implements SimulatedDataService{
 public SimulatedDataServiceImpl() {
 }
 @Override
    public List<SimulatedData> getSimulatedData() throws SimulatedDataException {
   
            ArrayList<SimulatedData> list = new ArrayList<SimulatedData>();
            
            String validLFN = "lfn://lfc-biomed.in2p3.fr/grid/biomed/creatis/vip/data/users/alban_gaignard/11-10-2012_16:12:23/dataLMF.ccs.sino"; 
            String validParam ="lfn://lfc-biomed.in2p3.fr/grid/biomed/creatis/vip/data/groups/Tutorial/TestData/Sorteo/protocol.txt";
            String validModel ="lfn://lfc-biomed.in2p3.fr/grid/biomed/creatis/vip/data/groups/Tutorial/TestData/Sorteo/fantome.v";
            SimulatedData pet = new SimulatedData(SimulatedData.Modality.PET,validLFN,"Sinogram",validParam,validModel,"workflow-g2p1Tf");
            SimulatedData us = new SimulatedData(SimulatedData.Modality.US,validLFN,"Beamformed data",validParam,validModel,"workflow-g2p1Tf");
             SimulatedData us1 = new SimulatedData(SimulatedData.Modality.US,validLFN,"RF image",validParam,validModel,"workflow-g2p1Tf");
            SimulatedData ct = new SimulatedData(SimulatedData.Modality.CT,validLFN,"CT Projection",validParam,validModel,"workflow-g2p1Tf");
            SimulatedData mri = new SimulatedData(SimulatedData.Modality.MRI,validLFN,"k-space",validParam,validModel,"workflow-g2p1Tf");
            
            list.add(pet);
            list.add(us);
             list.add(us1);
            list.add(ct);
            list.add(mri);
            
            return list;
       
    }
    
}
