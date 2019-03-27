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
package fr.insalyon.creatis.vip.core.server.dao.h2;

import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class UserData implements UserDAO {

    private final static Logger logger = Logger.getLogger(UserData.class);
    private Connection connection;

    public UserData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    @Override
    public void add(User user) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPUsers("
                    + "email, pass, first_name, last_name, institution, phone, "
                    + "code, confirmed, folder, registration, last_login, level, "
                    + "country_code, max_simulations, termsUse,lastUpdatePublications) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getInstitution());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getCode());
            ps.setBoolean(8, user.isConfirmed());
            ps.setString(9, user.getFolder());
            ps.setTimestamp(10, new Timestamp(user.getRegistration().getTime()));
            ps.setTimestamp(11, new Timestamp(user.getLastLogin().getTime()));
            ps.setString(12, user.getLevel().name());
            ps.setString(13, user.getCountryCode().name());
            ps.setInt(14, user.getMaxRunningSimulations());
            ps.setTimestamp(15, user.getTermsOfUse());
            ps.setTimestamp(16, user.getLastUpdatePublications());

            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("There is an existing account associated with the email: " + user.getEmail() + " or with this {first name,last name} (" + ex.getMessage() + ")");
                throw new DAOException("There is an existing account associated with this email or with this {first name,last name}.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public boolean authenticate(String email, String password) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "pass FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String pass = rs.getString("pass");
                ps.close();
                if (pass.equals(password)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean activate(String email, String code) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "code FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String c = rs.getString("code");
                if (c.equals(code)) {

                    ps = connection.prepareStatement("UPDATE VIPUsers SET "
                            + "confirmed=? WHERE email=?");

                    ps.setBoolean(1, true);
                    ps.setString(2, email);
                    ps.executeUpdate();
                    ps.close();

                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public User getUser(String email) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "email, first_name, last_name, institution, phone, "
                    + "code, confirmed, folder, session, registration, "
                    + "last_login, level, country_code, max_simulations, termsUse,"
                    +" lastUpdatePublications,failed_authentications,account_locked "
                    + "FROM VIPUsers "
                    + "WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("institution"),
                        "", rs.getString("phone"), rs.getBoolean("confirmed"),
                        rs.getString("code"), rs.getString("folder"),
                        rs.getString("session"),
                        new Date(rs.getTimestamp("registration").getTime()),
                        new Date(rs.getTimestamp("last_login").getTime()),
                        UserLevel.valueOf(rs.getString("level")),
                        CountryCode.valueOf(rs.getString("country_code")),
                        rs.getInt("max_simulations"),
                        rs.getTimestamp("termsUse"),
                        rs.getTimestamp("lastUpdatePublications"),
                        rs.getInt("failed_authentications"),
                        rs.getBoolean("account_locked"));

                ps.close();
                return user;
            }

            logger.error("There is no user registered with the e-mail: " + email);
            throw new DAOException("There is no user registered with the e-mail: " + email);

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> getUsers() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "email, first_name, last_name, institution, phone, "
                    + "code, confirmed, folder, registration, last_login, "
                    + "level, country_code, max_simulations, termsUse, lastUpdatePublications,"
                    + "failed_authentications,accounts_locked "
                    + "FROM VIPUsers "
                    + "ORDER BY LOWER(first_name), LOWER(last_name)");

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(new User(
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("institution"),
                        "", rs.getString("phone"), rs.getBoolean("confirmed"),
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
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(String email) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void update(User user) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET "
                    + "first_name = ?, last_name = ?, institution = ?, "
                    + "phone = ?, folder = ?, country_code = ? "
                    + "WHERE email = ?");

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getInstitution());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getFolder());
            ps.setString(6, user.getCountryCode().name());
            ps.setString(7, user.getEmail());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateEmail(String oldEmail, String newEmail) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET email = ? WHERE email = ?");

            ps.setString(1, newEmail);
            ps.setString(2, oldEmail);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("There is an existing account associated with the email: " + newEmail);
                throw new DAOException("There is an existing account associated with this email.", ex);
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void updatePassword(String email, String currentPassword,
            String newPassword) throws DAOException {

        if (authenticate(email, currentPassword)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE "
                        + "VIPUsers SET pass = ? WHERE email = ?");

                ps.setString(1, newPassword);
                ps.setString(2, email);

                ps.executeUpdate();
                ps.close();

            } catch (SQLException ex) {
                logger.error(ex);
                throw new DAOException(ex);
            }
        } else {
            logger.error("The current password mismatch for '" + email + "'.");
            throw new DAOException("The current password mismatch.");
        }
    }

    @Override
    public void updateSession(String email, String session) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET session = ? WHERE email = ?");

            ps.setString(1, session);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean verifySession(String email, String session) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "session FROM VIPUsers WHERE email = ?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String sess = rs.getString("session");
                if (sess.equals(session)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateLastLogin(String email, Date lastLogin) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET last_login = ? WHERE email = ?");

            ps.setTimestamp(1, new Timestamp(lastLogin.getTime()));
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public User getUserBySession(String session) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "email, first_name, last_name, institution, phone, "
                    + "code, confirmed, folder, session, registration, "
                    + "last_login, level, country_code, max_simulations, termsUse,"
                    + "lastUpdatePublications, failed_authentications, account_locked "
                    + "FROM VIPUsers "
                    + "WHERE session = ?");

            ps.setString(1, session);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("institution"),
                        "", rs.getString("phone"), rs.getBoolean("confirmed"),
                        rs.getString("code"), rs.getString("folder"),
                        rs.getString("session"),
                        new Date(rs.getTimestamp("registration").getTime()),
                        new Date(rs.getTimestamp("last_login").getTime()),
                        UserLevel.valueOf(rs.getString("level")),
                        CountryCode.valueOf(rs.getString("country_code")),
                        rs.getInt("max_simulations"),
                        rs.getTimestamp("termsUse"),
                        rs.getTimestamp("lastUpdatePublications"),
                        rs.getInt("failed_authentications"),
                        rs.getBoolean("account_locked")
                );
                ps.close();
                return user;
            }

            logger.error("There is no user registered with the session: " + session);
            throw new DAOException("There is no user registered with the session: " + session);

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> getAdministrators() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "email, first_name, last_name, institution, phone, "
                    + "code, confirmed, folder, registration, last_login, "
                    + "level, country_code, max_simulations, termsUse,"
                    + "lastUpdatePublications, failed_authentications, account_locked "
                    + "FROM VIPUsers WHERE level = ? "
                    + "ORDER BY LOWER(first_name), LOWER(last_name)");
            ps.setString(1, UserLevel.Administrator.name());

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(new User(
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("email"), rs.getString("institution"),
                        "", rs.getString("phone"), rs.getBoolean("confirmed"),
                        rs.getString("code"), rs.getString("folder"), "",
                        new Date(rs.getTimestamp("registration").getTime()),
                        new Date(rs.getTimestamp("last_login").getTime()),
                        UserLevel.valueOf(rs.getString("level")),
                        CountryCode.valueOf(rs.getString("country_code")),
                        rs.getInt("max_simulations"), rs.getTimestamp("termsUse"),
                        rs.getTimestamp("lastUpdatePublications"),
                        rs.getInt("failed_authentications"),
                        rs.getBoolean("account_locked")));
            }
            ps.close();
            return users;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void update(String email, UserLevel level, CountryCode countryCode, int maxRunningSimulations, boolean locked)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET level = ?, country_code = ?, locked = ? WHERE email = ?");
            ps.setString(1, level.name());
            ps.setString(2, countryCode.name());
            ps.setInt(3, maxRunningSimulations);
            ps.setBoolean(4, locked);
            ps.setString(5, email);
            
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateCode(String email, String code) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET code = ? WHERE email = ?");
            ps.setString(1, code);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void resetPassword(String email, String newPassword) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET pass = ? WHERE email = ?");

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public int getNUsers() throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("select COUNT(*) as count from VIPUsers");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("count");
            }
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        return -1;
    }

    @Override
    public int getNCountries() throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("select COUNT(distinct country_code) as count from VIPUsers");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("count");
            }
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        return -1;
    }

    @Override
    public void updateTermsOfUse(String email, Timestamp termsUse) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET termsUse = ? WHERE email = ?");

            ps.setTimestamp(1, termsUse);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void linkDropboxAccount(String email, String directory, String auth_key, String auth_secret) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void activateDropboxAccount(String email, String auth_key) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DropboxAccountStatus.AccountStatus getDropboxAccountStatus(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unlinkDropboxAccount(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Timestamp getLastPublicationUpdate(String email) throws DAOException {
        try {
            Timestamp lastupdatePublication = null;
            PreparedStatement ps = connection.prepareStatement("SELECT lastUpdatePublications "
                    + "FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastupdatePublication = rs.getTimestamp("lastUpdatePublications");
            }

            ps.close();
            return lastupdatePublication;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateLastUpdatePublication(String email, Timestamp lastUpdatePublication) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPUsers SET lastUpdatePublications = ? WHERE email = ?");

            ps.setTimestamp(1, lastUpdatePublication);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    @Override
    public int getNFailedAuthentications(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    
    @Override
    public boolean isLocked(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public void lock(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void unlock(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void resetNFailedAuthentications(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void incNFailedAuthentications(String email) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public User getUserByApikey(String apikey) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT email FROM VIPUsers WHERE apikey=?");

            ps.setString(1, apikey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String email = rs.getString("apikey");
                ps.close();
                return getUser(email);
            }
            ps.close();
            logger.info("There is no user registered with the key: " + apikey);
            return null;

        } catch (SQLException ex) {
            logger.error(ex, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public String getUserApikey(String email) throws DAOException {
        return new SQLRunnable<String>() {
            @Override
            protected String runSQL() throws SQLException, DAOException {
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT apikey FROM VIPUsers WHERE email=?");

                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String apikey = rs.getString("apikey");
                    ps.close();
                    return apikey;
                }

                ps.close();
                logger.error("Looking for an apikey, but there is no user registered with the email: " + email);
                throw new DAOException(
                        "Looking for an apikey, but there is no user registered with the email: " + email);
            }
        }.run();
    }

    @Override
    public void updateUserApikey(String email, String newApikey) throws DAOException {
        new SQLRunnable<Void>() {
            @Override
            protected Void runSQL() throws SQLException, DAOException {
                PreparedStatement ps = connection.prepareStatement("UPDATE "
                        + "VIPUsers SET apikey = ? WHERE email = ?");

                ps.setString(1, newApikey);
                ps.setString(2, email);
                int rowsUpdatedNb = ps.executeUpdate();
                ps.close();

                if (rowsUpdatedNb == 0) {
                    logger.error("Updating an apikey, but there is no user registered with the email: " + email);
                    throw new DAOException(
                            "Updating an apikey, but there is no user registered with the email: " + email);
                }
                return null;
            }
        }.run();
    }

    private abstract class SQLRunnable<T> {

        public final T run() throws DAOException {
            try {
                return runSQL();
            } catch (SQLException ex) {
                logger.error(ex, ex);
                throw new DAOException(ex);
            }
        }

        protected abstract T runSQL() throws SQLException, DAOException;

    }
}
