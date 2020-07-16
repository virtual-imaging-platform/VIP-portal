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
package fr.insalyon.creatis.vip.application.server.dao.h2;

import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.server.dao.ExecutionNodeDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Component
@Scope("prototype")
public class ExecutionNodeData extends AbstractJobData implements ExecutionNodeDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ExecutionNodeData(String dbPath) {
        super(dbPath);
    }

    /**
     *
     * @param siteID
     * @param nodeName
     * @return
     * @throws DAOException
     */
    @Override
    public Node getNode(String siteID, String nodeName) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "site, node_name, ncpus, cpu_model_name, cpu_mhz, "
                    + "cpu_cache_size, cpu_bogomips, mem_total "
                    + "FROM nodes WHERE site = ? AND node_name = ?");
            ps.setString(1, siteID);
            ps.setString(2, nodeName);

            ResultSet rs = ps.executeQuery();

            rs.next();

            Node node = new Node(rs.getString("site"), rs.getString("node_name"),
                    rs.getInt("ncpus"), rs.getString("cpu_model_name"),
                    rs.getDouble("cpu_mhz"), rs.getInt("cpu_cache_size"),
                    rs.getDouble("cpu_bogomips"), rs.getInt("mem_total"));

            ps.close();
            return node;

        } catch (SQLException ex) {
            logger.error("Error getting node {} from site {}", nodeName, siteID, ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }
}
