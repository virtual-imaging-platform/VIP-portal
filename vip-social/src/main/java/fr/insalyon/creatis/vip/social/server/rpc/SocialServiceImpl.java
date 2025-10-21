package fr.insalyon.creatis.vip.social.server.rpc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
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
    private ConfigurationBusiness configurationBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        messageBusiness = getBean(MessageBusiness.class);
        configurationBusiness = getBean(ConfigurationBusiness.class);
    }

    public List<Message> getMessagesByUser(Date startDate) throws SocialException {
        try {
            return messageBusiness.getMessagesByUser(
                getSessionUser().getEmail(), startDate);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public List<Message> getSentMessagesByUser(Date startDate) throws SocialException {
        try {
            return messageBusiness.getSentMessagesByUser(
                    getSessionUser().getEmail(), startDate);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public List<GroupMessage> getGroupMessages(String groupName, Date startDate)
            throws SocialException {
        try {
            return messageBusiness.getGroupMessages(groupName, startDate);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void markMessageAsRead(long id, String receiver) throws SocialException {
        try {
            messageBusiness.markAsRead(id, receiver);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void removeMessage(long id) throws SocialException {
        try {
            messageBusiness.remove(id);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void removeMessageByReceiver(long id) throws SocialException {
        try {
            messageBusiness.removeByReceiver(id, getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void removeGroupMessage(long id) throws SocialException {
        try {
            messageBusiness.removeGroupMessage(id);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public List<User> getUsers() throws SocialException {
        try {
            if (isSystemAdministrator()) {
                return configurationBusiness.getUsers();
            }
            logger.error("{} is not an admin, he cant access all users", getSessionUser());
            throw new SocialException("Only administrators can send message.");
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendMessage(String[] recipients, String subject, String message)
            throws SocialException {
        try {
            trace(logger, "Sending message '" + subject + "' to '" + Arrays.asList(recipients) + "'.");
            messageBusiness.sendMessage(
                getSessionUser(), recipients, subject, message);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendMessageWithSupportCopy(
            String[] recipients, String subject, String message)
            throws SocialException {

        try {
            trace(logger, "Sending message '" + subject + "' to '" + Arrays.asList(recipients) + "'.");
            messageBusiness.sendMessage(
                    getSessionUser(), recipients, subject, message);
            trace(logger, "Sending message '" + subject + "' to 'vip-support' as copy.");
            messageBusiness.copyMessageToVipSupport(
                    getSessionUser(), recipients, subject, message);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendMessageToVipSupport(
            String subject, String message, List<String> workflowID,
            List<String> simulationNames) throws SocialException {

        try{
            trace(logger, "Sending message '" + subject + "' to 'vip-support'.");
            messageBusiness.sendMessageToVipSupport(
                getSessionUser(),
                subject, message, workflowID, simulationNames);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendGroupMessage(String groupName, String subject, String message)
            throws SocialException {

        try {
            trace(logger, "Sending message '" + subject + "' to group '" + groupName + "'.");
            messageBusiness.sendGroupMessage(
                getSessionUser(),
                groupName,
                configurationBusiness.getUsersFromGroup(groupName),
                subject,
                message);
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }

    public int verifyMessages() throws SocialException {
        try {
            return messageBusiness.verifyMessages(
                getSessionUser().getEmail());
        } catch (VipException ex) {
            throw new SocialException(ex);
        }
    }
}
