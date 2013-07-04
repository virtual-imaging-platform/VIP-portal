/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulatedata.server.rpc;

import fr.cnrs.i3s.neusemstore.persistence.RDFManagerFactory;
import fr.cnrs.i3s.neusemstore.provenance.expsummaries.ExperimentSummary;
import fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SemEntity;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.rpc.WorkflowServiceImpl;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataException;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
public class SimulatedDataServiceImpl extends AbstractRemoteServiceServlet implements SimulatedDataService {

    private static final Logger logger = Logger.getLogger(SimulatedDataServiceImpl.class);

    public SimulatedDataServiceImpl() {
    }

    @Override
    public List<SimulatedData> getSimulatedData() throws SimulatedDataException {

        ArrayList<SimulatedData> list = new ArrayList<SimulatedData>();

        //real data
        ExperimentSummary expeSummary = new ExperimentSummary(Server.getInstance().getSimulatedDataDBUser(), Server.getInstance().getSimulatedDataDBPass(),Server.getInstance().getSimulatedDataDBURL());

//        Model summaries = RDFManagerFactory.createSDBResultsRepository("root", "creatis2011", "jdbc:mysql://kingkong.grid.creatis.insa-lyon.fr:3306/vip_simulated_data").getBaseModel();
        List<fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SimulatedData> beans = expeSummary.generateBeans();

        for (fr.cnrs.i3s.neusemstore.provenance.expsummaries.dto.SimulatedData sd : beans) {
            SimulatedData ssd = new SimulatedData();

            ssd.setModality(ssd.parseModality(sd.getModality().toString()));

            for (SemEntity se : sd.getFiles()) {
                fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(), se.getUri());
                ssd.getFiles().add(sse);
            }

            ArrayList<String> toRemove = new ArrayList<String>();
            for (SemEntity se : sd.getParameters()) {
                fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(), se.getUri());
                ssd.getParameters().add(sse);
                if (!sse.getUri().substring(se.getUri().lastIndexOf('#') + 1).equals("simulation-parameter")) {
                    toRemove.add(sse.getLabel());
                }
            }

            ArrayList<fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity> toRemove1 = new ArrayList<fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity>();
            for (String s : toRemove) {
                for (fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity se : ssd.getParameters()) {
                    if (se.getLabel().equals(s) && se.getUri().substring(se.getUri().lastIndexOf('#') + 1).equals("simulation-parameter")) {
                        toRemove1.add(se);
                        break;
                    }

                }
            }
            for (fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity se : toRemove1) {
                ssd.getParameters().remove(se);
            }

            for (SemEntity se : sd.getModels()) {
                fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity sse = new fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity(se.getLabel(), se.getUri());

                String name = "";
                if (sse.getUri().equals("")) {
                    try {
                        SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(se.getLabel(), true);
                        name = som.getModelName();
                    } catch (NullPointerException e) {
                        logger.warn("Cannot reconstruct model " + se.getLabel() + " from triple store");
                    }
                }

                sse.setName(name);
                ssd.getModels().add(sse);
            }
            ssd.setSimulation(sd.getSimulation().substring(0, sd.getSimulation().lastIndexOf("#")).substring(sd.getSimulation().lastIndexOf("/") + 1));

            WorkflowServiceImpl ws = new WorkflowServiceImpl();
            try {
                Simulation s = ws.getSimulation(ssd.getSimulation());
                if(s!=null){
                    ssd.setDate(s.getDate());
                    ssd.setName(s.getSimulationName());
                }

            } catch (ApplicationException ex) {
                logger.warn("Cannot set simulation date or name for "+ssd.getSimulation());
                ssd.setName("unknown");
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
            File dir = new File(Server.getInstance().getWorkflowsPath() + File.separator + subdir + File.separator);
            if (!dir.exists()) {
                dir.mkdir();
            }
            f = File.createTempFile("dump-", ".rdf", dir);

            RDFManagerFactory.createSDBResultsRepository("root", "creatis2011", "jdbc:mysql://kingkong.grid.creatis.insa-lyon.fr:3306/vip_simulated_data").dumpToFile(f);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SimulatedDataException(ex);
        }
        return File.separator + subdir + File.separator + f.getName();

    }
}
