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
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractRemoteServiceServlet extends RemoteServiceServlet {

    protected ConfigurationBusiness configurationBusiness;

    public AbstractRemoteServiceServlet() {

        configurationBusiness = new ConfigurationBusiness();
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

    /**
     * 
     * @return 
     */
    protected HttpSession getSession() {
        return this.getThreadLocalRequest().getSession();
    }

    /**
     * 
     * @return
     * @throws CoreException 
     */
    public User getSessionUser() throws CoreException {

        User user = (User) getSession().getAttribute(CoreConstants.SESSION_USER);
        if (user != null) {
            return user;
        }
        throw new CoreException("User not logged in.");
    }

    /**
     * 
     * @return
     * @throws CoreException 
     */
    protected Map<Group, GROUP_ROLE> getSessionUserGroups() throws CoreException {

        Map<Group, GROUP_ROLE> groups = (Map<Group, GROUP_ROLE>) getSession().getAttribute(CoreConstants.SESSION_GROUPS);
        if (groups != null) {
            return groups;
        }
        throw new CoreException("User has no groups defined.");
    }

    /**
     * 
     * @param logger
     * @throws CoreException 
     */
    protected void authenticateSystemAdministrator(Logger logger) throws CoreException {

        User user = getSessionUser();
        if (!user.isSystemAdministrator()) {
            logger.error("The user has no system administrator rights: " + user.getEmail());
            throw new CoreException("The user has no system administrator rights.");
        }
    }

    /**
     * 
     * @param logger
     * @throws CoreException
     * @throws BusinessException 
     */
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

    /**
     * 
     * @param logger
     * @param message
     * @throws CoreException 
     */
    protected void trace(Logger logger, String message) throws CoreException {
        logger.info("(" + getSessionUser().getEmail() + ") " + message);
    }
}
