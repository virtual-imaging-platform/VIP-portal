package fr.insalyon.creatis.vip.core.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class Account implements IsSerializable {

    private String name;
    private List<Group> groups;

    public Account() {
    }

    public Account(String name) {
        this.name = name;
        this.groups = new ArrayList<Group>();
    }

    public Account(String name, List<Group> groups) {
        this.name = name;
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
