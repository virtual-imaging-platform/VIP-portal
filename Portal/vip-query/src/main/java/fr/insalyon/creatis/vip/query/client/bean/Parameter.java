package fr.insalyon.creatis.vip.query.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha Boujelben
 */
public class Parameter implements IsSerializable {

    Long parameterID;
    String name;
    String type;
    String description;
    String example;
    Long queryVersionID;

    public Parameter(Long queryVersionID) {
        this.queryVersionID = queryVersionID;
    }

    public Parameter() {
    }

    public Parameter(String name, String type, String description, String example) {

        this.name = name;
        this.type = type;
        this.description = description;
        this.example = example;

    }

    public Parameter(String name, String type, String description, String example, Long parameterID) {

        this.name = name;
        this.type = type;
        this.description = description;
        this.example = example;
        this.parameterID = parameterID;

    }

    public Parameter(String name, String type, Long queryVersionID) {
        this.name = name;
        this.type = type;
        this.queryVersionID = queryVersionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParameterID() {
        return parameterID;
    }

    public void setParameterID(Long parameterID) {
        this.parameterID = parameterID;
    }

    public Long getQueryVersionID() {
        return queryVersionID;
    }

    public void setQueryVersionID(Long queryVersionID) {
        this.queryVersionID = queryVersionID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}