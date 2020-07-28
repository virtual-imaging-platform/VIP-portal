package fr.insalyon.creatis.vip.core.server;

import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.client.GRIDAZombieClient;
import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.function.Consumer;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "fr.insalyon.creatis.vip")
public class SpringCoreConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringCoreConfig.class);

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

    // to verify the @Value injection existence
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    // to handle list en spring @value
    @Bean
    public static ConversionService conversionService() {
        return new DefaultConversionService();
    }

    // to find properties file
    @Bean
    public static Resource vipConfigFolder(
            ConfigurableApplicationContext configurableApplicationContext) throws IOException {
        ConfigurableEnvironment env = configurableApplicationContext.getEnvironment();
        // look for configLocation in environment
        String configFolder = env.getProperty("vipConfigFolder");
        // if present, look for file
        if (configFolder != null) {
            logger.info("found vipConfigFolder property : {}", configFolder);
        } else {
            // if not, look in user home folder
            configFolder = env.getProperty("user.home")  + Server.VIP_DIR;
            logger.info("vipConfigFolder property not found, using user-home : {}", configFolder);
        }
        Resource vipConfigFolder;
        if ( configFolder.startsWith(CLASSPATH_URL_PREFIX)) {
            vipConfigFolder = new ClassPathResource(configFolder.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            vipConfigFolder = new FileSystemResource(configFolder);
        }
        if ( ! vipConfigFolder.exists() &&
                ! vipConfigFolder.getFile().mkdir()) {
            logger.error("Cannot create VIP config folder : {}", vipConfigFolder);
            throw new BeanInitializationException("Cannot create VIP config folder");
        }
        return vipConfigFolder;
    }

}
