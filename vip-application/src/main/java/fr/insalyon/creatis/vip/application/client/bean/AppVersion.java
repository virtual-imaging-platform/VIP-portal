package fr.insalyon.creatis.vip.application.client.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AppVersion implements IsSerializable {

    private String applicationName;
    private String version;
    private String descriptor;
    private String doi;
    private boolean visible;
    private List<Resource> resources;
    private List<Tag> tags;
    private Map<String, String> settings;
    private String source;
    private String note;

    public AppVersion() {}

    public AppVersion(String applicationName, String version, String descriptor,
                      Map<String, String> settings, boolean visible, String source, String note) {
        this.applicationName = applicationName;
        this.version = version;
        this.descriptor = descriptor;
        this.visible = visible;
        this.source = source;
        this.note = note;
        this.resources = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.settings = settings;
    }

    public AppVersion(String applicationName, String version, String descriptor, boolean visible) {
        this(applicationName, version, descriptor, new HashMap<>(), visible, "", "");
    }

    public AppVersion(String applicationName, String version, String descriptor,
                      String doi, Map<String, String> settings, boolean visible, String source, String note) {
        this(applicationName, version, descriptor, settings, visible, source, note);
        this.doi = doi;
    }

    public AppVersion(String applicationName, String version, String descriptor,
                      String doi, boolean visible, String source, String note, List<Resource> resources, List<Tag> tags) {
        this(applicationName, version, descriptor, new HashMap<>(), visible, source, note);
        this.doi = doi;
        this.resources = resources;
        this.tags = tags;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getVersion() {
        return version;
    }

    public String getDescriptorFilename() {
        // Return the "canonical filename" of the boutiques descriptor for this AppVersion.
        // Both applicationName and version strings are assumed to be filename-safe already,
        // except for spaces which are replaced with underscores.
        return applicationName.replace(' ', '_') + '-' + version.replace(' ','_') + ".json";
    }

    public String getDescriptor() { return descriptor; }

    public String getDoi() {
        return doi;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getSource() {
        return source;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<String> getResourcesNames() {
        return resources.stream().map(Resource::getName).collect(Collectors.toList());
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getNote() {
        return note;
    }

}
