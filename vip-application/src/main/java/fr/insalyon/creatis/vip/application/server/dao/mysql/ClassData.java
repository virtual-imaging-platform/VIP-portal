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
package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Ferreira da Silva
 */
@Repository
@Transactional
public class ClassData extends JdbcDaoSupport implements ClassDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(AppClass appClass) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO VIPClasses(name) "
                            + "VALUES (?)");

            ps.setString(1, appClass.getName());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("A class named \"{}\" already exists.", appClass.getName());
                throw new DAOException("A class named \"" + appClass.getName() + "\" already exists.");
            } else {
                logger.error("Error adding class {}", appClass.getName(), ex);
                throw new DAOException(ex);
            }
        }

        addToClass(appClass.getName(), appClass.getEngines(), "engine");
        addToClass(appClass.getName(), appClass.getGroups(), "group");
    }

    @Override
    public void update(AppClass appClass) throws DAOException {

        removeFromClass(appClass.getName(), "engine");
        addToClass(appClass.getName(), appClass.getEngines(), "engine");

        removeFromClass(appClass.getName(), "group");
        addToClass(appClass.getName(), appClass.getGroups(), "group");

    }

    @Override
    public void remove(String className) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                    + "FROM VIPClasses WHERE name=?");

            ps.setString(1, className);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing class {}", className, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AppClass> getClasses() throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT distinct name FROM VIPClasses ORDER BY name");

            ResultSet rs = ps.executeQuery();
            List<AppClass> classes = new ArrayList<AppClass>();

            while (rs.next()) {
                List<String> groups = new ArrayList<String>();
                PreparedStatement ps2 = getConnection().prepareStatement(
                        "SELECT name FROM VIPGroupsClasses "
                                + "WHERE classname=? ORDER BY name");
                ps2.setString(1, rs.getString("name"));
                ResultSet rg = ps2.executeQuery();

                while (rg.next()) {
                    groups.add(rg.getString("name"));
                }
                ps2.close();

                List<String> engines = new ArrayList<String>();
                PreparedStatement ps3 = getConnection().prepareStatement(
                        "SELECT engine FROM VIPClassesEngines "
                                + "WHERE class=? ORDER BY engine");
                ps3.setString(1, rs.getString("name"));
                ResultSet re = ps3.executeQuery();
                while (re.next()) {
                    engines.add(re.getString("engine"));
                }
                ps3.close();

                classes.add(new AppClass(rs.getString("name"),
                        engines, groups));
            }

            ps.close();
            return classes;

        } catch (SQLException ex) {
            logger.error("Error getting all classes", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public AppClass getClass(String className) throws DAOException {
        try {

            // Get class
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT name FROM VIPClasses WHERE name=?");
            ps.setString(1, className);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {

                // Get groups associated to class
                List<String> groups = new ArrayList<String>();
                PreparedStatement ps2 = getConnection().prepareStatement(
                        "SELECT name FROM VIPGroupsClasses "
                                + "WHERE classname=? ORDER BY name");
                ps2.setString(1, rs.getString("name"));
                ResultSet r = ps2.executeQuery();
                while (r.next()) {
                    groups.add(r.getString("name"));
                }
                ps2.close();
                // Get engines associated to class
                List<String> engines = new ArrayList<String>();
                PreparedStatement ps3 = getConnection().prepareStatement(
                        "SELECT engine FROM VIPClassesEngines "
                                + "WHERE class=? ORDER BY engine");
                ps3.setString(1, rs.getString("name"));
                ResultSet re = ps3.executeQuery();
                while (re.next()) {
                    engines.add(re.getString("engine"));
                }
                ps3.close();

                return new AppClass(rs.getString("name"),
                        engines, groups);
            }
            ps.close();
            return null;

        } catch (SQLException ex) {
            logger.error("Error getting class {}", className, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AppClass> getUserClasses(String email, boolean validAdmin) throws DAOException {

        try {
            String clause = validAdmin ? " AND ug.role = ?" : "";

            PreparedStatement ps = getConnection().prepareStatement("SELECT DISTINCT classname "
                    + "FROM VIPGroupsClasses gc, VIPUsersGroups ug "
                    + "WHERE ug.name = gc.name AND ug.email = ?" + clause);

            ps.setString(1, email);
            if (validAdmin) {
                ps.setString(2, GROUP_ROLE.Admin.name());
            }

            ResultSet rs = ps.executeQuery();
            List<AppClass> classes = new ArrayList<AppClass>();

            while (rs.next()) {
                List<String> groups = new ArrayList<String>();
                PreparedStatement ps2 = getConnection().prepareStatement(
                        "SELECT name FROM VIPGroupsClasses "
                                + "WHERE classname=? ORDER BY name");
                ps2.setString(1, rs.getString("classname"));
                ResultSet r = ps2.executeQuery();

                while (r.next()) {
                    groups.add(r.getString("name"));
                }
                classes.add(new AppClass(rs.getString("classname"), groups));
                ps2.close();
            }

            ps.close();
            return classes;

        } catch (SQLException ex) {
            logger.error("Error getting classes for {} (validAdmin : {})", email, validAdmin, ex);
            throw new DAOException(ex);
        }
    }

    /**
     * @param objectType (group or engine)
     */
    private void addToClass(String className, List<String> objectList, String objectType) throws DAOException {

        for (String name : objectList) {
            try {
                PreparedStatement ps;
                switch (objectType) {
                    case "group":
                        ps = getConnection().prepareStatement("INSERT INTO "
                                + "VIPGroupsClasses(classname, name) "
                                + "VALUES(?, ?)");
                        break;
                    case "engine":
                        ps = getConnection().prepareStatement("INSERT INTO "
                                + "VIPClassesEngines(class, engine) "
                                + "VALUES(?, ?)");
                        break;
                    default:
                        logger.error("Error adding something to class {} : invalid objectType {}",
                                className, objectType);
                        throw new IllegalArgumentException("Invalid objectType: " + objectType);
                }
                ps.setString(1, className);
                ps.setString(2, name);
                ps.execute();
                ps.close();

            } catch (SQLException ex) {
                if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                    logger.error("a " + objectType + " named \"" + name + "\" is already associated with the class.");
                    throw new DAOException("a " + objectType + " named \"" + name + "\" is already associated with the class.", ex);
                } else {
                    logger.error("Error adding {} {} to class {}", objectType, objectList, className, ex);
                    throw new DAOException(ex);
                }
            }
        }
    }

    /**
     * @param objectType (group or engine)
     */
    private void removeFromClass(String className, String objectType) throws DAOException {

        try {
            PreparedStatement ps;
            switch (objectType) {
                case "group":
                    ps = getConnection().prepareStatement("DELETE FROM "
                            + "VIPGroupsClasses WHERE className=?");
                    break;
                case "engine":
                    ps = getConnection().prepareStatement("DELETE FROM "
                            + "VIPClassesEngines WHERE class=?");
                    break;
                default:
                    logger.error("Error removing something from class {} : invalid objectType {}",
                            className, objectType);
                    throw new IllegalArgumentException("Invalid objectType: " + objectType);
            }

            ps.setString(1, className);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing {} from class {}", objectType, className, ex);
            throw new DAOException(ex);
        }
    }
}
