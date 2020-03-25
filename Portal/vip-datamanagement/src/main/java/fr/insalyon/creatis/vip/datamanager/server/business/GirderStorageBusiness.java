/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.*;
import fr.insalyon.creatis.vip.core.server.business.*;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by abonnet on 7/17/19.
 */
public class GirderStorageBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApiKeyBusiness apiKeyBusiness;

    public GirderStorageBusiness(ApiKeyBusiness apiKeyBusiness) {
        this.apiKeyBusiness = apiKeyBusiness;
    }

    /* GASW regexps in 3.2.0 version
        local fileName=`echo $URI | sed -r 's#^girder:/(//)?([^/].*)\?.*$#\2#i'`
        local apiUrl=`echo $URI | sed -r 's/^.*[?&]apiurl=([^&]*)(&.*)?$/\1/i'`
        local fileId=`echo $URI | sed -r 's/^.*[?&]fileid=([^&]*)(&.*)?$/\1/i'`
        local token=`echo $URI | sed -r 's/^.*[?&]token=([^&]*)(&.*)?$/\1/i'`

        So objective : generate "girder:/filename?apiurl=[...]&fileId=[...]&token=[...]
     */
    public String generateUri(
        ExternalPlatform externalPlatform,
        String parameterName,
        String fileIdentifier,
        User user,
        Connection connection)
        throws BusinessException {

        verifyExternalPlatform(externalPlatform);

        String apiUrl = externalPlatform.getUrl() + "/api/v1";

        String token = getToken(
            user.getEmail(),
            apiUrl,
            externalPlatform.getIdentifier(),
            connection);

        String filename = "//";
        if (! CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(parameterName)) {
            filename = getFilename(apiUrl, fileIdentifier, token);
        }
        return buildUri(filename, apiUrl, fileIdentifier, token);
    }

    private void verifyExternalPlatform(ExternalPlatform externalPlatform) throws BusinessException {
        if ( ! externalPlatform.getType().equals(Type.GIRDER)) {
            logger.error("Trying to generate a girder URI for a non girder storage" +
                    "{" + externalPlatform.getType() + "}");
            throw new BusinessException("Cannot generate girder uri");
        }
        if (externalPlatform.getUrl() == null) {
            logger.error("A girder external storage must have an URL to generate an URI");
            throw new BusinessException("Cannot generate girder uri");
        }
    }

    private String buildUri(String filename, String apiUrl, String fileId, String token) {
        return new StringBuilder()
                .append("girder:/")
                .append(filename)
                .append("?apiurl=")
                .append(apiUrl)
                .append("&amp;fileId=")
                .append(fileId)
                .append("&amp;token=")
                .append(token)
                .toString();
    }

    private String getToken(
        String userEmail,
        String apiUrl,
        String storageId,
        Connection connection) throws BusinessException {

        String key = apiKeyBusiness.apiKeysFor(userEmail, connection)
            .stream()
            .filter(k -> storageId.equals(k.getStorageIdentifier()))
            .findFirst()
            .map(k -> k.getApiKey())
            .orElseThrow(() -> new BusinessException(
                             "No api key found for storageId: " + storageId));

        try {
            HttpResult res = makeHttpRequest(
                apiUrl
                  + "/api_key/token?key=" + key
                  + "&duration="
                    + Server.getInstance().getGirderTokenDurationInDays(),
                METHOD_POST,
                Optional.empty());

            if (res.code >= 400) {
                throw new BusinessException(
                    "Unable to get token from api key: " + res.response);
            }

            ObjectNode node =
                new ObjectMapper().readValue(res.response, ObjectNode.class);
            String token = node.get("authToken").get("token").asText();

            return token;
        } catch (IOException | NullPointerException ex) {
            logger.error("Error getting girder token for {} with key {}",
                    userEmail, key, ex);
            throw new BusinessException("Unable to get token from api key", ex);
        }
    }

    private String getFilename(
        String apiUrl,
        String fileId,
        String token) throws BusinessException {

        try {
            HttpResult res = makeHttpRequest(
                apiUrl + "/file/" + fileId,
                METHOD_GET,
                Optional.of(
                    con -> con.setRequestProperty("Girder-Token", token)));

            if (res.code >= 400) {
                throw new BusinessException(
                    "Unable to get file info: " + res.response);
            }

            ObjectNode node =
                new ObjectMapper().readValue(res.response, ObjectNode.class);
            String name = node.get("name").asText();

            return name;
        } catch (IOException ex) {
            logger.error("Error getting girder filename for {} with token {}",
                    fileId, token, ex);
            throw new BusinessException("Unable to get file info", ex);
        }
    }

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private HttpResult makeHttpRequest(
        String surl,
        String method,
        Optional<Consumer<HttpURLConnection>> connectionUpdater)
        throws IOException {

        URL url = new URL(surl);

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
