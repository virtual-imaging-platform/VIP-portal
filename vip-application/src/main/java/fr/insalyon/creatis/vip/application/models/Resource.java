package fr.insalyon.creatis.vip.application.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.models.Group;

public class Resource implements IsSerializable {
    private String name;
    private boolean status;
    private ResourceType type;
    private String configuration;
    private List<String> engines;
    private List<Group> groups;

    public Resource(String name, boolean status, ResourceType type, String configuration, List<String> engines, List<Group> groups) {
        this.name = name;
        this.status = status;
        this.type = type;
        this.configuration = configuration;
        this.engines = engines;
        this.groups = groups;
    }

    public Resource(String name, boolean status, String type, String configuration, List<String> engines, List<Group> groups) {
        this(name, status, ResourceType.fromString(type), configuration, engines, groups);
    }

    public Resource(String name) {
        this(name, false, ResourceType.getDefault(), "", new ArrayList<>(), new ArrayList<>());
    }

    public Resource() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Group> getGroups() {
        return groups;
    }

    public List<String> getGroupsNames() {
        return groups.stream().map(Group::getName).collect(Collectors.toList());
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Resource other = (Resource) obj;
        return status == other.status &&
               type == other.type &&
               Objects.equals(name, other.name) &&
               Objects.equals(configuration, other.configuration) &&
               Objects.equals(engines, other.engines) &&
               Objects.equals(groups, other.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, configuration, engines, groups, status, type);
    }
}
