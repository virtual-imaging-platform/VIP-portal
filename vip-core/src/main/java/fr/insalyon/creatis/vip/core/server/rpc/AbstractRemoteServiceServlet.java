package fr.insalyon.creatis.vip.core.server.rpc;

import java.util.Map;

import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Parent for all vip GWT RPC servlet.
 *
 * Includes the mechanism to access spring managed beans in all subclasses,
 * as the Server bean here.
 */
public abstract class AbstractRemoteServiceServlet extends RemoteServiceServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Server server;
    private ApplicationContext applicationContext;
    private VipSessionBusiness vipSessionBusiness;
    private SessionAuthenticationProvider sessionAuthenticationProvider;

    @Override
    public void init() throws ServletException {
        super.init();
        applicationContext =
                WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        server = applicationContext.getBean(Server.class);
        vipSessionBusiness = getBean(VipSessionBusiness.class);
        sessionAuthenticationProvider = getBean(SessionAuthenticationProvider.class);
    }

    /*
        allows spring beans injection in all subclass
     */
    protected final <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    // see http://blog.excilys.com/2011/05/12/gwt-google-wont-throw/
    @Override
    protected void doUnexpectedFailure(Throwable e) {
        try {
            super.doUnexpectedFailure(e);
        } finally {
            // log the error (otherwise only logged in container log files)
            logger.error("Unexpected exception caught in a GWT service impl ", e);
            // do not silence Error because some should stop the JVM
            if (e instanceof Error) {
                throw (Error) e;
            }
        }
    }

    @Override
    protected void checkPermutationStrongName() throws SecurityException {
        // Content-Type text/x-gwt-rpc; charset=utf-8
        // X-GWT-Permutation F1AEC601C5D8E4490E7096AB58EB
        HttpServletRequest req = this.getThreadLocalRequest();
        if (!req.getContentType().contains("text/x-gwt-rpc")) {
            super.checkPermutationStrongName();
        }
    }

    @Override
    public String processCall(RPCRequest rpcRequest) throws SerializationException {
        try {
            // normally it should not happen, but in case of a weird user browser session or cookies, ignore if no
            // user is found
            User user = getSessionUser();
            if (user != null) {
                // To set up the user in spring security to make it accessible through the Supplier<User> used in
                // updated business's
                // Necessary from GWT context, as spring security is not activated there
                SecurityContextHolder.getContext().setAuthentication(
                        sessionAuthenticationProvider.createAuthenticationFromUser(user));
            }
        } catch (CoreException e) {
            doUnexpectedFailure(e);
        }
        return super.processCall(rpcRequest);
    }

    protected HttpSession getSession() {
        return this.getThreadLocalRequest().getSession();
    }

    protected User getSessionUser() throws CoreException {
        return vipSessionBusiness.getUserFromSession(getThreadLocalRequest(), getThreadLocalResponse());
    }

    protected Map<Group, GROUP_ROLE> getUserGroupsFromSession() throws CoreException {
        return vipSessionBusiness.getUserGroupsFromSession(this.getThreadLocalRequest());
    }

    protected User setUserInSession(User user) throws CoreException {
        return vipSessionBusiness.setUserInSession(user, getSession());
    }

    protected void authenticateSystemAdministrator(Logger logger) throws CoreException {
        User user = getSessionUser();
        if (!user.isSystemAdministrator()) {
            logger.error("The user has no system administrator rights: " + user.getEmail());
            throw new CoreException("The user has no system administrator rights.");
        }
    }

    protected void authenticateDeveloper(Logger logger) throws CoreException {
        User user = getSessionUser();
        if (!user.isDeveloper()) {
            logger.error("The user has no system administrator rights: " + user.getEmail());
            throw new CoreException("The user has no system developer rights.");
        }
    }

    protected void authenticateGroupAdministrator(Logger logger) throws CoreException {

        User user = getSessionUser();
        if (!user.isGroupAdmin()) {
            logger.error("The user has no group administrator rights: " + user.getEmail());
            throw new CoreException("The user has no group administrator rights.");
        }
    }
    
    protected boolean isSystemAdministrator() throws CoreException {
        return getSessionUser().isSystemAdministrator();
    }
    
    protected boolean isGroupAdministrator() throws CoreException {
        return getSessionUser().isGroupAdmin();
    }

    protected boolean isDeveloper() throws CoreException {
        return getSessionUser().isDeveloper();
    }

    protected boolean isUserConnected() {
        return vipSessionBusiness.isUserConnected(this.getThreadLocalRequest());
    }

    protected void trace(Logger logger, String message) throws CoreException {
        if (vipSessionBusiness.isUserConnected(this.getThreadLocalRequest())) {
            logger.info("(" + getSessionUser().getEmail() + ") " + message);
        } else {
            logger.info("(Anonymous) " + message);
        }
    }
}
