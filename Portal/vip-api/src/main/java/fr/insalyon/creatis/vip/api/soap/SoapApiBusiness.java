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
package fr.insalyon.creatis.vip.api.soap;

import fr.insalyon.creatis.vip.api.business.ApiContext;
import fr.insalyon.creatis.vip.api.business.ApiException;
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
public class SoapApiBusiness {

    private final static Logger logger = Logger.getLogger(SoapApiBusiness.class);
    private final String authFailedMessage = "API user is not logged in.";

    private final ConfigurationBusiness configurationBusiness;

    public SoapApiBusiness() {
        this.configurationBusiness = new ConfigurationBusiness();
    }

    public SoapApiBusiness(ConfigurationBusiness configurationBusiness) {
        this.configurationBusiness = configurationBusiness;
    }

    public ApiContext getApiContext(
            WebServiceContext wsContext,
            boolean authenticate) throws ApiException {

        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest request = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse) mc.get(MessageContext.SERVLET_RESPONSE);

        return getApiContext(request, response, authenticate);
    }

    public ApiContext getApiContext(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean authenticate) throws ApiException {

        try {
            // set logging properties and DB connection
            PropertyConfigurator.configure(ConfigurationBusiness.class.getClassLoader().getResource("vipLog4j.properties"));
            PlatformConnection.getInstance();

            //set request and response
            HttpSession session = request.getSession();
            if (session == null) {
                throw new ApiException("No session in WebServiceContext");
            }

            // Authentication
            User user = null;
            if (authenticate) {
                user = authenticateSession(request, response);
            }
            return new ApiContext(request, response, user);
        } catch (DAOException ex) {
            throw new ApiException(ex);
        }
    }

    protected final User authenticateSession(HttpServletRequest request, HttpServletResponse response) throws ApiException {
        try {
            //verify session
            String email = getCookieValue(request, CoreConstants.COOKIES_USER);
            String sessionId = getCookieValue(request, CoreConstants.COOKIES_SESSION);
            if (email == null || sessionId == null) {
                throw new ApiException(this.authFailedMessage);
            }
            email = URLDecoder.decode(email, "UTF-8");
            if (configurationBusiness.validateSession(email, sessionId)) {
                logger.info("API successfully authenticated user " + email);
                User user = configurationBusiness.getUser(email);
                AbstractAuthenticationService.setVIPSession(request, response, user);
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
