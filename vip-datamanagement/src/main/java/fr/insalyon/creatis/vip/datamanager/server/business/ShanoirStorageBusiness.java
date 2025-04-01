package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.net.URI;
import java.util.List;

/**
 *  Created by Alae Es-saki on 12/04/2022
 */
@Service
public class ShanoirStorageBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // output URL scheme
    private final String outputScheme = "shanoir";
    enum UrlKeys{
        // input/output query parameters
        RESOURCE_ID("resourceId", "resourceId", "resourceId"),
        FORMAT("format", "format", "format"),
        TOKEN("token", "token", "token"),
        REFRESH_TOKEN("refreshToken", "refreshToken", "refresh token"),
        MD5("md5", "md5", "md5"),
        TYPE("type", "type", "type"),
        KEYCLOAK_CLIENT_ID("keycloak_client_id", "clientId", "clientId"),
        CONVERTER_ID("converterId", "converterId", "converterId", "8"),
        // output-only query parameters
        API_URI("apiUrl", "", "download Url"),
        UPLOAD_URL("upload_url", "", "Import endpoint url"),
        REFRESH_TOKEN_URL("refresh_token_url", "", "");

        public final String key;
        public final String input;
        public final String errorKey;
        public final String defaultValue;

        UrlKeys(String label, String input, String errorKey) {
            this(label, input, errorKey, null);
        }

        UrlKeys(String label, String input, String errorKey, String defaultValue) {
            this.key = label;
            this.input = input;
            this.errorKey = errorKey;
            this.defaultValue = defaultValue;
        }
    }

    /* Transform a shanoir input URI into a download or upload URI for a given ExternalPlatform:
     * - change URI scheme from ExternalPlatform name to "shanoir"
     * - keep filename/path as is
     * - copy and add some query string parameters
     * For all practical purposes, input URI format should verify:
     * - uri.getScheme().equals(externalPlatform.getIdentifier()): assumed checked in caller
     * - uri.getAuthority() == null, i.e. single or triple slash without a "host" part
     */
    public String generateUri(
            ExternalPlatform externalPlatform, String parameterName, String parameterValue) throws BusinessException {

        verifyExternalPlatform(externalPlatform);

        String apiUrl = externalPlatform.getUrl();
        String refreshTokenUrl = externalPlatform.getRefreshTokenUrl();

        // parse input URI.
        URI uri;
        try {
            uri = new URI(parameterValue);
        } catch (URISyntaxException e) {
            throw new BusinessException(e);
        }

        // get filename (including the leading slash)
        String fileName = uri.getPath();
        if (fileName == null) {
            logger.error("Cannot get fileName from the uri");
            throw new BusinessException("Cannot get fileName from the uri");
        }

        // parse query string
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(parameterValue).build().getQueryParams();
        String keycloakClientId = getKey(UrlKeys.KEYCLOAK_CLIENT_ID, queryParams);
        String token = getKey(UrlKeys.TOKEN, queryParams);
        String refreshToken = getKey(UrlKeys.REFRESH_TOKEN, queryParams);

        if(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(parameterName)) { // upload
            String type = getKey(UrlKeys.TYPE, queryParams);
            String md5 = getKey(UrlKeys.MD5, queryParams);
            String uploadUrl = externalPlatform.getUploadUrl();
            return buildUploadUri(fileName, uploadUrl, token, refreshToken, type, md5, keycloakClientId, refreshTokenUrl);
        } else { // download
            String format = getKey(UrlKeys.FORMAT, queryParams);
            String resourceId = getKey(UrlKeys.RESOURCE_ID, queryParams);
            String converterId = getKey(UrlKeys.CONVERTER_ID, queryParams);
            return buildDownloadUri(resourceId, apiUrl, token, refreshToken, format, converterId, fileName, keycloakClientId, refreshTokenUrl);
        }
    }

    private void verifyExternalPlatform(ExternalPlatform externalPlatform)
            throws RuntimeException, BusinessException {
        if ( ! externalPlatform.getType().equals(Type.SHANOIR)) {
            logger.error("Trying to generate a shanoir URI for a non shanoir storage {}",
                    externalPlatform.getType());
            throw new BusinessException("Cannot generate shanoir uri");
        }
        if (externalPlatform.getUrl() == null) {
            logger.error("A shanoir external storage must have an URL to generate an URI");
            throw new BusinessException("Cannot generate shanoir uri");
        }
        if (externalPlatform.getUploadUrl() == null || externalPlatform.getRefreshTokenUrl() == null) {
            logger.error("Cannot get keycloak informations for shanoir storage from database");
            throw new BusinessException("Cannot generate shanoir uri");
        }
    }

    private String buildDownloadUri(
            String resourceId, String apiUrl, String token, String refreshToken,
            String format, String converterId, String fileName, String keycloakClientId, String refreshTokenUrl){
        return outputScheme+":" +
                fileName +
                "?"+ UrlKeys.API_URI.key+"=" +
                apiUrl +
                "&amp;"+ UrlKeys.RESOURCE_ID.key+"=" +
                resourceId +
                "&amp;"+ UrlKeys.FORMAT.key+"=" +
                format +
                "&amp;"+ UrlKeys.CONVERTER_ID.key+"=" +
                converterId +
                "&amp;"+ UrlKeys.KEYCLOAK_CLIENT_ID.key+"=" +
                keycloakClientId +
                "&amp;"+ UrlKeys.REFRESH_TOKEN_URL.key+"=" +
                refreshTokenUrl +
                "&amp;"+ UrlKeys.TOKEN.key+"=" +
                token +
                "&amp;"+ UrlKeys.REFRESH_TOKEN.key+"=" +
                refreshToken;
    }

    private String buildUploadUri(String filePath, String uploadUrl, String token, String refreshToken, String type, String md5 , String keycloakClientId, String refreshTokenUrl){
        return outputScheme+":" +
                filePath +
                "?"+ UrlKeys.UPLOAD_URL.key+"=" +
                uploadUrl +
                "&amp;"+ UrlKeys.TYPE.key+"=" +
                type +
                "&amp;"+ UrlKeys.MD5.key+"=" +
                md5 +
                "&amp;"+ UrlKeys.KEYCLOAK_CLIENT_ID.key+"=" +
                keycloakClientId +
                "&amp;"+ UrlKeys.REFRESH_TOKEN_URL.key+"=" +
                refreshTokenUrl +
                "&amp;"+ UrlKeys.TOKEN.key+"=" +
                token +
                "&amp;"+ UrlKeys.REFRESH_TOKEN.key+"=" +
                refreshToken;
    }

    private String getKey(UrlKeys urlKey, MultiValueMap<String, String> queryParams) throws BusinessException {
        List<String> values = queryParams.get(urlKey.input);
        if (values != null && !values.isEmpty()) {
            return values.getFirst();
        } else if (urlKey.defaultValue != null) {
            return urlKey.defaultValue;
        } else {
            logger.error("Cannot get {} from the uri", urlKey.errorKey);
            throw new BusinessException("Cannot get " + urlKey.errorKey + " from the uri");
        }
    }
}
