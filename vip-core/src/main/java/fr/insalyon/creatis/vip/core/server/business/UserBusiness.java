package fr.insalyon.creatis.vip.core.server.business;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import com.google.gwt.thirdparty.guava.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

@Service
@Transactional
public class UserBusiness extends CommonBusiness {

    private final UserDAO userDAO;
    private final UsersGroupsDAO usersGroupsDAO;
    private final EmailBusiness emailBusiness;
    private final GRIDAPoolClient gridaPoolClient;
    private final Server server;
    private final EmailTemplateUtils emailTemplateUtils;
    private final PasswordBusiness passwordBusiness;
    private final GroupBusiness groupBusiness;

    @Autowired
    public UserBusiness(UserDAO userDAO, UsersGroupsDAO usersGroupsDAO, EmailBusiness emailBusiness,
                        GRIDAPoolClient gridaPoolClient, Server server, EmailTemplateUtils emailTemplateUtils,
                        PasswordBusiness passwordBusiness, GroupBusiness groupBusiness) {
        this.userDAO = userDAO;
        this.usersGroupsDAO = usersGroupsDAO;
        this.emailBusiness = emailBusiness;
        this.gridaPoolClient = gridaPoolClient;
        this.server = server;
        this.emailTemplateUtils = emailTemplateUtils;
        this.passwordBusiness = passwordBusiness;
        this.groupBusiness = groupBusiness;
    }

    public void updateUserEmail(String oldEmail, String newEmail)
            throws VipException {
        emailBusiness.verifyEmail(newEmail);
        try {
            userDAO.updateEmail(oldEmail, newEmail);
        } catch (DAOException e) {
            String errorMessage = "Error changing email from " + newEmail + " to " + newEmail;
            emailBusiness.sendErrorEmailToAdmins(errorMessage, e, oldEmail);
            throw new VipException("Error changing email address", e);
        }

    }

    public User getUserData(String email) throws VipException {
        try {
            return userDAO.get(email);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public User update(User user) throws VipException {
        User existingUser = getUserWithGroupsById(user.getId());
        Set<Group> groupsToJoin = new HashSet<>(user.getGroups());
        Set<Group> groupsToLeave = new HashSet<>(existingUser.getGroups());

        groupsToJoin.removeAll(existingUser.getGroups());
        groupsToLeave.removeAll(user.getGroups());

        if ( ! getUserLevel().equals(UserLevel.Administrator)) {
            if ( ! user.getId().equals(getUser().getId())) {
                // only admin can edit others accounts
                throw new VipException(DefaultError.ACCESS_DENIED);
            }

            // here user try to join private group (forbidden)
            // but it's okay if the user try to leave a private group
            assertPublicNonAutoGroups(groupsToJoin);
        } else {
            // verify groups exist
            assertGroupsExist(groupsToJoin);
        }

        // most fields can be null, then they will be filled with the existing values
        // That is especially the case with JsonViews, but not only
        // also most fields are not editable, some only by admins
        loadAndVerifyFields(user, existingUser);
        try {
            userDAO.update(user);

            for (Group group : groupsToLeave) {
                usersGroupsDAO.removeUserFromGroup(user.getEmail(), group.getName());
            }
            for (Group group : groupsToJoin) {
                usersGroupsDAO.add(user.getEmail(), group.getName(), GROUP_ROLE.User);
            }
            return user;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    private void loadAndVerifyFields(User user, User existingUser) throws VipException {
        // These fields can never be changed. Some can be present if the same, other must never be present
        // id, registration time, last login time, terms of use time, last publication update time, api key
        // next email, code, session, failed auth
        assertFieldUnchanged(User::getId, user, existingUser, "id");
        user.setId(existingUser.getId());
        assertFieldUnchanged(User::getRegistration, user, existingUser, "registration");
        user.setRegistration(existingUser.getRegistration());
        assertFieldUnchanged(User::getLastLogin, user, existingUser, "lastLogin");
        user.setLastLogin(existingUser.getLastLogin());
        assertFieldUnchanged(User::getTermsOfUse, user, existingUser, "termsOfUse");
        user.setTermsOfUse(existingUser.getTermsOfUse());
        assertFieldUnchanged(User::getLastUpdatePublications, user, existingUser, "lastUpdatePublication");
        user.setLastUpdatePublications(existingUser.getLastUpdatePublications());
        assertFieldUnchanged(User::getApiKey, user, existingUser, "apikey");
        user.setApiKey(existingUser.getApiKey());
        assertFieldAbsent(User::getNextEmail, user, "nextEmail");
        user.setNextEmail(existingUser.getNextEmail());
        assertFieldAbsent(User::getCode, user, "code");
        user.setCode(existingUser.getCode());
        assertFieldAbsent(User::getSession, user, "session");
        user.setSession(existingUser.getSession());
        assertFieldAbsent(User::getFailedAuthentications, user, "failedAuthentication");
        user.setFailedAuthentications(existingUser.getFailedAuthentications());

        // These fields are always editable, fill them if they are null
        // institution, country code
        if (user.getInstitution() == null) user.setInstitution(existingUser.getInstitution());
        if (user.getCountryCode() == null) user.setCountryCode(existingUser.getCountryCode());


        // These fields can only be modified by admin
        // folder, accountLocked, first name, last name, email, confirmed, level, max running simulation
        if ( ! getCurrentUser().isSystemAdministrator()) {
            assertFieldAbsent(User::getFolder, user, "folder");
            user.setFolder(existingUser.getFolder());
            assertFieldAbsent(User::isAccountLocked, user, "locked");
            user.setAccountLocked(existingUser.isAccountLocked());
            assertFieldUnchanged(User::getFirstName, user, existingUser, "firstName");
            user.setFirstName(existingUser.getFirstName());
            assertFieldUnchanged(User::getLastName, user, existingUser, "lastName");
            user.setLastName(existingUser.getLastName());
            assertFieldUnchanged(User::getEmail, user, existingUser, "email");
            user.setEmail(existingUser.getEmail());
            assertFieldUnchanged(User::isConfirmed, user, existingUser, "confirmed");
            user.setConfirmed(existingUser.isConfirmed());
            assertFieldUnchanged(User::getLevel, user, existingUser, "level");
            user.setLevel(existingUser.getLevel());
            assertFieldUnchanged(User::getMaxRunningSimulations, user, existingUser, "maxRunningSimulations");
            user.setMaxRunningSimulations(existingUser.getMaxRunningSimulations());
        } else {
            if (user.getFolder() == null) user.setFolder(existingUser.getFolder());
            if (user.isAccountLocked() == null) user.setAccountLocked(existingUser.isAccountLocked());
            if (user.getFirstName() == null) user.setFirstName(existingUser.getFirstName());
            if (user.getLastName() == null) user.setLastName(existingUser.getLastName());
            if (user.getEmail() == null) user.setEmail(existingUser.getEmail());
            if (user.isConfirmed() == null) user.setConfirmed(existingUser.isConfirmed());
            if (user.getLevel() == null) user.setLevel(existingUser.getLevel());
            if (user.getMaxRunningSimulations() == null) user.setMaxRunningSimulations(existingUser.getMaxRunningSimulations());
        }
    }

    private void assertFieldUnchanged(Function<User, Object> getter, User user, User existingUser, String fieldName) throws VipException {
        Object oldValue = getter.apply(existingUser);
        Object newValue = getter.apply(user);
        if (newValue == null) {
            return;
        }
        if ( ! Objects.equals(newValue, oldValue)) {
            logger.error("Forbidden Field {} changed from [{}] to [{}]", fieldName, oldValue, newValue);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, fieldName, "Cannot be updated");
        }
    }

    private void assertFieldAbsent(Function<User, Object> getter, User user, String fieldName) throws VipException {
        Object value = getter.apply(user);
        if (value != null) {
            logger.error("Forbidden Field {} present with [{}]", fieldName, value);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, fieldName, "Cannot be present");
        }
    }

    public void assertPublicNonAutoGroups(Set<Group> groups) throws VipException {
        Set<Group> publicNonAutoGroups =
                groupBusiness.getPublic().stream().filter(group -> ! group.isAuto()).collect(Collectors.toSet());
        if ( ! publicNonAutoGroups.containsAll(groups)) {
            logger.error("Cannot join these groups [{}], one of them does not exist or is not public or is auto", groups);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "groups", "Must only contains public non-auto groups");
        }
    }

    public void assertGroupsExist(Set<Group> groups) throws VipException {
        Set<Group> allGroups = new HashSet<>(groupBusiness.get());
        if ( ! allGroups.containsAll(groups)) {
            logger.error("Cannot join these groups [{}], one of them does not exist ", groups);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "groups", "Contains a not existing group");
        }
    }

    public Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email)
            throws VipException {
        try {
            return usersGroupsDAO.getUserGroups(email);

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
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

    public List<String> getAllUserNames() throws VipException {
        return getUsers().stream().map(User::getFullName).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Boolean> getUserPropertiesGroups(String email)
            throws VipException {
        try {
            return usersGroupsDAO.getUserPropertiesGroups(email);

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

    @VIPExternalSafe
    public List<User> searchUsers(String query, int limit) throws VipException {
        // only administrators and developers can perform this search
        if (!getUserLevel().equals(UserLevel.Administrator) && !getUserLevel().equals(UserLevel.Developer)) {
            throw new VipException(DefaultError.ACCESS_DENIED);
        }

        if (query == null || query.isBlank()) {
            return List.of();
        }

        int safeLimit = Math.max(1, Math.min(limit, 50));
        String normalized = query.trim().toLowerCase();

        // TODO : optimize by doing the filtering in the database instead of in memory
        return getUsers().stream()
                .filter((user) -> matchesQuery(user, normalized))
                .limit(safeLimit)
                // return abbreviated User objects to avoid leaking data
                .map(u -> new User(u.getId(), u.getFirstName(), u.getLastName()))
                .collect(Collectors.toList());
    }

    private boolean matchesQuery(User user, String query) {
        if (user == null) {
            return false;
        }

        Stream<String> fields = Stream.of(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName());

        return fields.filter((value) -> value != null && !value.isBlank())
                .map((value) -> value.toLowerCase())
                .anyMatch((value) -> value.contains(query));
    }

    @VIPExternalSafe
    public void remove(String id, boolean sendNotificationEmail) throws VipException {
        User user = get(id);

        if (user == null) {
            throw new VipException(DefaultError.NOT_FOUND, User.class.getSimpleName(), id);
        }
        if ( ! getUserLevel().equals(UserLevel.Administrator)) {
            if (user.getId() != getUser().getId()) {
                // only admin can remove "other" accounts
                throw new VipException(DefaultError.ACCESS_DENIED);
            }
        }
        try {
    
            gridaPoolClient.removeOperationsByUser(user.getEmail());

            gridaPoolClient.delete(server.getDataManagerUsersHome() + "/"
                    + user.getFolder(), user.getEmail());
            gridaPoolClient.delete(server.getDataManagerUsersHome() + "/"
                    + user.getFolder() + "_" + CoreConstants.FOLDER_TRASH, user.getEmail());

            userDAO.remove(user.getEmail());

            if (sendNotificationEmail) {

                String adminsEmailContents = emailTemplateUtils.removeAccount(user);
                emailBusiness.sendEmailToAdmins("[VIP Admin] Account Removed", adminsEmailContents,
                        true, user.getEmail());
            }
        } catch (GRIDAClientException ex) {
            logger.error("Error removing user id {}", id, ex);
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
            logger.warn("Cannot determine country from email extension {}: user will be mapped to Antartica", country,
                    e);
        }

        return new User(
                firstName.trim(),
                lastName.trim(),
                email.trim(),
                institution.trim(),
                cc);
    }

    public User getUser(String email) throws VipException {
        try {
            return userDAO.get(email);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public User getUserWithGroups(String email) throws VipException {
        try {
            User user = userDAO.get(email);
            user.setGroups(usersGroupsDAO.getUserGroups(email));
            return user;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public User getUserWithGroupsById(String id) throws VipException {
        try {
            User user = userDAO.getById(id);
            if (user != null) {
                user.setGroups(usersGroupsDAO.getUserGroups(user.getEmail()));
            }
            return user;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public User getCurrentUser() throws VipException {
        return getUser();
    }

    public void updateTermsOfUse(String email) throws VipException {
        try {
            userDAO.updateTermsOfUse(email, new Timestamp(System.currentTimeMillis()));
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateLastUpdatePublication(String email) throws VipException {
        try {
            userDAO.updateLastUpdatePublication(email, new Timestamp(System.currentTimeMillis()));
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateUser(String email, UserLevel level, CountryCode countryCode,
            int maxRunningSimulations, boolean locked)
            throws VipException {
        try {
            userDAO.update(email, level, countryCode, maxRunningSimulations, locked);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateUserLastLogin(String email) throws VipException {
        try {
            userDAO.updateLastLogin(email, new Date());
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

    public String getFromApikey(String email) throws VipException {
        try {
            return userDAO.getUserApikey(email);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public User getUserWithSession(String email) throws DAOException {
        String session = UUID.randomUUID().toString();
        userDAO.updateSession(email, session);

        return userDAO.get(email);
    }

    @VIPExternalSafe
    public void updateUserPassword(String userId, String password) throws VipException {
        if (password == null || password.isBlank()) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "password", "Password is required!");
        }

        // Ensure user can only change their own password
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new VipException(DefaultError.ACCESS_DENIED);
        }

        passwordBusiness.setPassword(currentUser.getEmail(), password);
    }

    public boolean testLastUpdatePublication(String email) throws VipException {
        try {
            if (userDAO.getLastPublicationUpdate(email) == null) {
                return true;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(userDAO.getLastPublicationUpdate(email));
                cal.add(Calendar.MONTH, server.getNumberMonthsToTestLastPublicationUpdates());
                Timestamp ts = new Timestamp(cal.getTime().getTime());
                return ts.before(new Timestamp(System.currentTimeMillis()));
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

        public void resetNextEmail(String currentEmail) throws VipException {
        try {
            userDAO.updateNextEmail(currentEmail, null);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void addUserToGroup(String email, String groupName) throws VipException {
        try {
            usersGroupsDAO.add(email, groupName, GROUP_ROLE.User);
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

    @VIPExternalSafe
    public User get(String id) throws VipException {
        // becareful sensitive fields are only removed when processing
        // the json response, before that, all users objects ARE NOT safe
        if ( ! getUserLevel().equals(UserLevel.Administrator) && ! getUser().getId().equals(id)) {
            throw new VipException(DefaultError.ACCESS_DENIED);
        } else {
            return getUserWithGroupsById(id);
        }
    }

    @VIPExternalSafe
    public PrecisePage<User> getAll(int offset, int quantity) throws VipException {
        permissions.filter((c) -> c.admin());

        List<User> users = getUsers();

        for (User u: users) {
            u.setGroups(usersGroupsDAO.getUserGroups(u.getEmail()));
        }

        return pageBuilder.doPrecise(offset, quantity, users);
    }

    public Optional<User> getByFullname(String fullname) throws VipException {
        List<User> searchResult = getByFullnames(List.of(fullname));
        // maybe there could be 2 users with the same name...
        if (searchResult.size() > 1) {
            logger.warn("Found more than 1 user with the fullname {} : {}", fullname,
                    searchResult.stream().map(User::getEmail).toList());
            // doing as if not found
            return Optional.empty();
        }
        return searchResult.stream().findFirst();
    }

    public List<User> getByFullnames(List<String> fullnames) throws VipException {
        try {
            return userDAO.getByFullNames(fullnames);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }
}
