package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.server.dao.ResourceDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class ResourceBusiness {

    private ResourceDAO resourceDAO;

    @Autowired
    public ResourceBusiness(ResourceDAO dao) {
        this.resourceDAO = dao;
    }

    public void add(Resource resource) throws BusinessException {
        try {
            resourceDAO.add(resource);
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public void update(Resource resource) throws BusinessException {
        try {
            resourceDAO.update(resource);
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public void remove(Resource resource) throws BusinessException {
        try {
            resourceDAO.remove(resource);
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public Resource getByName(String name) throws BusinessException {
        try {
            return resourceDAO.getAll().stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst().get();
        } catch (DAOException | NoSuchElementException e){
            throw new BusinessException(e);
        }
    }

    public List<Resource> getAll() throws BusinessException {
        try {
            return resourceDAO.getAll();
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public List<Resource> getAll(boolean visible) throws BusinessException {
        try {
            return resourceDAO.getAll()
                .stream()
                .filter(r -> r.isVisible() == visible)
                .collect(Collectors.toList());
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public List<Resource> getActiveResources() throws BusinessException {
        try {
            return resourceDAO.getAll()
                .stream()
                .filter(Resource::getStatus)
                .collect(Collectors.toList());
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public List<Resource> getAvailableForUser(User user) throws BusinessException {
        try {
            return resourceDAO.getByUser(user)
                .stream()
                .filter(Resource::getStatus)
                .collect(Collectors.toList());
        } catch (DAOException e){
            throw new BusinessException(e);
        }
    }

    public List<Resource> getByGroup(Group group) throws BusinessException {
        try {
            return resourceDAO.getByGroup(group);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<Resource> getByAppVersion(AppVersion appVersion) throws BusinessException {
        try {
            return resourceDAO.getByAppVersion(appVersion);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<Resource> getByEngine(Engine engine) throws BusinessException {
        try {
            return resourceDAO.getByEngine(engine);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void putInGroup(Resource resource, Group group) throws BusinessException {
        try {
            resourceDAO.putInGroup(resource, group);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void removeFromGroup(Resource resource, Group group) throws BusinessException {
        try {
            resourceDAO.removeFromGroup(resource, group);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void associate(Resource resource, AppVersion appVersion) throws BusinessException {
        try {
            if ( ! resourceDAO.getByAppVersion(appVersion).stream().filter((e) -> e.getName().equals(resource.getName())).findFirst().isPresent()) {
                resourceDAO.associate(resource, appVersion);
            }
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void associate(List<Resource> resources, AppVersion appVersion) throws BusinessException {
        for (Resource resource : resources) {
            associate(resource, appVersion);
        }
    }

    public void dissociate(Resource resource, AppVersion appVersion) throws BusinessException {
        try {
            resourceDAO.dissociate(resource, appVersion);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }
}
