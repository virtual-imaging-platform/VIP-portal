
package fr.insalyon.creatis.vip.query.server.dao.persistance;


import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class MySQLDAOFactory extends QueryDAOFactory {

    private final static Logger logger = Logger.getLogger(MySQLDAOFactory.class);
    private static QueryDAOFactory instance;

    // Singleton
    protected static QueryDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    private MySQLDAOFactory() {

        try {
            logger.info("Configuring VIP Application database.");

            PlatformConnection.getInstance().createTable("Query",
                    "name VARCHAR(255), "
                    + "description VARCHAR(255)");

            

        } catch (DAOException ex) {
            logger.error(ex);
        }
    }

    @Override
    public QueryDAO getQueryDAO() throws DAOException
    {
         return new QueryData();
    }
    
}
