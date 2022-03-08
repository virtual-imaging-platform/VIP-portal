package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;

@RestController
public class EgiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final VipSessionBusiness vipSessionBusiness;
    private final ConfigurationBusiness configurationBusiness;

    @Autowired
    public EgiController(VipSessionBusiness vipSessionBusiness, ConfigurationBusiness configurationBusiness) {
        this.vipSessionBusiness = vipSessionBusiness;
        this.configurationBusiness = configurationBusiness;
    }

    @GetMapping("/loginEgi")
    public RedirectView getOauth2LoginInfo(
            HttpServletRequest request, HttpServletResponse response, Principal user)
            throws ApiException {

        if (!(user instanceof OAuth2AuthenticationToken)) {
            logger.error("Egi login must only be called after an OIDC login. User name : [{}]. User type : [{}]",
                    user.getClass().getCanonicalName(), user.getName());
            throw new ApiException(ApiException.ApiError.WRONG_OIDC_LOGIN);
        }
        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        if (!authToken.isAuthenticated()) {
            logger.error("Egi login method called with an anonymous user");
            throw new ApiException(ApiException.ApiError.WRONG_OIDC_LOGIN);
        }

        Map<String, Object> userAttributes = authToken.getPrincipal().getAttributes();
        try {
            User vipUser = configurationBusiness.getOrCreateUser((String) userAttributes.get("email"), null);
            SecurityContextHolder.clearContext(); // destroy spring session and use VIP own session mechanism
            vipSessionBusiness.setVIPSession(request, response, vipUser); // creates VIP cookies and session
        } catch (BusinessException | CoreException e) {
            throw new ApiException(ApiException.ApiError.WRONG_OIDC_LOGIN, e);
        }

        return new RedirectView(getRootUrl(request)); // redirect on the home page where the VIP cookies will authenticate the user
    }

    private String getRootUrl(HttpServletRequest request) {
        String decodedUri = UriUtils.decode(request.getRequestURI(), "UTF-8");
        int index = decodedUri.indexOf("/rest/loginEgi");
        return decodedUri.substring(0, index);
    }
}


