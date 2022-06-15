package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author alaeessaki this class is only for testing the refresh mechanism
 */
@Service
public class KeycloakBusiness {

    private final Environment env;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte ALLOWED_ATTEMPTS = 2;

    @Autowired
    KeycloakBusiness(Environment env){
        this.env = env;
    }

    public ResponseEntity<AccessTokenResponse> refreshToken(String offlineToken) throws ApiException {
        int attempt = 1; // 2 attempts max
        RestTemplate restTemplate = new RestTemplate();

        /**
         *
         * Creating the http headers with the values needed for the refreshing action
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("client_id", env.getRequiredProperty(CarminProperties.KEYCLOAK_CLIENT_ID));
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", offlineToken);
        map.add("client_secret", env.getRequiredProperty(CarminProperties.KEYCLOAK_CLIENT_SECRET));

        HttpEntity entity = new HttpEntity(map, httpHeaders);
        ResponseEntity<AccessTokenResponse> responseEntity = null;
        while (attempt <= ALLOWED_ATTEMPTS) {
            try {
                responseEntity = restTemplate.exchange(
                        env.getRequiredProperty(CarminProperties.KEYCLOAK_REALM_URL), HttpMethod.POST,
                        entity, AccessTokenResponse.class);
                break;
            } catch (HttpClientErrorException httpClientErrorException) {
                logger.info(httpClientErrorException.getMessage());
                attempt++;
            }
        }

        if (responseEntity == null){
            throw new ApiException("token not refreshed after " + attempt + " attempts");
        }
        return responseEntity;
    }

}
