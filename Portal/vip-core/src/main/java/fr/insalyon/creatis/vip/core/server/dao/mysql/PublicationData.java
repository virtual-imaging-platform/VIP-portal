package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.PublicationDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationData implements PublicationDAO {

    private final static Logger logger = Logger.getLogger(PublicationData.class);
    private Connection connection;

    public PublicationData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    @Override
    public void add(Publication pub) throws DAOException {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                    "INSERT INTO VIPPublication(title,date,doi,authors,type,typeName,vipAuthor) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, pub.getTitle());
            ps.setString(2, pub.getDate());
            ps.setString(3, pub.getDoi());
            ps.setString(4, pub.getAuthors());
            ps.setString(5, pub.getType());
            ps.setString(6, pub.getTypeName());
            ps.setString(7, pub.getVipAuthor());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void update(Publication publication) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPPublication "
                    + "SET title=?, date=?, doi=?, authors=?, type=?, typeName=?,vipAuthor=? "
                    + "WHERE id=?");

            ps.setString(1, publication.getTitle());
            ps.setString(2, publication.getDate());
            ps.setString(3, publication.getDoi());
            ps.setString(4, publication.getAuthors());
            ps.setString(5, publication.getType());
            ps.setString(6, publication.getTypeName());
            ps.setString(7, publication.getVipAuthor());
            ps.setLong(8, publication.getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }

    }

    @Override
    public void remove(Long id) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPPublication WHERE id=?");

            ps.setLong(1, id);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Publication> getList() throws DAOException {
        try {
            String level = null;
            PreparedStatement ps;

            ps = connection.prepareStatement("SELECT "
                    + "id,title,date,doi,authors,type,typeName,VIPAuthor FROM "
                    + "VIPPublication");

            ResultSet rs = ps.executeQuery();

            List<Publication> publications = new ArrayList<Publication>();

            while (rs.next()) {

                publications.add(new Publication(rs.getLong("id"), rs.getString("title"), rs.getString("date"), rs.getString("doi"), rs.getString("authors"), rs.getString("type"), rs.getString("typeName"), rs.getString("VIPAuthor")));
            }

            rs.close();
            return publications;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public Publication getPublication(Long id) throws DAOException {
        try {
            String level = null;
            PreparedStatement ps;

            ps = connection.prepareStatement("SELECT "
                    + "id,title,date,doi,authors,type,typeName,VIPAuthor FROM "
                    + "VIPPublication where id=?");

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            Publication p = null;
            if (rs.next()) {
                p = new Publication(rs.getLong("id"), rs.getString("title"), rs.getString("date"), rs.getString("doi"), rs.getString("authors"), rs.getString("type"), rs.getString("typeName"), rs.getString("VIPAuthor"));
            }

            rs.close();
            return p;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
