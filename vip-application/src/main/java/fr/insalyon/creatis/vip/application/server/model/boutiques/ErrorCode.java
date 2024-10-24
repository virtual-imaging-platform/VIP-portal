
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "description"
})
@Generated("jsonschema2pojo")
public class ErrorCode {

    /**
     * Value of the exit code
     * (Required)
     * 
     */
    @JsonProperty("code")
    @JsonPropertyDescription("Value of the exit code")
    private Integer code;
    /**
     * Description of the error code.
     * (Required)
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Description of the error code.")
    private String description;

    /**
     * Value of the exit code
     * (Required)
     * 
     */
    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    /**
     * Value of the exit code
     * (Required)
     * 
     */
    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Description of the error code.
     * (Required)
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description of the error code.
     * (Required)
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

}
