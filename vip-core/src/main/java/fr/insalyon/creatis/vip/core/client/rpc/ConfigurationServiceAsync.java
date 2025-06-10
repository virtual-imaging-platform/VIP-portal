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

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ConfigurationServiceAsync {

    void configure(String email, String session, AsyncCallback<User> asyncCallback);

    void signout(AsyncCallback<Void> asyncCallback);

    void activate(String code, AsyncCallback<User> asyncCallback);

    void sendActivationCode(AsyncCallback<String> asyncCallback);

    void sendResetCode(String email, AsyncCallback<Void> asyncCallback);

    void addGroup(Group group, AsyncCallback<Void> asyncCallback);

    void removeGroup(String groupName, AsyncCallback<Void> asyncCallback);

    void getGroups(AsyncCallback<List<Group>> asyncCallback);

    void getPublicGroups(AsyncCallback<List<Group>> asyncCallback);

    void getItemsGroup(String groupname, AsyncCallback<List<String>> asyncCallback);

    void removeItemFromGroup(String item, String groupname, AsyncCallback<Void> asyncCallback);

    void updateGroup(String name, Group group, AsyncCallback<Void> asyncCallback);

    void removeUser(String email, AsyncCallback<User> asyncCallback);

    void getUserGroups(String email, AsyncCallback<Map<Group, CoreConstants.GROUP_ROLE>> asyncCallback);

    void getUserPropertiesGroups(AsyncCallback<List<Boolean>> asyncCallback);

    void getUserGroups(AsyncCallback<List<String>> asyncCallback);

    void updateUser(String email, UserLevel level, CountryCode countryCode, int maxRunningSimulations, Map<String, CoreConstants.GROUP_ROLE> groups, boolean locked, AsyncCallback<Void> asyncCallback);

    void getUsers(AsyncCallback<List<User>> asyncCallback);

    void getUserData(AsyncCallback<User> asyncCallback);

    void updateUser(User user, AsyncCallback<User> asyncCallback);

    void updateTermsOfUse(AsyncCallback<Void> asyncCallback);

    void updateUserPassword(String currentPassword, String newPassword, AsyncCallback<Void> asyncCallback);

    void requestNewEmail(String newEmail, AsyncCallback<User> async);

    void confirmNewEmail(String code, AsyncCallback<User> async);

    void cancelNewEmail(AsyncCallback<User> async);

    void sendContactMail(String category, String subject, String comment, AsyncCallback<Void> asyncCallback);

    void activateUser(String email, AsyncCallback<Void> asyncCallback);

    void addUserToGroup(String groupName, AsyncCallback<Void> asyncCallback);

    void getUsersFromGroup(String groupName, AsyncCallback<List<User>> asyncCallback);

    void removeUserFromGroup(String email, String groupName, AsyncCallback<Void> asyncCallback);

    void resetPassword(String email, String code, String password, AsyncCallback<Void> asyncCallback);

    void updateLastUpdatePublication(AsyncCallback<Void> asyncCallback);

    void getCASLoginPageUrl(AsyncCallback<String> asyncCallback);

    void getUsageStats(AsyncCallback<UsageStats> asyncCallback);

    void testLastUpdatePublication(AsyncCallback<Boolean> asyncCallback);

    // public void getPostParameters(AsyncCallback<Void> asyncCallback);

    //TermsOfUse
    void addTermsUse(AsyncCallback<Void> asyncCallback);

    void getLastUpdateTermsOfUse(AsyncCallback<Timestamp> asyncCallback);

    void compare(AsyncCallback<Boolean> asyncCallback);

    // api key management

    void getUserApikey(String email, AsyncCallback<String> async);

    void deleteUserApikey(String email, AsyncCallback<Void> async);

    void generateNewUserApikey(String email, AsyncCallback<String> async);

    void getMissingGroupsRessources(String email, AsyncCallback<List<String>> async);
}
