/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.datamanager.server.rpc;

import fr.insalyon.creatis.agent.vlet.client.VletAgentPoolClient;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Rafael Silva
 */
public class FileUploadServiceImpl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rootDirectory = ServerConfiguration.getInstance().getDataManagerPath() 
                + "/uploads/" + System.nanoTime() + "/";
        File dir = new File(rootDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (ServletFileUpload.isMultipartContent(request)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List items = upload.parseRequest(request);
                Iterator iter = items.iterator();
                String fileName = null;
                FileItem fileItem = null;
                String userdn = null;
                String user = null;
                String proxy = null;
                String path = null;

                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    if (item.getFieldName().equals("userdn")) {
                        userdn = item.getString();
                    } else if (item.getFieldName().equals("user")) {
                        user = item.getString();
                    } else if (item.getFieldName().equals("proxy")) {
                        proxy = item.getString();
                    } else if (item.getFieldName().equals("path")) {
                        path = item.getString();
                    } else if (item.getFieldName().equals("file")) {
                        fileName = item.getName();
                        fileItem = item;
                    }
                }
                if (fileName != null && !fileName.equals("")) {
                    fileName = new File(fileName).getName();
                    File uploadedFile = new File(rootDirectory + fileName);
                    try {
                        fileItem.write(uploadedFile);
                        response.getWriter().write(fileName);

                        if(!path.equals("local")){//otherwise we don't want to upload the file to the grid
                            // Vlet Agent Pool Client
                            VletAgentPoolClient client = new VletAgentPoolClient(
                                ServerConfiguration.getInstance().getVletagentHost(),
                                ServerConfiguration.getInstance().getVletagentPort(),
                                proxy);
                            client.uploadFile(
                                uploadedFile.getAbsolutePath(),
                                DataManagerUtil.parseBaseDir(user, path),
                                userdn);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                response.setContentType("text/html");
                response.setHeader("Pragma", "No-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache");
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<body>");
                out.println("<script type=\"text/javascript\">");
                out.println("if (parent.uploadComplete) parent.uploadComplete('"
                        + fileName + "');");
                out.println("</script>");
                out.println("</body>");
                out.println("</html>");
                out.flush();

            } catch (FileUploadException ex) {
                ex.printStackTrace();
            }
        }
    }
}
