/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server;

import fr.insalyon.creatis.vip.n4u.client.rpc.N4uException;
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
import fr.insalyon.creatis.vip.n4u.server.velocity.VelocityException;
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
    //get the value from first appel to generateScriptFile
    private String generateTime=null;

    @Override
    public int[] fileJobProcess(String jobFile, String expressFile) throws N4uException {
        int[] result = new int[2];
        int nb = 0;
        String localJobFilePath = null;
        String localExpressFilePath = null;
        try {
            localJobFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), jobFile), Server.getInstance().getN4uApplicationFilesRepository());
            localExpressFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), expressFile), Server.getInstance().getN4uApplicationFilesRepository());
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }

        try {
            Scanner scanner = new Scanner(new FileInputStream(localJobFilePath));
            String ligne = scanner.nextLine();
            while (ligne.substring(0, 1).equals("#")) {
                ligne = scanner.nextLine();
            }
            StringTokenizer strok = new StringTokenizer(ligne, "\t");
            nb = strok.countTokens();
            result[0] = nb;
            Scanner scanner2 = new Scanner(new FileInputStream(localExpressFilePath));
            while (scanner2.hasNext()) {
                String readLigne = scanner2.nextLine();
                if (readLigne.contains("ExplicitFiles")) {
                    int fileNumber = Integer.parseInt(readLigne.substring(14));
                    result[1] = fileNumber;
                }
            }
        } catch (IOException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
        return result;
    }

    @Override
    public void generateScriptFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) throws N4uException {
        String applicationRealLocation = null;
        try {
            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            generateTime=getCurrentTimeStamp().toString();
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" +generateTime );
            theDir.mkdirs();
            String dir = theDir.getAbsolutePath();
            new Velocity().wrapperScriptFile(listInput, listOutput, applicationName, scriptFile, applicationRealLocation, dir);
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (VelocityException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
    }

    @Override
    public String generateGwendiaFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) throws N4uException {
        String applicationRealLocation = null;
        try {
            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
            theDir.mkdirs();
            String dir = theDir.getAbsolutePath();
            return new Velocity().gwendiaFile(listInput, listOutput, applicationName, description, applicationRealLocation, dir);
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (VelocityException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }


    }

    @Override
    public void generateGaswFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) throws N4uException {
        String applicationRealLocation = null;
        try {
            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            //create folder to genrate file
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
            theDir.mkdirs();
            // if the directory does not exist, create it
            String dir = theDir.getAbsolutePath();
            new Velocity().gassFile(listInput, listOutput, applicationName, wrapperScriptPath, applicationRealLocation, dir);
        } catch (CoreException e) {
            logger.error(e);
            throw new N4uException(e);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (VelocityException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
    }

    @Override
    public String getApplicationClass() throws N4uException {
        return Server.getInstance().getApplicationN4uClass();
    }

    private java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }
}
