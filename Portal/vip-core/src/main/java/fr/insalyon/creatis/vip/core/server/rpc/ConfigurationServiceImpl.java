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
package fr.insalyon.creatis.vip.core.server.rpc;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import fr.insalyon.creatis.vip.core.client.bean.UsageStats;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.user.publication.PublicationTypes;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

/**
 *
 * @author Rafael Ferreira da Silva,Nouha boujelben
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
     * @param accountType User's accounts type
     * @throws CoreException
     */
    @Override
    public void signup(User user, String comments, String[] accountType) throws CoreException {

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
            logger.info("Connected.");

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
            //do not add a trace here: it should be reachable without authentication (#2632)
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
     * @return @throws CoreException
     */
    @Override
    public List<Group> getPublicGroups() throws CoreException {

        try {
            return configurationBusiness.getPublicGroups();

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
     * Throws an exception if the user is not a group or system administrator.
     * @param logger
     * @param groupName
     * @throws CoreException
     */
    protected void authenticateGroupAdministrator(Logger logger, String groupName) throws CoreException {

        try{
            authenticateSystemAdministrator(logger);
            return;
        } catch(CoreException ex){ } // The user is not a system administrator. Ignore the exception.
        
        User user = getSessionUser();
        Map<Group, GROUP_ROLE> userGroups = getUserGroups(null);

        for (Group g : userGroups.keySet()) {
            if (g.getName().equals(groupName) && userGroups.get(g) == GROUP_ROLE.Admin) {
                return;
            }
        }

        logger.error("The user has no admin rights for group " + groupName + ": " + user.getEmail());
        throw new CoreException("The user has no group administrator rights.");
    }

    @Override
    public List<Boolean> getUserPropertiesGroups() throws CoreException {
        try {
            String email = getSessionUser().getEmail();
            return configurationBusiness.getUserPropertiesGroups(email);

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
            int maxRunningSimulations, Map<String, GROUP_ROLE> groups, boolean locked) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating user '" + email + "'.");
            configurationBusiness.updateUser(email, level, countryCode, maxRunningSimulations, locked);
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
    public User setUserSession(User user) throws BusinessException {

        return setUserSession(user, getSession());

    }

    public User setUserSession(User user, HttpSession session) throws BusinessException {
        Map<Group, GROUP_ROLE> groups = configurationBusiness.getUserGroups(user.getEmail());
        user.setGroups(groups);

        session.setAttribute(CoreConstants.SESSION_USER, user);
        session.setAttribute(CoreConstants.SESSION_GROUPS, groups);

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
            authenticateGroupAdministrator(logger, groupName);
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

    @Override
    public UsageStats getUsageStats() throws CoreException {
        try {
            Integer users = CoreDAOFactory.getDAOFactory().getUserDAO().getNUsers();
            Integer countries = CoreDAOFactory.getDAOFactory().getUserDAO().getNCountries();
            return new UsageStats(users, countries);
        } catch (DAOException ex) {
            throw new CoreException(ex);
        }

    }

    @Override
    public String linkDropboxAccount() throws CoreException {
        trace(logger, "Linking Dropbox account.");
        User user = getSessionUser();
        //TODO: put this key pair in config file
        AppKeyPair consumerTokenPair = new AppKeyPair("wqkjwy11upck7vi", "euieqqi699m3zu6");
        WebAuthSession session = new WebAuthSession(consumerTokenPair, AccessType.APP_FOLDER);
        try {
            //TODO: put server URL instead
            WebAuthInfo wai = session.getAuthInfo("REDIRECT");

            try {
                String dir = Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder();
                CoreUtil.getGRIDAClient().createFolder(dir, "Dropbox");
                CoreDAOFactory.getDAOFactory().getUserDAO().linkDropboxAccount(user.getEmail(), dir + "/Dropbox", wai.requestTokenPair.key, wai.requestTokenPair.secret);
            } catch (DAOException ex) {
                throw new CoreException(ex);
            } catch (GRIDAClientException ex) {
                throw new CoreException(ex);
            }
            return wai.url;
        } catch (DropboxException ex) {
            throw new CoreException(ex);
        }

    }

    @Override
    public void activateDropboxAccount(String oauth_token) throws CoreException {
        trace(logger, "Activating Dropbox account.");
        User user = getSessionUser();
        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().activateDropboxAccount(user.getEmail(), oauth_token);
        } catch (DAOException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public DropboxAccountStatus.AccountStatus getDropboxAccountStatus() throws CoreException {
        trace(logger, "Getting Dropbox account status.");
        User user = getSessionUser();
        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO().getDropboxAccountStatus(user.getEmail());
        } catch (DAOException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void unlinkDropboxAccount() throws CoreException {
        trace(logger, "Unlinking Dropbox account.");
        User user = getSessionUser();
        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().unlinkDropboxAccount(user.getEmail());
        } catch (DAOException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateTermsOfUse() throws CoreException {
        trace(logger, "Updating terms of use.");
        User user = getSessionUser();
        try {
            configurationBusiness.updateTermsOfUse(user.getEmail());

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<Publication> getPublications() throws CoreException {
        trace(logger, "Getting publication list.");
        try {
            return configurationBusiness.getPublications();

        } catch (BusinessException ex) {
            logger.error(ex);
            throw new CoreException(ex);
        }
    }

    @Override
    public void removePublication(Long id) throws CoreException {
        trace(logger, "Removing publication.");

        try {
            User user = getSessionUser();
            if (user.isSystemAdministrator() || configurationBusiness.getPublication(id).getVipAuthor().equals(user.getEmail())) {
                configurationBusiness.removePublication(id);
            } else {
                throw new CoreException("you can't remove a publication that is not yours");

            }

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void addPublication(Publication pub) throws CoreException {
        trace(logger, "Adding publication.");

        try {
            User user = getSessionUser();
            pub.setVipAuthor(user.getEmail());
            configurationBusiness.addPublication(pub);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }

    }

    @Override
    public void updatePublication(Publication pub) throws CoreException {
        trace(logger, "Updating publication.");

        try {
            User user = getSessionUser();
            if (user.isSystemAdministrator() || configurationBusiness.getPublication(pub.getId()).getVipAuthor().equals(user.getEmail())) {
                pub.setVipAuthor(user.getEmail());
                configurationBusiness.updatePublication(pub);
            } else {
                throw new CoreException("you can't modify a publication that is not yours");

            }

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }

    }

    @Override
    public void addTermsUse(TermsOfUse termsofUse) throws CoreException {
        trace(logger, "adding new terms of Use.");
        try {

            authenticateSystemAdministrator(logger);
            configurationBusiness.addTermsUse(termsofUse);

        } catch (BusinessException ex) {

            throw new CoreException(ex);
        }
    }

    @Override
    public Timestamp getLastUpdateTermsOfUse() throws CoreException {
        try {
            return configurationBusiness.getLastUpdateTermsOfUse();
        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public boolean compare() throws CoreException {
        return getLastUpdateTermsOfUse().after(getSessionUser().getTermsOfUse());
    }

    @Override
    public int getMaxConfiguredPlatformSimulation() throws CoreException {
        return Server.getInstance().getMaxPlatformRunningSimulations();
    }

    @Override
    public void changeMaxConfiguredPlatformSimulation(int maxPlatformRunningSimulations) throws CoreException {
        try {
            Server.getInstance().setMaxPlatformRunningSimulations(maxPlatformRunningSimulations);
        } catch (ConfigurationException ex) {
            logger.error(ex);
            throw new CoreException(ex);
        }
    }

    @Override
    public List<Publication> parseBibtexText(String s) throws CoreException {
        List<Publication> publications = new ArrayList<Publication>();
        try {
            Reader reader = new StringReader(s);
            org.jbibtex.BibTeXParser bibtexParser = new org.jbibtex.BibTeXParser();
            org.jbibtex.BibTeXDatabase database = bibtexParser.parseFully(reader);
            Map<org.jbibtex.Key, org.jbibtex.BibTeXEntry> entryMap = database.getEntries();
            Collection<org.jbibtex.BibTeXEntry> entries = entryMap.values();
            for (org.jbibtex.BibTeXEntry entry : entries) {
                String type = entry.getType().toString();
                org.jbibtex.Value title = entry.getField(org.jbibtex.BibTeXEntry.KEY_TITLE);
                org.jbibtex.Value date = entry.getField(org.jbibtex.BibTeXEntry.KEY_YEAR);
                org.jbibtex.Value doi = entry.getField(org.jbibtex.BibTeXEntry.KEY_DOI);
                org.jbibtex.Value authors = entry.getField(org.jbibtex.BibTeXEntry.KEY_AUTHOR);
                org.jbibtex.Value typeName = entry.getField(org.jbibtex.BibTeXEntry.KEY_BOOKTITLE);
                String doiv;
                if (doi == null) {
                    doiv = "";
                } else {
                    doiv = doi.toUserString();
                }
                publications.add(new Publication(title.toUserString(), date.toUserString(), doiv, authors.toUserString(), parseTypePublication(type), getTypeName(entry, type), getSessionUser().getEmail()));

            }

        } catch (ParseException ex) {
            logger.error(ex);
            throw new CoreException(ex);
        } catch (TokenMgrException ex) {
            logger.error(ex);
            throw new CoreException(ex);
        }
        return publications;
    }

    private String parseTypePublication(String type) {
        if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference")) {
            return PublicationTypes.ConferenceArticle.toString();
        } else if (type.equalsIgnoreCase("article")) {
            return PublicationTypes.Journal.toString();
        } else if (type.equalsIgnoreCase("inbook") || type.equalsIgnoreCase("incollection")) {
            return PublicationTypes.BookChapter.toString();
        } else {
            return PublicationTypes.Other.toString();
        }

    }

    private String getTypeName(org.jbibtex.BibTeXEntry entry, String type) {
        if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference") || type.equalsIgnoreCase("incollection")) {
            return entry.getField(org.jbibtex.BibTeXEntry.KEY_BOOKTITLE).toUserString();
        } else if (type.equalsIgnoreCase("article")) {
            return entry.getField(org.jbibtex.BibTeXEntry.KEY_JOURNAL).toUserString();
        } else {
            return "";
        }

    }

    @Override
    public boolean testLastUpdatePublication() throws CoreException {
        try {

            return configurationBusiness.testLastUpdatePublication(getSessionUser().getEmail());

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updateLastUpdatePublication() throws CoreException {
        trace(logger, "Updating Last Update Publication.");
        User user = getSessionUser();
        try {
            configurationBusiness.updateLastUpdatePublication(user.getEmail());

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }
}
