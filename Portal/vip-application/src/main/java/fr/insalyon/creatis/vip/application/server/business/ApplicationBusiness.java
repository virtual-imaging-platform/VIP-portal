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
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationBusiness {

    /**
     *
     * @return @throws BusinessException
     */
    public List<Application> getApplications(Connection connection)
        throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getApplications();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param className
     * @return
     * @throws BusinessException
     */
    public List<String[]> getApplications(
        String className, Connection connection)
        throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getApplications(className);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @return
     * @throws BusinessException
     */
    public Application getApplication(
        String applicationName, Connection connection)
        throws BusinessException {
        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getApplication(applicationName);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param classes
     * @return
     * @throws BusinessException
     */
    public List<Application> getApplications(
        List<String> classes, Connection connection)
        throws BusinessException {
        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getApplications(classes);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<String> getApplicationNames(Connection connection)
        throws BusinessException {

        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications(connection)) {
            applicationNames.add(application.getName());
        }

        return applicationNames;
    }

    /**
     *
     * @param classes
     * @return
     * @throws BusinessException
     */
    public List<String> getApplicationNames(
        List<String> classes, Connection connection)
        throws BusinessException {

        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications(classes, connection)) {
            applicationNames.add(application.getName());
        }

        return applicationNames;
    }

    /**
     *
     * @param application
     * @throws BusinessException
     */
    public void add(Application application, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .add(application);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param application
     * @throws BusinessException
     */
    public void update(Application application, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .update(application);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param name
     * @throws BusinessException
     */
    public void remove(String name, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .remove(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param name
     * @throws BusinessException
     */
    public void remove(String email, String name, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .remove(email, name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param version
     * @throws BusinessException
     */
    public void addVersion(AppVersion version, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .addVersion(version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param version
     * @throws BusinessException
     */
    public void updateVersion(AppVersion version, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .updateVersion(version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param doi
     * @param applicationName
     * @param version
     * @throws BusinessException
     */
    public void updateDoiForVersion(
        String doi, String applicationName, String version,
        Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .updateDoiForVersion(doi, applicationName, version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @param version
     * @throws BusinessException
     */
    public void removeVersion(
        String applicationName, String version, Connection connection)
        throws BusinessException {
        try {
            ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .removeVersion(applicationName, version);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param name
     * @return
     * @throws BusinessException
     */
    public String getCitation(String name, Connection connection)
        throws BusinessException {
        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getCitation(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @return
     * @throws BusinessException
     */
    public List<AppVersion> getVersions(
        String applicationName, Connection connection) throws BusinessException {
        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getVersions(applicationName);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public AppVersion getVersion(
        String applicationName, String applicationVersion, Connection connection)
        throws BusinessException {
        try {
            return ApplicationDAOFactory.getDAOFactory()
                .getApplicationDAO(connection)
                .getVersion(applicationName, applicationVersion);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
