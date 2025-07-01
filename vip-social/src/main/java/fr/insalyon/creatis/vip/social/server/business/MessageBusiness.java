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
package fr.insalyon.creatis.vip.social.server.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.server.dao.GroupMessageDAO;
import fr.insalyon.creatis.vip.social.server.dao.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Transactional
public class MessageBusiness {

    private MessageDAO messageDAO;
    private GroupMessageDAO groupMessageDAO;
    private UsersGroupsDAO usersGroupsDAO;
    private ConfigurationBusiness configurationBusiness;
    private EmailBusiness emailBusiness;

    @Autowired
    public MessageBusiness(
            MessageDAO messageDAO, GroupMessageDAO groupMessageDAO,
            UsersGroupsDAO usersGroupsDAO, ConfigurationBusiness configurationBusiness, EmailBusiness emailBusiness) {
        this.messageDAO = messageDAO;
        this.groupMessageDAO = groupMessageDAO;
        this.usersGroupsDAO = usersGroupsDAO;
        this.configurationBusiness = configurationBusiness;
        this.emailBusiness = emailBusiness;
    }

    public List<Message> getMessagesByUser(String email, Date startDate)
            throws BusinessException {

        try {
            return messageDAO.getMessagesByUser(
                    email, SocialConstants.MESSAGE_MAX_DISPLAY, startDate);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Message> getSentMessagesByUser(String email, Date startDate)
            throws BusinessException {

        try {
            return messageDAO.getSentMessagesByUser(
                    email, SocialConstants.MESSAGE_MAX_DISPLAY, startDate);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<GroupMessage> getGroupMessages(String groupName, Date startDate)
            throws BusinessException {

        try {
            return groupMessageDAO.getMessageByGroup(
                    groupName, SocialConstants.MESSAGE_MAX_DISPLAY, startDate);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void markAsRead(long id, String receiver) throws BusinessException {
        try {
            messageDAO.markAsRead(id, receiver);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void remove(long id) throws BusinessException {
        try {
            messageDAO.remove(id);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeByReceiver(long id, String receiver) throws BusinessException {
        try {
            messageDAO.removeByReceiver(id, receiver);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeGroupMessage(long id) throws BusinessException {
        try {
           groupMessageDAO.remove(id);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendMessage(
            User user, String[] recipients, String subject, String message)
            throws BusinessException {

        try {
            if (recipients[0].equals("All")) {
                List<String> users = new ArrayList<>();
                for (User u : configurationBusiness.getUsers()) {
                    // Dont send mail to locked users
                    if (!u.isAccountLocked()) {
                        users.add(u.getEmail());
                    }
                }
                recipients = users.toArray(new String[]{});
            }

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Hello,</p>"
                    + "<p><b>" + user.getFullName() + "</b> sent you a message on VIP:</p>"
                    + "<div style=\"background-color: #F2F2F2\">"
                    + "<br /><b>Subject:</b> " + subject + "<br />"
                    + "<em>" + message + "</em><br /></div>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            for (String email : recipients) {
                emailBusiness.sendEmail("VIP Message: " + subject + " (" + user.getFullName() + ")",
                        emailContent, new String[]{email}, true, user.getEmail());
            }

            long messageId = messageDAO.add(user.getEmail(), subject, message);

            for (String recipient : recipients) {
                messageDAO.associateMessageToUser(recipient, messageId);
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void copyMessageToVipSupport(
            User sender, String[] recipients, String subject, String message)
            throws BusinessException {

        String emailContent = "<html>"
                + "<head></head>"
                + "<body>"
                + "<p><b>" + sender.getFullName() + "</b> sent a message to <b>"
                + Arrays.asList(recipients) + "</b> on VIP:</p>"
                + "<div style=\"background-color: #F2F2F2\">"
                + "<br /><b>Subject:</b> " + subject + "<br />"
                + "<em>" + message + "</em><br /></div>"
                + "</body>"
                + "</html>";

        // if there is only one receiver, name it in subject, otherwise name the sender
        String subjectInfo = recipients.length == 1 ?
                "to " + recipients[0] : "from " + sender.getFullName();

        emailBusiness.sendEmailToAdmins(
            "[VIP Support Copy] " + subject + "(" + subjectInfo + ")",
            emailContent, true, sender.getEmail());
    }

    public void sendMessageToVipSupport(
            User user, String subject, String message, List<String> workflowIDs,
            List<String> simulationNames) throws BusinessException {

        String emailContent = "<html>"
                + "<head></head>"
                + "<body>"
                + "<p><b>" + user.getFullName() + "</b> sent you a message on VIP:</p>"
                + "<div style=\"background-color: #F2F2F2\">"
                + "<br /><b>Subject:</b> " + subject + "<br />"
                + "<em>" + message + "</em><br /></div>"
                + "<p>Workflow ID " + workflowIDs + "</p>"
                + "<p>Simulation Name " + simulationNames + "</p>"
                + "</body>"
                + "</html>";

        emailBusiness.sendEmailToAdmins(
            "[VIP Contact] " + subject + " (" + user.getFullName() + ")",
            emailContent, true, user.getEmail());
    }

    public void sendGroupMessage(
            User user, String groupName, List<User> users, String subject,
            String message) throws BusinessException {

        try {
            groupMessageDAO.add(user.getEmail(), groupName, subject, message);

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Hello,</p>"
                    + "<p><b>" + user.getFullName() + "</b> sent a message to "
                    + "the group '" + groupName + "' on VIP:</p>"
                    + "<p style=\"background-color: #F2F2F2\"><br />"
                    + "<b>Subject:</b> " + subject + "<br />"
                    + "<em>" + message + "</em><br /></p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            for (User u : users) {
                // Dont send mail to locked users and to itself
                if (!u.isAccountLocked() &&
                        !u.getEmail().equals(user.getEmail())) {
                    emailBusiness.sendEmail("VIP Message: " + subject + " (" + groupName + ")",
                            emailContent, new String[]{u.getEmail()}, true, user.getEmail());
                }
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public int verifyMessages(String email) throws BusinessException {

        try {
            return messageDAO.verifyMessages(email);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
