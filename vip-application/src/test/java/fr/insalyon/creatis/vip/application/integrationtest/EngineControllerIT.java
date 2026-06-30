package fr.insalyon.creatis.vip.application.integrationtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.User;

public class EngineControllerIT extends BaseInternalApiSpringIT {

    private User adminUser;
    private User basicUser;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        adminUser = createUser(emailUser1, UserLevel.Administrator);
        basicUser = createUser(emailUser3, UserLevel.Beginner);

    }

    //TODO use @Nested
    @Test
    public void create() throws Exception {
        Engine e1 = new Engine("e1", "http://localhost:5000", "enabled");

        //not the rights
        //TODO fails with admin dataview nullifying the object before reaching the controller (thus unable to validate permission on Business level)
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(basicUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e1)))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                .andExpect(status().is4xxClientError());
        // create
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(e1.getName()));
    }

    @Test
    public void getOne() throws Exception {
        Engine engine = new Engine("e1", "http://localhost:5000", "enabled");
        // save one
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(engine)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/internal/engines/" + engine.getName())
                        .with(getUserSecurityMock(adminUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("endpoint").value(engine.getEndpoint()));
    }

    @Test
    public void getList() throws Exception {
        Engine e1 = new Engine("e1", "http://localhost:5000", "enabled");
        Engine e2 = new Engine("e2", "http://localhost:4999", "disabled");
        // save
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e1)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("offset", "0")
                        .queryParam("quantity", "2"))
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(status().isOk());
    }

    @Test
    public void remove() throws Exception {
        Engine e1 = new Engine("e1", "http://localhost:5000", "enabled");
        Engine e2 = new Engine("e2", "http://localhost:4999", "disabled");
        // save
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e1)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e2)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/internal/engines/" + e1.getName())
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("offset", "0")
                        .queryParam("quantity", "2"))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.data[0].name").value("e2"))
                .andExpect(status().isOk());
    }

    @Test
    public void update() throws Exception {
        Engine e1 = new Engine("e1", "http://localhost:5000", "enabled");

        // save
        mockMvc.perform(post("/internal/engines")
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e1)))
                .andExpect(status().isOk());
        e1.setStatus("disabled");
        mockMvc.perform(put("/internal/engines/" + e1.getName())
                        .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(e1)))
                .andExpect(jsonPath("status").value("disabled"))
                .andExpect(status().isOk());
    }

}
