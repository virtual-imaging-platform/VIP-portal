package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;

import java.sql.Timestamp;
import java.util.*;


/**
 *
 * @author Rafael Ferreira da Silva
 */
public class User implements IsSerializable {

    private String firstName;
    private String lastName;
    private String email;
    private String nextEmail;
    private String institution;
    private String password;
    private boolean confirmed;
    private String code;
    private String folder;
    private String session;
    private Date registration;
    private Date lastLogin;
    private UserLevel level;
    private int maxRunningSimulations;
    private CountryCode countryCode;
    private Map<Group, GROUP_ROLE> groups;
    private boolean hasGroups;
    private Timestamp termsOfUse;
    private Timestamp lastUpdatePublications;
    private int failedAuthentications;
    private boolean accountLocked;
    
    public User() {
    }

    public User(String firstName, String lastName, String email, String institution, String password, UserLevel level, CountryCode countryCode, String applications) {
    }

    public User(String firstName, String lastName, String email, String institution, UserLevel level, CountryCode countryCode) {

        this(firstName, lastName, email, null, institution, "", false, "", "",
                "", null, null, level, countryCode, 1,null,null,0,false);

    }

    public User(String firstName, String lastName, String email, String institution, String password, UserLevel level, CountryCode countryCode) {

        this(firstName, lastName, email, null, institution, password, false, "", "",
                "", null, null, level, countryCode, 1,null,null,0,false);

    }

    public User(String firstName, String lastName, String email, String institution,
            String password, CountryCode countryCode,Timestamp lastUpdatePublications) {

        this(firstName, lastName, email, null, institution, password, false,
                "", "", "", new Date(), new Date(), null, countryCode, 1,null,lastUpdatePublications,0,false);
    }

    public User(
            String firstName,
            String lastName,
            String email,
            String nextEmail,
            String institution,
            String password,
            boolean confirmed,
            String code,
            String folder,
            String session,
            Date registration,
            Date lastLogin,
            UserLevel level,
            CountryCode countryCode, 
            int maxRunningSimulations,
            Timestamp termsOfUse,
            Timestamp lastUpdatePublications,
            int failedAuthentications,
            boolean locked
    ) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nextEmail = nextEmail;
        this.institution = institution;
        this.password = password;
        this.confirmed = confirmed;
        this.code = code;
        this.folder = folder;
        this.session = session;
        this.registration = registration;
        this.lastLogin = lastLogin;
        this.level = level;
        this.maxRunningSimulations = maxRunningSimulations;
        this.countryCode = countryCode;
        this.termsOfUse=termsOfUse;
        this.lastUpdatePublications=lastUpdatePublications;
        this.failedAuthentications = failedAuthentications;
        this.accountLocked = locked;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getNextEmail() {
        return nextEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFolder() {
        return folder;
    }

    public String getInstitution() {
        return institution;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isSystemAdministrator() {
        return level == UserLevel.Administrator;
    }

    public boolean isDeveloper() {
        return level == UserLevel.Developer;
    }

    public String getSession() {
        return session;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserLevel getLevel() {
        return level;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }

    public int getMaxRunningSimulations() {
        return maxRunningSimulations;
    }

    public void setMaxRunningSimulations(int maxRunningSimulations) {
        this.maxRunningSimulations = maxRunningSimulations;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Date getRegistration() {
        return registration;
    }

    public void setRegistration(Date registration) {
        this.registration = registration;
    }

    public void setGroups(Map<Group, GROUP_ROLE> groups) {
        this.groups = groups;
        this.hasGroups = !groups.isEmpty();
        filterGroups();
    }

    public Timestamp getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(Timestamp termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public Timestamp getLastUpdatePublications() {
        return lastUpdatePublications;
    }

    public void setLastUpdatePublications(Timestamp lastUpdatePublications) {
        this.lastUpdatePublications = lastUpdatePublications;
    }

    public boolean hasGroupAccess(String groupName) {

        for (Group group : groups.keySet()) {
            if (group.getName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }

    public Set<Group> getGroups() {
        return groups.keySet();
    }

    private void filterGroups() {
        Iterator<Group> it = groups.keySet().iterator();
        while (it.hasNext()) {
            Group group = it.next();
            if (groups.get(group) == GROUP_ROLE.None) {
                it.remove();
            }
        }
    }

    public boolean isGroupAdmin() {

        for (GROUP_ROLE role : groups.values()) {
            if (role == GROUP_ROLE.Admin) {
                return true;
            }
        }
        return false;
    }

    public boolean isGroupAdmin(String groupName) {

        for (Group group : groups.keySet()) {
            if (group.getName().equals(groupName)
                    && groups.get(group) == GROUP_ROLE.Admin) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAcceptTermsOfUse(){
        return getTermsOfUse()!=null;
       }

    public boolean hasGroups(){
        return hasGroups;
    }

    public int getFailedAuthentications() {
        return this.failedAuthentications;
    }

    public boolean isAccountLocked() {
        return this.accountLocked;
    }

    @Override
    public String toString() {
        return email;
    }
}
