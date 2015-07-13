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
package fr.insalyon.creatis.vip.social.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public interface SocialServiceAsync {

    public void getMessagesByUser(Date startDate, AsyncCallback<List<Message>> asyncCallback);
    
    public void getSentMessagesByUser(Date startDate, AsyncCallback<List<Message>> asyncCallback);
    
    public void getGroupMessages(String groupName, Date startDate, AsyncCallback<List<GroupMessage>> asyncCallback);
    
    public void markMessageAsRead(long id, String receiver, AsyncCallback<Void> asyncCallback);
    
    public void removeMessage(long id, AsyncCallback<Void> asyncCallback);
    
    public void removeMessageByReceiver(long id, AsyncCallback<Void> asyncCallback);
    
    public void removeGroupMessage(long id, AsyncCallback<Void> asyncCallback);
    
    public void getUsers(AsyncCallback<List<User>> asyncCallback);
    
    public void sendMessage(String[] recipients, String subject, String message, AsyncCallback<Void> asyncCallback);
    
    public void sendGroupMessage(String groupName, String subject, String message, AsyncCallback<Void> asyncCallback);
    
    public void verifyMessages(AsyncCallback<Integer> asyncCallback);
}
