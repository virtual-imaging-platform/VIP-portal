/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.server.rpc;

import com.hp.hpl.jena.rdf.model.Model;
import fr.cnrs.i3s.neusemstore.persistence.RDFManager;
import fr.cnrs.i3s.neusemstore.persistence.RDFManagerFactory;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataException;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataService;
import fr.cnrs.i3s.neusemstore.provenance.expsummaries.ExperimentSummary;
import fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SemEntity;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import fr.insalyon.creatis.vip.application.server.rpc.WorkflowServiceImpl;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.models.client.view.ModelException;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import java.io.File;
import java.util.logging.Level;
import org.apache.log4j.Logger;


/**
 *
 * @author glatard
 */
public class SimulatedDataServiceImpl extends AbstractRemoteServiceServlet implements SimulatedDataService{
    
 private static final Logger logger = Logger.getLogger(SimulatedDataServiceImpl.class);
    
 public SimulatedDataServiceImpl() {
 }
 @Override
    public List<SimulatedData> getSimulatedData() throws SimulatedDataException {
   
            ArrayList<SimulatedData> list = new ArrayList<SimulatedData>();
            
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
                        try{
                        SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(se.getLabel(), true);
                        name = som.getModelName();
                        } catch(NullPointerException e){
                            logger.warn("Cannot reconstruct model "+se.getLabel()+" from triple store");
                        }
                    }

                    sse.setName(name);
                    ssd.getModels().add(sse);
                }
                ssd.setSimulation(sd.getSimulation().substring(0, sd.getSimulation().lastIndexOf("#")).substring(sd.getSimulation().lastIndexOf("/") + 1));

                WorkflowServiceImpl ws = new WorkflowServiceImpl();
                try {
                    ssd.setDate(ws.getSimulation(ssd.getSimulation()).getDate());
                    ssd.setName(ws.getSimulation(ssd.getSimulation()).getSimulationName());
                    
                } catch (ApplicationException ex) {
                    logger.warn("Cannot set simulation date or name");
                }


                list.add(ssd);
            }
            
            return list;
       
    }
    
     @Override
    public String getRdfDump() throws SimulatedDataException {
        logger.trace("Getting RDF dump of simulated data repository");
        String subdir = "tmp";
        File f = null;
        try {
            File dir = new File(Server.getInstance().getWorkflowsPath()+File.separator+subdir+File.separator);
            if(!dir.exists())
                dir.mkdir();
            f = File.createTempFile("dump-", ".rdf", dir);
            
            RDFManagerFactory.createSDBResultsRepository("root", "creatis2011", "jdbc:mysql://kingkong.grid.creatis.insa-lyon.fr:3306/vip_simulated_data").dumpToFile(f);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SimulatedDataException(ex);
        }
        return File.separator+subdir+File.separator+f.getName();

    }
}
