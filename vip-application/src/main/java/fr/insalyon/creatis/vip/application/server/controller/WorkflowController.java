package fr.insalyon.creatis.vip.application.server.controller;

import java.util.Date;

import fr.insalyon.creatis.vip.application.models.Workflow;
import fr.insalyon.creatis.vip.application.server.business.ListWorkflowsBusiness;
import fr.insalyon.creatis.vip.application.server.business.util.WorkflowLaunchBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/**
 * Permissions:
 * - To be defined
 */
@RestController()
@RequestMapping("/workflows")
public class WorkflowController {

    private final WorkflowLaunchBusiness workflowLaunchBusiness;
    private final ListWorkflowsBusiness listWorkflowsBusiness;

    @Autowired
    public WorkflowController(WorkflowLaunchBusiness workflowLaunchBusiness, ListWorkflowsBusiness listWorkflowsBusiness) {
        this.workflowLaunchBusiness = workflowLaunchBusiness;
        this.listWorkflowsBusiness = listWorkflowsBusiness;
    }

    // TODO : add filters : status, user, application, start date, end date
    @GetMapping
    public PrecisePage<Workflow> list(
            @RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity,
            @RequestParam(defaultValue = "true") boolean refreshed,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws VipException {
        PrecisePage<Workflow> workflows = listWorkflowsBusiness.searchOwnWorkflowsPaginated(
                offset, quantity, search, status, startDate, endDate);
        if (refreshed) {
            listWorkflowsBusiness.refreshRunningWorkflows(workflows.data);
        }
        return workflows;
    }

    @GetMapping(value = "{wid}")
    public Workflow get(@PathVariable String wid) throws VipException {
        Workflow w = listWorkflowsBusiness.getRefreshedWorkflow(wid);

        if (w == null) {
            throw new VipException(DefaultError.NOT_FOUND, Workflow.class.getSimpleName(), wid);
        } else {
            return w;
        }
    }

    /**
     * used to update status, to kill or clean
     */
    @PutMapping(value = "{wid}")
    public Workflow updateStatus(@PathVariable String wid, @RequestBody @Valid Workflow workflow)
            throws VipException {
        if ( ! wid.equals(workflow.getID())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, wid, "Workflow id do not match!");
        } else {
            return null;
            //return workflowBusiness.updateStatus(workflow);
        }
    }

    @PostMapping
    public Workflow launch(@RequestBody @Valid Workflow workflow) throws VipException {
        return workflowLaunchBusiness.launch(workflow);
    }
}
