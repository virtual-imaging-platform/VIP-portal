/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Tristan Glatard
 */
public class ApiBusiness {

    private final static Logger logger = Logger.getLogger(ApiBusiness.class);

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ApiBusiness(WebServiceContext wsContext) throws ApiException {

        try {

            // set logging properties and DB connection
            PropertyConfigurator.configure(ConfigurationBusiness.class.getClassLoader().getResource("vipLog4j.properties"));
            logger.info("Calling API method");
            PlatformConnection.getInstance();

            //set request and response
            MessageContext mc = wsContext.getMessageContext();
            request = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
            response = (HttpServletResponse) mc.get(MessageContext.SERVLET_RESPONSE);
            
            // set session
            session = ((javax.servlet.http.HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();
            if (session == null) {
                throw new ApiException("No session in WebServiceContext");
            }
        } catch (DAOException ex) {
            throw new ApiException(ex);
        }

    }

    public HttpSession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }


}
