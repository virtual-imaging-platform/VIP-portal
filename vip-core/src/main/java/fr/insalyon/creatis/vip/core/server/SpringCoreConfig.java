package fr.insalyon.creatis.vip.core.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
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
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.UnknownHostException;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * Root spring configuration class.
 * Basically, the @ComponentScan annotation allows spring to scan all vip
 * classes and to create/inject automatically all the beans / service. Note that
 * the GWT servlet cannot be created this way (they are created by the application
 * server / tomcat) through the web.xml file.
 *
 * This also configures the database/transaction/connection : spring will
 * automatically handle transactions and connection creation (and closing)
 * by annotating classes with @Transactional and using spring utils to get
 * the connection in the dao
 *
 * This also creates spring beans for services coming from maven dependencies (grida and sma)
 *
 * This also manage the vip configuration folder, defaulting to "$HOME/.vip" but
 * allowing change for tests and local use.
 */
@Configuration
@EnableTransactionManagement
// The root context doesn't contain any controller, so at first it doesn't have to be a WebMvc context.
// However, we do want to have Spring Security in the root context, and Spring mandates that
// Spring Security & Spring MVC are configured in a shared ApplicationContext.
// This constraint implies that the root context must also be a WebMvc one.
@EnableWebMvc
@ComponentScan(
        basePackages = "fr.insalyon.creatis.vip",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        }
)
public class SpringCoreConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringCoreConfig.class);

    /*
    wrapper around the "real" datasource to open a connection only when needed
    (and not every time a @Transactional method is called)
    The "real" datasource must have the "db-datasource" qualifier
     */
    @Bean
    @Primary // to indicate to spring this is the datasource to inject
    public LazyConnectionDataSourceProxy lazyDataSource(@Qualifier("db-datasource") DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    /*
    Spring service that create a transaction (and a connection) automatically
    in classes with the @Transactional annotation (so the business layer)
     */
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
    public SMAClient smaClient(Server server) throws UnknownHostException {
        return new SMAClient(server.getSMAHost(), server.getSMAPort());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }

    // to verify the @Value injection existence
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    // to handle list in spring @value
    @Bean
    public static ConversionService conversionService() {
        return new DefaultConversionService();
    }

    /*
    find the vip configuration folder. This defaults to $HOME/.vip as this is
    the traditional behavior.
    This is changeable through the vipConfigFolder property which can be given
    as a JVM parameter or a system environment variable. This can be changed
    to a absolute path or a relative classpath.
     */
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
