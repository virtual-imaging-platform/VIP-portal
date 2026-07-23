package fr.insalyon.creatis.vip.application.server.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.application.models.Workflow;
import fr.insalyon.creatis.vip.application.server.business.WorkflowLogBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowLogBusiness.JobLogType;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflows")
public class WorkflowLogController {

    private final WorkflowLogBusiness workflowLogBusiness;

    @Autowired
    public WorkflowLogController(WorkflowLogBusiness workflowLogBusiness) {
        this.workflowLogBusiness = workflowLogBusiness;
    }

    @GetMapping(value = "{wid}/stdout", produces = MediaType.TEXT_PLAIN_VALUE)
    public String readStdout(@PathVariable String wid) throws VipException {
        requireWorkflowExists(wid);
        return workflowLogBusiness.readExecutionStdout(wid);
    }

    @GetMapping(value = "{wid}/stderr", produces = MediaType.TEXT_PLAIN_VALUE)
    public String readStderr(@PathVariable String wid) throws VipException {
        requireWorkflowExists(wid);
        return workflowLogBusiness.readExecutionStderr(wid);
    }

    @GetMapping("{wid}/jobs")
    public List<Task> listJobs(@PathVariable String wid) throws VipException {
        requireWorkflowExists(wid);
        return workflowLogBusiness.listJobs(wid);
    }

    @GetMapping(value = "{wid}/jobs/{invocationId}/logs/{type}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String readJobLog(
            @PathVariable String wid,
            @PathVariable Integer invocationId,
            @PathVariable String type) throws VipException {
        if (invocationId == null || invocationId <= 0) {
            throw new VipException(DefaultError.BAD_PARAMETERS, "Invalid invocationId");
        }
        requireWorkflowExists(wid);
        JobLogType logType = parseLogType(type);
        Task task = workflowLogBusiness.getTaskByInvocationId(wid, invocationId);
        return workflowLogBusiness.readTaskLog(task, wid, logType);
    }

    private Workflow requireWorkflowExists(String wid) throws VipException {
        Workflow w = workflowLogBusiness.getWorkflow(wid);
        if (w == null) {
            throw new VipException(DefaultError.NOT_FOUND, Workflow.class.getSimpleName(), wid);
        }
        return w;
    }

    private JobLogType parseLogType(String type) throws VipException {
        try {
            return JobLogType.valueOf(type.toUpperCase().replace('-', '_'));
        } catch (IllegalArgumentException e) {
            String valid = Arrays.stream(JobLogType.values())
                    .map(JobLogType::name)
                    .collect(Collectors.joining(", "));
            throw new VipException(DefaultError.BAD_PARAMETERS,
                    "Invalid log type '" + type + "'. Valid types: " + valid);
        }
    }
}
