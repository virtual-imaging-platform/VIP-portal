
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "description",
    "members",
    "mutually-exclusive",
    "one-is-required",
    "all-or-none"
})
@Generated("jsonschema2pojo")
public class Group {

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "outfile_group".
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: \"outfile_group\".")
    private String id;
    /**
     * A human-readable name for the input group.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("A human-readable name for the input group.")
    private String name;
    /**
     * Description of the input group.
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Description of the input group.")
    private String description;
    /**
     * IDs of the inputs belonging to this group.
     * (Required)
     * 
     */
    @JsonProperty("members")
    @JsonPropertyDescription("IDs of the inputs belonging to this group.")
    private List<String> members = new ArrayList<String>();
    /**
     * True if only one input in the group may be active at runtime.
     * 
     */
    @JsonProperty("mutually-exclusive")
    @JsonPropertyDescription("True if only one input in the group may be active at runtime.")
    private Boolean mutuallyExclusive;
    /**
     * True if at least one of the inputs in the group must be active at runtime.
     * 
     */
    @JsonProperty("one-is-required")
    @JsonPropertyDescription("True if at least one of the inputs in the group must be active at runtime.")
    private Boolean oneIsRequired;
    /**
     * True if members of the group need to be toggled together
     * 
     */
    @JsonProperty("all-or-none")
    @JsonPropertyDescription("True if members of the group need to be toggled together")
    private Boolean allOrNone;

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "outfile_group".
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "outfile_group".
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * A human-readable name for the input group.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * A human-readable name for the input group.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Description of the input group.
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description of the input group.
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * IDs of the inputs belonging to this group.
     * (Required)
     * 
     */
    @JsonProperty("members")
    public List<String> getMembers() {
        return members;
    }

    /**
     * IDs of the inputs belonging to this group.
     * (Required)
     * 
     */
    @JsonProperty("members")
    public void setMembers(List<String> members) {
        this.members = members;
    }

    /**
     * True if only one input in the group may be active at runtime.
     * 
     */
    @JsonProperty("mutually-exclusive")
    public Boolean getMutuallyExclusive() {
        return mutuallyExclusive;
    }

    /**
     * True if only one input in the group may be active at runtime.
     * 
     */
    @JsonProperty("mutually-exclusive")
    public void setMutuallyExclusive(Boolean mutuallyExclusive) {
        this.mutuallyExclusive = mutuallyExclusive;
    }

    /**
     * True if at least one of the inputs in the group must be active at runtime.
     * 
     */
    @JsonProperty("one-is-required")
    public Boolean getOneIsRequired() {
        return oneIsRequired;
    }

    /**
     * True if at least one of the inputs in the group must be active at runtime.
     * 
     */
    @JsonProperty("one-is-required")
    public void setOneIsRequired(Boolean oneIsRequired) {
        this.oneIsRequired = oneIsRequired;
    }

    /**
     * True if members of the group need to be toggled together
     * 
     */
    @JsonProperty("all-or-none")
    public Boolean getAllOrNone() {
        return allOrNone;
    }

    /**
     * True if members of the group need to be toggled together
     * 
     */
    @JsonProperty("all-or-none")
    public void setAllOrNone(Boolean allOrNone) {
        this.allOrNone = allOrNone;
    }

}
