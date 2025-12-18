package fr.insalyon.creatis.vip.application.integrationtest;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.SpringInternalApiConfig;

@ContextConfiguration(classes = { SpringInternalApiConfig.class })
public class ResourceControllerIT extends BaseSpringIT {

    @Autowired
    private WebApplicationContext wac;

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
    public void add() throws Exception {
        Resource resource = new Resource("test");

        // forbidden for classic User
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(1001))
                .andExpect(status().is4xxClientError());

        // forbidden for developer User
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(1001))
                .andExpect(status().is4xxClientError());

        // ok for admin
        MvcResult result = mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(resource));
    }

    @Test
    public void update() throws Exception {
        Resource resource = new Resource("test");

        // create first
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(status().isOk());

        resource.setConfiguration("ma super configuration");

        // wrong permissions
        mockMvc.perform(put("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(1001))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(put("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(1001))
                .andExpect(status().is4xxClientError());

        // update resource wrong ids
        mockMvc.perform(put("/internal/resources/bad_id")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(8009));

        // update & check
        MvcResult result = mockMvc.perform(put("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(resource));
    }

    @Test
    public void remove() throws Exception {
        Resource resource = new Resource("test");

        // create first
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(status().isOk());

        resource.setConfiguration("ma super configuration");

        // wrong permissions (= NotFound)
        mockMvc.perform(delete("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(1000))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(1000))
                .andExpect(status().is4xxClientError());

        // delete
        mockMvc.perform(put("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(status().isOk());
    }

    @Test
    public void getResource() throws Exception {
        createGroup("private", GroupType.RESOURCE, false);
        createGroup("public", GroupType.RESOURCE, true);

        Group groupPrivate = groupBusiness.get("private");
        Group groupPublic = groupBusiness.get("public");
        Resource resource = new Resource("test");

        resource.setGroups(List.of(groupPrivate, groupPublic));
        configurationBusiness.addUserToGroup(emailUser2, "private");
        developperUser = configurationBusiness.getUserWithGroups(emailUser2);

        // create first
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(status().isOk());

        // wrong
        mockMvc.perform(get("/internal/resources/wrong")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(1000))
                .andExpect(status().is4xxClientError());

        // not access (basic user)
        mockMvc.perform(get("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(1000))
                .andExpect(status().is4xxClientError());

        // access because private group of developer
        mockMvc.perform(get("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(resource.getName()))
                // should only see the private group and not the public group!
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(status().isOk());

        // ok admin
        mockMvc.perform(get("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(resource.getName()))
                .andExpect(jsonPath("$.groups", hasSize(2)))
                .andExpect(status().isOk());
    }

    @Test
    public void getList() throws Exception {
        Resource resource1 = new Resource("testA");
        Resource resource2 = new Resource("testB");
        Resource resource3 = new Resource("testC");
        createGroup("test", GroupType.RESOURCE, true);

        Group group = groupBusiness.get("test");
        resource1.setGroups(List.of(group));

        // create resources
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(resource1)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(resource2)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(resource3)))
                .andExpect(status().isOk());

        // all
        mockMvc.perform(get("/internal/resources")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(status().isOk());

        // filter by group
        mockMvc.perform(get("/internal/resources?group=test")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(status().isOk());
    }
}
