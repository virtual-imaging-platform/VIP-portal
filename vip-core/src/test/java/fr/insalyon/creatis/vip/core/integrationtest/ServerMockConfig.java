package fr.insalyon.creatis.vip.core.integrationtest;

import fr.insalyon.creatis.vip.core.server.business.Server;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        when(server.getDataManagerUsersHome()).thenReturn("/test/prefix/vip/data/test_users");
        when(server.getDataManagerGroupsHome()).thenReturn("/test/prefix/vip/data/test_groups");
        when(server.getVoRoot()).thenReturn("/vo_test/root");
    }

    @Bean
    @Primary
    public Server testServer() throws IOException {
        Server server = mock(Server.class);
        reset(server);
        return server;
    }

}
