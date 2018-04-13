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
package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.shiwapool.client.services.ShiwaClientService;
import fr.insalyon.creatis.shiwapool.common.BundleHelpers;
import fr.insalyon.creatis.shiwapool.common.Connection;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import org.apache.log4j.Logger;
import org.shiwa.desktop.data.description.workflow.SHIWAProperty;

/**
 *
 * @author Ibrahim kallel
 */
public class ShiwaPoolEngine extends WorkflowEngineInstantiator {

    private static Logger logger = Logger.getLogger(ShiwaPoolEngine.class);
    private ShiwaClientService service;
    private List<SHIWAProperty> settings;

    public ShiwaPoolEngine(Connection connection) {
        this(null, null);

        service = ShiwaClientService.getInstance();
        //TODO remove setConnection from client service
        service.setConnection(connection);
    }

    /**
     *
     * @param workflow workflow file
     * @param parameters
     */
    private ShiwaPoolEngine(File workflow, List<ParameterSweep> parameters) {
        super(workflow, parameters);

        mode = "pool";
    }

    public void setSettings(List<SHIWAProperty> settings) {
        this.settings = settings;
    }

    /**
     *
     * @param proxy proxy filename
     * @param dn user DN
     * @return
     * @throws RemoteException
     * @throws ServiceException
     */
    @Override
    public String launch(String proxy, String dn)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        String id = null;
        try {

            File bundle = File.createTempFile("bundle-", ".zip");
            bundle.deleteOnExit();
            File workflowFile = ShiwaPoolEngine.stringToFile("workflow", ".gwendia", workflow);
            File inputFile = ShiwaPoolEngine.stringToFile("input", ".xml", input);
            BundleHelpers.createGwendiaInputBundle(workflowFile, inputFile, bundle.getAbsolutePath(), settings);
            id = service.submit(bundle);
        } catch (org.jivesoftware.smack.XMPPException ex) {
            logger.error("Error launching a workflow", ex);
        } catch (java.io.IOException ex) {
            logger.error("Error launching a workflow", ex);
        }

        return id;
    }

    /**
     *
     * @param id workflow ID
     * @throws RemoteException
     * @throws ServiceException
     */
    @Override
    public void kill(String id)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        try {
            service.cancel(id);
        } catch (org.jivesoftware.smack.XMPPException ex) {
            logger.error("Error killing workflow", ex);
        }

    }

    @Override
    public SimulationStatus getStatus(String workflowID)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        SimulationStatus status = SimulationStatus.Unknown;
        String workflowStatus = "UNKNOWN";
        try {

            workflowStatus = service.getStatus(workflowID);
        } catch (org.jivesoftware.smack.XMPPException ex) {
            logger.error("Error getting simulation status", ex);
        }

        PoolStatus poolStatus = PoolStatus.valueOf(workflowStatus);
        switch (poolStatus) {

            case FINISHED:
                return SimulationStatus.Completed;

            case CANCELLED:
            case FAILED:
            case KILLED:
                return SimulationStatus.Killed;

            case SENDING:
            case SENT:
            case LAUNCHING:
            case RUNNING:
                return SimulationStatus.Running;

            case PENDING:
            case WAITING_INSTANCE:
            case QUEUED:
                return SimulationStatus.Queued;

            default:
                return SimulationStatus.Unknown;
        }
    }

    enum PoolStatus {

        PENDING, QUEUED, RUNNING, CANCELLED, FAILED, FINISHED, KILLED, WAITING_INSTANCE, LAUNCHING, UNKNOWN, SENT, SENDING;
    };

    private static File stringToFile(String name, String extension, String content)
            throws
            java.io.IOException {

        File file = File.createTempFile(name, extension);

        FileWriter writer = new FileWriter(file);
        BufferedWriter buffer = new BufferedWriter(writer);
        PrintWriter printer = new PrintWriter(buffer);
        printer.write(content);
        printer.close();
        buffer.close();
        writer.close();

        return file;
    }
}