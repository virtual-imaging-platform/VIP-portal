/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ClassData implements ClassDAO {

    private final static Logger logger = Logger.getLogger(ClassData.class);
    private Connection connection;

    public ClassData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    /**
     *
     * @param appClass
     * @throws DAOException
     */
    @Override
    public void add(AppClass appClass) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPClasses(name, engine) "
                    + "VALUES (?, ?)");

            ps.setString(1, appClass.getName());
            ps.setString(2, appClass.getEngine());
            ps.execute();
            ps.close();

            addGroupsToClass(appClass.getName(), appClass.getGroups());

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                throw new DAOException("A class named \"" + appClass.getName() + "\" already exists.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    /**
     *
     * @param appClass
     * @throws DAOException
     */
    @Override
    public void update(AppClass appClass) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE VIPClasses SET engine = ? "
                    + "WHERE name = ?");

            ps.setString(1, appClass.getEngine());
            ps.setString(2, appClass.getName());
            ps.executeUpdate();
            ps.close();

            removeGroupsFromClass(appClass.getName());
            addGroupsToClass(appClass.getName(), appClass.getGroups());

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param className
     * @throws DAOException
     */
    @Override
    public void remove(String className) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPClasses WHERE name=?");

            ps.setString(1, className);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws DAOException 
     */
    @Override
    public List<AppClass> getClasses() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT name, engine FROM VIPClasses ORDER BY name");

            ResultSet rs = ps.executeQuery();
            List<AppClass> classes = new ArrayList<AppClass>();

            while (rs.next()) {
                List<String> groups = new ArrayList<String>();
                PreparedStatement ps2 = connection.prepareStatement(
                        "SELECT groupname FROM VIPGroupsClasses "
                        + "WHERE classname=? ORDER BY groupname");
                ps2.setString(1, rs.getString("name"));
                ResultSet r = ps2.executeQuery();

                while (r.next()) {
                    groups.add(r.getString("groupname"));
                }
                ps2.close();
                classes.add(new AppClass(rs.getString("name"), 
                        rs.getString("engine"), groups));
            }

            ps.close();
            return classes;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @param validAdmin
     * @return
     * @throws DAOException
     */
    @Override
    public List<AppClass> getUserClasses(String email, boolean validAdmin) throws DAOException {

        try {
            String clause = validAdmin ? " AND ug.role = ?" : "";

            PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT classname "
                    + "FROM VIPGroupsClasses gc, VIPUsersGroups ug "
                    + "WHERE ug.groupname = gc.groupname AND ug.email = ?" + clause);

            ps.setString(1, email);
            if (validAdmin) {
                ps.setString(2, GROUP_ROLE.Admin.name());
            }

            ResultSet rs = ps.executeQuery();
            List<AppClass> classes = new ArrayList<AppClass>();

            while (rs.next()) {
                List<String> groups = new ArrayList<String>();
                PreparedStatement ps2 = connection.prepareStatement(
                        "SELECT groupname FROM VIPGroupsClasses "
                        + "WHERE classname=? ORDER BY groupname");
                ps2.setString(1, rs.getString("classname"));
                ResultSet r = ps2.executeQuery();

                while (r.next()) {
                    groups.add(r.getString("groupname"));
                }
                classes.add(new AppClass(rs.getString("classname"), groups));
                ps2.close();
            }

            ps.close();
            return classes;

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param className
     * @param groups
     * @throws DAOException
     */
    private void addGroupsToClass(String className, List<String> groups) throws DAOException {

        for (String groupName : groups) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO "
                        + "VIPGroupsClasses(classname, groupname) "
                        + "VALUES(?, ?)");

                ps.setString(1, className);
                ps.setString(2, groupName);
                ps.execute();
                ps.close();

            } catch (SQLException ex) {
                if (ex.getMessage().contains("Duplicate entry")) {
                    logger.error("a group named \"" + groupName + "\" is already associated with the class.");
                    throw new DAOException("a group named \"" + groupName + "\" is already associated with the class.");
                } else {
                    logger.error(ex);
                    throw new DAOException(ex);
                }
            }
        }
    }

    /**
     *
     * @param className
     * @throws DAOException
     */
    private void removeGroupsFromClass(String className) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "
                    + "VIPGroupsClasses WHERE className=?");

            ps.setString(1, className);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex.getMessage());
        }
    }
}
