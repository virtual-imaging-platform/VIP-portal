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

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;

public class ResourceControllerIT extends BaseInternalApiSpringIT {

    private User adminUser;
    private User developperUser;
    private User basicUser;
    private Group groupPrivate;
    private Group groupPublic;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

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
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                .andExpect(status().is4xxClientError());

        // forbidden for developer User
        mockMvc.perform(post("/internal/resources")
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
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

        // forbidden for users and developers
        mockMvc.perform(put("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(put("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                .andExpect(status().is4xxClientError());

        // update resource wrong ids
        mockMvc.perform(put("/internal/resources/bad_id")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.BAD_INPUT_FIELD.getCode()));

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

        // wrong permissions (= NotFound) for users and devs
        mockMvc.perform(delete("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()))
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
        asAdminContext(() -> {
            createGroup("private", GroupType.RESOURCE, false);
            createGroup("public", GroupType.RESOURCE, true);

            groupPrivate = groupBusiness.get("private");
            groupPublic = groupBusiness.get("public");
        });
        Resource resource = new Resource("test");

        resource.setGroups(Set.of(groupPrivate, groupPublic));
        userBusiness.addUserToGroup(emailUser2, "private");
        developperUser = userBusiness.getUserWithGroups(emailUser2);

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
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()))
                .andExpect(status().is4xxClientError());

        // not access (basic user)
        mockMvc.perform(get("/internal/resources/" + resource.getName())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()))
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

        asAdminContext(() -> {
            createGroup("test", GroupType.RESOURCE, true);
            groupPublic = groupBusiness.get("test");
        });
        resource1.setGroups(Set.of(groupPublic));

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
