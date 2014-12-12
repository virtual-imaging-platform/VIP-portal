/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tristan Glatard
 */
@Path("/auth")

public class Auth extends APIClass {

    @POST
    public Response authenticate(@Context HttpServletRequest req, @FormParam("email") String email, @FormParam("password") String password) {
        try {
            
            getLogger().info("Authenticating "+email+" "+password);
            this.getConfigurationServiceImpl().setSession(req.getSession());
            User user = getConfigurationServiceImpl().signin(email, password);
       
            try {
                return Response.
                        status(Response.Status.OK)
                        .cookie(new NewCookie(CoreConstants.COOKIES_USER,URLEncoder.encode(user.getEmail(), "UTF-8")))
                        .cookie(new NewCookie(CoreConstants.COOKIES_SESSION,user.getSession()))
                        .build();
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
         } catch (CoreException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }    

    
}
