package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.exception.SQLRuntimeException;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class VipBeansSpringConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public Function<Connection, UserDAO> userDaoFactory() {
        return connection -> {
            try {
                return CoreDAOFactory.getDAOFactory().getUserDAO(connection);
            } catch (DAOException e) {
                logger.error("error creating user dao bean");
                throw new RuntimeException("Cannot create user dao", e);
            }
        };
    }

    @Bean
    public Supplier<Connection> connectionSupplier() {
        return () -> {
            try {
                return PlatformConnection.getInstance().getConnection();
            } catch (SQLException e) {
                // Checked exceptions are not supported so use a runtime exception
                // It will be caught in API controllers as a business exception
                // so print the stack here
                logger.error("error getting a connection for spring context", e);
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
        return new ExternalPlatformBusiness(
                new GirderStorageBusiness(
                        apiKeyBusiness()));
    }

    @Bean
    public ApiKeyBusiness apiKeyBusiness() {
        return new ApiKeyBusiness();
    }

    @Bean
    public StatsBusiness statsBusiness() {
        return new StatsBusiness();
    }
}
