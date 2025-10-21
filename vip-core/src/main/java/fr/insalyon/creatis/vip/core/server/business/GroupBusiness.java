package fr.insalyon.creatis.vip.core.server.business;

import java.util.ArrayList;
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
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
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

    public void add(Group group) throws VipException {
        try {
            checkAuto(group);
            gridaClient.createFolder(server.getDataManagerGroupsHome(),
            group.getName().replaceAll(" ", "_"));
            
            groupDAO.add(group);
        } catch (GRIDAClientException ex) {
            logger.error("Error adding group : {}", group.getName(), ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void remove(String user, String groupName)
            throws VipException {
        try {
            gridaPoolClient.delete(server.getDataManagerGroupsHome() + "/"
                    + groupName.replaceAll(" ", "_"), user);
            groupDAO.remove(groupName);
        } catch (GRIDAClientException ex) {
            logger.error("Error removing group : {}", groupName, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void update(String name, Group group) throws VipException {
        try {
            checkAuto(group);
            if ( ! name.equals(group.getName())) {
                gridaClient.rename(
                        server.getDataManagerGroupsHome() + "/" + name.replaceAll(" ", "_"),
                        server.getDataManagerGroupsHome() + "/" + group.getName().replaceAll(" ", "_"));
            }
            groupDAO.update(name, group);
        } catch (GRIDAClientException ex) {
            logger.error("Error updating group : {}", name, ex);
            throw new VipException(ex);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Group> get() throws VipException {
        try {
            return groupDAO.get();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public Group get(String groupName) throws VipException {
        if (groupName == null) {
            return null;
        }
        return get().stream()
                .filter(g -> groupName.equals(g.getName()))
                .findAny().orElse(null);
    }

    public List<Group> getPublic() throws VipException {
        return get().stream()
            .filter((g) -> g.isPublicGroup())
            .collect(Collectors.toList());
    }

    public List<Group> getByType(GroupType type) throws VipException {
        try {
            return groupDAO.getByType(type);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Group> getByApplication(String appName) throws VipException {
        try {
            return groupDAO.getByApplication(appName);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Group> getByResource(String ressourceName) throws VipException {
        try {
            return groupDAO.getByRessource(ressourceName);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void checkAuto(Group group) throws VipException {
        if (group.isAuto()) {
            Group existing = getByType(group.getType()).stream().filter((g) -> g.isAuto()).findFirst().orElse(null);

            if ( ! group.isPublicGroup()) {
                throw new VipException("You can only create public auto groups!");
            } else if (existing != null && ! existing.getName().equals(group.getName())) {
                throw new VipException("You can't have multiples auto groups of the same type!");
            }
        }
    }

    public String getWarningSameVisibility(List<String> groupNames) throws VipException {
        List<Group> groups = new ArrayList<>();

        for (String name : groupNames) {
            groups.add(get(name));
        }

        if (groups.stream().map(Group::isPublicGroup).toList().stream().distinct().count() > 1) {
            return "Be careful: the groups that you have chosen do not have the same visibility!";
        } else {
            return null;
        }
    }
}