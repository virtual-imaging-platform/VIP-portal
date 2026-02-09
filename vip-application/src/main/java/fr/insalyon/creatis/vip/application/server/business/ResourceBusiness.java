package fr.insalyon.creatis.vip.application.server.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.server.dao.ResourceDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class ResourceBusiness {

    private ResourceDAO resourceDAO;
    private EngineBusiness engineBusiness;
    private GroupBusiness groupBusiness;

    @Autowired
    public ResourceBusiness(ResourceDAO dao, EngineBusiness engineBusiness, GroupBusiness groupBusiness) {
        this.resourceDAO = dao;
        this.engineBusiness = engineBusiness;
        this.groupBusiness = groupBusiness;
    }

    public void add(Resource resource) throws VipException {
        try {
            resourceDAO.add(resource);

            for (String engineName : resource.getEngines()) {
                resourceDAO.associate(resource, new Engine(engineName));
            }
            for (String groupName : resource.getGroupsNames()) {
                resourceDAO.associate(resource, new Group(groupName));
            }
        } catch (DAOException e){
            throw new VipException(e);
        }
    }

    public void update(Resource resource) throws VipException {
        try {
            Resource before = getByName(resource.getName());
            List<String> beforeEnginesNames = before.getEngines();
            Set<String> beforeGroupsNames = before.getGroupsNames();

            resourceDAO.update(resource);
            for (String engine : resource.getEngines()) {
                if ( ! beforeEnginesNames.removeIf((s) -> s.equals(engine))) {
                    associate(resource, new Engine(engine));
                }
            }
            for (String group : resource.getGroupsNames()) {
                if ( ! beforeGroupsNames.removeIf((s) -> s.equals(group))) {
                    associate(resource, new Group(group));
                }
            }
            for (String engine : beforeEnginesNames) {
                dissociate(resource, new Engine(engine));
            }
            for (String group : beforeGroupsNames) {
                dissociate(resource, new Group(group));
            }
        } catch (DAOException e){
            throw new VipException(e);
        }
    }

    public void remove(Resource resource) throws VipException {
        try {
            resourceDAO.remove(resource);
        } catch (DAOException e){
            throw new VipException(e);
        }
    }

    public Resource getByName(String name) throws VipException {
        try {
            return mapAssociated(resourceDAO.getAll().stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst().get());
        } catch (DAOException | NoSuchElementException e){
            throw new VipException(e);
        }
    }

    public List<Resource> getAll() throws VipException {
        try {
            return mapAssociated(resourceDAO.getAll());
        } catch (DAOException e){
            throw new VipException(e);
        }
    }

    public List<Resource> getAll(boolean isPublic) throws VipException {
        return getAll()
            .stream()
            .collect(Collectors.toList());
    }

    public List<Resource> getActiveResources() throws VipException {
        return getAll()
            .stream()
            .filter(Resource::getStatus)
            .collect(Collectors.toList());
    }

    public List<Resource> getAvailableForUser(User user) throws VipException {
        try {
            if (user.isSystemAdministrator()) {
                return getAll()
                    .stream()
                    .filter(Resource::getStatus)
                    .collect(Collectors.toList());
            } else {
                return mapAssociated(resourceDAO.getByUser(user)
                    .stream()
                    .filter(Resource::getStatus)
                    .collect(Collectors.toList()));
            }
        } catch (DAOException e){
            throw new VipException(e);
        }
    }

    public List<Resource> getByGroup(Group group) throws VipException {
        try {
            return mapAssociated(resourceDAO.getByGroup(group));
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Resource> getByAppVersion(AppVersion appVersion) throws VipException {
        try {
            return mapAssociated(resourceDAO.getByAppVersion(appVersion));
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Resource> getByEngine(Engine engine) throws VipException {
        try {
            return mapAssociated(resourceDAO.getByEngine(engine));
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void associate(Resource resource, Group group) throws VipException {
        resource = getByName(resource.getName());
        group = groupBusiness.get(group.getName());

        try {
            resourceDAO.associate(resource, group);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void dissociate(Resource resource, Group group) throws VipException {
        try {
            resourceDAO.dissociate(resource, group);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void associate(Resource resource, AppVersion appVersion) throws VipException {
        try {
            if ( ! resourceDAO.getByAppVersion(appVersion).stream().filter((e) -> e.getName().equals(resource.getName())).findFirst().isPresent()) {
                resourceDAO.associate(resource, appVersion);
            }
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void dissociate(Resource resource, AppVersion appVersion) throws VipException {
        try {
            resourceDAO.dissociate(resource, appVersion);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void associate(Resource resource, Engine engine) throws VipException {
        try {
            if ( ! resourceDAO.getByEngine(engine).stream().filter((e) -> e.getName().equals(resource.getName())).findFirst().isPresent()) {
                resourceDAO.associate(resource, engine);
            }
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void dissociate(Resource resource, Engine engine) throws VipException {
        try {
            resourceDAO.dissociate(resource, engine);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Resource> getUsableResources(User user, AppVersion appVersion) throws VipException {
        Set<Resource> usableResource = new HashSet<>();
        List<String> userResources = getAvailableForUser(user)
            .stream()
            .map((r) -> r.getName())
            .collect(Collectors.toList());

        for (String r : userResources) {
            if (appVersion.getResourcesNames().contains(r)) {
                usableResource.add(getByName(r));
            }
        }

        return new ArrayList<>(usableResource);
    }

    private Resource mapAssociated(Resource resource) throws VipException {
        resource.setEngines(engineBusiness.getByResource(resource).stream().map((e) -> e.getName()).collect(Collectors.toList()));
        resource.setGroups(groupBusiness.getByResource(resource.getName()));

        return resource;
    }

    private List<Resource> mapAssociated(List<Resource> resources) throws VipException {
        for (Resource resource : resources) {
            resource = mapAssociated(resource);
        }
        return resources;
    }
}
