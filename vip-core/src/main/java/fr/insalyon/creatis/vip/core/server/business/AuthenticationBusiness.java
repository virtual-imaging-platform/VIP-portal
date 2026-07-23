package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;

@Service
public class AuthenticationBusiness extends CommonBusiness {

    private final UserDAO userDAO;
    private final EmailBusiness emailBusiness;
    private final Server server;
    private final GRIDAClient gridaClient;
    private final UsersGroupsDAO usersGroupsDAO;
    private final UserBusiness userBusiness;
    private final GroupBusiness groupBusiness;
    private final EmailTemplateUtils emailTemplateUtils;

    @Autowired
    public AuthenticationBusiness(UserDAO userDAO, EmailBusiness emailBusiness, Server server, GRIDAClient gridaClient, UsersGroupsDAO usersGroupsDAO, UserBusiness userBusiness, GroupBusiness groupBusiness, EmailTemplateUtils emailTemplateUtils) {
        this.userDAO = userDAO;
        this.emailBusiness = emailBusiness;
        this.server = server;
        this.gridaClient = gridaClient;
        this.usersGroupsDAO = usersGroupsDAO;
        this.userBusiness = userBusiness;
        this.groupBusiness = groupBusiness;
        this.emailTemplateUtils = emailTemplateUtils;
    }

    @VIPExternalSafe
    public User signup(User user, String comments) throws VipException {
        return signup(user, comments, false);
    }

    @VIPExternalSafe
    public User signup(User user, String comments, boolean automaticCreation)
            throws VipException {
        logger.info("Starting signup flow for email='{}' (automaticCreation={})",
                user != null ? user.getEmail() : null, automaticCreation);

        // should be unauthentified or admin (related to internal methods with asAdminContext)
        if (getUser() != null && ! getUserLevel().equals(UserLevel.Administrator)) { 
            throw new VipException(DefaultError.UNAUTHENTICATED_ONLY);
        }
        userBusiness.assertPublicNonAutoGroups(user.getGroups());
        emailBusiness.verifyEmail(user.getEmail());
        verifyCountryCode(user);
        verifyUserFields(user);

        try {
            Timestamp ts = new Timestamp(System.currentTimeMillis());

            if (!automaticCreation) {
                user.setTermsOfUse(ts);
            }
            user.setConfirmed(automaticCreation);
            user.setAccountLocked(false);
            user.setLastLogin(ts);
            user.setRegistration(ts);
            user.setLastUpdatePublications(ts);
            user.setCode(UUID.randomUUID().toString());
            user.setFailedAuthentications(0);

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
            user.setMaxRunningSimulations(1);
            user.setId(CoreUtil.createUUID());
            userDAO.add(user);
            userDAO.definePassword(user.getEmail(), user.getPassword());

            // Adding user to groups
            for (Group group : user.getGroups()) {
                usersGroupsDAO.add(user.getEmail(), group.getName(), GROUP_ROLE.User);
            }
            String groupsString = user.getGroups().stream().map(Group::getName).collect(Collectors.joining(","));

            logger.info("Signup persistence succeeded for email='{}' with generatedId='{}'",
                user.getEmail(), user.getId());

            if (!automaticCreation) {
                String emailContent = emailTemplateUtils.registrationUserEmail(user);
                logger.info("Sending confirmation email to '{}'.", user.getEmail());
                emailBusiness.sendEmail("VIP account details", emailContent,
                        new String[] { user.getEmail() }, true, user.getEmail());

                String adminsEmailContents = emailTemplateUtils.registrationAdminEmail(user, groupsString, comments);
                emailBusiness.sendEmailToAdmins("[VIP Admin] Account Requested", adminsEmailContents,
                        true, user.getEmail());
            } else {
                String adminsEmailContents = emailTemplateUtils.registrationAdminEmailAutomatic(user, groupsString, comments);

                emailBusiness.sendEmailToAdmins("[VIP Admin] Automatic Account Creation", adminsEmailContents,
                        false, user.getEmail());
            }

            logger.info("Signup flow completed for email='{}'", user.getEmail());
            return user;
        } catch (GRIDAClientException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            logger.error("Error signing up user {}", user.getEmail(), ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    private void verifyUserFields(User user) throws VipException {
        // most of the fields must be absent at creation
        verifyUserField(user::getId, "id");
        verifyUserField(user::getRegistration, "registration");
        verifyUserField(user::getLevel, "level");
        verifyUserField(user::getLastLogin, "lastLogin");
        verifyUserField(user::getMaxRunningSimulations, "maxRunningSimulations");
        verifyUserField(user::getTermsOfUse, "termsOfUse");
        verifyUserField(user::getLastUpdatePublications, "lastUpdatePublication");
        verifyUserField(user::getNextEmail, "nextEmail");
        verifyUserField(user::getCode, "code");
        verifyUserField(user::getFolder, "folder");
        verifyUserField(user::getSession, "session");
        verifyUserField(user::getFailedAuthentications, "failedAuthentication");
        verifyUserField(user::isConfirmed, "confirmed");
        verifyUserField(user::isAccountLocked, "locked");
        verifyUserField(user::getApiKey, "apikey");
    }

    private void verifyUserField(Supplier<Object> f, String field) throws VipException {
        if (f.get() != null) {
            logger.error("{} must be absent in User on creation", field);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, field, "Must be absent");
        }
    }

    private void verifyCountryCode(User user) throws VipException {
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
                logger.error("Undesired country for {}", user.getEmail());
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
                    return userBusiness.getUserWithSession(email);
                } else {
                    return userDAO.get(email);
                }

            } else {
                userDAO.incNFailedAuthentications(email);
                if (userDAO.getNFailedAuthentications(email) > 5) {
                    userDAO.lock(email);
                }
                logger.error(
                        "Authentication failed to '" + email + "' (email or password incorrect, or user is locked).");
                throw new VipException(DefaultError.BAD_CREDENTIALS);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error signing in user {}", email, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
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

                userDAO.resetNFailedAuthentications(email);
                User user = userBusiness.getUserWithSession(email);

                gridaClient.createFolder(server.getDataManagerUsersHome(),
                        user.getFolder());

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


    public User getOrCreateUser(String email, String institution)
            throws VipException {

        emailBusiness.verifyEmail(email);

        User user;
        try {
            user = userBusiness.getUserWithSession(email);
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

            user = userBusiness.getNewUser(email, firstName, lastName, institution);
            try {
                signup(user, "Generated automatically", true);
            } catch (VipException ex2) {
                if (ex2.getMessage().contains("existing")) {
                    //try with a different last name
                    lastName += "_" + System.currentTimeMillis();
                    user = userBusiness.getNewUser(email, firstName, lastName, institution);
                    signup(user, "Generated automatically", true);
                }
            }
            activateUser(user.getEmail());
            try {
                user = userBusiness.getUserWithSession(email);
            } catch (DAOException ex1) {
                throw new VipException(ex1);
            }
        }
        return user;
    }

    public void activateUser(String email) throws VipException {
        try {
            User user = userDAO.get(email);
            activate(email, user.getCode());
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }
}
