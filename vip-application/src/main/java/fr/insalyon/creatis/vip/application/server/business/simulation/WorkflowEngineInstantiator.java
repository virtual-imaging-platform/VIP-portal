package fr.insalyon.creatis.vip.application.server.business.simulation;

import java.nio.file.Path;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.Server;

public abstract class WorkflowEngineInstantiator {

    static enum MoteurStatus {
        RUNNING, COMPLETE, FAILED, TERMINATED, UNKNOWN
    }

    public abstract String launch(String addressWS, String workflowContent, String inputs, String settings, String executorConfig, String proxyFileName)
            throws VipException, Exception;

    public abstract void kill(String addressWS, String workflowID)
            throws VipException;

    public abstract SimulationStatus getStatus(String addressWS, String workflowID)
            throws VipException;

    protected void loadTrustStore(Server server) {
        // Configuration SSL
        if (Path.of(server.getTruststoreFile()).toFile().exists()) {
            System.setProperty("javax.net.ssl.trustStore", server.getTruststoreFile());
            System.setProperty("javax.net.ssl.trustStorePassword", server.getTruststorePass());
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        }
    }
}
