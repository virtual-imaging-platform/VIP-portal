package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;

@RestController
public class EgiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final VipSessionBusiness vipSessionBusiness;
    private ConfigurationBusiness configurationBusiness;

    @Autowired
    public EgiController(VipSessionBusiness vipSessionBusiness, ConfigurationBusiness configurationBusiness) {
        this.vipSessionBusiness = vipSessionBusiness;
        this.configurationBusiness = configurationBusiness;
    }

    @GetMapping("/loginEgi")
    public RedirectView getOauth2LoginInfo(HttpServletRequest request,
                                           HttpServletResponse response, Principal user) throws BusinessException, CoreException {

        StringBuffer protectedInfo = new StringBuffer();

        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        if(authToken.isAuthenticated()){

            Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();
            protectedInfo.append( userAttributes.get("email"));
            String accountType = null;
            User vipUser = configurationBusiness.getOrCreateUser((String) userAttributes.get("email"), accountType);
            vipSessionBusiness.setVIPSession(request, response, vipUser);
        }
        else{
            protectedInfo.append("NA");
        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/");
        logger.info(String.valueOf(redirectView));

        return redirectView;
    }
}


