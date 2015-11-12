/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api.soap;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.Execution.ExecutionStatus;
import fr.insalyon.creatis.vip.api.bean.GlobalProperties;
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import fr.insalyon.creatis.vip.api.bean.Response;
import fr.insalyon.creatis.vip.api.bean.pairs.PairOfPipelineAndBooleanLists;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.api.business.AuthenticationBusiness;
import fr.insalyon.creatis.vip.api.business.ExecutionBusiness;
import fr.insalyon.creatis.vip.api.business.GlobalPropertiesBusiness;
import fr.insalyon.creatis.vip.api.business.PipelineBusiness;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
@WebService(serviceName = "api", targetNamespace = "http://france-life-imaging.fr/api")
public class Carmin {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Carmin.class);

    @Resource
    private WebServiceContext wsContext;

    /**
     * Execution
     *
     * @param executionId
     * @return
     */
    @WebMethod(operationName = "getExecution")
    public @XmlElement(required = true)
    Response getExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getExecution", executionId);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ExecutionBusiness eb = new ExecutionBusiness(wsContext);
            eb.checkIfUserCanAccessExecution(executionId);
            Execution e = eb.getExecution(executionId);
            r = new Response(0, ApiUtils.getMessage(eb), e);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "updateExecution")
    public @XmlElement(required = true)
    Response updateExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @XmlElement(required = true) @WebParam(name = "keyValuePairs") ArrayList<StringKeyValuePair> keyValuePairs) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("updateExecution", executionId, keyValuePairs);
            ApiUtils.throwIfNull(executionId, "Execution id"); 
            ApiUtils.throwIfNull(keyValuePairs, "Values");
            ExecutionBusiness eb = new ExecutionBusiness(wsContext);
            eb.checkIfUserCanAccessExecution(executionId);
            eb.updateExecution(executionId, keyValuePairs);
            r = new Response(0, ApiUtils.getMessage(eb), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "initExecution")
    public @XmlElement(required = true)
    Response initExecution(
            @XmlElement(required = true) @WebParam(name = "pipelineId") String pipelineId,
            @XmlElement(required = true) @WebParam(name = "inputValue") ArrayList<StringKeyParameterValuePair> inputValues,
            @WebParam(name = "timeout") Integer timeout,
            @WebParam(name = "executionName") String executionName,
            @WebParam(name = "studyId") String studyId,
            @WebParam(name = "playExecution") Boolean playExecution) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("initExecution", pipelineId, inputValues, timeout, executionName, studyId, playExecution);
            ApiUtils.throwIfNull(pipelineId, "Pipeline id");
            ApiUtils.throwIfNull(inputValues, "Input values");
            if (playExecution == null || !playExecution) {
                throw new ApiException("Cannot initialize an execution in VIP without starting it (we know it breaks the specification). Please set playExecution to 'true'");
            }
            if(executionName == null)
                executionName = "Untitled";
            PipelineBusiness pb = new PipelineBusiness(wsContext);
            pb.checkIfUserCanAccessPipeline(pipelineId);
            ExecutionBusiness eb = new ExecutionBusiness(pb);
            String id = eb.initExecution(pipelineId, inputValues, timeout, executionName, studyId, playExecution);
            r = new Response(0, ApiUtils.getMessage(eb), id);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "playExecution")
    public @XmlElement(required = true)
    Response playExecution(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("playExecution", executionId);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ExecutionBusiness eb = new ExecutionBusiness(wsContext);
            eb.checkIfUserCanAccessExecution(executionId);
            ExecutionStatus s = eb.playExecution(executionId);
            r = new Response(0, ApiUtils.getMessage(eb), s);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "killExecution")
    public @XmlElement(required = true)
    Response killExecution(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("killExecution", executionId);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ExecutionBusiness eb = new ExecutionBusiness(wsContext);
            eb.checkIfUserCanAccessExecution(executionId);
            eb.killExecution(executionId);
            r = new Response(0, ApiUtils.getMessage(eb), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "deleteExecution")
    public @XmlElement(required = true)
    Response deleteExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @XmlElement(required = true) @WebParam(name = "deleteFiles") Boolean deleteFiles) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("deleteExecution", executionId, deleteFiles);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ExecutionBusiness eb = new ExecutionBusiness(wsContext);
            eb.checkIfUserCanAccessExecution(executionId);
            eb.deleteExecution(executionId, deleteFiles);
            r = new Response(0, ApiUtils.getMessage(eb), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "getExecutionResults")
    public @XmlElement(required = true)
    Response getExecutionResults(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @WebParam(name = "protocol") String protocol) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getExecutionResults", executionId, protocol);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ApiUtils.throwIfNull(protocol, "Protocol");
            ExecutionBusiness eb = new ExecutionBusiness(wsContext);
            eb.checkIfUserCanAccessExecution(executionId);
            List<URL> results = eb.getExecutionResults(executionId, protocol);
            r = new Response(0, ApiUtils.getMessage(eb), results);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    /**
     * Global Properties
     * @return 
     */
    @WebMethod(operationName = "getGlobalProperties")
    public @XmlElement(required = true)
    Response getGlobalProperties() {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getGlobalProperties");
            GlobalPropertiesBusiness bpb = new GlobalPropertiesBusiness(wsContext);
            GlobalProperties gp = bpb.getGlobalProperties();
            r = new Response(0, ApiUtils.getMessage(bpb), gp);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        }
        return r;
    }

    /**
     * Pipeline
     * @param pipelineId
     * @return 
     */
    @WebMethod(operationName = "getPipeline")
    public @XmlElement(required = true)
    Response getPipeline(@XmlElement(required = true) @WebParam(name = "pipelineId") String pipelineId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getPipeline", pipelineId); 
            ApiUtils.throwIfNull(pipelineId, "Pipeline id");
            PipelineBusiness pb = new PipelineBusiness(wsContext);
            pb.checkIfUserCanAccessPipeline(pipelineId);
            Pipeline p = pb.getPipeline(pipelineId);
            r = new Response(0, ApiUtils.getMessage(pb), p);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "listPipelines")
    public @XmlElement(required = true)
    Response listPipelines(@WebParam(name = "studyIdentifier") String studyIdentifier) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("listPipelines", studyIdentifier);   
            PipelineBusiness pb = new PipelineBusiness(wsContext);
            PairOfPipelineAndBooleanLists pipelinesWithRights = pb.listPipelines(studyIdentifier);
            r = new Response(0, ApiUtils.getMessage(pb), pipelinesWithRights);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    /**
     * Authentication
     * @param userName
     * @param password
     * @return 
     */
    @WebMethod(operationName = "authenticateSession")
    public @XmlElement(required = true)
    Response authenticateSession(@XmlElement(required = true) @WebParam(name = "userName") String userName, @XmlElement(required = true) @WebParam(name = "password") String password) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("authenticateSession", userName, "*****");
            ApiUtils.throwIfNull(userName, "User name");
            ApiUtils.throwIfNull(password, "Password");
            AuthenticationBusiness ab = new AuthenticationBusiness(wsContext);
            ab.authenticateSession(userName, password);
            r = new Response(0, ApiUtils.getMessage(ab), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "authenticateHTTP")
    public @XmlElement(required = true)
    Response authenticateHTTP(@XmlElement(required = true) @WebParam(name = "userName") String userName) {
        Response r;
        try {
            AuthenticationBusiness ab = new AuthenticationBusiness(wsContext);
            ab.authenticateHTTP(userName);
            r = new Response(0, ApiUtils.getMessage(ab), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "logout")
    public @XmlElement(required = true)
    Response logout() {
        Response r;
        try {
            ApiUtils.methodInvocationLog("logout");
            AuthenticationBusiness ab = new AuthenticationBusiness(wsContext);
            ab.logout();
            r = new Response(0, ApiUtils.getMessage(ab), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

}
