/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.client.bean.UserApiKey;
import fr.insalyon.creatis.vip.datamanager.server.dao.ApiKeysDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ApiKeysData implements ApiKeysDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Connection connection;

    public ApiKeysData(Connection connection) throws DAOException {
        this.connection = connection;
    }

    @Override
    public void addKey(UserApiKey apiKey)
        throws DAOException {
        try (PreparedStatement ps = connection.prepareStatement(
                 "insert into VIPApiKeys(email, identifier, apiKey) "
                 + "values(?, ?, ?)")) {
            ps.setString(1, apiKey.getUserEmail());
            ps.setString(2, apiKey.getStorageIdentifier());
            ps.setString(3, apiKey.getApiKey());

            ps.execute();
        } catch (SQLException e) {
            logger.error("Error inserting api key: {}", apiKey, e);
            throw new DAOException(e);
        }
    }

    @Override
    public void updateKey(UserApiKey apiKey)
        throws DAOException {
        try (PreparedStatement ps = connection.prepareStatement(
                 "update VIPApiKeys set apiKey = ? "
                 + "where email = ? and identifier = ?")) {
            ps.setString(1, apiKey.getApiKey());
            ps.setString(2, apiKey.getUserEmail());
            ps.setString(3, apiKey.getStorageIdentifier());

            ps.execute();
        } catch (SQLException e) {
            logger.error("Error updating api key: {}", apiKey, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<UserApiKey> getByUser(String userEmail) throws DAOException {
        try (PreparedStatement ps = connection.prepareStatement(
                 "select * from VIPApiKeys " +
                 "where email=?")) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            List<UserApiKey> apiKeys = new LinkedList<>();
            while (rs.next()) {
                UserApiKey apiKey = new UserApiKey(
                    rs.getString("identifier"),
                    rs.getString("email"),
                    rs.getString("apiKey"));
                apiKeys.add(apiKey);
            }

            return apiKeys;
        } catch (SQLException e) {
            logger.error(
                "Error getting api keys for user: {}", userEmail, e);
            throw new DAOException(e);
        }
    }

    @Override
    public void deleteKeyFor(String userEmail, String storageIdentifier)
        throws DAOException {
        try (PreparedStatement ps = connection.prepareStatement(
                 "delete from VIPApiKeys "
                 + "where email = ? and identifier = ?")) {
            ps.setString(1, userEmail);
            ps.setString(2, storageIdentifier);

            int nbRows = ps.executeUpdate();
            if (nbRows != 1) {
                logger.error("Error deleting api key for user {} and storage {} : {} rows deleted",
                        userEmail, storageIdentifier, nbRows);
                throw new DAOException(
                    "Number of deleted rows ("
                    + nbRows
                    + ") not equal to 1, for params: "
                    + "userEmail=" + userEmail
                    + ", storageIdentifier=" + storageIdentifier);
            }
        } catch (SQLException e) {
            logger.error(
                "Error deleting api key for user {} and storage {}",
                    userEmail, storageIdentifier, e);
            throw new DAOException(e);
        }
    }
}
