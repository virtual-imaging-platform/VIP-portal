package fr.insalyon.creatis.vip.social.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import java.util.Date;
import java.util.List;

public interface GroupMessageDAO {
    
    public long add(String sender, String groupName, String title, String message) throws DAOException;
    
    public void remove(long id) throws DAOException;
    
    public List<GroupMessage> getMessageByGroup(String groupName, int limit, Date startDate) throws DAOException;
}
