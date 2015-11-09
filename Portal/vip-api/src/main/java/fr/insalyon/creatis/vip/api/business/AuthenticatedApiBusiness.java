/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.auth.AbstractAuthenticationService;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
public class AuthenticatedApiBusiness extends ApiBusiness {

    private final static Logger logger = Logger.getLogger(AuthenticatedApiBusiness.class);
    private final String authFailedMessage = "API user is not logged in.";

    private User user;
        
    public AuthenticatedApiBusiness(WebServiceContext wsContext) throws ApiException {
        super(wsContext);
        user = authenticateSession();
    }

    public User getUser(){
        return user;
    }
    
    private User authenticateSession() throws ApiException {
        try {
            //verify session
            ConfigurationBusiness configurationBusiness = new ConfigurationBusiness();
            HttpServletRequest request = getRequest(); 
            String email = getCookieValue(request, CoreConstants.COOKIES_USER);
            String sessionId = getCookieValue(getRequest(), CoreConstants.COOKIES_SESSION);
            if(email == null || sessionId == null)
                throw new ApiException(this.authFailedMessage);
            email = URLDecoder.decode(email, "UTF-8");
            if (configurationBusiness.validateSession(email, sessionId)) {
                logger.info("API successfully authenticated user "+email);
                user = configurationBusiness.getUser(email);
                AbstractAuthenticationService.setVIPSession(getRequest(), getResponse(), user);             
                configurationBusiness.updateUserLastLogin(email);
                return user;
            }
            throw new ApiException(authFailedMessage);
        } catch (BusinessException | UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }
    }

    private String getCookieValue(HttpServletRequest req, String name) {
        if(req==null){
            return null;
        }
        Cookie[] cookies = req.getCookies();
        if(cookies == null)
            return null;
        for (Cookie c : cookies) {
            if (c.getName().equals(name)) {
                return c.getValue();
            }
        }
        return null;
    }
}
