package fr.insalyon.creatis.vip.core.server.dao.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Transactional
public class TableInitializer extends JdbcDaoSupport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${db.tableEngine:InnoDB}")
    private String tableEngine = "InnoDB";

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public boolean createTable(String name, String columnsDefinition) {

        try {
            String suffix = tableEngine.isEmpty() ? "" : " ENGINE=" + tableEngine;
            Statement stat = getConnection().createStatement();
            stat.executeUpdate("CREATE TABLE " + name + " ("
                    + columnsDefinition + ")" + suffix);

            logger.info("Table " + name + " successfully created.");
            return true;

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("already exists")) {
                logger.error("Error creating db table {}", name, ex);
            }
            return false;
        }
    }
}
