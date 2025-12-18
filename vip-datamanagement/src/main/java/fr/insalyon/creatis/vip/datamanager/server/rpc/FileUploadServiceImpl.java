package fr.insalyon.creatis.vip.datamanager.server.rpc;

import java.io.File;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.List;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletDiskFileUpload;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.insalyon.creatis.devtools.zip.UnZipper;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FileUploadServiceImpl extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GRIDAClient client;
    private GRIDAPoolClient poolClient;
    private LfcPathsBusiness lfcPathsBusiness;
    private DataManagerBusiness dataManagerBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext =
                WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        client = applicationContext.getBean(GRIDAClient.class);
        poolClient = applicationContext.getBean(GRIDAPoolClient.class);
        lfcPathsBusiness = applicationContext.getBean(LfcPathsBusiness.class);
        dataManagerBusiness = applicationContext.getBean(DataManagerBusiness.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            User user = (User) request.getSession().getAttribute(CoreConstants.SESSION_USER);
            logger.info("upload received from " + user.getEmail());
            if (user != null && JakartaServletFileUpload.isMultipartContent(request)) {

                DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
                JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);
                List<DiskFileItem> items = upload.parseRequest(request);
                String fileName = null;
                DiskFileItem fileItem = null;
                String path = null;
                String target = "uploadComplete";
                boolean single = true;
                boolean unzip = true;
                boolean usePool = true;
                String operationID = "no-id";

                for (DiskFileItem item : items) {
                    switch (item.getFieldName()) {
                        case "path":
                            path = item.getString();
                            break;
                        case "file":
                            fileName = item.getName();
                            fileItem = item;
                            break;
                        case "target":
                            target = item.getString();
                            break;
                        case "single":
                            single = Boolean.valueOf(item.getString());
                            break;
                        case "unzip":
                            unzip = Boolean.valueOf(item.getString());
                            break;
                        case "pool":
                            usePool = Boolean.valueOf(item.getString());
                            break;
                        default:
                            logger.error("File upload : invalid FieldName {}", item.getFieldName());
                            throw new IllegalArgumentException("Invalid FieldName: " + item.getFieldName());
                    }

                }
                if (fileName != null && !fileName.equals("")) {

                    boolean local = path.equals("local");
                    String rootDirectory = dataManagerBusiness.getUploadRootDirectory(local);
                    fileName = DataManagerUtil.getCleanFilename(fileName);
                    File uploadedFile = new File(rootDirectory + fileName);

                    try {
                        fileItem.write(uploadedFile.toPath());
                        response.getWriter().write(fileName);

                        if (!local) {
                            // GRIDA Client
                            logger.info("(" + user.getEmail() + ") Uploading '" + uploadedFile.getAbsolutePath() + "' to '" + path + "'.");
                            if (single || !unzip) {
                                operationID = uploadFile(user, uploadedFile.getAbsolutePath(), path, usePool);
                            } else {
                                UnZipper.unzip(uploadedFile.getAbsolutePath());
                                String dir = uploadedFile.getParent();
                                uploadedFile.delete();
                                operationID = processDir(user, dir, path, usePool);
                                if (operationID.endsWith("##")) {
                                    operationID = operationID.substring(0, operationID.length() - 2);
                                }
                            }

                        } else {
                            operationID = fileName;
                            logger.info("(" + user.getEmail() + ") Uploaded '" + uploadedFile.getAbsolutePath() + "'.");
                        }
                    } catch (Exception ex) {
                        logger.error("Error uploading a file", ex);
                    }
                }
                //TODO: change the HTML/JS response to XML data that could be directly processed in JS
                response.setContentType("text/html");
                response.setHeader("Pragma", "No-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache");
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<body>");
                out.println("<script type=\"text/javascript\" id=\"runscript\">");
                out.println("if (parent." + target + ") parent." + target + "('"
                        + operationID + "');");
                out.println("</script>");
                out.println("</body>");
                out.println("</html>");
                out.flush();


            }
        } catch (Exception ex) {
            logger.error("Error uploading a file", ex);
            throw new ServletException(ex);
        }
    }

    private String processDir(User user, String dir, String baseDir, boolean usePool)
            throws GRIDAClientException, DataManagerException {

        StringBuilder ids = new StringBuilder();
        for (File f : new File(dir).listFiles()) {
            if (f.isDirectory()) {
                ids.append(
                        processDir(
                                user,
                                f.getAbsolutePath(),
                                baseDir + "/" + f.getName(),
                                usePool));
            } else {
                ids.append(
                        uploadFile(
                                user, f.getAbsolutePath(), baseDir, usePool));
                ids.append("##");
            }
        }

        return ids.toString();
    }

    private String uploadFile(User user, String fileName, String dir, boolean usePool)
            throws GRIDAClientException, DataManagerException {

        String parsed = fileName.trim().replaceAll(" ", "_");
        parsed = Normalizer.normalize(parsed, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        if (!parsed.equals(fileName)) {
            new File(fileName).renameTo(new File(parsed));
            fileName = parsed;
        }

        logger.info("(" + user.getEmail() + ") Uploading '" + fileName + "' to '" + dir + "'.");
        if (usePool) {
            return poolClient.uploadFile(
                    fileName,
                    lfcPathsBusiness.parseBaseDir(user, dir),
                    user.getEmail());
        } else {
            client.uploadFile(fileName, lfcPathsBusiness.parseBaseDir(user, dir));
            return "no-id";
        }
    }
}
