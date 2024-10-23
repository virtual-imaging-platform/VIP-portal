
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.*;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "image",
        "entrypoint",
        "index",
        "container-opts",
        "url",
        "working-directory",
        "container-hash"
})
@Generated("jsonschema2pojo")
public class ContainerImage {

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    private ContainerImage.Type type;
    /**
     * Name of an image where the tool is installed and configured. Example: bids/mriqc.
     *
     */
    @JsonProperty("image")
    @JsonPropertyDescription("Name of an image where the tool is installed and configured. Example: bids/mriqc.")
    private String image;
    /**
     * Flag indicating whether or not the container uses an entrypoint.
     *
     */
    @JsonProperty("entrypoint")
    @JsonPropertyDescription("Flag indicating whether or not the container uses an entrypoint.")
    private Boolean entrypoint;
    /**
     * Optional index where the image is available, if not the standard location. Example: docker.io
     *
     */
    @JsonProperty("index")
    @JsonPropertyDescription("Optional index where the image is available, if not the standard location. Example: docker.io")
    private String index;
    /**
     * Container-level arguments for the application. Example: --privileged
     *
     */
    @JsonProperty("container-opts")
    @JsonPropertyDescription("Container-level arguments for the application. Example: --privileged")
    private List<String> containerOpts = new ArrayList<String>();
    /**
     * URL where the image is available.
     *
     */
    @JsonProperty("url")
    @JsonPropertyDescription("URL where the image is available.")
    private String url;
    /**
     * Location from which this task must be launched within the container.
     *
     */
    @JsonProperty("working-directory")
    @JsonPropertyDescription("Location from which this task must be launched within the container.")
    private String workingDirectory;
    /**
     * Hash for the given container.
     *
     */
    @JsonProperty("container-hash")
    @JsonPropertyDescription("Hash for the given container.")
    private String containerHash;

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    public ContainerImage.Type getType() {
        return type;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    public void setType(ContainerImage.Type type) {
        this.type = type;
    }

    /**
     * Name of an image where the tool is installed and configured. Example: bids/mriqc.
     *
     */
    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    /**
     * Name of an image where the tool is installed and configured. Example: bids/mriqc.
     *
     */
    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Flag indicating whether or not the container uses an entrypoint.
     *
     */
    @JsonProperty("entrypoint")
    public Boolean getEntrypoint() {
        return entrypoint;
    }

    /**
     * Flag indicating whether or not the container uses an entrypoint.
     *
     */
    @JsonProperty("entrypoint")
    public void setEntrypoint(Boolean entrypoint) {
        this.entrypoint = entrypoint;
    }

    /**
     * Optional index where the image is available, if not the standard location. Example: docker.io
     *
     */
    @JsonProperty("index")
    public String getIndex() {
        return index;
    }

    /**
     * Optional index where the image is available, if not the standard location. Example: docker.io
     *
     */
    @JsonProperty("index")
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * Container-level arguments for the application. Example: --privileged
     *
     */
    @JsonProperty("container-opts")
    public List<String> getContainerOpts() {
        return containerOpts;
    }

    /**
     * Container-level arguments for the application. Example: --privileged
     *
     */
    @JsonProperty("container-opts")
    public void setContainerOpts(List<String> containerOpts) {
        this.containerOpts = containerOpts;
    }

    /**
     * URL where the image is available.
     *
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * URL where the image is available.
     *
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Location from which this task must be launched within the container.
     *
     */
    @JsonProperty("working-directory")
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Location from which this task must be launched within the container.
     *
     */
    @JsonProperty("working-directory")
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * Hash for the given container.
     *
     */
    @JsonProperty("container-hash")
    public String getContainerHash() {
        return containerHash;
    }

    /**
     * Hash for the given container.
     *
     */
    @JsonProperty("container-hash")
    public void setContainerHash(String containerHash) {
        this.containerHash = containerHash;
    }

    @Generated("jsonschema2pojo")
    public enum Type {

        DOCKER("docker"),
        SINGULARITY("singularity"),
        ROOTFS("rootfs");
        private final String value;
        private final static Map<String, ContainerImage.Type> CONSTANTS = new HashMap<String, Type>();

        static {
            for (ContainerImage.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static ContainerImage.Type fromValue(String value) {
            ContainerImage.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}