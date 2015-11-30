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
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesTool;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Tristan Glatard
 */
public class ApplicationImporterBusiness {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ApplicationImporterBusiness.class);

    public String readFileAsString(String fileLFN, User user) throws BusinessException {
        try {
            File localDir = new File(Server.getInstance().getApplicationImporterFileRepository()+"/"+(new File(DataManagerUtil.parseBaseDir(user, fileLFN))).getParent());
            if(!localDir.exists() && !localDir.mkdirs()){
                throw new BusinessException("Cannot create directory "+localDir.getCanonicalPath());
            }
            String localFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(user, fileLFN), localDir.getCanonicalPath());
            String fileContent = new Scanner(new File(localFilePath)).useDelimiter("\\Z").next();
            return fileContent;
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void createApplication(BoutiquesTool bt, boolean overwriteApplicationVersion, User user) throws BusinessException {

        try {
            // Will enventually be taken from the Constants.
            String wrapperTemplate = "vm/wrapper.vm";
            String gaswTemplate = "vm/gasw.vm";
            String gwendiaTemplate = "vm/gwendia.vm";
          
            // Check rights
            checkEditionRights(bt.getName(), bt.getToolVersion(), overwriteApplicationVersion, user);
            bt.setApplicationLFN(DataManagerUtil.parseBaseDir(user, bt.getApplicationLFN()));

            // Generate strings
            String wrapperString = VelocityUtils.getInstance().createDocument(bt, wrapperTemplate);
            String gaswString = VelocityUtils.getInstance().createDocument(bt, gaswTemplate);
            String gwendiaString = VelocityUtils.getInstance().createDocument(bt, gwendiaTemplate);

            // Write files
            String wrapperFileName = Server.getInstance().getApplicationImporterFileRepository() + bt.getWrapperLFN();
            String wrapperArchiveName = wrapperFileName+".tar.gz";
            String gaswFileName = Server.getInstance().getApplicationImporterFileRepository() + bt.getGASWLFN();
            String gwendiaFileName = Server.getInstance().getApplicationImporterFileRepository() + bt.getGwendiaLFN();

            writeString(wrapperString, wrapperFileName);
            writeString(gaswString, gaswFileName);
            writeString(gwendiaString, gwendiaFileName);
            
            ArrayList<File> dependencies = new ArrayList<File>();
            dependencies.add(new File(wrapperFileName));
            TargzUtils.createTargz(dependencies, wrapperArchiveName);
 
            // Transfer files
            uploadFile(wrapperFileName,bt.getWrapperLFN());
            uploadFile(gaswFileName, bt.getGASWLFN());
            uploadFile(gwendiaFileName, bt.getGwendiaLFN());
            uploadFile(wrapperArchiveName, bt.getWrapperLFN()+".tar.gz");
            
            // Register application
            registerApplicationVersion(bt.getName(), bt.getToolVersion(), user.getEmail(), bt.getGwendiaLFN());

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    private void uploadFile(String localFile, String lfn) throws BusinessException{
        try {
            GRIDAClient gc = CoreUtil.getGRIDAClient();
            logger.info("Uploading file "+localFile+" to "+lfn);
            if(gc.exist(lfn))
                gc.delete(lfn);
            gc.uploadFile(localFile, (new File(lfn)).getParent());
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
    
    private void writeString(String string, String fileName) throws BusinessException, FileNotFoundException, UnsupportedEncodingException{
        // Check if base file directory exists, otherwise create it.
        File directory = (new File(fileName)).getParentFile();
        if(!directory.exists() && !directory.mkdirs())
            throw new BusinessException("Cannot create directory "+directory.getAbsolutePath());
        
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.write(string);
        writer.close();
    }
    
    private void registerApplicationVersion(String vipApplicationName, String vipVersion, String owner, String lfnGwendiaFile) throws BusinessException {
        ApplicationBusiness ab = new ApplicationBusiness();
        Application app = ab.getApplication(vipApplicationName);
        AppVersion newVersion = new AppVersion(vipApplicationName, vipVersion, lfnGwendiaFile, true);
        if (app == null) {
            // If application doesn't exist, create it.
            // New applications are not associated with any class (admins may add classes independently).
            ab.add(new Application(vipApplicationName, new ArrayList<String>(), owner, ""));
        }
        // If version exists, update it
        List<AppVersion> versions = ab.getVersions(vipApplicationName);
        for (AppVersion existingVersion : versions) {
            if (existingVersion.getVersion().equals(newVersion.getVersion())) {
                ab.updateVersion(newVersion);
                return;
            }
        }
        // add new version
        ab.addVersion(newVersion);
    }

    private void checkEditionRights(String vipApplicationName, String vipVersion, boolean overwrite, User user) throws BusinessException {

        ApplicationBusiness ab = new ApplicationBusiness();
        Application app = ab.getApplication(vipApplicationName);
        if (app == null) {
            return; // any user may create an application (nobody could run it unless an admin adds it to a class
        }
        // Only the owner of an existing application and a system administrator can modify it.
        if (!user.isSystemAdministrator() && !app.getOwner().equals(user.getEmail())) {
            logger.error(user.getEmail() + " tried to modify application " + app.getName() + " which belongs to " + app.getOwner());
            throw new BusinessException("Permission denied.");
        }
        // Refuse to overwrite an application version silently if the version overwrite parameter is not set.
        if (!overwrite) {
            List<AppVersion> versions = ab.getVersions(vipApplicationName);
            for (AppVersion v : versions) {
                if (v.getVersion().equals(vipVersion)) {
                    logger.error(user.getEmail() + " tried to overwrite version " + vipVersion + " of application " + vipApplicationName + " without setting the overwrite flag.");
                    throw new BusinessException("Application version already exists.");
                }
            }
        }

    }
}
