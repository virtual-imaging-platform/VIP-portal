package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.utils.KeycloakRefreshUtils;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

/**
 * @author alaeessaki
 */
@RestController
@RequestMapping("/simulate-refresh")
public class RefreshTokenController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    public RefreshTokenController(Supplier<User> currentUserSupplier) {
        super(currentUserSupplier);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String simulatingRefreshToken(final HttpServletRequest request) throws InterruptedException {
        String offline_token = request.getHeader("offline_token");//getting the offline token from header sent by the client.
        ResponseEntity<AccessTokenResponse> tokenResponseEntity = KeycloakRefreshUtils.refreshToken(offline_token);  //refreshing the token
        return tokenResponseEntity.getBody().getToken();
    }
}
