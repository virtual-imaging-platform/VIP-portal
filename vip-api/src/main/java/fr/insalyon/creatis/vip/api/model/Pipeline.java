package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Map;

public class Pipeline {

    private String identifier;
    private String name;
    private String description;
    private String version;
    private ArrayList<PipelineParameter> parameters;
    private boolean canExecute;
    @JsonIgnore
    private Map<String, String> overriddenInputs;

    public Pipeline() {
    }
    
     public Pipeline(String identifier, String name, String version) {
        this.identifier = identifier;
        this.name = name;       
        this.version = version;
        this.canExecute = true;
        parameters = new ArrayList<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public ArrayList<PipelineParameter> getParameters() {
        return parameters;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("canExecute") // jackson only take into account getter by default
    public boolean canExecute(){
        return canExecute;
    }

    public Map<String, String> getOverriddenInputs() {
        return overriddenInputs;
    }

    public void setOverriddenInputs(Map<String, String> overriddenInputs) {
        this.overriddenInputs = overriddenInputs;
    }
}
