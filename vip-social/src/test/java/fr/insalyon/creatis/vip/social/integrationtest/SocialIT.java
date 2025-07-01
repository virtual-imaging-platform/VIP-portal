package fr.insalyon.creatis.vip.social.integrationtest;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.server.business.MessageBusiness;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SocialIT extends BaseSpringIT {

    @Autowired
    private MessageBusiness messageBusiness;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Create test group
        group1 = new Group(nameGroup1, true, GroupType.APPLICATION);
        groupBusiness.add(group1);

        // Create test users
        createUserInGroup(emailUser1, "suffix1", nameGroup1);
        createUserInGroup(emailUser2, "suffix2", nameGroup1);
        createUserInGroup(emailUser3, "suffix3", nameGroup1);
        createUser(emailUser4, "suffix4");

        // Get test users
        user1 = configurationBusiness.getUser(emailUser1);
        user2 = configurationBusiness.getUser(emailUser2);
        user3 = configurationBusiness.getUser(emailUser3);
        user4 = configurationBusiness.getUser(emailUser4);
        adminEmail = ServerMockConfig.TEST_ADMIN_EMAIL;
        admin = configurationBusiness.getUser(adminEmail);

        // Send test messages
        messageBusiness.sendMessage(admin, new String[]{emailUser1, emailUser3}, "test subject", "test message");
        messageBusiness.sendGroupMessage(user1, nameGroup1,
                configurationBusiness.getUsersFromGroup(nameGroup1),
                "subject user 1", "message user 1");

    }

    @Test
    public void testInitialisation() throws BusinessException {
        Message firstIndividualMessage = messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).get(0);
        GroupMessage firstGroupMessage = messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).get(0);
        List<Message> sentMessagesByAdmin = messageBusiness.getSentMessagesByUser(adminEmail, getNextSecondDate());

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
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
        Assertions.assertEquals(emailUser3, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).get(0).getReceivers()[0].getEmail(), "Incorrect message receivers number");
        Assertions.assertEquals(nameGroup1, firstGroupMessage.getGroupName(), "Incorrect group name");
        Assertions.assertEquals("message user 1", firstGroupMessage.getMessage(), "Incorrect group message");
        Assertions.assertEquals(emailUser1, firstGroupMessage.getSender().getEmail(), "Incorrect group message sender");
    }


    /* ********************************************************************************************************************************************** */
    /* ************************************************************ send individual message ********************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSendMessage() throws Exception {
        messageBusiness.sendMessage(user1, new String[]{emailUser1, emailUser3}, "subject user 2", "message user 2");

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 2);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 4);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify number of messages by user
        Assertions.assertEquals(2, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, messageBusiness.verifyMessages(emailUser1), "Incorrect number of unread messages");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of unread messages");
        Assertions.assertEquals(2, messageBusiness.verifyMessages(emailUser3), "Incorrect number of unread messages");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of unread messages");

        // verify nb group message
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    @Test
    public void testSendMessageAll() throws BusinessException {
        messageBusiness.sendMessage(user2, new String[]{"All"}, "subject user 2", "message user 2");

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 2);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 7);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify number of messages by user
        Assertions.assertEquals(2, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(2, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
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
                BusinessException.class, () ->
                        messageBusiness.sendMessage(
                                nonExistentUser,
                                new String[]{emailUser1, emailUser3},
                                "subject user 2", "message user 2")
        );

        // INSERT + nonExistent foreign key sender => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "JdbcSQLException: Referential integrity constraint violation"));
    }


    /* ********************************************************************************************************************************************** */
    /* *********************************************************** remove individual message ******************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveMessage() throws BusinessException {
        messageBusiness.remove(messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).get(0).getId());

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 0);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 0);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number of group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchRemoveMessage() throws BusinessException {
        // DELETE + nonExistent primary key messageId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        messageBusiness.remove(100);
    }


    /* ********************************************************************************************************************************************** */
    /* *************************************************** remove individual message from receiver  ************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveByReceiver() throws BusinessException {
        messageBusiness.removeByReceiver(messageBusiness.getMessagesByUser(emailUser1, new Date(System.currentTimeMillis())).get(0).getId(), emailUser3);

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 1);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify number of messages by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Invalid number of indivud messages");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Nombre incorrect de messages non lus");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Nombre incorrect de messages non lus");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser3), "Nombre incorrect de messages non lus");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Nombre incorrect de messages non lus");

        // verify number of group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }

    @Test
    public void testCatchNonExistentUserRemoveByReceiver() throws BusinessException {
        // DELETE + nonExistent foreign key / part of primary key receiver => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        messageBusiness.removeByReceiver(messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).get(0).getId(), "nonExistent user");
    }


    @Test
    public void testCatchNonExistentMessageRemoveByReceiver() throws BusinessException {
        // DELETE + nonExistent foreign key / part of primary key messageId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        messageBusiness.removeByReceiver(2, emailUser3);
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************* send individual message with support in copy *********************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testCopyMessageToVipSupport() throws BusinessException {
        messageBusiness.copyMessageToVipSupport
                (
                        user1,
                        new String[]{emailUser1, emailUser3},
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
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    //FIXME : does not check if the sender exists
    @Test
    public void testCatchCopyMessageToVipSupport() throws BusinessException {
        messageBusiness.copyMessageToVipSupport(nonExistentUser, new String[]{emailUser1, emailUser3}, "subject test copy message to Vip support", "message test copy message to Vip support");

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
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
    public void testSendMessageToVipSupport() throws BusinessException {

        messageBusiness.sendMessageToVipSupport
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
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    //FIXME : does not check if the sender exists
    @Test
    public void testCatchNonExistentEmailSendMessageToVipSupport() throws BusinessException {
        messageBusiness.sendMessageToVipSupport
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
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
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
    public void testMarkAsRead() throws BusinessException {
        messageBusiness.markAsRead
                (
                        messageBusiness.getMessagesByUser(emailUser1,
                                getNextSecondDate()).get(0).getId(),
                        emailUser1
                );

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchNonExistentUserMarkAsRead() throws BusinessException {
        // UPDATE + nonExistent primary key receiver => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be updated
        messageBusiness.markAsRead
                (
                        messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).get(0).getId(),
                        "nonExistent user"
                );

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);


        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchNonExistentMessageMarkAsRead() throws BusinessException {
        // UPDATE + nonExistent part of primary key messageId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be updated
        messageBusiness.markAsRead(100, emailUser1);

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);


        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
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
    public void testGetMessageByUser() throws BusinessException {
        List<Message> messages = messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate());

        Assertions.assertEquals(1, messages.size(), "Incorrect number of messages received");
    }

    @Test
    public void testCatchGetMessageByUser() throws BusinessException {
        // SELECT + nonExistent foreign key / part of primary key email => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        List<Message> messages = messageBusiness.getMessagesByUser("nonExistent user", getNextSecondDate());

        Assertions.assertEquals(0, messages.size(), "Incorrect number of messages received");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* send group message ************************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testSendGroupMessage() throws BusinessException {
        messageBusiness.sendGroupMessage
                (
                        user2,
                        nameGroup1, configurationBusiness.getUsersFromGroup(nameGroup1),
                        "subject user 2",
                        "message user 2"
                );

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 2);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(2, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    @Test
    public void testCatchNonExistentSenderGroupMessage() {
        Exception exception = assertThrows
                (BusinessException.class, () ->
                        messageBusiness.sendGroupMessage
                                (
                                        nonExistentUser,
                                        nameGroup1,
                                        configurationBusiness.getUsersFromGroup(nameGroup1),
                                        "subject user 2",
                                        "message user 2"
                                )
                );

        // INSERT + nonExistent foreign key sender => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "JdbcSQLException: Referential integrity constraint violation"));
    }


    @Test
    public void testCatchNonExistentGroupMessage() throws BusinessException {
        Exception exception = assertThrows
                (BusinessException.class, () ->
                        messageBusiness.sendGroupMessage
                                (
                                        user3,
                                        "nonExistent group",
                                        configurationBusiness.getUsersFromGroup(nameGroup1),
                                        "subject user 2",
                                        "message user 2"
                                )
                );

        // INSERT + nonExistent foreign key groupName => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "JdbcSQLException: Referential integrity constraint violation"));
    }

    @Test
    public void testCatchNonExistentUsersSendGroupMessage() throws BusinessException {
        // SELECT + nonExistent foreign key / part of primary key groupName => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        messageBusiness.sendGroupMessage
                (
                        user3,
                        nameGroup1,
                        configurationBusiness.getUsersFromGroup("nonExistent group"),
                        "subject user 2",
                        "message user 2"
                );

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 2); // Inserted even if there is no receiver
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(2, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received"); // Inserted even if there is no receiver
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* remove group message *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveGroupMessage() throws BusinessException {
        messageBusiness.removeGroupMessage(messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).get(0).getId());

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 0); // changed
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser1), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(emailUser2), "Incorrect number of messages not read");
        Assertions.assertEquals(1, messageBusiness.verifyMessages(emailUser3), "Incorrect number of messages not read");
        Assertions.assertEquals(0, messageBusiness.verifyMessages(adminEmail), "Incorrect number of messages not read");

        // verify number group messages
        Assertions.assertEquals(0, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received"); // changed
    }

    @Test
    public void testCatchRemoveGroupMessage() throws BusinessException {
        // DELETE + nonExistent primary key groupId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        messageBusiness.removeGroupMessage(100); // inexisting group message id

        // Nothing changes

        // verify entry numbers in each table
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 1);
        assertRowsNbInTable("VIPUsers", 5);

        // verify message nb by user
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser2, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(1, messageBusiness.getMessagesByUser(emailUser3, getNextSecondDate()).size(), "Incorrect number of individual messages received");
        Assertions.assertEquals(0, messageBusiness.getMessagesByUser(emailUser4, getNextSecondDate()).size(), "Incorrect number of individual messages received");
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
    public void testGetGroupMessages() throws BusinessException {
        Assertions.assertEquals(1, messageBusiness.getGroupMessages(nameGroup1, getNextSecondDate()).size(), "Incorrect number of group messages received");
    }


    @Test
    public void testCatchGetNonExistentGroupMessages() throws BusinessException {
        // SELECT + nonExistent foreign key groupName => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        Assertions.assertEquals(0, messageBusiness.getGroupMessages("nonExistent group", getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    /* ******************************************************************************************************************************************************* */
    /* *********************************************************** get individual message by user ************************************************************ */
    /* ******************************************************************************************************************************************************* */


    @Test
    public void testGetSentMessageByUser() throws BusinessException {
        Assertions.assertEquals(0, messageBusiness.getSentMessagesByUser(emailUser1, getNextSecondDate()).size(), "Incorrect number of individual messages sent");
    }


    @Test
    public void testAdminGetSentMessageByUser() throws BusinessException {
        Assertions.assertEquals(1, messageBusiness.getSentMessagesByUser(adminEmail, getNextSecondDate()).size(), "Incorrect number of individual messages sent");
    }


    @Test
    public void testCatchGetSentMessageByUser() throws BusinessException {
        // SELECT + nonExistent foreign key sender email  => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        Assertions.assertEquals(0, messageBusiness.getSentMessagesByUser("nonExistent user", getNextSecondDate()).size(), "Incorrect number of group messages received");

    }

    /* ******************************************************************************************************************************************************* */
    /* ****************************************************************** verify messages ******************************************************************** */
    /* ******************************************************************************************************************************************************* */


    @Test
    public void testCatchIncorrectEmailVerifyMessages() throws BusinessException {
        // SELECT + nonExistent foreign key receiver  => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        messageBusiness.verifyMessages("nonExistent user");
    }


}
