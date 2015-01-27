/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.rest;

import fr.insalyon.creatis.vip.api.bean.Study;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.StudyBusiness;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tristan Glatard
 */
@Path("/studies")
/**
 * A study in the PAF API is a group in VIP.
 */
public class StudyService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStudies(@Context HttpServletRequest req) throws ApiException {
       throw new ApiException("Not implemented");
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStudy(@PathParam("name") Study study, @Context HttpServletRequest req) throws ApiException {
       throw new ApiException("Not implemented");

    }
    
    
    
}
