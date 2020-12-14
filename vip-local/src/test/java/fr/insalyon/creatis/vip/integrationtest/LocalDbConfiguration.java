package fr.insalyon.creatis.vip.integrationtest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/*
 h2 local database configuration to override the default jndi one
 */
@Configuration
@Profile("local-db")
public class LocalDbConfiguration {

    @Bean
    @Qualifier("db-datasource")
    public DataSource localDataSource(@Value("${local.h2db.url}") String h2url) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.h2.Driver.class.getCanonicalName());
        dataSource.setUrl(h2url);
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
