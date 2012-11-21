/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.server.rpc;

import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataException;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataService;
import fr.cnrs.i3s.neusemstore.provenance.expsummaries.ExperimentSummary;
import fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SemEntity;
import java.util.ArrayList;
import java.util.List;
import fr.insalyon.creatis.vip.application.server.rpc.WorkflowServiceImpl;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            
            //test data
//            String validLFN = "lfn://lfc-biomed.in2p3.fr/grid/biomed/creatis/vip/data/users/alban_gaignard/11-10-2012_16:12:23/dataLMF.ccs.sino"; 
//            String validParam ="lfn://lfc-biomed.in2p3.fr/grid/biomed/creatis/vip/data/groups/Tutorial/TestData/Sorteo/protocol.txt";
//            String validModel ="lfn://lfc-biomed.in2p3.fr/grid/biomed/creatis/vip/data/groups/Tutorial/TestData/Sorteo/fantome.v";
//            SimulatedData pet = new SimulatedData(SimulatedData.Modality.PET,validLFN,"Sinogram",validParam,validModel,"workflow-g2p1Tf");
//            SimulatedData us = new SimulatedData(SimulatedData.Modality.US,validLFN,"Beamformed data",validParam,validModel,"workflow-g2p1Tf");
//            SimulatedData us1 = new SimulatedData(SimulatedData.Modality.US,validLFN,"RF image",validParam,validModel,"workflow-g2p1Tf");
//            SimulatedData ct = new SimulatedData(SimulatedData.Modality.CT,validLFN,"CT Projection",validParam,validModel,"workflow-g2p1Tf");
//            SimulatedData mri = new SimulatedData(SimulatedData.Modality.MRI,validLFN,"k-space",validParam,validModel,"workflow-g2p1Tf");
//            
//            list.add(pet);
//            list.add(us);
//            list.add(us1);
//            list.add(ct);
//            list.add(mri);
            
            //real data
            ExperimentSummary expeSummary = new ExperimentSummary("root", "creatis2011", "jdbc:mysql://kingkong.grid.creatis.insa-lyon.fr:3306/vip_simulated_data"); 
       //     Model summaries = RDFManagerFactory.createSDBResultsRepository("root", "creatis2011", "jdbc:mysql://kingkong.grid.creatis.insa-lyon.fr:3306/vip_simulated_data").getBaseModel();
            List<fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SimulatedData> beans = expeSummary.generateBeans();
            
            for(fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SimulatedData sd : beans){
                SimulatedData ssd = new SimulatedData();
                
                ssd.setModality(ssd.parseModality(sd.getModality().toString()));
                ssd.setFile(sd.getFile());
                ssd.setType(sd.getType().substring(sd.getType().lastIndexOf("#")+1));
                for(SemEntity se : sd.getParameters()){
                    fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(),se.getUri());         
                    ssd.getParameters().add(sse);
                }
               for(SemEntity se : sd.getModels()){
                   fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(),se.getUri());         
                   ssd.getModels().add(sse);
               }
                ssd.setSimulation(sd.getSimulation().substring(0, sd.getSimulation().lastIndexOf("#")).substring(sd.getSimulation().lastIndexOf("/") + 1));

                WorkflowServiceImpl ws = new WorkflowServiceImpl();
                try {
                    ssd.setDate(ws.getSimulation(ssd.getSimulation()).getDate());
                } catch (ApplicationException ex) {
                    Logger.getLogger(SimulatedDataServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }


                list.add(ssd);
            }
            
            return list;
       
    }
    
}
