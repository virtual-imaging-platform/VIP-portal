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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ConfigurationBusiness {

    private final static Logger logger = Logger.getLogger(ConfigurationBusiness.class);

    /**
     *
     * @throws BusinessException
     */
    public void configure() throws BusinessException {

        PropertyConfigurator.configure(ConfigurationBusiness.class.getClassLoader().getResource("vipLog4j.properties"));

        try {
            logger.info("Configuring VIP server proxy.");
            ProxyClient myproxy = new ProxyClient();
            myproxy.getProxy();

        } catch (Exception ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param session
     * @return
     * @throws BusinessException
     */
    public boolean validateSession(String email, String session) throws BusinessException {

        try {
            if (email != null && session != null) {
                UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();

                if (userDAO.verifySession(email, session)) {
                    String newSession = UUID.randomUUID().toString();
                    userDAO.updateSession(email, newSession);

                    return true;
                }
            }
            return false;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws BusinessException
     */
    public User getUser(String email) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void signup(User user, String comments, boolean createdFromCAS, boolean mapPrivateGroups, String... accountType) throws BusinessException {
        try {
            user.setCode(UUID.randomUUID().toString());
            user.setPassword(MD5.get(user.getPassword()));
            String folder = user.getFirstName().replaceAll(" ", "_").toLowerCase() + "_"
                    + user.getLastName().replaceAll(" ", "_").toLowerCase();
            folder = Normalizer.normalize(folder, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            GRIDAClient client = CoreUtil.getGRIDAClient();
            while (client.exist(Server.getInstance().getDataManagerUsersHome() + "/" + folder)) {
                folder += "_" + new Random().nextInt(10000);
            }

            user.setFolder(folder);
            user.setLevel(UserLevel.Beginner);

            CoreDAOFactory.getDAOFactory().getUserDAO().add(user);

            // Adding user to groups
            for (Group group : CoreDAOFactory.getDAOFactory().getAccountDAO().getGroups(accountType)) {
                if (mapPrivateGroups || createdFromCAS || group.isPublicGroup()) {
                    CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().add(user.getEmail(), group.getName(), GROUP_ROLE.User);
                } else {
                    logger.info("Don't map user " + user.getEmail() + " to private group " + group.getName());
                }
            }

            if (!createdFromCAS) {
                String emailContent = "<html>"
                        + "<head></head>"
                        + "<body>"
                        + "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                        + "<p>We have successfully received your membership registration "
                        + "and your personal profile has been created.</p>"
                        + "<p>Please confirm your registration using the following activation "
                        + "code on your first login:</p>"
                        + "<p><b>" + user.getCode() + "</b></p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                logger.info("Sending confirmation email to '" + user.getEmail() + "'.");
                CoreUtil.sendEmail("VIP account details", emailContent,
                        new String[]{user.getEmail()}, true, user.getEmail());

                StringBuilder accounts = new StringBuilder();
                for (String account : accountType) {
                    if (accounts.length() > 0) {
                        accounts.append(", ");
                    }
                    accounts.append(account);
                }

                String adminsEmailContents = "<html>"
                        + "<head></head>"
                        + "<body>"
                        + "<p>Dear Administrator,</p>"
                        + "<p>A new user requested an account:</p>"
                        + "<p><b>First Name:</b> " + user.getFirstName() + "</p>"
                        + "<p><b>Last Name:</b> " + user.getLastName() + "</p>"
                        + "<p><b>Email:</b> " + user.getEmail() + "</p>"
                        + "<p><b>Institution:</b> " + user.getInstitution() + "</p>"
                        + "<p><b>Phone:</b> " + user.getPhone() + "</p>"
                        + "<p><b>Country:</b> " + user.getCountryCode().getCountryName() + "</p>"
                        + "<p><b>Accounts:</b> " + accounts.toString() + "</p>"
                        + "<p><b>Comments:</b><br />" + comments + "</p>"
                        + "<p>&nbsp;</p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                for (String email : getAdministratorsEmails()) {
                    CoreUtil.sendEmail("[VIP Admin] Account Requested", adminsEmailContents,
                            new String[]{email}, true, user.getEmail());
                }
            } else {
                String adminsEmailContents = "<html>"
                        + "<head></head>"
                        + "<body>"
                        + "<p>Dear Administrators,</p>"
                        + "<p>The following account was automatically created for a CAS user:</p>"
                        + "<p><b>First Name:</b> " + user.getFirstName() + "</p>"
                        + "<p><b>Last Name:</b> " + user.getLastName() + "</p>"
                        + "<p><b>Email:</b> " + user.getEmail() + "</p>"
                        + "<p><b>Institution:</b> " + user.getInstitution() + "</p>"
                        + "<p><b>Phone:</b> " + user.getPhone() + "</p>"
                        + "<p><b>Country:</b> " + user.getCountryCode().getCountryName() + "</p>"
                        + "<p><b>Account Type:</b> " + accountType + "</p>"
                        + "<p><b>Comments:</b><br />" + comments + "</p>"
                        + "<p>&nbsp;</p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                for (String email : getAdministratorsEmails()) {
                    CoreUtil.sendEmail("[VIP Admin] Automatic Account Creation", adminsEmailContents,
                            new String[]{email}, false, user.getEmail());
                }
            }
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param comments
     * @param accountType
     * @throws BusinessException
     */
    public void signup(User user, String comments, String... accountType) throws BusinessException {
        signup(user, comments, false, false, accountType);

    }

    /**
     *
     * @param email
     * @param password
     * @return
     * @throws BusinessException
     */
    public User signin(String email, String password) throws BusinessException {

        try {
            password = MD5.get(password);
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();

            if (userDAO.authenticate(email, password)) {

                return getUserWithSession(email);

            } else {
                logger.error("Authentication failed to '" + email + "' (email or password incorrect).");
                throw new BusinessException("Authentication failed (email or password incorrect).");
            }
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param ticket
     * @param password
     * @return
     * @throws BusinessException
     */
    public User signin(String ticket, URL serviceURL) throws BusinessException {

        //validate ticket
        User user = getCASUser(ticket, serviceURL);

        if (user != null) {

            UserDAO userDAO;
            try {
                userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }
            try {
                user = userDAO.getUser(user.getEmail());
            } catch (DAOException ex) {
                try {
                    signup(user, "Generated from CAS login", true, true, Server.getInstance().getCasAccountType());
                    this.activateUser(user.getEmail());
                    user = userDAO.getUser(user.getEmail());

                } catch (DAOException ex1) {
                    throw new BusinessException(ex1);
                }
            }
            try {
                return getUserWithSession(user.getEmail());
            } catch (DAOException e) {
                throw new BusinessException(e);
            }

        } else {
            logger.error("Authentication failed to CAS ticket '" + ticket + "' (invalid ticket, cannot get user email or cannot contact server).");
            throw new BusinessException("Authentication failed (invalid CAS ticket, cannot get user email or cannot contact server).");
        }

    }

    private User getCASUser(String ticket, URL serviceURL) {
        //  String userId = null;
        if (ticket != null) {

            Saml11TicketValidator sv = new Saml11TicketValidator(Server.getInstance().getCasURL());

            Assertion a;
            try {
                a = sv.validate(ticket, serviceURL.toString());
            } catch (TicketValidationException ex) {
                return null;
            }
            String email = (a.getPrincipal().getAttributes().get("USER_EMAIL_ADDRESS")).toString();
            String[] n = (a.getPrincipal().getAttributes().get("USER_FULL_NAME")).toString().split(" ");
            String first = "";
            String last = "";
            if (n.length >= 2) {
                first = n[0];
                for (int i = 1; i < n.length; i++) {
                    if (i > 1) {
                        last += " ";
                    }
                    last += n[i];
                }
            } else {
                last = n[0];
            }

            CountryCode cc = CountryCode.aq;

            String country = "";

            try {
                country = email.substring(email.lastIndexOf('.') + 1);
            } catch (NullPointerException e) {
            }

            try {
                if (CountryCode.valueOf(country) != null) {
                    cc = CountryCode.valueOf(country);
                }
            } catch (IllegalArgumentException e) {
                logger.info("Cannot determine country from email extension " + country + ": user will be mapped to Antartica");
            }

            User u = new User(
                    first.trim(),
                    last.trim(),
                    email.trim(),
                    "Unknown",
                    UUID.randomUUID().toString(),
                    "0000",
                    cc);

            return u;
        }
        return null;
    }

    /**
     *
     * @param email
     * @throws BusinessException
     */
    public void signout(String email) throws BusinessException {

        try {
            String session = UUID.randomUUID().toString();
            CoreDAOFactory.getDAOFactory().getUserDAO().updateSession(email, session);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param code
     * @return
     * @throws BusinessException
     */
    public User activate(String email, String code) throws BusinessException {

        try {
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            if (userDAO.activate(email, code)) {

                User user = userDAO.getUser(email);

                GRIDAClient client = CoreUtil.getGRIDAClient();
                client.createFolder(Server.getInstance().getDataManagerUsersHome(),
                        user.getFolder());

                client.createFolder(Server.getInstance().getDataManagerUsersHome(),
                        user.getFolder() + "_" + CoreConstants.FOLDER_TRASH);

                return user;

            } else {
                logger.error("Activation failed to '" + email + "' (wrong code).");
                throw new BusinessException("Activation failed.");
            }

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws BusinessException
     */
    public void activateUser(String email) throws BusinessException {

        try {
            User user = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);
            activate(email, user.getCode());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws BusinessException
     */
    public void sendActivationCode(String email) throws BusinessException {

        try {
            User user = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear " + user.getFullName() + ",</p>"
                    + "<p>You requested us to send you your personal activation code.</p>"
                    + "<p>Please use the following code to activate your account:</p>"
                    + "<p><b>" + user.getCode() + "</b></p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            CoreUtil.sendEmail("VIP activation code (reminder)", emailContent,
                    new String[]{user.getEmail()}, true, user.getEmail());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws BusinessException
     */
    public void sendResetCode(String email) throws BusinessException {

        try {
            User user = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

            String code = UUID.randomUUID().toString();
            CoreDAOFactory.getDAOFactory().getUserDAO().updateCode(email, code);

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear " + user.getFullName() + ",</p>"
                    + "<p>You recently requested a new password to sign in to your VIP account.</p>"
                    + "<p>Please use the following code to reset your password:</p>"
                    + "<p><b>" + code + "</b></p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            CoreUtil.sendEmail("Code to reset your VIP password", emailContent,
                    new String[]{user.getEmail()}, true, user.getEmail());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param sendNotificationEmail
     * @throws BusinessException
     */
    public void removeUser(String email, boolean sendNotificationEmail) throws BusinessException {

        try {
            User user = getUser(email);
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();

            client.removeOperationsByUser(email);

            client.delete(Server.getInstance().getDataManagerUsersHome() + "/"
                    + user.getFolder(), user.getEmail());
            client.delete(Server.getInstance().getDataManagerUsersHome() + "/"
                    + user.getFolder() + "_" + CoreConstants.FOLDER_TRASH, user.getEmail());

            CoreDAOFactory.getDAOFactory().getUserDAO().remove(email);

            if (sendNotificationEmail) {

                String adminsEmailContents = "<html>"
                        + "<head></head>"
                        + "<body>"
                        + "<p>Dear Administrators,</p>"
                        + "<p>The following user removed her/his account:</p>"
                        + "<p><b>First Name:</b> " + user.getFirstName() + "</p>"
                        + "<p><b>Last Name:</b> " + user.getLastName() + "</p>"
                        + "<p><b>Email:</b> " + user.getEmail() + "</p>"
                        + "<p><b>Institution:</b> " + user.getInstitution() + "</p>"
                        + "<p><b>Phone:</b> " + user.getPhone() + "</p>"
                        + "<p>&nbsp;</p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                for (String adminEmail : getAdministratorsEmails()) {
                    CoreUtil.sendEmail("[VIP Admin] Account Removed", adminsEmailContents,
                            new String[]{adminEmail}, true, user.getEmail());
                }
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<User> getUsers() throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO().getUsers();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<String> getUserNames(String email, boolean validGroup)
            throws BusinessException {

        try {
            if (validGroup) {

                List<String> groups = CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserAdminGroups(email);

                if (groups.isEmpty()) {
                    List<String> userNames = new ArrayList<String>();
                    userNames.add(CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email).getFullName());
                    return userNames;

                } else {
                    return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroups(groups);
                }

            } else {
                List<String> userNames = new ArrayList<String>();
                for (User user : getUsers()) {
                    userNames.add(user.getFullName());
                }

                return userNames;
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param group
     * @return
     * @throws BusinessException
     */
    public void addGroup(Group group) throws BusinessException {

        try {
            GRIDAClient client = CoreUtil.getGRIDAClient();
            client.createFolder(Server.getInstance().getDataManagerGroupsHome(),
                    group.getName().replaceAll(" ", "_"));

            CoreDAOFactory.getDAOFactory().getGroupDAO().add(group);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param groupName
     * @throws BusinessException
     */
    public void removeGroup(String user, String groupName) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            client.delete(Server.getInstance().getDataManagerGroupsHome() + "/"
                    + groupName.replaceAll(" ", "_"), user);
            CoreDAOFactory.getDAOFactory().getGroupDAO().remove(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param name
     * @param group
     * @throws BusinessException
     */
    public void updateGroup(String name, Group group) throws BusinessException {

        try {
            GRIDAClient client = CoreUtil.getGRIDAClient();
            if (!name.equals(group.getName())) {
                client.rename(
                        Server.getInstance().getDataManagerGroupsHome() + "/" + name.replaceAll(" ", "_"),
                        Server.getInstance().getDataManagerGroupsHome() + "/" + group.getName().replaceAll(" ", "_"));
            }

            CoreDAOFactory.getDAOFactory().getGroupDAO().update(name, group);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<Group> getGroups() throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws BusinessException
     */
    public Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserGroups(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param groups
     * @return
     * @throws BusinessException
     */
    public List<String> getUserGroupsName(Map<String, CoreConstants.GROUP_ROLE> groups) throws BusinessException {

        return new ArrayList<String>(groups.keySet());
    }

    /**
     *
     * @param email
     * @param groups
     * @throws BusinessException
     */
    public void setUserGroups(String email, Map<String, CoreConstants.GROUP_ROLE> groups)
            throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().setUserGroups(email, groups);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public User getUserData(String email) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param oldData
     * @param user
     * @return
     * @throws BusinessException
     */
    public User updateUser(User oldData, User user) throws BusinessException {

        try {

            user.setFolder(user.getFirstName().replaceAll(" ", "_").toLowerCase() + "_"
                    + user.getLastName().replaceAll(" ", "_").toLowerCase());

            CoreDAOFactory.getDAOFactory().getUserDAO().update(user);

            if (!oldData.getFolder().equals(user.getFolder())) {
                GRIDAClient client = CoreUtil.getGRIDAClient();
                client.rename(
                        Server.getInstance().getDataManagerUsersHome() + "/" + oldData.getFolder(),
                        Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder());

                client.rename(
                        Server.getInstance().getDataManagerUsersHome() + "/" + oldData.getFolder() + "_" + CoreConstants.FOLDER_TRASH,
                        Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder() + "_" + CoreConstants.FOLDER_TRASH);
            }

            return user;

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param currentPassword
     * @param newPassword
     * @throws BusinessException
     */
    public void updateUserPassword(String email, String currentPassword,
            String newPassword) throws BusinessException {

        try {
            currentPassword = MD5.get(currentPassword);
            newPassword = MD5.get(newPassword);
            CoreDAOFactory.getDAOFactory().getUserDAO().updatePassword(
                    email, currentPassword, newPassword);

        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param category
     * @param subject
     * @param comment
     * @throws BusinessException
     */
    public void sendContactMail(User user, String category, String subject,
            String comment) throws BusinessException {

        try {
            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p><b>VIP Contact</b></p>"
                    + "<p><b>User:</b> " + user.getFullName() + "</p>"
                    + "<p><b>Email:</b> <a href=\"mailto:" + user.getEmail() + "\">" + user.getEmail() + "</a></p>"
                    + "<p>&nbsp;</p>"
                    + "<p><b>Category:</b> " + category + "</p>"
                    + "<p><b>Subject:</b> " + subject + "</p>"
                    + "<p>&nbsp;</p>"
                    + "<p><b>Comments:</b></p>"
                    + "<p>" + comment + "</p>"
                    + "</body>"
                    + "</html>";

            for (User u : CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroup(CoreConstants.GROUP_SUPPORT)) {
                CoreUtil.sendEmail("[VIP Contact] " + category, emailContent,
                        new String[]{u.getEmail()}, true, user.getEmail());
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws BusinessException
     */
    public void updateUserLastLogin(String email) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().updateLastLogin(email, new Date());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param groupName
     * @throws BusinessException
     */
    public void addUserToGroup(String email, String groupName) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().add(email, groupName, GROUP_ROLE.User);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param level
     * @param countryCode
     * @param maxRunningSimulations 
     * @throws BusinessException
     */
    public void updateUser(String email, UserLevel level, CountryCode countryCode,
            int maxRunningSimulations) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().update(email, level,
                    countryCode, maxRunningSimulations);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param groupName
     * @return
     * @throws BusinessException
     */
    public List<User> getUsersFromGroup(String groupName) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroup(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param groupName
     * @throws BusinessException
     */
    public void removeUserFromGroup(String email, String groupName) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().removeUserFromGroup(email, groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param code
     * @param password
     * @throws BusinessException
     */
    public void resetPassword(String email, String code, String password) throws BusinessException {

        try {
            User user = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

            if (code.equals(user.getCode())) {
                CoreDAOFactory.getDAOFactory().getUserDAO().resetPassword(email, MD5.get(password));

            } else {
                throw new BusinessException("Wrong reset code.");
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * Gets an array of administrator's e-mails
     *
     * @return
     * @throws DAOException
     */
    private String[] getAdministratorsEmails() throws DAOException {

        List<String> emails = new ArrayList<String>();
        for (User admin : CoreDAOFactory.getDAOFactory().getUserDAO().getAdministrators()) {
            emails.add(admin.getEmail());
        }
        return emails.toArray(new String[]{});
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<Account> getAccounts() throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getAccountDAO().getList();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param name
     * @param groups
     * @throws BusinessException
     */
    public void addAccount(String name, List<String> groups) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getAccountDAO().add(name, groups);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param oldName
     * @param newName
     * @param groups
     * @throws BusinessException
     */
    public void updateAccount(String oldName, String newName, List<String> groups) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getAccountDAO().update(oldName, newName, groups);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param name
     * @throws BusinessException
     */
    public void removeAccount(String name) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getAccountDAO().remove(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    private User getUserWithSession(String email) throws DAOException {
        UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();

        String session = UUID.randomUUID().toString();
        userDAO.updateSession(email, session);

        return userDAO.getUser(email);
    }

    public String getLoginUrlCas(URL serviceURL) {
        return Server.getInstance().getCasURL() + "/login?service=" + serviceURL;
    }
}
