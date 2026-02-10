package fr.insalyon.creatis.vip.api.controller.admin;

import java.util.ArrayList;
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
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("admin/appVersions")
public class AppVersionController extends ApiController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationBusiness applicationBusiness;
    private final AppVersionBusiness appVersionBusiness;

    @Autowired
    protected AppVersionController(ApplicationBusiness applicationBusiness, AppVersionBusiness appVersionBusiness) {
        this.applicationBusiness = applicationBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AppVersion> listAppVersions() throws VipException {
        logMethodInvocation(logger, "listAppVersions");
        List<Application> apps = applicationBusiness.getApplications();
        List<AppVersion> appVersions = new ArrayList<>();
        for (Application app : apps) {
            appVersions.addAll(appVersionBusiness.getVersions(app.getName()));
        }
        return appVersions;
    }

    private class AppVersionStrings {
        public String appName;
        public String version;
        AppVersionStrings(String appName, String version) {
            this.appName = appName;
            this.version = version;
        }
    }

    private AppVersionStrings parseAppVersionId(String appVersionId) throws VipException {
        int delimiterPos = appVersionId.lastIndexOf("/");
        if (delimiterPos >= 0) {
            String appName = appVersionId.substring(0, delimiterPos);
            String version = appVersionId.substring(delimiterPos + 1);
            return new AppVersionStrings(appName, version);
        } else {
            logger.error("Error decoding appVersionId {}", appVersionId);
            throw new VipException(ApiError.INVALID_PIPELINE_IDENTIFIER, appVersionId);
        }
    }

    @RequestMapping(value = "{appVersionId}", method = RequestMethod.GET)
    public AppVersion getAppVersion(@PathVariable String appVersionId) throws VipException {
        logMethodInvocation(logger, "getAppVersion", appVersionId);
        AppVersionStrings input = parseAppVersionId(appVersionId);
        AppVersion appVersion = appVersionBusiness.getVersion(input.appName, input.version);
        if (appVersion == null) {
            throw new VipException(ApiError.INVALID_PIPELINE_IDENTIFIER, appVersionId);
        }
        return appVersion;
    }

    @RequestMapping(value = "{appVersionIdFirstPart}/{appVersionIdSecondPart}", method = RequestMethod.GET)
    public AppVersion getAppVersion(@PathVariable String appVersionIdFirstPart,
                                    @PathVariable String appVersionIdSecondPart) throws VipException {
        return getAppVersion(appVersionIdFirstPart + "/" + appVersionIdSecondPart);
    }

    @RequestMapping(value = "/{appVersionId}", method = RequestMethod.PUT)
    public AppVersion createOrUpdateAppVersion(@PathVariable String appVersionId,
                                               @RequestBody @Valid AppVersion appVersion) throws VipException {
        logMethodInvocation(logger, "createOrUpdateAppVersion", appVersionId);
        AppVersionStrings input = parseAppVersionId(appVersionId);
        String appVersionIdBody = appVersion.getApplicationName() + "/" + appVersion.getVersion();
        if (!appVersionId.equals(appVersionIdBody)) {
            logger.error("appVersionId mismatch: {}!={}", appVersionId, appVersionIdBody);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, appVersionId);
        }
        AppVersion existingAppVersion = appVersionBusiness.getVersion(input.appName, input.version);
        if (existingAppVersion == null) {
            appVersionBusiness.add(appVersion);
        } else {
            appVersionBusiness.update(appVersion);
        }
        return appVersionBusiness.getVersion(input.appName, input.version);
    }

    @RequestMapping(value = "/{appVersionIdFirstPart}/{appVersionIdSecondPart}", method = RequestMethod.PUT)
    public AppVersion createOrUpdateAppVersion(@PathVariable String appVersionIdFirstPart,
                                               @PathVariable String appVersionIdSecondPart,
                                               @RequestBody @Valid AppVersion appVersion) throws VipException {
        return createOrUpdateAppVersion(appVersionIdFirstPart + "/" + appVersionIdSecondPart, appVersion);
    }

    @RequestMapping(method = RequestMethod.POST)
    public AppVersion createAppVersion(@RequestBody @Valid AppVersion appVersion) throws VipException {
        return createOrUpdateAppVersion(appVersion.getApplicationName(), appVersion.getVersion(), appVersion);
    }

    @RequestMapping(value = "/{appVersionId}", method = RequestMethod.DELETE)
    public void deleteAppVersion(@PathVariable String appVersionId) throws VipException {
        logMethodInvocation(logger, "deleteAppVersion", appVersionId);
        AppVersionStrings input = parseAppVersionId(appVersionId);
        appVersionBusiness.remove(input.appName, input.version);
    }

    @RequestMapping(value = "/{appVersionIdFirstPart}/{appVersionIdSecondPart}", method = RequestMethod.DELETE)
    public void deleteAppVersion(@PathVariable String appVersionIdFirstPart,
                                 @PathVariable String appVersionIdSecondPart) throws VipException {
        deleteAppVersion(appVersionIdFirstPart + "/" + appVersionIdSecondPart);
    }
}
