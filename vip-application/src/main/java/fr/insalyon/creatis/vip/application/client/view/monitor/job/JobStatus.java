package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public enum JobStatus {

    Failed("#d64949", "Failed"),
    Running_with_erros("#65D3A1", "Running with errors"),
    Queued_with_errors("#D3864C", "Queued with errors"),
    Running("#8CC653", "Running"),
    Queued("#DBA400", "Queued"),
    Completed("#287fd6", "Completed"),
    Held("#9CAB4C", "Held");
    //
    private String color;
    private String description;

    private JobStatus(String color, String description) {

        this.color = color;
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> valuesAsList() {

        List<String> list = new ArrayList<String>();
        for (JobStatus status : values()) {
            list.add(status.name());
        }
        return list;
    }
}
