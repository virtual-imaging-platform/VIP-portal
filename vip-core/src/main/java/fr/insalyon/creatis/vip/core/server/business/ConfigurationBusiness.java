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
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;
import fr.insalyon.creatis.vip.core.server.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rafael Ferreira da Silva, Nouha Boujelben
 */
@Service
@Transactional
public class ConfigurationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private ProxyClient proxyClient;
    private EmailBusiness emailBusiness;
    private GRIDAPoolClient gridaPoolClient;
    private GRIDAClient gridaClient;

    private GroupDAO groupDAO;
    private TermsUseDAO termsUseDAO;
    private UserDAO userDAO;
    private UsersGroupsDAO usersGroupsDAO;

    @Autowired
    public ConfigurationBusiness(
            Server server, ProxyClient proxyClient, EmailBusiness emailBusiness,
            GRIDAClient gridaClient, GRIDAPoolClient gridaPoolClient,
            GroupDAO groupDAO, TermsUseDAO termsUseDAO,
            UserDAO userDAO, UsersGroupsDAO usersGroupsDAO) {
        this.server = server;
        this.proxyClient = proxyClient;
        this.emailBusiness = emailBusiness;
        this.gridaClient = gridaClient;
        this.gridaPoolClient = gridaPoolClient;
        this.groupDAO = groupDAO;
        this.termsUseDAO = termsUseDAO;
        this.userDAO = userDAO;
        this.usersGroupsDAO = usersGroupsDAO;
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    public void configure() throws BusinessException {
        if (server.getMyProxyEnabled()) {
            try {
                logger.debug("Configuring VIP server proxy.");
                proxyClient.checkProxy();
    
            } catch (Exception ex) {
                logger.error("Error configuring myproxy : {}", ex.getMessage());
                throw new BusinessException(ex);
            }
        } else {
            logger.info("Proxy not needed and not validated !");
        }
    }

    public boolean validateSession(String email, String session)
            throws BusinessException {
        try {
            if (email != null && session != null) {
                if (userDAO.verifySession(email, session) && !userDAO.isLocked(email)) {
                    return true;
                }
                logger.info("Failed to verify user [{}]'s session {}", email, session);
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

    public User getUser(String email) throws BusinessException {
        try {
            return userDAO.getUser(email);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public User getUserWithGroups(String email) throws BusinessException {
        try {
            User user = userDAO.getUser(email);
            user.setGroups(usersGroupsDAO.getUserGroups(email));
            return user;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void signup(
            User user, String comments, boolean automaticCreation,
            boolean mapPrivateGroups, Group group)
            throws BusinessException {
        this.signup(user, comments, automaticCreation, mapPrivateGroups,
                group == null ? new ArrayList<>() : Collections.singletonList(group));
    }

    public void signup(
            User user, String comments, boolean automaticCreation,
            boolean mapPrivateGroups, List<Group> groups)
            throws BusinessException {

        verifyEmail(user.getEmail());

        // Build log message
        StringBuilder message = new StringBuilder("Signing up ");
        message.append(". List of undesired countries: ");
        for (String s : server.getUndesiredCountries()) {
            if (!s.trim().isEmpty()) {
                message.append(" ");
                message.append(s);
            }
        }
        message.append(".");
        logger.info(message.toString());

        // Check if country is undesired
        for (String udc : server.getUndesiredCountries()) {
            if (udc.trim().isEmpty()) {
                // An empty config file entry gets here as an empty or
                // whitespace-only string, skip it
                continue;
            }
            if (user.getCountryCode().toString().equals(udc)) {
                logger.error("Undesired country for " + user.getEmail());
                throw new BusinessException("Error");
            }
        }

        try {
            if (!automaticCreation) {
                user.setTermsOfUse(getCurrentTimeStamp());
            }
            user.setLastUpdatePublications(getCurrentTimeStamp());
            user.setCode(UUID.randomUUID().toString());
            if (user.getPassword() == null) {
                user.setPassword(null);
            } else {
                user.setPassword(MD5.get(user.getPassword()));
            }
            String folder = user.getFirstName().replaceAll(" ", "_").toLowerCase() + "_"
                    + user.getLastName().replaceAll(" ", "_").toLowerCase();
            // normalise user folder : remove accents and non ascii characters
            folder = CoreUtil.getCleanString(folder);

            while (gridaClient.exist(server.getDataManagerUsersHome() + "/" + folder)) {
                folder += "_" + new Random().nextInt(10000);
            }

            user.setFolder(folder);
            user.setLevel(UserLevel.Beginner);

            userDAO.add(user);

            // Adding user to groups
            if (groups == null) {
                groups = new ArrayList<>();
            }
            StringBuilder groupsString = new StringBuilder();
            for (Group group : groups) {
                if (mapPrivateGroups || automaticCreation || group.isPublicGroup()) {
                    usersGroupsDAO.add(user.getEmail(), group.getName(), GROUP_ROLE.User);
                } else {
                    logger.info("Don't map user " + user.getEmail() + " to private group " + group.getName());
                }
                groupsString.append(group.getName()).append(", ");
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
                emailBusiness.sendEmail("VIP account details", emailContent,
                        new String[]{user.getEmail()}, true, user.getEmail());

                String adminsEmailContents = "<html>"
                        + "<head></head>"
                        + "<body>"
                        + "<p>Dear Administrator,</p>"
                        + "<p>A new user requested an account:</p>"
                        + "<p><b>First Name:</b> " + user.getFirstName() + "</p>"
                        + "<p><b>Last Name:</b> " + user.getLastName() + "</p>"
                        + "<p><b>Email:</b> " + user.getEmail() + "</p>"
                        + "<p><b>Institution:</b> " + user.getInstitution() + "</p>"
                        + "<p><b>Country:</b> " + user.getCountryCode().getCountryName() + "</p>"
                        + "<p><b>Groups:</b> " + groupsString + "</p>"
                        + "<p><b>Comments:</b><br />" + comments + "</p>"
                        + "<p>&nbsp;</p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                for (String email : getAdministratorsEmails()) {
                    emailBusiness.sendEmail("[VIP Admin] Account Requested", adminsEmailContents,
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
                        + "<p><b>Country:</b> " + user.getCountryCode().getCountryName() + "</p>"
                        + "<p><b>Groups:</b> " + groupNames + "</p>"
                        + "<p><b>Comments:</b><br />" + comments + "</p>"
                        + "<p>&nbsp;</p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                for (String email : getAdministratorsEmails()) {
                    emailBusiness.sendEmail("[VIP Admin] Automatic Account Creation", adminsEmailContents,
                            new String[]{email}, false, user.getEmail());
                }
            }
        } catch (GRIDAClientException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            logger.error("Error signing up user {}", user.getEmail(), ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    private void verifyEmail(String email) throws BusinessException {
        // verify email format
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            logger.error("The email {} is invalid", email);
            throw new BusinessException("The email " + email + " is invalid");
        }

        // Build log message
        StringBuilder message = new StringBuilder("verifying ");
        message.append(email);
        message.append(". List of undesired mail domains: ");
        for (String s : server.getUndesiredMailDomains()) {
            if (!s.trim().isEmpty()) {
                message.append(" ");
                message.append(s);
            }
        }
        message.append(". ");
        logger.info(message.toString());

        // Check if email domain is undesired
        for (String udm : server.getUndesiredMailDomains()) {
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
                logger.error("Undesired Mail Domain for " + email);
                throw new BusinessException("Error");
            }
        }
    }

    public void signup(User user, String comments, Group group) throws BusinessException {
        signup(user, comments, false, false, group);
    }

    public void signup(User user, String comments, List<Group> groups)
            throws BusinessException {
        signup(user, comments, false, false, groups);
    }

    public User signin(String email, String password) throws BusinessException {
        return signin(email, password, true);
    }

    public User signinWithoutResetingSession(String email, String password)
            throws BusinessException {
        return signin(email, password, false);
    }

    private User signin(String email, String password, boolean resetSession)
            throws BusinessException {

        try {
            password = MD5.get(password);

            if (userDAO.authenticate(email, password)) {

                userDAO.resetNFailedAuthentications(email);

                if (resetSession) {
                    return getUserWithSession(email);
                } else {
                    return userDAO.getUser(email);
                }

            } else {
                userDAO.incNFailedAuthentications(email);
                if (userDAO.getNFailedAuthentications(email) > 5) {
                    userDAO.lock(email);
                }
                logger.error("Authentication failed to '" + email + "' (email or password incorrect, or user is locked).");
                throw new BusinessException("Authentication failed (email or password incorrect, or user is locked).");
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error signing in user {}", email, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public User getNewUser(String email, String firstName, String lastName, String institution) {
        CountryCode cc = CountryCode.aq;

        String country = "";

        try {
            country = email.substring(email.lastIndexOf('.') + 1);
        } catch (NullPointerException e) {
            logger.warn("Error finding country from email {}", email, e);
        }

        try {
            if (CountryCode.valueOf(country) != null) {
                cc = CountryCode.valueOf(country);
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Cannot determine country from email extension {}: user will be mapped to Antartica", country, e);
        }

        return new User(
                firstName.trim(),
                lastName.trim(),
                email.trim(),
                institution.trim(),
                "0000",
                cc, getCurrentTimeStamp());
    }

    public void signout(String email) throws BusinessException {
        try {
            String session = UUID.randomUUID().toString();
            userDAO.updateSession(email, session);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public User activate(String email, String code) throws BusinessException {
        try {
            if (userDAO.isLocked(email)) {
                logger.error("Activation failed to '" + email + "' (user is locked).");
                throw new BusinessException("User is locked.");
            }
            if (userDAO.activate(email, code)) {

                User user = userDAO.getUser(email);
                userDAO.resetNFailedAuthentications(email);

                gridaClient.createFolder(server.getDataManagerUsersHome(),
                        user.getFolder());

                gridaClient.createFolder(server.getDataManagerUsersHome(),
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
            logger.error("Error activating {}", email, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void activateUser(String email) throws BusinessException {
        try {
            User user = userDAO.getUser(email);
            activate(email, user.getCode());
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendActivationCode(String email) throws BusinessException {
        try {
            User user = userDAO.getUser(email);

            if (userDAO.isLocked(email)) {
                logger.error("Cannot send activation code to {} : account locked", email);
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

            emailBusiness.sendEmail("VIP activation code (reminder)", emailContent,
                    new String[]{user.getEmail()}, true, user.getEmail());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendResetCode(String email) throws BusinessException {
        try {
            User user = userDAO.getUser(email);

            if (userDAO.isLocked(email)) {
                logger.error("Cannot send reset code to {} : account locked", email);
                throw new BusinessException("User is locked.");
            }

            String code = UUID.randomUUID().toString();
            userDAO.updateCode(email, code);

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

            emailBusiness.sendEmail("Code to reset your VIP password", emailContent,
                    new String[]{user.getEmail()}, true, user.getEmail());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void requestNewEmail(User user, String newEmail)
            throws BusinessException {

        try {
            String code = UUID.randomUUID().toString();
            userDAO.updateCode(user.getEmail(), code);
            userDAO
                    .updateNextEmail(user.getEmail(), newEmail);

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear " + user.getFullName() + ",</p>"
                    + "<p>You requested to link your VIP account to this email address.</p>"
                    + "<p>Please use the following code to activate it in your VIP account page:</p>"
                    + "<p><b>" + code + "</b></p>"
                    + "<p>You will have to refresh your VIP web page if you have not done it since you requested the change.</p>"
                    + "<p>Please note that your login email is still "
                    + user.getEmail() + " until you validate it.</p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            emailBusiness.sendEmail("Code to confirm your VIP email address", emailContent,
                    new String[]{newEmail}, true, newEmail);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeUser(String email, boolean sendNotificationEmail)
            throws BusinessException {

        try {
            User user = getUser(email);

            gridaPoolClient.removeOperationsByUser(email);

            gridaPoolClient.delete(server.getDataManagerUsersHome() + "/"
                    + user.getFolder(), user.getEmail());
            gridaPoolClient.delete(server.getDataManagerUsersHome() + "/"
                    + user.getFolder() + "_" + CoreConstants.FOLDER_TRASH, user.getEmail());

            userDAO.remove(email);

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
                        + "<p>&nbsp;</p>"
                        + "<p>Best Regards,</p>"
                        + "<p>VIP Team</p>"
                        + "</body>"
                        + "</html>";

                for (String adminEmail : getAdministratorsEmails()) {
                    emailBusiness.sendEmail("[VIP Admin] Account Removed", adminsEmailContents,
                            new String[]{adminEmail}, true, user.getEmail());
                }
            }
        } catch (GRIDAClientException ex) {
            logger.error("Error removing user {}", email, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<User> getUsers() throws BusinessException {
        try {
            return userDAO.getUsers();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getUserNames(String email, boolean validGroup)
            throws BusinessException {
        try {
            if (validGroup) {
                // Discarded the effect of validGroups as this has several side effects (see #2669)
                //List<String> groups = CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserAdminGroups(email);
                // if (groups.isEmpty()) {
                List<String> userNames = new ArrayList<>();
                userNames.add(userDAO.getUser(email).getFullName());
                return userNames;

//                } else {
//                    return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroups(groups);
//                }
            } else {
                List<String> userNames = new ArrayList<>();
                for (User user : getUsers()) {
                    userNames.add(user.getFullName());
                }

                return userNames;
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void addGroup(Group group) throws BusinessException {
        try {
            gridaClient.createFolder(server.getDataManagerGroupsHome(),
                    group.getName().replaceAll(" ", "_"));

            groupDAO.add(group);
        } catch (GRIDAClientException ex) {
            logger.error("Error adding group : {}", group.getName(), ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeGroup(String user, String groupName)
            throws BusinessException {
        try {
            gridaPoolClient.delete(server.getDataManagerGroupsHome() + "/"
                    + groupName.replaceAll(" ", "_"), user);
            groupDAO.remove(groupName);
        } catch (GRIDAClientException ex) {
            logger.error("Error removing group : {}", groupName, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateGroup(String name, Group group) throws BusinessException {
        try {
            if (!name.equals(group.getName())) {
                gridaClient.rename(
                        server.getDataManagerGroupsHome() + "/" + name.replaceAll(" ", "_"),
                        server.getDataManagerGroupsHome() + "/" + group.getName().replaceAll(" ", "_"));
            }
            groupDAO.update(name, group);
        } catch (GRIDAClientException ex) {
            logger.error("Error updating group : {}", name, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Group> getGroups() throws BusinessException {
        try {
            return groupDAO.getGroups();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Group getGroup(String groupName) throws BusinessException {
        if (groupName == null) {
            return null;
        }
        return this.getGroups().stream()
                .filter(g -> groupName.equals(g.getName()))
                .findAny().orElse(null);
    }

    public List<Group> getPublicGroups() throws BusinessException {
        try {
            List<Group> publicGroups = new ArrayList<>();
            for (Group g : groupDAO.getGroups()) {
                if (g.isPublicGroup()) {
                    publicGroups.add(g);
                }
            }
            return publicGroups;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email)
            throws BusinessException {
        try {
            return usersGroupsDAO.getUserGroups(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Boolean> getUserPropertiesGroups(String email)
            throws BusinessException {
        try {
            return usersGroupsDAO.getUserPropertiesGroups(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getUserGroupsName(
            Map<String, CoreConstants.GROUP_ROLE> groups) {
        return new ArrayList<>(groups.keySet());
    }

    public void setUserGroups(
            String email, Map<String, CoreConstants.GROUP_ROLE> groups)
            throws BusinessException {
        try {
            usersGroupsDAO.setUserGroups(email, groups);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public User getUserData(String email) throws BusinessException {
        try {
            return userDAO.getUser(email);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public User updateUser(User user) throws BusinessException {
        try {
            userDAO.update(user);
            return user;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateUserPassword(
            String email, String currentPassword, String newPassword)
            throws BusinessException {

        try {
            currentPassword = MD5.get(currentPassword);
            newPassword = MD5.get(newPassword);
            userDAO.updatePassword(email, currentPassword, newPassword);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error updating password for {}", email, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateUserEmail(String oldEmail, String newEmail)
            throws BusinessException {
        verifyEmail(newEmail);
        try {
            userDAO.updateEmail(oldEmail, newEmail);
        } catch (DAOException e) {
            String errorMessage = "Error changing email from " + newEmail + " to " + newEmail;
            sendErrorEmailToAdmins(errorMessage, e, oldEmail);
            throw new BusinessException("Error changing email address", e);
        }

    }

    public void resetNextEmail(String currentEmail) throws BusinessException {
        try {
            userDAO.updateNextEmail(currentEmail, null);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void updateTermsOfUse(String email) throws BusinessException {
        try {
            userDAO.updateTermsOfUse(email, getCurrentTimeStamp());
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateLastUpdatePublication(String email) throws BusinessException {
        try {
            userDAO.updateLastUpdatePublication(email, getCurrentTimeStamp());
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendContactMail(
            User user, String category, String subject, String comment)
            throws BusinessException {
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

            for (User u : usersGroupsDAO.getUsersFromGroup(CoreConstants.GROUP_SUPPORT)) {
                emailBusiness.sendEmail("[VIP Contact] " + category, emailContent,
                        new String[]{u.getEmail()}, true, user.getEmail());
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateUserLastLogin(String email) throws BusinessException {
        try {
            userDAO.updateLastLogin(email, new Date());
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void addUserToGroup(String email, String groupName) throws BusinessException {
        try {
            usersGroupsDAO.add(email, groupName, GROUP_ROLE.User);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateUser(
            String email, UserLevel level, CountryCode countryCode,
            int maxRunningSimulations, boolean locked)
            throws BusinessException {
        try {
            userDAO.update(
                    email, level, countryCode, maxRunningSimulations, locked);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<User> getUsersFromGroup(String groupName) throws BusinessException {
        try {
            return usersGroupsDAO.getUsersFromGroup(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeUserFromGroup(String email, String groupName)
            throws BusinessException {
        try {
            usersGroupsDAO.removeUserFromGroup(email, groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void resetPassword(String email, String code, String password)
            throws BusinessException {

        try {
            User user = userDAO.getUser(email);

            if (code.equals(user.getCode())) {
                userDAO.resetPassword(email, MD5.get(password));
            } else {
                logger.error("Wrong reset code for {} : {}", email, code);
                throw new BusinessException("Wrong reset code.");
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error resetting password for {}", email, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getSupportEmails() throws BusinessException {
        try {
            return usersGroupsDAO.getUsersFromGroup(CoreConstants.GROUP_SUPPORT)
                    .stream().map(User::getEmail)
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Gets an array of administrator's e-mails
     */
    private String[] getAdministratorsEmails() throws DAOException {
        List<String> emails = new ArrayList<>();
        for (User admin : userDAO.getAdministrators()) {
            emails.add(admin.getEmail());
        }
        return emails.toArray(new String[]{});
    }

    public User getUserWithSession(String email) throws DAOException {

        String session = UUID.randomUUID().toString();
        userDAO.updateSession(email, session);

        return userDAO.getUser(email);
    }

    public String getLoginUrlCas(URL serviceURL) {
        return server.getCasURL() + "/login?service=" + serviceURL;
    }


    public User getOrCreateUser(String email, String institution, String groupName)
            throws BusinessException {

        verifyEmail(email);

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

            user = getNewUser(email, firstName, lastName, institution);
            try {
                signup(user, "Generated automatically", true, true,
                        getGroup(groupName));
            } catch (BusinessException ex2) {
                if (ex2.getMessage().contains("existing")) {
                    //try with a different last name
                    lastName += "_" + System.currentTimeMillis();
                    user = getNewUser(email, firstName, lastName, institution);
                    signup(user, "Generated automatically", true,
                            true, getGroup(groupName));
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

    public void addTermsUse() throws BusinessException {
        try {
            TermsOfUse termsOfUse = new TermsOfUse(getCurrentTimeStamp());
            termsUseDAO.add(termsOfUse);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Timestamp getLastUpdateTermsOfUse() throws BusinessException {
        try {
            return termsUseDAO.getLastUpdateTermsOfUse();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    // api key management

    public boolean testLastUpdatePublication(String email) throws BusinessException {
        try {
            if (userDAO.getLastPublicationUpdate(email) == null) {
                return true;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(userDAO.getLastPublicationUpdate(email));
                cal.add(Calendar.MONTH, server.getNumberMonthsToTestLastPublicationUpdates());
                Timestamp ts = new Timestamp(cal.getTime().getTime());
                return ts.before(getCurrentTimeStamp());
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String getUserApikey(String email) throws BusinessException {
        try {
            return userDAO.getUserApikey(email);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void deleteUserApikey(String email) throws BusinessException {
        try {
            userDAO.updateUserApikey(email, null);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public String generateNewUserApikey(String email) throws BusinessException {
        try {
            SecureRandom random = new SecureRandom();
            String apikey = new BigInteger(130, random).toString(32);
            userDAO.updateUserApikey(email, apikey);
            return apikey;
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    private void sendErrorEmailToAdmins(
            String errorMessage, Exception exception, String userEmail) {
        try {
            StringBuilder emailContent = new StringBuilder("<html><head></head><body>");
            emailContent.append("<p>Dear Administrator,</p>");

            emailContent.append("<p>An error has been encountered in VIP with the following user:");
            emailContent.append("<b>" + userEmail + "</b></p>");

            emailContent.append("<p><b>" + errorMessage + "</b></p>");

            if (exception != null) {
                emailContent.append("The exception was:");
                emailContent.append(exception);
            }

            emailContent.append("<p>Please check the logs for more information</p>");
            emailContent.append("<p>&nbsp;</p>");
            emailContent.append("<p>Best Regards,</p><p>VIP Team</p>");
            emailContent.append("</body></html>");

            for (String email : getAdministratorsEmails()) {
                emailBusiness.sendEmail("[VIP Admin] VIP error", emailContent.toString(),
                        new String[]{email}, true, userEmail);
            }
        } catch (BusinessException e) {
            logger.error("Cannot sent mail to admin. Ignoring", e);
        } catch (DAOException e) {
            logger.error("Cannot sent mail to admin : {}. Ignoring", e.getMessage());
        }
    }
}
