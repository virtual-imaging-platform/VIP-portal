package fr.insalyon.creatis.vip.application.client.view.launch;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-side validation logic for input groups
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class GroupValidator {
    private final BoutiquesGroup group;
    private final List<InputLayout> members = new ArrayList<>();
    private final List<String> memberNames = new ArrayList<>();
    private final LaunchFormLayout parentLayout;

    /**
     * Initializes group members
     * @param group         BoutiquesGroup to handle validation for
     * @param parentLayout  LaunchFormLayout for which validation is performed
     */
    public GroupValidator(BoutiquesGroup group, LaunchFormLayout parentLayout){
        this.group = group;
        this.parentLayout = parentLayout;
        for (String currentMemberId : group.getMembers()){
            InputLayout memberInput = parentLayout.getInputsMap().get(currentMemberId);
            this.members.add(memberInput);
            this.memberNames.add(memberInput.getInputName());
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
                    ((nActiveMembers != 0) & (nActiveMembers != this.members.size())));
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
    public List<InputLayout> getMembers() {
        return members;
    }

    /**
     * @return String array containing group member IDs
     */
    public List<String> getMemberNames() {
        return this.memberNames;
    }
}