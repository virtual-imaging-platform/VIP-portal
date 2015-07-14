/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.core.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import fr.insalyon.creatis.vip.core.client.bean.UsageStats;
import fr.insalyon.creatis.vip.core.client.bean.User;
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
public interface ConfigurationServiceAsync {

    public void configure(String email, String session, AsyncCallback<User> asyncCallback);

    public void signup(User user, String comments, String[] accountType, AsyncCallback<Void> asyncCallback);

    public void signin(String email, String password, AsyncCallback<User> asyncCallback);

    public void signin(String ticket, AsyncCallback<User> asyncCallback);

    public void signout(AsyncCallback<Void> asyncCallback);

    public void activate(String code, AsyncCallback<User> asyncCallback);

    public void sendActivationCode(AsyncCallback<String> asyncCallback);

    public void sendResetCode(String email, AsyncCallback<Void> asyncCallback);

    public void addGroup(Group group, AsyncCallback<Void> asyncCallback);

    public void removeGroup(String groupName, AsyncCallback<Void> asyncCallback);

    public void getGroups(AsyncCallback<List<Group>> asyncCallback);

    public void getPublicGroups(AsyncCallback<List<Group>> asyncCallback);

    public void updateGroup(String name, Group group, AsyncCallback<Void> asyncCallback);

    public void removeUser(String email, AsyncCallback<User> asyncCallback);

    public void getUserGroups(String email, AsyncCallback<Map<Group, CoreConstants.GROUP_ROLE>> asyncCallback);

    public void getUserPropertiesGroups(AsyncCallback<List<Boolean>> asyncCallback);

    public void getUserGroups(AsyncCallback<List<String>> asyncCallback);

    public void updateUser(String email, UserLevel level, CountryCode countryCode, int maxRunningSimulations, Map<String, CoreConstants.GROUP_ROLE> groups, AsyncCallback<Void> asyncCallback);

    public void getUsers(AsyncCallback<List<User>> asyncCallback);

    public void getUserData(AsyncCallback<User> asyncCallback);

    public void updateUser(User user, AsyncCallback<User> asyncCallback);

    public void updateTermsOfUse(AsyncCallback<Void> asyncCallback);

    public void updateUserPassword(String currentPassword, String newPassword, AsyncCallback<Void> asyncCallback);

    public void sendContactMail(String category, String subject, String comment, AsyncCallback<Void> asyncCallback);

    public void activateUser(String email, AsyncCallback<Void> asyncCallback);

    public void addUserToGroup(String groupName, AsyncCallback<Void> asyncCallback);

    public void getUsersFromGroup(String groupName, AsyncCallback<List<User>> asyncCallback);

    public void removeUserFromGroup(String email, String groupName, AsyncCallback<Void> asyncCallback);

    public void resetPassword(String email, String code, String password, AsyncCallback<Void> asyncCallback);

    // Accounts
    public void getAccounts(AsyncCallback<List<Account>> asyncCallback);

    public void addAccount(String name, List<String> groups, AsyncCallback<Void> asyncCallback);

    public void updateAccount(String oldName, String newName, List<String> groups, AsyncCallback<Void> asyncCallback);

    public void removeAccount(String name, AsyncCallback<Void> asyncCallback);

    public void getCASLoginPageUrl(AsyncCallback<String> asyncCallback);

    public void getUsageStats(AsyncCallback<UsageStats> asyncCallback);

    public void linkDropboxAccount(AsyncCallback<String> asyncCallback);

    public void activateDropboxAccount(String oauth_token, AsyncCallback<Void> asyncCallback);

    public void getDropboxAccountStatus(AsyncCallback<DropboxAccountStatus.AccountStatus> asyncCallback);

    public void unlinkDropboxAccount(AsyncCallback<Void> asyncCallback);
    
    public void testLastUpdatePublication(AsyncCallback<Boolean> asyncCallback);
    
    // public void getPostParameters(AsyncCallback<Void> asyncCallback);
    //publications
    public void getPublications(AsyncCallback<List<Publication>> asyncCallback);

    public void removePublication(Long id, AsyncCallback<Void> asyncCallback);

    public void addPublication(Publication pub, AsyncCallback<Void> asyncCallback);

    public void updatePublication(Publication pub, AsyncCallback<Void> asyncCallback);
    
     public void parseBibtexText(String text,AsyncCallback<List<Publication>> asyncCallback) ;
    
    //TermsOfUse
    public void addTermsUse(TermsOfUse termsofUse,AsyncCallback<Void> asyncCallback);
    
    public void getLastUpdateTermsOfUse(AsyncCallback<Timestamp> asyncCallback);
    
    public void compare(AsyncCallback<Boolean> asyncCallback);
    
    public void getMaxConfiguredPlatformSimulation(AsyncCallback<Integer> asyncCallback);
    
    public void changeMaxConfiguredPlatformSimulation(int maxPlatformRunningSimulations,AsyncCallback<Void> asyncCallback);
}
