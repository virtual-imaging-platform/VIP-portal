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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanagement.client.bean.Data;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class FileCatalogServiceImpl extends RemoteServiceServlet implements FileCatalogService {

    public List<Data> listDir(String proxyFileName, String baseDir) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            List<String> list = client.getFilesAndFoldersList(baseDir);

            List<Data> dataList = new ArrayList<Data>();
            for (String d : list) {
                if (!d.isEmpty()) {
                    String[] data = d.split("--");
                    dataList.add(new Data(data[0], data[1]));
                }
            }
            return dataList;

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void delete(String proxyFileName, String path) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            client.delete(path);

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteFiles(String proxyFileName, List<String> paths) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            client.deleteFiles(paths);

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
    }

    public void createDir(String proxyFileName, String baseDir, String name) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            client.createDirectory(baseDir, name);

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
    }
}
