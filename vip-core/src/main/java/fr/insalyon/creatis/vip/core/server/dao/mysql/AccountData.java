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
package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.dao.AccountDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Repository
@Transactional
public class AccountData extends JdbcDaoSupport implements AccountDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    /**
     *
     * @param name
     * @param groups
     * @throws DAOException
     */
    @Override
    public void add(String name, List<String> groups) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO "
                    + "VIPAccounts(name) VALUES(?)");
            ps.setString(1, name);
            ps.execute();
            ps.close();

            addGroupsToAccount(name, groups);

        } catch (SQLException ex) {
            logger.error("Error adding account {}", name, ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param oldName
     * @param newName
     * @param groups
     * @throws DAOException
     */
    @Override
    public void update(String oldName, String newName, List<String> groups) throws DAOException {

        try {
            if (oldName != null && !oldName.equals(newName)) {
                PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                        + "VIPAccounts SET name = ? WHERE name = ?");
                ps.setString(1, newName);
                ps.setString(2, oldName);
                ps.executeUpdate();
                ps.close();
            }

            removeGroupsFromAccount(newName);
            addGroupsToAccount(newName, groups);

        } catch (SQLException ex) {
            logger.error("Error updating account from {} to {}", oldName, newName, ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param name
     * @throws DAOException
     */
    @Override
    public void remove(String name) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                    + "FROM VIPAccounts WHERE name=?");

            ps.setString(1, name);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing account {}", name, ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @return @throws DAOException
     */
    @Override
    public List<Account> getList() throws DAOException {

        try {
            return getJdbcTemplate().query(
                "SELECT name FROM VIPAccounts ORDER BY LOWER(name)",
                    (resultSet, rowNum) -> {
                        String name = resultSet.getString("name");
                        return new Account(name, getGroupsInternal(name));
                    });
        } catch (DataAccessException ex) {
            logger.error("Error getting all accounts", ex);
            throw new DAOException(ex);
        }
    }

    private List<Group> getGroupsInternal(String... accounts) {

        StringBuilder query = new StringBuilder();
        for (String account : accounts) {
            if (query.length() > 0) {
                query.append(" OR ");
            }
            query.append("name = '").append(account).append("'");
        }

        return getJdbcTemplate().query(
                "SELECT DISTINCT "
                        + "ag.groupname AS name, g.public AS pub, g.gridfile AS gfile, g.gridjobs AS gjobs "
                        + "FROM VIPAccountsGroups AS ag "
                        + "LEFT JOIN VIPGroups AS g ON ag.groupname = g.groupname "
                        + "WHERE " + query.toString(),
                (rs, rowNum) -> new Group(
                        rs.getString("name"), rs.getBoolean("pub"),
                        rs.getBoolean("gfile"),rs.getBoolean("gjobs")) );
    }

    /**
     *
     * @param accounts
     * @return
     * @throws DAOException
     */
    @Override
    public List<Group> getGroups(String... accounts) throws DAOException {

        try {
            return getGroupsInternal(accounts);

        } catch (DataAccessException ex) {
            logger.error("Error getting groups for accounts {}", Arrays.toString(accounts), ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param name
     * @param groups
     * @throws DAOException
     */
    private void addGroupsToAccount(String name, List<String> groups) throws DAOException {

        try {
            for (String group : groups) {
                PreparedStatement ps = getConnection().prepareStatement("INSERT INTO "
                        + "VIPAccountsGroups(name, groupname) VALUES(?, ?)");
                ps.setString(1, name);
                ps.setString(2, group);
                ps.execute();
                ps.close();
            }

        } catch (SQLException ex) {
            logger.error("Error adding groups to account {}", name, ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param name
     * @throws DAOException
     */
    private void removeGroupsFromAccount(String name) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM "
                    + "VIPAccountsGroups WHERE name = ?");
            ps.setString(1, name);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing groups from account {}", name, ex);
            throw new DAOException(ex);
        }
    }
}
