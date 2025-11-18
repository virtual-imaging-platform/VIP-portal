package fr.insalyon.creatis.vip.core.client.rpc;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.UsageStats;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;

public interface ConfigurationServiceAsync {

    void configure(AsyncCallback<User> asyncCallback);

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

    void getUserGroups(AsyncCallback<List<Group>> asyncCallback);

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
