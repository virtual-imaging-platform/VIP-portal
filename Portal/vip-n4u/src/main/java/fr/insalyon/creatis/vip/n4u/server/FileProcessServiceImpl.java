/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import fr.insalyon.creatis.vip.n4u.server.velocity.Velocity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author Nouha Boujelben
 */
public class FileProcessServiceImpl extends fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet
        implements FileProcessService {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileProcessServiceImpl.class);

    /**
     *
     * @param param1
     * @return
     * @throws IOException
     */
    //local dir
    public List<List<String>> fileTraitement(String param1) {
        String localFilePat = null;
       try {
            
                localFilePat = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(),param1), System.getProperty("user.home"));
                } catch (CoreException ex) {
                   logger.error(ex);
                   logger.error(param1);
               
                }
            catch (DataManagerException ex) {
               logger.error(ex);
            }
         catch (GRIDAClientException ex) {
             logger.error(ex);
        }
       
        List<List<String>> result = new ArrayList<List<String>>();

        List<String> inputsDir = new ArrayList<String>();
        List<String> outputsDir = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new FileInputStream(localFilePat));
            while (scanner.hasNext()) {
                String ligne = scanner.nextLine();
                if (ligne.contains("inputDir=")) {
                    int index = ligne.indexOf("inputDir=");
                    inputsDir.add(ligne.substring(index + 9));

                } else if (ligne.contains("outputDir=")) {
                    int index = ligne.indexOf("outputDir=");
                    outputsDir.add(ligne.substring(index + 11));
                }


            }


        } catch (IOException ex) {
            logger.error(ex);
        }
        result.add(inputsDir);
        result.add(outputsDir);
        return result;
    }
    
    
    
    
    
    //retourne nombre d'entree ainsi que le nombre des entree de type file
    
     public int[] fileJobTraitement(String jobFile,String expressFile) {
         int[]result=new int[2];
          int nb=0;
       String localFilePat = null;
       String localFilePathExpress = null;
      
       
       try {
            
                localFilePat = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(),jobFile), System.getProperty("user.home"));
                } catch (CoreException ex) {
                   logger.error(ex);
                   
               
                }
            catch (DataManagerException ex) {
               logger.error(ex);
            }
         catch (GRIDAClientException ex) {
             logger.error(ex);
        }
     
      
        try {
            
            
                    localFilePathExpress = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(),expressFile), System.getProperty("user.home"));
                } catch (CoreException ex) {
                   logger.error(ex);
                   
               
                }
            catch (DataManagerException ex) {
               logger.error(ex);
            }
         catch (GRIDAClientException ex) {
             logger.error(ex);
        }
      
      
        try {
          Scanner scanner = new Scanner(new FileInputStream(localFilePat));
           //Scanner scanner = new Scanner(new FileInputStream("/home/nouha/Siena.jobs"));
            
               String ligne = scanner.nextLine();
               while(ligne.substring(0, 1).equals("#")){
               ligne = scanner.nextLine();
               }
                
               StringTokenizer strok = new StringTokenizer(ligne,"\t");
              
               nb = strok.countTokens(); 
              result[0]=nb;
               
        Scanner scanner2 = new Scanner(new FileInputStream(localFilePathExpress));
       // Scanner scanner2 = new Scanner(new FileInputStream("/home/nouha/Siena.express"));
         
        while( scanner2.hasNext()){
          String readLigne = scanner2.nextLine();   
          if(readLigne.contains("ExplicitFiles")){
          int fileNumber=Integer.parseInt(readLigne.substring(14));
          result[1]=fileNumber;
          }
              
        
        }

        } catch (IOException ex) {
            logger.error(ex);
        }
      
        return result;
    }
   
     private  java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    @Override
    public void generateScriptFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) {
        String applicationRealLocation=null;
        try {
            applicationRealLocation=DataManagerUtil.parseBaseDir(getSessionUser(),applicationLocation);
        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataManagerException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //create floder to genrate file
        final File homeDir = new File(Server.getInstance().getApplicationFilesRepository());
        File theDir = null;
        try {
            theDir = new File(homeDir,applicationName+"/"+getSessionUser().getFolder()+"/"+getCurrentTimeStamp());
            theDir.mkdirs();
        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        
       String dir=theDir.getAbsolutePath();
      
        new Velocity().wrapperScriptFile(listInput,listOutput,applicationName, scriptFile,applicationRealLocation,dir);
    }

    @Override
    public String generateGwendiaFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) {
           String applicationRealLocation=null;
        try {
            applicationRealLocation=DataManagerUtil.parseBaseDir(getSessionUser(),applicationLocation);
        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataManagerException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //create floder to genrate file
        final File homeDir = new File(Server.getInstance().getApplicationFilesRepository());
        File theDir = null;
        try {
            theDir = new File(homeDir,applicationName+"/"+getSessionUser().getFolder()+"/"+getCurrentTimeStamp());
            theDir.mkdirs();
        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    
        
       String dir=theDir.getAbsolutePath();
      
       
        return new Velocity().gwendiaFile(listInput,listOutput, applicationName,description,applicationRealLocation,dir);
    }

    @Override
    public void generateGaswFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) {
       String applicationRealLocation=null;
        try {
            applicationRealLocation=DataManagerUtil.parseBaseDir(getSessionUser(),applicationLocation);
        } catch (CoreException e) {
            logger.error(e);
        } catch (DataManagerException ex) {
           logger.error(ex);
        }
        
        //create floder to genrate file
        final File homeDir = new File(Server.getInstance().getApplicationFilesRepository());
        File theDir = null;
        try {
            theDir = new File(homeDir,applicationName+"/"+getSessionUser().getFolder()+"/"+getCurrentTimeStamp());
            theDir.mkdirs();
            
        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(FileProcessServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    // if the directory does not exist, create it
    
        
       String dir=theDir.getAbsolutePath();
        new Velocity().gassFile(listInput,listOutput,applicationName,wrapperScriptPath,applicationRealLocation,dir );
    }

   
    public String getApplicationClasse() {
        return Server.getInstance().getApplicationN4uClasse();
    }
    
    
    
   
   
}
