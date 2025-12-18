package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
@Transactional
public class UsersGroupsData extends JdbcDaoSupport implements UsersGroupsDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(String email, String name, GROUP_ROLE role)
            throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO VIPUsersGroups(email, groupname, role) "
                    + "VALUES(?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, role.name());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error adding group {} to {}", name, email, ex);
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
            PreparedStatement ps = getConnection().prepareStatement(
                        "SELECT g.name, g.public, g.type, g.auto, role "
                    +   "FROM VIPGroups g JOIN VIPUsersGroups ug "
                    +   "ON g.name = ug.groupname AND email = ? "
                    +   "UNION "
                    +   "SELECT name, public, type, auto, NULL AS role "
                    +   "FROM VIPGroups WHERE auto = true");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            Map<Group, GROUP_ROLE> groups = new HashMap<Group, GROUP_ROLE>();

            while (rs.next()) {
                String role = rs.getString("role");
                if (role == null || role.isEmpty() || role.equals("null")) {
                    role = "None";
                }
                groups.put(new Group(rs.getString("name"), rs.getBoolean("public"), rs.getString("type"), rs.getBoolean("auto")),
                        GROUP_ROLE.valueOf(role));
            }
            ps.close();
            return groups;

        } catch (SQLException ex) {
            logger.error("Error getting groups for {}", email, ex);
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
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "groupname FROM VIPUsersGroups "
                    + "WHERE email = ? AND role = ?");
            ps.setString(1, email);
            ps.setString(2, GROUP_ROLE.Admin.name());

            ResultSet rs = ps.executeQuery();
            List<String> groupsName = new ArrayList<String>();

            while (rs.next()) {
                groupsName.add(rs.getString("name"));
            }
            ps.close();
            return groupsName;

        } catch (SQLException ex) {
            logger.error("Error getting user admin groups for {}", email, ex);
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
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                    + "FROM VIPUsersGroups "
                    + "WHERE email=?");

            ps.setString(1, email);
            ps.execute();
            ps.close();

            for (String group : groups.keySet()) {
                add(email, group, groups.get(group));
            }

        } catch (SQLException ex) {
            logger.error("Error setting user groups for {}", email, ex);
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
            PreparedStatement ps = getConnection().prepareStatement("SELECT DISTINCT "
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
            logger.error("Error getting users from {}", groups, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Boolean> getUserPropertiesGroups(String email)
            throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT public "
                    + "FROM VIPGroups g, VIPUsersGroups ug "
                    + "WHERE g.name = ug.groupname AND ug.email= ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            List<Boolean> properties = new ArrayList<Boolean>();
            boolean isPublic = false;

            while (rs.next()) {
                if (rs.getInt("public") == 1) {
                    isPublic = true;
                }
            }
            properties.add(0, isPublic);

            ps.close();

            return properties;

        } catch (SQLException ex) {
            logger.error("Error getting users properties groups for {} ", email, ex);
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
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "us.email AS uemail, next_email, first_name, last_name, institution, "
                    + "code, confirmed, folder, registration, last_login, "
                    + "level, country_code, max_simulations, termsUse, lastUpdatePublications, "
                    + "failed_authentications, account_locked "
                    + "FROM VIPUsers us, VIPUsersGroups ug "
                    + "WHERE us.email = ug.email AND ug.groupname = ? "
                    + "ORDER BY LOWER(first_name), LOWER(last_name)");

            ps.setString(1, groupName);

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(new User(
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("uemail"), rs.getString("next_email"),
                        rs.getString("institution"),
                        "", rs.getBoolean("confirmed"),
                        rs.getString("code"), rs.getString("folder"), "",
                        new Date(rs.getTimestamp("registration").getTime()),
                        new Date(rs.getTimestamp("last_login").getTime()),
                        UserLevel.valueOf(rs.getString("level")),
                        CountryCode.valueOf(rs.getString("country_code")),
                        rs.getInt("max_simulations"),
                        rs.getTimestamp("termsUse"),
                        rs.getTimestamp("lastUpdatePublications"),
                        rs.getInt("failed_authentications"),
                        rs.getBoolean("account_locked")));
            }
            ps.close();
            return users;

        } catch (SQLException ex) {
            logger.error("Error getting users from group {} ", groupName, ex);
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
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM "
                    + "VIPUsersGroups WHERE email = ? AND groupname = ?");
            ps.setString(1, email);
            ps.setString(2, groupName);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing {} from group {} ", email, groupName, ex);
            throw new DAOException(ex);
        }
    }
}
