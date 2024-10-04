
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "propertyNames"
})
@Generated("jsonschema2pojo")
public class ConditionalPathTemplate {

    @JsonProperty("propertyNames")
    private Object propertyNames;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("propertyNames")
    public Object getPropertyNames() {
        return propertyNames;
    }

    @JsonProperty("propertyNames")
    public void setPropertyNames(Object propertyNames) {
        this.propertyNames = propertyNames;
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
