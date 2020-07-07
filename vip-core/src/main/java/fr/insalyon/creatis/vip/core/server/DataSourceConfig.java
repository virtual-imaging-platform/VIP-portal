package fr.insalyon.creatis.vip.core.server;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(destroyMethod="") // needed because the application server (tomcat) must close it, not Spring
    @Qualifier("db-datasource")
    @Profile("default")  //needed to change db in tests
    public DataSource jndiDataSource() {
        return new JndiDataSourceLookup().getDataSource("jdbc/vip");
    }

}
