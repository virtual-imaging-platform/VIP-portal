package fr.insalyon.creatis.vip.application.server.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

@Service
@Transactional
public class AppVersionBusiness extends CommonBusiness {

    private final TagBusiness tagBusiness;
    private final ResourceBusiness resourceBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final ApplicationDAO applicationDAO;
    private final GroupBusiness groupBusiness;
    private final WorkflowDAO workflowDAO;

    @Autowired
    public AppVersionBusiness(TagBusiness tagBusiness, ResourceBusiness resourceBusiness, ApplicationDAO applicationDAO, ApplicationBusiness applicationBusiness, GroupBusiness groupBusiness, WorkflowDAO workflowDAO) {
        this.tagBusiness = tagBusiness;
        this.resourceBusiness = resourceBusiness;
        this.applicationBusiness = applicationBusiness;
        this.applicationDAO = applicationDAO;
        this.groupBusiness = groupBusiness;
        this.workflowDAO = workflowDAO;
    }

    @VIPExternalSafe
    public void add(AppVersion version) throws VipException {
        Application app = permissions.shouldExist(
            applicationBusiness.get(version.getApplicationName()),
            Application.class,
            version.getApplicationName());
        permissions.filter((chain) -> chain
            .admin()
            .developer(() -> {
                List<Resource> userResources = resourceBusiness.getUserContextResources();

                for (Resource wantedResource : version.getResources()) {
                    permissions.checkItemInList(wantedResource, userResources);
                }
                permissions.checkOnlyUserPrivateGroups(app.getGroups());
        }));
        try {
            applicationDAO.addVersion(version);

            for (Tag tag : version.getTags()) {
                tag.setApplication(version.getApplicationName());
                tag.setVersion(version.getVersion());
                tagBusiness.addOrUpdate(tag);
            }
            for (Resource resource : version.getResources()) {
                resourceBusiness.associate(resource, version);
            }
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    @VIPExternalSafe
    public void update(AppVersion version) throws VipException {
        AppVersion existingVersion = permissions.shouldExist(
            get(version.getApplicationName(), version.getVersion()),
            AppVersion.class,
            version.getVersion());
        Application app = applicationBusiness.get(version.getApplicationName());

        permissions.filter((chain) -> chain
            .admin()
            .developer(() -> {
                // developer can only associate resources at CREATION (on private apps)
                permissions.checkUnchanged(version.getResources(), existingVersion.getResources());
                permissions.checkOnlyUserPrivateGroups(app.getGroups());
        }));
        try {
            List<String> beforeResourceNames = existingVersion.getResourcesNames();
            Set<Tag> editedTags = existingVersion.getTags();
            editedTags.removeAll(version.getTags());

            applicationDAO.updateVersion(version);
            for (Resource resource : version.getResources()) {
                if ( ! beforeResourceNames.removeIf((s) -> s.equals(resource.getName()))) {
                    resourceBusiness.associate(resource, version);
                }
            }
            for (Tag tag : editedTags) {
                tagBusiness.remove(tag);
            }
            for (Tag tag : version.getTags()) {
                tagBusiness.addOrUpdate(tag);
            }
            for (String resource : beforeResourceNames) {
                resourceBusiness.dissociate(new Resource(resource), version);
            }

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public void remove(String applicationName, String version) throws VipException {
        Application app = permissions.shouldExist(
            applicationBusiness.getApplication(applicationName),
            Application.class,
            applicationName);
        AppVersion appVersion = get(applicationName, version);

        if (appVersion == null) return;
        permissions.filter((chain) -> chain
            .admin()
            .developer(() -> {
                // same rule than for Application
                permissions.checkItemInList(app, applicationBusiness.getUserContextApplications());
                permissions.checkOnlyUserPrivateGroups(app.getGroups());
        }));
        try {
            applicationDAO.removeVersion(applicationName, version);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updateDoiForVersion(String doi, String applicationName, String version) throws VipException {
        try {
            applicationDAO.updateDoiForVersion(doi, applicationName, version);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<AppVersion> getVersions(String applicationName) throws VipException {
        try {
            List<AppVersion> versions = applicationDAO.getVersions(applicationName);

            for (AppVersion version : versions) {
                version.setResources(new HashSet<>(resourceBusiness.getByAppVersion(version)));
                version.setTags(new HashSet<>(tagBusiness.getTags(version)));
            }
            return versions;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Application> getPublicApplications() throws VipException {
        List<Group> publicAppGroups = groupBusiness.getPublic()
            .stream()
            .filter((g) -> g.getType().equals(GroupType.APPLICATION))
            .collect(Collectors.toList());
        List<Application> apps = new ArrayList<>();

        for (Group group : publicAppGroups) {
            for (Application app : applicationBusiness.getApplications(group)) {
                // keep application if at least a Version is visible
                if (getVersions(app.getName()).stream().anyMatch(AppVersion::isVisible)) {
                    apps.add(app);
                }
            }
        }

        Map<String, Long> popularity = getApplicationsPopularity();

        // remove doublons + sort by popularity desc and then name asc
        return apps.stream().collect(Collectors.toMap(Application::getName, a -> a, (a1, a2) -> a1)).values()
                .stream().sorted(Comparator.comparingLong((Application a) -> popularity.getOrDefault(a.getName(), 0L)).reversed()
                        .thenComparing(Application::getName)).collect(Collectors.toList());
    }

    private Map<String, Long> getApplicationsPopularity() throws VipException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            Date oneYearAgo = cal.getTime();

            List<fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow> workflows =
                    workflowDAO.get(null, null, null, null, oneYearAgo, new Date(), null);

            // group by application name and count distinct users to produce a popularity score for each application
            return workflows.stream()
                    .collect(Collectors.groupingBy(
                            fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow::getApplication,
                            Collectors.mapping(
                                    fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow::getUsername,
                                    Collectors.toSet())))
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> (long) e.getValue().size()));
        } catch (WorkflowsDBDAOException ex) {
            throw new VipException(ex);
        }
    }

    public AppVersion getVersion(String applicationName, String applicationVersion) throws VipException {
        try {
            AppVersion version = applicationDAO.getVersion(applicationName, applicationVersion);

            if (version != null) {
                version.setResources(new HashSet<>(resourceBusiness.getByAppVersion(version)));
                version.setTags(new HashSet<>(tagBusiness.getTags(version)));
            }

            return version;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public AppVersion get(String application, String version) throws VipException {
        try {
            Application app = permissions.shouldExist(
                applicationBusiness.get(application),
                Application.class,
                application);
            AppVersion appVersion = applicationDAO.getVersion(app.getName(), version);

            if (appVersion != null) {
                // to avoid permissions leaks
                appVersion.setResources(new HashSet<>(permissions.filterOnlySame(
                        resourceBusiness.getByAppVersion(appVersion),
                        resourceBusiness.getUserContextResources())));
                appVersion.setTags(new HashSet<>(tagBusiness.getTags(appVersion)));
            }

            return appVersion;
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    @VIPExternalSafe
    public PrecisePage<AppVersion> get(int offset, int quantity, String application) throws VipException {
        try {
            List<AppVersion> versions = new ArrayList<>();

            if (application == null) {
                for (Application app : applicationBusiness.getUserContextApplications()) {
                    versions.addAll(applicationDAO.getVersions(app.getName()));
                }
            } else {
                Application app = permissions.shouldExist(
                    applicationBusiness.get(application),
                    Application.class,
                    application);
                versions = applicationDAO.getVersions(app.getName());
            }

            List<Resource> userResources = resourceBusiness.getUserContextResources();

            for (AppVersion v : versions) {
                // to avoid permissions leaks
                v.setResources(new HashSet<>(permissions.filterOnlySame(
                        resourceBusiness.getByAppVersion(v),
                        userResources)));
                v.setTags(new HashSet<>(tagBusiness.getTags(v)));
            }

            return pageBuilder.doPrecise(offset, quantity, new ArrayList<>(versions));
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }
}


