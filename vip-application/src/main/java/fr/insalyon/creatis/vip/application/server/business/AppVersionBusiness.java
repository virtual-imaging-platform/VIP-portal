package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class AppVersionBusiness {

    private TagBusiness tagBusiness;
    private ResourceBusiness resourceBusiness;
    private ApplicationDAO applicationDAO;

    @Autowired
    public AppVersionBusiness(TagBusiness tagBusiness, ResourceBusiness resourceBusiness, ApplicationDAO applicationDAO) {
        this.tagBusiness = tagBusiness;
        this.resourceBusiness = resourceBusiness;
        this.applicationDAO = applicationDAO;
    }

    public void add(AppVersion version) throws BusinessException {
        try {
            applicationDAO.addVersion(version);

            for (Tag tag : version.getTags()) {
                tag.setApplication(version.getApplicationName());
                tag.setVersion(version.getVersion());
                tagBusiness.addOrUpdate(tag);
            }
            for (Resource resource : version.getResources()) {
                resourceBusiness.associate(resource, version);
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void update(AppVersion version) throws BusinessException {
        try {
            AppVersion before = getVersion(version.getApplicationName(), version.getVersion());
            List<String> beforeResourceNames = before.getResourcesNames();
            List<Tag> editedTags = before.getTags();
            editedTags.removeAll(version.getTags());

            applicationDAO.updateVersion(version);
            for (Resource resource : version.getResources()) {
                if ( ! beforeResourceNames.removeIf((s) -> s.equals(resource.getName()))) {
                    resourceBusiness.associate(resource, version);
                }
            }
            for (Tag tag : editedTags) {
                tagBusiness.remove(tag);
            }
            for (Tag tag : version.getTags()) {
                tagBusiness.addOrUpdate(tag);
            }
            for (String resource : beforeResourceNames) {
                resourceBusiness.dissociate(new Resource(resource), version);
            }

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void remove(String applicationName, String version) throws BusinessException {
        try {
            applicationDAO.removeVersion(applicationName, version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateDoiForVersion(String doi, String applicationName, String version) throws BusinessException {
        try {
            applicationDAO.updateDoiForVersion(doi, applicationName, version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<AppVersion> getVersions(String applicationName) throws BusinessException {
        try {
            List<AppVersion> versions = applicationDAO.getVersions(applicationName);

            for (AppVersion version : versions) {
                version.setResources(resourceBusiness.getByAppVersion(version));
                version.setTags(tagBusiness.getTags(version));
            }
            return versions;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public AppVersion getVersion(String applicationName, String applicationVersion)
            throws BusinessException {
        try {
            AppVersion version = applicationDAO.getVersion(applicationName, applicationVersion);
            if (version == null) {
                return null;
            }

            version.setResources(resourceBusiness.getByAppVersion(version));
            version.setTags(tagBusiness.getTags(version));

            return version;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
