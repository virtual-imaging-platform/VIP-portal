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
package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
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

    /**
     * @param email
     * @param code
     * @return
     * @throws DAOException 
     */
    public boolean activate(String email, String code) throws DAOException;

    public User getUser(String email) throws DAOException;

    public List<User> getUsers() throws DAOException;

    public void remove(String email) throws DAOException;

    public void updatePassword(String email, String currentPassword, String newPassword) throws DAOException;

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

    public void linkDropboxAccount(String email, String directory, String auth_key, String auth_secret) throws DAOException;

    public void activateDropboxAccount(String email, String auth_key) throws DAOException;

    public DropboxAccountStatus.AccountStatus getDropboxAccountStatus(String email) throws DAOException;

    public void unlinkDropboxAccount(String email) throws DAOException;

    public Timestamp getLastPublicationUpdate(String email) throws DAOException;

    public void updateLastUpdatePublication(String email, Timestamp lastUpdatePublication) throws DAOException;
    
    /**
     * Returns the number of failed authentication since the last successful one.
     * @param email email of the user
     * @throws DAOException 
     */
    public int getNFailedAuthentications(String email) throws DAOException;
    
    /**
     * Resets the number of failed authentication since the last successful one.
     * Typically used when authentication is successful.
     * @param email email of the user
     * @param n value to set
     * @throws DAOException 
     */
    public void resetNFailedAuthentications(String email) throws DAOException;
    
    /**
     * Increments the number of failed authentications since the last successful one.
     * @param email email of the user.
     * @throws DAOException 
     */
    public void incNFailedAuthentications(String email) throws DAOException;
    
    /**
     * Locks the user.
     * A user is locked manually through the interface or when the number of failed authentications is too high. 
     * @param email email of the user
     * @throws DAOException 
     */
    public void lock(String email) throws DAOException;
    
    /**
     * Unlocks the user.
     * Only administrators should be able to unlock users. 
     * This method also resets the number of failed authentications since the last successful one.
     * @param email email of the user.
     * @throws DAOException 
     */
    public void unlock(String email) throws DAOException;
    
    /**
     * Returns true if the user account is locked.
     * @param email email of the user.
     * @return
     * @throws DAOException 
     */
    public boolean isLocked(String email) throws DAOException;

    /**
     * Return the user linked to the specified apikey
     * If there isn't any, return null
     */
    User getUserByApikey(String apikey) throws DAOException;

    /**
     * Read the apikey of a specific user, identified by its mail address
     *
     * @param email the email idenfier of the user
     * @return the key, or null if the user doesn't have any
     * @throws DAOException if there isn't any user for the given email
     */
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
}
