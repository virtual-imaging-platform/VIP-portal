/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.soap;

import fr.insalyon.creatis.vip.api.bean.Study;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.AuthenticationBusiness;
import fr.insalyon.creatis.vip.api.business.StudyBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author glatard
 */
@WebService(serviceName = "ApiWebService")
public class ApiWebService {

    @Resource
    private WebServiceContext wsContext;

    @WebMethod(operationName = "authenticate")
    public void authenticate(@WebParam(name = "email") String email, @WebParam(name = "password") String password) throws ApiException {

        User user = null;
        user = new AuthenticationBusiness().authenticate(getRequest(), email, password); //throws an exception if authentication fails

        // user is authenticated
        HttpSession session = getRequest().getSession();
        try {
            session.setAttribute(CoreConstants.COOKIES_USER, URLEncoder.encode(user.getEmail(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }
        session.setAttribute(CoreConstants.COOKIES_SESSION, user.getSession());

    }

    
    //Studies
    @WebMethod(operationName = "list_studies")
    public List<Study> getStudies() throws ApiException {
        return new StudyBusiness().getStudies(getRequest());
    }

    @WebMethod(operationName = "list_study")
    public Study getStudy(@WebParam(name = "studyName") Study study) throws ApiException {
        return (new StudyBusiness()).getStudy(study, getRequest());
    }
    
     private HttpServletRequest getRequest() {
        MessageContext mc = wsContext.getMessageContext();
        return ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST));
    }

}
