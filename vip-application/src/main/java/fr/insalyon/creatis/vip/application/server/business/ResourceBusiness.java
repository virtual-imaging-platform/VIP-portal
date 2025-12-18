package fr.insalyon.creatis.vip.application.server.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.server.dao.ResourceDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

@Service
@Transactional
public class ResourceBusiness extends CommonBusiness {

    private ResourceDAO resourceDAO;
    private EngineBusiness engineBusiness;
    private GroupBusiness groupBusiness;

    @Autowired
    public ResourceBusiness(ResourceDAO dao, EngineBusiness engineBusiness, GroupBusiness groupBusiness) {
        this.resourceDAO = dao;
        this.engineBusiness = engineBusiness;
        this.groupBusiness = groupBusiness;
    }

    // the resource<list> here is not safe to be returned to the user
    // this SHOULD be only for server-side context
    // use `getUserContextResources` if you want to return data to the user
    public List<Resource> getAvailableForExecution(User user) throws VipException {
        List<Resource> result;
        try {
            if (user.isSystemAdministrator()) {
                result = getAll();
            } else {
                result = mapAssociated(resourceDAO.getByUser(user));
            }
            return result
                .stream()
                .filter(Resource::getStatus)
                .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Resource> getAvailableForExecution(User user, AppVersion appVersion) throws VipException {
        List<String> resourcesName = appVersion.getResourcesNames();

        return getAvailableForExecution(user).stream()
                .filter((resource) -> resourcesName.contains(resource.getName()))
                .distinct()
                .toList();
    }

    @VIPExternalSafe
    public Resource get(String name) throws VipException {
        return getUserContextResources().stream().filter(
                (resource) -> resource.getName().equals(name))
                .findFirst().orElse(null);
    }

    @VIPExternalSafe
    public PrecisePage<Resource> get(int offset, int quantity, String group) throws VipException {
        List<Resource> context = getUserContextResources();

        if (group != null) {
            context = context.stream().filter((r) -> r.getGroupsNames().contains(group)).toList();
        }

        return pageBuilder.doPrecise(offset, quantity, context);
    }

    // use this method for returning data to the user
    // prefer use `getAvailableForExecution` for workflow management
    // this method applies more filters
    @VIPExternalSafe
    public List<Resource> getUserContextResources() throws VipException {
        User user = userSupplier.get();
        List<Resource> resources;

        if (user.isSystemAdministrator()) {
            return getAll();
        } else if (user.isDeveloper()) {
            resources = mapAssociated(resourceDAO.getByUser(user));
            // developers can only see resources that only belongs to private groups
            // developers can't see engines!
            return resources.stream()
                .filter((r) -> r.getGroups().stream().noneMatch(Group::isPublicGroup))
                .peek((r) -> r.setEngines(null))
                .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @VIPExternalSafe
    public void add(Resource resource) throws VipException {
        permissions.checkLevel(UserLevel.Administrator);

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

    @VIPExternalSafe
    public void update(Resource resource) throws VipException {
        permissions.checkLevel(UserLevel.Administrator);

        try {
            Resource before = mapAssociated(resourceDAO.getByName(resource.getName()));
            List<String> beforeEnginesNames = before.getEngines();
            List<String> beforeGroupsNames = before.getGroupsNames();

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

    @VIPExternalSafe
    public void remove(Resource resource) throws VipException {
        permissions.checkLevel(UserLevel.Administrator);

        try {
            resourceDAO.remove(resource);
        } catch (DAOException e){
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

    private Resource mapAssociated(Resource resource) throws VipException {
        // engines should be filtered/cleared, if returning data to user
        resource.setEngines(engineBusiness.getByResource(resource).stream().map((e) -> e.getName()).collect(Collectors.toList()));

        // filter groups to avoid permissions leaks
        resource.setGroups(
            permissions.filterOnlyUserGroups(groupBusiness.getByResource(resource.getName())));

        return resource;
    }

    private List<Resource> mapAssociated(List<Resource> resources) throws VipException {
        for (Resource resource : resources) {
            resource = mapAssociated(resource);
        }
        return resources;
    }
}
