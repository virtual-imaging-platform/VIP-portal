package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness.UserSearchCriteria;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
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
import java.sql.Timestamp;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional
public class UserData extends JdbcDaoSupport implements UserDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String FIELDS = """
        id, email, next_email, first_name, last_name, institution, 
        code, confirmed, folder, session, registration, 
        last_login, level, country_code, max_simulations, 
        termsUse, lastUpdatePublications, failed_authentications, account_locked, apikey 
        """;

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(User user) throws DAOException {
        String query =  "INSERT INTO VIPUsers(" + FIELDS + ") "
        +               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getNextEmail());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getInstitution());
            ps.setString(7, user.getCode());
            ps.setBoolean(8, user.isConfirmed());
            ps.setString(9, user.getFolder());
            ps.setString(10, user.getSession());
            ps.setTimestamp(11, new Timestamp(user.getRegistration().getTime()));
            ps.setTimestamp(12, new Timestamp(user.getLastLogin().getTime()));
            ps.setString(13, user.getLevel().name());
            ps.setString(14, user.getCountryCode().name());
            ps.setInt(15, user.getMaxRunningSimulations());
            ps.setTimestamp(16, user.getTermsOfUse());
            ps.setTimestamp(17, user.getLastUpdatePublications());
            ps.setInt(18, user.getFailedAuthentications());
            ps.setBoolean(19, user.isAccountLocked());
            ps.setString(20, user.getApiKey());

            ps.execute();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("There is an existing account associated with the email: {} or with this {first name,last name} ({})", user.getEmail(), ex.getMessage());
                throw new DAOException("There is an existing account associated with the email: " + user.getEmail() + " or with the first name,last name: " + user.getFirstName() + "," + user.getLastName(), ex);
            } else {
                logger.error("Error adding user {}", user.getEmail(), ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public boolean authenticate(String email, String password) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "pass,account_locked FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String pass = rs.getString("pass");
                boolean locked = rs.getBoolean("account_locked");
                ps.close();
                return !locked && pass != null && pass.equals(password);
            }
            return false;

        } catch (SQLException ex) {
            logger.error("Error authenticating user {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean activate(String email, String code) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "code,account_locked FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String c = rs.getString("code");
                boolean locked = rs.getBoolean("account_locked");
                if (!locked && c.equals(code)) {

                    ps = getConnection().prepareStatement("UPDATE VIPUsers SET "
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
            logger.error("Error activating user {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public User get(String email) throws DAOException {
        String query =  "SELECT " + FIELDS
        +               "FROM VIPUsers WHERE email=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return userFromRs(rs);
            }

            logger.error("There is no user registered with the e-mail {}", email);
            throw new DAOException("There is no user registered with the e-mail: " + email);

        } catch (SQLException ex) {
            logger.error("Error getting user {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> getUsers() throws DAOException {
        String query =  "SELECT " + FIELDS
        +               "FROM VIPUsers ORDER BY LOWER(first_name), LOWER(last_name)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(userFromRs(rs));
            }
            return users;

        } catch (SQLException ex) {
            logger.error("Error getting all users", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> searchUsers(UserSearchCriteria searchCriteria) throws DAOException {
        StringBuilder query =  new StringBuilder("SELECT "
        +               "id, email, next_email, first_name, last_name, institution, "
        +               "code, confirmed, folder, session, registration, "
        +               "last_login, level, country_code, max_simulations, "
        +               "termsUse, lastUpdatePublications, failed_authentications, account_locked, apikey "
        +               "FROM VIPUsers");
        List<Object> params = new ArrayList<>();

        buildSearchQuery(searchCriteria)
                .ifPresent(queryEntry -> {
                    query.append(queryEntry.getKey());
                    params.addAll(queryEntry.getValue());
                });
        query.append("ORDER BY LOWER(registration)");

        try (PreparedStatement ps = getConnection().prepareStatement(query.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                ps.setObject(paramIndex, param);
                paramIndex++;
            }

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(userFromRs(rs));
            }
            return users;

        } catch (SQLException ex) {
            logger.error("Error getting all users", ex);
            throw new DAOException(ex);
        }
    }


    @Override
    public Long countUsers(
            UserSearchCriteria searchCriteria) throws DAOException {
        try {
            StringBuilder query = new StringBuilder("select COUNT(*) as count from VIPUsers ");
            List<Object> params = new ArrayList<>();

            buildSearchQuery(searchCriteria)
                    .ifPresent(queryEntry -> {
                        query.append(queryEntry.getKey());
                        params.addAll(queryEntry.getValue());
                    });


            PreparedStatement ps = getConnection().prepareStatement(query.toString());
            int paramIndex = 1;
            for (Object param : params) {
                ps.setObject(paramIndex, param);
                paramIndex++;
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getLong("count");
            }
            ps.close();
        } catch (SQLException ex) {
            logger.error("Error getting users number", ex);
            throw new DAOException(ex);
        }
        return -1l;
    }

    private Optional<Map.Entry<String, List<Object>>> buildSearchQuery(
            UserSearchCriteria searchCriteria) {

        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (searchCriteria.getRegistrationStart() != null) {
            query.append("AND registration >= ? ");
            params.add(java.sql.Date.valueOf(searchCriteria.getRegistrationStart()));
        }
        if (searchCriteria.getRegistrationEnd() != null) {
            query.append("AND registration <= ? ");
            params.add(java.sql.Date.valueOf(searchCriteria.getRegistrationEnd()));
        }

        if (searchCriteria.getCountry() != null) {
            query.append("AND country_code = ? ");
            params.add(searchCriteria.getCountry().name());
        }

        if (searchCriteria.getInstitution() != null) {
            query.append("AND institution = ? ");
            params.add(searchCriteria.getInstitution());
        }

        if (query.length() > 0) {
            // replace starting "AND" by "WHERE"
            query.replace(0, 3, "WHERE");
            return Optional.of(new SimpleEntry<>(
                    query.toString(),
                    params
            ));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void remove(String email) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                    + "FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing user {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void update(User user) throws DAOException {
        String query =  "UPDATE VIPUsers SET "
        +               "email = ?, next_email = ?, first_name = ?, last_name = ?, institution = ?, "
        +               "code = ?, confirmed = ?, folder = ?, session = ?, registration = ?, last_login = ?, "
        +               "level = ?, country_code = ?, max_simulations = ?, termsUse = ?, lastUpdatePublications = ?, "
        +               "failed_authentications = ?, account_locked = ? "
        +               "WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getNextEmail());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getInstitution());
            ps.setString(6, user.getCode());
            ps.setBoolean(7, user.isConfirmed());
            ps.setString(8, user.getFolder());
            ps.setString(9, user.getSession());
            ps.setTimestamp(10, user.getRegistration());
            ps.setTimestamp(11, user.getLastLogin());
            ps.setString(12, user.getLevel().name());
            ps.setString(13, user.getCountryCode().name());
            ps.setInt(14, user.getMaxRunningSimulations());
            ps.setTimestamp(15, user.getTermsOfUse());
            ps.setTimestamp(16, user.getLastUpdatePublications());
            ps.setInt(17, user.getFailedAuthentications());
            ps.setBoolean(18, user.isAccountLocked());
            ps.setString(19, user.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error updating user {}", user.getEmail(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void definePassword(String email, String password) throws DAOException {
        String queryA = "SELECT pass FROM VIPUsers WHERE email = ?";
        String queryB = "UPDATE VIPUsers SET pass = ? WHERE email = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(queryA)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if ( ! rs.next()) {
                logger.error("User not found for password creation ({})", email);
                throw new DAOException("User not found for password creation!");
            }
            if (rs.getString("pass") != null) {
                logger.error("Password already defined for {} use update method!", email);
                throw new DAOException("Password already defined use update method!");
            }
            try (PreparedStatement ps2 = getConnection().prepareStatement(queryB)) {
                ps2.setString(1, password);
                ps2.setString(2, email);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error while creating user password for {}", email, e);
            throw new DAOException(e);
        }
    }

    @Override
    public void updatePassword(String email, String currentPassword,
                               String newPassword) throws DAOException {

        if (authenticate(email, currentPassword)) {
            try {
                PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                        + "VIPUsers SET pass = ? WHERE email = ?");

                ps.setString(1, newPassword);
                ps.setString(2, email);

                ps.executeUpdate();
                ps.close();

            } catch (SQLException ex) {
                logger.error("Error updating password for {}", email, ex);
                throw new DAOException(ex);
            }
        } else {
            logger.error("The current password mismatch for {}", email);
            throw new DAOException("The current password mismatch.");
        }
    }

    @Override
    public void updateEmail(String oldEmail, String newEmail) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET email = ? WHERE email = ?");

            ps.setString(1, newEmail);
            ps.setString(2, oldEmail);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("There is an existing account associated with the email: {}", newEmail);
                throw new DAOException("There is an existing account associated with this email.", ex);
            } else {
                logger.error("Error updating email from {} to {}", oldEmail, newEmail, ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void updateNextEmail(String currentEmail, String nextEmail) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET next_email = ? WHERE email = ?");


            ps.setString(1, nextEmail); // work even if it's null
            ps.setString(2, currentEmail);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            logger.error("Error updating next email from {} to {}", currentEmail, nextEmail, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateSession(String email, String session) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET session = ? WHERE email = ?");

            ps.setString(1, session);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating session for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean verifySession(String email, String session) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "session FROM VIPUsers WHERE email = ?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String sess = rs.getString("session");
                if (sess != null && sess.equals(session)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            logger.error("Error verifying session for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateLastLogin(String email, Date lastLogin) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET last_login = ? WHERE email = ?");

            ps.setTimestamp(1, new Timestamp(lastLogin.getTime()));
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating last login to {} for {}", lastLogin, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateTermsOfUse(String email, Timestamp termsUse) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET termsUse = ? WHERE email = ?");

            ps.setTimestamp(1, termsUse);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating terms of use to {} for {}", termsUse, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public User getUserBySession(String session) throws DAOException {
        String query =  "SELECT " + FIELDS
        +               "FROM VIPUsers WHERE session=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, session);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return userFromRs(rs);
            }
            return null;
        } catch (SQLException ex) {
            logger.error("Error getting user from session: {}", session, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> getAdministrators() throws DAOException {
        String query =  "SELECT " + FIELDS
        +               "FROM VIPUsers WHERE level = ? ORDER BY LOWER(first_name), LOWER(last_name)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, UserLevel.Administrator.name());

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(userFromRs(rs));
            }
            return users;

        } catch (SQLException ex) {
            logger.error("Error getting all administrators", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void update(String email, UserLevel level, CountryCode countryCode,
                       int maxRunningSimulations, boolean locked) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET level = ?, country_code = ?, "
                    + "max_simulations = ?, account_locked = ? WHERE email = ?");
            ps.setString(1, level.name());
            ps.setString(2, countryCode.name());
            ps.setInt(3, maxRunningSimulations);
            ps.setBoolean(4, locked);
            ps.setString(5, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating user {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateCode(String email, String code) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET code = ? WHERE email = ?");
            ps.setString(1, code);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating code to {} for user {}", code, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void resetPassword(String email, String newPassword) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET pass = ? WHERE email = ?");

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error resetting password for user {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public int getNUsers() throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select COUNT(*) as count from VIPUsers");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("count");
            }
            ps.close();
        } catch (SQLException ex) {
            logger.error("Error getting users number", ex);
            throw new DAOException(ex);
        }
        return -1;
    }

    @Override
    public int getNCountries() throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("select COUNT(distinct country_code) as count from VIPUsers");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("count");
            }
            ps.close();
        } catch (SQLException ex) {
            logger.error("Error getting countries number", ex);
            throw new DAOException(ex);
        }
        return -1;
    }

    @Override
    public Timestamp getLastPublicationUpdate(String email) throws DAOException {
        try {
            Timestamp lastupdatePublication = null;
            PreparedStatement ps = getConnection().prepareStatement("SELECT lastUpdatePublications "
                    + "FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastupdatePublication = rs.getTimestamp("lastUpdatePublications");
            }

            ps.close();
            return lastupdatePublication;

        } catch (SQLException ex) {
            logger.error("Error getting last publication update for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateLastUpdatePublication(String email, Timestamp lastUpdatePublication) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET lastUpdatePublications = ? WHERE email = ?");

            ps.setTimestamp(1, lastUpdatePublication);
            ps.setString(2, email);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating last publication to {} for {}", lastUpdatePublication, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public int getNFailedAuthentications(String email) throws DAOException {
        int n = 0;
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT failed_authentications FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                n = rs.getInt("failed_authentications");
            }

            ps.close();
            return n;

        } catch (SQLException ex) {
            logger.error("Error getting failed auth number for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void lock(String email) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET "
                    + "account_locked=1 "
                    + "WHERE email = ?");

            ps.setString(1, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error locking {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void unlock(String email) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET "
                    + "account_locked=0, failed_authentications=0 "
                    + "WHERE email = ?");

            ps.setString(1, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error unlocking {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void resetNFailedAuthentications(String email) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET "
                    + "failed_authentications=0 "
                    + "WHERE email = ?");

            ps.setString(1, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error resetting failed auth number for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void incNFailedAuthentications(String email) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET "
                    + "failed_authentications = failed_authentications + 1 "
                    + "WHERE email = ?");

            ps.setString(1, email);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error increasing failed auth number for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean isLocked(String email) throws DAOException {
        boolean locked = true;
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT account_locked FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                locked = rs.getBoolean("account_locked");
            }

            ps.close();
            return locked;

        } catch (SQLException ex) {
            logger.error("Error checking if {} is locked", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public User getUserByApikey(String apikey) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT email FROM VIPUsers WHERE apikey=?");

            ps.setString(1, apikey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                ps.close();
                return get(email);
            }
            ps.close();
            logger.info("There is no user registered with the key: " + apikey);
            return null;

        } catch (SQLException ex) {
            logger.error("Error getting user by api key for {}", apikey, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public String getUserApikey(String email) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT apikey FROM VIPUsers WHERE email=?");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String apikey = rs.getString("apikey");
                ps.close();
                return apikey;
            }

            ps.close();
            logger.error("Looking for an apikey, but there is no user registered with the email: {}", email);
            throw new DAOException(
                    "Looking for an apikey, but there is no user registered with the email: " + email);
        } catch (SQLException ex) {
            logger.error("Error getting user api key for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateUserApikey(String email, String newApikey) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPUsers SET apikey = ? WHERE email = ?");

            ps.setString(1, newApikey);
            ps.setString(2, email);
            int rowsUpdatedNb = ps.executeUpdate();
            ps.close();

            if (rowsUpdatedNb == 0) {
                logger.error("Updating an apikey, but there is no user registered with the email: {}", email);
                throw new DAOException(
                        "Updating an apikey, but there is no user registered with the email: " + email);
            }
        } catch (SQLException ex) {
            logger.error("Error updating {} api key to {}", email, newApikey, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public User getById(String id) throws DAOException {
        String query =  "SELECT " + FIELDS
        +               "FROM VIPUsers WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)){
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return userFromRs(rs);
            }

            return null;

        } catch (SQLException ex) {
            logger.error("Error getting user with id {}", id, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<User> getByFullNames(List<String> fullNames) throws DAOException {
        if (fullNames == null || fullNames.isEmpty()) {
            return new ArrayList<>();
        }
        String query =  "SELECT " + FIELDS
                +               "FROM VIPUsers WHERE CONCAT(first_name, ' ', last_name) IN ("
                + fullNames.stream().map(v -> "?").collect(Collectors.joining(", "))
                + ")";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            int index = 1;
            for (String fullName : fullNames) {
                ps.setString(index++, fullName);
            }

            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<User>();

            while (rs.next()) {
                users.add(userFromRs(rs));
            }
            return users;

        } catch (SQLException ex) {
            logger.error("Error getting users with fullnames : {}", fullNames, ex);
            throw new DAOException(ex);
        }
    }

    private User userFromRs(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("id"),
                rs.getString("first_name"), rs.getString("last_name"),
                rs.getString("email"), rs.getString("next_email"),
                rs.getString("institution"),
                rs.getBoolean("confirmed"),
                rs.getString("code"), rs.getString("folder"),
                rs.getString("session"),
                rs.getTimestamp("registration"),
                rs.getTimestamp("last_login"),
                UserLevel.valueOf(rs.getString("level")),
                CountryCode.valueOf(rs.getString("country_code")),
                rs.getInt("max_simulations"),
                rs.getTimestamp("termsUse"),
                rs.getTimestamp("lastUpdatePublications"),
                rs.getInt("failed_authentications"),
                rs.getBoolean("account_locked"),
                rs.getString("apikey"));
    }
}
