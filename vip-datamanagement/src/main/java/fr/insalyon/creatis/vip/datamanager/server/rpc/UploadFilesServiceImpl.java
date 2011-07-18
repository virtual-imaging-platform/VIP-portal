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
package fr.insalyon.creatis.vip.datamanager.server.rpc;

import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.agent.vlet.client.VletAgentPoolClient;
import fr.insalyon.creatis.devtools.zip.UnZipper;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class UploadFilesServiceImpl extends HttpServlet {

    private static Logger logger = Logger.getLogger(UploadFilesServiceImpl.class);
    private VletAgentClient client;
    private VletAgentPoolClient poolClient;
    private String user;
    private String userdn;
    private String path;
    private boolean usePool;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        boolean overwrite = true;
        user = req.getHeader("user");
        userdn = req.getHeader("userdn");
        String proxy = req.getHeader("proxy");
        path = req.getHeader("path");
        String fileName = req.getHeader("fileName");
        boolean single = Boolean.valueOf(req.getHeader("single"));
        boolean unzip = Boolean.valueOf(req.getHeader("unzip"));
        this.usePool = Boolean.valueOf(req.getHeader("pool"));

        boolean local = path.equals("local") ? true : false;
        String rootDirectory = DataManagerUtil.getUploadRootDirectory(local);

        File uploadedFile = new File(rootDirectory + fileName);
        if (uploadedFile.exists() && overwrite) {
            uploadedFile.delete();
        }

        FileOutputStream fos = new FileOutputStream(uploadedFile);
        InputStream is = req.getInputStream();

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
        } finally {
            is.close();
            fos.close();
        }

        if (!local) {
            try {
                if (usePool) {
                    poolClient = new VletAgentPoolClient(
                            ServerConfiguration.getInstance().getVletagentHost(),
                            ServerConfiguration.getInstance().getVletagentPort(),
                            proxy);
                } else {
                    client = new VletAgentClient(
                            ServerConfiguration.getInstance().getVletagentHost(),
                            ServerConfiguration.getInstance().getVletagentPort(),
                            proxy);
                }

                if (single || !unzip) {
                    uploadFile(uploadedFile.getAbsolutePath(), path);

                } else {
                    UnZipper.unzip(uploadedFile.getAbsolutePath());
                    String dir = uploadedFile.getParent();
                    uploadedFile.delete();
                    processDir(dir, path);
                }
            } catch (DataManagerException ex) {
                logger.error(ex);
            } catch (VletAgentClientException ex) {
                logger.error(ex);
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
    }

    private void processDir(String dir, String baseDir)
            throws VletAgentClientException, DataManagerException {

        File d = new File(dir);
        for (File f : d.listFiles()) {
            if (f.isDirectory()) {
                processDir(f.getAbsolutePath(), baseDir + "/" + f.getName());
            } else {
                uploadFile(f.getAbsolutePath(), baseDir);
            }
        }
    }

    private void uploadFile(String fileName, String dir)
            throws VletAgentClientException, DataManagerException {

        if (usePool) {
            poolClient.uploadFile(fileName,
                    DataManagerUtil.parseBaseDir(user, dir), userdn);
        } else {
            client.uploadFile(fileName, DataManagerUtil.parseBaseDir(user, dir));
        }
    }
}
