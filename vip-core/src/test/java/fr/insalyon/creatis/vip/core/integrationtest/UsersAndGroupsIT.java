package fr.insalyon.creatis.vip.core.integrationtest;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static fr.insalyon.creatis.vip.core.client.view.user.UserLevel.Beginner;
import static fr.insalyon.creatis.vip.core.client.view.user.UserLevel.Developer;
import static org.junit.jupiter.api.Assertions.*;

public class UsersAndGroupsIT extends BaseSpringIT {
    private User user5;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Create test group
        group1 = new Group("group1", true, true, true);
        configurationBusiness.addGroup(group1);

        // Create test group
        group2 = new Group("group2", false, true, false);
        configurationBusiness.addGroup(group2);

        // Create test users
        createUserInGroup(emailUser1, "suffix1", "group1");
        createUserInGroup(emailUser2, "suffix2", "group1");
        createUserInGroup(emailUser3, "suffix3", "group1");
        createUserInGroup(emailUser4, "suffix4", "group2");
        user1 = configurationBusiness.getUser(emailUser1);
        user2 = configurationBusiness.getUser(emailUser2);
        user3 = configurationBusiness.getUser(emailUser3);
        user4 = configurationBusiness.getUser(emailUser4);

        // Create a very complete test users
        user5 = new User("firstName", "lastName", "email5@test.fr", "nextEmail@test.fr", "institution", "password", false, "code", "folder", "session", new Date(), new Date(), Beginner, CountryCode.fr, 1, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, false);
        createUserInGroup("email5@test.fr", "suffix5", "group2");

        Map<Group, CoreConstants.GROUP_ROLE> groups = new HashMap<>();
        groups.put(group1, CoreConstants.GROUP_ROLE.User);
        // Affect groups to users
        user1.setGroups(groups);
        user2.setGroups(groups);
        user3.setGroups(groups);

        groups = new HashMap<>();
        groups.put(group2, CoreConstants.GROUP_ROLE.Admin);
        // Affect groups to user
        user4.setGroups(groups);
    }

    @Test
    public void testInitialization() throws BusinessException {
        assertEquals(3, configurationBusiness.getGroups().size(), "incorrect groups number");// Support + group1
        assertEquals(6, configurationBusiness.getUsers().size(), "incorrect users number");// Created users + admin
        assertEquals(1, user5.getMaxRunningSimulations(), "incorrect max running simulations");// Created users + admin
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** create and add user *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCreateUser() throws BusinessException, GRIDAClientException {
        // try all the constructors
        User user6 = new User("firstName", "lastName", "email9@test.fr", "institution", "password", CountryCode.fr, new Timestamp(System.currentTimeMillis()));
        configurationBusiness.signup(user6, "", false, true, group2);

        // Check users number
        assertRowsNbInTable("VIPUsers", 7);
    }

    @Test
    public void testCatchExistingEmailCreateUser() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        createUser(emailUser4, "suffix0")
        );
        System.out.println("exception.getMessage() : " + exception.getMessage());

        // INSERT + existing primary key groupName => Unique index or primary key violation
        assertTrue(StringUtils.contains(exception.getMessage(), "There is an existing account associated with the email: test4@test.fr or with the first name,last name: test firstName suffix0,test lastName suffix0"));
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** create group *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCreateGroup() throws DAOException, BusinessException {
        Group group = new Group("group3", true, true, true);
        configurationBusiness.addGroup(group);
        assertNotNull(configurationBusiness.getGroup("group3"));
    }

    @Test
    public void testCatchCreateGroupAlreadyExisting() {
        Group group = new Group("group1", true, true, true);

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.addGroup(group)
        );
        // INSERT + existing primary key groupName => Unique index or primary key violation
        assertTrue(StringUtils.contains(exception.getMessage(), "A group named group1 already exists"));
    }


    /* ********************************************************************************************************************************************** */
    /* ******************************************************************* get group **************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCatchGetGroup() throws BusinessException {
        Group group = configurationBusiness.getGroup(nameGroup1);
        Assertions.assertEquals(nameGroup1, group.getName(), "Incorrect group name");
        assertTrue(group.isPublicGroup(), "Incorrect group privacy");
    }

    @Test
    public void testCatchGetNonExistentGroup() throws BusinessException {
        assertNull(configurationBusiness.getGroup("group3"));
    }

    /* ********************************************************************************************************************************************** */
    /* ******************************************************************* get group **************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCatchGetGroups() throws BusinessException {
        List<Group> groups = configurationBusiness.getGroups();
        Assertions.assertEquals(3, groups.size(), "Incorrect groups number");// group1, group2 and support
    }


    /* ********************************************************************************************************************************************** */
    /* **************************************************************** update group **************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateGroup() throws BusinessException {
        Group group = configurationBusiness.getGroup(nameGroup1);
        group.setPublicGroup(false);
        group.setName("group_name_updated");
        configurationBusiness.updateGroup(nameGroup1, group);
        Group updatedGroup = configurationBusiness.getGroup("group_name_updated");
        Assertions.assertEquals("group_name_updated", updatedGroup.getName(), "Incorrect group name");
        assertFalse(updatedGroup.isPublicGroup(), "Incorrect group privacy");
    }


    @Test
    public void testCatchUpdateNonExistentGroup() throws BusinessException {
        Group group = new Group();
        group.setPublicGroup(false);
        group.setName("group_name_updated");
        // UPDATE + nonExistent primary key group name => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be updated
        configurationBusiness.updateGroup("non existent group", group);
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** get group ************************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateNonExistentGroup() throws BusinessException {
        // SELECT + nonExistent foreign key / part of primary key groupName => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        assertNull(configurationBusiness.getGroup("nonExistent_group"));
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** add user to group ************************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAddUserToGroup() throws BusinessException {
        configurationBusiness.addUserToGroup(emailUser4, nameGroup1);

        List<String> emails = configurationBusiness.getUsersFromGroup(nameGroup1)
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        Assertions.assertEquals(4, emails.size(), "incorrect number of users in the group");
        Assertions.assertTrue(emails.containsAll(Arrays.asList(emailUser1, emailUser2, emailUser3, emailUser4)), "Incorrect group message receivers");

    }


    @Test
    public void testCatchNonExistentUserAddUserToGroup() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.addUserToGroup("nonExistent user", nameGroup1)
        );
        // INSERT + nonExistent foreign key / part of primary key groupName => user email
        assertTrue(StringUtils.contains(exception.getMessage(), "JdbcSQLException: Referential integrity constraint violation"));
    }

    @Test
    public void testCatchNonExistentGroupAddUserToGroup() {

        Exception exception = assertThrows
                (BusinessException.class, () ->
                        configurationBusiness.addUserToGroup(emailUser4, "nonExistent group")
                );

        // INSERT + nonExistent foreign key / part of primary key groupName => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "JdbcSQLException: Referential integrity constraint violation"));

    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** remove user *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCatchRemoveUser() throws BusinessException {
        configurationBusiness.removeUser(emailUser1, false);
        assertRowsNbInTable("VIPUsers", 5);
    }

    @Test
    public void testCatchRemoveNonExistentUser() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.removeUser("nonExistent user", false)

        );
        System.out.println("exception.getMessage() : " + exception.getMessage());
        // getUser is called and had an exception before the beginning of the internship
        assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail: nonExistent user"));
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** remove group *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveGroup() throws BusinessException {
        configurationBusiness.removeGroup(emailUser1, nameGroup1);
        Assertions.assertEquals(2, configurationBusiness.getGroups().size(), "incorrect groups number");
    }

    @Test
    public void testCatchRemoveNonExistentGroup() throws BusinessException {
        // DELETE + nonExistent foreign key / part of primary key groupName => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        configurationBusiness.removeGroup(emailUser1, "nonExistent group");
        Assertions.assertEquals(3, configurationBusiness.getGroups().size(), "incorrect groups number");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************ remove user from group ********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveUserFromGroup() throws BusinessException {
        configurationBusiness.removeUserFromGroup(emailUser1, nameGroup1);

        List<String> emails = configurationBusiness.getUsersFromGroup(nameGroup1)
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        Assertions.assertEquals(2, emails.size(), "incorrect number of users in the group");
        Assertions.assertTrue(emails.containsAll(Arrays.asList(emailUser2, emailUser3)), "Incorrect group message receivers");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** get or create user ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetOrCreateExistingUser() throws BusinessException, DAOException {
        User user = configurationBusiness.getOrCreateUser(emailUser2, "test institution", "group1");

        Assertions.assertEquals("test firstName suffix2", user.getFirstName(), "incorrect user firstname");
        Assertions.assertEquals("test lastName suffix2", user.getLastName(), "incorrect user firstname");

    }

    @Test
    public void testGetOrCreateNonExistentUser() throws BusinessException, DAOException {
        configurationBusiness.getOrCreateUser("nonExistentUser@test.fr", "institution", "group1");

        // verify entry numbers in VIPUsers table
        assertRowsNbInTable("VIPUsers", 7);
    }

    @Test
    public void testGetOrCreateIncorrectEmailUser() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.getOrCreateUser("nonExistent_user", "institution", "group1")
        );

        System.out.println("exception.getMessage() : " + exception.getMessage());
        // exception added because before the exception java.lang.StringIndexOutOfBoundsException was raised
        assertTrue(StringUtils.contains(exception.getMessage(), "The email nonExistent_user is invalid"));
    }


    /* ********************************************************************************************************************************************** */
    /* ********************************************************* get last update term of use ******************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetLastUpdateTermOfUse() throws BusinessException {
        // a term-of-use update must be inserted at database creation
        Timestamp timestamp = configurationBusiness.getLastUpdateTermsOfUse();
        assertEquals(LocalDate.now(), timestamp.toLocalDateTime().toLocalDate(), "Missing default term of use in database");
    }

    /* ********************************************************************************************************************************************** */
    /* *************************************************************** get user api key ************************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCatchGetApiKeyNonExistentUser() {

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.getUserApikey("nonExistent_user")
        );
        assertTrue(StringUtils.contains(exception.getMessage(), "Looking for an apikey, but there is no user registered with the email: nonExistent_user"));
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* get user admin groups ********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetUserAdminGroups() throws BusinessException {
        Map<Group, CoreConstants.GROUP_ROLE> userGroups = configurationBusiness.getUserGroups(emailUser3);
        assertFalse(userGroups.isEmpty());

    }

    /* ********************************************************************************************************************************************** */
    /* *************************************************************** validate session ************************************************************* */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testValidateNonExistentSession() throws BusinessException {
        // SELECT + nonExistent primary key session => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        assertFalse(configurationBusiness.validateSession(emailUser3, "nonExistent session"));
    }

    @Test
    public void testValidateNullSession() throws BusinessException {
        assertFalse(configurationBusiness.validateSession(emailUser3, null));
    }


    /* ********************************************************************************************************************************************** */
    /* *********************************************************** generate new user api key ******************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGenerateNewApiKeyUser() throws BusinessException {
        String newApiKey = configurationBusiness.generateNewUserApikey(emailUser3);
        assertEquals(configurationBusiness.getUserApikey(emailUser3), newApiKey, "Incorrect new user api key value");
    }

    @Test
    public void testCatchGenerateNewApiKeyNonExistentUser() {

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.generateNewUserApikey("nonExistent_user")
        );
        assertTrue(StringUtils.contains(exception.getMessage(), " Updating an apikey, but there is no user registered with the email: nonExistent_user"));
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** delete user api key *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCatchDeleteApiKeyNonExistentUser() throws BusinessException {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.deleteUserApikey("nonExistent_user")
        );

        assertTrue(StringUtils.contains(exception.getMessage(), "Updating an apikey, but there is no user registered with the email: nonExistent_user"));
    }


    /* ********************************************************************************************************************************************** */
    /* *************************************************************** get public groups ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetPublicGroups() throws BusinessException {
        List<String> groupsNames = configurationBusiness.getPublicGroups()
                .stream()
                .map(Group::getName)
                .collect(Collectors.toList());

        Assertions.assertEquals(1, groupsNames.size(), "incorrect number of public groups");
        Assertions.assertTrue(groupsNames.containsAll(List.of(nameGroup1)), "Incorrect public groups names");
    }


    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** get user data ************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetUserData() throws BusinessException {
        User user = configurationBusiness.getUserData(emailUser4);
        Assertions.assertEquals("test firstName suffix4", user.getFirstName(), "incorrect user firstname");
        Assertions.assertEquals("test lastName suffix4", user.getLastName(), "incorrect user firstname");
    }

    @Test
    public void testCatchGetNonExistentUserData() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.getUserData("nonExistent_user")
        );

        // Exception added before the beginning of the internship
        assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail: nonExistent_user"));

    }

    @Test
    public void testCatchNonExistentUserRemoveGroup() throws BusinessException {
        // DELETE + nonExistent foreign key user email => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        configurationBusiness.removeGroup("nonExistent user", nameGroup1);
        Assertions.assertEquals(2, configurationBusiness.getGroups().size(), "incorrect groups number");
    }

    /* ********************************************************************************************************************************************** */
    /* *********************************************************** get user properties groups ******************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetUserPropertiesGroup() throws BusinessException {
        assertTrue(configurationBusiness.getUserPropertiesGroups(emailUser1).get(0)); // isPublic : group is public, it is accessible to every user
        assertTrue(configurationBusiness.getUserPropertiesGroups(emailUser1).get(1)); // isGridFile : group allows to files sharing
        assertTrue(configurationBusiness.getUserPropertiesGroups(emailUser1).get(2)); // isGridJobs : group allows job offers sharing

        assertFalse(configurationBusiness.getUserPropertiesGroups(emailUser4).get(0));
        assertTrue(configurationBusiness.getUserPropertiesGroups(emailUser4).get(1));
        assertFalse(configurationBusiness.getUserPropertiesGroups(emailUser4).get(2));
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** signin user *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSigninUser() throws BusinessException {
        assertNotNull(configurationBusiness.signin(emailUser3, "testPassword"));
    }

    @Test
    public void testCatchSigninUserWrongPassword() throws BusinessException {

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.signin(emailUser3, "test wrong password")
        );

        // Exception added before the beginning of the internship
        assertTrue(StringUtils.contains(exception.getMessage(), "Authentication failed (email or password incorrect, or user is locked)"));

    }


    @Test
    public void testSigninWithoutResetingSessionUser() throws BusinessException {
        String session = configurationBusiness.getUser(emailUser3).getSession();
        assertNotNull(configurationBusiness.signinWithoutResetingSession(emailUser3, "testPassword"));
        Assertions.assertEquals(session, configurationBusiness.getUser(emailUser3).getSession(), "incorrect session value");
    }

    /* ********************************************************************************************************************************************** */
    /* ******************************************************************** send email ************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSendActivationCode() throws BusinessException {
        // Reset not to capture the calls to sendEmail in the Setup
        Mockito.reset(emailBusiness);

        // Capture email content
        ArgumentCaptor<String> emailContent = ArgumentCaptor.forClass(String.class);
        // Capture email recipient
        ArgumentCaptor<String[]> emailRecipients = ArgumentCaptor.forClass(String[].class);

        configurationBusiness.sendActivationCode(emailUser1);
        Mockito.verify(emailBusiness).sendEmail(Mockito.anyString(), emailContent.capture(), emailRecipients.capture(), Mockito.eq(true), Mockito.anyString());

        // Check email content
        Assertions.assertTrue(emailContent.getValue().contains(user1.getFirstName()), "Incorrect user firstname");
        Assertions.assertTrue(emailContent.getValue().contains(user1.getLastName()), "Incorrect user lastname");
        // Check recipient
        Assertions.assertEquals(1, emailRecipients.getValue().length, "Incorrect length of recipients");
        Assertions.assertEquals(emailUser1, emailRecipients.getValue()[0], "Incorrect user recipient");
    }

    @Test
    public void testCatchSendActivationCode() throws BusinessException {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.sendActivationCode("nonExistentUser@test.fr")
        );

        // getUser is called and had an exception before the beginning of the internship
        Assertions.assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail: nonExistentUser@test.fr"));
    }

    @Test
    public void testSendResetCode() throws BusinessException {
        // Reset not to capture the calls to sendEmail in the Setup
        Mockito.reset(emailBusiness);

        // Capture email content
        ArgumentCaptor<String> emailContent = ArgumentCaptor.forClass(String.class);
        // Capture email recipient
        ArgumentCaptor<String[]> emailRecipients = ArgumentCaptor.forClass(String[].class);

        configurationBusiness.sendResetCode(emailUser1);
        Mockito.verify(emailBusiness).sendEmail(Mockito.anyString(), emailContent.capture(), emailRecipients.capture(), Mockito.eq(true), Mockito.anyString());

        // Check email content
        Assertions.assertTrue(emailContent.getValue().contains(user1.getFirstName()), "Incorrect user firstname");
        Assertions.assertTrue(emailContent.getValue().contains(user1.getLastName()), "Incorrect user lastname");
        // Check recipient
        Assertions.assertEquals(1, emailRecipients.getValue().length, "Incorrect length of recipients");
        Assertions.assertEquals(emailUser1, emailRecipients.getValue()[0], "Incorrect user recipient");
    }

    @Test
    public void testCatchSendResetCode() throws BusinessException {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.sendResetCode("nonExistentUser@test.fr")
        );

        // getUser is called and had an exception before the beginning of the internship
        Assertions.assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail: nonExistentUser@test.fr"));
    }

    @Test
    public void testRequestNewEmail() throws BusinessException {
        // Reset not to capture the calls to sendEmail in the Setup
        Mockito.reset(emailBusiness);

        // Capture email content
        ArgumentCaptor<String> emailContent = ArgumentCaptor.forClass(String.class);
        // Capture email recipient
        ArgumentCaptor<String[]> emailRecipients = ArgumentCaptor.forClass(String[].class);

        configurationBusiness.requestNewEmail(user2, "newEmail@test.fr");
        Mockito.verify(emailBusiness).sendEmail(Mockito.anyString(), emailContent.capture(), emailRecipients.capture(), Mockito.eq(true), Mockito.anyString());

        // Check email content
        Assertions.assertTrue(emailContent.getValue().contains(user2.getFirstName()), "Incorrect user firstname");
        Assertions.assertTrue(emailContent.getValue().contains(user2.getLastName()), "Incorrect user lastname");
        // Check recipient
        Assertions.assertEquals(1, emailRecipients.getValue().length, "Incorrect length of recipients");
        Assertions.assertEquals("newEmail@test.fr", emailRecipients.getValue()[0], "Incorrect user recipient");
        // Check update db
        Assertions.assertNotNull(configurationBusiness.getUser(emailUser2));
        Assertions.assertEquals("newEmail@test.fr", configurationBusiness.getUser(emailUser2).getNextEmail(), "Incorrect user next email");

    }


    @Test
    public void testSendContactEmail() throws BusinessException {
        // Reset not to capture the calls to sendEmail in the Setup
        Mockito.reset(emailBusiness);

        // Capture email content
        ArgumentCaptor<String> emailContent = ArgumentCaptor.forClass(String.class);
        // Capture email recipient
        ArgumentCaptor<String[]> emailRecipients = ArgumentCaptor.forClass(String[].class);

        configurationBusiness.sendContactMail(user1, "category", "subject", "comment");
        Mockito.verify(emailBusiness).sendEmail(Mockito.anyString(), emailContent.capture(), emailRecipients.capture(), Mockito.eq(true), Mockito.anyString());

        // Check email content
        Assertions.assertTrue(emailContent.getValue().contains(user1.getFirstName()), "Incorrect user firstname");
        Assertions.assertTrue(emailContent.getValue().contains(user1.getLastName()), "Incorrect user lastname");
        // Check recipient
        Assertions.assertEquals(1, emailRecipients.getValue().length, "Incorrect length of recipients");
        Assertions.assertEquals(adminEmail, emailRecipients.getValue()[0], "Incorrect user recipient");

    }



    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** update user *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateUser() throws BusinessException {
        User user = configurationBusiness.getUser(emailUser1);
        user.setFolder("folder_updated");
        configurationBusiness.updateUser(user);
        User userUpdated = configurationBusiness.getUser(emailUser1);
        Assertions.assertEquals("folder_updated", userUpdated.getFolder(), "Incorrect user folder");
    }

    @Test
    public void testUpdateUserEmail() throws BusinessException {
        configurationBusiness.updateUserEmail(emailUser1, "newEmail@test.fr");

        // verify users number
        assertEquals(6, configurationBusiness.getUsers().size(), "incorrect users number");// Created users + admin

        // verify modified user infos
        Assertions.assertEquals("newEmail@test.fr", configurationBusiness.getUser("newEmail@test.fr").getEmail(), "incorrect email update user");
        Assertions.assertEquals("test firstName suffix1", configurationBusiness.getUser("newEmail@test.fr").getFirstName(), "incorrect first name update user");
    }

    @Test
    public void testCatchUpdateInexistantUserEmail() throws BusinessException {
        configurationBusiness.updateUserEmail("nonExistent email", "newEmail@test.fr");

        // verify users number
        assertEquals(6, configurationBusiness.getUsers().size(), "incorrect users number");// Created users + admin

        // verify modified user infos
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        Assertions.assertNull(configurationBusiness.getUser("newEmail@test.fr"))
        );

        // getUser is called and had an exception before the beginning of the internship
        Assertions.assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail: newEmail@test.fr"));
    }

    @Test
    public void testUpdatePassword() throws BusinessException {
        configurationBusiness.updateUserPassword(emailUser1, "testPassword", "testPassword updated");

        // because getPassword() returns empty, try to signin
        configurationBusiness.signin(emailUser1, "testPassword updated");

        Assertions.assertEquals("", configurationBusiness.getUser(emailUser1).getPassword(), "incorrect password update user");
    }

    @Test
    public void testCatchUpdateWrongCurrentPassword() throws BusinessException {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.updateUserPassword(emailUser1, "test password", "testPassword updated")
        );

        // Exception added before the beginning of the internship
        assertTrue(StringUtils.contains(exception.getMessage(), "The current password mismatch"));
    }

    @Test
    public void testSetRegistrationDate() throws BusinessException {
        Date now = new Date();
        user1.setRegistration(now);
        Assertions.assertEquals(now, user1.getRegistration(), "incorrect registration date");

    }

    @Test
    public void testSetLastLogin() throws BusinessException {
        Date now = new Date();
        user1.setLastLogin(now);
        Assertions.assertEquals(now, user1.getLastLogin(), "incorrect last login");

    }

    @Test
    public void testUpdateUserLastLogin() throws BusinessException, InterruptedException {
        configurationBusiness.signin(emailUser1, "testPassword");
        Date date = user1.getLastLogin();
        configurationBusiness.updateUserLastLogin(emailUser1);
        Assertions.assertTrue(date.compareTo(configurationBusiness.getUser(emailUser1).getLastLogin()) < 0);
    }

    @Test
    public void testSetMaxRunningSimulations() throws BusinessException {

        user1.setMaxRunningSimulations(5);
        Assertions.assertEquals(5, user1.getMaxRunningSimulations(), "incorrect max running simulations");

    }

    @Test
    public void testSetCountryCode() throws BusinessException {

        user1.setCountryCode(CountryCode.us);
        Assertions.assertEquals(CountryCode.us, user1.getCountryCode(), "incorrect country code");

    }

    @Test
    public void testUpdateTermsOfUse() throws BusinessException {
        Date oldDateTermeOfUse = configurationBusiness.getUser(emailUser1).getTermsOfUse();
        configurationBusiness.updateTermsOfUse(emailUser1);
        Date newDateTermeOfUse = configurationBusiness.getUser(emailUser1).getTermsOfUse();

        Assertions.assertTrue(oldDateTermeOfUse.compareTo(newDateTermeOfUse) < 0);
    }

    @Test
    public void testUpdateLastUpdatePublication() throws BusinessException {
        Date oldDateTermeOfUse = configurationBusiness.getUser(emailUser1).getLastUpdatePublications();
        configurationBusiness.updateLastUpdatePublication(emailUser1);
        Date newDateTermeOfUse = configurationBusiness.getUser(emailUser1).getLastUpdatePublications();

        Assertions.assertTrue(oldDateTermeOfUse.compareTo(newDateTermeOfUse) < 0);
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** reset next email user ********************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testResetNextEmail() throws BusinessException {
        configurationBusiness.resetNextEmail(emailUser1);

        // verify next email is null
        assertNull(configurationBusiness.getUser(emailUser1).getNextEmail(), "incorrect next email update user");
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** get user names ************************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetUserNames() throws BusinessException {
        List<String> userNames = configurationBusiness.getAllUserNames();
        Assertions.assertTrue(userNames.containsAll(List.of("test firstName suffix1 test lastName suffix1")), "Incorrect user names");
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** reset password ************************************************************* */
    /* ********************************************************************************************************************************************** */

    // TODO : add password verification
    /*@Test
    public void testResetPasswordCode() throws BusinessException {
        configurationBusiness.resetPassword(emailUser1, configurationBusiness.getUser(emailUser1).getCode(), "test new password");
    }*/

    @Test
    public void testCatchSendResetPasswordWrongCode() throws BusinessException {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.resetPassword(emailUser1, "test code", "test new password")
        );

        // Exception added before the beginning of the internship
        assertTrue(StringUtils.contains(exception.getMessage(), "Wrong reset code"));
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** check roles *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testIsDeveloper() throws BusinessException {
        assertFalse(user3.isDeveloper());
        user3.setLevel(Developer);
        assertTrue(user3.isDeveloper());
    }

    @Test
    public void testIsGroupNotAdmin() throws BusinessException {
        assertFalse(user3.isGroupAdmin());
    }

    @Test
    public void testIsGroupAdmin() throws BusinessException {
        assertTrue(user4.isGroupAdmin());
    }

    @Test
    public void testAssAcceptedTermOfUses() throws BusinessException {
        assertTrue(user4.hasAcceptTermsOfUse());
    }

    /* ********************************************************************************************************************************************** */
    /* ******************************************************************** signout ***************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSignout() throws BusinessException {
        assertNull(configurationBusiness.getUser(emailUser1).getSession());
        configurationBusiness.signin(emailUser1, "testPassword");
        assertNotNull(configurationBusiness.getUser(emailUser1).getSession());
        configurationBusiness.signout(emailUser1);
        // session has now a random value
        assertNotNull(configurationBusiness.getUser(emailUser1).getSession());
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** activate account *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCatchActivateIncorrectCode() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        configurationBusiness.activate(emailUser1, "incorrect code"));

        // Exception added before the beginning of the internship
        assertTrue(StringUtils.contains(exception.getMessage(), "Activation failed."));

    }

}
