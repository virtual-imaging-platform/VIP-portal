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
package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationServiceImpl extends AbstractRemoteServiceServlet implements ApplicationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationBusiness applicationBusiness;
    private AppVersionBusiness appVersionBusiness;
    private EngineBusiness engineBusiness;
    private BoutiquesBusiness boutiquesBusiness;
    private ConfigurationBusiness configurationBusiness;
    private WorkflowBusiness workflowBusiness;
    private SimulationBusiness simulationBusiness;
    private ResourceBusiness resourceBusiness;
    private TagBusiness tagBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        setBeans(
                getBean(ApplicationBusiness.class),
                getBean(EngineBusiness.class),
                getBean(BoutiquesBusiness.class),
                getBean(ConfigurationBusiness.class),
                getBean(WorkflowBusiness.class),
                getBean(SimulationBusiness.class),
                getBean(ResourceBusiness.class),
                getBean(TagBusiness.class),
                getBean(AppVersionBusiness.class)
        );
    }

    public void setBeans(
            ApplicationBusiness applicationBusiness, EngineBusiness engineBusiness,
            BoutiquesBusiness boutiquesBusiness, ConfigurationBusiness configurationBusiness,
            WorkflowBusiness workflowBusiness, SimulationBusiness simulationBusiness, 
            ResourceBusiness resourceBusiness, TagBusiness tagBusiness,
            AppVersionBusiness appVersionBusiness) {
        this.applicationBusiness = applicationBusiness;
        this.engineBusiness = engineBusiness;
        this.boutiquesBusiness = boutiquesBusiness;
        this.configurationBusiness = configurationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.simulationBusiness = simulationBusiness;
        this.resourceBusiness = resourceBusiness;
        this.tagBusiness = tagBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    @Override
    public void add(Application application) throws ApplicationException {

        try {
            if (isSystemAdministrator() || isGroupAdministrator() || isDeveloper()) {
                trace(logger, "Adding application '" + application.getName() + "'.");
                application.setOwner(getSessionUser().getEmail());
                applicationBusiness.add(application);
            } else {
                logger.error("Unauthorized to add application {}", application.getName());
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void update(Application application) throws ApplicationException {
        try {
            if (isSystemAdministrator() || isGroupAdministrator() || isDeveloper()) {
                trace(logger, "Updating application '" + application.getName() + "'.");
                applicationBusiness.update(application);

            } else {
                logger.error("Unauthorized to update application {}", application.getName());
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void remove(String name) throws ApplicationException {
        try {
            if (isSystemAdministrator()) {
                trace(logger, "Removing application '" + name + "'.");
                applicationBusiness.remove(name);

            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void addVersion(AppVersion version) throws ApplicationException {
        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Adding version '" + version.getVersion() + "' ('" + version.getApplicationName() + "').");
                appVersionBusiness.add(version);
            } else {
                logger.error("Unauthorized to add version {} to {}",
                        version.getVersion(), version.getApplicationName());
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void updateVersion(AppVersion version) throws ApplicationException {
        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Updating version '" + version.getVersion() + "' ('" + version.getApplicationName() + "').");
                appVersionBusiness.update(version);
            } else {
                logger.error("Unauthorized to update version {}/{}",
                        version.getApplicationName(), version.getVersion());
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void removeVersion(String applicationName, String version) throws ApplicationException {
        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Removing application '" + applicationName + "'.");
                appVersionBusiness.remove(applicationName, version);

            } else {
                logger.error("Unauthorized to remove version {}/{}",
                        applicationName, version);
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public String publishVersion(String applicationName, String version) throws ApplicationException {
        try {
            if (isSystemAdministrator() || isGroupAdministrator()) {
                trace(logger, "Publishing version " + version + "' ('" + applicationName + "').");
                return boutiquesBusiness.publishVersion(getSessionUser(), applicationName, version);

            } else {
                logger.error("Unauthorized to publish version {}/{}",
                        applicationName, version);
                throw new ApplicationException("You have no administrator rights.");
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public Map<Application, List<AppVersion>> getPublicApplications() throws ApplicationException {
        List<Application> apps = new ArrayList<>();
        Map<Application, List<AppVersion>> map = new LinkedHashMap<>();

        try {
            apps = applicationBusiness.getPublicApplicationsWithGroups();

            for (Application app : apps) {
                map.put(app, appVersionBusiness.getVersions(app.getName()));
            }
            return map;
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public Map<Application, List<AppVersion>> getApplications() throws ApplicationException {
        List<Application> apps = new ArrayList<>();
        Map<Application, List<AppVersion>> map = new LinkedHashMap<>();

        try {
            if (isSystemAdministrator()) {
                apps = applicationBusiness.getApplications();
            } else if (isDeveloper()) {
                apps = applicationBusiness.getApplicationsWithOwner(getSessionUser().getEmail());
            } else {
                apps = applicationBusiness.getApplications(getSessionUser());
            }
            for (Application app : apps) {
                map.put(app, appVersionBusiness.getVersions(app.getName()));
            }
            return map;
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String>[] getApplicationsAndUsers() throws ApplicationException {
        try {
            User user = getSessionUser();
            if (isSystemAdministrator()) {
                return new List[]{
                    configurationBusiness.getAllUserNames(),
                    applicationBusiness.getApplicationNames(),
                };
            } else {;
                return new List[] {
                    new ArrayList<>(Arrays.asList(user.getFullName())),
                    applicationBusiness.getApplicationNames()
                };
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public ApplicationStatus getApplicationStatus() throws ApplicationException {

        try {
            List<Simulation> runningSimulations = workflowBusiness.getRunningSimulations();

            ApplicationStatus status = new ApplicationStatus();
            status.setRunningWorkflows(runningSimulations.size());

            int[] tasks = simulationBusiness.getNumberOfActiveTasks(runningSimulations);
            status.setRunningTasks(tasks[0]);
            status.setWaitingTasks(tasks[1]);

            return status;

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public String getCitation(String applicationName) throws ApplicationException {
        // I think this is meant to nullify empty citation like "  <br /> "
        try {
            String citation = applicationBusiness.getCitation(applicationName);
            String citationWithoutHtml = Jsoup.parse(citation).text();
            if (citationWithoutHtml.isEmpty()) {
                return null;
            } else {
                return citation;
            }
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public List<AppVersion> getVersions(String applicationName) throws ApplicationException {
        try {
            return appVersionBusiness.getVersions(applicationName);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void addEngine(Engine engine) throws ApplicationException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding engine '" + engine.getName() + "'.");
            engineBusiness.add(engine);
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void updateEngine(Engine engine) throws ApplicationException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating engine '" + engine.getName() + "'.");
            engineBusiness.update(engine);
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void removeEngine(String engineName) throws ApplicationException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing engine '" + engineName + "'.");
            engineBusiness.remove(engineName);
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public List<Engine> getEngines() throws ApplicationException {
        try {
            authenticateSystemAdministrator(logger);
            return engineBusiness.get();
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public AppVersion getVersion(String applicationName, String applicationVersion) throws ApplicationException {
        try {
            return appVersionBusiness.getVersion(applicationName, applicationVersion);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void addResource(Resource resource) throws ApplicationException {
        try {
            resourceBusiness.add(resource);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void removeResource(Resource resource) throws ApplicationException {
        try {
            resourceBusiness.remove(resource);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void updateResource(Resource resource) throws ApplicationException {
        try {
            resourceBusiness.update(resource);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public List<Resource> getResources() throws ApplicationException {
        try {
            return resourceBusiness.getAll();
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void addTag(Tag tag) throws ApplicationException {
        try {
            tagBusiness.add(tag);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void removeTag(Tag tag) throws ApplicationException {
        try {
            tagBusiness.remove(tag);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void updateTag(Tag oldTag, Tag newTag) throws ApplicationException {
        try {
            tagBusiness.update(oldTag, newTag);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public List<Tag> getTags() throws ApplicationException {
        try {
            return tagBusiness.getAll();
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    public List<Tag> getNonBoutiquesTags() throws ApplicationException {
        try {
            return tagBusiness.getAll().stream()
                .filter(tag -> ! tag.isBoutiques())
                .collect(Collectors.toMap(
                    Tag::toString,
                    tag -> tag,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                )).values().stream()
                .collect(Collectors.toList());
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }  
    }

    @Override
    public List<Tag> getTags(AppVersion appVersion) throws ApplicationException {
        try {
            return tagBusiness.getTags(appVersion);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * This fonction will check if ressources/engines are available !
     */
    @Override
    public Boolean isAppUsableWithCurrentUser(String appName, String version) throws ApplicationException {
        try {
            AppVersion appVersion = appVersionBusiness.getVersion(appName, version);
            List<Resource> usableResource = resourceBusiness.getUsableResources(getSessionUser(), appVersion);
            List<Engine> usableEngines;

            if (usableResource.isEmpty()) {
                return false;
            }

            usableEngines = engineBusiness.getUsableEngines(usableResource.get(0));
            if (usableEngines.isEmpty()) {
                return false;
            }
            
            return true;
        } catch (BusinessException | CoreException e) {
            throw new ApplicationException(e);
        }
    }
}
