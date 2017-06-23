/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
    private String institution;
    private String phone;
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
    
    private Boolean isGridFile;
    private Boolean isGridJob;
    
    public User() {
    }

    public User(String firstName, String lastName, String email, String institution,
            String phone, UserLevel level, CountryCode countryCode) {

        this(firstName, lastName, email, institution, "", phone, false, "", "",
                "", null, null, level, countryCode, 1,null,null,0,false);

    }

    public User(String firstName, String lastName, String email, String institution,
            String password, String phone, CountryCode countryCode,Timestamp lastUpdatePublications) {

        this(firstName, lastName, email, institution, password, phone, false,
                "", "", "", new Date(), new Date(), null, countryCode, 1,null,lastUpdatePublications,0,false);
    }

    public User(
            String firstName,
            String lastName,
            String email,
            String institution,
            String password,
            String phone,
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
        this.institution = institution;
        this.password = password;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
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

    public Boolean getIsgridfile() {
        return isGridFile;
    }

    public void setIsgridfile(Boolean isgridfile) {
        this.isGridFile = isgridfile;
    }

    public Boolean getIsgridjobs() {
        return isGridJob;
    }

    public void setIsgridjobs(Boolean isgridjobs) {
        this.isGridJob = isgridjobs;
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
