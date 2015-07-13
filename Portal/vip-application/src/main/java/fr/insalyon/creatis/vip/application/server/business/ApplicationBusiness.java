/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationBusiness {

    /**
     *
     * @param applicationName
     * @return
     * @throws BusinessException
     */
    public Application getApplication(String applicationName) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getApplication(applicationName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<Application> getApplications() throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getApplications();

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
    public List<String[]> getApplications(String className) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getApplications(className);

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
    public List<Application> getApplications(List<String> classes) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getApplications(classes);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<String> getApplicationNames() throws BusinessException {

        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications()) {
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
    public List<String> getApplicationNames(List<String> classes) throws BusinessException {

        List<String> applicationNames = new ArrayList<String>();
        for (Application application : getApplications(classes)) {
            applicationNames.add(application.getName());
        }

        return applicationNames;
    }

    /**
     *
     * @param application
     * @throws BusinessException
     */
    public void add(Application application) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().add(application);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param application
     * @throws BusinessException
     */
    public void update(Application application) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().update(application);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param name
     * @throws BusinessException
     */
    public void remove(String name) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().remove(name);
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
    public void remove(String email, String name) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().remove(email, name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param version
     * @throws BusinessException 
     */
    public void addVersion(AppVersion version) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().addVersion(version);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param version
     * @throws BusinessException 
     */
    public void updateVersion(AppVersion version) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().updateVersion(version);

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
    public void removeVersion(String applicationName, String version) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationDAO().removeVersion(applicationName, version);
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
    public String getCitation(String name) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getCitation(name);
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
    public List<AppVersion> getVersions(String applicationName) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getVersions(applicationName);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
