package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SimulationRecord extends ListGridRecord {

    public SimulationRecord() {
    }

    public SimulationRecord(String simulationName, String application,
            String applicationVersion, String applicationClass,
            SimulationStatus status, String simulationId, String user, Date date) {

        setAttribute("statusIco", "ico_" + status.name().toLowerCase());
        setAttribute("application", application);
        setAttribute("applicationVersion", applicationVersion);
        setAttribute("applicationClass", applicationClass);
        setAttribute("status", status.name());
        setAttribute("simulationId", simulationId);
        setAttribute("user", user);
        setAttribute("date", date);
        if (simulationName == null || simulationName.equals("null") || simulationName.isEmpty()) {
            setAttribute("simulationName", simulationId);
        } else {
            setAttribute("simulationName", simulationName);
        }
    }

    public String getStatusIco() {
        return getAttributeAsString("statusIco");
    }

    public String getApplication() {
        return getAttributeAsString("application");
    }

    public String getApplicationVersion() {
        return getAttributeAsString("applicationVersion");
    }

    public String getStatus() {
        return getAttributeAsString("status");
    }

    public String getSimulationId() {
        return getAttributeAsString("simulationId");
    }

    public String getUser() {
        return getAttributeAsString("user");
    }

    public String getDate() {
        return getAttributeAsString("date");
    }

    public String getSimulationName() {
        return getAttributeAsString("simulationName");
    }
}
