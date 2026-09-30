package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness.UserSearchCriteria;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface UserDAO {

    public void add(User user) throws DAOException;

    public void update(User user) throws DAOException;

    /**
     * This method verifies if the password is correct AND the account is not locked.
     * @param email
     * @param password
     * @return
     * @throws DAOException 
     */
    public boolean authenticate(String email, String password) throws DAOException;

    public boolean activate(String email, String code) throws DAOException;

    public User get(String email) throws DAOException;

    public List<User> getUsers() throws DAOException;

    List<User> searchUsers(UserSearchCriteria searchCriteria) throws DAOException;

    Long countUsers(UserSearchCriteria searchCriteria) throws DAOException;

    public void remove(String email) throws DAOException;

    public void definePassword(String email, String password) throws DAOException;

    public void updatePassword(String email, String currentPassword, String newPassword) throws DAOException;

    void updateEmail(String oldEmail, String newEmail) throws DAOException;

    void updateNextEmail(String currentEmail, String nextEmail) throws DAOException;

    public void updateSession(String email, String session) throws DAOException;

    public boolean verifySession(String email, String session) throws DAOException;

    public void updateLastLogin(String email, Date lastLogin) throws DAOException;

    public User getUserBySession(String session) throws DAOException;

    public List<User> getAdministrators() throws DAOException;

    public void update(String email, UserLevel level, CountryCode countryCode, int maxRunningSimulations, boolean locked) throws DAOException;

    public void updateCode(String email, String code) throws DAOException;

    public void updateTermsOfUse(String email, Timestamp termsUse) throws DAOException;

    public void resetPassword(String email, String newPassword) throws DAOException;

    public int getNUsers() throws DAOException;

    public int getNCountries() throws DAOException;

    public Timestamp getLastPublicationUpdate(String email) throws DAOException;

    public void updateLastUpdatePublication(String email, Timestamp lastUpdatePublication) throws DAOException;
    
    public int getNFailedAuthentications(String email) throws DAOException;
    
    public void resetNFailedAuthentications(String email) throws DAOException;
    
    public void incNFailedAuthentications(String email) throws DAOException;
    
    public void lock(String email) throws DAOException;
    
    public void unlock(String email) throws DAOException;
    
    public boolean isLocked(String email) throws DAOException;

    User getUserByApikey(String apikey) throws DAOException;

    String getUserApikey(String email) throws DAOException;

    /**
     * change the api key of a specific user, identified by its mail address
     * no validation is done on the new key which should be secure
     *
     * @param email email the email idenfier of the user
     * @param newApikey the new key to link to the user
     * @throws DAOException if there isn't any user for the given email
     * or if there is already a user with this key
     */
    void updateUserApikey(String email, String newApikey) throws DAOException;

    User getById(String id) throws DAOException;

    List<User> getByFullNames(List<String> fullNames) throws DAOException;

    public String getPasswordHash(String email) throws DAOException;
}