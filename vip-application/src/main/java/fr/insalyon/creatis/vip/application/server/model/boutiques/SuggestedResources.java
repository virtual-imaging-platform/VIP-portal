
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
    "cpu-cores",
    "ram",
    "disk-space",
    "nodes",
    "walltime-estimate"
})
@Generated("jsonschema2pojo")
public class SuggestedResources {

    /**
     * The requested number of cpu cores to run the described application
     * 
     */
    @JsonProperty("cpu-cores")
    @JsonPropertyDescription("The requested number of cpu cores to run the described application")
    private Integer cpuCores;
    /**
     * The requested number of GB RAM to run the described application
     * 
     */
    @JsonProperty("ram")
    @JsonPropertyDescription("The requested number of GB RAM to run the described application")
    private Double ram;
    /**
     * The requested number of GB of storage to run the described application
     * 
     */
    @JsonProperty("disk-space")
    @JsonPropertyDescription("The requested number of GB of storage to run the described application")
    private Double diskSpace;
    /**
     * The requested number of nodes to spread the described application across
     * 
     */
    @JsonProperty("nodes")
    @JsonPropertyDescription("The requested number of nodes to spread the described application across")
    private Integer nodes;
    /**
     * Estimated wall time of a task in seconds.
     * 
     */
    @JsonProperty("walltime-estimate")
    @JsonPropertyDescription("Estimated wall time of a task in seconds.")
    private Double walltimeEstimate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * The requested number of cpu cores to run the described application
     * 
     */
    @JsonProperty("cpu-cores")
    public Integer getCpuCores() {
        return cpuCores;
    }

    /**
     * The requested number of cpu cores to run the described application
     * 
     */
    @JsonProperty("cpu-cores")
    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    /**
     * The requested number of GB RAM to run the described application
     * 
     */
    @JsonProperty("ram")
    public Double getRam() {
        return ram;
    }

    /**
     * The requested number of GB RAM to run the described application
     * 
     */
    @JsonProperty("ram")
    public void setRam(Double ram) {
        this.ram = ram;
    }

    /**
     * The requested number of GB of storage to run the described application
     * 
     */
    @JsonProperty("disk-space")
    public Double getDiskSpace() {
        return diskSpace;
    }

    /**
     * The requested number of GB of storage to run the described application
     * 
     */
    @JsonProperty("disk-space")
    public void setDiskSpace(Double diskSpace) {
        this.diskSpace = diskSpace;
    }

    /**
     * The requested number of nodes to spread the described application across
     * 
     */
    @JsonProperty("nodes")
    public Integer getNodes() {
        return nodes;
    }

    /**
     * The requested number of nodes to spread the described application across
     * 
     */
    @JsonProperty("nodes")
    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    /**
     * Estimated wall time of a task in seconds.
     * 
     */
    @JsonProperty("walltime-estimate")
    public Double getWalltimeEstimate() {
        return walltimeEstimate;
    }

    /**
     * Estimated wall time of a task in seconds.
     * 
     */
    @JsonProperty("walltime-estimate")
    public void setWalltimeEstimate(Double walltimeEstimate) {
        this.walltimeEstimate = walltimeEstimate;
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
