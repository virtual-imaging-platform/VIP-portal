package fr.insalyon.creatis.vip.api.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.boutiques.model.Input;
import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.model.ParameterType;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.api.model.PipelineParameter;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

@Service
public class PipelineBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;

    private final Supplier<User> currentUserProvider;
    private final ApplicationBusiness applicationBusiness;
    private final BoutiquesBusiness boutiquesBusiness;
    private final AppVersionBusiness appVersionBusiness;

    @Autowired
    public PipelineBusiness(
            Supplier<User> currentUserProvider, Server server, ApplicationBusiness applicationBusiness,
            BoutiquesBusiness boutiquesBusiness, AppVersionBusiness appVersionBusiness) {
        this.currentUserProvider = currentUserProvider;
        this.server = server;
        this.applicationBusiness = applicationBusiness;
        this.boutiquesBusiness = boutiquesBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    //*********************** pipeline id format validation **********************************

    public String getPipelineIdentifier(String applicationName, String applicationVersion) {
        return applicationName + "/" + applicationVersion;
    }

    public String getApplicationVersion(String pipelineIdentifier) throws VipException {
        checkIfValidPipelineIdentifier(pipelineIdentifier);
        return pipelineIdentifier.substring(pipelineIdentifier.lastIndexOf("/") + 1);
    }

    public String getApplicationName(String pipelineIdentifier) throws VipException {
        checkIfValidPipelineIdentifier(pipelineIdentifier);
        return pipelineIdentifier.substring(0, pipelineIdentifier.lastIndexOf("/"));
    }

    private void checkIfValidPipelineIdentifier(String identifier) throws VipException {
        if (!identifier.contains("/")) {
            logger.error("Invalid pipeline identifier {} : missing /", identifier);
            throw new VipException(ApiError.INVALID_PIPELINE_IDENTIFIER, identifier);
        }
    }

    // *************************** public methods ***********************


    /**
     * Returns pipeline + parameters without the results-directory param
     */
    public Pipeline getPipelineWithoutResultsDirectory(String pipelineId) throws VipException {
        return getPipelineFromBoutiquesDescriptor(pipelineId);
    }

    /**
     * Returns pipeline + parameters with the results-directory param
     */
    public Pipeline getPipelineWithResultsDirectory(String pipelineId) throws VipException {
        // boutiques must not contain it, we always add it
        Pipeline p = getPipelineFromBoutiquesDescriptor(pipelineId);
        p.getParameters().add(new PipelineParameter(
                CoreConstants.RESULTS_DIRECTORY_PARAM_NAME, ParameterType.File, false, false,
                DataManagerConstants.ROOT + "/" + DataManagerConstants.USERS_HOME, "Results directory"));
        return p;
    }

    public BoutiquesDescriptor getBoutiquesDescriptor(String pipelineId) throws VipException {
        AppVersion appVersion = getAppVersionFromPipelineId(pipelineId);

        return boutiquesBusiness.parseBoutiquesString(appVersion.getDescriptor());
    }

    private List<Pipeline> appsToPipelines(List<Application> applications) throws VipException {
        List<Pipeline> pipelines = new ArrayList<>();
        for (Application a : applications) {
            List<AppVersion> versions = appVersionBusiness.getVersions(a.getName());
            for (AppVersion av : versions) {
                if (isApplicationVersionUsableInApi(av)) {
                    pipelines.add(
                            new Pipeline(getPipelineIdentifier(
                                    a.getName(), av.getVersion()),
                                    a.getName(), av.getVersion())
                    );
                }
            }
        }
        return pipelines;
    }

    /**
     * List all the pipeline the user can access
     */
    public List<Pipeline> listPipelines(String studyIdentifier) throws VipException {
        if (studyIdentifier != null) {
            logger.warn("Study identifier ({}) was ignored.", studyIdentifier);
        }

        List<Application> applications = applicationBusiness.getApplications(currentUserProvider.get());
        return appsToPipelines(applications);
    }

    // Specific stuff that return in 'Application' class format and not 'Pipeline'
    // used for the VIP landing page
    public List<Pipeline> listPublicPipelines() throws VipException {
        List<Application> applications = applicationBusiness.getPublicApplications();
        return appsToPipelines(applications);
    }

    // ********************* Basic stuff **************************************

    /**
     *  Get the pipeline parameters from the boutiques file
     *  Warning : this does not include the results-directory parameter
     */
    private Pipeline getPipelineFromBoutiquesDescriptor(String pipelineId) throws VipException {
        // download the boutiques file and parse it
        BoutiquesDescriptor boutiques = getBoutiquesDescriptor(pipelineId);
        Pipeline p = new Pipeline(pipelineId, boutiques.getName(), boutiques.getToolVersion());
        p.setDescription(boutiques.getDescription());

        Map<String, String> overriddenInputs = boutiquesBusiness.getOverriddenInputs(boutiques);
        for (Input input : boutiques.getInputs()) {
            if (overriddenInputs != null && overriddenInputs.containsKey(input.getId())) {
                continue; // hide overriddenInputs from pipeline visible parameters
            }
            ParameterType type = ParameterType.fromBoutiquesInput(input);
            PipelineParameter pp = new PipelineParameter(
                    input.getId(), type, input.getOptional() != null && input.getOptional(),false,
                    input.getDefaultValue(), input.getDescription());
            p.getParameters().add(pp);
        }
        // store overriddenInputs in pipeline object to avoid parsing the descriptor again later
        if (overriddenInputs != null) {
            p.setOverriddenInputs(overriddenInputs);
        }
        return p;
    }

    // return basic pipeline without parameters
    private Pipeline getPipelineWithoutParameters(String pipelineId) throws VipException {
        AppVersion appVersion = getAppVersionFromPipelineId(pipelineId);

        return new Pipeline(pipelineId, appVersion.getApplicationName(), appVersion.getVersion());
    }

    private AppVersion getAppVersionFromPipelineId(String pipelineId) throws VipException {
        String applicationName = getApplicationName(pipelineId);
        String applicationVersion = getApplicationVersion(pipelineId);
        AppVersion appVersion = appVersionBusiness.getVersion(applicationName, applicationVersion);
        if (appVersion == null) {
            logger.error("Cannot find pipeline {}/{}", applicationName, applicationVersion);
            throw new VipException(ApiError.PIPELINE_NOT_FOUND, pipelineId);
        }
        checkAppVersionAccess(appVersion);
        return appVersion;
    }

    // ********************* VERIFICATION STUFF *******************************

    // should be called it all cases
    private void checkAppVersionAccess(AppVersion appVersion) throws VipException {
        String appName = appVersion.getApplicationName();
        String version = appVersion.getVersion();

        // check it is visible or in white list
        if ( ! isApplicationVersionUsableInApi(appVersion)) {
            logger.error("Application {}/{} not visible or in api whitelist", appName, version);
            throw new VipException(ApiError.PIPELINE_NOT_FOUND, getPipelineIdentifier(appName, version));
        }

        // check the user can use it 
        List<Application> apps = applicationBusiness.getApplications(currentUserProvider.get());

        for (Application app : apps) {
            if (app.getName().equals(appName)) {
                return;
            }
        }
        logger.error("User {} not allowed to access application {}", currentUserProvider.get(), appName);
        throw new VipException(ApiError.NOT_ALLOWED_TO_USE_PIPELINE, getPipelineIdentifier(appName, version));
    }

    private boolean isApplicationVersionUsableInApi(AppVersion appVersion) {
        if (appVersion.isVisible()) {
            return true;
        }
        List<String> whiteList = Arrays.asList(server.getCarminApiPipelineWhiteList());
        return whiteList.stream().anyMatch(appString -> {
            String[] splitAppString = appString.split("/");
            if (splitAppString.length != 2) {
                return false;
            }
            return splitAppString[0].equals(appVersion.getApplicationName())
                    && splitAppString[1].equals(appVersion.getVersion());
        });
    }
}
