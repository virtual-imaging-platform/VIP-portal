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

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 *
 * @author Tristan Glatard
 */
public class ApplicationImporterBusiness {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ApplicationImporterBusiness.class);

    public static String readFileAsString(String fileLFN, User user) throws ApplicationImporterException {
        try {
            String localFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(user, fileLFN), Server.getInstance().getApplicationImporterFileRepository());
            String fileContent = new Scanner(new File(localFilePath)).useDelimiter("\\Z").next();
            return fileContent;
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new ApplicationImporterException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new ApplicationImporterException(ex);
        } catch (FileNotFoundException ex) {
            logger.error(ex);
            throw new ApplicationImporterException(ex);
        }
    }

    public static void createApplication(String jsonString, String applicationLocation, String[] vipClasses, User user) throws ApplicationImporterException {
        throw new ApplicationImporterException("Method not implemented yet");
    }
    
//    private void generateScriptFile(String templateFolder, 
//                                    JSONObject jsonObject,
//                                    User user) throws ApplicationImporterException {
//        Velocity ve = new Velocity();
//        String applicationRealLocation;
//        try {
//            applicationRealLocation = DataManagerUtil.parseBaseDir(user, applicationLocation);
//            final File homeDir = new File(Server.getInstance().getApplicationImporterFileRepository());
//            File theDir = new File(homeDir, applicationName + "/" + user.getFolder() + "/" + generateTime);
//            theDir.mkdirs();
//            String dir = theDir.getAbsolutePath();
//            if (scriptFile != null) {
//                if (!scriptFile.isEmpty()) {
//                    scriptFile = DataManagerUtil.parseBaseDir(getSessionUser(), scriptFile);
//                }
//            }
//            ve.wrapperScriptFile(templateFolder,
//                                 listInput,
//                                 listOutput,
//                                 applicationName,
//                                 scriptFile,
//                                 applicationRealLocation,
//                                 environementFile,
//                                 dir,
//                                 generateTime,
//                                 mandatoryDir,
//                                 dockerImage,
//                                 commandLine);
//        } catch (CoreException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        } catch (DataManagerException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        } catch (VelocityException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        }
//    }
//
//    /**
//     * method to generate the Gwendia file of the application
//     *
//     * @param listInput
//     * @param listOutput
//     * @param wrapperScriptPath
//     * @param scriptFile
//     * @param applicationName
//     * @param applicationLocation
//     * @param description
//     * @return
//     * @throws ApplicationImporterException
//     */
//    private String generateGwendiaFile(String templateFolder, HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String vo) throws ApplicationImporterException {
//        String applicationRealLocation;
//        try {
//            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
//            final File homeDir = new File(Server.getInstance().getApplicationImporterFileRepository());
//            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
//            theDir.mkdirs();
//            String dir = theDir.getAbsolutePath();
//            return ve.gwendiaFile(templateFolder, listInput, listOutput, applicationName, description, applicationRealLocation, dir, new File(homeDir, applicationName), generateTime, mandatoryDir, vo);
//        } catch (CoreException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        } catch (DataManagerException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        } catch (VelocityException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        }
//
//    }
//
//    /**
//     * method to generate the Gasw file of the application
//     *
//     * @param listInput
//     * @param listOutput
//     * @param wrapperScriptPath
//     * @param scriptFile
//     * @param applicationName
//     * @param applicationLocation
//     * @param description
//     * @throws ApplicationImporterException
//     */
//    private void generateGaswFile(String templateFolder, HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String sandboxFile, String environementFile, String extensionFile) throws ApplicationImporterException {
//        String applicationRealLocation = null;
//        try {
//            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
//            //create folder to genrate file
//            final File homeDir = new File(Server.getInstance().getApplicationImporterFileRepository());
//            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
//            theDir.mkdirs();
//
//            // if the directory does not exist, create it
//            String dir = theDir.getAbsolutePath();
//            String executableSandbox = null;
//            String envF = null;
//            String sandboxF = null;
//            List<String> extensionFValue = null;
//            String extensionF = "";
//            if (extensionFile != null) {
//                extensionFValue = new ArrayList<String>();
//                if (!extensionFile.isEmpty()) {
//                    try {
//                        extensionF = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), extensionFile), Server.getInstance().getApplicationImporterFileRepository());
//                    } catch (GRIDAClientException ex) {
//                        logger.error(ex);
//                        throw new ApplicationImporterException(ex);
//                    }
//
//                    try {
//
//                        Scanner scanner = new Scanner(new FileInputStream(extensionF));
//                        List<String> requirements = new ArrayList<String>();
//                        String ligne = null;
//                        while (scanner.hasNextLine()) {
//                            ligne = scanner.nextLine();
//                            if (ligne.startsWith("requirements=")) {
//                                requirements.add(ligne.substring(14));
//                            }
//                        }
//                        scanner.close();
//
//                        for (String requirement : requirements) {
//
//                            extensionFValue.add(StringEscapeUtils.escapeXml(requirement.substring(0, requirement.length() - 1)));
//
//                        }
//
//                    } catch (FileNotFoundException exc) {
//                        logger.error(exc);
//                        throw new ApplicationImporterException("Can't find the extension file :" + extensionF);
//
//                    }
//                }
//            }
//            if (scriptFile != null) {
//                if (!scriptFile.isEmpty()) {
//                    executableSandbox = DataManagerUtil.parseBaseDir(getSessionUser(), scriptFile);
//
//                }
//            }
//            if (environementFile != null) {
//                if (!environementFile.isEmpty()) {
//                    envF = DataManagerUtil.parseBaseDir(getSessionUser(), environementFile);
//
//                }
//            }
//            if (sandboxFile != null) {
//                if (!sandboxFile.isEmpty()) {
//                    sandboxF = DataManagerUtil.parseBaseDir(getSessionUser(), sandboxFile);
//                }
//            }
//            ve.gaswFile(templateFolder, listInput, listOutput, applicationName, wrapperScriptPath, applicationRealLocation, dir, generateTime, sandboxF, envF, extensionFValue, executableSandbox);
//        } catch (CoreException e) {
//            logger.error(e);
//            throw new ApplicationImporterException(e);
//        } catch (DataManagerException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        } catch (VelocityException ex) {
//            logger.error(ex);
//            throw new ApplicationImporterException(ex);
//        }
//    }
//
//    private java.sql.Timestamp getCurrentTimeStamp() {
//        java.util.Date today = new java.util.Date();
//        return new java.sql.Timestamp(today.getTime());
//
//    }
}
