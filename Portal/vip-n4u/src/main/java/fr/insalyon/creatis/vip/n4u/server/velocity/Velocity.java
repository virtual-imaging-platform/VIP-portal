/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.jsoup.Jsoup;

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
    public void wrapperScriptFile(Map<Integer, Map> listInput, List<Map> listOutput, String applicationName, String scriptFile, String applicationLocation, String environementFile, String dir, String date) throws VelocityException {

        applicationLocation = applicationLocation + "/" + date;
        int lastIndex = scriptFile.lastIndexOf("/");
        //String script = scriptFile.substring(lastIndex + 1);
        String env = "";
        if (!environementFile.isEmpty()) {
            env = environementFile.substring(environementFile.lastIndexOf("/") + 1);
        } else {
            env = environementFile;
        }
        Template t = ve.getTemplate("vm/wrapper_script.vm");
        VelocityContext context = new VelocityContext();
        context.put("inputList", listInput);
        context.put("number", listInput.size() + listOutput.size());
        context.put("scriptFile", scriptFile);
        context.put("outputList", listOutput);
        if (!env.isEmpty()) {
            context.put("env", env);
        }
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + "_wrapper.sh";
        final File fichier = new File(chemin);
        try {
            createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "bin");
            copyFile(chemin, dir + "/" + applicationName + ".bak" + "_wrapper.sh");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "bin");
            //SECOND copy of the wrapper file to create archive
            copyFile(dir + "/" + applicationName + ".bak" + "_wrapper.sh", chemin);
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
    public void gassFile(Map<Integer, Map> listInput, List<Map> listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir, String date, String sandboxFile, String environementFile, String extensionFileValue, String executableSandbox) throws VelocityException {
        Template t = ve.getTemplate("vm/gass.vm");
        VelocityContext context = new VelocityContext();
        applicationLocation = applicationLocation + "/" + date;
        context.put("inputList", listInput);
        context.put("outputList", listOutput);
        context.put("applicationName", applicationName);
        context.put("applicationLocation", applicationLocation);
        context.put("extensionFileValue", StringEscapeUtils.escapeXml(extensionFileValue));
        context.put("gassValue", applicationLocation + "/bin/" + applicationName + "_wrapper.sh");
        context.put("exSandbox", executableSandbox);
        if (!sandboxFile.isEmpty()) {
            try {
                CoreUtil.getGRIDAClient().getRemoteFile(sandboxFile, dir);
                files.add(new File(dir + sandboxFile.substring(sandboxFile.lastIndexOf("/")+1)));
            } catch (GRIDAClientException ex) {
                logger.error(ex);
                throw new VelocityException(ex);
            }
        }
        if (!environementFile.isEmpty()) {
            try {
                CoreUtil.getGRIDAClient().getRemoteFile(environementFile, dir);
                // CoreUtil.getGRIDAClient().rename(environementFile, applicationLocation + "/bin/" + environementFile.substring(environementFile.lastIndexOf("/") + 1));
                files.add(new File(dir + environementFile.substring(environementFile.lastIndexOf("/") + 1)));
            } catch (GRIDAClientException ex) {
                logger.error(ex);
                throw new VelocityException(ex);
            }
        }

        if (!sandboxFile.isEmpty()) {
            context.put("sandboxFile", applicationLocation + "/bin/" + sandboxFile.substring(sandboxFile.lastIndexOf("/") + 1));
        }
        if (!environementFile.isEmpty()) {
            context.put("environementFile", applicationLocation + "/bin/" + environementFile.substring(environementFile.lastIndexOf("/") + 1));
        }
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + ".xml";
        final File fichier = new File(chemin);

        try {
            createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "gasw");
            copyFile(chemin, dir + "/" + applicationName + ".bak" + ".xml");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "gasw");
            copyFile(dir + "/" + applicationName + ".bak" + ".xml", chemin);
            String n4uLocation = applicationLocation.replace("/grid/biomed/creatis/vip", "/grid/vo.neugrid.eu/home/vip");
            CoreUtil.getGRIDAN4uClient().uploadFile(chemin, n4uLocation + "/" + "gasw");

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
    public String gwendiaFile(Map<Integer, Map> listInput, List<Map> listOutput, String applicationName, String description, String applicationLocation, String dir, String date) throws VelocityException {
        Template t = ve.getTemplate("vm/gwendia.vm");
        applicationLocation = applicationLocation + "/" + date;
        final String gaswDescriptor = applicationLocation.replace("/grid/biomed/creatis/vip", "/grid/vo.neugrid.eu/home/vip") + "/gasw" + "/" + applicationName + ".xml";
        VelocityContext context = new VelocityContext();
        context.put("inputList", listInput);
        context.put("outputList", listOutput);
        context.put("applicationName", applicationName);
        if (description == null) {
            context.put("description", "");
        } else {
            String des = html2text(description);
            context.put("description", des);
        }
        context.put("gaswDescriptor", gaswDescriptor);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + ".gwendia";
        final File fichier = new File(chemin);

        try {
            createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "workflows");
            copyFile(chemin, dir + "/" + applicationName + ".bak" + ".gwendia");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "workflows");

          
            createArchive(files, dir, applicationName + "_wrapper.sh.tar.gz");
            CoreUtil.getGRIDAClient().uploadFile(dir + "/" + applicationName + "_wrapper.sh.tar.gz", applicationLocation + "/" + "bin");

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new VelocityException(ex);
        }

        return applicationLocation + "/workflows" + "/" + applicationName + ".gwendia";
    }

    /**
     *
     * @param source
     * @param dest
     */
    public void copyFile(String source, String dest) {
        FileChannel in = null;
        FileChannel out = null;

        try {
            // Init
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

    }

    /**
     *
     * @param f
     * @param writer
     */
    private void createFile(File f, StringWriter writer) {
        FileWriter writerr;
        try {
            f.createNewFile();
            writerr = new FileWriter(f);
            try {
                writerr.write(writer.toString());
            } finally {
                writerr.close();
            }
        } catch (IOException e) {
            logger.error("can't create file" + e);
        }

    }

    /**
     * parse a html string to text
     *
     * @param html
     * @return
     */
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    private File createArchive(final List<File> listFiles, String targetDirectory, String outputFileName) throws VelocityException {

        File targetFileDirectory = new File(targetDirectory);
        if (!targetFileDirectory.exists() && !targetFileDirectory.mkdir()) {
            throw new VelocityException("Could not create: " + targetFileDirectory.getAbsolutePath());
        }
        final File destFile = new File(targetDirectory, outputFileName);
        if (destFile.exists() && !destFile.delete()) {
            throw new VelocityException("Could not delete: " + destFile.getAbsolutePath());
        }
        Archiver archiver = new TarArchiver();
        archiver.setDestFile(destFile);
        try {
            for (File dir : listFiles) {
                archiver.addFile(dir, dir.getName());
            }

            archiver.createArchive();
        } catch (ArchiverException e) {
            throw new VelocityException("Error building archive." + e);
        } catch (IOException e) {
            throw new VelocityException("Error building archive." + e);
        }
        return destFile;
    }
}
