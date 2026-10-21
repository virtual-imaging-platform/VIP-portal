package fr.insalyon.creatis.vip.core.server.dao.mysql;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;


public abstract class AbstractJdbcDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected DataSource getDataSource() {
        return dataSource;
    }

    protected Connection getConnection() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource not set on " + getClass().getName());
        }
        return DataSourceUtils.getConnection(dataSource);
    }
}