package fr.insalyon.creatis.vip.application.client.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Resource implements IsSerializable {
    private String name;
    private boolean isPublic;
    private boolean status;
    private ResourceType type;
    private String configuration;
    private List<String> engines;
    private List<String> groups;

    public Resource(String name, boolean isPublic, boolean status, ResourceType type, String configuration, List<String> engines, List<String> groups) {
        this.name = name;
        this.isPublic = isPublic;
        this.status = status;
        this.type = type;
        this.configuration = configuration;
        this.engines = engines;
        this.groups = groups;
    }

    public Resource(String name, boolean isPublic, boolean status, String type, String configuration, List<String> engines, List<String> groups) {
        this(name, isPublic, status, ResourceType.fromString(type), configuration, engines, groups);
    }

    public Resource(String name) {
        this(name, false, false, ResourceType.getDefault(), "", new ArrayList<>(), new ArrayList<>());
    }

    public Resource() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public List<String> getEngines() {
        return this.engines;
    }

    public void setEngines(List<String> engines) {
        this.engines = engines;
    }

    public List<String> getGroups() {
        return this.groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Resource other = (Resource) obj;
        return isPublic == other.isPublic &&
               status == other.status &&
               type == other.type &&
               Objects.equals(name, other.name) &&
               Objects.equals(configuration, other.configuration) &&
               Objects.equals(engines, other.engines) &&
               Objects.equals(groups, other.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, configuration, engines, groups, isPublic, status, type);
    }
}
