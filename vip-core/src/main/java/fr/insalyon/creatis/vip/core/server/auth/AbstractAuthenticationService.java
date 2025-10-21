package fr.insalyon.creatis.vip.core.server.auth;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractAuthenticationService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationService.class);

    protected abstract void checkValidRequest(HttpServletRequest request) throws VipException;

    protected abstract String getEmail() throws VipException;

    public abstract String getDefaultGroup();

    private UserDAO userDAO;
    private ConfigurationBusiness configurationBusiness;
    private VipSessionBusiness vipSessionBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext = WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        userDAO = applicationContext.getBean(UserDAO.class);
        configurationBusiness = applicationContext.getBean(ConfigurationBusiness.class);
        vipSessionBusiness = applicationContext.getBean(VipSessionBusiness.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (VipException ex) {
            logger.error("Error handling a request : {}. Ignoring", ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (VipException ex) {
            logger.error("Error handling a request : {}. Ignoring", ex.getMessage());
        }
    }

    private void processRequest(
            HttpServletRequest request,
            HttpServletResponse response) throws VipException, CoreException {

        logger.info("Third-party authentication request.");
        String email;
        try {
            checkValidRequest(request);
            email = getEmail();
        } catch (VipException ex) {
            logger.info(ex.getMessage());
            authFailedResponse(request, response);
            return;
        }
        //verify email format
        if (email == null || !isValidEmailAddress(email)) {
            logger.info("Invalid email: " + email);
            authFailedResponse(request, response);
            return;
        }
        resetFailedAuthenticationCount(email);
        //authenticate email in VIP
        authSuccessResponse(request, response, email);
    }

    private void resetFailedAuthenticationCount(String email) {
        try {
            userDAO.resetNFailedAuthentications(email);
            logger.debug("Reset auth count for " + email);
        } catch (DAOException e) {
            logger.error("Error resetting failed auth counter for {}. Ignoring it", email, e);
        }
    }

    private String getBaseURL() {
        return "/";
    }

    private void authSuccessResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            String email) throws VipException, CoreException {

        String groupName = getDefaultGroup();
        User user = configurationBusiness.getOrCreateUser(email, "Unknown", groupName);
        //third-party authentication services will *not* be trusted to let admins in
        if (user.isSystemAdministrator()) {
            authFailedResponse(request, response);
            return;
        }
        vipSessionBusiness.setVIPSession(request, response, user);
        try {
            response.sendRedirect(getBaseURL());
        } catch (IOException ex) {
            throw new VipException(ex);
        }
        logger.info("User " + email + " connected.");
        response.setStatus(HttpServletResponse.SC_OK);
        setResponseText(email, response);
    }

    private void setResponseText(String message, HttpServletResponse response) throws VipException {
        PrintWriter out = null;
        try {
            response.setContentType("text/html");
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setContentLength(message.length());
            out = response.getWriter();
            out.println(message);
            out.flush();
        } catch (IOException ex) {
            logger.error("Error writing auth response " + message, ex);
            throw new VipException(ex);
        } finally {
            out.close();
        }
    }



    private void authFailedResponse(
            HttpServletRequest request, HttpServletResponse response) {
        logger.info("Third-party authentication failed.");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
