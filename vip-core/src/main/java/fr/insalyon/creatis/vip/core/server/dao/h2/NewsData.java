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
package fr.insalyon.creatis.vip.core.server.dao.h2;

import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.News;
import fr.insalyon.creatis.vip.core.server.dao.NewsDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class NewsData implements NewsDAO {

    private Connection connection;

    public NewsData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    public String add(News news) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO PlatformNews(title, message, posted, owner) "
                    + "VALUES (?, ?, ?, ?)");

            ps.setString(1, news.getTitle());
            ps.setString(2, news.getMessage());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4, news.getAuthor());
            ps.execute();

            return "The news was succesfully saved!";

        } catch (SQLException ex) {
            return "Error: an entry named \"" + news.getTitle() + "\" already exists.";
        }
    }

    public String remove(News news) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM PlatformNews WHERE title = ? AND owner = ?");
            ps.setString(1, news.getTitle());
            ps.setString(2, news.getAuthor());
            
            ps.executeUpdate();
            
            return "The news was succesfully removed!";
            
        } catch (SQLException ex) {
            return "Error: unable to delete news.";
        }
    }
    
    public String update(News news) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE PlatformNews SET message = ? "
                    + "WHERE title = ? AND owner = ?");
            ps.setString(1, news.getMessage());
            ps.setString(2, news.getTitle());
            ps.setString(3, news.getAuthor());
            
            ps.executeUpdate();
            
            return "The news was succesfully updated!";
            
        } catch (SQLException ex) {
            return "Error: unable to update news.";
        }
    }

    public List<News> getNews() throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT title, message, posted, owner FROM PlatformNews "
                    + "ORDER BY posted DESC");
            ResultSet rs = ps.executeQuery();

            List<News> news = new ArrayList<News>();
            SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            while (rs.next()) {
                news.add(new News(
                        rs.getString("title"),
                        rs.getString("message"),
                        f.format(rs.getTimestamp("posted")),
                        rs.getString("owner")));
            }

            return news;

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }
}
