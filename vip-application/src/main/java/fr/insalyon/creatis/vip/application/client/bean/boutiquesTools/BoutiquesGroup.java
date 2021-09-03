package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Representation of a group in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesGroup implements IsSerializable {
    private final String id;
    private final boolean allOrNone;
    private final boolean mutuallyExclusive;
    private final boolean oneIsRequired;
    private final List<String> members;

    /**
     * @param id                String
     * @param allOrNone         boolean: true if this group is only valid when all or none of its member are empty
     * @param mutuallyExclusive boolean: true if this group is only valid when at most one member is non-empty
     * @param oneIsRequired     boolean: true if this group is only valid when at least one member is non-empty
     * @param members           List of String IDs of this group members
     */
    public BoutiquesGroup(String id, boolean allOrNone, boolean mutuallyExclusive, boolean oneIsRequired,
                          List<String> members){
        this.id = id;
        this.allOrNone = allOrNone;
        this.mutuallyExclusive = mutuallyExclusive;
        this.oneIsRequired = oneIsRequired;
        this.members = members;
    }

    /**
     * @return Group ID as String
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return Group member IDs as an array of Strings
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * @return boolean: true if group is 'all-or-none', meaning either all or none of member inputs must be empty
     */
    public boolean isAllOrNone() {
        return allOrNone;
    }

    /**
     * @return boolean: true if group is 'mutually-exclusive', meaning at most one member input can be non empty
     */
    public boolean isMutuallyExclusive() {
        return mutuallyExclusive;
    }

    /**
     * @return boolean: true if group is 'one-is-required', meaning at least one member input must be non empty
     */
    public boolean isOneIsRequired() {
        return oneIsRequired;
    }
}