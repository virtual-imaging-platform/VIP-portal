
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    "exit-code",
    "output-files"
})
@Generated("jsonschema2pojo")
public class Assertions {

    /**
     * Expected code returned by the program.
     * 
     */
    @JsonProperty("exit-code")
    @JsonPropertyDescription("Expected code returned by the program.")
    private Integer exitCode;
    @JsonProperty("output-files")
    private List<TestOutputFile> testOutputFiles = new ArrayList<TestOutputFile>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * Expected code returned by the program.
     * 
     */
    @JsonProperty("exit-code")
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * Expected code returned by the program.
     * 
     */
    @JsonProperty("exit-code")
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    @JsonProperty("output-files")
    public List<TestOutputFile> getOutputFiles() {
        return testOutputFiles;
    }

    @JsonProperty("output-files")
    public void setOutputFiles(List<TestOutputFile> testOutputFiles) {
        this.testOutputFiles = testOutputFiles;
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
