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
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
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
                
                for(SemEntity se : sd.getFiles()){
                    fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(),se.getUri()); 
                    ssd.getFiles().add(sse);
                }

                ArrayList<String> toRemove = new ArrayList<String>();
                for (SemEntity se : sd.getParameters()) {
                    fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(), se.getUri());
                    ssd.getParameters().add(sse);
                    if(!sse.getUri().substring(se.getUri().lastIndexOf('#') + 1).equals("simulation-parameter")){
                        toRemove.add(sse.getLabel());
                    }
                }
                
                ArrayList<fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity> toRemove1 = new  ArrayList<fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity>();
                for(String s : toRemove){
                    for(fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity se : ssd.getParameters()){
                        if(se.getLabel().equals(s) && se.getUri().substring(se.getUri().lastIndexOf('#') + 1).equals("simulation-parameter")){
                            toRemove1.add(se);
                            break;
                        }
                            
                    }
                }
                for(fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity se : toRemove1)
                    ssd.getParameters().remove(se);
                
                for (SemEntity se : sd.getModels()) {
                    fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(), se.getUri());

                    String name = "";
                    if (sse.getUri().equals("")) {
                        SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(se.getLabel(), true);
                        name = som.getModelName();
                    }

                    sse.setName(name);
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
