/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.business.ApiContext;
import fr.insalyon.creatis.vip.api.exception.SQLRuntimeException;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.*;
import fr.insalyon.creatis.vip.core.server.dao.*;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.servlet.config.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.*;

import static fr.insalyon.creatis.vip.api.CarminProperties.CORS_AUTHORIZED_DOMAINS;

/**
 * Configuration class for spring web.
 *
 * It declares all the business beans from vip-core etc used in vip-api. All are singleton
 * (spring default) except UserDao which is created at each reference by a factory.
 *
 * It enables annotation configuration by subpackage scan.
 *
 * It declares an api conf file which location is configured from the main vip conf file
 *
 * Created by abonnet on 7/13/16.
 */
@EnableWebMvc
@ComponentScan
public class SpringWebConfig extends WebMvcConfigurerAdapter {

    public static final Logger logger = Logger.getLogger(SpringWebConfig.class);

    @Autowired
    private Environment env;
    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        // Otherwise all that follow a dot in an URL is considered an extension and removed
        // It's a problem for URL like "/pipelines/gate/3.2
        matcher.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // necessary in the content negotiation stuff of carmin data
        configurer.favorPathExtension(false);
    }

    @Bean
    public Function<Connection, UserDAO> userDaoFactory() {
        return connection -> beanFactory.getBean(UserDAO.class, connection);
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = BeanDefinition.SCOPE_PROTOTYPE)
    public UserDAO userDAO(Connection connection) {
        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO(connection);
        } catch (DAOException e) {
            logger.error("error creating user dao bean", e);
            throw new RuntimeException("Cannot create user dao", e);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
            .allowedOrigins(env.getProperty(CORS_AUTHORIZED_DOMAINS, String[].class, new String[0]));
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
    public ApiContext apiContext() {
        return new ApiContext();
    }

    @Bean
    public Supplier<Connection> connectionSupplier() {
        return () -> {
            try {
                return PlatformConnection.getInstance().getConnection();
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            }
        };
    }

    @Bean
    public WorkflowBusiness workflowBusiness() {
        return new WorkflowBusiness();
    }

    @Bean
    public ApplicationBusiness applicationBusiness() {
        return new ApplicationBusiness();
    }

    @Bean
    public ClassBusiness classBusiness() {
        return  new ClassBusiness();
    }

    @Bean
    public SimulationBusiness simulationBusiness() {
        return new SimulationBusiness();
    }

    @Bean
    public ConfigurationBusiness configurationBusiness() {
        return new ConfigurationBusiness();
    }

    @Bean
    public TransferPoolBusiness transferPoolBusiness() {
        return new TransferPoolBusiness();
    }

    @Bean
    public LFCBusiness lfcBusiness() {
        return new LFCBusiness();
    }

    @Bean
    public DataManagerBusiness dataManagerBusiness() {
        return new DataManagerBusiness();
    }

    @Bean
    public LFCPermissionBusiness lfcPermissionBusiness() {
        return new LFCPermissionBusiness();
    }

    @Bean
    public ExternalPlatformBusiness externalPlatformBusiness() {
        return new ExternalPlatformBusiness(new GirderStorageBusiness());
    }
}
