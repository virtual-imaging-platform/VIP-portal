/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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
package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.shiwapool.client.ClientConfiguration;
import fr.insalyon.creatis.shiwapool.client.ClientMessageProcessor;
import fr.insalyon.creatis.shiwapool.client.services.ShiwaUserClientService;
import fr.insalyon.creatis.shiwapool.common.BundleHelpers;
import fr.insalyon.creatis.shiwapool.common.Connection;
import fr.insalyon.creatis.shiwapool.common.FileTransfer;
import fr.insalyon.creatis.shiwapool.common.Receiver;
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
public class PoolExecutor extends WorkflowEngineInstance {

    private Logger logger = Logger.getLogger(PoolExecutor.class);
    private ClientConfiguration configuration;
    ShiwaUserClientService clientService;
    private Connection connection;
    private List<SHIWAProperty> settings;

    public PoolExecutor() {
        this(null, null);

        configuration = ClientConfiguration.getInstance();
        connection = new Connection(configuration.getServerHost(), configuration.getServerPort());
    }

    /**
     *
     * @param workflow workflow file
     * @param parameters
     */
    PoolExecutor(File workflow, List<ParameterSweep> parameters) {
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

        Receiver receiver = new Receiver(connection, ClientMessageProcessor.getInstance());
        (new Thread(receiver)).start();

        FileTransfer carrier = new FileTransfer(connection, configuration.getBundleRepository(), "CLIENT");
        (new Thread(carrier)).start();

        String id = null;
        try {

            connection.connect(configuration.getClientName(), configuration.getClientPassword());
            File bundle = File.createTempFile("bundle-", ".zip");
            bundle.deleteOnExit();
            File workflowFile = PoolExecutor.stringToFile("workflow", ".gwendia", workflow);
            File inputFile = PoolExecutor.stringToFile("input", ".xml", input);
            BundleHelpers.createGwendiaInputBundle(workflowFile, inputFile, bundle.getAbsolutePath(), null);
            clientService = ShiwaUserClientService.getInstance();
            //TODO remove setConnection from client service
            clientService.setConnection(connection);
            id = clientService.submit(bundle);
        } catch (org.jivesoftware.smack.XMPPException ex) {
            logger.error(null, ex);
        } catch (java.io.IOException ex) {
            logger.error(null, ex);
        } finally {

            connection.disconnectClient();

            synchronized (receiver) {
                receiver.notifyAll();
            }

            synchronized (carrier) {
                carrier.notifyAll();
            }
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
//            ClientFactory.getUserClient().cancelWorkflow(Integer.parseInt(id.split("shiwa-instance-")[1]), connection.getConnection());
    }

    @Override
    public String getStatus(String workflowID)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException {

        Receiver receiver = new Receiver(connection, ClientMessageProcessor.getInstance());
        (new Thread(receiver)).start();

        String status = null;
        try {

            connection.connect(configuration.getClientName(), configuration.getClientPassword());
            clientService.setConnection(connection);
            status = clientService.getStatus(workflowID).name();
        } catch (org.jivesoftware.smack.XMPPException ex) {
            logger.error(null, ex);
        } catch (java.io.FileNotFoundException ex) {
            logger.error(null, ex);
        } catch (java.io.IOException ex) {
            logger.error(null, ex);
        } catch (InterruptedException ex) {
            logger.error(null, ex);
        } finally {
            connection.disconnectClient();

            synchronized (receiver) {
                receiver.notifyAll();
            }
        }

        return status;
    }

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