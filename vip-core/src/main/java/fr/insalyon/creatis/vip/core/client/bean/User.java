/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Rafael Silva
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
    private Date lastLogin;
    private UserLevel level;
    private CountryCode countryCode;
    private Map<String, GROUP_ROLE> groups;

    public User() {
    }

    public User(String firstName, String lastName, String email, String institution,
            String phone, UserLevel level, CountryCode countryCode) {

        this(firstName, lastName, email, institution, "", phone, false, "", "",
                "", null, level, countryCode);
    }

    public User(String firstName, String lastName, String email, String institution,
            String password, String phone, CountryCode countryCode) {

        this(firstName, lastName, email, institution, password, phone, false,
                "", "", "", null, null, countryCode);
    }

    public User(String firstName, String lastName, String email,
            String institution, String password, String phone, boolean confirmed,
            String code, String folder, String session, Date lastLogin,
            UserLevel level, CountryCode countryCode) {

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
        this.lastLogin = lastLogin;
        this.level = level;
        this.countryCode = countryCode;
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

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public void setGroups(Map<String, GROUP_ROLE> groups) {
        this.groups = groups;
    }

    public boolean hasGroupAccess(String groupName) {
        return groups.containsKey(groupName);
    }

    public boolean isGroupAdmin() {

        for (String groupName : groups.keySet()) {
            if (groups.get(groupName) == GROUP_ROLE.Admin) {
                return true;
            }
        }
        return false;
    }

    public boolean isGroupAdmin(String groupName) {

        if (hasGroupAccess(groupName) && groups.get(groupName) == GROUP_ROLE.Admin) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return email;
    }
}
