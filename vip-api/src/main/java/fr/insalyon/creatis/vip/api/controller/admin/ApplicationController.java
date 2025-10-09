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
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
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
    public List<Application> listApplications() throws ApiException {
        logMethodInvocation(logger, "listApplications");
        try {
            return applicationBusiness.getApplications();
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }

    @RequestMapping(value = "{applicationId}", method = RequestMethod.GET)
    public Application getApplication(@PathVariable String applicationId) throws ApiException {
        logMethodInvocation(logger, "getApplication", applicationId);
        try {
            Application app = applicationBusiness.getApplication(applicationId);
            if (app == null) {
                throw new ApiException(ApiException.ApiError.APPLICATION_NOT_FOUND, applicationId);
            }
            return app;
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }

    @RequestMapping(value = "/{applicationId}", method = RequestMethod.PUT)
    public Application createOrUpdateApplication(@PathVariable String applicationId,
                                                 @RequestBody @Valid Application app) throws ApiException {
        logMethodInvocation(logger, "createOrUpdateApplication", applicationId);
        if (!applicationId.equals(app.getName())) {
            logger.error("applicationId mismatch: {}!={}", applicationId, app.getName());
            throw new ApiException(ApiException.ApiError.INPUT_FIELD_NOT_VALID, applicationId);
        }
        try {
            Application existingApp = applicationBusiness.getApplication(applicationId);
            if (existingApp == null) {
                applicationBusiness.add(app);
            } else {
                applicationBusiness.update(app);
            }
            return applicationBusiness.getApplication(applicationId);
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Application createApplication(@RequestBody @Valid Application app) throws ApiException {
        return createOrUpdateApplication(app.getName(), app);
    }

    @RequestMapping(value = "/{applicationId}", method = RequestMethod.DELETE)
    public void deleteApplication(@PathVariable String applicationId) throws ApiException {
        logMethodInvocation(logger, "deleteApplication", applicationId);
        try {
            applicationBusiness.remove(applicationId);
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }
}
