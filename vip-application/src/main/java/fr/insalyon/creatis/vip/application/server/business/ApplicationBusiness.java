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

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
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

    private ApplicationDAO applicationDAO;
    private ClassDAO classDAO;
    private GroupDAO groupDAO;

    @Autowired
    public ApplicationBusiness(ApplicationDAO applicationDAO, ClassDAO classDAO, GroupDAO groupDAO) {
        this.applicationDAO = applicationDAO;
        this.classDAO = classDAO;
        this.groupDAO = groupDAO;
    }

    public List<Application> getApplications() throws BusinessException {

        try {
            return applicationDAO.getApplications();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Application> getApplicationsWithOwner(String email) throws BusinessException {

        try {
            return applicationDAO.getApplicationsWithOwner(email);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Application> getPublicApplicationsWithGroups() throws BusinessException {

        try {
            Map<String,AppClass> allClasses = classDAO.getClasses().stream().collect(Collectors.toMap(
                        AppClass::getName, appClass -> appClass));
            Map<String,Group> allGroups = groupDAO.getGroups().stream().collect(Collectors.toMap(
                    Group::getName, group -> group));
            Set<String> allVisibleApplicationNames = applicationDAO.getAllVisibleVersions().stream()
                    .map(AppVersion::getApplicationName).collect(Collectors.toSet());
            List<Application> allApplications = getApplications().stream()
                    .filter(app -> allVisibleApplicationNames.contains(app.getName()))
                    .collect(Collectors.toList());

            List<Application> applicationsWithGroups = new ArrayList<>();
            for (Application application : allApplications) {
                Set<String> currentAppPublicGroups = application.getApplicationClasses().stream()
                        .map(className -> allClasses.get(className).getGroups())
                        .flatMap(List::stream) // transform the stream of (List<String>) groupNames in a stream of (String) groupName
                        .map(groupName -> allGroups.get(groupName))
                        .filter(group -> group.isPublicGroup())
                        .map(group -> group.getName())
                        .collect(Collectors.toSet());

                if (currentAppPublicGroups.isEmpty()){
                    continue;
                }

                applicationsWithGroups.add(new Application(
                        application.getName(),
                        application.getApplicationClasses(),
                        application.getOwner(),
                        application.getFullName(),
                        application.getCitation(),
                        new ArrayList<>(currentAppPublicGroups)));
            }
            return applicationsWithGroups;
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String[]> getApplications(String className)
            throws BusinessException {

        try {
            return applicationDAO.getApplicationsFromClass(className);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Application getApplication(String applicationName)
            throws BusinessException {
        try {
            return applicationDAO.getApplication(applicationName);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Application> getApplications(List<String> classes)
            throws BusinessException {
        try {
            return applicationDAO.getApplicationsFromClasses(classes);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getApplicationNames() throws BusinessException {

        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications()) {
            applicationNames.add(application.getName());
        }

        return applicationNames;
    }

    public List<String> getApplicationNames(List<String> classes)
            throws BusinessException {

        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications(classes)) {
            applicationNames.add(application.getName());
        }

        return applicationNames;
    }

    public void add(Application application) throws BusinessException {
        try {
            applicationDAO.add(application);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void update(Application application) throws BusinessException {
        try {
            applicationDAO.update(application);
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

    public void remove(String email, String name) throws BusinessException {
        try {
            applicationDAO.remove(email, name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void addVersion(AppVersion version) throws BusinessException {
        try {
            applicationDAO.addVersion(version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateVersion(AppVersion version) throws BusinessException {
        try {
            applicationDAO.updateVersion(version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateDoiForVersion(
            String doi, String applicationName, String version)
            throws BusinessException {
        try {
            applicationDAO.updateDoiForVersion(doi, applicationName, version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeVersion(String applicationName, String version)
            throws BusinessException {
        try {
            applicationDAO.removeVersion(applicationName, version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String getCitation(String name) throws BusinessException {
        try {
            return applicationDAO.getCitation(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<AppVersion> getVersions(String applicationName)
            throws BusinessException {
        try {
            return applicationDAO.getVersions(applicationName);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public AppVersion getVersion(String applicationName, String applicationVersion)
            throws BusinessException {
        try {
            return applicationDAO.getVersion(applicationName, applicationVersion);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
