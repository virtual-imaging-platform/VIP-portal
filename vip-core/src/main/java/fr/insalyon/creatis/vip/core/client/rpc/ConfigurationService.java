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
package fr.insalyon.creatis.vip.core.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface ConfigurationService extends RemoteService {

    String SERVICE_URI = "/configurationservice";

    class Util {

        public static ConfigurationServiceAsync getInstance() {

            ConfigurationServiceAsync instance = GWT.create(ConfigurationService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    User configure(String email, String session) throws CoreException;

    void signup(User user, String comments) throws CoreException;

    User signin(String email, String password) throws CoreException;

    void signout() throws CoreException;

    User activate(String code) throws CoreException;

    String sendActivationCode() throws CoreException;

    void sendResetCode(String email) throws CoreException;

    List<User> getUsers() throws CoreException;

    void addGroup(Group group) throws CoreException;

    void updateGroup(String name, Group group) throws CoreException;

    void removeGroup(String groupName) throws CoreException;

    List<Group> getGroups() throws CoreException;

    List<Group> getPublicGroups() throws CoreException;

    User removeUser(String email) throws CoreException;

    Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email) throws CoreException;

    List<Boolean> getUserPropertiesGroups() throws CoreException;

    List<String> getUserGroups() throws CoreException;

    void updateUser(String email, UserLevel level, CountryCode countryCode, int maxRunningSimulations, Map<String, CoreConstants.GROUP_ROLE> groups, boolean locked) throws CoreException;

    User getUserData() throws CoreException;

    User updateUser(User user) throws CoreException;

    void updateTermsOfUse() throws CoreException;

    void updateUserPassword(String currentPassword, String newPassword) throws CoreException;

    User requestNewEmail(String newEmail) throws CoreException;

    User confirmNewEmail(String code) throws CoreException;

    User cancelNewEmail() throws CoreException;

    void sendContactMail(String category, String subject, String comment) throws CoreException;

    void activateUser(String email) throws CoreException;

    void addUserToGroup(String groupName) throws CoreException;

    List<User> getUsersFromGroup(String groupName) throws CoreException;

    void removeUserFromGroup(String email, String groupName) throws CoreException;

    void resetPassword(String email, String code, String password) throws CoreException;

    void updateLastUpdatePublication() throws CoreException;

    String getCASLoginPageUrl() throws CoreException;

    UsageStats getUsageStats() throws CoreException;

    boolean testLastUpdatePublication() throws CoreException;

    // TermsOfUse
    void addTermsUse() throws CoreException;

    Timestamp getLastUpdateTermsOfUse() throws CoreException;

    boolean compare() throws CoreException;

    // api key management

    String getUserApikey(String email) throws CoreException;

    void deleteUserApikey(String email) throws CoreException;

    String generateNewUserApikey(String email) throws CoreException;
}
