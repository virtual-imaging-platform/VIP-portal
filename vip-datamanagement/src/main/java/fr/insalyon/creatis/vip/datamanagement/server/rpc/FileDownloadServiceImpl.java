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
package fr.insalyon.creatis.vip.datamanagement.server.rpc;

import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.agent.vlet.client.VletAgentPoolClient;
import fr.insalyon.creatis.agent.vlet.common.bean.Operation;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
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

/**
 *
 * @author Rafael Silva
 */
public class FileDownloadServiceImpl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String operationId = req.getParameter("operationid");
        String proxy = req.getParameter("proxy");

        if (operationId != null && proxy != null && !operationId.isEmpty() && !proxy.isEmpty()) {

            try {
                ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
                VletAgentPoolClient client = new VletAgentPoolClient(
                        serverConfiguration.getVletagentHost(),
                        serverConfiguration.getVletagentPort(),
                        proxy);
                Operation operation = client.getOperationById(operationId);

                File file = new File(operation.getDest() + "/" + new File(operation.getSource()).getName());
                int length = 0;
                ServletOutputStream op = resp.getOutputStream();
                ServletContext context = getServletConfig().getServletContext();
                String mimetype = context.getMimeType(file.getName());

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
            } catch (VletAgentClientException ex) {
                ex.printStackTrace();
            }
        }
    }
}
