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
import fr.insalyon.creatis.vip.application.server.dao.ResourceDAO;
import fr.insalyon.creatis.vip.application.server.dao.TagDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Transactional
public class ApplicationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationDAO applicationDAO;
    private GroupDAO groupDAO;

    @Autowired
    public ApplicationBusiness(ApplicationDAO applicationDAO, GroupDAO groupDAO, TagDAO tagDAO, ResourceDAO resourceDAO) {
        this.applicationDAO = applicationDAO;
        this.groupDAO = groupDAO;
    }

    public void add(Application application) throws BusinessException {
        try {
            for (String groupName : application.getApplicationGroups()) {
                associate(application, new Group(groupName));
            }
            applicationDAO.add(application);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void remove(String name) throws BusinessException {
        try {
            applicationDAO.remove(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void update(Application application) throws BusinessException {
        try {
            Application before = getApplication(application.getName());
            List<String> beforeGroupsNames = before.getApplicationGroups();

            applicationDAO.update(application);
            for (String group : application.getApplicationGroups()) {
                if ( ! beforeGroupsNames.removeIf((s) -> s.equals(group))) {
                    associate(application, new Group(group));
                }
            }
            for (String group : beforeGroupsNames) {
                dissociate(application, new Group(group));
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
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

    public List<Application> getPublicApplicationsWithGroups() throws BusinessException {
        try {
            Map<String, Group> allGroups = groupDAO.get().stream().collect(Collectors.toMap(
                    Group::getName, group -> group));
            Set<String> allVisibleApplicationNames = applicationDAO.getAllVisibleVersions().stream()
                    .map(AppVersion::getApplicationName).collect(Collectors.toSet());
            List<Application> allApplications = getApplications().stream()
                    .filter(app -> allVisibleApplicationNames.contains(app.getName()))
                    .collect(Collectors.toList());

            List<Application> applicationsWithGroups = new ArrayList<>();
            // changer
            //
            // for (Application application : allApplications) {
            //     Set<String> currentAppPublicGroups = application.getApplicationClasses().stream()
            //             .map(className -> allClasses.get(className).getGroups())
            //             .flatMap(List::stream) // transform the stream of (List<String>) groupNames in a stream of (String) groupName
            //             .map(groupName -> allGroups.get(groupName))
            //             .filter(group -> group.isPublicGroup())
            //             .map(group -> group.getName())
            //             .collect(Collectors.toSet());

            //     List<String> publicClasses = application.getApplicationClasses().stream()
            //             .filter(className -> allClasses.get(className).getGroups().stream()
            //                     .map(groupName -> allGroups.get(groupName))
            //                     .anyMatch(Group::isPublicGroup))
            //             .collect(Collectors.toList());

            //     if (currentAppPublicGroups.isEmpty() || publicClasses.isEmpty()){
            //         continue;
            //     }

            //     applicationsWithGroups.add(new Application(
            //             application.getName(),
            //             application.getOwner(),
            //             application.getFullName(),
            //             application.getCitation(),
            //             new ArrayList<>(currentAppPublicGroups)));
            // }
            return applicationsWithGroups;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Group> getPublicGroupsForApplication(String applicationName) throws BusinessException {
        try {
            Application application = this.getApplication(applicationName);
            if (application == null) {
                logger.error("No application exists with name {}", applicationName);
                throw new BusinessException("Wrong application name");
            }
            // need to fetch all the groups to get their properties
            Map<String,Group> allGroups = groupDAO.get().stream().collect(
                    Collectors.toMap(Group::getName, group -> group));
            List<Group> appGroups = new ArrayList<>();
            // changer
            // for (String className : application.getApplicationClasses()) {
            //     // need to fetch the classes to get their groups
            //     for (String groupName : classDAO.getClass(className).getGroups()) {
            //         appGroups.add(allGroups.get(groupName));
            //     }
            // }
            return appGroups.stream().filter(g -> g.isPublicGroup()).collect(Collectors.toList());
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
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

    private Application mapGroups(Application app) throws BusinessException {
        if (app == null) {
            return null;
        } else {
            try {
                List<Group> groups = groupDAO.getByApplication(app.getName());
    
                app.setApplicationGroups(groups.stream().map((e) -> e.getName()).collect(Collectors.toList()));
                return app;
            } catch (DAOException e) {
                throw new BusinessException(e);
            }
        }
    }

    private List<Application> mapGroups(List<Application> apps) throws BusinessException {
        for (Application app : apps) {
            mapGroups(app);
        }
        return apps;
    }
}
