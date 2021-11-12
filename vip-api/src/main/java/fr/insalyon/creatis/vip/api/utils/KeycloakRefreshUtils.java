package fr.insalyon.creatis.vip.api.utils;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author alaeessaki this class is only for testing the refresh mechanism
 */
public class KeycloakRefreshUtils {


    private static final String CLIENT_ID = "vip-api";
    private static final byte ALLOWED_ATTEMPTS = 2;

    public static ResponseEntity<AccessTokenResponse> refreshToken(String offlineToken) {
        int attempt = 1; // 3 attempts max
        RestTemplate restTemplate = new RestTemplate();

        /**
         *
         * Creating the http headers with the values needed for the refreshing action
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("client_id", CLIENT_ID);
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", offlineToken);
        map.add("client_secret", "15652ba5-5e3b-4f75-9df1-d84879a7a0fa");

        HttpEntity entity = new HttpEntity(map, httpHeaders);
        ResponseEntity<AccessTokenResponse> responseEntity = null;
        while (attempt <= ALLOWED_ATTEMPTS) {
            try {
                responseEntity = restTemplate.exchange(
                        "http://localhost:8180/auth/realms/poc-realm/protocol/openid-connect/token", HttpMethod.POST,
                        entity, AccessTokenResponse.class);
                break;
            } catch (HttpClientErrorException httpClientErrorException) {
                attempt++;
            }

        }
        if (responseEntity == null) throw new RuntimeException("token not refreshed after " + attempt + " attempts");
        return responseEntity;
    }

}
