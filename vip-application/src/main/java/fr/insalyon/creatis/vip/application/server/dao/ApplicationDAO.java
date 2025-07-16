package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface ApplicationDAO {

    public void add(Application app) throws DAOException;

    public void update(Application app) throws DAOException;

    public void remove(String name) throws DAOException;

    public void addVersion(AppVersion version) throws DAOException;

    public void updateVersion(AppVersion version) throws DAOException;

    void updateDoiForVersion(String doi, String applicationName, String version) throws DAOException;

    public void removeVersion(String applicationName, String version) throws DAOException;

    public List<Application> getApplications() throws DAOException;

    public List<Application> getApplicationsWithOwner(String owner) throws DAOException;

    public List<Application> getApplicationsByGroup(Group group) throws DAOException;

    public Application getApplication(String applicationName) throws DAOException;

    public String getCitation(String name) throws DAOException;

    List<AppVersion> getAllVisibleVersions() throws DAOException;

    public List<AppVersion> getVersions(String name) throws DAOException;

    public AppVersion getVersion(String applicationName, String applicationVersion) throws DAOException;

    public void associate(Application app, Group group) throws DAOException;

    public void dissociate(Application app, Group group) throws DAOException;
}
