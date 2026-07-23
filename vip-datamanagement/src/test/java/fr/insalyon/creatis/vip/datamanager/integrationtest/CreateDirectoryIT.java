package fr.insalyon.creatis.vip.datamanager.integrationtest;

import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateDirectoryIT extends BaseInternalApiSpringIT {

    /**
     * when creating a directory.
     * Needs :
     * - a pathinfo on the path (verify not exist)
     * - a pathinfo on the parent (verify exist and a folder) if necessary
     */

    private User basicUser;
    private Group groupTest1;
    private Group groupTest2;

    @Autowired StorageTestConfigurer utils;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        asAdminContext(() -> {
            groupTest1 = createGroup("groupTest1");
            groupTest2 = createGroup("groupTest2");
            basicUser = createUserInGroup(emailUser1, groupTest1.getName());
        });
    }

    public void expectOkOnUserPath(User user, String newDirPath) throws Exception {
        String fullPath = Path.of("/vip/Home", newDirPath).toString();
        URI uri = URI.create("/internal/storage/directories"
                + UriUtils.encodePath(fullPath, StandardCharsets.UTF_8));

        var result = mockMvc.perform(post(uri)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andReturn();

        int status = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        if (status != 201) {
            throw new AssertionError("Expected status 201 but got " + status + ". Response: " + responseBody);
        }

        utils.verifyCreateFolderForUser(user, newDirPath, 1);
    }

    public void expectOkOnGroupPath(User user, Group group, String newDirPath) throws Exception {
        String fullPath = Path.of("/vip", group.getName() + DataManagerConstants.GROUP_APPEND, newDirPath).toString();
        URI uri = URI.create("/internal/storage/directories"
                + UriUtils.encodePath(fullPath, StandardCharsets.UTF_8));

        mockMvc.perform(post(uri)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        utils.verifyCreateFolderForGroup(group, newDirPath, 1);
    }

    public void expectBadRequestOnPath(User user, String newDirPath, Integer expectedErrorCode) throws Exception {
        URI uri = URI.create("/internal/storage/directories"
                + UriUtils.encodePath(newDirPath, StandardCharsets.UTF_8));

        mockMvc.perform(post(uri)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));

        utils.verifyNoCreateFolder();
    }

    public void expectForbiddenOnPath(User user, String newDirPath, Integer expectedErrorCode) throws Exception {
        URI uri = URI.create("/internal/storage/directories"
                + UriUtils.encodePath(newDirPath, StandardCharsets.UTF_8));

        mockMvc.perform(post(uri)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));

        utils.verifyNoCreateFolder();
    }

    // Creating in Home
    @Test
    public void testDirectoryCreationInHome() throws Exception {
        // Configure that Home folder exists and is a folder
        utils.configureFolderForUser(basicUser, "");
        utils.configureNonExistingElementForUser(basicUser, "newDir");

        expectOkOnUserPath(basicUser, "newDir");
    }

    // Creating in Home subdir
    @Test
    public void testDirectoryCreationInHomeSubdir() throws Exception {
        utils.configureFolderForUser(basicUser, "path/to", (String) null);
        utils.configureNonExistingElementForUser(basicUser, "path/to/newDir");

        expectOkOnUserPath(basicUser, "path/to/newDir");
    }

    // Forbidden for basic user in group
    @Test
    public void testDirectoryCreationKoInGroupForBeginner() throws Exception {
        expectForbiddenOnPath(basicUser, "/vip/groupTest1 (group)/newDir", 4001);
    }

    // allowed for advanced user in group
    @Test
    public void testDirectoryCreationOkInGroupForAdvandced() throws Exception {
        asAdminContext(() -> {
            basicUser = new User(basicUser);
            basicUser.setLevel(UserLevel.Advanced);
            basicUser = userBusiness.update(basicUser);
        });
        utils.configureFolderInGroup(groupTest1, "", (String) null);
        utils.configureNonExistingElementForGroup(groupTest1, "newDir");

        expectOkOnGroupPath(basicUser, groupTest1, "newDir");

        // but only in its group
        Mockito.reset(gridaClient);

        expectForbiddenOnPath(basicUser, "/vip/groupTest2 (group)/newDir", 4001);
    }

     /*
        Error cases :
        - dir/file already exist
        - parent dir does not exist
        - parent dir is a file
        - creating /vip or /vip/something
        - dir not starting with /vip
        - creating with forbidden ..
     */

    @Test
    public void testSomeErrorCases() throws Exception {

        // dir already exist
        utils.configureFolderForUser(basicUser, "", (String) null);
        utils.configureFolderForUser(basicUser, "newDir", (String) null);
        expectBadRequestOnPath(basicUser, "/vip/Home/newDir", 4005);

        // dir already exist as a file
        Mockito.reset(gridaClient);

        utils.configureFolderForUser(basicUser, "", (String) null);
        utils.configureFileForUser(basicUser, "newDir");

        expectBadRequestOnPath(basicUser, "/vip/Home/newDir", 4005);

        // Parent dir does not exist
        Mockito.reset(gridaClient);

        utils.configureNonExistingElementForUser(basicUser, "path/to");
        utils.configureNonExistingElementForUser(basicUser, "path/to/newDir");

        expectBadRequestOnPath(basicUser, "/vip/Home/path/to/newDir", 4005);

        // Parent dir exist as a file
        Mockito.reset(gridaClient);

        utils.configureFileForUser(basicUser, "path/to");
        utils.configureNonExistingElementForUser(basicUser, "path/to/newDir");

        expectBadRequestOnPath(basicUser, "/vip/Home/path/to/newDir", 4005);
    }


    @Test
    public void testOtherErrorCases() throws Exception {
        // creating /vip
        expectForbiddenOnPath(basicUser, "/vip", 4001);

        // creating /vip/Home
        Mockito.reset(gridaClient);
        expectForbiddenOnPath(basicUser, "/vip/Home", 4001);

        // creating /vip/group (group)
        Mockito.reset(gridaClient);
        expectForbiddenOnPath(basicUser, "/vip/groupTest1 (group)", 4001);
        Mockito.reset(gridaClient);
        expectForbiddenOnPath(basicUser, "/vip/unknownGroup (group)", 4001);

        // creating /vip/Anything
        Mockito.reset(gridaClient);
        expectBadRequestOnPath(basicUser, "/vip/Anything", 4000);

        // creating /NOTVIP
        Mockito.reset(gridaClient);
        expectBadRequestOnPath(basicUser, "/NOTVIP/Home", 4000);

        // creating /vip/../../something
        Mockito.reset(gridaClient);
        expectBadRequestOnPath(basicUser, "/vip/Home/../../../secret_stuff.txt", 9000);
    }

}
