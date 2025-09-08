package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;

import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public interface GroupDAO {

    public void add(Group group) throws DAOException;

    public void remove(String groupName) throws DAOException;

    public void update(String name, Group group) throws DAOException;

    public List<Group> get() throws DAOException;

    public List<Group> getByType(GroupType type) throws DAOException;

    public List<Group> getByApplication(String applicationName) throws DAOException;

    public List<Group> getByRessource(String ressourceName) throws DAOException;
}
