package fr.insalyon.creatis.vip.application.server.rpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.ApplicationStatus;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Pair;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletException;

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
    private GroupBusiness groupBusiness;

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
                getBean(AppVersionBusiness.class),
                getBean(GroupBusiness.class)
        );
    }

    public void setBeans(
            ApplicationBusiness applicationBusiness, EngineBusiness engineBusiness,
            BoutiquesBusiness boutiquesBusiness, ConfigurationBusiness configurationBusiness,
            WorkflowBusiness workflowBusiness, SimulationBusiness simulationBusiness, 
            ResourceBusiness resourceBusiness, TagBusiness tagBusiness,
            AppVersionBusiness appVersionBusiness, GroupBusiness groupBusiness) {
        this.applicationBusiness = applicationBusiness;
        this.engineBusiness = engineBusiness;
        this.boutiquesBusiness = boutiquesBusiness;
        this.configurationBusiness = configurationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.simulationBusiness = simulationBusiness;
        this.resourceBusiness = resourceBusiness;
        this.tagBusiness = tagBusiness;
        this.appVersionBusiness = appVersionBusiness;
        this.groupBusiness = groupBusiness;
    }

    @Override
    public String add(Application application) throws VipException {
        if (isSystemAdministrator() || isGroupAdministrator() || isDeveloper()) {
            trace(logger, "Adding application '" + application.getName() + "'.");
            application.setOwner(getSessionUser().getEmail());
            applicationBusiness.add(application);

            return groupBusiness.getWarningSameVisibility(application.getGroupsNames());
        } else {
            logger.error("Unauthorized to add application {}", application.getName());
            throw new VipException("You have no administrator rights.");
        }
    }

    @Override
    public String update(Application application) throws VipException {
        if (isSystemAdministrator() || isGroupAdministrator() || isDeveloper()) {
            trace(logger, "Updating application '" + application.getName() + "'.");
            applicationBusiness.update(application);

            return groupBusiness.getWarningSameVisibility(application.getGroupsNames());
        } else {
            logger.error("Unauthorized to update application {}", application.getName());
            throw new VipException("You have no administrator rights.");
        }
    }

    @Override
    public String remove(String name) throws VipException {
        if (isSystemAdministrator()) {
            trace(logger, "Removing application '" + name + "'.");
            applicationBusiness.remove(name);

        }
        return null;
    }

    @Override
    public void addVersion(AppVersion version) throws VipException {
        if (isSystemAdministrator() || isGroupAdministrator()) {
            trace(logger, "Adding version '" + version.getVersion() + "' ('" + version.getApplicationName() + "').");
            appVersionBusiness.add(version);
        } else {
            logger.error("Unauthorized to add version {} to {}",
                    version.getVersion(), version.getApplicationName());
            throw new VipException("You have no administrator rights.");
        }
    }

    @Override
    public void updateVersion(AppVersion version) throws VipException {
        if (isSystemAdministrator() || isGroupAdministrator()) {
            trace(logger, "Updating version '" + version.getVersion() + "' ('" + version.getApplicationName() + "').");
            appVersionBusiness.update(version);
        } else {
            logger.error("Unauthorized to update version {}/{}",
                    version.getApplicationName(), version.getVersion());
            throw new VipException("You have no administrator rights.");
        }
    }

    @Override
    public void removeVersion(String applicationName, String version) throws VipException {
        if (isSystemAdministrator() || isGroupAdministrator()) {
            trace(logger, "Removing application '" + applicationName + "'.");
            appVersionBusiness.remove(applicationName, version);

        } else {
            logger.error("Unauthorized to remove version {}/{}",
                    applicationName, version);
            throw new VipException("You have no administrator rights.");
        }
    }

    @Override
    public String publishVersion(String applicationName, String version) throws VipException {
        if (isSystemAdministrator() || isGroupAdministrator()) {
            trace(logger, "Publishing version " + version + "' ('" + applicationName + "').");
            return boutiquesBusiness.publishVersion(getSessionUser(), applicationName, version);

        } else {
            logger.error("Unauthorized to publish version {}/{}",
                    applicationName, version);
            throw new VipException("You have no administrator rights.");
        }
    }

    @Override
    public List<Application> getPublicApplications() throws VipException {
        return applicationBusiness.getPublicApplications();
    }

    @Override
    public Map<Application, List<AppVersion>> getApplications() throws VipException {
        List<Application> apps = new ArrayList<>();
        Map<Application, List<AppVersion>> map = new LinkedHashMap<>();

        if (isSystemAdministrator()) {
            apps = applicationBusiness.getApplications();
        } else {
            apps = applicationBusiness.getApplications(getSessionUser());
        }
        for (Application app : apps) {
            map.put(app, appVersionBusiness.getVersions(app.getName()));
        }
        return map;
    }

    @Override
    public Map<Application, Set<Resource>> getManageableApplications() throws VipException {
        List<Application> apps = new ArrayList<>();
        Map<Application, Set<Resource>> map = new LinkedHashMap<>();

        if (isSystemAdministrator()) {
            apps = applicationBusiness.getApplications();
        } else if (isDeveloper()) {
            apps = applicationBusiness.getApplicationsWithOwner(getSessionUser().getEmail());
        } else {
            logger.error("Unauthorized to get manageable applications for regular user");
            throw new VipException("You have no administrator rights.");
        }

        for (Application app : apps) {
            map.put(app, appVersionBusiness.getVersions(app.getName()).stream()
                    .flatMap(version -> version.getResources().stream()).collect(Collectors.toSet()));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String>[] getApplicationsAndUsers() throws VipException {
        User user = getSessionUser();
        if (isSystemAdministrator()) {
            return new List[] {
                    configurationBusiness.getAllUserNames(),
                    applicationBusiness.getApplicationNames(),
            };
        } else {
            ;
            return new List[] {
                    new ArrayList<>(Arrays.asList(user.getFullName())),
                    applicationBusiness.getApplicationNames()
            };
        }
    }

    @Override
    public ApplicationStatus getApplicationStatus() throws VipException {
        List<Simulation> runningSimulations = workflowBusiness.getRunningSimulations();

        ApplicationStatus status = new ApplicationStatus();
        status.setRunningWorkflows(runningSimulations.size());

        int[] tasks = simulationBusiness.getNumberOfActiveTasks(runningSimulations);
        status.setRunningTasks(tasks[0]);
        status.setWaitingTasks(tasks[1]);

        return status;
    }

    @Override
    public String getCitation(String applicationName) throws VipException {
        // I think this is meant to nullify empty citation like "  <br /> "
        String citation = applicationBusiness.getCitation(applicationName);
        String citationWithoutHtml = Jsoup.parse(citation).text();
        if (citationWithoutHtml.isEmpty()) {
            return null;
        } else {
            return citation;
        }
    }

    @Override
    public List<AppVersion> getVersions(String applicationName) throws VipException {
        return appVersionBusiness.getVersions(applicationName);
    }

    @Override
    public void addEngine(Engine engine) throws VipException {
        authenticateSystemAdministrator(logger);
        trace(logger, "Adding engine '" + engine.getName() + "'.");
        engineBusiness.add(engine);
    }

    @Override
    public void updateEngine(Engine engine) throws VipException {
        authenticateSystemAdministrator(logger);
        trace(logger, "Updating engine '" + engine.getName() + "'.");
        engineBusiness.update(engine);
    }

    @Override
    public void removeEngine(String engineName) throws VipException {
        authenticateSystemAdministrator(logger);
        trace(logger, "Removing engine '" + engineName + "'.");
        engineBusiness.remove(engineName);
    }

    @Override
    public List<Engine> getEngines() throws VipException {
        authenticateSystemAdministrator(logger);
        return engineBusiness.get();
    }

    @Override
    public AppVersion getVersion(String applicationName, String applicationVersion) throws VipException {
        return appVersionBusiness.getVersion(applicationName, applicationVersion);
    }

    @Override
    public String addResource(Resource resource) throws VipException {
        resourceBusiness.add(resource);
    
        return groupBusiness.getWarningSameVisibility(resource.getGroupsNames());
    }

    @Override
    public String removeResource(Resource resource) throws VipException {
        resourceBusiness.remove(resource);

        return null;
    }

    @Override
    public String updateResource(Resource resource) throws VipException {
        resourceBusiness.update(resource);

        return groupBusiness.getWarningSameVisibility(resource.getGroupsNames());
    }

    @Override
    public List<Resource> getResources() throws VipException {
        return resourceBusiness.getAll();
    }

    @Override
    public void addTag(Tag tag) throws VipException {
        tagBusiness.add(tag);
    }

    @Override
    public void removeTag(Tag tag) throws VipException {
        tagBusiness.remove(tag);
    }

    @Override
    public void updateTag(Tag oldTag, Tag newTag) throws VipException {
        tagBusiness.update(oldTag, newTag);
    }

    @Override
    public List<Tag> getTags() throws VipException {
        return tagBusiness.getAll();
    }

    public List<Tag> getNonBoutiquesTags() throws VipException {
        return tagBusiness.getAll().stream()
                .filter(tag -> !tag.isBoutiques())
                .collect(Collectors.toMap(
                        Tag::toString,
                        tag -> tag,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new))
                .values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Tag> getTags(AppVersion appVersion) throws VipException {
        return tagBusiness.getTags(appVersion);
    }

    /**
     * This fonction will check if ressources/engines are available !
     */
    @Override
    public Pair<Boolean, String> isAppUsableWithCurrentUser(String appName, String version) throws VipException {
        AppVersion appVersion = appVersionBusiness.getVersion(appName, version);
        List<Resource> usableResource = resourceBusiness.getUsableResources(getSessionUser(), appVersion);
        List<Engine> usableEngines;

        if (usableResource.isEmpty()) {
            return new Pair<Boolean, String>(false,
                    "Sorry, there are no ressources actually availables for this application!");
        }

        usableEngines = engineBusiness.getUsableEngines(usableResource.get(0));
        if (usableEngines.isEmpty()) {
            return new Pair<Boolean, String>(false,
                    "Sorry, there are no engines actually availables for this application!");
        }
        return new Pair<Boolean, String>(true, "Application usable!");
    }
}
