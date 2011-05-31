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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.agent.vlet.client.VletAgentPoolClient;
import fr.insalyon.creatis.agent.vlet.common.bean.Operation;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva, Tristan Glatard
 */
public class TransferPoolServiceImpl extends RemoteServiceServlet implements TransferPoolService {

    private ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();

    public List<PoolOperation> getOperations(String userDN, String proxy) {

        try {
            VletAgentPoolClient client = new VletAgentPoolClient(
                    serverConfiguration.getVletagentHost(),
                    serverConfiguration.getVletagentPort(),
                    proxy);

            List<Operation> operationsList = client.getOperationsListByUser(userDN);
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();

            for (Operation op : operationsList) {
                String source = new File(op.getSource()).getName();
                poolOperations.add(new PoolOperation(
                        op.getId(), op.getRegistration(), source,
                        op.getDest(), op.getType().name(), op.getStatus().name(), op.getUser()));
            }

            return poolOperations;

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public PoolOperation getOperationById(String id, String proxy) {
        try {
            VletAgentPoolClient client = new VletAgentPoolClient(
                    serverConfiguration.getVletagentHost(),
                    serverConfiguration.getVletagentPort(),
                    proxy);
            Operation op = client.getOperationById(id);

            return new PoolOperation(
                    op.getId(), op.getRegistration(), op.getSource(),
                    op.getDest(), op.getType().name(), op.getStatus().name(), op.getUser());

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void removeOperationById(String id, String proxy) {
        try {
            VletAgentPoolClient client = new VletAgentPoolClient(
                    serverConfiguration.getVletagentHost(),
                    serverConfiguration.getVletagentPort(),
                    proxy);
            client.removeOperationById(id);

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
    }

    public void downloadFile(String user, String remoteFile, String userDN, String proxy) {
        try {
            VletAgentPoolClient client = new VletAgentPoolClient(
                    serverConfiguration.getVletagentHost(),
                    serverConfiguration.getVletagentPort(),
                    proxy);
            String remotePath = DataManagerUtil.parseBaseDir(user, remoteFile);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + new File(remotePath).getParent();
            client.downloadFile(remotePath, localDirPath, userDN);

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
    }

    public void uploadFile(String user, String remoteFile, String localFile, 
            String userDN, String proxy) {
        try {
            VletAgentPoolClient client = new VletAgentPoolClient(
                    serverConfiguration.getVletagentHost(),
                    serverConfiguration.getVletagentPort(),
                    proxy);

            String localPath = "file://" + serverConfiguration.getDataManagerPath()
                    + "/uploads/" + localFile;
            String remotePath = DataManagerUtil.parseBaseDir(user, remoteFile);
            client.uploadFile(localPath, remotePath, userDN);

        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
    }
}
