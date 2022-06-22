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
        public final int regex_group;
        public final String error_key;

        UrlKeys(String label, String regex, int regex_group, String error_key) {
            this.key = label;
            this.regex = regex;
            this.regex_group = regex_group;
            this.error_key = error_key;
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
        String keycloak_client_id = externalPlatform.getKeycloakClientId();
        String refresh_token_url = externalPlatform.getRefreshTokenUrl();

        String token = subString(UrlKeys.TOKEN, parameterValue);
        String refreshToken = subString(UrlKeys.REFRESH_TOKEN, parameterValue);
        String fileName = subString(UrlKeys.FILE_NAME, parameterValue);

        if(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(parameterName)){
            String type = subString(UrlKeys.TYPE, parameterValue);
            String md5 = subString(UrlKeys.MD5, parameterValue);
            String upload_url = externalPlatform.getUploadUrl();

            return buildUploadUri(fileName, upload_url, token, refreshToken, type, md5, keycloak_client_id, refresh_token_url);
        }
        
        String format = subString(UrlKeys.FORMAT, parameterValue);
        String datasetId = subString(UrlKeys.DATASET_ID, parameterValue);

        return buildDownloadUri(datasetId, apiUrl, token, refreshToken, format, fileName, keycloak_client_id, refresh_token_url);
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

    private String buildDownloadUri(String datasetId, String apiUrl, String token, String refreshToken, String format, String fileName, String keycloak_client_id, String refresh_token_url){
        return UrlKeys.FILE_NAME.key+":/" +
                fileName +
                "?"+ UrlKeys.API_URI.key+"=" +
                apiUrl +
                "&amp;"+ UrlKeys.DATASET_ID.key+"=" +
                datasetId +
                "&amp;"+ UrlKeys.FORMAT.key+"=" +
                format +
                "&amp;"+ UrlKeys.KEYCLOAK_CLIENT_ID.key+"=" +
                keycloak_client_id +
                "&amp;"+ UrlKeys.REFRESH_TOKEN_URL.key+"=" +
                refresh_token_url +
                "&amp;"+ UrlKeys.TOKEN.key+"=" +
                token +
                "&amp;"+ UrlKeys.REFRESH_TOKEN.key+"=" +
                refreshToken;
    }

    private String buildUploadUri(String filePath, String uploadUrl, String token, String refreshToken, String type, String md5 , String keycloak_client_id, String refresh_token_url){
        return UrlKeys.FILE_NAME.key+":/" +
                filePath +
                "?"+ UrlKeys.UPLOAD_URL.key+"=" +
                uploadUrl +
                "&amp;"+ UrlKeys.TYPE.key+"=" +
                type +
                "&amp;"+ UrlKeys.MD5.key+"=" +
                md5 +
                "&amp;"+ UrlKeys.KEYCLOAK_CLIENT_ID.key+"=" +
                keycloak_client_id +
                "&amp;"+ UrlKeys.REFRESH_TOKEN_URL.key+"=" +
                refresh_token_url +
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
            if(matcher.group(urlKey.regex_group) == null){
                logger.error("Cannot get {} from the uri, the {} is null",urlKey.error_key ,urlKey.error_key);
                throw new BusinessException("Cannot get "+urlKey.error_key+" from the uri, the "+urlKey.error_key+" is null");
            }
            return matcher.group(urlKey.regex_group);
        }else{
            logger.error("Cannot get {} from the uri",urlKey.error_key);
            throw new BusinessException("Cannot get "+urlKey.error_key+" from the uri");
        }
    }

}
