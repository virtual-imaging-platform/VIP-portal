/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface UserDAO {

    public void add(User user) throws DAOException;
    
    public void update(User user) throws DAOException;

    public boolean authenticate(String email, String password) throws DAOException;

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
    
    public void update(String email, UserLevel level, CountryCode countryCode, int maxRunningSimulations) throws DAOException;
    
    public void updateCode(String email, String code) throws DAOException;
    
    public void resetPassword(String email, String newPassword) throws DAOException;
    
    public int getNUsers() throws DAOException;
    
    public int getNCountries() throws DAOException;
    
    public void linkDropboxAccount(String email, String directory, String auth_key, String auth_secret) throws DAOException;
    
    public void activateDropboxAccount(String email, String auth_key) throws DAOException;
    
    public DropboxAccountStatus.AccountStatus getDropboxAccountStatus(String email) throws DAOException;
    
    public void unlinkDropboxAccount(String email) throws DAOException;   
}
