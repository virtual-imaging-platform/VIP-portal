package fr.insalyon.creatis.vip.core.server.rpc;

import fr.insalyon.creatis.devtools.zip.FolderZipper;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.apache.commons.io.FileUtils;
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

public class GetFileServiceImpl extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserDAO userDAO;
    private Server server;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext = WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        userDAO = applicationContext.getBean(UserDAO.class);
        server = applicationContext.getBean(Server.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        try {
            User user = userDAO.getUserBySession(
                    req.getParameter(CoreConstants.COOKIES_SESSION));

            String filepath = req.getParameter("filepath");

            if (filepath != null && !filepath.isEmpty()) {

                File file = new File(server.getWorkflowsPath()
                        + filepath);

                boolean isDir = false;
                if (file.isDirectory()) {
                    String zipName = file.getAbsolutePath() + ".zip";
                    FolderZipper.zipFolder(file.getAbsolutePath(), zipName);
                    filepath = zipName;
                    file = new File(zipName);
                    isDir = true;
                }

                logger.info("(" + user.getEmail() + ") Downloading file '" + filepath + "'.");
                ServletOutputStream op = resp.getOutputStream();
                ServletContext context = getServletConfig().getServletContext();
                String mimetype = context.getMimeType(file.getName());

                resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
                resp.setContentLength((int) file.length());
                resp.setHeader("Content-Disposition", "attachment; filename=\""
                        + file.getName() + "\"");

                byte[] bbuf = new byte[4096];
                DataInputStream in = new DataInputStream(new FileInputStream(file));

                int length;
                while ((length = in.read(bbuf)) != -1) {
                    op.write(bbuf, 0, length);
                }

                in.close();
                op.flush();
                op.close();

                if (isDir) {
                    FileUtils.deleteQuietly(file);
                }
            }
        } catch (Exception ex) {
            logger.error("Error downloading a file", ex);
            throw new ServletException(ex);
        }
    }
}
