package fr.insalyon.creatis.vip.social.integrationtest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.social.models.GroupMessage;
import fr.insalyon.creatis.vip.social.models.Message;
import fr.insalyon.creatis.vip.social.server.business.MessageBusiness;


public class SocialIT extends BaseSpringIT {

    @Autowired
    private MessageBusiness messageBusiness;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        setAdminContext();

        adminEmail = server.getAdminEmail();
        admin = userBusiness.getUserWithGroups(adminEmail);

        // Create test group
        group1 = new Group(nameGroup1, true, GroupType.APPLICATION);
        groupBusiness.add(group1);

        // Create test users
        createUserInGroup(emailUser1, "suffix1", nameGroup1);
        createUserInGroup(emailUser2, "suffix2", nameGroup1);
        createUserInGroup(emailUser3, "suffix3", nameGroup1);
        createUser(emailUser4, "suffix4");

        // Get test users
        user1 = userBusiness.getUser(emailUser1);
        user2 = userBusiness.getUser(emailUser2);
        user3 = userBusiness.getUser(emailUser3);
        user4 = userBusiness.getUser(emailUser4);

        asAdminContext(() -> {
            HashMap<String, CoreConstants.GROUP_ROLE> adminGroupRole = new HashMap<>();
            adminGroupRole.put(nameGroup1, CoreConstants.GROUP_ROLE.Admin);
            userBusiness.setUserGroups(emailUser1, adminGroupRole);
            userBusiness.setUserGroups(emailUser2, adminGroupRole);
        });

        user1 = userBusiness.getUserWithGroups(emailUser1);
        user2 = userBusiness.getUserWithGroups(emailUser2);

        // Send test messages
        setCurrentUser(admin);
        sendMessageAs(admin, new String[]{user1.getId(), user3.getId()}, "test subject", "test message");

        sendGroupMessageAs(user1, nameGroup1, "subject user 1", "message user 1");

    }

    @Test
    public void testInitialisation() throws VipException {
        Message firstIndividualMessage = getMessagesByUser(user1, getNextSecondDate()).get(0);
        GroupMessage firstGroupMessage = messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).get(0);
        List<Message> sentMessagesByAdmin = getSentMessagesByUser(admin, getNextSecondDate());

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of unread messages");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of unread messages");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of unread messages");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of unread messages");

        // verify first individual message properties (sender, subject, content, receiver)
        Assertions.assertEquals("test-admin@test.com", firstIndividualMessage.getSender().getEmail(), "Incorrect sender email");
        Assertions.assertEquals("test message", firstIndividualMessage.getMessage(), "Incorrect message");
        Assertions.assertEquals(false, firstIndividualMessage.isRead(), "Incorrect message isRead");
        Assertions.assertEquals("test subject", firstIndividualMessage.getTitle(), "Incorrect message title");
        Assertions.assertEquals(1, firstIndividualMessage.getReceivers().length, "Incorrect message receivers number");
        Assertions.assertEquals(emailUser1, firstIndividualMessage.getReceivers()[0].getEmail(), "Incorrect message receivers");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");

        // verify first individual message receivers
        Assertions.assertEquals(1, sentMessagesByAdmin.size(), "Incorrect admin messages number");
        List<String> receivers = Arrays.stream(sentMessagesByAdmin.get(0).getReceivers()).map(User::getEmail).collect(Collectors.toList());
        Assertions.assertEquals(2, receivers.size(), "Incorrect message receivers number");
        Assertions.assertTrue(receivers.containsAll(Arrays.asList(emailUser1, emailUser3)), "Incorrect message receivers");

        // verify first group message properties
        Assertions.assertEquals(emailUser3, getMessagesByUser(user3, getNextSecondDate()).get(0).getReceivers()[0].getEmail(), "Incorrect message receivers number");
        Assertions.assertEquals(nameGroup1, firstGroupMessage.getGroupName(), "Incorrect group name");
        Assertions.assertEquals("message user 1", firstGroupMessage.getMessage(), "Incorrect group message");
        Assertions.assertEquals(emailUser1, firstGroupMessage.getSender().getEmail(), "Incorrect group message sender");
    }


    /* ********************************************************************************************************************************************** */
    /* ************************************************************ send individual message ********************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSendMessage() throws Exception {
        sendMessageAs(admin, new String[]{user1.getId(), user3.getId()}, "subject user 2", "message user 2");

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 2);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 4);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify number of messages by user
        Assertions.assertEquals(2, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, messageBusiness.verifyMessages(emailUser1), "Incorrect number of unread messages");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of unread messages");
        Assertions.assertEquals(2, messageBusiness.verifyMessages(emailUser3), "Incorrect number of unread messages");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of unread messages");

        // verify nb group message
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    @Test
    public void testSendMessageAll() throws VipException {
        sendMessageAs(admin, new String[]{"All"}, "subject user 2", "message user 2");

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 2);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 7);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify number of messages by user
        Assertions.assertEquals(2, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, messageBusiness.verifyMessages(emailUser1), "Incorrect number of unread messages");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser2), "Incorrect number of unread messages");
        Assertions.assertEquals(2, messageBusiness.verifyMessages(emailUser3), "Incorrect number of unread messages");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser4), "Incorrect number of unread messages");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(adminEmail), "Incorrect number of unread messages");

        // verify number of group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    @Test
    public void testCatchNonExistentUserSendMessage() {
        Exception exception = assertThrows(
                VipException.class, () ->
                sendMessageAs(
                    nonExistentUser,
                                new String[]{user1.getId(), user3.getId()},
                                "subject user 2", "message user 2")
        );

        // INSERT + nonExistent foreign key sender => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "You do not have the right to do that! (Error code 1001)"));
    }


    /* ********************************************************************************************************************************************** */
    /* *********************************************************** remove individual message ******************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveMessage() throws VipException {
        long messageId = getMessagesByUser(user1, getNextSecondDate()).get(0).getId();
        setCurrentUser(admin);
        messageBusiness.remove(messageId);

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 0);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 0);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(0, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number of group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchRemoveMessage() throws VipException {
        // DELETE + nonExistent primary key messageId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        setCurrentUser(admin);
        messageBusiness.remove(100);
    }


    /* ********************************************************************************************************************************************** */
    /* *************************************************** remove individual message from receiver  ************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveByReceiver() throws VipException {
        setCurrentUser(user3);
        removeByReceiverAs(user3, getMessagesByUser(user1, getNextSecondDate()).get(0).getId());
        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 1);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify number of messages by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Invalid number of indivud messages");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Nombre incorrect de messages non lus");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Nombre incorrect de messages non lus");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser3), "Nombre incorrect de messages non lus");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Nombre incorrect de messages non lus");

        // verify number of group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    @Test
    public void testCatchNonExistentUserRemoveByReceiver() throws VipException {
        // DELETE + nonExistent foreign key / part of primary key receiver => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        removeByReceiverAs(nonExistentUser, getMessagesByUser(user1, getNextSecondDate()).get(0).getId());
    }


    @Test
    public void testCatchNonExistentMessageRemoveByReceiver() throws VipException {
        // DELETE + nonExistent foreign key / part of primary key messageId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        removeByReceiverAs(user3, 2);
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************* send individual message with support in copy *********************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCopyMessageToVipSupport() throws VipException {
        copyMessageToVipSupportAs
                (
                        user1,
                        new String[]{user1.getId(), user3.getId()},
                        "subject test copy message to Vip support",
                        "message test copy message to Vip support"
                );

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    //FIXME : does not check if the sender exists
    @Test
    public void testCatchCopyMessageToVipSupport() throws VipException {
        copyMessageToVipSupportAs(nonExistentUser, new String[]{user1.getId(), user3.getId()}, "subject test copy message to Vip support", "message test copy message to Vip support");

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************** send individual message to support ***************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSendMessageToVipSupport() throws VipException {

        sendMessageToVipSupportAs
                (
                        user2,
                        "subject",
                        "message from test2@test.fr to Vip support",
                        List.of("workflow 1", "workflow 2"),
                        List.of("simulation 1", "simulation 2")
                );

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);


        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    //FIXME : does not check if the sender exists
    @Test
    public void testCatchNonExistentEmailSendMessageToVipSupport() throws VipException {
        sendMessageToVipSupportAs
                (
                        nonExistentUser,
                        "subject",
                        "message from test2@test.fr to Vip support",
                        List.of("workflow 1", "workflow 2"),
                        List.of("simulation 1", "simulation 2")
                );

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);


        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    /* ********************************************************************************************************************************************** */
    /* ******************************************************* mark individual message as read ****************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testMarkAsRead() throws VipException {
        markAsReadAs(user1, getMessagesByUser(user1, getNextSecondDate()).get(0).getId());

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchNonExistentUserMarkAsRead() throws VipException {
        // UPDATE + nonExistent primary key receiver => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be updated
        markAsReadAs(nonExistentUser, getMessagesByUser(user1, getNextSecondDate()).get(0).getId());

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);


        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchNonExistentMessageMarkAsRead() throws VipException {
        // UPDATE + nonExistent part of primary key messageId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be updated
        markAsReadAs(user1, 100);

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);


        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    /* ********************************************************************************************************************************************** */
    /* ******************************************************* get individual message by user ******************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetMessageByUser() throws VipException {
        List<Message> messages = getMessagesByUser(user1, getNextSecondDate());

        Assertions.assertEquals(1, messages.size(), "Incorrect number of messages received");
    }

    @Test
    public void testCatchGetMessageByUser() throws VipException {
        // SELECT + nonExistent foreign key / part of primary key email => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        List<Message> messages = getMessagesByUser(nonExistentUser, getNextSecondDate());

        Assertions.assertEquals(0, messages.size(), "Incorrect number of messages received");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* send group message ************************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSendGroupMessage() throws VipException {
        sendGroupMessageAs(user2, nameGroup1, "subject user 2", "message user 2");

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 2);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        Assertions.assertEquals(2, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    @Test
    public void testCatchNonExistentSenderGroupMessage() {
        Exception exception = assertThrows(VipException.class,
            () -> sendGroupMessageAs(nonExistentUser, nameGroup1, "subject user 2", "message user 2"));

        // INSERT + nonExistent foreign key sender => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "You do not have the right to do that! (Error code 1001)"));
    }


    @Test
        public void testCatchNonExistentGroupMessage() {
        Exception exception = assertThrows(VipException.class,
            () -> sendGroupMessageAs(user3, "nonExistent group", "subject user 2", "message user 2"));

        // INSERT + nonExistent foreign key groupName => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "You do not have the right to do that! (Error code 1001)"));
    }

    @Test
    public void testCatchNonExistentUsersSendGroupMessage() throws VipException {
        try {
            setAdminContext();
            Group emptyGroup = new Group("empty-group", true, GroupType.APPLICATION);
            groupBusiness.add(emptyGroup);
            HashMap<String, CoreConstants.GROUP_ROLE> adminGroupRole = new HashMap<>();
            adminGroupRole.put("empty-group", CoreConstants.GROUP_ROLE.Admin);
            userBusiness.setUserGroups(emailUser1, adminGroupRole);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        sendGroupMessageAs(admin, "empty-group", "subject user 2", "message user 2");

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 2); // Inserted even if there is no receiver
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
        Assertions.assertEquals(1, messageBusiness.getGroupMessages("empty-group", getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* remove group message *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveGroupMessage() throws VipException {
        removeGroupMessageAs(user1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).get(0).getId());

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 0); // changed
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(0, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received"); // changed
    }

    @Test
    public void testCatchRemoveGroupMessage() throws VipException {
        // DELETE + nonExistent primary key groupId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        setCurrentUser(admin);
        messageBusiness.removeGroupMessage(100); // inexisting group message id

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, getMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, getMessagesByUser(user3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, getMessagesByUser(user4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    /* ******************************************************************************************************************************************************* */
    /* **************************************************************** get message by group ***************************************************************** */
    /* ******************************************************************************************************************************************************* */

    @Test
    public void testGetGroupMessages() throws VipException {
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchGetNonExistentGroupMessages() throws VipException {
        // SELECT + nonExistent foreign key groupName => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        Assertions.assertEquals(0, messageBusiness.getGroupMessages("nonExistent group", getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    /* ******************************************************************************************************************************************************* */
    /* *********************************************************** get individual message by user ************************************************************ */
    /* ******************************************************************************************************************************************************* */


    @Test
    public void testGetSentMessageByUser() throws VipException {
        Assertions.assertEquals(0, getSentMessagesByUser(user1, getNextSecondDate()).size(), "Incorrect number of individual messages sent");
    }


    @Test
    public void testAdminGetSentMessageByUser() throws VipException {
        Assertions.assertEquals(1, getSentMessagesByUser(admin, getNextSecondDate()).size(), "Incorrect number of individual messages sent");
    }


    @Test
    public void testCatchGetSentMessageByUser() throws VipException {
        // SELECT + nonExistent foreign key sender email  => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        Assertions.assertEquals(0, getSentMessagesByUser(nonExistentUser, getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    /* ******************************************************************************************************************************************************* */
    /* ****************************************************************** verify messages ******************************************************************** */
    /* ******************************************************************************************************************************************************* */


    @Test
    public void testCatchIncorrectEmailVerifyMessages() throws VipException {
        // SELECT + nonExistent foreign key receiver  => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        messageBusiness.verifyMessages("nonExistent user");
    }

    private List<Message> getMessagesByUser(User user, Date startDate) throws VipException {
        setCurrentUser(user);
        return messageBusiness.getMessagesByUser(startDate);
    }

    private List<Message> getSentMessagesByUser(User user, Date startDate) throws VipException {
        setCurrentUser(user);
        return messageBusiness.getSentMessagesByUser(startDate);
    }

    private void sendMessageAs(User user, String[] recipients, String subject, String message) throws VipException {
        setCurrentUser(user);
        messageBusiness.sendMessage(recipients, subject, message);
    }

    private void sendGroupMessageAs(User user, String groupName, String subject, String message) throws VipException {
        setCurrentUser(user);
        messageBusiness.sendGroupMessage(groupName, subject, message);
    }

    private void copyMessageToVipSupportAs(User user, String[] recipients, String subject, String message) throws VipException {
        setCurrentUser(user);
        messageBusiness.copyMessageToVipSupport(recipients, subject, message);
    }

    private void sendMessageToVipSupportAs(User user, String subject, String message,
            List<String> workflowIDs, List<String> simulationNames) throws VipException {
        setCurrentUser(user);
        messageBusiness.sendMessageToVipSupport(subject, message, workflowIDs, simulationNames);
    }

    private void removeByReceiverAs(User user, long id) throws VipException {
        setCurrentUser(user);
        messageBusiness.removeByReceiver(id);
    }

    private void markAsReadAs(User user, long id) throws VipException {
        setCurrentUser(user);
        messageBusiness.markAsRead(id);
    }

    private void removeGroupMessageAs(User user, long id) throws VipException {
        setCurrentUser(user);
        messageBusiness.removeGroupMessage(id);
    }


}
