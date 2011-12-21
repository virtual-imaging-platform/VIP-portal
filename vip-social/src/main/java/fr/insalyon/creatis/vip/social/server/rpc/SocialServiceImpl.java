/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.social.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.view.SocialException;
import fr.insalyon.creatis.vip.social.server.business.MessageBusiness;
import java.util.Arrays;
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

    public List<Message> getMessagesByUser() throws SocialException {

        try {
            return messageBusiness.getMessagesByUser(getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }
    
    public List<Message> getSentMessagesByUser() throws SocialException {

        try {
            return messageBusiness.getSentMessagesByUser(getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
            throw new SocialException(ex);
        }
    }

    public void markMessageAsRead(long id) throws SocialException {

        try {
            messageBusiness.markAsRead(id);

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
            messageBusiness.sendMessage(getSessionUser().getEmail(), recipients,
                    subject, message);

        } catch (CoreException ex) {
            throw new SocialException(ex);
        } catch (BusinessException ex) {
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
