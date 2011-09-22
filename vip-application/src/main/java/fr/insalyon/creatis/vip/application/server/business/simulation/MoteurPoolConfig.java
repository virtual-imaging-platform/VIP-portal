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
import java.util.logging.Level;
import org.apache.log4j.Logger;
import javax.xml.rpc.ServiceException;
import org.jivesoftware.smack.XMPPException;
import org.shiwa.desktop.data.description.workflow.SHIWAProperty;
import org.shiwa.desktop.data.util.SHIWADesktopIOException;

/**
 *
 * @author Ibrahim kallel
 *
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

            wfFile =  new File("/tmp/workflow.gwendia");
            inputFile =  new File("/tmp/workflow.xml");

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
        list.add(new SHIWAProperty("RETRYCOUNT", "3"));
        list.add(new SHIWAProperty("MULTIJOB", "1"));
        try {
            BundleHelpers.createBundle(wfFile, inputFile, bundleFile, list);
        } catch (SHIWADesktopIOException ex) {
            logger.error (" Bundle Creation Exception, Error in method createBundle in MooteurPoolConfig ");
            RemoteException e = new RemoteException("Exception Made in bundle creation, check parameters ", ex);
            throw e;
        }

        //submit bundle
        int id = 0;
        try {
            id = ClientFactory.getUserClient().submitWorkflow(bundleFile, connection.getConnection());
            logger.error ( " Bundle instance submitted Fine " + id);
        } catch (SHIWADesktopIOException ex) {
            logger.error ( "XMPPException in submit bundle");
            RemoteException e = new RemoteException("Cannot submit workflow to SHIWA pool", ex);
            throw e;
        } catch (XMPPException ex) {
            connection.disconnectClient();
            logger.error ("XMPPException in submit bundle");
            RemoteException e = new RemoteException("Cannot submit workflow to SHIWA pool", ex);
            throw e;
        }
        connection.getConnection().disconnect();
        wfFile.delete();
        inputFile.delete();
        return "" + id;
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
            logger.error ( "XMPPException Cannot cancel workflow execution pool");
            RemoteException e = new RemoteException("Cannot cancel workflow execution pool", ex);
            throw e;
        }
        connection.disconnectClient();
    }

    @Override
    public String getStatus(String workflowID) throws RemoteException, ServiceException {
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

        String status = null;
            try {
                status = ClientFactory.getUserClient().getStatus(Integer.parseInt(workflowID.split("shiwa-instance-")[1]), connection.getConnection()).toString();

        } catch (InterruptedException ex) {
            connection.disconnectClient();
            logger.error ("[Getstatus Method] InterreptedException: check thread to get status in shiwa client");
            RemoteException e = new RemoteException("Thread in Getstatus method Fail", ex);
            throw e;
        } catch (XMPPException ex){
            connection.disconnectClient();
            logger.error ("[Getstatus Method] InterreptedException: check thread to get status in shiwa client");
            RemoteException e = new RemoteException("Thread in Getstatus method Fail", ex);
            throw e;
        }

        connection.disconnectClient();
        return status;
    }
}
