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

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.rpc.ConfigurationServiceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.net.ssl.X509TrustManager;
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
public abstract class AbstractAuthenticationService extends HttpServlet {

    private static Logger logger = Logger.getLogger(AbstractAuthenticationService.class);

    protected abstract String getEmailIfValidRequest(HttpServletRequest request) throws BusinessException;

    public abstract String getDefaultAccountType();

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
        logger.info("Third-party authentication request.");
        String email = null;
        try {
            email = getEmailIfValidRequest(request);
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
        //authenticate email in VIP
        authSuccessResponse(request, response, email);
    }

    private String getBaseURL(HttpServletRequest request) {
        return "/";
    }

    private void authSuccessResponse(HttpServletRequest request, HttpServletResponse response, String email) throws BusinessException {
        //   try {
        ConfigurationBusiness cb = new ConfigurationBusiness();
        String message = "";
        String accountType = getDefaultAccountType();
        User user = cb.getOrCreateUser(email, accountType);
        //third-party authentication services will *not* be trusted to let admins in
        if (user.isSystemAdministrator()) {
            authFailedResponse(request, response);
            return;
        }
        setVIPSession(request, response, user);
        try {
            response.sendRedirect(getBaseURL(request));
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
            throw new BusinessException(ex);
        } finally {
            out.close();
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

    private void authFailedResponse(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
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

    private final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    private class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }
}
