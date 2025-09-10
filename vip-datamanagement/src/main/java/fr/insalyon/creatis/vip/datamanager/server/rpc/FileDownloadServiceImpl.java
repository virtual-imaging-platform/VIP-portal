package fr.insalyon.creatis.vip.datamanager.server.rpc;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness.LFCAccessType;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

    /**
 *
 * @author Rafael Silva
 */
public class FileDownloadServiceImpl extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GRIDAPoolClient gridaPoolClient;
    private LFCPermissionBusiness lfcPermissionBusiness;
    private LfcPathsBusiness lfcPathsBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext =
                WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        lfcPathsBusiness = applicationContext.getBean(LfcPathsBusiness.class);
        gridaPoolClient = applicationContext.getBean(GRIDAPoolClient.class);
        lfcPermissionBusiness = applicationContext.getBean(LFCPermissionBusiness.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            User user = (User) req.getSession().getAttribute(CoreConstants.SESSION_USER);
            String operationId = req.getParameter("operationid");
            String vipPath = req.getParameter("path");

            if (user == null) {
                logger.warn("Download from an unlogged user (operationid : {}, path : {})",
                        operationId, vipPath);
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            if (operationId != null) {
                downloadByOperationId(user, operationId, resp);
            } else if (vipPath != null) {
                downloadByPath(user, vipPath, resp);
            } else {
                logger.warn("Download without operation nor path by {}", user.getEmail());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No operation id or path provided");
            }
        } catch (Exception ex) {
            logger.error("Error downloading a file", ex);
            throw new ServletException("Error downloading a file", ex);
        }
    }

    private void downloadByOperationId(
            User user, String operationId, HttpServletResponse resp)
            throws GRIDAClientException, IOException, ServletException {
        if (operationId.isEmpty()) {
            logger.warn("Empty operationid for download for user {}", user.getEmail());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty operation id");
            return;
        }

        Operation operation = gridaPoolClient.getOperationById(operationId);

        File file = new File(operation.getDest());
        if (file.isDirectory()) {
            file = new File(operation.getDest() + "/"
                    + FilenameUtils.getName(operation.getSource()));
        }
        downloadFile(user, file, resp);
    }

    private void downloadByPath(
            User user, String vipPath, HttpServletResponse resp)
            throws IOException, BusinessException, DataManagerException, ServletException {
        if (vipPath.isEmpty()) {
            logger.warn("Empty operationid for download for user {}", user.getEmail());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty operation id");
            return;
        }

        Path path = Paths.get(vipPath);
        if ( ! path.isAbsolute()) {
            logger.error("download path [{}] should be absolute for user {}", path, user.getEmail());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path should be absolute");
            return;
        }

        if ( ! lfcPermissionBusiness.isLFCPathAllowed(
                user, vipPath, LFCAccessType.READ, true)) {
            logger.error("download path [{}] not allowed for user {}", path, user.getEmail());
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access forbidden to this file");
            return;
        }

        String remotePath = lfcPathsBusiness.parseBaseDir(user, vipPath);
        String localFilePath = lfcPathsBusiness.getLocalDownloadPath(remotePath);
        File localFile = new File(localFilePath);
        downloadFile(user, localFile, resp);
    }

    private void downloadFile(User user, File file, HttpServletResponse resp) throws IOException, ServletException {
        if ( ! file.exists() || file.isDirectory()) {
            logger.error("({}) download file [{}] not existing or is a directory",
                    user.getEmail(), file.getAbsolutePath());
            throw new ServletException("Internal error downloading a file");
        }

        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        ServletContext context = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(file.getName());

        logger.info("({}) Downloading '{}'.",user.getEmail(), file.getAbsolutePath());

        resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");

        byte[] bbuf = new byte[4096];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
            op.write(bbuf, 0, length);
        }

        in.close();
        op.flush();
        op.close();
    }
}
