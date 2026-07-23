package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoutiquesControllerIT extends BaseInternalApiSpringIT  {

    private User developperUser;
    private User basicUser;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        developperUser = createUser(emailUser2, UserLevel.Developer);
        basicUser = createUser(emailUser3, UserLevel.Beginner);
    }

    @Test
    public void testPermissions() throws Exception {
        // we mock ProcessBuilder to avoid system calls.
        // So it will fail but that's fine, we just want to check the permissions
        try (MockedConstruction<ProcessBuilder> ignored = Mockito.mockConstruction(ProcessBuilder.class))
        {
            // Basic user can NOT check boutiques
            mockMvc.perform(post("/internal/boutiques/check")
                            .contentType("application/json")
                            .content(getResourceAsString("FreeSurfer-Recon-all_v731.json"))
                            .with(getUserSecurityMock(basicUser))
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.ACCESS_DENIED.getCode()))
                    .andExpect(status().is4xxClientError());

            // But developers can
            mockMvc.perform(post("/internal/boutiques/check")
                            .contentType("application/json")
                            .content(getResourceAsString("FreeSurfer-Recon-all_v731.json"))
                            .with(getUserSecurityMock(developperUser))
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.GENERIC_ERROR_WITH_MESSAGE.getCode()))
                    .andExpect(status().is5xxServerError());

            // And admins too
            mockMvc.perform(post("/internal/boutiques/check")
                            .contentType("application/json")
                            .content(getResourceAsString("FreeSurfer-Recon-all_v731.json"))
                            .with(getUserSecurityMock(getAdminUser()))
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.errorCode").value(DefaultError.GENERIC_ERROR_WITH_MESSAGE.getCode()))
                    .andExpect(status().is5xxServerError());
        }
    }



}
