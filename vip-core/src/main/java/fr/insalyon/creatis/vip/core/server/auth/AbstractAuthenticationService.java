/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.auth;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import fr.insalyon.creatis.vip.core.server.dao.*;

import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author glatard
 */
public abstract class AbstractAuthenticationService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationService.class);

    protected abstract void checkValidRequest(HttpServletRequest request) throws BusinessException;

    protected abstract String getEmail() throws BusinessException;

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
        } catch (BusinessException | CoreException ex) {
            logger.error("Error handling a request : {}. Ignoring", ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (BusinessException | CoreException ex) {
            logger.error("Error handling a request : {}. Ignoring", ex.getMessage());
        }
    }

    private void processRequest(
            HttpServletRequest request,
            HttpServletResponse response) throws BusinessException, CoreException {

        logger.info("Third-party authentication request.");
        String email;
        try {
            checkValidRequest(request);
            email = getEmail();
        } catch (BusinessException ex) {
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
            String email) throws BusinessException, CoreException {

        String groupName = getDefaultGroup();
        User user = configurationBusiness.getOrCreateUser(email, null, groupName);
        //third-party authentication services will *not* be trusted to let admins in
        if (user.isSystemAdministrator()) {
            authFailedResponse(request, response);
            return;
        }
        vipSessionBusiness.setVIPSession(request, response, user);
        try {
            response.sendRedirect(getBaseURL());
        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
        logger.info("User " + email + " connected.");
        response.setStatus(HttpServletResponse.SC_OK);
        setResponseText(email, response);
    }

    private void setResponseText(String message, HttpServletResponse response) throws BusinessException {
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
            throw new BusinessException(ex);
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
