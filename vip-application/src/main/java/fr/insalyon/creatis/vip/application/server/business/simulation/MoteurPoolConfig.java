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

import fr.insalyon.creatis.shiwapoolclient.BundleHelpers;
import fr.insalyon.creatis.shiwapoolclient.StatusManager;
import fr.insalyon.creatis.shiwapoolclient.clientservices.ClientFactory;
import fr.insalyon.creatis.shiwapoolclient.xmpp.ClientConfiguration;
import fr.insalyon.creatis.shiwapoolclient.xmpp.ClientConnection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import javax.xml.rpc.ServiceException;
import org.jivesoftware.smack.XMPPException;
import org.shiwa.desktop.data.description.workflow.SHIWAProperty;
import org.shiwa.desktop.data.util.SHIWADesktopIOException;

/**
 *
 * @author Ibrahim kallel
 */
public class MoteurPoolConfig extends WorkflowMoteurConfig {

    private Logger logger = Logger.getLogger(MoteurPoolConfig.class.getName());

    public MoteurPoolConfig() {
        super();
        String file = ClientConfiguration.getInstance().getBUNDLE_REPOSITORY();
        File fl = new File(file);
        if (!fl.exists()) {
            fl.mkdir();
        }

        StatusManager.createStatusFile();
    }

    public MoteurPoolConfig(String workflowPath, List<ParameterSweep> parameters) {
        super(workflowPath, parameters);
        String file = ClientConfiguration.getInstance().getBUNDLE_REPOSITORY();
        File fl = new File(file);
        if (!fl.exists()) {
            fl.mkdir();
        }

        StatusManager.createStatusFile();
    }

    @Override
    public String launch(String proxyFileName, String userDN) throws RemoteException, ServiceException {

        ClientConnection connection = new ClientConnection();
        try {
            connection.connect();
        } catch (XMPPException ex) {
            RemoteException e = new RemoteException("XMPPException : BAD connexion ", ex);
            throw e;
        }

        if (!connection.getConnection().isAuthenticated()) {
            System.out.println("Authentication Failed");
            RemoteException e = new RemoteException("XMPPException : Bad User Authentication ");
            throw e;
        }

        //create new bundle
        String bundleFile = ClientConfiguration.getInstance().getBUNDLE_REPOSITORY() + System.getProperty("file.separator") + "bundle" + System.currentTimeMillis() + ".zip";
        logger.info("bundle file is:  " + bundleFile);
        File wfFile = null, inputFile = null;
        try {

//            wfFile = File.createTempFile("workflow", ".gwendia");
//            inputFile = File.createTempFile("workflow", ".xml");

            wfFile = new File("/tmp/workflow.gwendia");
            inputFile = new File("/tmp/workflow.xml");

            FileWriter fw = new FileWriter(wfFile);
            fw.write(this.contentXMLworkflow);
            fw.close();

            fw = new FileWriter(inputFile);
            fw.write(this.contentXMLInput.toString());
            fw.close();
        } catch (IOException ex) {
            logger.error("input and workflow file Error:  creation files method Exception  in MoteurPoolConfig");
            RemoteException e = new RemoteException("Workflow and input Files creation Exception in MoteurPoolConfig", ex);
            throw e;
        }

        //TODO: create bundle here

        List<SHIWAProperty> list = new ArrayList<SHIWAProperty>();
        list.add(new SHIWAProperty("DN", userDN));
        list.add(new SHIWAProperty("GRID", "DIRAC"));
        list.add(new SHIWAProperty("SE", "ccsrm02.in2p3.fr"));
        list.add(new SHIWAProperty("TIMEOUT", "100000"));
//        list.add(new SHIWAProperty("RETRYCOUNT", "3"));
//        list.add(new SHIWAProperty("MULTIJOB", "1"));
//        try {
//            BundleHelpers.createBundle(wfFile, inputFile, bundleFile, list);
//        } catch (SHIWADesktopIOException ex) {
//            logger.error(" Bundle Creation Exception, Error in method createBundle in MooteurPoolConfig ");
//            RemoteException e = new RemoteException("Exception Made in bundle creation, check parameters ", ex);
//            throw e;
//        }
//
//        //submit bundle
//        int id = 0;
//        try {
//            id = ClientFactory.getUserClient().submitWorkflow(bundleFile, connection.getConnection());
//            logger.error(" Bundle instance submitted Fine " + id);
//        } catch (SHIWADesktopIOException ex) {
//            logger.error("XMPPException in submit bundle");
//            RemoteException e = new RemoteException("Cannot submit workflow to SHIWA pool", ex);
//            throw e;
//        } catch (XMPPException ex) {
//            connection.disconnectClient();
//            logger.error("XMPPException in submit bundle");
//            RemoteException e = new RemoteException("Cannot submit workflow to SHIWA pool", ex);
//            throw e;
//        }
        connection.getConnection().disconnect();
        wfFile.delete();
        inputFile.delete();
        return "";// + id;
    }

    @Override
    public void kill(String workflowID) throws RemoteException, ServiceException {
        
        ClientConnection connection = new ClientConnection();
        try {
            connection.connect();
        } catch (XMPPException ex) {
            RemoteException e = new RemoteException("XMPPException : BAD connexion ", ex);
            throw e;
        }

        if (!connection.getConnection().isAuthenticated()) {
            System.out.println("Authentication Failed");
            RemoteException e = new RemoteException("XMPPException : Bad User Authentication ");
            throw e;
        }
        try {
            ClientFactory.getUserClient().cancelWorkflow(Integer.parseInt(workflowID.split("shiwa-instance-")[1]), connection.getConnection());
        } catch (XMPPException ex) {
            connection.disconnectClient();
            logger.error("XMPPException Cannot cancel workflow execution pool");
            RemoteException e = new RemoteException("Cannot cancel workflow execution pool", ex);
            throw e;
        }
        connection.disconnectClient();
    }

    @Override
    public String getStatus(String workflowID) throws RemoteException, ServiceException {
        return "";
//        ClientConnection connection = new ClientConnection();
//        try {
//            connection.connect();
//        } catch (XMPPException ex) {
//            RemoteException e = new RemoteException("XMPPException : BAD connexion ", ex);
//            throw e;
//        }
//
//        if (!connection.getConnection().isAuthenticated()) {
//            System.out.println("Authentication Failed");
//            RemoteException e = new RemoteException("XMPPException : Bad User Authentication ");
//            throw e;
//        }
//
//        String status = null;
//        try {
//            status = ClientFactory.getUserClient().getStatus(Integer.parseInt(workflowID.split("shiwa-instance-")[1]), connection.getConnection()).toString();
//
//        } catch (InterruptedException ex) {
//            connection.disconnectClient();
//            logger.error("[Getstatus Method] InterreptedException: check thread to get status in shiwa client");
//            RemoteException e = new RemoteException("Thread in Getstatus method Fail", ex);
//            throw e;
//        } catch (XMPPException ex) {
//            connection.disconnectClient();
//            logger.error("[Getstatus Method] InterreptedException: check thread to get status in shiwa client");
//            RemoteException e = new RemoteException("Thread in Getstatus method Fail", ex);
//            throw e;
//        }
//
//        connection.disconnectClient();
//        return status;
    }
}
