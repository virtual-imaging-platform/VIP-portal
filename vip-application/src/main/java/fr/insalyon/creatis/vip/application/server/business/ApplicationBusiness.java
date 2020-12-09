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
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Transactional
public class ApplicationBusiness {

    private ApplicationDAO applicationDAO;

    public ApplicationBusiness(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }

    public List<Application> getApplications() throws BusinessException {

        try {
            return applicationDAO.getApplications();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String[]> getApplications(String className)
            throws BusinessException {

        try {
            return applicationDAO.getApplications(className);
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
            return applicationDAO.getApplications(classes);
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
