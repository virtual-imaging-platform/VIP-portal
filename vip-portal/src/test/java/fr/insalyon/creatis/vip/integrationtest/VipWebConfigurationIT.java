package fr.insalyon.creatis.vip.integrationtest;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.vip.api.security.SpringCompatibleUser;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
Functional test with the vip-portal configuration, so very close from
the production one. Do tests on the api.
 */
@SpringJUnitWebConfig(value = SpringCoreConfig.class)
@ActiveProfiles({"test-db", "test"}) // to take random h2 database and not the test h2 jndi one
// to disable the default mysql/innodb engine on database init
// also configure the vip conf files to be searched in classpath
@TestPropertySource(properties = {"db.tableEngine=", "vipConfigFolder=classpath:"})
@Transactional
public class VipWebConfigurationIT {

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired private GRIDAClient gridaClient;
    @Autowired private ConfigurationBusiness configurationBusiness;

    @BeforeEach
    public final void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/rest"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void testGetPipelines() throws Exception {
        User newUser = new User("firstName",
                "LastName", "testEmail@test.tst", "Test institution",
                "testPassword", "testPhone", CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        configurationBusiness.signup(newUser, "", (String) null);
        mockMvc.perform(get("/rest/pipelines")
            .with(SecurityMockMvcRequestPostProcessors.user(new SpringCompatibleUser(newUser))))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$[*]", Matchers.hasSize(0)));
    }

    @Test
    public void testGetPlatformProperties() throws Exception {
        mockMvc.perform(get("/rest/platform"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.platformName").value("VIP_TEST"));
    }



}
