package fr.insalyon.creatis.vip.application.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.server.inter.DataViews;

@JsonView(DataViews.User.class)
public class AppVersion implements IsSerializable {
    private String applicationName;
    private String version;
    private String descriptor;
    private String doi;
    private boolean visible;
    private Set<Resource> resources;
    private Set<Tag> tags;
    private Map<String, String> settings;
    private String source;
    private String note;

    public AppVersion() {
    }

    public AppVersion(String applicationName, String version, String descriptor,
                      Map<String, String> settings, boolean visible, String source, String note) {
        this.applicationName = applicationName;
        this.version = version;
        this.descriptor = descriptor;
        this.visible = visible;
        this.source = source;
        this.note = note;
        this.resources = new HashSet<>();
        this.tags = new HashSet<>();
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
                      String doi, boolean visible, String source, String note, Set<Resource> resources, Set<Tag> tags) {
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

    @JsonIgnore
    public String getDescriptorFilename() {
        // Return the "canonical filename" of the boutiques descriptor for this
        // AppVersion.
        // Both applicationName and version strings are assumed to be filename-safe
        // already,
        // except for spaces which are replaced with underscores.
        return applicationName.replace(' ', '_') + '-' + version.replace(' ', '_') + ".json";
    }

    public String getDescriptor() {
        return descriptor;
    }

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

    public Set<Resource> getResources() {
        return resources;
    }

    public List<String> getResourcesNames() {
        return resources.stream().map(Resource::getName).collect(Collectors.toList());
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AppVersion))
            return false;

        AppVersion other = (AppVersion) o;

        return visible == other.visible &&
                Objects.equals(applicationName, other.applicationName) &&
                Objects.equals(version, other.version) &&
                Objects.equals(descriptor, other.descriptor) &&
                Objects.equals(doi, other.doi) &&
                Objects.equals(resources, other.resources) &&
                Objects.equals(tags, other.tags) &&
                Objects.equals(settings, other.settings) &&
                Objects.equals(source, other.source) &&
                Objects.equals(note, other.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                applicationName,
                version,
                descriptor,
                doi,
                visible,
                resources,
                tags,
                settings,
                source,
                note);
    }
}
