package fr.insalyon.creatis.vip.core.integrationtest;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;

/**
 * Spring configuration class for tests.
 *
 * Overrides the dependency services (grida, proxyclient, email) by mocked ones
 */
@Configuration
@Profile("test")
public class SpingTestConfig {

    @Bean @Primary
    public GRIDAClient testGridaClient() {return Mockito.mock(GRIDAClient.class);}

    @Bean @Primary
    public GRIDAPoolClient testGridaPoolClient() {return Mockito.mock(GRIDAPoolClient.class);}

    @Bean @Primary
    public EmailBusiness testEmailBusiness() {return Mockito.mock(EmailBusiness.class);}

    @Bean @Primary
    public ProxyClient testProxyClient() {return Mockito.mock(ProxyClient.class);}
}
