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
package fr.insalyon.creatis.vip.cowork.server.rpc;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.rpc.ApplicationServiceImpl;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.cowork.client.CoworkConstants;
import fr.insalyon.creatis.vip.cowork.client.view.CoworkException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import fr.insalyon.creatis.vip.datamanager.server.rpc.DataManagerServiceImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
public class CoworkServiceImpl extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CoworkServiceImpl.class);

    public CoworkServiceImpl() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        String appName = req.getHeader("appName");
        String email = req.getHeader("email");
        String sessionId = req.getHeader("sessionId");
        try {
            saveWorkflow(req.getInputStream(), appName, email, sessionId);
        } catch (CoworkException ex) {
            throw new ServletException(ex);
        }
    }

    private void saveWorkflow(InputStream gwendiaContent, String appName, String email, String sessionId) throws CoworkException {
              FileOutputStream fos = null;
            try {
               User user = null;
                try {
                    user = CoreDAOFactory.getDAOFactory().getUserDAO().getUserBySession(sessionId);
                } catch (DAOException ex) {
                        throw new CoworkException(ex);
                }
                
                File gwendiaFile = new File(Server.getInstance().getDataManagerPath()
                            + "/uploads/"+appName+".gwendia");
                fos = new FileOutputStream(gwendiaFile);
                InputStream is = gwendiaContent;
                try {
                    byte[] buffer = new byte[4096];
                    while (true) {
                        int bytes = is.read(buffer);
                        if (bytes < 0) {
                            break;
                        }
                        fos.write(buffer, 0, bytes);
                    }
                    fos.flush();
                } catch (IOException ex) {
                    throw new CoworkException(ex);
                } finally {
                    try {
                        is.close();
                        fos.close();
                    } catch (IOException ex) {
                        throw new CoworkException(ex);
                    }
                }

                DataManagerServiceImpl dms = new DataManagerServiceImpl();
                String lfn = CoworkConstants.GWENDIA_HOME + "/";
                lfn = lfn.replaceAll(" ", "_");
                lfn = Normalizer.normalize(lfn, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                
               

                //transfer the file
                TransferPoolBusiness transferPoolBusiness = new TransferPoolBusiness();
                try {
                    //TODO test if file exists
                    transferPoolBusiness.uploadFile(user, gwendiaFile.getName(), lfn);
                } catch (BusinessException ex) {
                   throw new CoworkException(ex);
                }
                
                //create the application
                ApplicationBusiness ab = new ApplicationBusiness();
                
                ArrayList<String> classes = new ArrayList<String>();
                // TODO test if class exists
                classes.add(CoworkConstants.VIP_APPLICATION_CLASS);
                Application app = new Application(appName, classes, " Nadia Cerezo, Johan Montagnat. \"Scientific Workflow Reuse through Conceptual Workflows\" in Proceedings of the Proceedings of the 6th Workshop on Workflows in Support of Large-Scale Science, pages 1-10, ACM, Seattle, WA, USA, 12--18 November 2011");
                try {
                    ab.add(app);
                } catch (BusinessException ex) {
                   throw new CoworkException(ex);
                }
                
                AppVersion version = new AppVersion(app.getName(), "0.1", lfn+ "/"+gwendiaFile.getName(), true);
                try {
                    ab.addVersion(version);
                } catch (BusinessException ex) {
                   throw new CoworkException(ex);
                }
            } catch (FileNotFoundException ex) {
                throw new CoworkException(ex);
            }
        }


        
        
        
    
}
