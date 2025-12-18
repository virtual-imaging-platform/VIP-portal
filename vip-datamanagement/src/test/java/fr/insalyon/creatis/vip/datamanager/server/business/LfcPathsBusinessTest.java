package fr.insalyon.creatis.vip.datamanager.server.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;

public class LfcPathsBusinessTest {

    @Mock
    private Server server;

    @Mock
    private GroupDAO groupDAO;

    @Mock
    private User user;

    private LfcPathsBusiness lfcPathsBusiness;

    @BeforeEach
    public void init() throws DAOException {
        MockitoAnnotations.openMocks(this);

        when(server.getVoRoot()).thenReturn("/var/data");
        when(server.getDataManagerUsersHome()).thenReturn("/var/data/users");
        when(server.getDataManagerGroupsHome()).thenReturn("/var/data/groups");
        when(server.getAltDataManagerUsersHome()).thenReturn("");
        when(server.getAltDataManagerGroupsHome()).thenReturn("");

        when(groupDAO.get()).thenReturn(Collections.emptyList());
        when(user.getFolder()).thenReturn("test_user");

        lfcPathsBusiness = new LfcPathsBusiness(server, groupDAO);
    }

    @Test
    public void localFileTransformations() throws DataManagerException {
        String relativePath = "/vip/Home/my_file.txt";
        String absolutePath = "/var/data/users/test_user/my_file.txt";

        assertEquals(absolutePath, 
            lfcPathsBusiness.parseBaseDir(user, relativePath));

        assertEquals(relativePath, 
            lfcPathsBusiness.parseRealDir(absolutePath, user.getFolder()));
    }

    @Test
    public void testLfnTransformation() throws DataManagerException {
        assertEquals("/vip/Home/path/to/file.txt",
                lfcPathsBusiness.parseRealDir("lfn:/var/data/users/test_user/path/to/file.txt", user.getFolder()));
        assertEquals("/vip/Home/path/to/file.txt",
                lfcPathsBusiness.parseRealDir("lfn://var/data/users/test_user/path/to/file.txt", user.getFolder()));
        assertEquals("/vip/Home/path/to/file.txt",
                lfcPathsBusiness.parseRealDir("lfn:///var/data/users/test_user/path/to/file.txt", user.getFolder()));
    }
}
