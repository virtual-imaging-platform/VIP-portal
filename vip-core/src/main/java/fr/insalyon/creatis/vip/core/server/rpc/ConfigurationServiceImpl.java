/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.core.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ConfigurationServiceImpl extends AbstractRemoteServiceServlet implements ConfigurationService {

    private static Logger logger = Logger.getLogger(ConfigurationServiceImpl.class);

    public ConfigurationServiceImpl() {
        super();
        configurationBusiness = new ConfigurationBusiness();
    }

    /**
     *
     * @param email
     * @param session
     * @return
     * @throws CoreException
     */
    @Override
    public User configure(String email, String session) throws CoreException {

        try {
            logger.info("Initializing VIP configuration.");
            configurationBusiness.configure();
            logger.info("VIP successfully configured.");

            if (configurationBusiness.validateSession(email, session)) {

                User user = configurationBusiness.getUser(email);
                user = setUserSession(user);
                configurationBusiness.updateUserLastLogin(email);
                trace(logger, "Connected.");

                return user;
            }
            return null;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param user User bean object
     * @param comments User's comments
     * @param accountType User's account type
     * @throws CoreException
     */
    @Override
    public void signup(User user, String comments, String accountType) throws CoreException {

        try {
            logger.info("Sign up request from '" + user.getEmail() + "'.");
            configurationBusiness.signup(user, comments, accountType);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @param password
     * @throws CoreException
     * @return
     */
    @Override
    public User signin(String email, String password) throws CoreException {

        try {
            logger.info("Authenticating '" + email + "'.");
            User user = configurationBusiness.signin(email, password);
            user = setUserSession(user);
            configurationBusiness.updateUserLastLogin(email);
            trace(logger, "Connected.");

            return user;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param ticket
     * @throws CoreException
     * @return
     */
    @Override
    public User signin(String ticket) throws CoreException {

        try {
            logger.info("Authenticating CAS ticket '" + ticket + "'.");
            User user = configurationBusiness.signin(ticket, getBaseURL());
            user = setUserSession(user);
            configurationBusiness.updateUserLastLogin(user.getEmail());
            trace(logger, "Connected.");

            return user;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        } catch (MalformedURLException e) {
            throw new CoreException(e);
        }
    }

    /**
     *
     * @throws CoreException
     */
    @Override
    public void signout() throws CoreException {

        try {
            configurationBusiness.signout(getSessionUser().getEmail());
            trace(logger, "Signed out.");
            getSession().removeAttribute(CoreConstants.SESSION_USER);
            getSession().removeAttribute(CoreConstants.SESSION_GROUPS);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @param code
     * @return
     * @throws CoreException
     */
    @Override
    public User activate(String code) throws CoreException {

        try {
            User user = getSessionUser();
            logger.info("Activating '" + user.getEmail() + "'.");
            user = configurationBusiness.activate(user.getEmail(), code);

            return setUserSession(user);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws CoreException
     */
    @Override
    public String sendActivationCode() throws CoreException {

        try {
            User user = getSessionUser();
            logger.info("Sending activation code to: " + user.getEmail() + ".");
            configurationBusiness.sendActivationCode(user.getEmail());

            return user.getEmail();

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws CoreException
     */
    @Override
    public void sendResetCode(String email) throws CoreException {

        try {
            logger.info("(" + email + ") Requested to reset the password.");
            configurationBusiness.sendResetCode(email);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * Get list of users.
     *
     * @return
     */
    public List<User> getUsers() throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            return configurationBusiness.getUsers();

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param group
     * @throws CoreException
     */
    @Override
    public void addGroup(Group group) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding group '" + group + "'.");
            configurationBusiness.addGroup(group);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param name
     * @param group
     * @throws CoreException
     */
    @Override
    public void updateGroup(String name, Group group) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating group '" + name + "' to '" + group.getName() + "'.");
            configurationBusiness.updateGroup(name, group);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param groupName
     * @throws CoreException
     */
    @Override
    public void removeGroup(String groupName) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing group '" + groupName + "'.");
            configurationBusiness.removeGroup(getSessionUser().getEmail(), groupName);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @return @throws CoreException
     */
    @Override
    public List<Group> getGroups() throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            return configurationBusiness.getGroups();

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws CoreException
     */
    @Override
    public User removeUser(String email) throws CoreException {

        try {
            User user = email != null ? configurationBusiness.getUser(email) : getSessionUser();
            if (email != null) {
                authenticateSystemAdministrator(logger);
            }
            trace(logger, "Removing user '" + user.getEmail() + "'.");
            configurationBusiness.removeUser(user.getEmail(), true);

            return user;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws CoreException
     */
    @Override
    public Map<Group, GROUP_ROLE> getUserGroups(String email) throws CoreException {

        try {
            if (email != null) {
                authenticateSystemAdministrator(logger);
                return configurationBusiness.getUserGroups(email);

            } else {
                return configurationBusiness.getUserGroups(getSessionUser().getEmail());
            }
        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @return @throws CoreException
     */
    @Override
    public List<String> getUserGroups() throws CoreException {

        try {
            List<String> list = new ArrayList<String>();
            if (getSessionUser().isSystemAdministrator()) {
                for (Group group : configurationBusiness.getGroups()) {
                    list.add(group.getName());
                }
            } else {
                Map<Group, GROUP_ROLE> groups = getSessionUserGroups();
                for (Group group : groups.keySet()) {
                    if (groups.get(group) != GROUP_ROLE.None) {
                        list.add(group.getName());
                    }
                }
            }
            return list;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @param level
     * @param countryCode
     * @param groups
     * @throws CoreException
     */
    @Override
    public void updateUser(String email, UserLevel level, CountryCode countryCode,
            Map<String, GROUP_ROLE> groups) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating user '" + email + "'.");
            configurationBusiness.updateUser(email, level, countryCode);
            configurationBusiness.setUserGroups(email, groups);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @return @throws CoreException
     */
    @Override
    public User getUserData() throws CoreException {

        try {
            return configurationBusiness.getUserData(getSessionUser().getEmail());

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param user
     * @throws CoreException
     */
    @Override
    public User updateUser(User user) throws CoreException {

        try {
            trace(logger, "Updating user data '" + user.getEmail() + "'.");
            user = configurationBusiness.updateUser(getSessionUser(), user);
            return setUserSession(user);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param currentPassword
     * @param newPassword
     * @throws CoreException
     */
    public void updateUserPassword(String currentPassword, String newPassword)
            throws CoreException {

        try {
            trace(logger, "Updating user password.");
            configurationBusiness.updateUserPassword(getSessionUser().getEmail(),
                    currentPassword, newPassword);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param category
     * @param subject
     * @param comment
     * @throws CoreException
     */
    public void sendContactMail(String category, String subject, String comment)
            throws CoreException {

        try {
            configurationBusiness.sendContactMail(getSessionUser(), category, subject, comment);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param user
     * @return
     * @throws BusinessException
     */
    private User setUserSession(User user) throws BusinessException {

        Map<Group, GROUP_ROLE> groups = configurationBusiness.getUserGroups(user.getEmail());
        user.setGroups(groups);

        getSession().setAttribute(CoreConstants.SESSION_USER, user);
        getSession().setAttribute(CoreConstants.SESSION_GROUPS, groups);

        return user;
    }

    /**
     *
     * @param email
     * @throws CoreException
     */
    public void activateUser(String email) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Activating user: " + email);
            configurationBusiness.activateUser(email);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param groupName
     * @throws CoreException
     */
    @Override
    public void addUserToGroup(String groupName) throws CoreException {

        try {
            trace(logger, "Adding user to group '" + groupName + "'.");
            configurationBusiness.addUserToGroup(getSessionUser().getEmail(), groupName);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param groupName
     * @return
     * @throws CoreException
     */
    public List<User> getUsersFromGroup(String groupName) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            return configurationBusiness.getUsersFromGroup(groupName);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @param groupName
     * @throws CoreException
     */
    @Override
    public void removeUserFromGroup(String email, String groupName) throws CoreException {

        try {
            if (email != null) {
                authenticateSystemAdministrator(logger);
                trace(logger, "Removing '" + email + "' from '" + groupName + "' group.");
                configurationBusiness.removeUserFromGroup(email, groupName);
            } else {
                trace(logger, "Removing user from '" + groupName + "' group.");
                configurationBusiness.removeUserFromGroup(getSessionUser().getEmail(), groupName);
            }
        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param email
     * @param code
     * @param password
     * @throws CoreException
     */
    @Override
    public void resetPassword(String email, String code, String password) throws CoreException {

        try {
            logger.info("(" + email + ") Reseting password.");
            configurationBusiness.resetPassword(email, code, password);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @return List of accounts
     * @throws CoreException
     */
    @Override
    public List<Account> getAccounts() throws CoreException {

        try {
            return configurationBusiness.getAccounts();

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     *
     * @param name
     * @throws CoreException
     */
    @Override
    public void removeAccount(String name) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing account type '" + name + "'.");
            configurationBusiness.removeAccount(name);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void addAccount(String name, List<String> groups) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding account type '" + name + "'.");
            configurationBusiness.addAccount(name, groups);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateAccount(String oldName, String newName, List<String> groups)
            throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating account type from '" + oldName + "' to '" + newName + "'.");
            configurationBusiness.updateAccount(oldName, newName, groups);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

//    /**
//     *
//     * @return @throws CoreException
//     */
//    @Override
//    public String getCASLoginPageUrl() throws CoreException {
//
//        try {
//            return configurationBusiness.getLoginUrlCas(getBaseURL());
//
//        } catch (MalformedURLException e) {
//            throw new CoreException(e);
//        }
//    }
//
//    /**
//     *
//     * @return @throws MalformedURLException
//     */
//    private URL getBaseURL() throws MalformedURLException {
//
//        HttpServletRequest request = this.getThreadLocalRequest();
//        return (request.getServerPort() == 80)
//                || (request.getServerPort() == 443)
//                ? new URL(request.getScheme() + "://"
//                + request.getServerName()
//                + request.getContextPath())
//                : new URL(request.getScheme() + "://"
//                + request.getServerName() + ":"
//                + request.getServerPort()
//                + request.getContextPath());
//    }
    @Override
    public String getCASLoginPageUrl() throws CoreException {
        URL url = null;
        try {
            url = getBaseURL();
        } catch (MalformedURLException e) {
            throw new CoreException(e);
        }
        return configurationBusiness.getLoginUrlCas(url);
    }

    private URL getBaseURL() throws MalformedURLException {
        URL url = null;
        HttpServletRequest request = this.getThreadLocalRequest();
        if ((request.getServerPort() == 80)
                || (request.getServerPort() == 443)) {
            url = new URL(request.getScheme() + "://"
                    + request.getServerName()
                    + request.getContextPath());
        } else {
            url = new URL(request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath());
        }
        return url;
    }
}
