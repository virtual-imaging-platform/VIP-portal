package fr.insalyon.creatis.vip.social.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.client.view.SocialException;
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

    public void sendMessageWithSupportCopy(String[] recipients, String subject, String message, AsyncCallback<Void> async);

    public void sendMessageToVipSupport(String subject, String message, List<String> workflowID, List<String> simulationNames, AsyncCallback<Void> asyncCallback);

    public void sendGroupMessage(String groupName, String subject, String message, AsyncCallback<Void> asyncCallback);

    public void verifyMessages(AsyncCallback<Integer> asyncCallback);
}
