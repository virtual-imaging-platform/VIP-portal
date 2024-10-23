
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "invocation",
    "assertions"
})
@Generated("jsonschema2pojo")
public class Test {

    /**
     * Name of the test-case
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Name of the test-case")
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("invocation")
    private Invocation invocation;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("assertions")
    private Assertions assertions;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * Name of the test-case
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Name of the test-case
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("invocation")
    public Invocation getInvocation() {
        return invocation;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("invocation")
    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("assertions")
    public Assertions getAssertions() {
        return assertions;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("assertions")
    public void setAssertions(Assertions assertions) {
        this.assertions = assertions;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
