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
package fr.insalyon.creatis.vip.social.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.view.SocialException;
import fr.insalyon.creatis.vip.social.server.business.MessageBusiness;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class SocialServiceImpl extends AbstractRemoteServiceServlet implements SocialService {

    private static final Logger logger = Logger.getLogger(SocialServiceImpl.class);
    private MessageBusiness messageBusiness;

    public SocialServiceImpl() {

        messageBusiness = new MessageBusiness();
    }

    public List<Message> getMessagesByUser(Date startDate) throws SocialException {

        try {
            return messageBusiness.getMessagesByUser(getSessionUser().getEmail(), startDate);

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public List<Message> getSentMessagesByUser(Date startDate) throws SocialException {

        try {
            return messageBusiness.getSentMessagesByUser(getSessionUser().getEmail(), startDate);

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public List<GroupMessage> getGroupMessages(String groupName, Date startDate) throws SocialException {

        try {
            return messageBusiness.getGroupMessages(groupName, startDate);

        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void markMessageAsRead(long id, String receiver) throws SocialException {

        try {
            messageBusiness.markAsRead(id, receiver);

        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void removeMessage(long id) throws SocialException {

        try {
            messageBusiness.remove(id);

        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void removeMessageByReceiver(long id) throws SocialException {

        try {
            messageBusiness.removeByReceiver(id, getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void removeGroupMessage(long id) throws SocialException {

        try {
            messageBusiness.removeGroupMessage(id);

        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public List<User> getUsers() throws SocialException {

        try {
            if (isSystemAdministrator()) {
                return configurationBusiness.getUsers();
            }
            throw new SocialException("Only administrators can send message.");

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendMessage(String[] recipients, String subject, String message) throws SocialException {

        try {
            trace(logger, "Sending message '" + subject + "' to '" + Arrays.asList(recipients) + "'.");
            messageBusiness.sendMessage(getSessionUser(), recipients,
                    subject, message);

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendMessageWithSupportCopy(
        String[] recipients, String subject, String message)
        throws SocialException {

        try(Connection connection = PlatformConnection.getInstance().getConnection()) {
            trace(logger, "Sending message '" + subject + "' to '" + Arrays.asList(recipients) + "'.");
            messageBusiness.sendMessage(getSessionUser(), recipients,
                    subject, message);
            trace(logger, "Sending message '" + subject + "' to 'vip-support' as copy.");
            messageBusiness.copyMessageToVipSupport(
                getSessionUser(), recipients,
                subject, message, connection);
        } catch (CoreException | BusinessException | SQLException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendMessageToVipSupport(
        String subject, String message, List<String> workflowID,
        List<String> simulationNames)
        throws SocialException {

        try(Connection connection = PlatformConnection.getInstance().getConnection()) {
            trace(logger, "Sending message '" + subject + "' to 'vip-support'.");
            messageBusiness.sendMessageToVipSupport(
                getSessionUser(),
                subject, message, workflowID, simulationNames, connection);
        } catch (CoreException | BusinessException | SQLException ex) {
            throw new SocialException(ex);
        }
    }

    public void sendGroupMessage(
        String groupName, String subject, String message)
        throws SocialException {

        try(Connection connection = PlatformConnection.getInstance().getConnection()) {
            trace(logger, "Sending message '" + subject + "' to group '" + groupName + "'.");
            messageBusiness.sendGroupMessage(
                getSessionUser(),
                groupName,
                configurationBusiness.getUsersFromGroup(groupName, connection),
                subject,
                message);
        } catch (CoreException | BusinessException | SQLException ex) {
            throw new SocialException(ex);
        }
    }

    public int verifyMessages() throws SocialException {

        try {
            return messageBusiness.verifyMessages(getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }
}
