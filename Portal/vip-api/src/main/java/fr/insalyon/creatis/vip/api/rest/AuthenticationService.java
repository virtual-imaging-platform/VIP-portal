/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.rest;

import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.AuthenticationBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
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

public class AuthenticationService {

    @POST
    public Response authenticate(@Context HttpServletRequest req, @FormParam("email") String email, @FormParam("password") String password) {
        User user = null;
        try {
            user = new AuthenticationBusiness().authenticate(req, new fr.insalyon.creatis.vip.api.bean.User(email, password)); //throws an exception if authentication fails
        } catch (ApiException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build(); // we should separate unauthorized from internal server error
        }

        try {
            return Response.
                    status(Response.Status.OK)
                    .cookie(new NewCookie(CoreConstants.COOKIES_USER, URLEncoder.encode(user.getEmail(), "UTF-8")))
                    .cookie(new NewCookie(CoreConstants.COOKIES_SESSION, user.getSession()))
                    .build();
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
