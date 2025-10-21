package fr.insalyon.creatis.vip.application.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.models.Group;

public class Application implements IsSerializable {

    private String name;
    private String citation;
    private String owner;
    private String fullName;
    private List<Group> groups;

    public Application() {}

    public Application(String name, String citation) {
        this(name, null, null, citation);
    }

    public Application(String name, String citation, List<Group> groups) {
        this(name, null, null, citation, groups);
    }

    public Application(String name, String owner, String fullName, String citation) {
        this(name, owner, fullName, citation, new ArrayList<>());
    }

    public Application(String name, String owner, String citation) {
        this(name, owner, null, citation, new ArrayList<>());
    }

    public Application(String name, String owner, String fullName, String citation, List<Group> groups) {
        this.name = name;
        this.citation = citation;
        this.owner = owner;
        this.fullName = fullName;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public String getCitation() {
        return citation;
    }

    public String getOwner() {
        return owner;
    }

    public void removeOwner() {
        owner = null;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getFullName() {
        return fullName;
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

        Application other = (Application) obj;
        return Objects.equals(name, other.name) &&
               Objects.equals(citation, other.citation) &&
               Objects.equals(owner, other.owner) &&
               Objects.equals(fullName, other.fullName) &&
               Objects.equals(groups, other.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, citation, owner, fullName, groups);
    }
}
