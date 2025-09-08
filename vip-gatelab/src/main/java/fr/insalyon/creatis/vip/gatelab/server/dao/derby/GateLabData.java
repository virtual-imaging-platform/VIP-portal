package fr.insalyon.creatis.vip.gatelab.server.dao.derby;

import fr.insalyon.creatis.vip.application.server.dao.h2.AbstractJobData;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.gatelab.server.dao.GateLabDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** Each GateLabData is specific to a single database, and so to a single simulation.
 * So a new instance is needed at each call and this needs the prototype scope
 *
 * The h2 connection is configured in AbstractJobData
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
@Component
@Scope("prototype")
public class GateLabData extends AbstractJobData implements GateLabDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public GateLabData(String dbPath) throws DAOException {
        
        super(dbPath);
    }

    /**
     * 
     * @return
     * @throws DAOException 
     */
    public long getNumberParticles() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT somme FROM somme ");
            ResultSet rs = ps.executeQuery();
            boolean hasNext = rs.next();

            if ( ! hasNext) {
                // no row in somme table, no job is running yet
                ps.close();
                return 0;
            }
            
            long sum = rs.getLong("somme");
            
            ps.close();
            return sum;

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Table \"SOMME\" not found")) {
                // table does not exist, there is a problem in the h2 server or in the workflow
                // silence it for the user as it is not blocking
                logger.warn("Table Somme does not exist for {}", getDbPath(), ex);
                return 0;
            }
            logger.error("Error fetching simulation particle number for {}", getDbPath(), ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    /**
     * 
     * @throws DAOException 
     */
    public void StopWorkflowSimulation() throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE somme SET simulation = 'true' ");
            ps.execute();
            ps.close();
            
        } catch (SQLException ex) {
            logger.error("Error stopping a workflow", ex);
            throw new DAOException(ex);
        }

    }
}
