package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONObject;

/**
 * Representation of a group in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesGroup {
    private final String id;
    private final boolean allOrNone;
    private final boolean mutuallyExclusive;
    private final boolean oneIsRequired;
    private final String[] members;

    /**
     * Initialises group information based on its JSON description
     *
     * @param descriptor JSONObject describing this group
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesGroup(JSONObject descriptor) throws RuntimeException{
        this.id = BoutiquesUtil.getStringValue(descriptor, "id");
        this.members = BoutiquesUtil.getArrayValueAsStrings(descriptor, "members", false);
        this.allOrNone = BoutiquesUtil.getBooleanValue(descriptor, "all-or-none", true);
        this.mutuallyExclusive = BoutiquesUtil.getBooleanValue(descriptor, "mutually-exclusive", true);
        this.oneIsRequired = BoutiquesUtil.getBooleanValue(descriptor, "one-is-required", true);
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
    public String[] getMembers() {
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