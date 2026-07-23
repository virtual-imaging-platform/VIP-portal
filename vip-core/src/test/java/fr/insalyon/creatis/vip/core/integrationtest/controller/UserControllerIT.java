package fr.insalyon.creatis.vip.core.integrationtest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.insalyon.creatis.vip.core.client.VipError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.models.UserAndPassword;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;

public class UserControllerIT extends BaseInternalApiSpringIT {

    private User adminUser;
    private User developperUser;
    private User basicUser;
    private User testUser;
    private Group privateGroup;
    private Group publicGroup;
    private UserAndPassword form;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        adminUser = createUser(emailUser1, UserLevel.Administrator);
        developperUser = createUser(emailUser2, UserLevel.Developer);
        basicUser = createUser(emailUser3, UserLevel.Beginner);

        asAdminContext(() -> {
            createGroup("private", GroupType.APPLICATION, false);
            createGroup("public", GroupType.APPLICATION, true);

            privateGroup = groupBusiness.get("private");
            publicGroup = groupBusiness.get("public");
        });

        refreshTestUser();
    }

    private void refreshTestUser() {
        testUser = new User("test", "test", "test@insa.fr", "test", null, CountryCode.fr);
        form = new UserAndPassword();
        form.comment = "test";
        form.user = testUser;
        form.password = "testPassword123";

    }

    private Map<Group, GROUP_ROLE> asMapGroup(Set<Group> groups) {
        Map<Group, GROUP_ROLE> result = new HashMap<>();

        groups.forEach((g) -> result.put(g, GROUP_ROLE.User));
        return result;
    }

    @Test
    public void add() throws Exception {
        // no crsf token needed (same for session creation)
        // non authentified sign-up with private groups (=forbidden)
        testUser.setGroups(asMapGroup(Set.of(privateGroup, publicGroup)));
        mockMvc.perform(post("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.BAD_INPUT_FIELD.getCode()));

        // non authentified sign-up with public groups (=ok)
        testUser.setGroups(asMapGroup(Set.of(publicGroup)));
        mockMvc.perform(post("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist()) // check a non-visible field
                .andExpect(jsonPath("$.confirmed").exists());
        Assertions.assertEquals(5, userBusiness.getUsers().size());
        User createdUser = userBusiness.getUserWithGroups(testUser.getEmail());
        Assertions.assertEquals(UserLevel.Beginner, createdUser.getLevel());
        Assertions.assertEquals(false, createdUser.isConfirmed());
        Assertions.assertEquals(1, createdUser.getMaxRunningSimulations());
        Assertions.assertEquals(1, createdUser.getGroups().size());
        Assertions.assertEquals("test_test", createdUser.getFolder());

        asAdminContext(() -> {
            userBusiness.remove(userBusiness.getUser(testUser.getEmail()).getId(), false);
        });

        // authentified users cannot create others "users" (=forbidden) (but admin can)
        mockMvc.perform(post("/internal/users")
                .with(getUserSecurityMock(basicUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.UNAUTHENTICATED_ONLY.getCode()));
        mockMvc.perform(post("/internal/users")
                .with(getUserSecurityMock(developperUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.UNAUTHENTICATED_ONLY.getCode()));
    }

    @Test
    public void addWithForbiddenFields() throws Exception {
        addWithForbiddenField(user -> user.setLevel(UserLevel.Administrator), DefaultError.BAD_INPUT_FIELD);
        addWithForbiddenField(user -> user.setMaxRunningSimulations(4), DefaultError.BAD_INPUT_FIELD);
        addWithForbiddenField(user -> user.setFolder("folder_that_must_be_refused"), DefaultError.BAD_INPUT);
        addWithForbiddenField(user -> user.setConfirmed(true), DefaultError.BAD_INPUT_FIELD);
        addWithForbiddenField(user -> user.setApiKey("apikey_that_must_be_refused"), DefaultError.BAD_INPUT);
    }

    public void addWithForbiddenField(Consumer<User> userModifier, VipError expectedError) throws Exception {
        refreshTestUser();
        userModifier.accept(testUser);
        mockMvc.perform(post("/internal/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(expectedError.getCode()));
        Assertions.assertEquals(4, userBusiness.getUsers().size());
    }

    @Test
    public void updateOkWithMinimalFields() throws Exception {
        // basic & developer can join public groups (=ok)
        User updatedBasicUser = userWithMinimalInfo(basicUser);
        updatedBasicUser.setInstitution("NewInstitution");
        basicUser.setInstitution(updatedBasicUser.getInstitution()); // also changed for validation
        updatedBasicUser.setGroups(asMapGroup(Set.of(publicGroup)));
        basicUser.setGroups(asMapGroup(updatedBasicUser.getGroups()));
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(basicUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().isOk());
        User bddUser = userBusiness.getUserWithGroups(basicUser.getEmail());
        Assertions.assertEquals(basicUser, bddUser);

        User updatedDev = userWithMinimalInfo(developperUser);
        updatedDev.setCountryCode(CountryCode.de);
        developperUser.setCountryCode(updatedDev.getCountryCode());
        updatedDev.setGroups(asMapGroup(Set.of(publicGroup)));
        developperUser.setGroups(asMapGroup(updatedDev.getGroups()));
        mockMvc.perform(put("/internal/users/" + developperUser.getId())
                        .with(getUserSecurityMock(developperUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedDev)))
                .andExpect(status().isOk());
        bddUser = userBusiness.getUserWithGroups(developperUser.getEmail());
        Assertions.assertEquals(developperUser, bddUser);

        // But they can NOT join private groups
        updatedBasicUser.setGroups(asMapGroup(Set.of(privateGroup)));
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(basicUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.BAD_INPUT_FIELD.getCode()));
    }

    @Test
    public void updateOkWithMaximumFields() throws Exception {
        // basic & developer can join public groups (=ok)
        User updatedBasicUser = userWithMaxInfo(basicUser);
        updatedBasicUser.setInstitution("NewInstitution");
        basicUser.setInstitution(updatedBasicUser.getInstitution()); // also changed for validation
        updatedBasicUser.setGroups(asMapGroup(Set.of(publicGroup)));
        basicUser.setGroups(asMapGroup(updatedBasicUser.getGroups()));
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(basicUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().isOk());
        User bddUser = userBusiness.getUserWithGroups(basicUser.getEmail());
        Assertions.assertEquals(basicUser, bddUser);

        User updatedDev = userWithMaxInfo(developperUser);
        updatedDev.setCountryCode(CountryCode.de);
        developperUser.setCountryCode(updatedDev.getCountryCode());
        updatedDev.setGroups(asMapGroup(Set.of(publicGroup)));
        developperUser.setGroups(asMapGroup(updatedDev.getGroups()));
        mockMvc.perform(put("/internal/users/" + developperUser.getId())
                        .with(getUserSecurityMock(developperUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedDev)))
                .andExpect(status().isOk());
        bddUser = userBusiness.getUserWithGroups(developperUser.getEmail());
        Assertions.assertEquals(developperUser, bddUser);

        // But they can NOT join private groups
        updatedBasicUser.setGroups(asMapGroup(Set.of(privateGroup)));
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(basicUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.BAD_INPUT_FIELD.getCode()));
    }

    @Test
    public void updateOthers() throws Exception {
        // basic & developer cannot edit others
        User updatedBasicUser = userWithMaxInfo(basicUser);
        User updatedDev = userWithMaxInfo(developperUser);
        mockMvc.perform(put("/internal/users/" + developperUser.getId())
                        .with(getUserSecurityMock(basicUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedDev)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(developperUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // admin can edit others
        updatedDev.setCountryCode(CountryCode.de);
        developperUser.setCountryCode(updatedDev.getCountryCode());
        updatedDev.setGroups(asMapGroup(Set.of(publicGroup)));
        developperUser.setGroups(asMapGroup(updatedDev.getGroups()));
        mockMvc.perform(put("/internal/users/" + developperUser.getId())
                        .with(getUserSecurityMock(adminUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedDev)))
                .andExpect(status().isOk());
        User bddUser = userBusiness.getUserWithGroups(developperUser.getEmail());
        Assertions.assertEquals(developperUser, bddUser);
    }

    @Test
    public void updateForbiddenThings() throws Exception {
        // trying to update : apikey, password, level, email, folder, locked
        updateWithForbiddenField(basicUser, user -> user.setApiKey("forbidden_apikey"), DefaultError.BAD_INPUT);
        updateWithForbiddenField(basicUser, user -> user.setPassword("forbidden_password"), DefaultError.BAD_INPUT);
        updateWithForbiddenField(basicUser, user -> user.setLevel(UserLevel.Administrator), DefaultError.BAD_INPUT_FIELD);
        updateWithForbiddenField(basicUser, user -> user.setEmail("forbidden_email"), DefaultError.BAD_INPUT_FIELD);
        updateWithForbiddenField(basicUser, user -> user.setFolder("forbidden_folder"), DefaultError.BAD_INPUT);
        updateWithForbiddenField(basicUser, user -> user.setAccountLocked(true), DefaultError.BAD_INPUT);

        // verify ok normally
        updateShouldBeOk(basicUser, user -> user.setInstitution("New Institution"));

        // But admin can update some
        updateWithForbiddenField(adminUser, user -> user.setApiKey("forbidden_apikey"), DefaultError.BAD_INPUT);
        updateWithForbiddenField(adminUser, user -> user.setPassword("forbidden_password"), DefaultError.BAD_INPUT);
        updateShouldBeOk(adminUser, user -> {
            user.setLevel(UserLevel.Administrator);
            user.setEmail("new_email");
            user.setFolder("new_folder");
            user.setAccountLocked(true);
        });

    }

    public void updateWithForbiddenField(User clientUser, Consumer<User> userModifier, VipError expectedError) throws Exception {
        User updatedBasicUser = userWithMaxInfo(basicUser);
        userModifier.accept(updatedBasicUser);
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(clientUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(expectedError.getCode()));
    }

    public void updateShouldBeOk(User clientUser, Consumer<User> userModifier) throws Exception {
        User updatedBasicUser = userWithMaxInfo(basicUser);
        userModifier.accept(updatedBasicUser);
        userModifier.accept(basicUser);
        mockMvc.perform(put("/internal/users/" + basicUser.getId())
                        .with(getUserSecurityMock(clientUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBasicUser)))
                .andExpect(status().isOk());
        User bddUser = userBusiness.getUserWithGroups(basicUser.getEmail());
        Assertions.assertEquals(basicUser, bddUser);
    }

    public User userWithMinimalInfo(User user) {
        return new User(user.getId(), user.getFirstName(), user.getLastName());
    }

    public User userWithMaxInfo(User user) {
        return new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                null,
                user.getInstitution(),
                user.isConfirmed(),
                null,
                null,
                null,
                null,
                null,
                user.getLevel(),
                user.getCountryCode(),
                user.getMaxRunningSimulations(),
                null,
                null,
                null,
                null,
                null);
    }

    @Test
    public void remove() throws Exception {
        // user cannot delete someoneelse (=forbidden)
        mockMvc.perform(delete("/internal/users/" + developperUser.getId())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // user delete if self (=ok)
        mockMvc.perform(delete("/internal/users/" + basicUser.getId())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        // admin delete another user (=ok)
        mockMvc.perform(delete("/internal/users/" + developperUser.getId())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void getUser() throws Exception {
        // user & developer cannot see others (=forbidden)
        mockMvc.perform(get("/internal/users/" + developperUser.getId())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));
        mockMvc.perform(get("/internal/users/" + basicUser.getId())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // user & developer see themselves (but filtered by JsonView) (=ok)
        mockMvc.perform(get("/internal/users/" + basicUser.getId())
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basicUser.getId()))
                .andExpect(jsonPath("$.confirmed").exists())
                .andExpect(jsonPath("$.folder").doesNotExist())
                .andExpect(jsonPath("$.accountLocked").doesNotExist());
        mockMvc.perform(get("/internal/users/" + developperUser.getId())
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(developperUser.getId()))
                .andExpect(jsonPath("$.confirmed").exists())
                .andExpect(jsonPath("$.folder").doesNotExist())
                .andExpect(jsonPath("$.accountLocked").doesNotExist());

        // admin can see others accounts with more fields (but not everything) (=ok)
        mockMvc.perform(get("/internal/users/" + developperUser.getId())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(developperUser.getId()))
                .andExpect(jsonPath("$.confirmed").exists())
                .andExpect(jsonPath("$.accountLocked").exists())
                .andExpect(jsonPath("$.folder").exists())
                .andExpect(jsonPath("$.nextEmail").doesNotExist())
                .andExpect(jsonPath("$.failedAuthentications").doesNotExist());
        // himself
        mockMvc.perform(get("/internal/users/" + adminUser.getId())
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminUser.getId()))
                .andExpect(jsonPath("$.confirmed").exists())
                .andExpect(jsonPath("$.accountLocked").exists())
                .andExpect(jsonPath("$.folder").exists())
                .andExpect(jsonPath("$.nextEmail").doesNotExist())
                .andExpect(jsonPath("$.failedAuthentications").doesNotExist());

        // admin try to see non existing account
        mockMvc.perform(get("/internal/users/dotexist")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()));

    }

    @Test
    public void getAll() throws Exception {
        // user & developer cannot use the endpoint (=forbidden)
        mockMvc.perform(get("/internal/users")
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));
        mockMvc.perform(get("/internal/users")
                .with(getUserSecurityMock(developperUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()));

        // admin (=ok)
        mockMvc.perform(get("/internal/users")
                .with(getUserSecurityMock(adminUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(4))
                .andExpect(jsonPath("$.data[0].accountLocked").exists())
                .andExpect(jsonPath("$.data[0].folder").exists())
                // here we check that the JSONView still applies even if wrapper inside PrecisePage<T>
                .andExpect(jsonPath("$.data[0].nextCode").doesNotExist())
                .andExpect(jsonPath("$.data[0].password").doesNotExist());
    }
}