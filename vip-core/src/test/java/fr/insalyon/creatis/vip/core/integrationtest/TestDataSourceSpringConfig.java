package fr.insalyon.creatis.vip.core.integrationtest;

import javax.sql.DataSource;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * h2 memory embedded database for tests, which is erased at the end of a
 * test run.
 * the datasource is also wrapped by a mockito spy to verify how it is used
 *
 * only activated when there is the "test-db" profile
 */
@Configuration
@Profile("test-db") // to be only used when wanted
public class TestDataSourceSpringConfig {

    @Bean
    @Qualifier("db-datasource")
    public DataSource testDataSource() {
        return Mockito.spy(new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .build());
    }
}
