package fr.insalyon.creatis.vip.social.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.server.dao.MessageDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
@Transactional
public class MessageData extends JdbcDaoSupport implements MessageDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserDAO userDAO;

    @Autowired
    public MessageData(UserDAO userDAO, DataSource dataSource) {
        setDataSource(dataSource);
        this.userDAO = userDAO;
    }

    public long add(String sender, String title, String message) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO "
                    + "VIPSocialMessage(sender, title, message, posted) "
                    + "VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sender);
            ps.setString(2, title);
            ps.setString(3, message);
            ps.setTimestamp(4, new Timestamp(new Date().getTime()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            long id = rs.getLong(1);
            ps.close();

            return id;

        } catch (SQLException ex) {
            logger.error("Error adding message {} by {}", title, sender, ex);
            throw new DAOException(ex);
        }
    }

    public void associateMessageToUser(String receiver, long messageId) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO "
                    + "VIPSocialMessageSenderReceiver(receiver, message_id, user_read) "
                    + "VALUES(?, ?, ?)");
            ps.setString(1, receiver);
            ps.setLong(2, messageId);
            ps.setBoolean(3, false);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error associating message {} to {}", messageId, receiver, ex);
            throw new DAOException(ex);
        }
    }

    public List<Message> getMessagesByUser(
        String email, int limit, Date startDate)
        throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "id, sender, title, message, posted, user_read "
                    + "FROM VIPSocialMessage AS sc, VIPSocialMessageSenderReceiver AS ss "
                    + "WHERE sc.id = ss.message_id AND receiver = ? "
                    + "AND posted < ? "
                    + "ORDER BY posted DESC LIMIT 0," + limit);
            ps.setString(1, email);
            ps.setTimestamp(2, new Timestamp(startDate.getTime()));

            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<Message>();
            SimpleDateFormat f = new SimpleDateFormat("MMMM d, yyyy HH:mm");

            while (rs.next()) {
                User from = userDAO.getUser(rs.getString("sender"));
                User to = userDAO.getUser(email);
                Date posted = new Date(rs.getTimestamp("posted").getTime());
                messages.add(new Message(rs.getLong("id"), from, to, rs.getString("title"),
                        rs.getString("message"), f.format(posted), posted,
                        rs.getBoolean("user_read")));
            }
            ps.close();

            return messages;

        } catch (SQLException ex) {
            logger.error("Error getting messages for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    public List<Message> getSentMessagesByUser(
        String email, int limit, Date startDate)
        throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "id, title, message, posted "
                    + "FROM VIPSocialMessage "
                    + "WHERE sender = ? "
                    + "AND posted < ? "
                    + "ORDER BY posted DESC LIMIT 0," + limit);
            ps.setString(1, email);
            ps.setTimestamp(2, new Timestamp(startDate.getTime()));

            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<Message>();
            SimpleDateFormat f = new SimpleDateFormat("MMMM d, yyyy HH:mm");

            while (rs.next()) {

                User sender = userDAO.getUser(email);

                PreparedStatement ps2 = getConnection().prepareStatement("SELECT "
                        + "receiver FROM VIPSocialMessageSenderReceiver "
                        + "WHERE message_id = ?");
                ps2.setLong(1, rs.getLong("id"));

                ResultSet rs2 = ps2.executeQuery();
                List<User> receivers = new ArrayList<User>();

                while (rs2.next()) {
                    receivers.add(userDAO.getUser(rs2.getString("receiver")));
                }
                Date posted = new Date(rs.getTimestamp("posted").getTime());
                messages.add(new Message(rs.getLong("id"), sender,
                        receivers.toArray(new User[]{}), rs.getString("title"),
                        rs.getString("message"), f.format(posted), posted,
                        true));
            }
            ps.close();

            return messages;

        } catch (SQLException ex) {
            logger.error("Error getting messages by {}", email, ex);
            throw new DAOException(ex);
        }
    }

    public void markAsRead(long id, String receiver) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPSocialMessageSenderReceiver SET user_read = ? "
                    + "WHERE message_id = ? AND receiver = ?");
            ps.setBoolean(1, true);
            ps.setLong(2, id);
            ps.setString(3, receiver);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error marking message {} read by {}", id, receiver, ex);
            throw new DAOException(ex);
        }
    }

    public void remove(long id) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM "
                    + "VIPSocialMessage WHERE id = ?");
            ps.setLong(1, id);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing message {}", id, ex);
            throw new DAOException(ex);
        }
    }

    public void removeByReceiver(long id, String receiver) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM "
                    + "VIPSocialMessageSenderReceiver "
                    + "WHERE message_id = ? AND receiver = ?");
            ps.setLong(1, id);
            ps.setString(2, receiver);
            ps.executeUpdate();

            ps = getConnection().prepareStatement("SELECT count(message_id) AS num "
                    + "FROM VIPSocialMessageSenderReceiver "
                    + "WHERE message_id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getInt("num") == 0) {
                remove(id);
            }
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing message {} by {}", id, receiver, ex);
            throw new DAOException(ex);
        }
    }

    public int verifyMessages(String email) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "COUNT(id) AS num "
                    + "FROM VIPSocialMessage AS sc, VIPSocialMessageSenderReceiver AS ss "
                    + "WHERE sc.id = ss.message_id AND receiver = ? AND user_read = ?");
            ps.setString(1, email);
            ps.setBoolean(2, false);

            ResultSet rs = ps.executeQuery();
            int result = rs.next() ? rs.getInt("num") : 0;
            ps.close();

            return result;

        } catch (SQLException ex) {
            logger.error("Error verifying messages for {}", email, ex);
            throw new DAOException(ex);
        }
    }
}
