/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.n4u.client.view.N4uConstants;
import fr.insalyon.creatis.vip.n4u.server.FileProcessServiceImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 *
 * @author nouha Boujelben
 */
public class Velocity {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Velocity.class);
    VelocityEngine ve;

    public Velocity() {
        ve = new VelocityEngine();
        ve.setProperty("resource.loader", "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();

    }

    public void gassFile(ArrayList listInput,ArrayList listOutput,String applicationName,String wrapperScriptPath,String applicationLocation,String dir) {
        Template t = null;
        try {
            t = ve.getTemplate("vm/gass.vm");

        } catch (Exception e) {
            logger.error(e);
        }

        VelocityContext context = new VelocityContext();

        context.put("inputList", listInput);
        context.put("outputList", listOutput);
        context.put("applicationLocation", applicationLocation);
        context.put("gassValue",applicationLocation + "/bin/"+applicationName+"_wrapper.sh");
        StringWriter writer = new StringWriter();

        t.merge(context, writer);
        final String chemin = dir + "/"+applicationName+".xml";

        final File fichier = new File(chemin);
        FileWriter writerr;
        try {
           
            fichier.createNewFile();
          
            writerr = new FileWriter(fichier);
            try {
                writerr.write(writer.toString());

            } finally {
              
                writerr.close();
            }
        } catch (Exception e) {
            logger.error("Impossible de creer le fichier" + e);
        }
      
        try {
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "gasw");
            CoreUtil.getGRIDAClient().uploadFile(chemin,applicationLocation+"/"+"gasw" );
             logger.error(chemin+applicationLocation);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
        }
      
        

    }
    
    
    public void wrapperScriptFile(ArrayList listInput,ArrayList listOutput,String applicationName,String scriptFile,String applicationLocation,String dir){
        Template t = null;
        int lastIndex=scriptFile.lastIndexOf("/");
        String script=scriptFile.substring(lastIndex+1);
        try {
            t = ve.getTemplate("vm/wrapper_script.vm");

        } catch (Exception e) {
            logger.error(e);
        }
         VelocityContext context = new VelocityContext();
         context.put("inputList", listInput);
         context.put("number",listInput.size()+listOutput.size() );
         context.put("scriptFile",script);
         context.put("outputList", listOutput);
      
           StringWriter writer = new StringWriter();

        t.merge(context, writer);
        final String chemin = dir + "/"+applicationName+"_wrapper.sh";

        final File fichier = new File(chemin);
        FileWriter writerr;
        try {
           
            fichier.createNewFile();
          
            writerr = new FileWriter(fichier);
            try {
                writerr.write(writer.toString());

            } finally {
              
                writerr.close();
            }
        } catch (Exception e) {
            logger.error("Impossible de creer le fichier" + e);
        }
        
        try {
           
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "bin");
            CoreUtil.getGRIDAClient().uploadFile(chemin,applicationLocation+"/"+"bin" );
             
        } catch (GRIDAClientException ex) {
            logger.error(ex);
        }

        
        
    }
    
    public void gwendiaFile(ArrayList listInput,ArrayList listOutput,String applicationName,String description,String applicationLocation,String dir){
        Template t = null;
        try {
            t = ve.getTemplate("vm/gwendia.vm");

        } catch (Exception e) {
            logger.error(e);
        }
        final String gaswDescriptor = applicationLocation +"/gasw"+ "/"+applicationName+".xml";
         VelocityContext context = new VelocityContext();
         context.put("inputList", listInput);
         context.put("outputList",listOutput);
         context.put("applicationName", applicationName);
         context.put("description", description);
         context.put("gaswDescriptor", gaswDescriptor);
         StringWriter writer = new StringWriter();

        t.merge(context, writer);
        final String chemin = dir + "/"+applicationName+".gwendia";

        final File fichier = new File(chemin);
        FileWriter writerr;
        try {
           
            fichier.createNewFile();
          
            writerr = new FileWriter(fichier);
            try {
                writerr.write(writer.toString());

            } finally {
              
                writerr.close();
            }
        } catch (Exception e) {
            logger.error("Impossible de creer le fichier" + e);
        }
        
         try {
            CoreUtil.getGRIDAClient().createFolder(applicationLocation, "workflows");
            CoreUtil.getGRIDAClient().uploadFile(chemin,applicationLocation+"/"+"workflows" );
             
        } catch (GRIDAClientException ex) {
            logger.error(ex);
        }

        
    }
}
