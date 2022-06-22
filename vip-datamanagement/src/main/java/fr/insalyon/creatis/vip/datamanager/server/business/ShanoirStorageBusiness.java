package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Created by Alae Es-saki on 12/04/2022
 */
@Service
public class ShanoirStorageBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    enum UrlKeys{
        FILE_NAME("shanoir", "^shanoir:/(//)?([^/].*)\\?.*$", 2 ,"fileName"),
        DATASET_ID("datasetId", "^.*[?&]datasetId=([^&]*)(&.*)?$", 1 ,"datasetId"),
        FORMAT("format", "^.*[?&]format=([^&]*)(&.*)?$", 1 ,"format"),
        TOKEN("token", "^.*[?&]token=([^&]*)(&.*)?$", 1 , "token"),
        REFRESH_TOKEN("refreshToken", "^.*[?&]refreshToken=([^&]*)(&.*)?$", 1 , "refresh token"),
        MD5("md5","^.*[?&]md5=([^&]*)(&.*)?$", 1, "md5"),
        TYPE("type", "^.*[?&]type=([^&]*)(&.*)?$", 1, "type"),
        API_URI("apiUrl", "", 0, "download Url"),
        UPLOAD_URL("upload_url","",0,"Import endpoint url"),
        KEYCLOAK_CLIENT_ID("keycloak_client_id", "", 0, ""),
        REFRESH_TOKEN_URL("refresh_token_url","", 0, "");

        public final String key;
        public final String regex;
        public final int regexGroup;
        public final String errorKey;

        UrlKeys(String label, String regex, int regexGroup, String errorKey) {
            this.key = label;
            this.regex = regex;
            this.regexGroup = regexGroup;
            this.errorKey = errorKey;
        }
    }

    /* GASW regexps in 3.2.0 version

       local token=`echo $URI | sed -r 's/^.*[?&]token=([^&]*)(&.*)?$/\1/i'`
       local fileName=`echo $URI | sed -r 's#^shanoir:/(//)?([^/].*)\?.*$#\2#i'`
       local apiUrl=`echo $URI | sed -r 's/^.*[?&]apiurl=([^&]*)(&.*)?$/\1/i'`
       local refreshToken=`echo $URI | sed -r 's/^.*[?&]refreshToken=([^&]*)(&.*)?$/\1/i'`
       local format=`echo $URI | sed -r 's/^.*[?&]format=([^&]*)(&.*)?$/\1/i'`
       local datasetId=`echo $URI | sed -r 's/^.*[?&]datasetId=([^&]*)(&.*)?$/\1/i'`

       So objective : generate "shanoir:/fileName?apiurl=[...]&datasetId=[...]&format=[...]&token=[...]&refreshToken=[....]
    */
    public String generateUri(
            ExternalPlatform externalPlatform, String parameterName, String parameterValue) throws BusinessException {

        verifyExternalPlatform(externalPlatform);

        String apiUrl = externalPlatform.getUrl();
        String keycloakClientId = externalPlatform.getKeycloakClientId();
        String refreshTokenUrl = externalPlatform.getRefreshTokenUrl();

        String token = subString(UrlKeys.TOKEN, parameterValue);
        String refreshToken = subString(UrlKeys.REFRESH_TOKEN, parameterValue);
        String fileName = subString(UrlKeys.FILE_NAME, parameterValue);

        if(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(parameterName)){
            String type = subString(UrlKeys.TYPE, parameterValue);
            String md5 = subString(UrlKeys.MD5, parameterValue);
            String uploadUrl = externalPlatform.getUploadUrl();

            return buildUploadUri(fileName, uploadUrl, token, refreshToken, type, md5, keycloakClientId, refreshTokenUrl);
        }
        
        String format = subString(UrlKeys.FORMAT, parameterValue);
        String datasetId = subString(UrlKeys.DATASET_ID, parameterValue);

        return buildDownloadUri(datasetId, apiUrl, token, refreshToken, format, fileName, keycloakClientId, refreshTokenUrl);
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
    }

    private String buildDownloadUri(String datasetId, String apiUrl, String token, String refreshToken, String format, String fileName, String keycloakClientId, String refreshTokenUrl){
        return UrlKeys.FILE_NAME.key+":/" +
                fileName +
                "?"+ UrlKeys.API_URI.key+"=" +
                apiUrl +
                "&amp;"+ UrlKeys.DATASET_ID.key+"=" +
                datasetId +
                "&amp;"+ UrlKeys.FORMAT.key+"=" +
                format +
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
        return UrlKeys.FILE_NAME.key+":/" +
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

    /**
     * SubString a text with a key and regex
     * @param urlKey
     * @param text
     * @return
     */
    private String subString(UrlKeys urlKey, String text) throws BusinessException {
        Pattern pattern = Pattern.compile(urlKey.regex);
        Matcher matcher = pattern.matcher(text);
        if(matcher.matches()) {
            if(matcher.group(urlKey.regexGroup) == null){
                logger.error("Cannot get {} from the uri, the {} is null",urlKey.errorKey,urlKey.errorKey);
                throw new BusinessException("Cannot get "+urlKey.errorKey +" from the uri, the "+urlKey.errorKey +" is null");
            }
            return matcher.group(urlKey.regexGroup);
        }else{
            logger.error("Cannot get {} from the uri",urlKey.errorKey);
            throw new BusinessException("Cannot get "+urlKey.errorKey +" from the uri");
        }
    }

}
