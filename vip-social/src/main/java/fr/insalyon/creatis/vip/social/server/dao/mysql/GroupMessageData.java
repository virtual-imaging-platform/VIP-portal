package fr.insalyon.creatis.vip.social.server.dao.mysql;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.social.client.bean.GroupMessage;
import fr.insalyon.creatis.vip.social.server.dao.GroupMessageDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class GroupMessageData extends JdbcDaoSupport implements GroupMessageDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserDAO userDAO;

    @Autowired
    public GroupMessageData(UserDAO userDAO, DataSource dataSource) {
        setDataSource(dataSource);
        this.userDAO = userDAO;
    }

    public long add(String sender, String groupName, String title, String message) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO "
                    + "VIPSocialGroupMessage(sender, name, title, message, posted) "
                    + "VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sender);
            ps.setString(2, groupName);
            ps.setString(3, title);
            ps.setString(4, message);
            ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            long result = rs.getLong(1);
            ps.close();

            return result;

        } catch (SQLException ex) {
            logger.error("Error adding a group message {} by {}", title, sender, ex);
            throw new DAOException(ex);
        }
    }

    public void remove(long id) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM "
                    + "VIPSocialGroupMessage WHERE id = ?");
            ps.setLong(1, id);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing group message {}", id, ex);
            throw new DAOException(ex);
        }
    }

    public List<GroupMessage> getMessageByGroup(String groupName, int limit, Date startDate) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "id, sender, name, title, message, posted "
                    + "FROM VIPSocialGroupMessage "
                    + "WHERE posted < ? AND name = ? "
                    + "ORDER BY posted DESC LIMIT 0," + limit);
            ps.setTimestamp(1, new Timestamp(startDate.getTime()));
            ps.setString(2, groupName);

            ResultSet rs = ps.executeQuery();
            List<GroupMessage> messages = new ArrayList<GroupMessage>();
            SimpleDateFormat f = new SimpleDateFormat("MMMM d, yyyy HH:mm");

            while (rs.next()) {
                User from = userDAO.getUser(rs.getString("sender"));
                Date posted = new Date(rs.getTimestamp("posted").getTime());
                messages.add(new GroupMessage(rs.getLong("id"), from, groupName, rs.getString("title"),
                        rs.getString("message"), f.format(posted), posted));
            }
            ps.close();

            return messages;

        } catch (SQLException ex) {
            logger.error("Error getting group messages for {}", groupName, ex);
            throw new DAOException(ex);
        }
    }

}
