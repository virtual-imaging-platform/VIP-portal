/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client;

/**
 *
 * @author Nouha Boujelben
 */
public enum EnumFieldTitles {
    
    CommandLine("Command Line"),
    DockerImage("Docker Image"),
    DockerIndex("Docker Index"),
    SchemaVersion("Schema Version"),
    SandboxFile("Sandbox File"),
    ApplicationName("Application Name <font color=red>(*)</font>"),
    ApplicationVersion("Version <font color=red>(*)</font>"),
    ApplicationLocation("Application Location <font color=red>(*)</font>"),
    EnvironementFile("Environement File"),
    ExtensionFile("Extension File"),
    MainExecutable("Main Executable <font color=red>(*)</font>");
    
    
    
    private String name;
    EnumFieldTitles(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
