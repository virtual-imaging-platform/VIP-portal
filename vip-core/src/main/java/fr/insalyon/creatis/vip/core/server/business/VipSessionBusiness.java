package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*
    Manages the reads/writes of vip core information in the web session
    Handle the recovery when the session is lost (server restart) but
    the identification cookie is still there.
 */

@Service
@Transactional
public class VipSessionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Server server;
    protected ConfigurationBusiness configurationBusiness;

    @Autowired
    public VipSessionBusiness(Server server, ConfigurationBusiness configurationBusiness) {
        this.server = server;
        this.configurationBusiness = configurationBusiness;
    }

    public void setVIPSession(
            HttpServletRequest request,
            HttpServletResponse response,
            User user) throws BusinessException, CoreException {
        try {
            configurationBusiness.updateUserLastLogin(user.getEmail());
            user = setUserInSession(user, request.getSession());
            Cookie userCookie = new Cookie(CoreConstants.COOKIES_USER, URLEncoder.encode(user.getEmail(), "UTF-8"));
            userCookie.setMaxAge((int) (CoreConstants.COOKIES_EXPIRATION_DATE.getTime() - new Date().getTime()));
            userCookie.setPath("/");
            response.addCookie(userCookie);
            Cookie sessionCookie = new Cookie(CoreConstants.COOKIES_SESSION, user.getSession());
            userCookie.setMaxAge((int) (CoreConstants.COOKIES_EXPIRATION_DATE.getTime() - new Date().getTime()));
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        } catch (UnsupportedEncodingException ex) {
            logger.error("Error setting VIP session for {} ",user, ex);
            throw new BusinessException(ex);
        }
    }

    public User getUserFromSession(HttpServletRequest request) throws CoreException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(CoreConstants.SESSION_USER);
        if (user != null) {
            return user;
        }
        // the user is not in session (vip server restarted)
        // we try to find user info in cookie and reload the session from it
        user = resetSessionFromCookie(request);
        if (user != null) {
            return user;
        }
        // still not there -> no user info available -> no user logged in
        logger.error("No VIP user found in session {}. Attributes : {}",
                session.getId(), enumerationToString(session.getAttributeNames()));
        throw new CoreException("User not logged in.");
    }


    public Map<Group, GROUP_ROLE> getUserGroupsFromSession(HttpServletRequest request)
            throws CoreException {
        HttpSession session = request.getSession();

        @SuppressWarnings("unchecked")
        Supplier<Map<Group, GROUP_ROLE>> groupsSupplier =
                () -> (Map<Group, GROUP_ROLE>) session.getAttribute(CoreConstants.SESSION_GROUPS);
        Map<Group, GROUP_ROLE> groups = groupsSupplier.get();
        if (groups != null) {
            return groups;
        }
        // user groups info not in session (vip server restarted)
        // we try to find user info in cookie and reload the session from it
        resetSessionFromCookie(request);
        groups = groupsSupplier.get();
        if (groups != null) {
            return groups;
        }
        // still not there -> error (because no user is logged in)
        logger.error("No VIP groups found in session {}. Attributes : {}",
                session.getId(), enumerationToString(session.getAttributeNames()));
        throw new CoreException("User has no groups defined.");
    }

    protected User resetSessionFromCookie(HttpServletRequest request)
            throws CoreException {

        try {
            Map<String, String> cookies = getCookies(request);

            if ( ! cookies.containsKey(CoreConstants.COOKIES_USER) ||
                    ! cookies.containsKey(CoreConstants.COOKIES_SESSION)) {
                return null;
            }

            String email = cookies.get(CoreConstants.COOKIES_USER);
            String sessionId = cookies.get(CoreConstants.COOKIES_SESSION);
            // the cookies are there, verify them
            logger.info("Using cookies to reload session for {} ", email);

            if (configurationBusiness.validateSession(email, sessionId)) {
                return setUserInSession(email, request.getSession());
            }
            return null;

        } catch (BusinessException e) {
            throw new CoreException(e);
        }
    }

    private Map<String, String> getCookies(HttpServletRequest request) {
        HashMap<String,String> cookiesMap = new HashMap<>();
        for (Cookie cookie : request.getCookies()) {
            cookiesMap.put(cookie.getName(), decodeCookieValue(cookie.getValue()));
        }
        return cookiesMap;
    }

    private String decodeCookieValue(String encodedValue) {
        try {
            return URLDecoder.decode(encodedValue, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("cannot url decode cookie value {}", encodedValue);
            return encodedValue;
        }
    }

    private User setUserInSession(String  email, HttpSession session) throws CoreException {
        try {
            User user = configurationBusiness.getUser(email);
            return setUserInSession(user, session);
        } catch (BusinessException e) {
            throw new CoreException(e);
        }
    }

    public User setUserInSession(User user, HttpSession session)
            throws CoreException {
        try {
            Map<Group, GROUP_ROLE> groups =
                    configurationBusiness.getUserGroups(user.getEmail());
            user.setGroups(groups);

            session.setAttribute(CoreConstants.SESSION_USER, user);
            session.setAttribute(CoreConstants.SESSION_GROUPS, groups);

            return user;
        } catch (BusinessException e) {
            throw new CoreException(e);
        }
    }

    private String enumerationToString(Enumeration<String> enums) {
        StringBuilder st = new StringBuilder();
        while (enums.hasMoreElements()) {
            st.append(enums.nextElement()).append(" ");
        }
        return st.toString();
    }

    public Boolean isUserConnected(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(CoreConstants.SESSION_USER);
        if (user != null) {
            return true;
        }
        Map<String, String> cookies = getCookies(request);
        return cookies.containsKey(CoreConstants.COOKIES_USER) &&
                cookies.containsKey(CoreConstants.COOKIES_SESSION);
    }
}
