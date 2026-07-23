package fr.insalyon.creatis.vip.datamanager.integrationtest;

import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.models.StorageCreateDirectoryRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteStuffIT extends BaseInternalApiSpringIT {

    /**
     * when deleting
     * Needs :
     * - a pathinfo on the path (verify exist)
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
            basicUser = createUserInGroup(emailUser4, groupTest1.getName());
        });
    }

    public void expectDeleteOnHomePath(User user, String path) throws Exception {
        mockMvc.perform(delete("/internal/storage/vip/Home/" + path)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        utils.verifyDeleteForUser(user, path, 1);
    }

    public void expectDeleteOnGroupPath(Group group, User user, String path) throws Exception {
        mockMvc.perform(delete("/internal/storage/vip/" + group.getName() + DataManagerConstants.GROUP_APPEND + "/" + path)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        utils.verifyDeleteForGroup(group, user, path, 1);
    }

    public void expectForbiddenOnPath(User user, String pathToDelete, Integer expectedErrorCode) throws Exception {
        mockMvc.perform(delete("/internal/storage/" + pathToDelete)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));

        utils.verifyNoDelete();
    }

    public void expectBadRequestOnPath(User user, String pathToDelete, Integer expectedErrorCode) throws Exception {
        mockMvc.perform(delete("/internal/storage/" + pathToDelete)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));

        utils.verifyNoDelete();
    }

    public void expectNotFoundRequestOnPath(User user, String pathToDelete, Integer expectedErrorCode) throws Exception {
        mockMvc.perform(delete("/internal/storage/" + pathToDelete)
                        .with(getUserSecurityMock(user))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));

        utils.verifyNoDelete();
    }

    // deleting in Home
    @Test
    public void testDeletingFileInHome() throws Exception {
        // on file
        utils.configureFileForUser(basicUser, "somefile.txt");

        expectDeleteOnHomePath(basicUser, "somefile.txt");

        // on folder
        resetGridaMocks();
        utils.configureFolderForUser(basicUser, "somefolder", "file.txt", "folder");

        expectDeleteOnHomePath(basicUser, "somefolder");

        // on file in subdir
        resetGridaMocks();
        utils.configureFileForUser(basicUser, "path/to/somefile.txt");

        expectDeleteOnHomePath(basicUser, "path/to/somefile.txt");

        // on folder in subdir
        resetGridaMocks();
        utils.configureFolderForUser(basicUser, "path/to/somefolder", "file.txt", "folder");

        expectDeleteOnHomePath(basicUser, "path/to/somefolder");
    }

    // Deleting Forbidden for basic user in group
    @Test
    public void testDeleteKoInGroupForBeginner() throws Exception {
        expectForbiddenOnPath(basicUser,"vip/groupTest1 (group)/someFolder", 4001);
    }

    // Deleting allowed for advanced user in group
    @Test
    public void testDeleteOkInGroupForAdvanced() throws Exception {
        asAdminContext(() -> {
            basicUser = new User(basicUser);
            basicUser.setLevel(UserLevel.Advanced);
            basicUser = userBusiness.update(basicUser);
        });

        utils.configureFolderInGroup(groupTest1, "someFolder", "file.txt");
        expectDeleteOnGroupPath(groupTest1, basicUser, "someFolder");

        // but only in its group
        resetGridaMocks();
        expectForbiddenOnPath(basicUser,"vip/groupTest2 (group)/someFolder", 4001);
    }

     /*
        Error cases :
        - deleting dir/file does not exist
        - deleting /vip or /vip/something
        - dir not starting with /vip
        - deleting path with forbidden ..
     */

    @Test
    public void testSomeErrorCases() throws Exception {

        // dir does not exist
        utils.configureNonExistingElementForUser(basicUser, "somefile");
        expectNotFoundRequestOnPath(basicUser, "/vip/Home/somefile", 4002);

        // deleting /vip
        resetGridaMocks();
        expectForbiddenOnPath(basicUser, "/vip", 4001);

        // deleting /vip/Home
        resetGridaMocks();
        expectForbiddenOnPath(basicUser, "/vip/Home", 4001);

        // deleting /vip/group (group)
        resetGridaMocks();
        expectForbiddenOnPath(basicUser, "/vip/groupTest1 (group)", 4001);
        resetGridaMocks();
        expectForbiddenOnPath(basicUser, "/vip/unknownGroup (group)", 4001);

        // deleting /vip/Anything
        resetGridaMocks();
        expectBadRequestOnPath(basicUser, "/vip/Anything", 4000);

        // deleting /NOTVIP
        resetGridaMocks();
        expectBadRequestOnPath(basicUser, "/NOTVIP/Home", 4000);

        // deleting /vip/../../something
        resetGridaMocks();
        expectBadRequestOnPath(basicUser, "/vip/Home/../../../secret_stuff.txt", 9000);
    }


}
