package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PipelineParameter {

    private String name;
    private ParameterType type;
    private Boolean isOptional;
    private Boolean isReturnedValue;
    private Object defaultValue;
    private String description;

    public PipelineParameter(){}
    
    public PipelineParameter(String name, ParameterType type, Boolean isOptional, Boolean isReturnedValue, Object defaultValue, String description) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
        this.isReturnedValue = isReturnedValue;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ParameterType getType() {
        return type;
    }

    @JsonProperty("isOptional") // Jackson default is 'optional'
    public Boolean isOptional() {
        return isOptional;
    }

    @JsonProperty("isReturnedValue") // Jackson default is 'returnedValue'
    public Boolean isReturnedValue() {
        return isReturnedValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }
}
