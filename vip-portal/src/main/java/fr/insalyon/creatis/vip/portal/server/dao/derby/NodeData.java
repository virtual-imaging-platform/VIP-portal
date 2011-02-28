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
package fr.insalyon.creatis.vip.portal.server.dao.derby;

import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.JobsConnection;
import fr.insalyon.creatis.vip.portal.client.bean.Job;
import fr.insalyon.creatis.vip.portal.client.bean.Node;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.portal.server.dao.NodeDAO;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Rafael Silva
 */
public class NodeData implements NodeDAO {

    private String workflowID;
    private Connection connection;

    public NodeData(String workflowID) {
        this.workflowID = workflowID;
        connection = JobsConnection.getInstance().connect(
                ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/jobs.db");
    }

    /**
     * 
     * @param siteID
     * @param nodeName
     * @return
     */
    public Node getNode(String siteID, String nodeName) {
        try {
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "site, node_name, ncpus, cpu_model_name, cpu_mhz, "
                    + "cpu_cache_size, cpu_bogomips, mem_total "
                    + "FROM nodes WHERE site = ? "
                    + "AND node_name = ?");
            stat.setString(1, siteID);
            stat.setString(2, nodeName);

            ResultSet rs = stat.executeQuery();

            rs.next();
            return new Node(rs.getString("site"), rs.getString("node_name"),
                    rs.getInt("ncpus"), rs.getString("cpu_model_name"),
                    rs.getDouble("cpu_mhz"), rs.getInt("cpu_cache_size"),
                    rs.getDouble("cpu_bogomips"), rs.getInt("mem_total"));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param jobID
     * @return
     */
    public Node getNodeByJobID(String jobID) {
        Job job = DAOFactory.getDAOFactory().getJobDAO(workflowID).getJob(jobID);
        return getNode(job.getSiteName(), job.getNodeName());
    }
}
