package fr.insalyon.creatis.vip.applicationimporter.server.business;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;

@Service
@Transactional
public class ApplicationImporterBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BoutiquesBusiness boutiquesBusiness;
    private ApplicationBusiness applicationBusiness;
    private AppVersionBusiness appVersionBusiness;
    private DataManagerBusiness dataManagerBusiness;

    @Autowired
    public ApplicationImporterBusiness(
            BoutiquesBusiness boutiquesBusiness,
            ApplicationBusiness applicationBusiness,
            DataManagerBusiness dataManagerBusiness,
            AppVersionBusiness appVersionBusiness) {
        this.boutiquesBusiness = boutiquesBusiness;
        this.applicationBusiness = applicationBusiness;
        this.dataManagerBusiness = dataManagerBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    public String readAndValidationBoutiquesFile(String fileLFN, User user)
            throws VipException {
        try {
            String localFilePath =
                    dataManagerBusiness.getRemoteFile(user, fileLFN);
            boutiquesBusiness.validateBoutiqueFile(localFilePath);
            return new Scanner(new File(localFilePath)).useDelimiter("\\Z").next();
        } catch (IOException ex) {
            logger.error("Error validating boutiques file {}", fileLFN, ex);
            throw new VipException(ex);
        }
    }

    public void createApplication(BoutiquesApplication bt, boolean overwriteApplicationVersion,
            List<Tag> tags, List<String> resources, User user)
            throws VipException {

        // Check rights
        checkEditionRights(bt.getName(), bt.getToolVersion(), overwriteApplicationVersion, user);

        // Register application
        registerApplicationVersion(bt.getName(), bt.getToolVersion(), user.getEmail(), bt.getOriginalDescriptor(), tags, resources);
    }

    private void registerApplicationVersion(
            String vipApplicationName, String vipVersion, String owner, String descriptor,
            List<Tag> tags, List<String> resources) throws VipException {
        Application app = applicationBusiness.getApplication(vipApplicationName);
        AppVersion newVersion = new AppVersion(vipApplicationName, vipVersion, descriptor, true);

        newVersion.setResources(resources.stream().map(Resource::new).toList());
        newVersion.setTags(tags);
        if (app == null) {
            // If application doesn't exist, create it.
            // New applications are not associated with any class (admins may add classes independently).
            applicationBusiness.add(new Application(vipApplicationName, owner));
        }
        // If version exists, update it
        List<AppVersion> versions = appVersionBusiness.getVersions(vipApplicationName);
        for (AppVersion existingVersion : versions) {
            if (existingVersion.getVersion().equals(newVersion.getVersion())) {
                appVersionBusiness.update(newVersion);
                return;
            }
        }
        // add new version
        appVersionBusiness.add(newVersion);
    }

    private void checkEditionRights(
            String vipApplicationName, String vipVersion, boolean overwrite,
            User user) throws VipException {

        Application app = applicationBusiness.getApplication(vipApplicationName);
        if (app == null) {
            return; // any user may create an application (nobody could run it unless an admin adds it to a class
        }
        // Only the owner of an existing application and a system administrator can modify it.
        if (!user.isSystemAdministrator() && !user.isDeveloper() && !app.getOwner().equals(user.getEmail())) {
            logger.error("{} tried to modify application {} which belongs to {}",
                    user.getEmail(), app.getName(), app.getOwner());
            throw new VipException("Permission denied.");
        }
        // Refuse to overwrite an application version silently if the version overwrite parameter is not set.
        if (!overwrite) {
            List<AppVersion> versions = appVersionBusiness.getVersions(vipApplicationName);
            for (AppVersion v : versions) {
                if (v.getVersion().equals(vipVersion)) {
                    logger.error("{} tried to overwrite version {} of application {} without setting the overwrite flag.",
                            user.getEmail(), vipVersion,vipApplicationName);
                    throw new VipException("Application version already exists.");
                }
            }
        }
    }
}
