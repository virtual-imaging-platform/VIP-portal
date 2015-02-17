/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.rpc.ConfigurationServiceImpl;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
public class AuthenticationBusiness {

    private static final Logger logger = Logger.getLogger(AuthenticationBusiness.class);
    private final ConfigurationServiceImpl csi;

    public AuthenticationBusiness() {
        csi = new ConfigurationServiceImpl();
    }

    public static Logger getLogger() {
        return logger;
    }

    public ConfigurationServiceImpl getCsi() {
        return csi;
    }

    public User authenticate(HttpServletRequest req, fr.insalyon.creatis.vip.api.bean.User user) throws ApiException {
        throw new ApiException("Not implemented");
//        String email = user.getUserName();
//        String password = user.getPassword();
//        getLogger().info("API authenticating " + email + " " + password);
//        //getCsi().setSession(req.getSession()); 
//        try {
//            User vipUser = getCsi().signin(email, password);
//            return vipUser;
//        } catch (CoreException ex) {
//            throw new ApiException(ex);
//        }

    }

}
