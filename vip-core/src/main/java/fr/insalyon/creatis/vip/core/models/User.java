package fr.insalyon.creatis.vip.core.models;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.inter.DataViews;

public class User implements IsSerializable {

    @JsonView(DataViews.Admin.class) private Boolean accountLocked;
    @JsonView(DataViews.Admin.class) private String folder;

    @JsonView(DataViews.User.class) private String id;
    @JsonView(DataViews.User.class) private String firstName;
    @JsonView(DataViews.User.class) private String lastName;
    @JsonView(DataViews.User.class) private String email;
    @JsonView(DataViews.User.class) private Boolean confirmed;
    @JsonView(DataViews.User.class) private String institution;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(DataViews.User.class) private Timestamp registration;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(DataViews.User.class) private Timestamp lastLogin;
    @JsonView(DataViews.User.class) private UserLevel level;
    @JsonView(DataViews.User.class) private Integer maxRunningSimulations;
    @JsonView(DataViews.User.class) private CountryCode countryCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(DataViews.User.class) private Timestamp termsOfUse;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(DataViews.User.class) private Timestamp lastUpdatePublications;
    @JsonView(DataViews.User.class) private Set<Group> groups;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(DataViews.User.class) private String apiKey;
    
    private Map<Group, GROUP_ROLE> groupsMap;
    private String nextEmail;
    private String code;
    private String session;
    private String password;
    private Integer failedAuthentications;

    public User() {
        this.groupsMap = new HashMap<>();
        this.groups = new HashSet<>();
    }

    public User(User user) {
        // when copying, keep only mandatory stuff : id and groups
        this();
        setId(user.getId());
        setGroups(user.groupsMap);
    }

    public User(String id, String firstName, String lastName) {
        this(firstName, lastName, null, null, null, null);
        setId(id);
    }

    public User(String firstName, String lastName, String email, String institution, CountryCode countryCode) {
        this(firstName, lastName, email, institution, null, countryCode);
    }

    public User(String firstName, String lastName, String email, String institution, UserLevel level, CountryCode countryCode) {
        this(null, firstName, lastName, email, null, institution,
                null, null, null, null,
                null, null, level, countryCode, null,null,null,null,null, null);
    }

    public User(
            String id,
            String firstName,
            String lastName,
            String email,
            String nextEmail,
            String institution,
            Boolean confirmed,
            String code,
            String folder,
            String session,
            Timestamp registration,
            Timestamp lastLogin,
            UserLevel level,
            CountryCode countryCode,
            Integer maxRunningSimulations,
            Timestamp termsOfUse,
            Timestamp lastUpdatePublications,
            Integer failedAuthentications,
            Boolean locked,
            String apiKey
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nextEmail = nextEmail;
        this.institution = institution;
        this.password = null;
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
        this.groupsMap = new HashMap<>();
        this.groups = new HashSet<>();
        this.apiKey = apiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
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

    public void setNextEmail(String nextEmail) {
        this.nextEmail = nextEmail;
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

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @JsonIgnore
    public boolean isSystemAdministrator() {
        return level == UserLevel.Administrator;
    }

    @JsonIgnore
    public boolean isDeveloper() {
        return level == UserLevel.Developer;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserLevel getLevel() {
        return level;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }

    public Integer getMaxRunningSimulations() {
        return maxRunningSimulations;
    }

    public void setMaxRunningSimulations(Integer maxRunningSimulations) {
        this.maxRunningSimulations = maxRunningSimulations;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Timestamp getRegistration() {
        return registration;
    }

    public void setRegistration(Timestamp registration) {
        this.registration = registration;
    }

    @JsonIgnore // this field can't be jsonified (need a string as key of map)
    public void setGroups(Map<Group, GROUP_ROLE> groups) {
        this.groups = new HashSet<>(groups.keySet());
        this.groupsMap = groups;
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

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonIgnore // this field can't be jsonified (need a string as key of map)
    public boolean hasGroupAccess(String groupName) {
        for (Group group : groupsMap.keySet()) {
            if (group.getName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }

    public Set<Group> getGroups() {
        return groups == null ? Collections.emptySet() : groups;
    }

    @JsonIgnore
    public Set<Group> getAdminGroups() {
        return groupsMap.entrySet().stream()
                .filter(e -> e.getValue() == GROUP_ROLE.Admin)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @JsonIgnore // this field can't be jsonified (need a string as key of map)
    private void filterGroups() {
        Iterator<Group> it = groupsMap.keySet().iterator();
        while (it.hasNext()) {
            Group group = it.next();
            if (groupsMap.get(group) == GROUP_ROLE.None) {
                it.remove();
            }
        }
    }

    @JsonIgnore // this field can't be jsonified (need a string as key of map)
    public boolean isGroupAdmin() {
        for (GROUP_ROLE role : groupsMap.values()) {
            if (role == GROUP_ROLE.Admin) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore // this field can't be jsonified (need a string as key of map)
    public boolean isGroupAdmin(String groupName) {
        for (Group group : groupsMap.keySet()) {
            if (group.getName().equals(groupName)
                    && groupsMap.get(group) == GROUP_ROLE.Admin) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAcceptTermsOfUse(){
        return getTermsOfUse() != null;
       }

    public boolean hasGroups(){
        return this.groups.isEmpty();
    }

    public Integer getFailedAuthentications() {
        return this.failedAuthentications;
    }

    public void setFailedAuthentications(Integer failedAuthentications) {
        this.failedAuthentications = failedAuthentications;
    }

    public Boolean isAccountLocked() {
        return this.accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(accountLocked, user.accountLocked)
                && Objects.equals(folder, user.folder)
                && Objects.equals(id, user.id)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(email, user.email)
                && Objects.equals(confirmed, user.confirmed)
                && Objects.equals(institution, user.institution)
                && Objects.equals(registration, user.registration)
                && Objects.equals(lastLogin, user.lastLogin)
                && level == user.level
                && Objects.equals(maxRunningSimulations, user.maxRunningSimulations)
                && countryCode == user.countryCode
                && Objects.equals(termsOfUse, user.termsOfUse)
                && Objects.equals(lastUpdatePublications, user.lastUpdatePublications)
                && Objects.equals(groups, user.groups)
                && Objects.equals(apiKey, user.apiKey)
                && Objects.equals(nextEmail, user.nextEmail)
                && Objects.equals(code, user.code)
                && Objects.equals(session, user.session)
                && Objects.equals(password, user.password)
                && Objects.equals(failedAuthentications, user.failedAuthentications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountLocked, folder, id, firstName, lastName, email, confirmed, institution,
                registration, lastLogin, level, maxRunningSimulations, countryCode, termsOfUse, lastUpdatePublications,
                groups, apiKey, nextEmail, code, session, password, failedAuthentications);
    }
}
