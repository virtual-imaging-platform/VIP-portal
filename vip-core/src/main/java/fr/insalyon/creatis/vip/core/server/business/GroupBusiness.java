package fr.insalyon.creatis.vip.core.server.business;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;

@Service
@Transactional
public class GroupBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private GroupDAO groupDAO;
    private GRIDAClient gridaClient;
    private GRIDAPoolClient gridaPoolClient;

    @Autowired
    public GroupBusiness(GroupDAO groupDAO, GRIDAClient gridaClient, Server server, 
            GRIDAPoolClient gridaPoolClient) {
        this.groupDAO = groupDAO;
        this.gridaClient = gridaClient;
        this.server = server;
        this.gridaPoolClient = gridaPoolClient;
    }

    public void add(Group group) throws BusinessException {
        try {
            gridaClient.createFolder(server.getDataManagerGroupsHome(),
                    group.getName().replaceAll(" ", "_"));

            groupDAO.add(group);
        } catch (GRIDAClientException ex) {
            logger.error("Error adding group : {}", group.getName(), ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void remove(String user, String groupName)
            throws BusinessException {
        try {
            gridaPoolClient.delete(server.getDataManagerGroupsHome() + "/"
                    + groupName.replaceAll(" ", "_"), user);
            groupDAO.remove(groupName);
        } catch (GRIDAClientException ex) {
            logger.error("Error removing group : {}", groupName, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void update(String name, Group group) throws BusinessException {
        try {
            if (!name.equals(group.getName())) {
                gridaClient.rename(
                        server.getDataManagerGroupsHome() + "/" + name.replaceAll(" ", "_"),
                        server.getDataManagerGroupsHome() + "/" + group.getName().replaceAll(" ", "_"));
            }
            groupDAO.update(name, group);
        } catch (GRIDAClientException ex) {
            logger.error("Error updating group : {}", name, ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Group> get() throws BusinessException {
        try {
            return groupDAO.get();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Group get(String groupName) throws BusinessException {
        if (groupName == null) {
            return null;
        }
        return get().stream()
                .filter(g -> groupName.equals(g.getName()))
                .findAny().orElse(null);
    }

    public List<Group> getPublic() throws BusinessException {
        return get().stream()
            .filter((g) -> g.isPublicGroup())
            .collect(Collectors.toList());
    }

    public List<Group> getByType(GroupType type) throws BusinessException {
        try {
            return groupDAO.getByType(type);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<Group> getByApplication(String appName) throws BusinessException {
        try {
            return groupDAO.getByApplication(appName);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<Group> getByResource(String ressourceName) throws BusinessException {
        try {
            return groupDAO.getByRessource(ressourceName);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }
}
