package fr.insalyon.creatis.vip.core.integrationtest;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;
import org.springframework.stereotype.Component;

/**
 * Spring configuration class for tests.
 *
 * Overrides the dependency services (grida, proxyclient, email) by mocked ones
 * Configure them atomically with a TestConfigurer
 */
@Configuration
@Profile("test")
public class SpringTestConfig {

    @Component
    public static class CoreTestConfigurer implements TestConfigurer {

        @Autowired GRIDAClient gridaClient;
        @Autowired EmailBusiness emailBusiness;

        @Override
        public void setUpBeforeEachTest() throws DAOException {
            Mockito.reset(gridaClient);
            Mockito.doReturn(new String[]{"test@admin.test"}).when(emailBusiness).getAdministratorsEmails();
        }
    }

    @Bean @Primary
    public GRIDAClient testGridaClient() {return Mockito.mock(GRIDAClient.class);}

    @Bean @Primary
    public GRIDAPoolClient testGridaPoolClient() {return Mockito.mock(GRIDAPoolClient.class);}

    @Bean @Primary
    public EmailBusiness testEmailBusiness() {return Mockito.mock(EmailBusiness.class);}

    @Bean @Primary
    public ProxyClient testProxyClient() {return Mockito.mock(ProxyClient.class);}
}
