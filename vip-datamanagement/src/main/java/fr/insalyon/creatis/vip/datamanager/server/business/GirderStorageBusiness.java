package fr.insalyon.creatis.vip.datamanager.server.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform.Type;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;

@Service
@Transactional
public class GirderStorageBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApiKeyBusiness apiKeyBusiness;
    private Server server;

    @Autowired
    public GirderStorageBusiness(ApiKeyBusiness apiKeyBusiness, Server server) {
        this.apiKeyBusiness = apiKeyBusiness;
        this.server = server;
    }

    /* GASW regexps in 3.2.0 version
        local fileName=`echo $URI | sed -r 's#^girder:/(//)?([^/].*)\?.*$#\2#i'`
        local apiUrl=`echo $URI | sed -r 's/^.*[?&]apiurl=([^&]*)(&.*)?$/\1/i'`
        local fileId=`echo $URI | sed -r 's/^.*[?&]fileid=([^&]*)(&.*)?$/\1/i'`
        local token=`echo $URI | sed -r 's/^.*[?&]token=([^&]*)(&.*)?$/\1/i'`

        So objective : generate "girder:/filename?apiurl=[...]&fileId=[...]&token=[...]
     */
    public String generateUri(
            ExternalPlatform externalPlatform, String parameterName,
            String fileIdentifier, User user)
            throws VipException {

        verifyExternalPlatform(externalPlatform);

        String apiUrl = externalPlatform.getUrl() + "/api/v1";

        String token = getToken(user.getEmail(), apiUrl, externalPlatform.getIdentifier());

        String filename = "//";
        if (! CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(parameterName)) {
            filename = getFilename(apiUrl, fileIdentifier, token);
        }
        return buildUri(filename, apiUrl, fileIdentifier, token);
    }

    private void verifyExternalPlatform(ExternalPlatform externalPlatform)
            throws VipException {
        if ( ! externalPlatform.getType().equals(Type.GIRDER)) {
            logger.error("Trying to generate a girder URI for a non girder storage {}",
                    externalPlatform.getType());
            throw new VipException("Cannot generate girder uri");
        }
        if (externalPlatform.getUrl() == null) {
            logger.error("A girder external storage must have an URL to generate an URI");
            throw new VipException("Cannot generate girder uri");
        }
    }

    private String buildUri(
            String filename, String apiUrl, String fileId, String token) {
        return "girder:/" +
                filename +
                "?apiurl=" +
                apiUrl +
                "&fileId=" +
                fileId +
                "&token=" +
                token;
    }

    public String getToken(String userEmail, String apiUrl, String storageId)
            throws VipException {

        String key = apiKeyBusiness.apiKeysFor(userEmail)
            .stream()
            .filter(k -> storageId.equals(k.getStorageIdentifier()))
            .findFirst()
            .map(k -> k.getApiKey())
            .orElseThrow(() -> {
                logger.error("no girder api key found for {} on {}", userEmail, storageId);
                return new VipException(
                        "No api key found for storageId: " + storageId);
            });

        try {
            HttpResult res = makeHttpRequest(
                    apiUrl + "/api_key/token?key=" + key + "&duration="
                            + server.getGirderTokenDurationInDays(),
                    METHOD_POST,
                    Optional.empty());

            if (res.code >= 400) {
                logger.error("Unable to get girder token from api key {} : {}", key, res.response);
                throw new VipException(
                    "Unable to get token from api key: " + res.response);
            }

            ObjectNode node =
                new ObjectMapper().readValue(res.response, ObjectNode.class);
            return node.get("authToken").get("token").asText();
        } catch (IOException | NullPointerException | URISyntaxException ex) {
            logger.error("Error getting girder token for {} with key {}",
                    userEmail, key, ex);
            throw new VipException("Unable to get token from api key", ex);
        }
    }

    private String getFilename(String apiUrl, String fileId, String token)
            throws VipException {

        try {
            // first try resolving fileId as a file
            HttpResult res = makeHttpRequest(
                apiUrl + "/file/" + fileId,
                METHOD_GET,
                Optional.of(con -> con.setRequestProperty("Girder-Token", token)));
            // on 4xx error, try resolving fileId as a folder
            if (res.code >= 400 && res.code < 500) {
                res = makeHttpRequest(
                        apiUrl + "/folder/" + fileId,
                        METHOD_GET,
                        Optional.of(con -> con.setRequestProperty("Girder-Token", token)));
            }

            if (res.code >= 400) {
                logger.error("Unable to get girder filename for file {} : HTTP {}, body={}",
                        fileId, res.code, res.response);
                throw new VipException(
                    "Unable to get file info: " + res.response);
            }

            // get file or folder name
            ObjectNode node =
                new ObjectMapper().readValue(res.response, ObjectNode.class);
            String name = node.get("name").asText();

            // clean filename as in an uploaded file
            return DataManagerUtil.getCleanFilename(name);
        } catch (IOException | URISyntaxException ex) {
            logger.error("Error getting girder filename for {} with token {}",
                    fileId, token, ex);
            throw new VipException("Unable to get file info", ex);
        }
    }

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private HttpResult makeHttpRequest(
        String surl,
        String method,
        Optional<Consumer<HttpURLConnection>> connectionUpdater)
        throws IOException, URISyntaxException {

        URL url = new URI(surl).toURL();

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        if (METHOD_POST.equals(method)) {
            con.setDoOutput(true);
            con.setFixedLengthStreamingMode(0);
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        connectionUpdater.ifPresent(f -> f.accept(con));

        InputStream is = con.getResponseCode() >= 400
            ? con.getErrorStream()
            : con.getInputStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br1 =
             new BufferedReader(new InputStreamReader(is))) {

            String line = null;
            while ((line = br1.readLine()) != null) {
                response.append(line);
            }
        }

        con.disconnect();

        return new HttpResult(con.getResponseCode(), response.toString());
    }

    private static class HttpResult {
        final int code;
        final String response;

        public HttpResult(int code, String response) {
            this.code = code;
            this.response = response;
        }
    }
}
