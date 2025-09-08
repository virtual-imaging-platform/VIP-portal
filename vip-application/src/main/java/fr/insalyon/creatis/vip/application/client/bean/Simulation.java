package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import java.util.Date;

/**
 * This is almost the same as the workflow class from workflowsdb
 *
 * @author Rafael Ferreira da Silva
 */
public class Simulation implements IsSerializable {

    private String id;
    private String applicationName;
    private String applicationVersion;
    private String applicationClass;
    private String simulationName;
    private String userName;
    private Date date;
    private SimulationStatus status;
    private String engine;
    private String tags;

    public Simulation() {
    }

    public Simulation(String application, String applicationVersion,
            String applicationClass, String id, String userName, Date date, 
            String simulationName, String status, String engine, String tags) {

        this.applicationName = application;
        this.applicationVersion = applicationVersion;
        this.applicationClass = applicationClass;
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.simulationName = simulationName;
        this.status = SimulationStatus.valueOf(status);
        this.engine = engine;
        this.tags = tags;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public String getApplicationClass() {
        return applicationClass;
    }

    public Date getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }

    public String getID() {
        return id;
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public void setStatus(SimulationStatus status) {
        this.status = status;
    }
    
    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return applicationName + "\n" + id + "\n" + userName + "\n" + date;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }
    
}
