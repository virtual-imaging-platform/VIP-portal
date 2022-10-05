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
package fr.insalyon.creatis.vip.core.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.VipSessionBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Parent for all vip GWT RPC servlet.
 *
 * Includes the mechanism to access spring managed beans in all subclasses,
 * as the Server bean here.
 *
 * @author Rafael Silva
 */
public abstract class AbstractRemoteServiceServlet extends RemoteServiceServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Server server;
    private ApplicationContext applicationContext;
    private VipSessionBusiness vipSessionBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        applicationContext =
                WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        server = applicationContext.getBean(Server.class);
        vipSessionBusiness = getBean(VipSessionBusiness.class);
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

    protected HttpSession getSession() {
        return this.getThreadLocalRequest().getSession();
    }

    protected User getSessionUser() throws CoreException {
        return vipSessionBusiness.getUserFromSession(this.getThreadLocalRequest());
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
