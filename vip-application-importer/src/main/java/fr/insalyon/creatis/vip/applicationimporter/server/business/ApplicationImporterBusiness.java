/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.applicationimporter.server.business;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

/**
 *
 * @author Tristan Glatard
 */
@Service
@Transactional
public class ApplicationImporterBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private LfcPathsBusiness lfcPathsBusiness;
    private GRIDAClient gridaClient;
    private BoutiquesBusiness boutiquesBusiness;
    private VelocityUtils velocityUtils;
    private TargzUtils targzUtils;
    private ApplicationBusiness applicationBusiness;
    private AppVersionBusiness appVersionBusiness;
    private DataManagerBusiness dataManagerBusiness;
    private ResourceBusiness resourceBusiness;
    private TagBusiness tagBusiness;

    @Autowired
    public ApplicationImporterBusiness(
            Server server, LfcPathsBusiness lfcPathsBusiness,
            GRIDAClient gridaClient, BoutiquesBusiness boutiquesBusiness,
            VelocityUtils velocityUtils, TargzUtils targzUtils,
            ApplicationBusiness applicationBusiness,
            DataManagerBusiness dataManagerBusiness,
            ResourceBusiness resourceBusiness,
            TagBusiness tagBusiness,
            AppVersionBusiness appVersionBusiness) {
        this.server = server;
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.gridaClient = gridaClient;
        this.boutiquesBusiness = boutiquesBusiness;
        this.velocityUtils = velocityUtils;
        this.targzUtils = targzUtils;
        this.applicationBusiness = applicationBusiness;
        this.dataManagerBusiness = dataManagerBusiness;
        this.resourceBusiness = resourceBusiness;
        this.tagBusiness = tagBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    public String readAndValidationBoutiquesFile(String fileLFN, User user)
            throws BusinessException {
        try {
            String localFilePath =
                    dataManagerBusiness.getRemoteFile(user, fileLFN);
            boutiquesBusiness.validateBoutiqueFile(localFilePath);
            return new Scanner(new File(localFilePath)).useDelimiter("\\Z").next();
        } catch (IOException ex) {
            logger.error("Error validating boutiques file {}", fileLFN, ex);
            throw new BusinessException(ex);
        }
    }

    public void createApplication(BoutiquesApplication bt, String tag, boolean overwriteApplicationVersion, String fileAccessProtocol
        ,List<String> tags, List<String> resources, User user)
            throws BusinessException {

        try {
            String wrapperTemplate = "vm/wrapper.vm";
            String gaswTemplate = "vm/gasw.vm";
            String gwendiaTemplate = "vm/gwendia-standalone.vm";

            // Check rights
            checkEditionRights(bt.getName(), bt.getToolVersion(), overwriteApplicationVersion, user);
            // set the correct LFN
            bt.setApplicationLFN(
                lfcPathsBusiness.parseBaseDir(
                    user, bt.getApplicationLFN()).concat("/").concat(bt.getToolVersion().replaceAll("\\s+","")));

            // Generate strings
            String gwendiaString = velocityUtils.createDocument(bt, fileAccessProtocol, gwendiaTemplate);
            String gaswString = velocityUtils.createDocument(tag, bt, fileAccessProtocol, gaswTemplate);
            String wrapperString = velocityUtils.createDocument(tag, bt, wrapperTemplate);

            // Write files
            String gwendiaFileName = server.getApplicationImporterFileRepository() + bt.getGwendiaLFN();
            String gaswFileName = server.getApplicationImporterFileRepository() + bt.getGASWLFN();
            String wrapperFileName = server.getApplicationImporterFileRepository() + bt.getWrapperLFN();

            System.out.print(gwendiaFileName + "\n");
            writeString(gwendiaString, gwendiaFileName);
            uploadFile(gwendiaFileName, bt.getGwendiaLFN());
            
            // Write application json descriptor
            String jsonFileName = server.getApplicationImporterFileRepository() + bt.getJsonLFN();
            writeString(bt.getJsonFile(), jsonFileName);         
  
            String wrapperArchiveName;
            // Write files for each GASW and script file
            writeString(gaswString, gaswFileName);
            writeString(wrapperString, wrapperFileName);
            wrapperArchiveName = wrapperFileName + ".tar.gz";

            ArrayList<File> dependencies = new ArrayList<File>();
            dependencies.add(new File(wrapperFileName));
            //Add json file to archive so that it is downloaded on WN for Boutiques exec
            dependencies.add(new File(jsonFileName));
            targzUtils.createTargz(dependencies, wrapperArchiveName);

            // Transfer files
            System.out.print("gasw : " + gaswFileName + "\n");
            System.out.print("gasw : " + bt.getGASWLFN() + "\n");
            uploadFile(gaswFileName, bt.getGASWLFN());
            System.out.print("wrapper : " + wrapperFileName + "\n");
            System.out.print("wrapper : " + bt.getWrapperLFN() + "\n");
            uploadFile(wrapperFileName, bt.getWrapperLFN());

            uploadFile(wrapperArchiveName, bt.getWrapperLFN() + ".tar.gz");
            //Upload the JSON file at the end, so that it is not deleted before adding it as dependency to wrapperArchiveName
            uploadFile(jsonFileName, bt.getJsonLFN());
        
            // Register application
            registerApplicationVersion(bt.getName(), bt.getToolVersion(), user.getEmail(), bt.getGwendiaLFN(), bt.getJsonLFN(), tags, resources);

        } catch (IOException ex) {
            logger.error("Error creating app {}/{} from boutiques file", bt.getName(), bt.getToolVersion(), ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    private void uploadFile(String localFile, String lfn) throws BusinessException {
        try {
            logger.info("Uploading file " + localFile + " to " + lfn);
            if (gridaClient.exist(lfn)) {
                gridaClient.delete(lfn);
            }
            gridaClient.uploadFile(localFile, (new File(lfn)).getParent());
        } catch (GRIDAClientException ex) {
            logger.error("Error uploading file {} to {}", localFile, lfn, ex);
            throw new BusinessException(ex);
        }
    }

    private void writeString(String string, String fileName) throws BusinessException, FileNotFoundException, UnsupportedEncodingException {
        // Check if base file directory exists, otherwise create it.
        File directory = (new File(fileName)).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            logger.error("Error importing an application : Cannot create directory {}", directory);
            throw new BusinessException("Cannot create directory " + directory.getAbsolutePath());
        }

        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.write(string);
        writer.close();
    }

    private void registerApplicationVersion(
            String vipApplicationName, String vipVersion, String owner,
            String lfnGwendiaFile, String lfnJsonFile,
            List<String> tags, List<String> resources) throws BusinessException {
        Application app = applicationBusiness.getApplication(vipApplicationName);
        AppVersion newVersion = new AppVersion(vipApplicationName, vipVersion, lfnGwendiaFile, lfnJsonFile, true, true);
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
        registerResourcesAssociated(newVersion, resources);
        registerTagsAssociated(newVersion, tags);
    }

    private void checkEditionRights(
            String vipApplicationName, String vipVersion, boolean overwrite,
            User user) throws BusinessException {

        Application app = applicationBusiness.getApplication(vipApplicationName);
        if (app == null) {
            return; // any user may create an application (nobody could run it unless an admin adds it to a class
        }
        // Only the owner of an existing application and a system administrator can modify it.
        if (!user.isSystemAdministrator() && !user.isDeveloper() && !app.getOwner().equals(user.getEmail())) {
            logger.error("{} tried to modify application {} which belongs to {}",
                    user.getEmail(), app.getName(), app.getOwner());
            throw new BusinessException("Permission denied.");
        }
        // Refuse to overwrite an application version silently if the version overwrite parameter is not set.
        if (!overwrite) {
            List<AppVersion> versions = appVersionBusiness.getVersions(vipApplicationName);
            for (AppVersion v : versions) {
                if (v.getVersion().equals(vipVersion)) {
                    logger.error("{} tried to overwrite version {} of application {} without setting the overwrite flag.",
                            user.getEmail(), vipVersion,vipApplicationName);
                    throw new BusinessException("Application version already exists.");
                }
            }
        }
    }

    private void registerTagsAssociated(AppVersion appVersion, List<String> tags) throws BusinessException {
        for (String tagName : tags) {
            if ( ! tagBusiness.exist(tagName)) {
                tagBusiness.add(new Tag(tagName));
            }
            tagBusiness.associate(new Tag(tagName), appVersion);
        }
    }

    private void registerResourcesAssociated(AppVersion appVersion, List<String> resources) throws BusinessException {
        for (String resourceName : resources) {
            resourceBusiness.associate(resourceBusiness.getByName(resourceName), appVersion);
        }
    }
}
