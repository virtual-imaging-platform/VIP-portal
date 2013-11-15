/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.query.server.rpc;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author nouha
 */
public class FileDownload extends HttpServlet {

    private static Logger logger = Logger.getLogger(FileDownload.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute(CoreConstants.SESSION_USER);
        String queryId = req.getParameter("queryid");
        String path=req.getParameter("path");
        String queryName=req.getParameter("name");
        String tab[]=path.split("/");
        
        if (user != null && queryId != null && !queryId.isEmpty()) {

            try {
                //GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
               // Operation operation = client.getOperationById(operationId);
           String k=new String();
               // File file = new File(operation.getDest());
           int l=tab.length;
                for (int i=0;i<l-1;i++){
                     k+="//"+tab[i];
                }
                File file = new File(k);
                logger.info("that"+k);
                if (file.isDirectory()) {
                    //file = new File(operation.getDest() + "/" 
                            //+ FilenameUtils.getName(operation.getSource()));
                    file = new File(k+"/"+tab[l-1]);
                   
                }
                int length = 0;
                ServletOutputStream op = resp.getOutputStream();
                ServletContext context = getServletConfig().getServletContext();
                String mimetype = context.getMimeType(file.getName());
                
                logger.info("(" + user.getEmail() + ") Downloading '" + file.getAbsolutePath() + "'.");

                resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
                resp.setContentLength((int) file.length());
                //name of the file in servlet download
                resp.setHeader("Content-Disposition", "attachment; filename=\""
                        + queryName+"_"+getCurrentTimeStamp() + "\"");

                byte[] bbuf = new byte[4096];
                DataInputStream in = new DataInputStream(new FileInputStream(file));

                while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, length);
                }

                in.close();
                op.flush();
                op.close();
            } catch (Exception ex) {
                logger.error(ex);
            }
            
            
            
            
            
            
        }
    }
    
    
    
    
     private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }
}
