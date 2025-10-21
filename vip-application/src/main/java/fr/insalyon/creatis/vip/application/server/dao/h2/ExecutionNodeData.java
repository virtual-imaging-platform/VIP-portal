package fr.insalyon.creatis.vip.application.server.dao.h2;

import fr.insalyon.creatis.vip.application.models.Node;
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
 * Each ExecutionNodeData is specific to a single database, and so to a single simulation.
 * So a new instance is needed at each call and this needs the prototype scope
 *
 * The h2 connection is configured in AbstractJobData
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
