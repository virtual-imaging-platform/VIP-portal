/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.Configuration;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.h2.PlatformConnection;
import java.sql.Connection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class ConfigurationServiceImpl extends RemoteServiceServlet implements ConfigurationService {

    private static Logger logger = Logger.getLogger(ConfigurationServiceImpl.class);
    
    public Configuration loadConfiguration() {

        try {
            PlatformConnection.getInstance().setConnection(
                    (Connection) getServletContext().getAttribute("connection"));
            PlatformConnection.getInstance().createTables();
            
            HttpServletRequest request = this.getThreadLocalRequest();
            Object object = request.getAttribute("javax.servlet.request.X509Certificate");
            ConfigurationBusiness business = new ConfigurationBusiness();
            return business.loadConfiguration(object);
            
        } catch (DAOException ex) {
            return null;
        } catch (BusinessException ex) {
            return null;
        }
    }

    public String addGroup(String proxy, String groupName) {
        try {
            ConfigurationBusiness business = new ConfigurationBusiness();
            return business.addGroup(proxy, groupName);

        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String updateGroup(String proxy, String oldName, String newName) {
        try {
            ConfigurationBusiness business = new ConfigurationBusiness();
            return business.updateGroup(proxy, oldName, newName);
            
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public void removeGroup(String proxy, String groupName) {
        try {
            ConfigurationBusiness business = new ConfigurationBusiness();
            business.removeGroup(proxy, groupName);

        } catch (BusinessException ex) {
        }
    }

    public List<String> getGroups() {
        try {
            return DAOFactory.getDAOFactory().getGroupDAO().getGroups();
        } catch (DAOException ex) {
            return null;
        }
    }

    public String addUser(User user) {
        try {
            return DAOFactory.getDAOFactory().getUserDAO().add(user);
        } catch (DAOException ex) {
            return null;
        }
    }

    public String updateUser(User user) {
        try {
            return DAOFactory.getDAOFactory().getUserDAO().update(user);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void removeUser(String dn) {
        try {
            DAOFactory.getDAOFactory().getUserDAO().remove(dn);
        } catch (DAOException ex) {
        }
    }

    public List<User> getUsers() {
        try {
            return DAOFactory.getDAOFactory().getUserDAO().getUsers();
        } catch (DAOException ex) {
            return null;
        }
    }

    public User getUser(String dn) {
        try {
            return DAOFactory.getDAOFactory().getUserDAO().getUser(dn);
        } catch (DAOException ex) {
            return null;
        }
    }
}
