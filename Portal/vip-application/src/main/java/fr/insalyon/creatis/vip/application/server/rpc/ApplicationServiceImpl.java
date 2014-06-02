/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.ApplicationStatus;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationServiceImpl extends AbstractRemoteServiceServlet implements ApplicationService {

    private static Logger logger = Logger.getLogger(ApplicationServiceImpl.class);
    private ClassBusiness classBusiness;
    private ApplicationBusiness applicationBusiness;
    private EngineBusiness engineBusiness;

    public ApplicationServiceImpl() {

        engineBusiness = new EngineBusiness();
        classBusiness = new ClassBusiness();
        applicationBusiness = new ApplicationBusiness();
    }

    @Override
    public void signout() throws ApplicationException {

        getSession().removeAttribute(ApplicationConstants.SESSION_CLASSES);
    }

    @Override
    public void add(Application application) throws ApplicationException {

        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Adding application '" + application.getName() + "'.");
                application.setOwner(getSessionUser().getEmail());
                applicationBusiness.add(application);
            } else {
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void update(Application application) throws ApplicationException {

        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Updating application '" + application.getName() + "'.");
                applicationBusiness.update(application);

            } else {
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void remove(String name) throws ApplicationException {

        try {
            if (isSystemAdministrator()) {
                trace(logger, "Removing application '" + name + "'.");
                applicationBusiness.remove(name);

            } else {
                trace(logger, "Removing classes from application '" + name + "'.");
                applicationBusiness.remove(getSessionUser().getEmail(), name);
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void addVersion(AppVersion version) throws ApplicationException {

        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Adding version '" + version.getVersion() + "' ('" + version.getApplicationName() + "').");
                applicationBusiness.addVersion(version);
            } else {
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void updateVersion(AppVersion version) throws ApplicationException {

        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Updating version '" + version.getVersion() + "' ('" + version.getApplicationName() + "').");

                applicationBusiness.updateVersion(version);

            } else {
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void removeVersion(String applicationName, String version) throws ApplicationException {

        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Removing application '" + applicationName + "'.");
                applicationBusiness.removeVersion(applicationName, version);

            } else {
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public List<Application> getApplications() throws ApplicationException {

        try {
            if (isSystemAdministrator()) {
                return applicationBusiness.getApplications();

            } else if (isGroupAdministrator()) {
                List<String> classes = classBusiness.getUserClassesName(getSessionUser().getEmail(), true);
                return applicationBusiness.getApplications(classes);
            }
            throw new ApplicationException("You have no administrator rights.");

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public List<String[]> getApplications(String className) throws ApplicationException {

        try {
            return applicationBusiness.getApplications(className);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public boolean applicationExist(String applicationName) throws ApplicationException {

        try {
            return applicationBusiness.applicationExist(applicationName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param applicationClass
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<String[]> getApplicationsByClass(String applicationClass) throws ApplicationException {

        try {
            return applicationBusiness.getApplications(applicationClass);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public void addClass(AppClass c) throws ApplicationException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding class '" + c.getName() + "'.");
            classBusiness.addClass(c);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public void updateClass(AppClass c) throws ApplicationException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating class '" + c.getName() + "'.");
            classBusiness.updateClass(c);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public void removeClass(String name) throws ApplicationException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing class '" + name + "'.");
            classBusiness.removeClass(name);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public List<AppClass> getClasses() throws ApplicationException {

        try {
            if (isSystemAdministrator()) {
                return classBusiness.getClasses();
            }
            return classBusiness.getUserClasses(getSessionUser().getEmail(), false);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public List<String>[] getApplicationsAndUsers(List<String> reservedClasses) throws ApplicationException {

        try {
            User user = getSessionUser();
            if (isSystemAdministrator()) {
                List<String> classes = classBusiness.getClassesName();
                //trace(logger, "Admin classes are '" + classes.toString() + "'.");
                return new List[]{configurationBusiness.getUserNames(user.getEmail(), false),
                    applicationBusiness.getApplicationNames(), classes};

            } else {
                List<String> classes = classBusiness.getUserClassesName(user.getEmail(), !user.isSystemAdministrator());
                classes.removeAll(reservedClasses);
                //trace(logger, "User classes are '" + classes.toString() + "'.");
                return new List[]{configurationBusiness.getUserNames(user.getEmail(), true),
                    applicationBusiness.getApplicationNames(classes), classes};
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @return @throws ApplicationException
     */
    @Override
    public ApplicationStatus getApplicationStatus() throws ApplicationException {

        try {
            WorkflowBusiness workflowBusiness = new WorkflowBusiness();
            List<Simulation> runningSimulations = workflowBusiness.getRunningSimulations();

            ApplicationStatus status = new ApplicationStatus();
            status.setRunningWorkflows(runningSimulations.size());

            SimulationBusiness jobBusiness = new SimulationBusiness();
            int[] tasks = jobBusiness.getNumberOfActiveTasks(runningSimulations);
            status.setRunningTasks(tasks[0]);
            status.setWaitingTasks(tasks[1]);

            return status;

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @return
     * @throws ApplicationException
     */
    @Override
    public String getCitation(String applicationName) throws ApplicationException {

        try {
            return applicationBusiness.getCitation(applicationName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<AppVersion> getVersions(String applicationName) throws ApplicationException {

        try {
            return applicationBusiness.getVersions(applicationName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param engine
     * @throws ApplicationException
     */
    @Override
    public void addEngine(Engine engine) throws ApplicationException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding engine '" + engine.getName() + "'.");
            engineBusiness.add(engine);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param engine
     * @throws ApplicationException
     */
    @Override
    public void updateEngine(Engine engine) throws ApplicationException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating engine '" + engine.getName() + "'.");
            engineBusiness.update(engine);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param engineName
     * @throws ApplicationException
     */
    @Override
    public void removeEngine(String engineName) throws ApplicationException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing engine '" + engineName + "'.");
            engineBusiness.remove(engineName);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @return @throws ApplicationException
     */
    @Override
    public List<Engine> getEngines() throws ApplicationException {

        try {
            authenticateSystemAdministrator(logger);
            return engineBusiness.get();

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public List<String> getAppletGateLabClasses() throws ApplicationException {
        return Server.getInstance().getAppletGateLabClasses();

    }

    @Override
    public AppVersion getVersion(String applicationName, String applicationVersion) throws ApplicationException {
        try {
            return applicationBusiness.getVersion(applicationName, applicationVersion);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
}
