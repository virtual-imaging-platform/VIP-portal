package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;
import java.util.stream.Collectors;

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

            for (String tagName : version.getTags()) {
                tagBusiness.associate(new Tag(tagName), version);
            }
            for (String resourceName: version.getResources()) {
                resourceBusiness.associate(new Resource(resourceName), version);
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void update(AppVersion version) throws BusinessException {
        try {
            AppVersion before = getVersion(version.getApplicationName(), version.getVersion());
            List<String> beforeResourceNames = before.getResources();
            List<String> beforeTagsNames = before.getTags();

            applicationDAO.updateVersion(version);
            for (String resource : version.getResources()) {
                if ( ! beforeResourceNames.removeIf((s) -> s.equals(resource))) {
                    resourceBusiness.associate(new Resource(resource), version);
                }
            }
            for (String tag : version.getTags()) {
                if ( ! beforeTagsNames.removeIf((s) -> s.equals(tag))) {
                    tagBusiness.associate(new Tag(tag), version);
                }
            }
            for (String resource : beforeResourceNames) {
                resourceBusiness.dissociate(new Resource(resource), version);
            }
            for (String tag : beforeTagsNames) {
                tagBusiness.dissociate(new Tag(tag), version);
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
                version.setResources(resourceBusiness.getByAppVersion(version).stream().map((e) -> e.getName()).collect(Collectors.toList()));
                version.setTags(tagBusiness.getTags(version).stream().map((e) -> e.getName()).collect(Collectors.toList()));
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

            version.setResources(resourceBusiness.getByAppVersion(version).stream().map((e) -> e.getName()).collect(Collectors.toList()));
            version.setTags(tagBusiness.getTags(version).stream().map((e) -> e.getName()).collect(Collectors.toList()));

            return version;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
