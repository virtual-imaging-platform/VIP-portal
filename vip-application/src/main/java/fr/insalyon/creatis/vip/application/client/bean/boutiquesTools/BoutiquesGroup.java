package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;

public class BoutiquesGroup implements IsSerializable {

    private String id;
    private boolean allOrNone;
    private boolean mutuallyExclusive;
    private boolean oneIsRequired;
    private Set<String> members;

    public BoutiquesGroup() {
    }

    /**
     * @param id                String
     * @param allOrNone         boolean: true if this group is only valid when all or none of its member are empty
     * @param mutuallyExclusive boolean: true if this group is only valid when at most one member is non-empty
     * @param oneIsRequired     boolean: true if this group is only valid when at least one member is non-empty
     * @param members           List of String IDs of this group members
     */
    public BoutiquesGroup(String id, boolean allOrNone, boolean mutuallyExclusive, boolean oneIsRequired,
                          Set<String> members){
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
    public Set<String> getMembers() {
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