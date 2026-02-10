package fr.insalyon.creatis.vip.application.integrationtest;

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

import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;

public class ApplicationControllerIT extends BaseInternalApiSpringIT {
    
    private User adminUser;
    private User developperUser;
    private User basicUser;
    private Group group;
    private Group group2;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        adminUser = createUser(emailUser1, UserLevel.Administrator);
        developperUser = createUser(emailUser2, UserLevel.Developer);
        basicUser = createUser(emailUser3, UserLevel.Beginner);

        createGroup(nameGroup1, GroupType.APPLICATION, false);
        createGroup("group2", GroupType.APPLICATION, false);
        group = groupBusiness.get(nameGroup1);
        group2 = groupBusiness.get("group2");
    }

    @Test
    public void add() throws Exception {
        Application app = new Application("super_app", "les applications sont vraiment belles");
        app.setGroups(Set.of(group));

        // not the rights
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(basicUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                    .andExpect(status().is4xxClientError());

        // developer not in the private group
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                    .andExpect(status().is4xxClientError());

        configurationBusiness.addUserToGroup(emailUser2, nameGroup1);
        developperUser = configurationBusiness.getUserWithGroups(emailUser2);

        // developer in the private group
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(status().isOk());

        // // or admin
        // here it perform an update since application already exist
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(status().isOk());
    }

    @Test
    public void remove() throws Exception {
        Application app = new Application("super_app", "les applications sont vraiment belles");
        app.setGroups(Set.of(group));

        // create app first
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(status().isOk());

        // not the rights
        mockMvc.perform(delete("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(basicUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app.getName())))
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()))
                    .andExpect(status().is4xxClientError());

        configurationBusiness.addUserToGroup(emailUser2, nameGroup1);
        developperUser = configurationBusiness.getUserWithGroups(emailUser2);

        // developer/admin in the private group can do that
        mockMvc.perform(delete("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app.getName())))
                    .andExpect(status().isOk());
    }

    @Test
    public void update() throws Exception {
        // set group public
        group.setPublicGroup(true);
        groupBusiness.update(nameGroup1, group);

        Application app = new Application("super_app", "les applications sont vraiment belles");
        app.setGroups(Set.of(group));

        configurationBusiness.addUserToGroup(emailUser2, nameGroup1);
        developperUser = configurationBusiness.getUserWithGroups(emailUser2);

        // create app
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(app)))
                    .andExpect(status().isOk());

        app.setCitation("les applications sont vraiment moches");

        // developer try to edit application in public group
        mockMvc.perform(put("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // update app wrong matching ids
        mockMvc.perform(put("/internal/applications/not_good_name")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.BAD_INPUT_FIELD.getCode()));

        // do update
        mockMvc.perform(put("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                    .andExpect(status().isOk());
    }

    @Test
    public void getApp() throws Exception {
        Application app = new Application("app1", "wow super app1");

        // create apps
        mockMvc.perform(post("/internal/applications")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(app)))
                .andExpect(status().isOk());

        // retrieve wrong
        mockMvc.perform(get("/internal/applications/wrong")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()))
                    .andExpect(status().is4xxClientError());

        // good one
        mockMvc.perform(get("/internal/applications/" + app.getName())
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(app.getName()))
                .andExpect(jsonPath("$.citation").value(app.getCitation()));
    }

    @Test
    public void getList() throws Exception {
        Application app1 = new Application("app1", "wow super app1");
        Application app2 = new Application("app2", "wow super app2");
        Application app3 = new Application("app3", "wow super app3");

        app3.setGroups(Set.of(group, group2));
        configurationBusiness.addUserToGroup(emailUser2, nameGroup1);
        developperUser = configurationBusiness.getUserWithGroups(emailUser2);


        // create apps
        mockMvc.perform(post("/internal/applications")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(app1)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/applications")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(app2)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/applications")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(app3)))
                .andExpect(status().isOk());

        // retrieve all as an admin
        mockMvc.perform(get("/internal/applications")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.data").isArray());

        // quantity
        mockMvc.perform(get("/internal/applications?quantity=1")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.data.length()").value(1));

        // offset
        mockMvc.perform(get("/internal/applications?offset=2")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.data.length()").value(1));

        // offset + limit
        mockMvc.perform(get("/internal/applications?offset=1&quantity=1")
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.data.length()").value(1));

        // limited user + limited view groups
        mockMvc.perform(get("/internal/applications")
                .with(getUserSecurityMock(developperUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                // groups are filtered and only visible user groups are returned into the object
                // if they belong to it
                .andExpect(jsonPath("$.data[0].groups.length()").value(1))
                .andExpect(jsonPath("$.data.length()").value(1));
        
        // filtering
        mockMvc.perform(get("/internal/applications?group=" + group.getName())
                .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.data.length()").value(1));
    }
}
