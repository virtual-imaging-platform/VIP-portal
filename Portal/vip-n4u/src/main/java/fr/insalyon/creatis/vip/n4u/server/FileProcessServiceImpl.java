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
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
        logger.error(localFilePat);
      
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
}
