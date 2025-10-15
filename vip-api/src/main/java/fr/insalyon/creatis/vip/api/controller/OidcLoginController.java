package fr.insalyon.creatis.vip.api.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriUtils;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.SessionBusiness;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class OidcLoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SessionBusiness sessionBusiness;
    private final ConfigurationBusiness configurationBusiness;

    @Autowired
    public OidcLoginController(SessionBusiness sessionBusiness, ConfigurationBusiness configurationBusiness) {
        this.sessionBusiness = sessionBusiness;
        this.configurationBusiness = configurationBusiness;
    }

    @GetMapping("/loginOIDC")
    public RedirectView getOauth2LoginInfo(
            HttpServletRequest request, HttpServletResponse response, Principal user)
            throws ApiException {

        if ( ! (user instanceof OAuth2AuthenticationToken)) {
            logger.error("OIDC login error: Principal is not an OAuth2AuthenticationToken. User: [{}]", user);
            throw new ApiException(ApiException.ApiError.WRONG_OIDC_LOGIN);
        }
        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        if ( ! authToken.isAuthenticated()) {
            logger.error("OIDC login error: anonymous user");
            throw new ApiException(ApiException.ApiError.WRONG_OIDC_LOGIN);
        }
        Map<String, Object> userAttributes = authToken.getPrincipal().getAttributes();
        logger.info("OIDC login success. User attributes: [{}]", userAttributes);

        Object institution = userAttributes.get("eduperson_scoped_affiliation");
        String domainName;
        if(institution != null){
            String institution_string = institution.toString();
            String temp = institution_string .substring(institution_string .indexOf("@") + 1);
            domainName = temp.substring(0, temp.indexOf("."));
            domainName = domainName.toUpperCase();
        } else {
            domainName = "Unknown";
        }

        try {
            User vipUser = configurationBusiness.getOrCreateUser((String) userAttributes.get("email"), domainName, null);
            Session session = new Session();

            session.id = vipUser.getSession();
            session.email = vipUser.getEmail();
            
            SecurityContextHolder.clearContext(); // destroy spring session and use VIP own session mechanism
            sessionBusiness.createLoginCookies(request, response, session); // creates VIP cookies and session
        } catch (VipException | UnsupportedEncodingException e) {
            throw new ApiException(ApiException.ApiError.WRONG_OIDC_LOGIN, e);
        }

        return new RedirectView(getRootUrl(request)); // redirect on the home page where the VIP cookies will authenticate the user
    }

    private String getRootUrl(HttpServletRequest request) {
        String decodedUri = UriUtils.decode(request.getRequestURI(), "UTF-8");
        int index = decodedUri.indexOf("/rest/loginOIDC");
        return decodedUri.substring(0, index+1); // keep trailing slash
    }
}


