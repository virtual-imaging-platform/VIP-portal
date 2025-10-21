package fr.insalyon.creatis.vip.publication.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.publication.models.Publication;
import fr.insalyon.creatis.vip.publication.server.dao.PublicationDAO;
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

@Repository
@Transactional
public class PublicationData extends JdbcDaoSupport implements PublicationDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(Publication pub) throws DAOException {

        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(
                    "INSERT INTO VIPPublications(title,date,doi,authors,type,typeName,vipAuthor,vipApplication) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?,?)");

            ps.setString(1, pub.getTitle());
            ps.setString(2, pub.getDate());
            ps.setString(3, pub.getDoi());
            ps.setString(4, pub.getAuthors());
            ps.setString(5, pub.getType());
            ps.setString(6, pub.getTypeName());
            ps.setString(7, pub.getVipAuthor());
            ps.setString(8, pub.getVipApplication());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error("Error adding publication {} {}", pub.getTitle(), pub.getDoi(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void update(Publication publication) throws DAOException {
        try {

            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPPublications "
                    + "SET title=?, date=?, doi=?, authors=?, type=?, typeName=?,vipAuthor=?,vipApplication=? "
                    + "WHERE id=?");

            ps.setString(1, publication.getTitle());
            ps.setString(2, publication.getDate());
            ps.setString(3, publication.getDoi());
            ps.setString(4, publication.getAuthors());
            ps.setString(5, publication.getType());
            ps.setString(6, publication.getTypeName());
            ps.setString(7, publication.getVipAuthor());
            ps.setString(8, publication.getVipApplication());
            ps.setLong(9, publication.getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating publication {}", publication.getId(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(Long id) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                    + "FROM VIPPublications WHERE id=?");

            ps.setLong(1, id);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing publication {}", id, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Publication> getList() throws DAOException {
        logger.debug("PuBlicationData getList");
        try {
            String level = null;
            PreparedStatement ps;

            ps = getConnection().prepareStatement("SELECT "
                    + "id,title,date,doi,authors,type,typeName,VIPAuthor,VipApplication FROM "
                    + "VIPPublications");

            ResultSet rs = ps.executeQuery();

            List<Publication> publications = new ArrayList<Publication>();

            while (rs.next()) {

                publications.add(new Publication(rs.getLong("id"), rs.getString("title"), rs.getString("date"), rs.getString("doi"), rs.getString("authors"), rs.getString("type"), rs.getString("typeName"), rs.getString("VIPAuthor"), rs.getString("VIPApplication")));
            }

            rs.close();
            return publications;

        } catch (SQLException ex) {
            logger.error("Error getting all publications", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public Publication getPublication(Long id) throws DAOException {
        try {
            String level = null;
            PreparedStatement ps;

            ps = getConnection().prepareStatement("SELECT "
                    + "id,title,date,doi,authors,type,typeName,VIPAuthor, VIPApplication FROM "
                    + "VIPPublications where id=?");

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            Publication p = null;
            if (rs.next()) {
                p = new Publication(rs.getLong("id"), rs.getString("title"), rs.getString("date"), rs.getString("doi"), rs.getString("authors"), rs.getString("type"), rs.getString("typeName"), rs.getString("VIPAuthor"), rs.getString("VIPApplication"));
            }

            rs.close();
            return p;

        } catch (SQLException ex) {
            logger.error("Error getting publication {}", id, ex);
            throw new DAOException(ex);
        }
    }
}
