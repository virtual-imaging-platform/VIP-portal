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
package fr.insalyon.creatis.vip.core.server.dao.derby;

import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.dao.derby.connection.PlatformConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class UserData implements UserDAO {

    private Connection connection;

    public UserData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    /**
     * 
     * @param user
     * @return
     */
    public String add(User user) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO PlatformUsers(dn) "
                    + "VALUES (?)");

            ps.setString(1, user.getDistinguishedName());
            ps.execute();

            addGroupToUser(user.getDistinguishedName(), user.getGroups());

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error: a user named \"" + user.getDistinguishedName() + "\" already exists.";
        } catch (DAOException ex) {
            return "Error: " + ex.getMessage();
        }
        return "The user was succesfully saved!";
    }

    /**
     * 
     * @param user
     * @return
     */
    public String update(User user) {
        try {
            removeGroupsFromUser(user.getDistinguishedName());
            addGroupToUser(user.getDistinguishedName(), user.getGroups());

            return "The user was succesfully updated!";

        } catch (DAOException ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    /**
     * 
     * @param dn
     * @return
     */
    public void remove(String dn) {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM PlatformUsers WHERE dn=?");

            stat.setString(1, dn);
            stat.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 
     * @return
     */
    public List<User> getUsers() {
        try {

            List<User> users = new ArrayList<User>();
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "dn FROM "
                    + "PlatformUsers ORDER BY dn");

            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
//                users.add(new User(rs.getString("dn")));
                users.add(getUser(rs.getString("dn")));
            }
            return users;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param dn
     * @return
     */
    public User getUser(String dn) {
        try {
            Map<String, String> groups = new HashMap<String, String>();

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "groupname, role "
                    + "FROM PlatformUsersGroups "
                    + "WHERE userdn=?");
            stat.setString(1, dn);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                groups.put(rs.getString("groupname"), rs.getString("role"));
            }

            return new User(dn, groups);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param userName
     * @param groupName
     * @param role
     * @throws DAOException
     */
    private void addGroupToUser(String userName, Map<String, String> groups) throws DAOException {
        for (String groupName : groups.keySet()) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO "
                        + "PlatformUsersGroups(userdn, groupname, role) "
                        + "VALUES(?, ?, ?)");
                ps.setString(1, userName);
                ps.setString(2, groupName);
                ps.setString(3, groups.get(groupName));
                ps.execute();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DAOException("Error: a group named \"" + groupName + "\" is already associated with the user.");
            }
        }
    }

    /**
     * 
     * @param userName
     * @throws DAOException
     */
    private void removeGroupsFromUser(String userName) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "
                    + "PlatformUsersGroups WHERE userdn=?");
            ps.setString(1, userName);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException(ex.getMessage());
        }
    }

    public boolean exists(String dn) {
        try {
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "dn "
                    + "FROM PlatformUsers "
                    + "WHERE dn=?");
            stat.setString(1, dn);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
