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
package fr.insalyon.creatis.vip.core.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public interface ConfigurationService extends RemoteService {

    public static final String SERVICE_URI = "/configurationservice";

    public static class Util {

        public static ConfigurationServiceAsync getInstance() {

            ConfigurationServiceAsync instance = (ConfigurationServiceAsync) GWT.create(ConfigurationService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public User configure(String email, String session) throws CoreException;

    public void signup(User user, String comments) throws CoreException;

    public User signin(String email, String password) throws CoreException;

    public void signout() throws CoreException;

    public User activate(String code) throws CoreException;

    public void sendActivationCode() throws CoreException;

    public List<User> getUsers() throws CoreException;

    public void addGroup(String groupName) throws CoreException;

    public void updateGroup(String oldName, String newName) throws CoreException;

    public void removeGroup(String groupName) throws CoreException;

    public List<String> getGroups() throws CoreException;

    public void removeUser(String email) throws CoreException;

    public Map<String, CoreConstants.ROLE> getUserGroups(String email) throws CoreException;
    
    public List<String> getUserGroups() throws CoreException;

    public void setUserGroups(String email, Map<String, CoreConstants.ROLE> groups) throws CoreException;
    
    public User getUserData() throws CoreException;
    
    public void updateUser(User user) throws CoreException;
    
    public void updateUserPassword(String currentPassword, String newPassword) throws CoreException;
    
    public void sendContactMail(String category, String subject, String comment) throws CoreException;
    
    public void activateUser(String email) throws CoreException;
}
