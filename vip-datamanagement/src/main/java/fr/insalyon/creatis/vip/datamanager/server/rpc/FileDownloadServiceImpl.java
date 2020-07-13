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
package fr.insalyon.creatis.vip.datamanager.server.rpc;

import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Rafael Silva
 */
public class FileDownloadServiceImpl extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GRIDAPoolClient gridaPoolClient;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        gridaPoolClient = applicationContext.getBean(GRIDAPoolClient.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            User user = (User) req.getSession().getAttribute(CoreConstants.SESSION_USER);
            String operationId = req.getParameter("operationid");

            if (user != null && operationId != null && !operationId.isEmpty()) {

                    Operation operation = gridaPoolClient.getOperationById(operationId);

                    File file = new File(operation.getDest());
                    if (file.isDirectory()) {
                        file = new File(operation.getDest() + "/"
                                + FilenameUtils.getName(operation.getSource()));
                    }
                    int length = 0;
                    ServletOutputStream op = resp.getOutputStream();
                    ServletContext context = getServletConfig().getServletContext();
                    String mimetype = context.getMimeType(file.getName());

                    logger.info("(" + user.getEmail() + ") Downloading '" + file.getAbsolutePath() + "'.");

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
        } catch (Exception ex) {
            logger.error("Error downloading a file", ex);
            throw new ServletException(ex);
        }
    }
}
