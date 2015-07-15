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
package fr.insalyon.creatis.vip.applicationimporter.server.velocity;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author Nouha Boujelben
 */
public class Velocity implements VelocityProcess {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Velocity.class);
    VelocityEngine ve;
    List<File> files;

    public Velocity() {
        files = new ArrayList<File>();
        ve = new VelocityEngine();
        ve.setProperty("resource.loader", "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();
    }

    /**
     *
     * @param listInput
     * @param listOutput
     * @param applicationName
     * @param scriptFile
     * @param applicationLocation
     * @param dir
     * @throws VelocityException
     */
    @Override
    public void wrapperScriptFile(String templateFolder, HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String scriptFile, String applicationLocation, String environementFile, String dir, String date, List<String> mandatoryDir,String dockerImage,String commandLine) throws VelocityException {
        VelocityContext context = new VelocityContext();
        logger.error("1");
        applicationLocation = applicationLocation + "/" + date;
        String scriptName = null;
        if(scriptFile!=null){
        int lastIndex = scriptFile.lastIndexOf("/");
        scriptName = scriptFile.substring(lastIndex + 1);
        context.put("scriptFile", scriptName);
        }
        String env = "";
        if (environementFile != null) {
            if (!environementFile.isEmpty()) {
                env = environementFile.substring(environementFile.lastIndexOf("/") + 1);
                context.put("env", env);
            } else {
                env = environementFile;
            }
        }
        logger.error("2");
        if(mandatoryDir!=null){
        if (!mandatoryDir.isEmpty()) {
            int index = mandatoryDir.get(0).lastIndexOf("/") + 1;
            String directName = mandatoryDir.get(0).substring(index);
            context.put("directName", directName);
        } 
        }
        logger.error("3");
        if(dockerImage!=null){
        context.put("dockerImage", dockerImage);
        }
        if(commandLine!=null){
        context.put("commandLine",commandLine);
        
        }
        context.put("inputList", listInput);
        context.put("number", listInput.size() + listOutput.size());  
        context.put("outputList", listOutput);
        Template t;
        try {
            t = ve.getTemplate(templateFolder);
        } catch (Exception ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }
        logger.error("4");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + "_wrapper.sh";
        final File fichier = new File(chemin);
        try {
            FileTools.createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "bin");
            FileTools.copyFile(chemin, dir + "/" + applicationName + ".bak" + "_wrapper.sh");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "bin");

            //script File
            if(scriptFile!=null){
            FileTools.setDirRights(dir, "vip-services");
            CoreUtil.getGRIDAClient().getRemoteFile(scriptFile, dir);
            files.add(new File(dir + "/" + scriptName));
            }
            //SECOND copy of the wrapper file to create archive
            FileTools.copyFile(dir + "/" + applicationName + ".bak" + "_wrapper.sh", chemin);  
            files.add(new File(dir + "/" + applicationName + "_wrapper.sh"));
            
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }
    }

    /**
     *
     * @param listInput
     * @param listOutput
     * @param applicationName
     * @param wrapperScriptPath
     * @param applicationLocation
     * @param dir
     * @throws VelocityException
     */
    @Override
    public void gaswFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir, String date, String sandboxFile, String environementFile, List<String> requirementValues, String executableSandbox) throws VelocityException {
        Template t;
        try {
            t = ve.getTemplate(templateFolder);
        } catch (Exception ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }
        VelocityContext context = new VelocityContext();
        applicationLocation = applicationLocation + "/" + date;
        context.put("inputList", listInput);
        context.put("outputList", listOutput);
        context.put("applicationName", applicationName);
        context.put("applicationLocation", applicationLocation);
        context.put("gaswValue", applicationLocation + "/bin/" + applicationName + "_wrapper.sh");
        if(requirementValues!=null){
        context.put("requirementValues", requirementValues);
        }
        if(executableSandbox!=null){
        context.put("exSandbox", executableSandbox);
        }
        if(sandboxFile!=null){
        if (!sandboxFile.isEmpty()) {
            
            try {
                
                FileTools.setDirRights(dir, "vip-services");
                CoreUtil.getGRIDAClient().getRemoteFile(sandboxFile, dir);
                 context.put("sandboxFile", applicationLocation + "/bin/" + sandboxFile.substring(sandboxFile.lastIndexOf("/") + 1));
                files.add(new File(dir + sandboxFile.substring(sandboxFile.lastIndexOf("/"))));
            } catch (GRIDAClientException ex) {
                logger.error(ex);
                throw new VelocityException(ex);
            }
        }
        }
        if(environementFile!=null){
        if (!environementFile.isEmpty()) {
            try {
                FileTools.setDirRights(dir, "vip-services");
                CoreUtil.getGRIDAClient().getRemoteFile(environementFile, dir);
                  context.put("environementFile", applicationLocation + "/bin/" + environementFile.substring(environementFile.lastIndexOf("/") + 1));
                // CoreUtil.getGRIDAClient().rename(environementFile, applicationLocation + "/bin/" + environementFile.substring(environementFile.lastIndexOf("/") + 1));
                files.add(new File(dir + environementFile.substring(environementFile.lastIndexOf("/"))));
            } catch (GRIDAClientException ex) {
                logger.error(ex);
                throw new VelocityException(ex);
            }
        }
        }
        
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + ".xml";
        final File fichier = new File(chemin);

        try {
            FileTools.createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "gasw");
            FileTools.copyFile(chemin, dir + "/" + applicationName + ".bak" + ".xml");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "gasw");
            FileTools.copyFile(dir + "/" + applicationName + ".bak" + ".xml", chemin);
            if(templateFolder.contains("neugrid")){
            String n4uLocation = applicationLocation.replace("/grid/biomed/creatis/vip", "/grid/vo.neugrid.eu/home/vip");
            CoreUtil.getGRIDAN4uClient().uploadFile(chemin, n4uLocation + "/" + "gasw");
            }

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }
    }

    /**
     *
     * @param listInput
     * @param listOutput
     * @param applicationName
     * @param description
     * @param applicationLocation
     * @param dir
     * @return
     * @throws VelocityException
     */
    @Override
    public String gwendiaFile(String templateFolder, HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String applicationName, String description, String applicationLocation, String dir, File theDirApp, String date, List<String> mandatoryDir,String vo) throws VelocityException {
        Template t;
        try {
            t = ve.getTemplate(templateFolder);
        } catch (Exception ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }
        applicationLocation = applicationLocation + "/" + date;
        final String gaswDescriptor;
        if(vo.equals("biomed")){
        gaswDescriptor = applicationLocation + "/gasw" + "/" + applicationName + ".xml";
        }else{
        gaswDescriptor = applicationLocation.replace("/grid/biomed/creatis/vip", "/grid/vo.neugrid.eu/home/vip") + "/gasw" + "/" + applicationName + ".xml";
        }
        VelocityContext context = new VelocityContext();
        context.put("inputList", listInput);
        context.put("outputList", listOutput);
        context.put("applicationName", applicationName);
        if (description == null) {
            context.put("description", "");
        } else {
            String des = FileTools.html2text(description);
            context.put("description", des);
        }
        context.put("gaswDescriptor", gaswDescriptor);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + ".gwendia";
        final File fichier = new File(chemin);

        try {
            FileTools.createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "workflows");
            FileTools.copyFile(chemin, dir + "/" + applicationName + ".bak" + ".gwendia");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "workflows");

            if (mandatoryDir != null) {
                for (String strDir : mandatoryDir) {
                    int index = strDir.indexOf(":");
                    String folderName = strDir.substring((strDir.lastIndexOf("/")) + 1);
                    String dirPath = strDir.substring(index + 1);
                    if (folderName.contains(".")) {
                        FileTools.setDirRights(dir, "n4u-services");
                        CoreUtil.getGRIDAN4uClient().getRemoteFile(dirPath, dir);
                    } else {
                        FileTools.setDirRights(dir, "n4u-services");
                        CoreUtil.getGRIDAN4uClient().getRemoteFolder(dirPath, dir + "/" + folderName);
                        ArchiveTools.unzip(new File(dir + "/" + folderName + ".zip"), new File(dir));
                    }

                    files.add(new File(dir + "/" + folderName));

                }
            }
            ArchiveTools.compress(files, dir + "/" + applicationName + "_wrapper.sh.tar.gz");

            FileTools.setDirRights(dir + "/" + applicationName + "_wrapper.sh.tar.gz", "vip-services");
            CoreUtil.getGRIDAClient().uploadFile(dir + "/" + applicationName + "_wrapper.sh.tar.gz", applicationLocation + "/" + "bin");

            if (Server.getInstance().getDeleteFilesAfterUpload().equals("yes")) {
                FileTools.delete(theDirApp);
            }

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        } catch (IOException ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }

        return applicationLocation + "/workflows" + "/" + applicationName + ".gwendia";
    }

}
