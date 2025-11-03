package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public interface UsersGroupsDAO {

    public void add(String email, String name, CoreConstants.GROUP_ROLE role) throws DAOException;

    public Map<Group, CoreConstants.GROUP_ROLE> getUserGroups(String email) throws DAOException;
    
    public List<String> getUserAdminGroups(String email) throws DAOException;
    
    public void setUserGroups(String email, Map<String, CoreConstants.GROUP_ROLE> groups) throws DAOException;
    
    public List<String> getUsersFromGroups(List<String> groups) throws DAOException;
    
    public List<User> getUsersFromGroup(String groupName) throws DAOException;
    
    public void removeUserFromGroup(String email, String groupName) throws DAOException;

    public List<Boolean> getUserPropertiesGroups(String email)throws DAOException;
}
