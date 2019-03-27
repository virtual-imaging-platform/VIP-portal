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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.grida.client.*;
import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;
import fr.insalyon.creatis.vip.core.server.dao.*;
import org.apache.log4j.Logger;
import org.jasig.cas.client.validation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.*;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;

/**
 *
 * @author Rafael Ferreira da Silva, Nouha Boujelben
 */
public class ConfigurationBusiness {

    private final static Logger logger = Logger.getLogger(ConfigurationBusiness.class);

    /**
     *
     * @throws BusinessException
     */
    public void configure() throws BusinessException {

        try {
            logger.debug("Configuring VIP server proxy.");
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
                if (userDAO.verifySession(email, session) && !userDAO.isLocked(email)) {
                    return true;
                }
                userDAO.incNFailedAuthentications(email); //just in case...
                if (userDAO.getNFailedAuthentications(email) > 5) {
                    userDAO.lock(email);
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

    public void signup(User user, String comments, boolean automaticCreation, boolean mapPrivateGroups, String... accountType) throws BusinessException {

        verifyEmail(user.getEmail());

        // Build log message
        StringBuilder message = new StringBuilder("Signing up ");
        message.append(user.getEmail());
        message.append(". List of undesired countries: ");
        for (String s : Server.getInstance().getUndesiredCountries()) {
            if (!s.trim().isEmpty()) {
                message.append(" ");
                message.append(s);
            }
        }
        message.append(".");
        logger.info(message.toString());
        
        // Check if country is undesired
        for (String udc : Server.getInstance().getUndesiredCountries()) {
            if (udc.trim().isEmpty()) {
                // An empty config file entry gets here as an empty or
                // whitespace-only string, skip it
                continue;
            }
            if (user.getCountryCode().toString().equals(udc)) {
                logger.info("Undesired country for " + user.getEmail());
                throw new BusinessException("Error");
            }
        }

        try {
            if (!automaticCreation) {
                user.setTermsOfUse(getCurrentTimeStamp());
            }
            user.setLastUpdatePublications(getCurrentTimeStamp());
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
            List<Group> groups = null;
            if (accountType != null) {
                groups = CoreDAOFactory.getDAOFactory().getAccountDAO().getGroups(accountType);
                for (Group group : groups) {
                    if (mapPrivateGroups || automaticCreation || group.isPublicGroup()) {
                        CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().add(user.getEmail(), group.getName(), GROUP_ROLE.User);
                    } else {
                        logger.info("Don't map user " + user.getEmail() + " to private group " + group.getName());
                    }
                }
            } else {
                groups = new ArrayList<Group>();
            }

            if (!automaticCreation) {
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
                StringBuilder groupNames = new StringBuilder();
                for (Group group : groups) {
                    if (groupNames.length() > 0) {
                        groupNames.append(", ");
                    }
                    groupNames.append(group.getName());
                }
                String adminsEmailContents = "<html>"
                                             + "<head></head>"
                                             + "<body>"
                                             + "<p>Dear Administrators,</p>"
                                             + "<p>The following account was automatically created:</p>"
                                             + "<p><b>First Name:</b> " + user.getFirstName() + "</p>"
                                             + "<p><b>Last Name:</b> " + user.getLastName() + "</p>"
                                             + "<p><b>Email:</b> " + user.getEmail() + "</p>"
                                             + "<p><b>Institution:</b> " + user.getInstitution() + "</p>"
                                             + "<p><b>Phone:</b> " + user.getPhone() + "</p>"
                                             + "<p><b>Country:</b> " + user.getCountryCode().getCountryName() + "</p>"
                                             + "<p><b>Groups:</b> " + groupNames + "</p>"
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

    private void verifyEmail(String email) throws BusinessException {
        // Build log message
        StringBuilder message = new StringBuilder("verifying ");
        message.append(email);
        message.append(". List of undesired mail domains: ");
        for (String s : Server.getInstance().getUndesiredMailDomains()) {
            if (!s.trim().isEmpty()) {
                message.append(" ");
                message.append(s);
            }
        }
        message.append(". ");
        logger.info(message.toString());

        // Check if email domain is undesired
        for (String udm : Server.getInstance().getUndesiredMailDomains()) {
            if (udm.trim().isEmpty()) {
                // An empty config file entry gets here as an empty or
                // whitespace-only string, skip it
                continue;
            }
            String[] useremail = email.split("@");
            if (useremail.length != 2) {
                logger.info("User Mail address is incorrect : " + email);
                throw new BusinessException("Error");
            }
            // Only check against the domain part of the user's email address
            if (useremail[1].endsWith(udm)) {
                logger.info("Undesired Mail Domain for " + email);
                throw new BusinessException("Error");
            }
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
        return signin(email, password, true);
    }

    public User signinWithoutResetingSession(String email, String password) throws BusinessException {
        return signin(email, password, false);
    }

    private User signin(String email, String password, boolean resetSession) throws BusinessException {

        try {
            password = MD5.get(password);
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();

            if (userDAO.authenticate(email, password)) {

                userDAO.resetNFailedAuthentications(email);

                if (resetSession) {
                    return getUserWithSession(email);
                } else {
                    return CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);
                }

            } else {
                userDAO.incNFailedAuthentications(email);
                if (userDAO.getNFailedAuthentications(email) > 5) {
                    userDAO.lock(email);
                }
                logger.error("Authentication failed to '" + email + "' (email or password incorrect, or user is locked).");
                throw new BusinessException("Authentication failed (email or password incorrect, or user is locked).");
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

    public User getNewUser(String email, String firstName, String lastName) {
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
                firstName.trim(),
                lastName.trim(),
                email.trim(),
                "Unknown",
                UUID.randomUUID().toString(),
                "0000",
                cc, getCurrentTimeStamp());

        return u;
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

            return getNewUser(email, first, last);
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
            if (userDAO.isLocked(email)) {
                logger.error("Activation failed to '" + email + "' (user is locked).");
                throw new BusinessException("User is locked.");
            }
            if (userDAO.activate(email, code)) {

                User user = userDAO.getUser(email);
                userDAO.resetNFailedAuthentications(email);

                GRIDAClient client = CoreUtil.getGRIDAClient();
                client.createFolder(Server.getInstance().getDataManagerUsersHome(),
                                    user.getFolder());

                client.createFolder(Server.getInstance().getDataManagerUsersHome(),
                                    user.getFolder() + "_" + CoreConstants.FOLDER_TRASH);

                return user;

            } else {
                userDAO.incNFailedAuthentications(email);
                if (userDAO.getNFailedAuthentications(email) > 5) {
                    userDAO.lock(email);
                }
                logger.error("Activation failed to '" + email + "' (wrong code: " + code + ").");
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

            if (CoreDAOFactory.getDAOFactory().getUserDAO().isLocked(email)) {
                throw new BusinessException("User is locked.");
            }

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

            if (CoreDAOFactory.getDAOFactory().getUserDAO().isLocked(email)) {
                throw new BusinessException("User is locked.");
            }

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
     * @param email
     * @param validGroup
     * @return @throws BusinessException
     */
    public List<String> getUserNames(String email, boolean validGroup)
            throws BusinessException {

        try {
            if (validGroup) {

                // Discarded the effect of validGroups as this has several side effects (see #2669)
                //List<String> groups = CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserAdminGroups(email);
                // if (groups.isEmpty()) {
                List<String> userNames = new ArrayList<String>();
                userNames.add(CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email).getFullName());
                return userNames;

//                } else {
//                    return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroups(groups);
//                }
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

    public List<Group> getPublicGroups() throws BusinessException {
        try {
            List<Group> publicGroups = new ArrayList<Group>();
            for (Group g : CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                if (g.isPublicGroup()) {
                    publicGroups.add(g);
                }
            }
            return publicGroups;

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

    public List<Boolean> getUserPropertiesGroups(String email) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserPropertiesGroups(email);

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

    public void updateUserEmail(String oldEmail, String newEmail) throws BusinessException {
        verifyEmail(newEmail);
        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().updateEmail(oldEmail, newEmail);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void updateTermsOfUse(String email) throws BusinessException {
        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().updateTermsOfUse(email, getCurrentTimeStamp());

        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void updateLastUpdatePublication(String email) throws BusinessException {
        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().updateLastUpdatePublication(email, getCurrentTimeStamp());

        } catch (DAOException ex) {
            logger.error(ex);
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
     * @param locked
     * @throws BusinessException
     */
    public void updateUser(String email, UserLevel level, CountryCode countryCode,
                           int maxRunningSimulations, boolean locked) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().update(email, level,
                                                               countryCode, maxRunningSimulations, locked);

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

    public User getUserWithSession(String email) throws DAOException {
        UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();

        String session = UUID.randomUUID().toString();
        userDAO.updateSession(email, session);

        return userDAO.getUser(email);
    }

    public String getLoginUrlCas(URL serviceURL) {
        return Server.getInstance().getCasURL() + "/login?service=" + serviceURL;
    }

    public User getOrCreateUser(String email, String defaultAccountType) throws BusinessException {
        User user;
        try {
            user = getUserWithSession(email);
        } catch (DAOException ex) {
            //User doesn't exist: let's create an account 
            String name = email.substring(0, email.indexOf('@'));
            String firstName = name, lastName = name;

            String[] delimiters = {"\\.", "-", "_"};
            for (String delimiter : delimiters) {
                if (name.contains(".") && name.split(delimiter).length >= 2) {
                    firstName = name.split(delimiter)[0];
                    lastName = name.split(delimiter)[1];
                    break;
                }
            }

            user = getNewUser(email, firstName, lastName);
            try {
                signup(user, "Generated automatically", true, true, defaultAccountType);
            } catch (BusinessException ex2) {
                if (ex2.getMessage().contains("existing")) {
                    //try with a different last name
                    lastName += "_" + System.currentTimeMillis();
                    user = getNewUser(email, firstName, lastName);
                    signup(user, "Generated automatically", true, true, defaultAccountType);
                }
            }
            activateUser(user.getEmail());
            try {
                user = getUserWithSession(email);
            } catch (DAOException ex1) {
                throw new BusinessException(ex1);
            }

        }
        return user;
    }

    //Publications
    /**
     *
     * @return @throws BusinessException
     */
    public List<Publication> getPublications() throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getPublicationDAO().getList();
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void removePublication(Long id) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getPublicationDAO().remove(id);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void addPublication(Publication pub) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getPublicationDAO().add(pub);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void updatePublication(Publication pub) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getPublicationDAO().update(pub);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void updatePublicationOwner(String oldOwnerEmail, String newOwnerEmail) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getPublicationDAO().updateOwnerEmail(oldOwnerEmail, newOwnerEmail);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public Publication getPublication(Long id) throws BusinessException {

        try {

            return CoreDAOFactory.getDAOFactory().getPublicationDAO().getPublication(id);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void addTermsUse(TermsOfUse termsOfUse) throws BusinessException {

        try {

            termsOfUse.setDate(getCurrentTimeStamp());
            CoreDAOFactory.getDAOFactory().getTermsUseDAO().add(termsOfUse);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public Timestamp getLastUpdateTermsOfUse() throws BusinessException {

        try {

            return CoreDAOFactory.getDAOFactory().getTermsUseDAO().getLastUpdateTermsOfUse();
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public boolean testLastUpdatePublication(String email) throws BusinessException {

        try {
            if (CoreDAOFactory.getDAOFactory().getUserDAO().getLastPublicationUpdate(email) == null) {
                return true;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(CoreDAOFactory.getDAOFactory().getUserDAO().getLastPublicationUpdate(email));
                cal.add(Calendar.MONTH, Server.getInstance().getNumberMonthsToTestLastPublicationUpdates());
                Timestamp ts = new Timestamp(cal.getTime().getTime());
                return ts.before(getCurrentTimeStamp());
            }
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    // api key management

    public String getUserApikey(String email) throws BusinessException {
        try {
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            return userDAO.getUserApikey(email);
        } catch (DAOException e) {
            logger.error("Error getting apikey for " + email);
            throw new BusinessException(e);
        }
    }

    public void deleteUserApikey(String email) throws BusinessException {
        try {
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            userDAO.updateUserApikey(email, null);
        } catch (DAOException e) {
            logger.error("Error deleting apikey for " + email);
            throw new BusinessException(e);
        }
    }

    public String generateNewUserApikey(String email) throws BusinessException {
        try {
            SecureRandom random = new SecureRandom();
            String apikey = new BigInteger(130, random).toString(32);
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            userDAO.updateUserApikey(email, apikey);
            return apikey;
        } catch (DAOException e) {
            logger.error("Error generating apikey for " + email);
            throw new BusinessException(e);
        }
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }
}
