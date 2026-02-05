package fr.insalyon.creatis.vip.application.client.view.system.applications.version;

import java.util.List;
import java.util.Map;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VersionRecord extends ListGridRecord {
    
    public VersionRecord(String version, String descriptor, String doi, boolean isVisible,
            String source, String note, Map<String, String> settings, List<String> resources) {
        setAttribute("version", version);
        setAttribute("descriptor", descriptor);
        setAttribute("doi", doi);
        setAttribute("visible", isVisible);
        setAttribute("source", source);
        setAttribute("note", note);
        setAttribute("settings", settings);
        setAttribute("resources", resources);
    }
}
