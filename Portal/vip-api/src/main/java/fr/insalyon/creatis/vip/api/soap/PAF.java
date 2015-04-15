/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.soap;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.Execution.ExecutionStatus;
import fr.insalyon.creatis.vip.api.bean.GlobalProperties;
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import fr.insalyon.creatis.vip.api.bean.Response;
import fr.insalyon.creatis.vip.api.bean.pairs.PipelineKeyBooleanValuePair;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.AuthenticationBusiness;
import fr.insalyon.creatis.vip.api.business.ExecutionBusiness;
import fr.insalyon.creatis.vip.api.business.GlobalPropertiesBusiness;
import fr.insalyon.creatis.vip.api.business.PipelineBusiness;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Tristan Glatard
 */
@WebService(serviceName = "PAF", targetNamespace="http://france-life-imaging.fr/api")
public class PAF {

    @Resource
    private WebServiceContext wsContext;

    /**
     * Execution
     */
    @WebMethod(operationName = "getExecution")
    public @XmlElement(required = true)
    Response getExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r = null;
        try {
            Execution e = ExecutionBusiness.getExecution(executionId);
            r = new Response(0, "OK", e);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

    @WebMethod(operationName = "updateExecution")
    public @XmlElement(required = true)
    Response updateExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @XmlElement(required = true) @WebParam(name = "keyValuePairs") ArrayList<StringKeyValuePair> keyValuePairs) {
        Response r = null;
        try {
            ExecutionBusiness.updateExecution(executionId, keyValuePairs);
            r = new Response(0, "OK", null);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

    @WebMethod(operationName = "initExecution")
    public @XmlElement(required = true)
    Response initExecution(
            @XmlElement(required = true) @WebParam(name = "pipelineId") String pipelineId,
            @XmlElement(required = true) @WebParam(name = "inputValues") ArrayList<StringKeyParameterValuePair> inputValues,
            @WebParam(name = "timeout") Integer timeout,
            @WebParam(name = "executionName") String executionName,
            @WebParam(name = "studyId") String studyId,
            @WebParam(name = "playExecution") Boolean playExecution) {
        Response r = null;
        try {
            String id = ExecutionBusiness.initExecution(pipelineId, inputValues, timeout, executionName, studyId, playExecution);
            r = new Response(0, "OK", id);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

    @WebMethod(operationName = "playExecution")
    public @XmlElement(required = true)
    Response playExecution(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r = null;
        try {
            ExecutionStatus s = ExecutionBusiness.playExecution(executionId);
            r = new Response(0, "OK", s);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

    @WebMethod(operationName = "killExecution")
    public @XmlElement(required = true)
    Response killExecution(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r = null;
        try {
            ExecutionBusiness.killExecution(executionId);
            r = new Response(0, "OK", null);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

    @WebMethod(operationName = "deleteExecution")
    public @XmlElement(required = true)
    Response deleteExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @XmlElement(required = true) @WebParam(name = "deleteFiles") Boolean deleteFiles) {
        Response r = null;
        try {
            ExecutionBusiness.deleteExecution(executionId, deleteFiles);
            r = new Response(0, "OK", null);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

    @WebMethod(operationName = "getExecutionResults")
    public @XmlElement(required = true)
    Response getExecutionResults(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @WebParam(name = "protocol") String protocol) {
        Response r = null;
        try {
            ArrayList<URL> results = ExecutionBusiness.getExecutionResults(executionId, protocol);
            r = new Response(0, "OK", results);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }
    
    /**
     * Global Properties
     */
    @WebMethod(operationName = "getGlobalProperties")
    public @XmlElement(required = true)
    Response getGlobalProperties() {
        Response r = null;
        try {
            GlobalProperties gp = GlobalPropertiesBusiness.getGlobalProperties();
            r = new Response(0, "OK", gp);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }
    
    /**
     * Pipeline
     */
    @WebMethod(operationName = "getPipeline")
    public @XmlElement(required = true)
    Response getPipeline(@XmlElement(required = true) @WebParam(name = "pipelineId") String pipelineId) {
        Response r = null;
        try {
            Pipeline p = PipelineBusiness.getPipeline(pipelineId);
            r = new Response(0, "OK", p);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }
    
    @WebMethod(operationName = "listPipelines")
    public @XmlElement(required = true)
    Response listPipelines(@WebParam(name = "studyIdentifier") String studyIdentifier) {
        Response r = null;
        try {
            ArrayList<PipelineKeyBooleanValuePair> pipelines = PipelineBusiness.listPipelines(studyIdentifier);
            r = new Response(0, "OK", pipelines);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }
    
    /**
     * Authentication
     */
    @WebMethod(operationName = "authenticateSession")
    public @XmlElement(required = true)
    Response authenticateSession(@XmlElement(required = true) @WebParam(name = "userName") String userName, @XmlElement(required = true) @WebParam(name = "password") String password) {
        Response r = null;
        try {
            AuthenticationBusiness.authenticateSession(userName, password);
            r = new Response(0, "OK", null);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }
    
    @WebMethod(operationName = "authenticateHTTP")
    public @XmlElement(required = true)
    Response authenticateHTTP(@XmlElement(required = true) @WebParam(name = "userName") String userName) {
        Response r = null;
        try {
            AuthenticationBusiness.authenticateHTTP(userName);
            r = new Response(0, "OK", null);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }
    
    @WebMethod(operationName = "logout")
    public @XmlElement(required = true)
    Response logout() {
        Response r = null;
        try {
            AuthenticationBusiness.logout();
            r = new Response(0, "OK", null);
        } catch (ApiException ex) {
            Logger.getLogger(PAF.class.getName()).log(Level.SEVERE, null, ex);
            r = new Response(1, ex.getMessage(), null);
        } finally {
            return r;
        }
    }

}
