/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.portal.client.bean.AppClass;
import fr.insalyon.creatis.vip.portal.client.bean.WorkflowDescriptor;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationServiceImpl extends RemoteServiceServlet implements ApplicationService {

    public String add(WorkflowDescriptor workflowDescriptor) {
        return DAOFactory.getDAOFactory().getApplicationDAO().add(workflowDescriptor);
    }

    public String update(WorkflowDescriptor workflowDescriptor) {
        return DAOFactory.getDAOFactory().getApplicationDAO().update(workflowDescriptor);
    }

    public void remove(String name) {
        DAOFactory.getDAOFactory().getApplicationDAO().remove(name);
    }

    public WorkflowDescriptor getApplication(String name) {
        return DAOFactory.getDAOFactory().getApplicationDAO().getApplication(name);
    }

    public String addClass(AppClass c) {
        return DAOFactory.getDAOFactory().getClassDAO().add(c);
    }

    public String updateClass(AppClass c) {
        return DAOFactory.getDAOFactory().getClassDAO().update(c);
    }

    public void removeClass(String name) {
        DAOFactory.getDAOFactory().getClassDAO().remove(name);
    }

    public List<AppClass> getClasses() {
        return DAOFactory.getDAOFactory().getClassDAO().getClasses();
    }

    public List<String> getApplicationsName(String applicationClass) {
        return DAOFactory.getDAOFactory().getApplicationDAO().getApplicationsName(applicationClass);
    }

    public AppClass getClass(String className) {
        return DAOFactory.getDAOFactory().getClassDAO().getClass(className);
    }
}
