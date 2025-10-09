package fr.insalyon.creatis.vip.api.controller.processing;

import fr.insalyon.creatis.vip.api.business.ExecutionBusiness;
import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.model.DeleteExecutionConfiguration;
import fr.insalyon.creatis.vip.api.model.Execution;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/executions")
public class ExecutionController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ExecutionBusiness executionBusiness;

    @Autowired
    public ExecutionController(ExecutionBusiness executionBusiness) {
        this.executionBusiness = executionBusiness;
    }

    @RequestMapping
    public List<Execution> listExecutions(
            @RequestParam(required = false) String studyIdentifier,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit
    ) throws ApiException {
        logMethodInvocation(logger, "listExecutions", studyIdentifier, offset, limit);
        if (studyIdentifier != null) {
            logger.warn("studyIdentifier not supportet yet in listExecutions");
            throw new ApiException("studyIdentifier not supportet yet");
        }
        if (offset != null) {
            logger.warn("offset not supportet yet in listExecutions");
            throw new ApiException("offset not supported yet");
        }
        int executionMaxNb = (int)server.getCarminDefaultLimitListExecution();
        if (limit == null) limit = executionMaxNb;
        if (limit > executionMaxNb) {
            logger.warn("limit parameter too high {}", limit);
            throw new ApiException("limit parameter too high");
        }
        return executionBusiness.listExecutions(limit);
    }

    @RequestMapping("examples")
    public List<Execution> listExecutions() throws ApiException {
        logMethodInvocation(logger, "listExamples");
        return executionBusiness.listExamples();
    }

    @RequestMapping("examples/{exampleId}")
    public Execution getExample(@PathVariable String exampleId) throws VipException {
        logMethodInvocation(logger, "getExample", exampleId);
        return executionBusiness.getExecution(exampleId, false);
    }

    @RequestMapping(value = "count", produces = "text/plain;charset=UTF-8")
    public String countExecutions(
            @RequestParam(required = false) String studyIdentifier)
            throws ApiException {
        logMethodInvocation(logger, "countExecutions");
        if (studyIdentifier != null) {
            logger.warn("studyIdentifier not supportet yet in countExecutions");
            throw new ApiException("studyIdentifier not supportet yet");
        }
        return String.valueOf(executionBusiness.countExecutions());
    }

    @RequestMapping("/{executionId}")
    public Execution getExecution(@PathVariable String executionId)
            throws VipException {
        logMethodInvocation(logger, "getExecution", executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);

        return executionBusiness.getExecution(executionId, false);
    }
    
    @RequestMapping("/{executionId}/summary")
    public Execution getExecutionSummary(@PathVariable String executionId)
            throws VipException {
        logMethodInvocation(logger, "getExecutionSummary", executionId);
        return executionBusiness.getExecution(executionId, true);
    }
    

    @RequestMapping(value = "/{executionId}", method = RequestMethod.PUT)
    public Execution updateExecution(
            @PathVariable String executionId,
            @RequestBody @Valid Execution execution) throws VipException {
        logMethodInvocation(logger, "updateExecution", executionId);
        execution.setIdentifier(executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        executionBusiness.updateExecution(execution);
        return executionBusiness.getExecution(executionId, false);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Execution initExecution(@RequestBody @Valid Execution execution)
            throws VipException {
        logMethodInvocation(logger, "initExecution", execution);
        String execId = executionBusiness.initExecution(execution);
        return executionBusiness.getExecution(execId, false);
    }

    @RequestMapping("/{executionId}/results")
    public List<PathProperties> getExecutionResults(
            @PathVariable String executionId) throws ApiException {
        logMethodInvocation(logger, "getExecutionResults", executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        return executionBusiness.getExecutionResultsPaths(executionId);
    }

    @RequestMapping(value = "/{executionId}/stdout", produces = "text/plain;charset=UTF-8")
    public String getStdout(@PathVariable String executionId) throws ApiException {
        logMethodInvocation(logger, "getStdout", executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        return executionBusiness.getLog(executionId, "out");
    }

    @RequestMapping(value = "/{executionId}/jobs/{invocationId}/stdout", produces = "text/plain;charset=UTF-8")
    public String getJobStdout(@PathVariable String executionId, @PathVariable Integer invocationId) throws ApiException {
        logMethodInvocation(logger, "getJobStdout", executionId, invocationId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        return executionBusiness.getLog(executionId, invocationId, "out");
    }

    @RequestMapping(value= "/{executionId}/stderr", produces = "text/plain;charset=UTF-8")
    public String getStderr(@PathVariable String executionId) throws ApiException {
        logMethodInvocation(logger, "getStderr", executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        return executionBusiness.getLog(executionId, "err");
    }

    @RequestMapping(value = "/{executionId}/jobs/{invocationId}/stderr", produces = "text/plain;charset=UTF-8")
    public String getJobStderr(@PathVariable String executionId,  @PathVariable Integer invocationId) throws ApiException {
        logMethodInvocation(logger, "getJobStderr", executionId, invocationId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        return executionBusiness.getLog(executionId, invocationId, "err");
    }

    @RequestMapping(value = "/{executionId}/play", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void playExecution(@PathVariable String executionId) throws ApiException {
        logMethodInvocation(logger, "playExecution", executionId);
        logger.warn("playExecution should not be used");
        throw new ApiException(ApiError.NOT_IMPLEMENTED, "playExecution");
    }

    @RequestMapping(value = "/{executionId}/kill", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void killExecution(@PathVariable String executionId) throws ApiException {
        logMethodInvocation(logger, "killExecution", executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        executionBusiness.killExecution(executionId);
    }

    @RequestMapping(value = "/{executionId}/delete", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExecution(@PathVariable String executionId,
                                @RequestBody @Valid DeleteExecutionConfiguration delConfig) throws ApiException {
        logMethodInvocation(logger, "deleteExecution", executionId);
        executionBusiness.checkIfUserCanAccessExecution(executionId);
        executionBusiness.deleteExecution(executionId, delConfig.getDeleteFiles());
    }
}
