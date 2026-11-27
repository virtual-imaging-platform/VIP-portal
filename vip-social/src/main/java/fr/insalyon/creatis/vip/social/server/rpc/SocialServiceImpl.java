package fr.insalyon.creatis.vip.social.server.rpc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.UserBusiness;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.view.SocialException;
import fr.insalyon.creatis.vip.social.models.GroupMessage;
import fr.insalyon.creatis.vip.social.models.Message;
import fr.insalyon.creatis.vip.social.server.business.MessageBusiness;
import jakarta.servlet.ServletException;

public class SocialServiceImpl extends AbstractRemoteServiceServlet implements SocialService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MessageBusiness messageBusiness;
    private UserBusiness userBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        messageBusiness = getBean(MessageBusiness.class);
        userBusiness = getBean(UserBusiness.class);
    }

    @Override
    public List<Message> getMessagesByUser(Date startDate) throws SocialException {
        try {
            return messageBusiness.getMessagesByUser(startDate);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public List<Message> getSentMessagesByUser(Date startDate) throws SocialException {
        try {
            return messageBusiness.getSentMessagesByUser(startDate);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public List<GroupMessage> getGroupMessages(String groupName, Date startDate)
            throws SocialException {
        try {
            return messageBusiness.getGroupMessages(groupName, startDate);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void markMessageAsRead(long id) throws SocialException {
        try {
            messageBusiness.markAsRead(id);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void removeMessage(long id) throws SocialException {
        try {
            messageBusiness.remove(id);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void removeMessageByReceiver(long id) throws SocialException {
        try {
            messageBusiness.removeByReceiver(id);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void removeGroupMessage(long id) throws SocialException {
        try {
            messageBusiness.removeGroupMessage(id);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public List<User> getUsers() throws SocialException {
        try {
            if (isSystemAdministrator()) {
                return userBusiness.getUsers();
            }
            logger.error("{} is not an admin, he cant access all users", getSessionUser());
            throw new SocialException("Only administrators can send message.");
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void sendMessage(String[] recipients, String subject, String message)
            throws SocialException {
        try {
            trace(logger, "Sending message '" + subject + "' to '" + Arrays.asList(recipients) + "'.");
            messageBusiness.sendMessage(recipients, subject, message);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void sendMessageWithSupportCopy(
            String[] recipients, String subject, String message)
            throws SocialException {

        try {
            trace(logger, "Sending message '" + subject + "' to '" + Arrays.asList(recipients) + "'.");
            messageBusiness.sendMessage(
                    recipients, subject, message);
            trace(logger, "Sending message '" + subject + "' to 'vip-support' as copy.");
            messageBusiness.copyMessageToVipSupport(
                    recipients, subject, message);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void sendMessageToVipSupport(
            String subject, String message, List<String> workflowID,
            List<String> simulationNames) throws SocialException {

        try{
            trace(logger, "Sending message '" + subject + "' to 'vip-support'.");
            messageBusiness.sendMessageToVipSupport(
                subject, message, workflowID, simulationNames);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public void sendGroupMessage(String groupName, String subject, String message)
            throws SocialException {

        try {
            trace(logger, "Sending message '" + subject + "' to group '" + groupName + "'.");
            messageBusiness.sendGroupMessage(
                groupName,
                subject,
                message);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    @Override
    public int verifyMessages() throws SocialException {
        try {
            return messageBusiness.verifyMessages(
                getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }
}
