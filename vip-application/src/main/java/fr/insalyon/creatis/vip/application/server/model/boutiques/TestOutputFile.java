
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "md5-reference"
})
@Generated("jsonschema2pojo")
public class TestOutputFile {

    /**
     * Id referring to an output-file
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("Id referring to an output-file")
    private String id;
    /**
     * MD5 checksum string to match against the MD5 checksum of the output-file specified by the id object
     * 
     */
    @JsonProperty("md5-reference")
    @JsonPropertyDescription("MD5 checksum string to match against the MD5 checksum of the output-file specified by the id object")
    private String md5Reference;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * Id referring to an output-file
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Id referring to an output-file
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * MD5 checksum string to match against the MD5 checksum of the output-file specified by the id object
     * 
     */
    @JsonProperty("md5-reference")
    public String getMd5Reference() {
        return md5Reference;
    }

    /**
     * MD5 checksum string to match against the MD5 checksum of the output-file specified by the id object
     * 
     */
    @JsonProperty("md5-reference")
    public void setMd5Reference(String md5Reference) {
        this.md5Reference = md5Reference;
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
