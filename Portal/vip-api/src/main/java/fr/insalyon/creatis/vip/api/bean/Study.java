/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean;

import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class Study {

    private String identifier; // group name in our case
    private String name;
    private String description;
    private List<User> managers;
    private List<User> contributors;
    private List<User> users;
    private List<Service> services; //list of services accessible by this study

    public Study() {
    }

    public Study(String name, String description, List<User> managers, List<User> contributors, List<User> users) {
        this.identifier = name;
        this.description = description;
        this.managers = managers;
        this.contributors = contributors;
        this.users = users;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getManagers() {
        return managers;
    }

    public List<User> getContributors() {
        return contributors;
    }

    public List<User> getUsers() {
        return users;
    }

    public String toString() {
        StringBuilder output = new StringBuilder(getIdentifier() + "\n");
        for (User u : getUsers()) { // TODO: list contributors and managers too
            output.append(u.getUserName()).append("\n");
        }
        return output.toString();
    }

    /**
     *
     * @author Tristan Glatard
     */
    public static enum Role {
        USER, CONTRIBUTOR, MANAGER
    }
}
