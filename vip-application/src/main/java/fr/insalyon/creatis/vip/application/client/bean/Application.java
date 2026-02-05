package fr.insalyon.creatis.vip.application.client.bean;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.client.bean.Group;

public class Application implements IsSerializable {

    private String name;
    private String citation;
    private String owner;
    private String fullName;
    private Set<Group> groups;
    private String note;

    public Application() {}

    public Application(String name, String citation) {
        this(name, null, null, citation, null);
    }

    public Application(String name, String citation, String note, Set<Group> groups) {
        this(name, null, null, citation, note, groups);
    }

    public Application(String name, String owner, String fullName, String citation, String note) {
        this(name, owner, fullName, citation, note, new HashSet<>());
    }

    public Application(String name, String owner, String citation, String note) {
        this(name, owner, null, citation, note, new HashSet<>());
    }

    public Application(String name, String owner, String fullName, String citation, String note, Set<Group> groups) {
        this.name = name;
        this.citation = citation;
        this.owner = owner;
        this.fullName = fullName;
        this.groups = groups;
        this.note = note;
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

    public Set<Group> getGroups() {
        return groups;
    }

    public Set<String> getGroupsNames() {
        return groups.stream().map(Group::getName).collect(Collectors.toSet());
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getNote() {
        return note;
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
