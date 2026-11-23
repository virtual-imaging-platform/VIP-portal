package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.InOutData;
import fr.insalyon.creatis.vip.application.models.Workflow;
import fr.insalyon.creatis.vip.application.models.WorkflowInput;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.PageBuilder;
import fr.insalyon.creatis.vip.core.server.business.UserBusiness;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.inter.annotations.VIPExternalSafe;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ListWorkflowsBusiness extends CommonBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WorkflowDAO workflowDAO;
    private final ApplicationDAO applicationDAO;
    private final WorkflowExecutionBusiness workflowExecutionBusiness;
    private final UserBusiness userBusiness;
    private final PageBuilder pageBuilder;
    private final EngineBusiness engineBusiness;
    private final WorkflowBusiness workflowBusiness;

    @Autowired
    public ListWorkflowsBusiness(WorkflowDAO workflowDAO, ApplicationDAO applicationDAO,
                                 WorkflowExecutionBusiness workflowExecutionBusiness, UserBusiness userBusiness,
                                 PageBuilder pageBuilder, EngineBusiness engineBusiness,
                                 WorkflowBusiness workflowBusiness) {
        this.workflowDAO = workflowDAO;
        this.applicationDAO = applicationDAO;
        this.workflowExecutionBusiness = workflowExecutionBusiness;
        this.userBusiness = userBusiness;
        this.pageBuilder = pageBuilder;
        this.engineBusiness = engineBusiness;
        this.workflowBusiness = workflowBusiness;
    }

    @VIPExternalSafe
    public Workflow getRefreshedWorkflow(String workflowId) throws VipException {
        return getWorkflow(workflowId, true);
    }

    @VIPExternalSafe
    public Workflow getNotRefreshedWorkflow(String workflowId) throws VipException {
        return getWorkflow(workflowId, false);
    }

    private Workflow getWorkflow(String workflowId, boolean refresh) throws VipException {
        Workflow workflow;

        try {
            fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow = workflowDAO.get(workflowId);
            if (dbWorkflow == null) {
                logger.error("Cannot find execution with id {}", workflowId);
                throw new VipException(DefaultError.NOT_FOUND, Workflow.class.getSimpleName(), workflowId);
            }
            workflow = parseDbWorkflow(dbWorkflow);

            if ( ! getUser().isSystemAdministrator()
                    && !Objects.equals(workflow.getUserId(), getUser().getId())
                    && !userHasRole("SERVICE")) {
                logger.error("unauthorized access to workflow {} by {}", workflowId, getUser().getEmail());
                throw new VipException(DefaultError.ACCESS_DENIED);
            }

            if (refresh) {
                refreshRunningWorkflows(List.of(workflow));
            }

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulation {}", workflowId, ex);
            throw new VipException(ex);
        }

        return workflow;
    }

    public Workflow getExample(String workflowId) throws VipException {
        try {
            fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow = workflowDAO.get(workflowId);
            if (dbWorkflow == null) {
                logger.error("Cannot find example with id {}", workflowId);
                throw new VipException(DefaultError.NOT_FOUND, Workflow.class.getSimpleName(), workflowId);
            }
            if ( ! ApplicationConstants.WORKKFLOW_EXAMPLE_TAG.equals(dbWorkflow.getTags())) {
                logger.error("Invalid example id {} : not tag with example", workflowId);
                throw new VipException(DefaultError.NOT_FOUND, "Example", workflowId);
            }
            if ( !fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Completed
                    .equals(dbWorkflow.getStatus())) {
                logger.error("Invalid example status for {} : should be completed but is {}", workflowId, dbWorkflow.getStatus());
                throw new VipException(DefaultError.NOT_FOUND, "Example", workflowId);
            }
            return parseDbWorkflow(dbWorkflow);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulation {}", workflowId, ex);
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public PrecisePage<Workflow> getCurrentUserWorkflowsPaginated(int offset, int quantity, Date lastDate) throws VipException {
        return pageBuilder.doPrecise(offset, quantity, getCurrentUserWorkflows(lastDate));
    }

    @VIPExternalSafe
    public PrecisePage<Workflow> searchOwnWorkflowsPaginated(
            int offset, int quantity, String search, String status,
            Date startDate, Date endDate) throws VipException {
        return pageBuilder.doPrecise(offset, quantity,
                searchOwnWorkflows(null, search, status, startDate, endDate, null));
    }

    @VIPExternalSafe
    public List<Workflow> getAllWorkflows(Date lastDate) throws VipException {
        if ( ! getUser().isSystemAdministrator() ) {
            logger.error("unauthorized access to all workflows by {}", getUser().getEmail());
            throw new VipException(DefaultError.ACCESS_DENIED);
        }
        return getWorkflows(null, lastDate);
    }

    @VIPExternalSafe
    public List<Workflow> getCurrentUserWorkflows(Date lastDate) throws VipException {
        return getWorkflows(getUser(), lastDate);
    }

    private List<Workflow> getWorkflows(User user, Date lastDate) throws VipException {
        try {
            return parseDbWorkflows(workflowDAO.get(user != null ? user.getFullName() : null, lastDate));
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulations for {} since {}", user, lastDate, ex);
            throw new VipException(ex);
        }
    }

    @VIPExternalSafe
    public List<Workflow> searchOwnWorkflows(
            String applicationName, String search, String status,
            Date startDate, Date endDate, String tag) throws VipException {
        fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus wStatus =
                (status != null) ? fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.valueOf(status) : null;

        try {
            List<Workflow> workflows = parseDbWorkflows(
                    workflowDAO.get(getUser().getFullName(), applicationName, wStatus, null, startDate, endDate, tag));

            if (search != null && !search.isEmpty()) {
                String lowerSearch = search.toLowerCase();
                workflows = workflows.stream()
                        .filter(w -> w.getID().toLowerCase().contains(lowerSearch)
                                || (w.getWorkflowName() != null && w.getWorkflowName().toLowerCase().contains(lowerSearch))
                                || (w.getApplicationName() != null && w.getApplicationName().toLowerCase().contains(lowerSearch)))
                        .collect(Collectors.toList());
            }
            return workflows;
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error searching simulations for user {}", getUserEmail(), ex);
            throw new VipException(ex);
        }
    }

    /**
     * Only includes the apps the user is admin of
     */
    @VIPExternalSafe
    public List<Workflow> searchWithAdminRights(
            String userName, String applicationName,
            String status, Date startDate, Date endDate, String tag) throws VipException {
        return searchWithAdminRights(false, userName, applicationName, status, startDate, endDate, tag);
    }

    /**
     * To get the legacy GWT behavior.
     * Search with admin rights and also include the current user workflow
     */
    @VIPExternalSafe
    public List<Workflow> searchWithAdminRightsAndIncludeCurrentUserWorkflows(
            String userName, String applicationName,
            String status, Date startDate, Date endDate, String tag) throws VipException {
        return searchWithAdminRights(true, userName, applicationName, status, startDate, endDate, tag);
    }

    // examples are public, no auth required
    @VIPExternalSafe
    public List<Workflow> getAllExamples() throws VipException {
        try {
            return parseDbWorkflows(workflowDAO.get(null, null,
                    fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Completed,
                    null, null, null, ApplicationConstants.WORKKFLOW_EXAMPLE_TAG));
        } catch (WorkflowsDBDAOException e) {
            logger.error("Error searching example workflows for user {}", getUserEmail(), e);
            throw new VipException(e);
        }
    }

    @VIPExternalSafe
    private List<Workflow> searchWithAdminRights(
            boolean includeCurrentUserWorkflows,
            String userName, String applicationName,
            String status, Date startDate, Date endDate, String tag)
            throws VipException {
        fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus wStatus = (status != null) ? fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.valueOf(status) : null;

        // meaning the current user can see the executions from applications he is admin of
        // also he can only filter on these applications
        // many possibilities depending on profile, userName and applicationName

        try {
            // an admin can search everything
            if (getUser().isSystemAdministrator()) {
                return parseDbWorkflows(workflowDAO.get(userName, applicationName, wStatus, null, startDate, endDate, tag));
            }

            // only admins can filter on user
            if (userName != null) {
                logger.error("{} trying to search workflow for an user {}", getUserEmail(), userName);
                throw new VipException(DefaultError.ACCESS_DENIED);
            }
            List<Application> adminApps = applicationDAO.getApplicationsByGroups(getUser().getAdminGroups());

            // if app is given then filter on it
            if (applicationName != null) {
                if (adminApps.stream().noneMatch(app -> applicationName.equals(app.getName()))) {
                    logger.error("{} trying to filter worfklows with an application he is not an admin {}", getUser().getEmail(), applicationName);
                    throw new VipException(DefaultError.ACCESS_DENIED);
                }
                return parseDbWorkflows(workflowDAO.get(userName, applicationName, wStatus, null, startDate, endDate, tag));
            }

            // else it must be filtered on all the apps the user is admin of
            // can include all his own workflows for the default view (his own + his admin ones)
            List<String> adminAppsNames = adminApps.stream().map(Application::getName).toList();
            if (includeCurrentUserWorkflows) {
                return parseDbWorkflows(workflowDAO.getWithUserOrApplication(List.of(getUser().getFullName()), adminAppsNames, wStatus, null, startDate, endDate, tag));
            } else {
                return parseDbWorkflows(workflowDAO.getWithUserOrApplication(null, adminAppsNames, wStatus, null, startDate, endDate, tag));
            }
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error searching worfklows for user {}", getUserEmail(), ex);
            throw new VipException(ex);
        }
    }

    private List<Workflow> parseDbWorkflows(List<fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow> dbWorkflowList) throws VipException {
        if (dbWorkflowList == null) {
            return Collections.emptyList();
        }
        List<String> usernameList = dbWorkflowList.stream().map(dbworkflow -> dbworkflow.getUsername()).toList();
        List<User> userList = userBusiness.getByFullnames(usernameList);
        // create map of unique names found
        // in case of duplicate, it is removed
        Map<String, User> usersByFullname = userList.stream().collect(Collectors.toMap(
                u -> u.getFullName(),
                u -> u,
                (existing, replacement) -> null));
        List<Workflow> workflows = new ArrayList<>();
        for (fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow : dbWorkflowList) {
            workflows.add(parseDbWorkflow(dbWorkflow, usersByFullname));
        }
        return workflows;
    }


    private Workflow parseDbWorkflow(fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow) throws VipException {
        return parseDbWorkflow(dbWorkflow, null, true);
    }

    private Workflow parseDbWorkflow(fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow, Map<String, User> usersByFullname) throws VipException {
        return parseDbWorkflow(dbWorkflow, usersByFullname, false);
    }

    private Workflow parseDbWorkflow(fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow, Map<String, User> usersByFullname, boolean populateInOut) throws VipException {
        Optional<User> workflowUser;
        if (usersByFullname == null) {
            workflowUser = userBusiness.getByFullname(dbWorkflow.getUsername());
        } else {
            workflowUser = Optional.ofNullable(usersByFullname.get(dbWorkflow.getUsername()));
        }
        Workflow workflow;
        if (workflowUser.isEmpty()) {
            workflow = new Workflow(
                    dbWorkflow.getId(),
                    dbWorkflow.getDescription(),
                    dbWorkflow.getApplication(),
                    dbWorkflow.getApplicationVersion(),
                    dbWorkflow.getUsername(),
                    dbWorkflow.getStatus().name(),
                    dbWorkflow.getStartedTime(),
                    dbWorkflow.getFinishedTime(),
                    dbWorkflow.getEngine(),
                    dbWorkflow.getTags());
        } else {
            workflow = new Workflow(
                    dbWorkflow.getId(),
                    dbWorkflow.getDescription(),
                    dbWorkflow.getApplication(),
                    dbWorkflow.getApplicationVersion(),
                    workflowUser.get(),
                    dbWorkflow.getStatus().name(),
                    dbWorkflow.getStartedTime(),
                    dbWorkflow.getFinishedTime(),
                    dbWorkflow.getEngine(),
                    dbWorkflow.getTags());
        }
        if (populateInOut) {
            try {
                String folder = getUser().getFolder();
                List<InOutData> inputData = workflowBusiness.getInputData(workflow.getID(), folder);
                if (inputData != null && !inputData.isEmpty()) {
                    workflow.setInputs(inputData.stream()
                        .collect(Collectors.groupingBy(
                            InOutData::getProcessor,
                            Collectors.collectingAndThen(
                                Collectors.mapping(InOutData::getPath,
                                    Collectors.toCollection(ArrayList::new)),
                                WorkflowInput::ofList
                            )
                        )));
                }
                List<InOutData> outputData = workflowBusiness.getOutputData(workflow.getID(), folder);
                if (outputData != null && !outputData.isEmpty()) {
                    workflow.setOutputs(outputData.stream()
                        .collect(Collectors.groupingBy(
                            InOutData::getProcessor,
                            Collectors.mapping(InOutData::getPath,
                                Collectors.toCollection(ArrayList::new))
                        )));
                }
            } catch (VipException e) {
                logger.warn("Could not populate inputs/outputs for workflow {}", workflow.getID(), e);
            }
        }
        return workflow;
    }

    public List<Workflow> refreshRunningWorkflows(List<Workflow> workflows) throws VipException {
        try {
            for (Workflow workflow : workflows) {
                if (workflow.getStatus() == WorkflowStatus.Running
                        || workflow.getStatus() == WorkflowStatus.Unknown) {
                    WorkflowStatus workflowStatus = workflowExecutionBusiness.getStatus(workflow.getEngineEndpoint(), workflow.getID());
                    logger.debug("Simulation {} : old status : {}, new status : {} ",
                            workflow.getID(), workflow.getStatus(), workflowStatus);

                    if (workflowStatus != workflow.getStatus()) {
                        workflow.setStatus(workflowStatus);
                        fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow = workflowDAO.get(workflow.getID());
                        dbWorkflow.setStatus(fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.valueOf(workflowStatus.name()));
                        workflowDAO.update(dbWorkflow);
                    }
                }
            }
            return workflows;
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error refreshing workflows for user {}", getUserEmail(), ex);
            throw new VipException(ex);
        }
    }
}
