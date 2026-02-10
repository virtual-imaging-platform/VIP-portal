package fr.insalyon.creatis.vip.api.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("admin/applications")
public class ApplicationController extends ApiController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationBusiness applicationBusiness;

    @Autowired
    protected ApplicationController(ApplicationBusiness applicationBusiness) {
        this.applicationBusiness = applicationBusiness;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Application> listApplications() throws VipException {
        logMethodInvocation(logger, "listApplications");
        return applicationBusiness.getApplications();
    }

    @RequestMapping(value = "{applicationId}", method = RequestMethod.GET)
    public Application getApplication(@PathVariable String applicationId) throws VipException {
        logMethodInvocation(logger, "getApplication", applicationId);
        Application app = applicationBusiness.getApplication(applicationId);
        if (app == null) {
            throw new VipException(ApiError.APPLICATION_NOT_FOUND, applicationId);
        }
        return app;
    }

    @RequestMapping(value = "/{applicationId}", method = RequestMethod.PUT)
    public Application createOrUpdateApplication(@PathVariable String applicationId,
                                                 @RequestBody @Valid Application app) throws VipException {
        logMethodInvocation(logger, "createOrUpdateApplication", applicationId);
        if (!applicationId.equals(app.getName())) {
            logger.error("applicationId mismatch: {}!={}", applicationId, app.getName());
            throw new VipException(DefaultError.BAD_INPUT_FIELD, applicationId);
        }
        Application existingApp = applicationBusiness.getApplication(applicationId);
        if (existingApp == null) {
            applicationBusiness.add(app);
        } else {
            applicationBusiness.update(app);
        }
        return applicationBusiness.getApplication(applicationId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Application createApplication(@RequestBody @Valid Application app) throws VipException {
        return createOrUpdateApplication(app.getName(), app);
    }

    @RequestMapping(value = "/{applicationId}", method = RequestMethod.DELETE)
    public void deleteApplication(@PathVariable String applicationId) throws VipException {
        logMethodInvocation(logger, "deleteApplication", applicationId);
        applicationBusiness.remove(applicationId);
    }
}
