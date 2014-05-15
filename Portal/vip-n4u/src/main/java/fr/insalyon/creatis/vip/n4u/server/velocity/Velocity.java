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
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;

/**
 *
 * @author Nouha Boujelben
 */
public class Velocity implements VelocityProcess {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Velocity.class);
    VelocityEngine ve;

    public Velocity() {
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
     * @param wrapperScriptPath
     * @param applicationLocation
     * @param dir
     * @throws VelocityException
     */
    @Override
    public void gassFile(ArrayList listInput, ArrayList listOutput, String applicationName, String wrapperScriptPath, String applicationLocation, String dir) throws VelocityException {
        Template t = ve.getTemplate("vm/gass.vm");
        VelocityContext context = new VelocityContext();
        context.put("inputList", listInput);
        context.put("outputList", listOutput);
        context.put("applicationName", applicationName);
        context.put("applicationLocation", applicationLocation);
        context.put("gassValue", applicationLocation + "/bin/" + applicationName + "_wrapper.sh");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + ".xml";
        final File fichier = new File(chemin);

        try {
            createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "gasw");
            copyFile(chemin, dir + "/" + applicationName + ".bak" + ".xml");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "gasw");
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
     * @param scriptFile
     * @param applicationLocation
     * @param dir
     * @throws VelocityException
     */
    @Override
    public void wrapperScriptFile(ArrayList listInput, ArrayList listOutput, String applicationName, String scriptFile, String applicationLocation, String dir) throws VelocityException {

        int lastIndex = scriptFile.lastIndexOf("/");
        String script = scriptFile.substring(lastIndex + 1);
        Template t = ve.getTemplate("vm/wrapper_script.vm");
        VelocityContext context = new VelocityContext();
        context.put("inputList", listInput);
        context.put("number", listInput.size() + listOutput.size());
        context.put("scriptFile", script);
        context.put("outputList", listOutput);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        final String chemin = dir + "/" + applicationName + "_wrapper.sh";
        final File fichier = new File(chemin);
        try {
            createFile(fichier, writer);
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "bin");
            copyFile(chemin, dir + "/" + applicationName + ".bak" + "._wrapper.sh");
            CoreUtil.getGRIDAClient().uploadFile(chemin, applicationLocation + "/" + "bin");
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
    public String gwendiaFile(ArrayList listInput, ArrayList listOutput, String applicationName, String description, String applicationLocation, String dir) throws VelocityException {
        Template t = ve.getTemplate("vm/gwendia.vm");
        final String gaswDescriptor = applicationLocation + "/gasw" + "/" + applicationName + ".xml";
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
}
