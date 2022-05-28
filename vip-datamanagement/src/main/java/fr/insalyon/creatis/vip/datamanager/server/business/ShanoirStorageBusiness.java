package fr.insalyon.creatis.vip.datamanager.server.business;

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
        OUT_NAME("outName","^.*[?&]outName=([^&]*)(&.*)?$", 1,"output name"),
        MD5("md5","^.*[?&]md5=([^&]*)(&.*)?$", 1, "md5"),
        TYPE("type", "^.*[?&]type=([^&]*)(&.*)?$", 1, "type"),
        API_URI("apiUrl", "", 0, "Api Url");

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
            ExternalPlatform externalPlatform, String parameterValue) throws BusinessException {

        verifyExternalPlatform(externalPlatform);

        String datasetId = subString(UrlKeys.DATASET_ID, parameterValue);
        String fileName = subString(UrlKeys.FILE_NAME, parameterValue);
        String format = subString(UrlKeys.FORMAT, parameterValue);
        String token = subString(UrlKeys.TOKEN, parameterValue);
        String refreshToken = subString(UrlKeys.REFRESH_TOKEN, parameterValue);
        String outName = subString(UrlKeys.OUT_NAME, parameterValue);
        String md5 = subString(UrlKeys.MD5, parameterValue);
        String type = subString(UrlKeys.TYPE, parameterValue);
        String apiUrl = externalPlatform.getUrl();

        return buildUri(datasetId, apiUrl, token, refreshToken, format, fileName, outName, md5, type);
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

    private String buildUri(String datasetId, String apiUrl, String token, String refreshToken, String format, String fileName, String outName, String md5, String type){
        return UrlKeys.FILE_NAME.key+":/" +
                fileName +
                "?"+ UrlKeys.API_URI.key+"=" +
                apiUrl +
                "&amp;"+ UrlKeys.DATASET_ID.key+"=" +
                datasetId +
                "&amp;"+ UrlKeys.FORMAT.key+"=" +
                format +
                "&amp;"+ UrlKeys.TOKEN.key+"=" +
                token +
                "&amp;"+ UrlKeys.OUT_NAME.key+"=" +
                outName +
                "&amp;"+ UrlKeys.MD5.key+"=" +
                md5 +
                "&amp;"+ UrlKeys.TYPE.key+"=" +
                type +
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
