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

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class GroupData implements GroupDAO {

    private final static Logger logger = Logger.getLogger(GroupData.class);
    private Connection connection;

    public GroupData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    /**
     * 
     * @param groupName
     * @throws DAOException 
     */
    public void add(String groupName) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPGroups(groupname) VALUES(?)");

            ps.setString(1, groupName);
            ps.execute();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation")) {
                logger.error("A group named \"" + groupName + "\" already exists.");
                throw new DAOException("A group named \"" + groupName + "\" already exists.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    public void remove(String groupName) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM VIPGroups WHERE groupname=?");

            stat.setString(1, groupName);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public void update(String oldName, String newName) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("UPDATE "
                    + "VIPGroups "
                    + "SET groupname=? "
                    + "WHERE groupname=?");

            stat.setString(1, newName);
            stat.setString(2, oldName);
            stat.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public List<String> getGroups() throws DAOException {
        try {

            List<String> groups = new ArrayList<String>();
            PreparedStatement stat = null;
            stat = connection.prepareStatement("SELECT groupname FROM "
                    + "VIPGroups ORDER BY groupname");

            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                groups.add(rs.getString("groupname"));
            }
            return groups;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
