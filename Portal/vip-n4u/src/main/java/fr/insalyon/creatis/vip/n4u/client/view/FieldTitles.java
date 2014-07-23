/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

/**
 *
 * @author Nouha Boujelben
 */
public enum FieldTitles {

    SandboxFile("Sandbox File"),
    ApplicationName("Application Name <font color=red>(*)</font>"),
    ApplicationVersion("Version <font color=red>(*)</font>"),
    ApplicationLocation("Application Location <font color=red>(*)</font>"),
    EnvironementFile("Environement File"),
    MainExecutable("Main Executable <font color=red>(*)</font>");
    private String name;

    FieldTitles(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
