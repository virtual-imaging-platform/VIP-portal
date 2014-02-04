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
package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class UsersGroupsData implements UsersGroupsDAO {

    private final static Logger logger = Logger.getLogger(UsersGroupsData.class);
    private Connection connection;

    public UsersGroupsData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    @Override
    public void add(String email, String groupname, GROUP_ROLE role)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPUsersGroups(email, groupname, role) "
                    + "VALUES(?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, groupname);
            ps.setString(3, role.name());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws DAOException
     */
    @Override
    public Map<Group, GROUP_ROLE> getUserGroups(String email)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT g.groupname, g.public, g.gridfile, g.gridjobs, role "
                    + "FROM VIPGroups g JOIN VIPUsersGroups ug "
                    + "ON g.groupname = ug.groupname AND email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            Map<Group, GROUP_ROLE> groups = new HashMap<Group, GROUP_ROLE>();

            while (rs.next()) {
                String role = rs.getString("role");
                if (role == null || role.isEmpty() || role.equals("null")) {
                    role = "None";
                }
                groups.put(new Group(rs.getString("groupname"), rs.getBoolean("public"),rs.getBoolean("gridfile"),rs.getBoolean("gridjobs")),
                        GROUP_ROLE.valueOf(role));
            }
            ps.close();
            return groups;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws DAOException
     */
    @Override
    public List<String> getUserAdminGroups(String email) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "groupname FROM VIPUsersGroups "
                    + "WHERE email = ? AND role = ?");
            ps.setString(1, email);
            ps.setString(2, GROUP_ROLE.Admin.name());

            ResultSet rs = ps.executeQuery();
            List<String> groupsName = new ArrayList<String>();

            while (rs.next()) {
                groupsName.add(rs.getString("groupname"));
            }
            ps.close();
            return groupsName;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @param groups
     * @throws DAOException
     */
    @Override
    public void setUserGroups(String email, Map<String, GROUP_ROLE> groups)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPUsersGroups "
                    + "WHERE email=?");

            ps.setString(1, email);
            ps.execute();
            ps.close();

            for (String group : groups.keySet()) {
                add(email, group, groups.get(group));
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param groups
     * @return
     * @throws DAOException
     */
    @Override
    public List<String> getUsersFromGroups(List<String> groups) throws DAOException {

        try {
            StringBuilder sb = new StringBuilder();

            for (String groupName : groups) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append("groupname = '").append(groupName).append("'");
            }
            PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT "
                    + "first_name, last_name, LOWER(first_name), LOWER(last_name) "
                    + "FROM VIPUsers vu, VIPUsersGroups vg "
                    + "WHERE vu.email = vg.email AND (" + sb.toString() + ") "
                    + "ORDER BY LOWER(first_name), LOWER(last_name)");

            ResultSet rs = ps.executeQuery();
            List<String> users = new ArrayList<String>();

            while (rs.next()) {
                users.add(rs.getString("first_name") + " "
                        + rs.getString("last_name"));
            }
            ps.close();
            return users;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
    
    @Override
    public  List<Boolean> getUserPropertiesGroups(String email)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                   "SELECT public, gridfile, gridjobs "
                    + "FROM VIPGroups g, VIPUsersGroups ug "
                    + "WHERE g.groupname = ug.groupname AND ug.email= ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
           
             List<Boolean> proprties = new ArrayList<Boolean>();
             boolean isPublic=false;
             boolean isGridFile=false;
             boolean isGridJobs=false;

             while (rs.next()) { 
               if(rs.getInt("gridfile")==1){
               isGridFile=true;}
               if(rs.getInt("gridjobs")==1){
               isGridJobs=true;}
               if(rs.getInt("public")==1){
               isPublic=true;}
            }
            proprties.add(0, isPublic);
            proprties.add(1, isGridFile);
            proprties.add(2, isGridJobs);
            
            
            ps.close();
            
            
           
            return proprties;
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @return @throws DAOException
     */
    @Override
    public List<User> getUsersFromGroup(String groupName) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "us.email AS uemail, first_name, last_name, institution, "
                    + "phone, code, confirmed, folder, registration, last_login, "
                    + "level, country_code, max_simulations "
                    + "FROM VIPUsers us, VIPUsersGroups ug "
                    + "WHERE us.email = ug.email AND ug.groupname = ? "
                    + "ORDER BY LOWER(first_name), LOWER(last_name)");

            ps.setString(1, groupName);

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(new User(
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("uemail"), rs.getString("institution"),
                        "", rs.getString("phone"), rs.getBoolean("confirmed"),
                        rs.getString("code"), rs.getString("folder"), "",
                        new Date(rs.getTimestamp("registration").getTime()),
                        new Date(rs.getTimestamp("last_login").getTime()),
                        UserLevel.valueOf(rs.getString("level")),
                        CountryCode.valueOf(rs.getString("country_code")),
                        rs.getInt("max_simulations")));
            }
            ps.close();
            return users;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @param groupName
     * @throws DAOException
     */
    @Override
    public void removeUserFromGroup(String email, String groupName) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "
                    + "VIPUsersGroups WHERE email = ? AND groupname = ?");
            ps.setString(1, email);
            ps.setString(2, groupName);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
