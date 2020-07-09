package fr.insalyon.creatis.vip.core.server;

import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.client.GRIDAZombieClient;
import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "fr.insalyon.creatis.vip")
public class SpringCoreConfig {

    @Bean
    @Primary
    public LazyConnectionDataSourceProxy lazyDataSource(@Qualifier("db-datasource") DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(LazyConnectionDataSourceProxy lazyDataSource) {
        return new DataSourceTransactionManager(lazyDataSource);
    }

    // VIP dependencies beans

    @Bean
    public GRIDAClient gridaClient(Server server) {
        return new GRIDAClient(
                server.getGRIDAHost(),
                server.getGRIDAPort(),
                server.getServerProxy(server.getVoName()));
    }

    @Bean
    public GRIDAPoolClient gridaPoolClient(Server server) {
        return new GRIDAPoolClient(
                server.getGRIDAHost(),
                server.getGRIDAPort(),
                server.getServerProxy(server.getVoName()));
    }

    @Bean
    public GRIDACacheClient gridaCacheClient(Server server) {
        return new GRIDACacheClient(
                server.getGRIDAHost(),
                server.getGRIDAPort(),
                server.getServerProxy(server.getVoName()));
    }

    @Bean
    public GRIDAZombieClient gridaZombieClient(Server server) {
        return new GRIDAZombieClient(
                server.getGRIDAHost(),
                server.getGRIDAPort(),
                server.getServerProxy(server.getVoName()));
    }

    @Bean
    public SMAClient smaClient(Server server) {
        return new SMAClient(server.getSMAHost(), server.getSMAPort());
    }
}
