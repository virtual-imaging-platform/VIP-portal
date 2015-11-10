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
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Tristan Glatard
 */
public class ApiBusiness {

    private final static Logger logger = Logger.getLogger(ApiBusiness.class);
    private final String authFailedMessage = "API user is not logged in.";

    private final HttpSession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private User user;

    public ApiBusiness(WebServiceContext wsContext, boolean authenticate) throws ApiException {

        try {
            // set logging properties and DB connection
            PropertyConfigurator.configure(ConfigurationBusiness.class.getClassLoader().getResource("vipLog4j.properties"));
            logger.info("Calling API method");
            PlatformConnection.getInstance();

            //set request and response
            MessageContext mc = wsContext.getMessageContext();
            request = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
            response = (HttpServletResponse) mc.get(MessageContext.SERVLET_RESPONSE);

            // set session
            session = ((javax.servlet.http.HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();
            if (session == null) {
                throw new ApiException("No session in WebServiceContext");
            }

            // Authentication
            if (authenticate) {
                user = authenticateSession();
            }
        } catch (DAOException ex) {
            throw new ApiException(ex);
        }
    }

    public HttpSession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public User getUser() {
        return user;
    }

    protected final User authenticateSession() throws ApiException {
        try {
            //verify session
            ConfigurationBusiness configurationBusiness = new ConfigurationBusiness();
            String email = getCookieValue(request, CoreConstants.COOKIES_USER);
            String sessionId = getCookieValue(getRequest(), CoreConstants.COOKIES_SESSION);
            if (email == null || sessionId == null) {
                throw new ApiException(this.authFailedMessage);
            }
            email = URLDecoder.decode(email, "UTF-8");
            if (configurationBusiness.validateSession(email, sessionId)) {
                logger.info("API successfully authenticated user " + email);
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
        if (req == null) {
            return null;
        }
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (c.getName().equals(name)) {
                return c.getValue();
            }
        }
        return null;
    }
}
