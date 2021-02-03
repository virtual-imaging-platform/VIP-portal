package fr.insalyon.creatis.vip.integrationtest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;

/*
 h2 local database configuration to override the default jndi one
 */
@Configuration
@Profile("local-db")
public class LocalDbConfiguration {

    @Bean
    @Qualifier("db-datasource")
    public DataSource localDataSource(Resource vipConfigFolder) throws IOException {
        String h2url = "jdbc:h2:" + vipConfigFolder.getFile().getAbsolutePath() + "/vip";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(org.h2.Driver.class.getCanonicalName());
        dataSource.setUrl(h2url);
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
