/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.social.server.dao.h2;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.h2.PlatformConnection;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.server.dao.MessageDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class MessageData implements MessageDAO {

    private static final Logger logger = Logger.getLogger(MessageData.class);
    private Connection connection;

    public MessageData() throws DAOException {
        
        connection = PlatformConnection.getInstance().getConnection();
    }
    
    public void add(String email, String receiver, String title, String message) 
            throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO "
                    + "VIPSocialMessage(sender, receiver, title, message, posted, read) "
                    + "VALUES(?, ?, ?, ?, ?, ?)");
            ps.setString(1, email);
            ps.setString(2, receiver);
            ps.setString(3, title);
            ps.setString(4, message);
            ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            ps.setBoolean(6, false);
            ps.execute();
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
    public List<Message> getMessagesByUser(String email) throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "id, sender, title, message, posted, read "
                    + "FROM VIPSocialMessage AS sc WHERE receiver = ? "
                    + "ORDER BY posted DESC");
            ps.setString(1, email);
            
            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<Message>();
            SimpleDateFormat f = new SimpleDateFormat("MMMM d, yyyy HH:mm");
            
            while (rs.next()) {
                User from = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(rs.getString("sender"));
                User to = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);
                messages.add(new Message(rs.getLong("id"), from, to, rs.getString("title"), 
                        rs.getString("message"), f.format(rs.getTimestamp("posted")),
                        rs.getBoolean("read")));
            }
            
            return messages;
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public void markAsRead(long id) throws DAOException {
       
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPSocialMessage SET read = ? WHERE id = ?");
            ps.setBoolean(1, true);
            ps.setLong(2, id);
            
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
    public void remove(long id) throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "
                    + "VIPSocialMessage WHERE id = ?");
            ps.setLong(1, id);
            
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public int verifyMessages(String email) throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "COUNT(id) AS num FROM VIPSocialMessage "
                    + "WHERE receiver = ? AND read = ?");
            ps.setString(1, email);
            ps.setBoolean(2, false);
            
            ResultSet rs = ps.executeQuery();
            
            return rs.next() ? rs.getInt("num") : 0;
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
