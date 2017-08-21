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
package fr.insalyon.creatis.vip.api.rest.controller.processing;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.business.*;
import fr.insalyon.creatis.vip.api.exception.NotImplementedException;
import fr.insalyon.creatis.vip.api.rest.RestApiBusiness;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static fr.insalyon.creatis.vip.api.CarminProperties.DEFAULT_LIMIT_LIST_EXECUTION;
import static fr.insalyon.creatis.vip.core.client.view.util.CountryCode.li;

/**
 * Created by abonnet on 7/13/16.
 */
@RestController
@RequestMapping("/executions")
public class ExecutionControler {

    public static final Logger logger = Logger.getLogger(ExecutionControler.class);

    @Autowired
    private WorkflowBusiness workflowBusiness;
    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private ClassBusiness classBusiness;
    @Autowired
    private SimulationBusiness simulationBusiness;
    @Autowired
    private ConfigurationBusiness configurationBusiness;
    @Autowired
    private TransferPoolBusiness transferPoolBusiness;

    @Autowired
    private Environment environment;

    @Autowired
    private RestApiBusiness restApiBusiness;

    // although the controller is a singleton, these are proxies that always point on the current request
    @Autowired
    HttpServletRequest httpServletRequest;

    private PipelineBusiness buildPipelineBusiness(ApiContext apiContext) {
        return new PipelineBusiness(apiContext,
                workflowBusiness, applicationBusiness, classBusiness);
    }


    private ExecutionBusiness buildExecutionBusiness(ApiContext apiContext) {
        return buildExecutionBusiness(apiContext, null);
    }

    private ExecutionBusiness buildExecutionBusiness(ApiContext apiContext,
                                                     PipelineBusiness pipelineBusiness) {
        if (pipelineBusiness == null) {pipelineBusiness = new PipelineBusiness(apiContext, workflowBusiness,
                applicationBusiness, classBusiness);
        }
        return new ExecutionBusiness(apiContext,
                simulationBusiness, workflowBusiness, configurationBusiness, applicationBusiness,
                pipelineBusiness, transferPoolBusiness);
    }

    @RequestMapping
    public Execution[] listExecutions(
            @RequestParam(required = false) String studyIdentifier,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit
    ) throws ApiException {
        ApiUtils.methodInvocationLog("listExecutions", studyIdentifier, offset, limit);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        if (studyIdentifier != null) throw new ApiException("studyIdentifier not supportet yet");
        if (offset != null) throw new ApiException("offset not supported yet");
        int executionMaxNb = environment.getProperty(DEFAULT_LIMIT_LIST_EXECUTION, Integer.class);
        if (limit == null) limit = executionMaxNb;
        if (limit > executionMaxNb) throw new ApiException("limit parameter too high");
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        return eb.listExecutions(limit);
    }

    @RequestMapping("/{executionId}")
    public Execution getExecution(@PathVariable String executionId) throws ApiException {
        ApiUtils.methodInvocationLog("getExecution", executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        return eb.getExecution(executionId,false);
    }

    @RequestMapping(value = "/{executionId}", method = RequestMethod.PUT)
    public Execution updateExecution(@PathVariable String executionId,
                                     @RequestBody @Valid Execution execution) throws ApiException {
        ApiUtils.methodInvocationLog("updateExecution", executionId);
        execution.setIdentifier(executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        eb.updateExecution(execution);
        return eb.getExecution(executionId,false);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Execution initExecution(@RequestBody @Valid Execution execution) throws ApiException {
        ApiUtils.methodInvocationLog("initExecution", execution);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        PipelineBusiness pb = buildPipelineBusiness(apiContext);
        pb.checkIfUserCanAccessPipeline(execution.getPipelineIdentifier());
        ExecutionBusiness executionBusiness = buildExecutionBusiness(apiContext, pb);
        String execId = executionBusiness.initExecution(execution);
        return executionBusiness.getExecution(execId,false);
    }

    @RequestMapping("/{executionId}/results")
    public String[] getExecutionResults(@PathVariable String executionId,
                                        @RequestParam(defaultValue = "https") String protocol) throws ApiException {
        ApiUtils.methodInvocationLog("getExecutionResults", executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        return eb.getExecutionResults(executionId, protocol, true);
    }

    @RequestMapping(value = "/{executionId}/stdout", produces = "text/plain;charset=UTF-8")
    public String getStdout(@PathVariable String executionId) throws ApiException {
        ApiUtils.methodInvocationLog("getStdout", executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        return eb.getStdOut(executionId);
    }

    @RequestMapping(value= "/{executionId}/stderr", produces = "text/plain;charset=UTF-8")
    public String getStderr(@PathVariable String executionId) throws ApiException {
        ApiUtils.methodInvocationLog("getStderr", executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        return eb.getStdErr(executionId);
    }

    @RequestMapping(value = "/{executionId}/play", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void playExecution(@PathVariable String executionId) throws ApiException {
        ApiUtils.methodInvocationLog("playExecution", executionId);
        throw new NotImplementedException("Executions are started on creation");
    }

    @RequestMapping(value = "/{executionId}/kill", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void killExecution(@PathVariable String executionId) throws ApiException {
        ApiUtils.methodInvocationLog("killExecution", executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        eb.killExecution(executionId);
    }

    @RequestMapping(value = "/{executionId}/delete", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExecution(@PathVariable String executionId,
                                @RequestBody @Valid DeleteExecutionConfiguration delConfig) throws ApiException {
        ApiUtils.methodInvocationLog("deleteExecution", executionId);
        ApiContext apiContext = restApiBusiness.getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = buildExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        eb.deleteExecution(executionId, delConfig.getDeleteFiles());
    }
}
