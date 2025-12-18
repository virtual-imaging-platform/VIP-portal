package fr.insalyon.creatis.vip.core.server;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 * Configure the default jndi datasource for spring
 */
@Configuration
public class DataSourceConfig {

    @Bean(destroyMethod="") // needed because the application server (tomcat) must close it, not Spring
    @Qualifier("db-datasource") // there are several Datasource in spring context, qualifier is needed to avoid ambiguity
    @Profile({"default", "prod", "jndi-db"})  // needed to change database config in tests
    public DataSource jndiDataSource() {
        return new JndiDataSourceLookup().getDataSource("jdbc/vip");
    }

}
