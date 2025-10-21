package fr.insalyon.creatis.vip.social.server.dao;

import java.util.Date;
import java.util.List;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.social.models.Message;

public interface MessageDAO {
    
    public long add(String sender, String title, String message) throws DAOException;
    
    public void associateMessageToUser(String receiver, long messageId) throws DAOException;
            
    public List<Message> getMessagesByUser(String email, int limit, Date startDate) throws DAOException;
    
    public List<Message> getSentMessagesByUser(String email, int limit, Date startDate) throws DAOException;
    
    public void markAsRead(long id, String receiver) throws DAOException;
       
    public void remove(long id) throws DAOException;
    
    public void removeByReceiver(long id, String receiver) throws DAOException;
    
    public int verifyMessages(String email) throws DAOException;
}
