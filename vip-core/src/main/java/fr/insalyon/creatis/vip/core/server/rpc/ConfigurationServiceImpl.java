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
package fr.insalyon.creatis.vip.core.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.h2.PlatformConnection;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class ConfigurationServiceImpl extends AbstractRemoteServiceServlet implements ConfigurationService {

    private static Logger logger = Logger.getLogger(ConfigurationServiceImpl.class);

    public ConfigurationServiceImpl() {
        super();
        configurationBusiness = new ConfigurationBusiness();
    }

    /**
     * 
     * @throws CoreException 
     */
    public User configure(String email, String session) throws CoreException {

        try {
            logger.info("Initializing VIP configuration.");
            PlatformConnection.getInstance().setConnection(
                    (Connection) getServletContext().getAttribute("connection"));
            PlatformConnection.getInstance().createTables();

            configurationBusiness.configure();
            logger.info("VIP successfully configured.");

            User user = (User) getSession().getAttribute(CoreConstants.SESSION_USER);
            if (user != null) {
                user.setSystemAdministrator(configurationBusiness.isSystemAdministrator(user.getEmail()));

            } else if (configurationBusiness.validateSession(email, session)) {

                user = configurationBusiness.getUser(email);
                user.setSystemAdministrator(configurationBusiness.isSystemAdministrator(user.getEmail()));

                Map<String, ROLE> groups = configurationBusiness.getUserGroups(email);
                user.setGroups(groups);

                getSession().setAttribute(CoreConstants.SESSION_USER, user);
                getSession().setAttribute(CoreConstants.SESSION_GROUPS, groups);
            }

            return user;

        } catch (DAOException ex) {
            throw new CoreException(ex);
        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @param user
     * @throws CoreException 
     */
    public void signup(User user) throws CoreException {

        try {
            logger.info("Sign up request from '" + user.getEmail() + "'.");
            configurationBusiness.signup(user);

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
    public User signin(String email, String password) throws CoreException {

        try {
            logger.info("Authenticating '" + email + "'.");
            User user = configurationBusiness.signin(email, password);
            user.setSystemAdministrator(configurationBusiness.isSystemAdministrator(user.getEmail()));

            Map<String, ROLE> groups = configurationBusiness.getUserGroups(email);
            user.setGroups(groups);

            getSession().setAttribute(CoreConstants.SESSION_USER, user);
            getSession().setAttribute(CoreConstants.SESSION_GROUPS, groups);

            return user;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @throws CoreException 
     */
    public void signout() throws CoreException {

        try {
            configurationBusiness.signout(getSessionUser().getEmail());
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
    public User activate(String email, String code) throws CoreException {

        try {
            logger.info("Activating '" + email + "'.");
            User user = configurationBusiness.activate(email, code);

            getSession().setAttribute(CoreConstants.SESSION_USER, user);

            return user;

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @param email
     * @throws CoreException 
     */
    public void sendActivationCode(String email) throws CoreException {

        try {
            logger.info("Sending activation code to: " + email + ".");
            configurationBusiness.sendActivationCode(email);

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
     * @param groupName
     * @throws CoreException 
     */
    public void addGroup(String groupName) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding group '" + groupName + "'.");
            configurationBusiness.addGroup(groupName);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @param oldName
     * @param newName
     * @throws CoreException 
     */
    public void updateGroup(String oldName, String newName) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating group '" + oldName + "' to '" + newName + "'.");
            configurationBusiness.updateGroup(oldName, newName);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @param groupName
     * @throws CoreException 
     */
    public void removeGroup(String groupName) throws CoreException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing group '" + groupName + "'.");
            configurationBusiness.removeGroup(groupName);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws CoreException 
     */
    public List<String> getGroups() throws CoreException {

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
     * @throws CoreException 
     */
    public void removeUser(String email) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing user '" + email + "'.");
            configurationBusiness.removeUser(email);

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
    public Map<String, CoreConstants.ROLE> getUserGroups(String email) throws CoreException {

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
     * @return
     * @throws CoreException 
     */
    public List<String> getUserGroups() throws CoreException {

        try {
            return configurationBusiness.getUserGroupsName(getSessionUserGroups());

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param groups
     * @throws CoreException 
     */
    public void setUserGroups(String email, Map<String, CoreConstants.ROLE> groups) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Defining groups to '" + email + "'.");
            configurationBusiness.setUserGroups(email, groups);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws CoreException 
     */
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
    public void updateUser(User user) throws CoreException {

        try {
            trace(logger, "Updating user data '" + user.getEmail() + "'.");
            configurationBusiness.updateUser(user);

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
}
