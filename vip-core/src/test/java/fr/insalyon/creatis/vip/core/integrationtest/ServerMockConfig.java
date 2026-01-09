package fr.insalyon.creatis.vip.core.integrationtest;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Spring configuration class for tests.
 * <p>
 * Overrides the Server default implementation by
 * a mocked one, so this does not need a vip.conf file presence.
 */
@Configuration
@Profile("test")
public class ServerMockConfig {

    public static final String TEST_ADMIN_FIRST_NAME = "test admin";
    public static final String TEST_ADMIN_LAST_NAME = "TEST ADMIN";
    public static final String TEST_ADMIN_EMAIL = "test-admin@test.com";
    public static final String TEST_ADMIN_PASSWORD = "test-admin-password";
    public static final String TEST_ADMIN_INSTITUTION = "test admin institution";
    public static final String LAB_TRUSTSTORE_FILE = "lab_truststore_file";
    public static final String LAB_TRUSTSTORE_PASS = "lab_truststore_pass";

    // Added because the max number of simulations was 0
    public static final String MAX_NUMBER_EXECUTIONS = "5";
    public static final String TEST_CAS_URL = "testCasURL";

    // paths stuff
    public static final String TEST_USERS_ROOT = "/test/prefix/vip/data/test_users";
    public static final String TEST_GROUP_ROOT = "/test/prefix/vip/data/test_groups";
    public static final String TEST_CORS_URL = "https://test-cors-domain";

    public static void reset(Server server) {
        Mockito.reset(server);
        Mockito.when(server.getAdminFirstName()).thenReturn(TEST_ADMIN_FIRST_NAME);
        Mockito.when(server.getAdminLastName()).thenReturn(TEST_ADMIN_LAST_NAME);
        Mockito.when(server.getAdminEmail()).thenReturn(TEST_ADMIN_EMAIL);
        Mockito.when(server.getAdminPassword()).thenReturn(TEST_ADMIN_PASSWORD);
        Mockito.when(server.getAdminInstitution()).thenReturn(TEST_ADMIN_INSTITUTION);
        Mockito.when(server.getCasURL()).thenReturn(TEST_CAS_URL);
        Mockito.when(server.getTruststoreFile()).thenReturn(LAB_TRUSTSTORE_FILE);
        Mockito.when(server.getTruststorePass()).thenReturn(LAB_TRUSTSTORE_PASS);
        when(server.getMaxPlatformRunningSimulations()).thenReturn(Integer.valueOf(MAX_NUMBER_EXECUTIONS));
        when(server.getDataManagerUsersHome()).thenReturn(TEST_USERS_ROOT);
        when(server.getDataManagerGroupsHome()).thenReturn(TEST_GROUP_ROOT);
        when(server.getVoRoot()).thenReturn("/vo_test/root");
        // only CarminProperties that are used in vip-core should be mocked here, others should be in vip-api
        when(server.getApikeyHeaderName()).thenReturn("testapikey");
        when(server.getKeycloakActivated()).thenReturn(false);
        // CORS_AUTHORIZED_DOMAINS is only used in vip-api, but in the SpringRestApiConfig (servlet context),
        // which uses a Server bean internally. We mock it here, currently lacking a cleaner solution for a higher level mock.
        when(server.getCarminCorsAuthorizedDomains()).thenReturn(new String[]{TEST_CORS_URL});
    }

    @Component
    public static class CoreTestConfigurer implements TestConfigurer {

        @Autowired
        Server server;

        @Override
        public void setUpBeforeEachTest() throws DAOException {
            ServerMockConfig.reset(server);
        }
    }

    @Bean
    @Primary
    public Server testServer() throws IOException {
        Server server = mock(Server.class, withSettings().strictness(Strictness.STRICT_STUBS));
        reset(server);
        return server;
    }

}
