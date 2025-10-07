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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.business.PageBuilder;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public void add(Application app) throws BusinessException {
        permissions.checkLevel(UserLevel.Administrator, UserLevel.Developer);
        // this check is supposed to happend rarely since 
        // we recommand associating groups after application creation (using update)
        permissions.checkOnlyUserPrivateGroups(app.getGroups());
        try {
            applicationDAO.add(app);

            for (String groupName : app.getGroupsNames()) {
                associate(app, new Group(groupName));
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    @VIPExternalSafe
    public void remove(String name) throws BusinessException {
        permissions.checkLevel(UserLevel.Administrator, UserLevel.Developer);

        Application app = getApplication(name); // not safe, do not return to user! 
        if (app != null) {
            permissions.checkOnlyUserPrivateGroups(app.getGroups());
        }
        try {
            logger.trace("Removing application: {}", name);
            applicationDAO.remove(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    @VIPExternalSafe
    public void update(Application app) throws BusinessException {
        Application existingApp = getApplication(app.getName()); // not safe, do not return to user!

        permissions.checkLevel(UserLevel.Administrator, UserLevel.Developer);
        // check actual app groups to determine if editable
        permissions.checkOnlyUserPrivateGroups(existingApp.getGroups());
        // check edited apps to verify added groups
        permissions.checkOnlyUserPrivateGroups(app.getGroups());
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
            throw new BusinessException(ex);
        }
    }

    @VIPExternalSafe
    public Application get(String name) throws BusinessException {
        List<Application> apps = getUserContextApplications();

        return apps.stream().filter((app) -> name.equals(app.getName()))
            .findFirst().orElse(null);
    }

    @VIPExternalSafe
    public PrecisePage<Application> get(int offset, int quantity, String group) throws BusinessException {
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
    public List<Application> getUserContextApplications() throws BusinessException {
        User user = userSupplier.get();

        if (user.isSystemAdministrator()) {
            return getApplications();
        } else {
            return getApplications(user);
        }
    }

    public Application getApplication(String applicationName) throws BusinessException {
        try {
            return mapGroups(applicationDAO.getApplication(applicationName));
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Application> getApplications() throws BusinessException {
        try {
            return mapGroups(applicationDAO.getApplications());
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    @VIPExternalSafe
    public List<Application> getApplications(User user) throws BusinessException {
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

    public List<Application> getApplications(Group group) throws BusinessException {
        try {
            return mapGroups(applicationDAO.getApplicationsByGroup(group));
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Application> getApplicationsWithOwner(String email) throws BusinessException {
        try {
            return mapGroups(applicationDAO.getApplicationsWithOwner(email));
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Application> getPublicApplications() throws BusinessException {
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

    public List<String> getApplicationNames() throws BusinessException {
        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications()) {
            applicationNames.add(application.getName());
        }
        return applicationNames;
    }

    public String getCitation(String name) throws BusinessException {
        try {
            return applicationDAO.getCitation(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void associate(Application app, Group group) throws BusinessException {
        app = getApplication(app.getName());
        group = groupBusiness.get(group.getName());

        try {
            applicationDAO.associate(app, group);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void dissociate(Application app, Group group) throws BusinessException {
        try {
            applicationDAO.dissociate(app, group);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    // this function need to only map groups that user can see
    // otherwise it will lead to permission leak issues
    private Application mapGroups(Application app) throws BusinessException {
        List<Group> appGroups;

        if (app == null) {
            return null;
        } else {
            appGroups = groupBusiness.getByApplication(app.getName());

            app.setGroups(permissions.filterOnlyUserGroups(appGroups));
            return app;
        }
    }

    private List<Application> mapGroups(List<Application> apps) throws BusinessException {
        for (Application app : apps) {
            mapGroups(app);
        }
        return apps;
    }
}
