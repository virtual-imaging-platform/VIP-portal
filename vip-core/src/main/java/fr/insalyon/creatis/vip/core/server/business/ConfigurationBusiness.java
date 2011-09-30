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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.ROLE;
import fr.insalyon.creatis.vip.core.server.business.proxy.MyProxyClient;
import fr.insalyon.creatis.vip.core.server.business.proxy.Proxy;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Rafael Silva
 */
public class ConfigurationBusiness {

    private final static Logger logger = Logger.getLogger(ConfigurationBusiness.class);

    /**
     * 
     * @throws BusinessException 
     */
    public void configure() throws BusinessException {

        PropertyConfigurator.configure(ConfigurationBusiness.class.getClassLoader().getResource("vipLog4j.properties"));

        try {
            // MyProxy
            logger.info("Configuring VIP server proxy.");
            MyProxyClient myproxy = new MyProxyClient();
            Proxy proxy = myproxy.getProxy();

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            currentDate.add(Calendar.DAY_OF_MONTH, 2);
            Calendar proxyDate = Calendar.getInstance();
            proxyDate.setTime(proxy.getEndDate());

            if (proxyDate.before(currentDate)) {
                logger.error("The server proxy is valid for less than 2 days.");
                throw new BusinessException("Unable to load VIP configuration.");
            }

            // Voms Extension
//            Server serverConf = Server.getInstance();
//            String command = "voms-proxy-init --voms biomed"
//                    + " -cert " + serverConf.getServerProxy()
//                    + " -key " + serverConf.getServerProxy()
//                    + " -out " + serverConf.getServerProxy()
//                    + " -noregen -hours 240";
//            Process process = Runtime.getRuntime().exec(command);
//
//            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String s = null;
//            String cout = "";
//
//            while ((s = r.readLine()) != null) {
//                cout += s + "\n";
//            }
//            process.waitFor();
//
//            logger.info(cout);
//            
//            process.getOutputStream().close();
//            process.getInputStream().close();
//            process.getErrorStream().close();
//            r.close();
//            
//            process = null;

        } catch (Exception ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @throws BusinessException 
     */
    public void signup(User user) throws BusinessException {

        try {
            user.setCode(UUID.randomUUID().toString());
            user.setPassword(MD5.get(user.getPassword()));
            user.setFolder(user.getFirstName().toLowerCase() + "_"
                    + user.getLastName().toLowerCase());

            CoreDAOFactory.getDAOFactory().getUserDAO().add(user);

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                    + "<p>We have successfully received your membership registration "
                    + "and your personal profile has been created.</p>"
                    + "<p>Please confirm your registration using the following activation "
                    + "code on your first login:</p>"
                    + "<p><b>" + user.getCode() + "</b></p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            logger.info("Sending confirmation email to '" + user.getEmail() + "'.");
            CoreUtil.sendEmail("VIP", "VIP account details", emailContent,
                    new String[]{user.getEmail()});

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param password
     * @return
     * @throws BusinessException 
     */
    public User signin(String email, String password) throws BusinessException {

        try {
            password = MD5.get(password);
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            if (userDAO.authenticate(email, password)) {
                return userDAO.getUser(email);

            } else {
                logger.error("Authentication failed to '" + email + "' (email or password incorrect).");
                throw new BusinessException("Authentication failed.");
            }

        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param code
     * @return
     * @throws BusinessException 
     */
    public User activate(String email, String code) throws BusinessException {

        try {
            UserDAO userDAO = CoreDAOFactory.getDAOFactory().getUserDAO();
            if (userDAO.activate(email, code)) {

                User user = userDAO.getUser(email);

                VletAgentClient client = CoreUtil.getVletAgentClient();
                client.createDirectory(Server.getInstance().getDataManagerUsersHome(),
                        user.getFolder());

                client.createDirectory(Server.getInstance().getDataManagerUsersHome(),
                        user.getFolder() + "_" + CoreConstants.FOLDER_TRASH);

                CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().add(email,
                        CoreConstants.GROUP_GUEST, ROLE.User);

                return user;

            } else {
                logger.error("Activation failed to '" + email + "' (wrong code).");
                throw new BusinessException("Activation failed.");
            }

        } catch (VletAgentClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @throws BusinessException 
     */
    public void sendActivationCode(String email) throws BusinessException {

        try {
            User user = CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

            String emailContent = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                    + "<p>You requested us to send you your personal activation code.</p>"
                    + "<p>Please use the following code to activate your account:</p>"
                    + "<p><b>" + user.getCode() + "</b></p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            CoreUtil.sendEmail("VIP", "VIP activation code (reminder)", emailContent,
                    new String[]{user.getEmail()});

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @return
     * @throws BusinessException 
     */
    public boolean isSystemAdministrator(String email) throws BusinessException {

        try {
            Map<String, CoreConstants.ROLE> groups = CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserGroups(email);

            for (String group : groups.keySet()) {
                if (group.equals(CoreConstants.GROUP_ADMIN)) {
                    return true;
                }
            }
            return false;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @throws BusinessException 
     */
    public void removeUser(String email) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUserDAO().remove(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<User> getUsers() throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO().getUsers();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<String> getUserNames(String email, boolean validGroup)
            throws BusinessException {

        try {
            if (validGroup) {

                List<String> groups = new ArrayList<String>();
                Map<String, ROLE> userGroups = CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserGroups(email);

                for (String groupName : userGroups.keySet()) {
                    if (!groupName.equals(CoreConstants.GROUP_ADMIN)) {
                        if (userGroups.get(groupName) == ROLE.Admin) {
                            groups.add(groupName);
                        }
                    }
                }

                if (groups.isEmpty()) {
                    List<String> userNames = new ArrayList<String>();
                    userNames.add(CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email).getFullName());
                    return userNames;

                } else {
                    return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroups(groups);
                }

            } else {
                List<String> userNames = new ArrayList<String>();
                for (User user : getUsers()) {
                    userNames.add(user.getFullName());
                }

                return userNames;
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param groupName
     * @return
     * @throws BusinessException
     */
    public void addGroup(String groupName) throws BusinessException {

        try {
            VletAgentClient client = CoreUtil.getVletAgentClient();
            client.createDirectory(Server.getInstance().getDataManagerGroupsHome(),
                    groupName.replaceAll(" ", "_"));

            CoreDAOFactory.getDAOFactory().getGroupDAO().add(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param groupName
     * @throws BusinessException
     */
    public void removeGroup(String groupName) throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getGroupDAO().remove(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param oldName
     * @param newName
     * @return
     * @throws BusinessException 
     */
    public void updateGroup(String oldName, String newName) throws BusinessException {

        try {
            VletAgentClient client = CoreUtil.getVletAgentClient();
            client.rename(Server.getInstance().getDataManagerGroupsHome()
                    + "/" + oldName.replaceAll(" ", "_"), newName.replaceAll(" ", "_"));

            CoreDAOFactory.getDAOFactory().getGroupDAO().update(oldName, newName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<String> getGroups() throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @return
     * @throws BusinessException 
     */
    public Map<String, CoreConstants.ROLE> getUserGroups(String email) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserGroups(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @return
     * @throws BusinessException 
     */
    public List<String> getUserGroupsName(String email) throws BusinessException {

        try {
            return new ArrayList<String>(CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUserGroups(email).keySet());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param groups
     * @throws BusinessException 
     */
    public void setUserGroups(String email, Map<String, CoreConstants.ROLE> groups)
            throws BusinessException {

        try {
            CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().setUserGroups(email, groups);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @return 
     */
    public User getUserData(String email) throws BusinessException {

        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO().getUser(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @throws BusinessException 
     */
    public void updateUser(User user) throws BusinessException {

        try {

            String oldFolder = user.getFolder();
            user.setFolder(user.getFirstName().toLowerCase() + "_"
                    + user.getLastName().toLowerCase());

            CoreDAOFactory.getDAOFactory().getUserDAO().update(user);

            if (!oldFolder.equals(user.getFolder())) {
                VletAgentClient client = CoreUtil.getVletAgentClient();
                client.rename(
                        Server.getInstance().getDataManagerUsersHome() + "/" + oldFolder,
                        Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder());

                client.rename(
                        Server.getInstance().getDataManagerUsersHome() + "/" + oldFolder + "_" + CoreConstants.FOLDER_TRASH,
                        Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder() + "_" + CoreConstants.FOLDER_TRASH);
            }

        } catch (VletAgentClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param currentPassword
     * @param newPassword
     * @throws BusinessException 
     */
    public void updateUserPassword(String email, String currentPassword,
            String newPassword) throws BusinessException {

        try {
            currentPassword = MD5.get(currentPassword);
            newPassword = MD5.get(newPassword);
            CoreDAOFactory.getDAOFactory().getUserDAO().updatePassword(
                    email, currentPassword, newPassword);

        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
