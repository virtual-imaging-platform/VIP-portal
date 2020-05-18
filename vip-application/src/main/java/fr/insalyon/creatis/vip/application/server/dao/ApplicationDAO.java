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
package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface ApplicationDAO {

    public void add(Application workflowDescriptor) throws DAOException;

    public void update(Application workflowDescriptor) throws DAOException;

    public void remove(String name) throws DAOException;

    public void remove(String email, String name) throws DAOException;

    public void addVersion(AppVersion version) throws DAOException;

    public void updateVersion(AppVersion version) throws DAOException;

    void updateDoiForVersion(String doi, String applicationName, String version) throws DAOException;

    public void removeVersion(String applicationName, String version) throws DAOException;

    public List<Application> getApplications() throws DAOException;
    
    public Application getApplication(String applicationName) throws DAOException;

    public List<String[]> getApplications(String className) throws DAOException;

    public List<Application> getApplications(List<String> classes) throws DAOException;

    public List<String> getApplicationsName(String applicationClass) throws DAOException;

    public String getCitation(String name) throws DAOException;

    public List<AppVersion> getVersions(String name) throws DAOException;

    public AppVersion getVersion(String applicationName, String applicationVersion) throws DAOException;
}
