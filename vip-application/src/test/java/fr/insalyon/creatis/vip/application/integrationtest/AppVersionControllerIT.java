package fr.insalyon.creatis.vip.application.integrationtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;


public class AppVersionControllerIT extends BaseInternalApiSpringIT {

    @Autowired
    private ApplicationBusiness applicationBusiness;

    private User adminUser;
    private User developperUser;
    private User developperUser2;
    private User basicUser;

    private Group group;
    private Group group2;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        adminUser = createUser(emailUser1, UserLevel.Administrator);
        developperUser = createUser(emailUser2, UserLevel.Developer);
        developperUser2 = createUser(emailUser3, UserLevel.Developer);
        basicUser = createUser(emailUser4, UserLevel.Beginner);

        asAdminContext(() -> {
            createGroup(nameGroup1, GroupType.APPLICATION, true);
            createGroup("group2", GroupType.APPLICATION, false);
            groupBusiness.add(new Group("groupauto", true, GroupType.APPLICATION, true));

            group = groupBusiness.get(nameGroup1);
            group2 = groupBusiness.get("group2");
        });

        userBusiness.addUserToGroup(basicUser.getEmail(), group.getName());
        userBusiness.addUserToGroup(developperUser.getEmail(), group.getName());
        userBusiness.addUserToGroup(developperUser2.getEmail(), group2.getName());

        adminUser = userBusiness.getUserWithGroups(adminUser.getEmail());
        developperUser = userBusiness.getUserWithGroups(developperUser.getEmail());
        developperUser2 = userBusiness.getUserWithGroups(developperUser2.getEmail());
        basicUser = userBusiness.getUserWithGroups(basicUser.getEmail());
    }

    private Application createApplication(String name, Set<Group> groups) throws Exception {
        final Application app = new Application(name, "");
        app.setGroups(groups);

        asAdminContext(() -> {
            applicationBusiness.add(app);
        });
        return app;
    }

    @Test
    public void add() throws Exception {
        Application app = createApplication("tst", Set.of(group, group2));
        AppVersion version = new AppVersion(app.getName(), "v1", "", true);

        // basic user (=forbidden)
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // developer with application in public and private group (=forbidden)
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // developer with application in private group (=ok)
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(developperUser2))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());

        // admin (=ok), actually an update since version already exist
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());
    }

    @Test
    public void update() throws Exception {
        Application app = createApplication("tst", Set.of(group, group2));
        AppVersion version = new AppVersion(app.getName(), "v1", "", true);

        // create before
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());

        // basic user (=forbidden)
        mockMvc.perform(put("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // developer try to edit resources (=forbidden)
        AppVersion copy = new AppVersion(app.getName(), "v1", "", true);
        copy.setResources(Set.of(new Resource("super_resource")));

        mockMvc.perform(put("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(developperUser2))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(copy)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // developer with application in private group (=ok)
        mockMvc.perform(put("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(developperUser2))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());
    }

    @Test
    public void remove() throws Exception {
        Application app = createApplication("tst", Set.of(group, group2));
        AppVersion version = new AppVersion(app.getName(), "v1", "", true);

        // create before
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());

        // basic user (=forbidden)
        mockMvc.perform(delete("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // developer with application in public and private group (=forbidden)
        mockMvc.perform(delete("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // developer with application in private group (=ok)
        mockMvc.perform(delete("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(developperUser2))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());

        // admin, should fail since application deleted!
        mockMvc.perform(delete("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()));
    }

    @Test
    public void getVersion() throws Exception {
        Application app = createApplication("tst", Set.of(group, group2));
        AppVersion version = new AppVersion(app.getName(), "v1", "", true);

        // create before
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version)))
                .andExpect(status().isOk());

        // not existing version
        mockMvc.perform(get("/internal/applications/" + app.getName() + "/versions/dwqdwq")
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()));

        // user in the group (=ok)
        mockMvc.perform(get("/internal/applications/" + app.getName() + "/versions/" + version.getVersion())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // since the behavior is almost the same and based on /applications
    // this one is lightweighted here
    @Test
    public void getList() throws Exception {
        Application app = createApplication("tst", Set.of(group, group2));
        Application app2 = createApplication("tst2", Set.of(group));
        AppVersion version1 = new AppVersion(app.getName(), "v1", "", true);
        AppVersion version2 = new AppVersion(app.getName(), "v2", "", true);

        // create versions
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version1)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(version2)))
                .andExpect(status().isOk());

        // user in the group (=ok)
        mockMvc.perform(get("/internal/applications/" + app.getName() + "/versions")
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));

        // user in the group (=ok), but with no versions to retrieve
        mockMvc.perform(get("/internal/applications/" + app2.getName() + "/versions")
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }
}
