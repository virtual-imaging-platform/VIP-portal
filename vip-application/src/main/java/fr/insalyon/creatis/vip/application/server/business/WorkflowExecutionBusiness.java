/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.WebServiceEngine;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Wrapper on WebServiceEngine to configure it and to create a Workflow object
 * after a launch.
 *
 * WorkflowExecutionBusiness and WebServiceEngine both have spring prototype
 * scope, so each WorkflowExecutionBusiness use creates a new intance with a
 * dedicated WebServiceEngine instance that it will wrap.
 * This is needed as each time an engine is used, the endpoint can be different.
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Scope("prototype")
public class WorkflowExecutionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private WebServiceEngine engine;
    private String engineEndpoint;

    @Autowired
    public final void setServer(Server server) {
        this.server = server;
    }

    /*
    WebServiceEngine is also prototype scoped, this creates a new instance
    every time. 
    */
    @Autowired
    public final void setEngine(WebServiceEngine engine) {
        this.engine = engine;
    }

    public WorkflowExecutionBusiness(String engineEndpoint) throws BusinessException {

        //HACK for testing while still having simulations launched with VIP 1.16.1; to be removed before getting in production or replaced with a proper constant
        if(engineEndpoint == null){
            logger.info("WorkflowExecutionBusiness, endpoint is null, setting it to http://data-manager.grid.creatis.insa-lyon.fr/cgi-bin/m2Server-gasw3.1/moteur_server");
            engineEndpoint="http://data-manager.grid.creatis.insa-lyon.fr/cgi-bin/m2Server-gasw3.1/moteur_server";
        }
        this.engineEndpoint = engineEndpoint;
    }

    @PostConstruct
    public final void configureWebServiceEngine() {
        engine.setAddressWS(engineEndpoint);
        String settings = "GRID=DIRAC\n"
                + "SE=ccsrm02.in2p3.fr\n"
                + "TIMEOUT=100000\n"
                + "RETRYCOUNT=3\n"
                + "MULTIJOB=1";
        engine.setSettings(settings);

    }

    public Workflow launch(AppVersion appVersion, User user, String simulationName, String workflowPath, List<ParameterSweep> parameters) throws BusinessException {
        try {
            engine.setWorkflow(new File(workflowPath));
            engine.setInput(parameters);
            String launchID = engine.launch(server.getServerProxy(server.getVoName()), null);
            String workflowID = engine.getSimulationId(launchID);

            return new Workflow(workflowID, user.getFullName(),
                    WorkflowStatus.Running,
                    new Date(), null, simulationName, appVersion.getApplicationName(), appVersion.getVersion(), "",
                    engine.getAddressWS(), null);

        } catch (javax.xml.rpc.ServiceException | java.rmi.RemoteException ex) {
            logger.error("Error launching simulation {} ({}/{})",
                    simulationName, appVersion.getApplicationName(), appVersion.getVersion(), ex);
            throw new BusinessException(ex);
        }
    }

    public SimulationStatus getStatus(String simulationID) throws BusinessException {

        SimulationStatus status = SimulationStatus.Unknown;
        try {
            status = engine.getStatus(simulationID);
        } catch (javax.xml.rpc.ServiceException ex) {
            logger.error("Error getting status for {}", simulationID, ex);
            throw new BusinessException(ex);
        } catch (java.rmi.RemoteException ex) {
            logger.error("Error getting status for {}. Ignoring", simulationID, ex);
        }

        return status;
    }

    public void kill(String simulationID) throws BusinessException {

        try {
            engine.kill(simulationID);

        } catch (javax.xml.rpc.ServiceException ex) {
            logger.error("Error killing simulation {}", simulationID, ex);
            throw new BusinessException(ex);
        } catch (java.rmi.RemoteException ex) {
            logger.error("Error killing simulation {}. Ignoring", simulationID, ex);
        }
    }
}
