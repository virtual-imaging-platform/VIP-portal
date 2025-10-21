package fr.insalyon.creatis.vip.social.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.social.client.view.SocialException;
import fr.insalyon.creatis.vip.social.models.GroupMessage;
import fr.insalyon.creatis.vip.social.models.Message;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public interface SocialService extends RemoteService {

    public static final String SERVICE_URI = "/socialservice";

    public static class Util {

        public static SocialServiceAsync getInstance() {

            SocialServiceAsync instance = (SocialServiceAsync) GWT.create(SocialService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }
    
    public List<Message> getMessagesByUser(Date startDate) throws SocialException;
    
    public List<Message> getSentMessagesByUser(Date startDate) throws SocialException;
    
    public List<GroupMessage> getGroupMessages(String groupName, Date startDate) throws SocialException;
    
    public void markMessageAsRead(long id, String receiver) throws SocialException;
    
    public void removeMessage(long id) throws SocialException;
    
    public void removeMessageByReceiver(long id) throws SocialException;
    
    public void removeGroupMessage(long id) throws SocialException;
    
    public List<User> getUsers() throws SocialException;
    
    public void sendMessage(String[] recipients, String subject, String message) throws SocialException;

    public void sendMessageWithSupportCopy(String[] recipients, String subject, String message) throws SocialException;

    public void sendMessageToVipSupport( String subject, String message,List<String> workflowID,List<String> simulationNames)throws SocialException;
    
    public void sendGroupMessage(String groupName, String subject, String message) throws SocialException;
    
    public int verifyMessages() throws SocialException;
}
