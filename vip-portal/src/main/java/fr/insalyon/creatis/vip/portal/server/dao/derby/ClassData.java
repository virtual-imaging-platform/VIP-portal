/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.server.dao.derby;

import fr.insalyon.creatis.vip.portal.client.bean.AppClass;
import fr.insalyon.creatis.vip.portal.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.PlatformConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ClassData implements ClassDAO {

    private Connection connection;

    public ClassData() {
        connection = PlatformConnection.getInstance().getConnection();
    }

    public String add(AppClass c) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO WorkflowClass(name) "
                    + "VALUES (?)");

            ps.setString(1, c.getName());
            ps.execute();

            addGroupsToClass(c.getName(), c.getGroups());

            return "The class was succesfully saved!";

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error: an entry named \"" + c.getName() + "\" already exists.";
        } catch (DAOException ex) {
            return "Error: " + ex.getMessage();
        }
    }

    public String update(AppClass c) {
        try {
            removeGroupsFromClass(c.getName());
            addGroupsToClass(c.getName(), c.getGroups());

            return "The class was succesfully updated!";

        } catch (DAOException ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public void remove(String className) {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM WorkflowClass WHERE name=?");

            stat.setString(1, className);
            stat.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<AppClass> getClasses() {
        try {
            List<AppClass> classes = new ArrayList<AppClass>();
            PreparedStatement stat = connection.prepareStatement(
                    "SELECT name FROM WorkflowClass  ORDER BY name");

            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                List<String> groups = new ArrayList<String>();
                PreparedStatement s = connection.prepareStatement(
                        "SELECT groupname FROM PlatformGroupsClasses " +
                        "WHERE classname=? ORDER BY groupname");
                s.setString(1, rs.getString("name"));
                ResultSet r = s.executeQuery();
                
                while (r.next()) {
                    groups.add(r.getString("groupname"));
                }
                classes.add(new AppClass(rs.getString("name"), groups));
            }
            return classes;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public AppClass getClass(String className) {
        try {
            List<String> groups = new ArrayList<String>();

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "groupname "
                    + "FROM PlatformGroupsClasses "
                    + "WHERE classname=?");
            stat.setString(1, className);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                groups.add(rs.getString("groupname"));
            }

            return new AppClass(className, groups);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void addGroupsToClass(String className, List<String> groups) throws DAOException {
        for (String groupName : groups) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO "
                        + "PlatformGroupsClasses(classname, groupname) "
                        + "VALUES(?, ?)");
                ps.setString(1, className);
                ps.setString(2, groupName);
                ps.execute();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException("a group named \"" + groupName + "\" is already associated with the class.");
            }
        }
    }

    private void removeGroupsFromClass(String className) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "
                    + "PlatformGroupsClasses WHERE className=?");
            ps.setString(1, className);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException(ex.getMessage());
        }
    }
}
