package fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout;

import fr.insalyon.creatis.vip.application.client.view.launch.boutiquesParsing.BoutiquesGroup;

/**
 * Client-side validation logic for input groups
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class GroupValidator {
    private final BoutiquesGroup group;
    private final InputLayout[] members;
    private final String[] memberNames;
    private final LaunchFormLayout parentLayout;

    /**
     * Initializes group members
     * @param group         BoutiquesGroup to handle validation for
     * @param parentLayout  LaunchFormLayout for which validation is performed
     */
    public GroupValidator(BoutiquesGroup group,LaunchFormLayout parentLayout){
        this.group = group;
        this.parentLayout = parentLayout;
        String[] memberIds = group.getMembers();
        this.members = new InputLayout[memberIds.length];
        this.memberNames = new String[memberIds.length];
        for (int inputNo=0; inputNo<memberIds.length; inputNo++){
            InputLayout memberInput = parentLayout.getInputsMap().get(memberIds[inputNo]);
            this.members[inputNo] = memberInput;
            this.memberNames[inputNo] = memberInput.getInputName();
        }
    }

    /**
     * Perform client-side validation logic for the group. Make parentLayout display appropriate warning or error
     * messages
     */
    public void validate(){
        // Number of non empty group members
        int nActiveMembers = 0;
        for (InputLayout iMember : this.members){
            if(iMember.hasUniqueValue()){
                if (iMember.isUniqueFilled()){
                    nActiveMembers++;
                }
            } else {
                // If one member has multiple values, group dependency is not supported and a user warning is displayed
                this.parentLayout.addUnsupportedGroup(this);
                return;
            }
        }
        // Group dependency is supported, check if group conditions are satisfied and display appropriate error messages
        this.parentLayout.removeUnsupportedGroup(this);
        if(this.group.isMutuallyExclusive()){
            this.parentLayout.groupErrorMessage(LaunchFormLayout.mutuallyExclusiveMessage(this.memberNames),
                    (nActiveMembers > 1));
        }
        if(this.group.isAllOrNone()){
            this.parentLayout.groupErrorMessage(LaunchFormLayout.allOrNoneMessage(this.memberNames),
                    ((nActiveMembers != 0) & (nActiveMembers != this.members.length)));
        }
        if(this.group.isOneIsRequired()){
            this.parentLayout.groupErrorMessage(LaunchFormLayout.oneIsRequiredMessage(this.memberNames),
                    (nActiveMembers < 1));
        }
    }

    /**
     * @return String representing underlying group ID
     */
    public String getGroupId(){
        return this.group.getId();
    }

    /**
     * @return boolean: true if this represents a mutually exclusive group
     */
    public boolean isMutuallyExclusive(){
        return this.group.isMutuallyExclusive();
    }

    /**
     * @return boolean: true if this represents an all-or-none group
     */
    public boolean isAllOrNone(){
        return this.group.isAllOrNone();
    }

    /**
     * @return boolean: true if this represents a one-is-required  group
     */
    public boolean isOneIsRequired(){
        return this.group.isOneIsRequired();
    }

    /**
     * @return InputLayout array containing group members
     */
    public InputLayout[] getMembers() {
        return members;
    }

    /**
     * @return String array containing group member IDs
     */
    public String[] getMemberNames() {
        return this.memberNames;
    }
}