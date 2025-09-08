package fr.insalyon.creatis.vip.core.client.view.application;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class ApplicationParser {

    private List<ApplicationTileRecord> applications;

    public ApplicationParser() {
        applications = new ArrayList<ApplicationTileRecord>();
        loadApplications();
    }

    public abstract void loadApplications();

    public abstract boolean parse(String applicationName, String applicationVersion);

    protected void addApplication(String applicationName, String applicationImage) {
        applications.add(new ApplicationTileRecord(applicationName,
            applicationImage));
    }

    protected void addApplication(String applicationName, String applicationVersion, String applicationImage) {

        applications.add(new ApplicationTileRecord(applicationName, applicationVersion,
                applicationImage));
    }

    public List<ApplicationTileRecord> getApplications() {
        return applications;
    }
}
