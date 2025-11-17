package fr.insalyon.creatis.vip.core.client.rpc;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.UsageStats;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;

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

    User configure() throws CoreException;

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

    List<String> getItemsGroup(String groupname) throws CoreException;

    void removeItemFromGroup(String item, String groupname) throws CoreException;

    User removeUser(String email) throws CoreException;

    Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email) throws CoreException;

    List<Boolean> getUserPropertiesGroups() throws CoreException;

    List<Group> getUserGroups() throws CoreException;

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

    List<String> getMissingGroupsRessources(String email) throws CoreException;
}
