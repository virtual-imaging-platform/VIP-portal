package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShanoirStorageBusinessTest {

    @Test
    public void testShanoirUri() throws BusinessException {
        ShanoirStorageBusiness shanoirStorageBusiness = new ShanoirStorageBusiness();
        ExternalPlatform externalPlatform = new ExternalPlatform();
        externalPlatform.setIdentifier("testShanoir");
        externalPlatform.setType(ExternalPlatform.Type.SHANOIR);
        externalPlatform.setUrl("testShanoirUrl");
        externalPlatform.setRefreshTokenUrl("testRefreshTokenUrl");
        externalPlatform.setUploadUrl("testUploadUrl");

        String value = "testShanoir:/path/to/file.txt?" +
                "resourceId=testResourceId" +
                "&format=testFormat" +
                "&token=testToken" +
                "&refreshToken=testRefreshToken" +
                "&clientId=testKeycloakClientId" +
                "&converterId=testConverterId";

        String uri = shanoirStorageBusiness.generateUri(externalPlatform, "paramName", value);

        String expectedUri="shanoir:/path/to/file.txt?" +
                "apiUrl=testShanoirUrl" +
                "&amp;resourceId=testResourceId" +
                "&amp;format=testFormat" +
                "&amp;converterId=testConverterId" +
                "&amp;keycloak_client_id=testKeycloakClientId" +
                "&amp;refresh_token_url=testRefreshTokenUrl" +
                "&amp;token=testToken" +
                "&amp;refreshToken=testRefreshToken";

        Assertions.assertEquals(expectedUri, uri);
    }

    @Test
    public void testShanoirUploadUri() throws BusinessException {
        ShanoirStorageBusiness shanoirStorageBusiness = new ShanoirStorageBusiness();
        ExternalPlatform externalPlatform = new ExternalPlatform();
        externalPlatform.setIdentifier("testShanoir");
        externalPlatform.setType(ExternalPlatform.Type.SHANOIR);
        externalPlatform.setUrl("testShanoirUrl");
        externalPlatform.setRefreshTokenUrl("testRefreshTokenUrl");
        externalPlatform.setUploadUrl("testUploadUrl");

        String value = "testShanoir:/path/to/dir?" +
                "type=File" +
                "&md5=d41d8cd98f00b204e9800998ecf8427e" +
                "&token=testToken" +
                "&refreshToken=testRefreshToken" +
                "&clientId=testKeycloakClientId";

        String uploadUri = shanoirStorageBusiness.generateUri(externalPlatform, CoreConstants.RESULTS_DIRECTORY_PARAM_NAME, value);

        String expectedUri="shanoir:/path/to/dir?" +
                "upload_url=testUploadUrl" +
                "&amp;type=File" +
                "&amp;md5=d41d8cd98f00b204e9800998ecf8427e" +
                "&amp;keycloak_client_id=testKeycloakClientId" +
                "&amp;refresh_token_url=testRefreshTokenUrl" +
                "&amp;token=testToken" +
                "&amp;refreshToken=testRefreshToken";

        Assertions.assertEquals(expectedUri, uploadUri);
    }
}
