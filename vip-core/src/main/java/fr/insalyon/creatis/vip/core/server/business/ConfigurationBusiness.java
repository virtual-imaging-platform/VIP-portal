package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.TermsOfUse;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.TermsUseDAO;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

@Service
@Transactional
public class ConfigurationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private ProxyClient proxyClient;
    private EmailBusiness emailBusiness;
    private GRIDAPoolClient gridaPoolClient;
    private GRIDAClient gridaClient;
    private TermsUseDAO termsUseDAO;
    private UserDAO userDAO;
    private UsersGroupsDAO usersGroupsDAO;
    private GroupBusiness groupBusiness;

    @Autowired
    public ConfigurationBusiness(
            Server server, ProxyClient proxyClient, EmailBusiness emailBusiness,
            GRIDAClient gridaClient, GRIDAPoolClient gridaPoolClient,
            TermsUseDAO termsUseDAO, GroupBusiness groupBusiness,
            UserDAO userDAO, UsersGroupsDAO usersGroupsDAO) {
        this.server = server;
        this.proxyClient = proxyClient;
        this.emailBusiness = emailBusiness;
        this.gridaClient = gridaClient;
        this.gridaPoolClient = gridaPoolClient;
        this.termsUseDAO = termsUseDAO;
        this.userDAO = userDAO;
        this.usersGroupsDAO = usersGroupsDAO;
        this.groupBusiness = groupBusiness;
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    public void configure() throws VipException {
        if (server.getMyProxyEnabled()) {
            try {
                logger.debug("Configuring VIP server proxy.");
                proxyClient.checkProxy();
    
            } catch (Exception ex) {
                logger.error("Error configuring myproxy : {}", ex.getMessage());
                throw new VipException(ex);
            }
        } else {
            logger.info("Proxy not needed and not validated !");
        }
    }

    public boolean validateSession(String email, String session)
            throws VipException {
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
            throw new VipException(ex);
        }
    }

    public User getUser(String email) throws VipException {
        try {
            return userDAO.getUser(email);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public User getUserWithGroups(String email) throws VipException {
        try {
            User user = userDAO.getUser(email);
            user.setGroups(usersGroupsDAO.getUserGroups(email));
            return user;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void signup(User user, String comments, Group group) throws VipException {
        signup(user, comments, false, false, group);
    }

    public void signup(User user, String comments, boolean automaticCreation, boolean mapPrivateGroups, Group group)
            throws VipException {
        this.signup(user, comments, automaticCreation, mapPrivateGroups,
                group == null ? new ArrayList<>() : Arrays.asList(group));
    }

    public void signup(User user, String comments, boolean automaticCreation, boolean mapPrivateGroups, List<Group> groups)
            throws VipException {

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
                throw new VipException("Error");
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
            // normalise user folder : replace accents and non ascii characters by _
            String folder = CoreUtil.getCleanStringAlnum(user.getFirstName().toLowerCase() + "_"
                    + user.getLastName().toLowerCase(), "_");

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
                emailBusiness.sendEmailToAdmins("[VIP Admin] Account Requested", adminsEmailContents,
                        true, user.getEmail());
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

                emailBusiness.sendEmailToAdmins("[VIP Admin] Automatic Account Creation", adminsEmailContents,
                        false, user.getEmail());
            }
        } catch (GRIDAClientException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            logger.error("Error signing up user {}", user.getEmail(), ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    private void verifyEmail(String email) throws VipException {
        // verify email format
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            logger.error("The email {} is invalid", email);
            throw new VipException("The email " + email + " is invalid");
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
                throw new VipException("Error");
            }
            // Only check against the domain part of the user's email address
            if (useremail[1].endsWith(udm)) {
                logger.error("Undesired Mail Domain for " + email);
                throw new VipException("Error");
            }
        }
    }

    public User signin(String email, String password) throws VipException {
        return signin(email, password, true);
    }

    public User signinWithoutResetingSession(String email, String password)
            throws VipException {
        return signin(email, password, false);
    }

    private User signin(String email, String password, boolean resetSession)
            throws VipException {

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
                throw new VipException("Authentication failed (email or password incorrect, or user is locked).");
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error signing in user {}", email, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
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

    public void signout(String email) throws VipException {
        try {
            String session = UUID.randomUUID().toString();
            userDAO.updateSession(email, session);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public User activate(String email, String code) throws VipException {
        try {
            if (userDAO.isLocked(email)) {
                logger.error("Activation failed to '" + email + "' (user is locked).");
                throw new VipException("User is locked.");
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
                throw new VipException("Activation failed.");
            }

        } catch (GRIDAClientException ex) {
            logger.error("Error activating {}", email, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void activateUser(String email) throws VipException {
        try {
            User user = userDAO.getUser(email);
            activate(email, user.getCode());
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void sendActivationCode(String email) throws VipException {
        try {
            User user = userDAO.getUser(email);

            if (userDAO.isLocked(email)) {
                logger.error("Cannot send activation code to {} : account locked", email);
                throw new VipException("User is locked.");
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
            throw new VipException(ex);
        }
    }

    public void sendResetCode(String email) throws VipException {
        try {
            User user = userDAO.getUser(email);

            if (userDAO.isLocked(email)) {
                logger.error("Cannot send reset code to {} : account locked", email);
                throw new VipException("User is locked.");
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
            throw new VipException(ex);
        }
    }

    public void requestNewEmail(User user, String newEmail)
            throws VipException {

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
            throw new VipException(ex);
        }
    }

    public void removeUser(String email, boolean sendNotificationEmail)
            throws VipException {

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

                emailBusiness.sendEmailToAdmins("[VIP Admin] Account Removed", adminsEmailContents,
                        true, user.getEmail());
            }
        } catch (GRIDAClientException ex) {
            logger.error("Error removing user {}", email, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<User> getUsers() throws VipException {
        try {
            return userDAO.getUsers();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<String> getAllUserNames() throws VipException {
        return getUsers().stream().map(User::getFullName).collect(Collectors.toCollection(ArrayList::new));
    }

    public Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email)
            throws VipException {
        try {
            return usersGroupsDAO.getUserGroups(email);

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Boolean> getUserPropertiesGroups(String email)
            throws VipException {
        try {
            return usersGroupsDAO.getUserPropertiesGroups(email);

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<String> getUserGroupsName(
            Map<String, CoreConstants.GROUP_ROLE> groups) {
        return new ArrayList<>(groups.keySet());
    }

    public void setUserGroups(
            String email, Map<String, CoreConstants.GROUP_ROLE> groups)
            throws VipException {
        try {
            usersGroupsDAO.setUserGroups(email, groups);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public User getUserData(String email) throws VipException {
        try {
            return userDAO.getUser(email);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public User updateUser(User user) throws VipException {
        try {
            userDAO.update(user);
            return user;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateUserPassword(
            String email, String currentPassword, String newPassword)
            throws VipException {

        try {
            currentPassword = MD5.get(currentPassword);
            newPassword = MD5.get(newPassword);
            userDAO.updatePassword(email, currentPassword, newPassword);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error updating password for {}", email, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateUserEmail(String oldEmail, String newEmail)
            throws VipException {
        verifyEmail(newEmail);
        try {
            userDAO.updateEmail(oldEmail, newEmail);
        } catch (DAOException e) {
            String errorMessage = "Error changing email from " + newEmail + " to " + newEmail;
            sendErrorEmailToAdmins(errorMessage, e, oldEmail);
            throw new VipException("Error changing email address", e);
        }

    }

    public void resetNextEmail(String currentEmail) throws VipException {
        try {
            userDAO.updateNextEmail(currentEmail, null);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void updateTermsOfUse(String email) throws VipException {
        try {
            userDAO.updateTermsOfUse(email, getCurrentTimeStamp());
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateLastUpdatePublication(String email) throws VipException {
        try {
            userDAO.updateLastUpdatePublication(email, getCurrentTimeStamp());
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void sendContactMail(User user, String category, String subject, String comment) throws VipException {
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

        emailBusiness.sendEmailToAdmins("[VIP Contact] " + category, emailContent,
            true, user.getEmail());
    }

    public void updateUserLastLogin(String email) throws VipException {
        try {
            userDAO.updateLastLogin(email, new Date());
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void addUserToGroup(String email, String groupName) throws VipException {
        try {
            usersGroupsDAO.add(email, groupName, GROUP_ROLE.User);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateUser(
            String email, UserLevel level, CountryCode countryCode,
            int maxRunningSimulations, boolean locked)
            throws VipException {
        try {
            userDAO.update(
                    email, level, countryCode, maxRunningSimulations, locked);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<User> getUsersFromGroup(String groupName) throws VipException {
        try {
            return usersGroupsDAO.getUsersFromGroup(groupName);

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void removeUserFromGroup(String email, String groupName)
            throws VipException {
        try {
            usersGroupsDAO.removeUserFromGroup(email, groupName);

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void resetPassword(String email, String code, String password)
            throws VipException {

        try {
            User user = userDAO.getUser(email);

            if (code.equals(user.getCode())) {
                userDAO.resetPassword(email, MD5.get(password));
            } else {
                logger.error("Wrong reset code for {} : {}", email, code);
                throw new VipException("Wrong reset code.");
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error resetting password for {}", email, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
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
            throws VipException {

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
                        groupBusiness.get(groupName));
            } catch (VipException ex2) {
                if (ex2.getMessage().contains("existing")) {
                    //try with a different last name
                    lastName += "_" + System.currentTimeMillis();
                    user = getNewUser(email, firstName, lastName, institution);
                    signup(user, "Generated automatically", true,
                            true, groupBusiness.get(groupName));
                }
            }
            activateUser(user.getEmail());
            try {
                user = getUserWithSession(email);
            } catch (DAOException ex1) {
                throw new VipException(ex1);
            }

        }
        return user;
    }

    public void addTermsUse() throws VipException {
        try {
            TermsOfUse termsOfUse = new TermsOfUse(getCurrentTimeStamp());
            termsUseDAO.add(termsOfUse);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public Timestamp getLastUpdateTermsOfUse() throws VipException {
        try {
            return termsUseDAO.getLastUpdateTermsOfUse();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    // api key management

    public boolean testLastUpdatePublication(String email) throws VipException {
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
            throw new VipException(ex);
        }
    }

    public String getUserApikey(String email) throws VipException {
        try {
            return userDAO.getUserApikey(email);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void deleteUserApikey(String email) throws VipException {
        try {
            userDAO.updateUserApikey(email, null);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public String generateNewUserApikey(String email) throws VipException {
        try {
            SecureRandom random = new SecureRandom();
            String apikey = new BigInteger(130, random).toString(32);
            userDAO.updateUserApikey(email, apikey);
            return apikey;
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    private void sendErrorEmailToAdmins(String errorMessage, Exception exception, String userEmail) {
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

            emailBusiness.sendEmailToAdmins("[VIP Admin] VIP error", emailContent.toString(),
                    true, userEmail);
        } catch (VipException e) {
            logger.error("Cannot sent mail to admin. Ignoring", e);
        }
    }

    public Set<Group> getOrLoadUserGroups(User user) throws VipException {
        if (user.getGroups() == null || user.getGroups().isEmpty()) {
            user.setGroups(getUserGroups(user.getEmail()));
        }
        return user.getGroups();
    }
}
