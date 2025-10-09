package fr.insalyon.creatis.vip.application.server.dao.h2;

import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates dao for the h2 database of a simulation.
 * Each dao is specific to a single database, and so to a single simulation.
 *
 * The default is to access the h2 database through an h2 server via tcp,
 * but this is changeable to use (for instance) a memory or a local h2
 * database for testing or local use
 */
public abstract class AbstractJobData {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String DRIVER = "org.h2.Driver";

    @Value("${workflows.db.scheme:tcp}")
    private String workflowsScheme = "tcp";

    protected Server server;
    private String dbPath;
    protected Connection connection;

    @Autowired
    public final void setServer(Server server) {
        this.server = server;
    }

    public AbstractJobData(String dbPath) {
        this.dbPath = dbPath;
    }

    protected String getDbPath() {
        return dbPath;
    }

    @PostConstruct
    public final void initConnection() throws DAOException {
        try {
            Class.forName(DRIVER);
            StringBuilder dbUrl = new StringBuilder();
            dbUrl.append("jdbc:h2:").append(workflowsScheme).append(":");
            if ("tcp".equals(workflowsScheme)) {
                // if tcp, add server and port
                // otherwise, its a local file, only the path is needed
                dbUrl.append("//")
                        .append(server.getWorkflowsHost())
                        .append(":9092/");
            }
            dbUrl.append(server.getWorkflowsPath())
                    .append("/")
                    .append(dbPath)
                    .append("/db/jobs");

            connection = DriverManager.getConnection(dbUrl.toString(), "gasw", "gasw");
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException | SQLException ex) {
            logger.error("Error creating database connection for {}", dbPath,ex);
            throw new DAOException(ex);
        }
    }
    

    /**
     * Closes database connection.
     */
    protected void close(Logger logger) {

        try {
            connection.close();
        } catch (SQLException ex) {
            logger.error("Error closing connection", ex);
        }
    }
}
