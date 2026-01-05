package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.models.Tag.ValueType;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.SpringInternalApiConfig;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

@ContextConfiguration(classes = { SpringInternalApiConfig.class })
public class TagControllerIT extends BaseSpringIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TagBusiness tagBusiness;

    @Autowired
    private AppVersionBusiness appVersionBusiness;

    @Autowired
    private ApplicationDAO applicationDAO;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private User adminUser;
    private User developperUser;
    private User basicUser;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/internal"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        mapper = new ObjectMapper();

        adminUser = createUser(emailUser1, UserLevel.Administrator);
        developperUser = createUser(emailUser2, UserLevel.Developer);
        basicUser = createUser(emailUser3, UserLevel.Beginner);
    }

    @Test
    public void list() throws Exception {
        Application app = new Application("appA", "bla");
        AppVersion version = new AppVersion("appA", "0.1", "{}", true);
        Tag a = new Tag("test", "test", ValueType.STRING, "appA", "0.1", true, true);
        Tag b = new Tag("test2", "true", ValueType.BOOLEAN, "appA", "0.1", false, false);

        applicationDAO.add(app);
        appVersionBusiness.add(version);
        tagBusiness.add(a);
        tagBusiness.add(b);

        // not the rights
        mockMvc.perform(get("/internal/tags")
            .with(getUserSecurityMock(basicUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errorCode").value(1001))
                    .andExpect(status().is4xxClientError());

        // not the rights
        mockMvc.perform(get("/internal/tags")
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errorCode").value(1001))
                    .andExpect(status().is4xxClientError());

        // ok
        MvcResult result = mockMvc.perform(get("/internal/tags")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(2))
                    .andExpect(jsonPath("$.data").isArray())
                    .andReturn();

        PrecisePage<Tag> page = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PrecisePage<Tag>>() {});
        assertTrue(page.data.contains(a));
        assertTrue(page.data.contains(b));
    }
}
