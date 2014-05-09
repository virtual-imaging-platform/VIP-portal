/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
    private String generateTime = null;

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

            throw new N4uException(ex);
        }
        return result;
    }
    //name,type,description,requiredField

    @Override
    public List<String[]> parseXmlFile(String xmlFile) throws N4uException {

        List<String[]> listInputs = new ArrayList<String[]>();
        try {
            //String localXmlFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), xmlFile), Server.getInstance().getN4uApplicationFilesRepository());
            Scanner scanner = new Scanner(new FileInputStream("/home/nouha/module.txt"));
            String ligne = scanner.nextLine();


            do {
                ligne = scanner.nextLine();

            } while (!ligne.contains("<inputs>"));
            String readInputs = ligne;

            do {
                ligne = scanner.nextLine();
                readInputs = readInputs + ligne;

            } while (!ligne.contains("</inputs>"));
            readInputs = readInputs + ligne.substring(0, ligne.indexOf("</inputs>"));


            String[] inputs = readInputs.split("</input>");


            //don't take last string 
            for (int i = 0; i < inputs.length - 1; i++) {
                int beginDes=inputs[i].indexOf("<description>");
                int endDes=inputs[i].indexOf("</description>");
                String description=inputs[i].substring(beginDes+13, endDes);
                String[] values = inputs[i].split("\"");
                String[] value = new String[4];
                value[0] = values[1];//NAME
                value[1] = parseTypeSupported(values[3]);;
                value[2] = values[5];//required
                value[3]=description;
                listInputs.add(value);



            }
            /* } catch (GRIDAClientException ex) {
             throw new N4uException(ex);
             } catch (DataManagerException ex) {
             throw new N4uException(ex);
             } catch (CoreException ex) {
             throw new N4uException(ex);**/
        } catch (FileNotFoundException ex) {
            throw new N4uException(ex);
        }
        return listInputs;

    }

    @Override
    public void generateScriptFile(ArrayList listInput, ArrayList listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description) throws N4uException {
        String applicationRealLocation = null;
        try {
            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            generateTime = getCurrentTimeStamp().toString();
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
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

    private String parseTypeSupported(String val) throws N4uException {
        String param1 = new String("text");
        String param2 = new String("integer");
        String param3 = new String("float");
        String param4 = new String("double");
        String file = new String("file");
        if (val.equals(param1) || val.equals(param2) || val.equals(param3) || val.equals(param4)) {
            return new String(InputType.Parameter.name());
        } else if (val.equals(file)) {
            return new String(InputType.File.name());
        } else {
            throw new N4uException("type " + val + " " + "not supported by the application ");
        }
    }

    public static enum InputType {

        File, Parameter;
    }
}
