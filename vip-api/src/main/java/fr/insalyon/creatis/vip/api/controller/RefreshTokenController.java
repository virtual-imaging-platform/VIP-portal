package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.business.KeycloakBusiness;
import org.keycloak.representations.AccessTokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author alaeessaki
 */
@RestController
@RequestMapping("/simulate-refresh")
public class RefreshTokenController extends ApiController {

    private final KeycloakBusiness refreshUtils;

    @Autowired
    public RefreshTokenController(KeycloakBusiness refreshUtils ) {
        this.refreshUtils = refreshUtils;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String simulatingRefreshToken(final HttpServletRequest request) throws ApiException {
        if ( ! isKeycloakActive()) {
            throw new ApiException("Keycloak is not activated on the VIP server");
        }
        String offline_token = request.getHeader("offline_token"); //getting the offline token from header sent by the client.
        ResponseEntity<AccessTokenResponse> tokenResponseEntity = refreshUtils.refreshToken(offline_token);  //refreshing the token
        return tokenResponseEntity.getBody().getToken();
    }
}
