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

import fr.insalyon.creatis.devtools.zip.UnZipper;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.*;
import java.text.Normalizer;
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
    private GRIDAClient client;
    private GRIDAPoolClient poolClient;
    private User user;
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
        try {
            user = CoreDAOFactory.getDAOFactory().getUserDAO().getUserBySession(req.getHeader(CoreConstants.COOKIES_SESSION));

            boolean overwrite = true;
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
                if (usePool) {
                    poolClient = CoreUtil.getGRIDAPoolClient();
                } else {
                    client = CoreUtil.getGRIDAClient();
                }

                if (single || !unzip) {
                    uploadFile(uploadedFile.getAbsolutePath(), path);

                } else {
                    UnZipper.unzip(uploadedFile.getAbsolutePath());
                    String dir = uploadedFile.getParent();
                    uploadedFile.delete();
                    processDir(dir, path);
                }
            } else {
                logger.info("(" + user.getEmail() + ") Uploaded local file '" + uploadedFile.getAbsolutePath() + "'.");
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

        } catch (IOException ex) {
            logger.error(ex);
            throw new ServletException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new ServletException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new ServletException(ex);
        } catch (DAOException ex) {
            throw new ServletException(ex);
        }
    }

    private void processDir(String dir, String baseDir)
            throws GRIDAClientException, DataManagerException {

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
            throws GRIDAClientException, DataManagerException {

        String parsed = fileName.trim().replaceAll(" ", "_");
        parsed = Normalizer.normalize(parsed, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        if (!parsed.equals(fileName)) {
            new File(fileName).renameTo(new File(parsed));
            fileName = parsed;
        }

        logger.info("(" + user.getEmail() + ") Uploading '" + fileName + "' to '" + dir + "'.");
        if (usePool) {
            poolClient.uploadFile(fileName,
                    DataManagerUtil.parseBaseDir(user, dir), user.getEmail());
        } else {
            client.uploadFile(fileName, DataManagerUtil.parseBaseDir(user, dir));
        }
    }
}
