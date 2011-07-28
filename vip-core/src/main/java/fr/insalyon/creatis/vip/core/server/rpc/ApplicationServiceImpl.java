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
import fr.insalyon.creatis.vip.core.client.bean.AppClass;
import fr.insalyon.creatis.vip.core.client.bean.Application;
import fr.insalyon.creatis.vip.core.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.server.dao.DAOFactory;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationServiceImpl extends RemoteServiceServlet implements ApplicationService {

    private static Logger logger = Logger.getLogger(ApplicationServiceImpl.class);
    
    public String add(Application application) {
        try {
            return DAOFactory.getDAOFactory().getApplicationDAO().add(application);
        } catch (DAOException ex) {
            return null;
        }
    }

    public String update(Application application) {
        try {
            return DAOFactory.getDAOFactory().getApplicationDAO().update(application);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void remove(String name) {
        try {
            DAOFactory.getDAOFactory().getApplicationDAO().remove(name);
        } catch (DAOException ex) {
        }
    }
    
    public void removeClassFromApplication(String applicationClass, String applicationName) {
        try {
            DAOFactory.getDAOFactory().getApplicationDAO().removeClassFromApplication(applicationClass, applicationName);
        } catch (DAOException ex) {
        }
    }

    public Application getApplication(String name) {
        try {
            return DAOFactory.getDAOFactory().getApplicationDAO().getApplication(name);
        } catch (DAOException ex) {
            return null;
        }
    }
    
    public List<Application> getApplications(String applicationClass) {
        try {
            return DAOFactory.getDAOFactory().getApplicationDAO().getApplications(applicationClass);
        } catch (DAOException ex) {
            return null;
        }
    }

    public String addClass(AppClass c) {
        try {
            return DAOFactory.getDAOFactory().getClassDAO().add(c);
        } catch (DAOException ex) {
            return null;
        }
    }

    public String updateClass(AppClass c) {
        try {
            return DAOFactory.getDAOFactory().getClassDAO().update(c);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void removeClass(String name) {
        try {
            DAOFactory.getDAOFactory().getClassDAO().remove(name);
        } catch (DAOException ex) {
        }
    }

    public List<AppClass> getClasses() {
        try {
            return DAOFactory.getDAOFactory().getClassDAO().getClasses();
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<String> getApplicationsName(String applicationClass) {
        try {
            return DAOFactory.getDAOFactory().getApplicationDAO().getApplicationsName(applicationClass);
        } catch (DAOException ex) {
            return null;
        }
    }

    public AppClass getClass(String className) {
        try {
            return DAOFactory.getDAOFactory().getClassDAO().getClass(className);
        } catch (DAOException ex) {
            return null;
        }
    }
}
