package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;

import java.nio.file.Path;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;

/**
 *
 * @author Rafael Ferreira da Silva
 * @author kboulebiar
 */
public abstract class WorkflowEngineInstantiator {

    static enum MoteurStatus {
        RUNNING, COMPLETE, FAILED, TERMINATED, UNKNOWN
    }

    public abstract String launch(String addressWS, String workflowContent, String inputs, String settings, String executorConfig, String proxyFileName)
            throws java.rmi.RemoteException, javax.xml.rpc.ServiceException, BusinessException;

    public abstract void kill(String addressWS, String workflowID)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException, BusinessException;

    public abstract SimulationStatus getStatus(String addressWS, String workflowID)
            throws
            java.rmi.RemoteException,
            javax.xml.rpc.ServiceException, BusinessException;

    protected void loadTrustStore(Server server) {
        // Configuration SSL
        if (Path.of(server.getTruststoreFile()).toFile().exists()) {
            System.setProperty("javax.net.ssl.trustStore", server.getTruststoreFile());
            System.setProperty("javax.net.ssl.trustStorePassword", server.getTruststorePass());
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        }
    }
}
