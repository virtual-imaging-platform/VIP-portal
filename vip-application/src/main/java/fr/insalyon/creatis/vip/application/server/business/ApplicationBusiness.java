package fr.insalyon.creatis.vip.application.server.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.business.PageBuilder;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

@Service
@Transactional
public class ApplicationBusiness extends CommonBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationDAO applicationDAO;
    private GroupBusiness groupBusiness;
    private AppVersionBusiness appVersionBusiness;
    private PageBuilder pageBuilder;

    @Autowired
    public ApplicationBusiness(ApplicationDAO applicationDAO, GroupBusiness groupBusiness,
            AppVersionBusiness appVersionBusiness, PageBuilder pageBuilder) {
        this.applicationDAO = applicationDAO;
        this.groupBusiness = groupBusiness;
        this.appVersionBusiness = appVersionBusiness;
        this.pageBuilder = pageBuilder;
    }

    @VIPExternalSafe
    public void add(Application app) throws VipException {
        permissions.checkLevel(UserLevel.Administrator, UserLevel.Developer);

        if (userSupplier.get().getLevel().equals(UserLevel.Developer)) {
            // developers can only assign from private groups they belong to
            // at application creation
            permissions.checkOnlyUserPrivateGroups(app.getGroups());
        }
        try {
            applicationDAO.add(app);

            for (String groupName : app.getGroupsNames()) {
                associate(app, new Group(groupName));
            }
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public void remove(String name) throws VipException {
        Application app = getApplication(name); // not safe, do not return to user!

        if (app == null) {
            return;
        }
        permissions.checkLevel(UserLevel.Administrator, UserLevel.Developer);

        if (userSupplier.get().getLevel().equals(UserLevel.Developer)) {
            // this is related to developers
            // they can only remove application from private groups they belong to
            permissions.checkItemInList(app, getUserContextApplications());
            permissions.checkOnlyUserPrivateGroups(app.getGroups());
        }
        try {
            logger.trace("Removing application: {}", name);
            applicationDAO.remove(name);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public void update(Application app) throws VipException {
        Application existingApp = getApplication(app.getName()); // not safe, do not return to user!

        permissions.checkLevel(UserLevel.Administrator, UserLevel.Developer);

        if (userSupplier.get().getLevel().equals(UserLevel.Developer)) {
            // developer can only associate group at CREATION
            permissions.checkUnchanged(app.getGroups(), existingApp.getGroups());
            // edition only on application from privates groups
            permissions.checkOnlyUserPrivateGroups(existingApp.getGroups());
        }
        try {
            Application before = getApplication(app.getName());
            List<String> beforeGroupsNames = before.getGroupsNames();

            applicationDAO.update(app);
            for (String group : app.getGroupsNames()) {
                if (!beforeGroupsNames.removeIf((s) -> s.equals(group))) {
                    associate(app, new Group(group));
                }
            }
            for (String group : beforeGroupsNames) {
                dissociate(app, new Group(group));
            }
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public Application get(String name) throws VipException {
        List<Application> apps = getUserContextApplications();

        return apps.stream().filter((app) -> name.equals(app.getName()))
            .findFirst().orElse(null);
    }

    @VIPExternalSafe
    public PrecisePage<Application> get(int offset, int quantity, String group) throws VipException {
        List<Application> apps;

        // permissions
        apps = getUserContextApplications();

        // filter
        if (group != null) {
            apps = apps.stream().filter((app) -> app.getGroupsNames().contains(group)).toList();
        }

        // pagination
        return pageBuilder.doPrecise(offset, quantity, apps);
    }

    @VIPExternalSafe
    public List<Application> getUserContextApplications() throws VipException {
        User user = userSupplier.get();

        if (user.isSystemAdministrator()) {
            return getApplications();
        } else {
            return getApplications(user);
        }
    }

    public Application getApplication(String applicationName) throws VipException {
        try {
            return mapGroups(applicationDAO.getApplication(applicationName));
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Application> getApplications() throws VipException {
        try {
            return mapGroups(applicationDAO.getApplications());
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public List<Application> getApplications(User user) throws VipException {
        // this perform permissions filtering based on user group's membership
        // if you perform this function as an Admin you will only get
        // applications of groups you belong to.
        // notes: use getApplications() to retrieve everything in DB
        List<Group> userGroups = configurationBusiness.getUserGroups(user.getEmail()).keySet()
            .stream()
            .filter((g) -> g.getType().equals(GroupType.APPLICATION))
            .collect(Collectors.toList());
        List<Application> result = new ArrayList<>();
        Set<String> appNames = new HashSet<>();

        for (Group group : userGroups) {
            for (Application app : getApplications(group)) {
                if (appNames.add(app.getName())) {
                    result.add(app);
                }
            }
        }
        return result;
    }

    public List<Application> getApplications(Group group) throws VipException {
        try {
            return mapGroups(applicationDAO.getApplicationsByGroup(group));
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Application> getApplicationsWithOwner(String email) throws VipException {
        try {
            return mapGroups(applicationDAO.getApplicationsWithOwner(email));
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
            for (Application app : getApplications(group)) {
                // keep application if at least a Version is visible
                if (appVersionBusiness.getVersions(app.getName()).stream().anyMatch(AppVersion::isVisible)) {
                    apps.add(app);
                }
            }
        }

        // remove doublons + sort
        return apps.stream().collect(Collectors.toMap(Application::getName, a -> a, (a1, a2) -> a1)).values()
                .stream().sorted(Comparator.comparing(Application::getName)).collect(Collectors.toList());
    }

    public List<String> getApplicationNames() throws VipException {
        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications()) {
            applicationNames.add(application.getName());
        }
        return applicationNames;
    }

    public String getCitation(String name) throws VipException {
        try {
            return applicationDAO.getCitation(name);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void associate(Application app, Group group) throws VipException {
        app = getApplication(app.getName());
        group = groupBusiness.get(group.getName());

        try {
            applicationDAO.associate(app, group);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void dissociate(Application app, Group group) throws VipException {
        try {
            applicationDAO.dissociate(app, group);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    // this function need to only map groups that user can see
    // otherwise it will lead to permission leak issues
    private Application mapGroups(Application app) throws VipException {
        List<Group> appGroups;

        if (app == null) {
            return null;
        } else {
            appGroups = groupBusiness.getByApplication(app.getName());

            app.setGroups(permissions.filterOnlyUserGroups(appGroups));
            return app;
        }
    }

    private List<Application> mapGroups(List<Application> apps) throws VipException {
        for (Application app : apps) {
            mapGroups(app);
        }
        return apps;
    }
}
