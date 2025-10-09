package fr.insalyon.creatis.vip.core.server.rpc;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.UsageStats;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.inter.GroupInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ConfigurationServiceImpl extends AbstractRemoteServiceServlet implements ConfigurationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ConfigurationBusiness configurationBusiness;
    private GroupBusiness groupBusiness;
    private UserDAO userDAO;
    private GroupInterface groupInterface;
    private VipSessionBusiness vipSessionBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        configurationBusiness = getBean(ConfigurationBusiness.class);
        userDAO = getBean(UserDAO.class);
        groupBusiness = getBean(GroupBusiness.class);
        groupInterface = getBean(GroupInterface.class);
        vipSessionBusiness = getBean(VipSessionBusiness.class);
    }
    
    @Override
    public User configure() throws CoreException {
        try {

            logger.debug("Initializing VIP configuration.");
            configurationBusiness.configure();
            logger.debug("VIP successfully configured.");

            User user = vipSessionBusiness.resetSessionFromCookie(getThreadLocalRequest());

            if (user != null) {
                configurationBusiness.updateUserLastLogin(user.getEmail());
                trace(logger, "Connected.");

                return user;
            }
            return null;
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }


    @Override
    public void signout() throws CoreException {
        try {
            configurationBusiness.signout(getSessionUser().getEmail());
            trace(logger, "Signed out.");
            getSession().removeAttribute(CoreConstants.SESSION_USER);
            getSession().removeAttribute(CoreConstants.SESSION_GROUPS);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public User activate(String code) throws CoreException {
        try {
            User user = getSessionUser();
            logger.info("Activating '" + user.getEmail() + "'.");
            user = configurationBusiness.activate(user.getEmail(), code);

            return setUserInSession(user);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public String sendActivationCode() throws CoreException {
        try {
            User user = getSessionUser();
            logger.info("Sending activation code to: " + user.getEmail() + ".");
            configurationBusiness.sendActivationCode(user.getEmail());

            return user.getEmail();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void sendResetCode(String email) throws CoreException {
        try {
            //do not add a trace here: it should be reachable without authentication (#2632)
            configurationBusiness.sendResetCode(email);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<User> getUsers() throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            return configurationBusiness.getUsers();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void addGroup(Group group) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding group '" + group + "'.");
            groupBusiness.add(group);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateGroup(String name, Group group) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating group '" + name + "' to '" + group.getName() + "'.");
            groupBusiness.update(name, group);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void removeGroup(String groupName) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing group '" + groupName + "'.");
            groupBusiness.remove(getSessionUser().getEmail(), groupName);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<Group> getGroups() throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            return groupBusiness.get();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<Group> getPublicGroups() throws CoreException {
        try {
            return groupBusiness.getPublic();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<String> getItemsGroup(String groupName) throws CoreException {
        List<String> items = new ArrayList<>();

        try {
            if (groupInterface != null) {
                items = groupInterface.getItems(groupName);
            } else {
                logger.info("No interface is present for GroupInterface !"); 
            }
            
            return items;
        } catch (VipException e) {
            throw new CoreException(e);
        }
    }

    @Override
    public void removeItemFromGroup(String item, String groupname) throws CoreException {
        try {
            if (groupInterface != null) {
                groupInterface.delete(item, groupname);
            } else {
                logger.info("No interface is present for GroupInterface !"); 
            }
        } catch (VipException e) {
            throw new CoreException(e);
        }
    }

    @Override
    public User removeUser(String email) throws CoreException {
        try {
            User user = email != null
                ? configurationBusiness.getUser(email)
                : getSessionUser();
            if (email != null) {
                authenticateSystemAdministrator(logger);
            }
            trace(logger, "Removing user '" + user.getEmail() + "'.");
            configurationBusiness.removeUser(user.getEmail(), true);

            return user;
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public Map<Group, GROUP_ROLE> getUserGroups(String email)
        throws CoreException {
        try {
            if (email != null) {
                authenticateSystemAdministrator(logger);
                return configurationBusiness.getUserGroups(email);
            } else {
                return configurationBusiness.getUserGroups(
                        getSessionUser().getEmail());
            }
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * Throws an exception if the user is not a group or system administrator.
     */
    private void authenticateGroupAdministrator(Logger logger, String groupName) throws CoreException {

        try{
            authenticateSystemAdministrator(logger);
            return;
        } catch(CoreException ignored){ } // The user is not a system administrator. Ignore the exception.

        User user = getSessionUser();
        Map<Group, GROUP_ROLE> userGroups = getUserGroups(null);

        for (Group g : userGroups.keySet()) {
            if (g.getName().equals(groupName) && userGroups.get(g) == GROUP_ROLE.Admin) {
                return;
            }
        }

        logger.error("The user {} has no admin rights for group {}", user.getEmail(), groupName);
        throw new CoreException("The user has no group administrator rights.");
    }

    @Override
    public List<Boolean> getUserPropertiesGroups() throws CoreException {
        try {
            String email = getSessionUser().getEmail();
            return configurationBusiness.getUserPropertiesGroups(email);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<Group> getUserGroups() throws CoreException {
        try {
            List<Group> list = new ArrayList<>();
            if (getSessionUser().isSystemAdministrator()) {
                for (Group group : groupBusiness.get()) {
                    list.add(group);
                }
            } else {
                Map<Group, GROUP_ROLE> groups = getUserGroupsFromSession();
                for (Group group : groups.keySet()) {
                    if (groups.get(group) != GROUP_ROLE.None) {
                        list.add(group);
                    }
                }
            }
            return list;
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateUser(
        String email, UserLevel level, CountryCode countryCode,
        int maxRunningSimulations, Map<String, GROUP_ROLE> groups,
        boolean locked)
        throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating user '" + email + "'.");
            configurationBusiness.updateUser(
                email,
                level,
                countryCode,
                maxRunningSimulations,
                locked);
            configurationBusiness.setUserGroups(email, groups);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public User getUserData() throws CoreException {
        try {
            return configurationBusiness.getUserData(getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public User updateUser(User user) throws CoreException {
        try {
            trace(logger, "Updating user data '" + user.getEmail() + "'.");
            user = configurationBusiness.updateUser(user);
            return setUserInSession(user);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateUserPassword(String currentPassword, String newPassword)
            throws CoreException {
        try {
            trace(logger, "Updating user password.");
            configurationBusiness.updateUserPassword(
                    getSessionUser().getEmail(),
                currentPassword,
                newPassword);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public User requestNewEmail(String newEmail) throws CoreException {
        try {
            User currentUser = getSessionUser();
            String currentEmail = currentUser.getEmail();
            trace(logger, "Requesting email change from " + currentEmail + " to " + newEmail);

            configurationBusiness.requestNewEmail(currentUser, newEmail);

            currentUser = configurationBusiness.getUserData(currentUser.getEmail());
            return setUserInSession(currentUser);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public User confirmNewEmail(String code) throws CoreException {
        try {
            User currentUser = getSessionUser();
            String currentEmail = currentUser.getEmail();
            String newEmail = currentUser.getNextEmail();
            trace(logger, "Confirming email change from " + currentEmail + " to " + newEmail);
            if (code == null || !code.equals(currentUser.getCode())) {
                logger.error("Wrong validation code for {} : {} vs {}",
                        currentEmail, code, currentUser.getCode());
                throw new CoreException("Wrong validation code");
            }
            configurationBusiness.updateUserEmail(currentEmail, newEmail);
            configurationBusiness.resetNextEmail(newEmail);

            currentUser = configurationBusiness.getUserData(newEmail);
            return setUserInSession(currentUser);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public User cancelNewEmail() throws CoreException {
        try {
            User currentUser = getSessionUser();
            String currentEmail = currentUser.getEmail();
            String newEmail = currentUser.getNextEmail();
            trace(logger, "Canceling email change from " + currentEmail + " to " + newEmail);
            configurationBusiness.resetNextEmail(currentEmail);

            currentUser = configurationBusiness.getUserData(currentEmail);
            return setUserInSession(currentUser);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void sendContactMail(String category, String subject, String comment)
            throws CoreException {
        try {
            configurationBusiness.sendContactMail(
                    getSessionUser(), category, subject, comment);

        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void activateUser(String email) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Activating user: " + email);
            configurationBusiness.activateUser(email);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void addUserToGroup(String groupName) throws CoreException {
        try {
            trace(logger, "Adding user to group '" + groupName + "'.");
            configurationBusiness.addUserToGroup(
                    getSessionUser().getEmail(), groupName);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<User> getUsersFromGroup(String groupName) throws CoreException {
        try {
            authenticateGroupAdministrator(logger, groupName);
            return configurationBusiness.getUsersFromGroup(groupName);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void removeUserFromGroup(String email, String groupName)
            throws CoreException {
        try {
            if (email != null) {
                authenticateSystemAdministrator(logger);
                trace(logger, "Removing '" + email + "' from '" + groupName + "' group.");
                configurationBusiness.removeUserFromGroup(email, groupName);
            } else {
                trace(logger, "Removing user from '" + groupName + "' group.");
                configurationBusiness.removeUserFromGroup(
                        getSessionUser().getEmail(), groupName);
            }
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void resetPassword(String email, String code, String password)
            throws CoreException {
        try {
            logger.info("(" + email + ") Reseting password.");
            configurationBusiness.resetPassword(email, code, password);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public String getCASLoginPageUrl() throws CoreException {
        URL url;
        try {
            url = getBaseURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new CoreException(e);
        }
        return configurationBusiness.getLoginUrlCas(url);
    }

    private URL getBaseURL() throws MalformedURLException, URISyntaxException {
        URL url;
        HttpServletRequest request = this.getThreadLocalRequest();
        if ((request.getServerPort() == 80)
                || (request.getServerPort() == 443)) {
            url = new URI(request.getScheme() + "://"
                    + request.getServerName()
                    + request.getContextPath()).toURL();
        } else {
            url = new URI(request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath()).toURL();
        }
        return url;
    }

    @Override
    public UsageStats getUsageStats() throws CoreException {
        try {
            Integer users = userDAO.getNUsers();
            Integer countries = userDAO.getNCountries();
            return new UsageStats(users, countries);
        } catch (DAOException e) {
            throw new CoreException(e);
        }

    }

    @Override
    public void updateTermsOfUse() throws CoreException {
        trace(logger, "Updating terms of use.");
        User user = getSessionUser();
        try {
            configurationBusiness.updateTermsOfUse(user.getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void addTermsUse() throws CoreException {
        trace(logger, "adding new terms of Use.");
        try {
            authenticateSystemAdministrator(logger);
            configurationBusiness.addTermsUse();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public Timestamp getLastUpdateTermsOfUse() throws CoreException {
        try {
            return configurationBusiness.getLastUpdateTermsOfUse();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public boolean compare() throws CoreException {
        return getLastUpdateTermsOfUse().after(getSessionUser().getTermsOfUse());
    }

    @Override
    public boolean testLastUpdatePublication() throws CoreException {
        try {
            return configurationBusiness.testLastUpdatePublication(
                    getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateLastUpdatePublication() throws CoreException {
        trace(logger, "Updating Last Update Publication.");
        User user = getSessionUser();
        try {
            configurationBusiness.updateLastUpdatePublication(user.getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    // api key management
    @Override
    public String getUserApikey(String email) throws CoreException {
        try {
            return configurationBusiness
                    .getUserApikey(getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void deleteUserApikey(String email) throws CoreException {
        try {
            configurationBusiness
                .deleteUserApikey(getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public String generateNewUserApikey(String email) throws CoreException {
        try {
            return configurationBusiness
                .generateNewUserApikey(getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<String> getMissingGroupsRessources(String email) throws CoreException {
        try {
            User user = configurationBusiness.getUser(email);

            return groupInterface.getMissingGroupsRessources(user);
        } catch (VipException e) {
            throw new CoreException(e);
        }
    }
}
