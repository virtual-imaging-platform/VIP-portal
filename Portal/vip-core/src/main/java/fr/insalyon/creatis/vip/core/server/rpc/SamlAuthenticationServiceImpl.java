/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.SamlTokenValidator;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
public class SamlAuthenticationServiceImpl extends HttpServlet {

    private static Logger logger = Logger.getLogger(SamlAuthenticationServiceImpl.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (BusinessException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (BusinessException ex) {
               logger.error(ex.getMessage());
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        logger.info("SAML authentication request");

        String token = request.getParameter("_saml_token");
        if(token == null){
            logger.info("SAML token is null");
             authFailedResponse(request, response);
             return;
        }          

        String email;
        try {
            email = SamlTokenValidator.getEmailIfValid(token, Server.getInstance().getSamlTrustedCertificate() , request.getRequestURL().toString());
        } catch (CoreException ex) {
            logger.info(ex.getMessage());
            authFailedResponse(request, response);
            return;
        }

        //authenticate email in VIP
        authSuccessResponse(request, response, email);
    }

    private void authFailedResponse(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        logger.info("SAML Authentication failed.");
        try {
            response.sendRedirect(getBaseURL(request));
        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
         setResponseText(response);
    }

    private void authSuccessResponse(HttpServletRequest request, HttpServletResponse response, String email) throws BusinessException {
        try {
            ConfigurationBusiness cb = new ConfigurationBusiness();
            
            String message = "";
            
            // put this in a common package that could be reused by different authentication schemes
            // get or signup user
            User user = cb.getOrCreateUser(email);
            
            // Admins have to log through our system
            if (user.isSystemAdministrator()) {
                authFailedResponse(request, response);
                return;
            }
            
            // Set proper VIP session
            setVIPSession(request, response, user);
            
            // Redirect to home page
            response.sendRedirect(getBaseURL(request));
            
            logger.info("User " + email + " connected.");
            setResponseText(response);
            
        } catch (IOException ex) {
            throw new BusinessException(ex);
        }

    }

    private void setVIPSession(HttpServletRequest request, HttpServletResponse response, User user) throws BusinessException {
        try {
            ConfigurationServiceImpl csi = new ConfigurationServiceImpl();
            user = csi.setUserSession(user, request.getSession());
            ConfigurationBusiness cb = new ConfigurationBusiness();
            cb.updateUserLastLogin(user.getEmail());

            Cookie userCookie = new Cookie(CoreConstants.COOKIES_USER, URLEncoder.encode(user.getEmail(), "UTF-8"));
            userCookie.setMaxAge((int) (CoreConstants.COOKIES_EXPIRATION_DATE.getTime() - new Date().getTime()));
            userCookie.setPath("/");
            response.addCookie(userCookie);

            Cookie sessionCookie = new Cookie(CoreConstants.COOKIES_SESSION, user.getSession());
            userCookie.setMaxAge((int) (CoreConstants.COOKIES_EXPIRATION_DATE.getTime() - new Date().getTime()));
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        } catch (UnsupportedEncodingException ex) {
            throw new BusinessException(ex);
        }
    }

    private static String getBaseURL(HttpServletRequest request) {
        return "/";
    }

    private void setResponseText(HttpServletResponse response) throws BusinessException {
        PrintWriter out = null;
        try {
            response.setContentType("text/html");
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            out = response.getWriter();
            out.println("<html>");
            out.println("<body>");
            out.println("<p>If you see this page you probably failed to authenticate on VIP. Please contact vip-support@creatis.insa-lyon.fr for support.</p>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
        } catch (IOException ex) {
            throw new BusinessException(ex);
        } finally {
            out.close();
        }

    }
}
