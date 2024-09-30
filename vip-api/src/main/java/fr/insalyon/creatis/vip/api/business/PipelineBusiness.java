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
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.model.ParameterType;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.api.model.PipelineParameter;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.application.server.model.boutiques.BoutiquesDescriptor;
import fr.insalyon.creatis.vip.application.server.model.boutiques.Input;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static fr.insalyon.creatis.vip.api.exception.ApiException.ApiError.*;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class PipelineBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment env;
    private final Server server;

    private final Supplier<User> currentUserProvider;
    private final WorkflowBusiness workflowBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final ClassBusiness classBusiness;
    private final BoutiquesBusiness boutiquesBusiness;
    private final DataManagerBusiness dataManagerBusiness;

    @Autowired
    public PipelineBusiness(
            Supplier<User> currentUserProvider, Environment env,
            Server server, WorkflowBusiness workflowBusiness, ApplicationBusiness applicationBusiness,
            ClassBusiness classBusiness, BoutiquesBusiness boutiquesBusiness, DataManagerBusiness dataManagerBusiness) {
        this.currentUserProvider = currentUserProvider;
        this.env = env;
        this.server = server;
        this.workflowBusiness = workflowBusiness;
        this.applicationBusiness = applicationBusiness;
        this.classBusiness = classBusiness;
        this.boutiquesBusiness = boutiquesBusiness;
        this.dataManagerBusiness = dataManagerBusiness;
    }

    //*********************** pipeline id format validation **********************************

    public String getPipelineIdentifier(String applicationName, String applicationVersion) {
        return applicationName + "/" + applicationVersion;
    }

    public String getApplicationVersion(String pipelineIdentifier) throws ApiException {
        checkIfValidPipelineIdentifier(pipelineIdentifier);
        return pipelineIdentifier.substring(pipelineIdentifier.lastIndexOf("/") + 1);
    }

    public String getApplicationName(String pipelineIdentifier) throws ApiException {
        checkIfValidPipelineIdentifier(pipelineIdentifier);
        return pipelineIdentifier.substring(0, pipelineIdentifier.lastIndexOf("/"));
    }

    private void checkIfValidPipelineIdentifier(String identifier) throws ApiException {
        if (!identifier.contains("/")) {
            logger.error("Invalid pipeline identifier {} : missing /", identifier);
            throw new ApiException(ApiError.INVALID_PIPELINE_IDENTIFIER, identifier);
        }
    }

    // *************************** public methods ***********************


    /**
     * Returns pipeline + parameters without the results-directory param
     */
    public Pipeline getPipelineWithoutResultsDirectory(String pipelineId) throws ApiException {

        if (server.useMoteurlite()) {
            return getPipelineFromBoutiquesDescriptor(pipelineId);
        } else {
            Pipeline p = getPipelineFromGwendiaDescriptor(pipelineId);
            p.getParameters().removeIf(
                    param -> CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(param.getName()));
            return p;
        }
    }

    /**
     * Returns pipeline + parameters with the results-directory param
     */
    public Pipeline getPipelineWithResultsDirectory(String pipelineId) throws ApiException {

        if (server.useMoteurlite()) {
            // boutiques must not contain it, we always add it
            Pipeline p = getPipelineFromBoutiquesDescriptor(pipelineId);
            p.getParameters().add(new PipelineParameter(
                    CoreConstants.RESULTS_DIRECTORY_PARAM_NAME, ParameterType.File, false, false,
                    DataManagerConstants.ROOT + "/" + DataManagerConstants.USERS_HOME, "Results directory"));
            return p;
        } else {
            return getPipelineFromGwendiaDescriptor(pipelineId);
        }
    }

    public BoutiquesDescriptor getBoutiquesDescriptor(String pipelineId) throws ApiException {
        AppVersion appVersion = getAppVersionFromPipelineId(pipelineId);

        String boutiquesUri = appVersion.getJsonLfn();
        if (boutiquesUri == null || boutiquesUri.isEmpty()) {
            logger.error("boutiques lfn not specified for app {}", pipelineId);
            throw new ApiException(NOT_COMPATIBLE_WITH_BOUTIQUES, pipelineId);
        }

        try {
            String boutiquesFilePath = dataManagerBusiness.getRemoteFile(currentUserProvider.get(), boutiquesUri);
            File boutiquesFile = Paths.get(boutiquesFilePath).toFile();
            if ( ! boutiquesFile.exists()) {
                logger.error("Boutiques file ({}) absent after download in {}", boutiquesUri, boutiquesFilePath);
                throw new ApiException(GENERIC_API_ERROR);
            }
            return boutiquesBusiness.parseBoutiquesFile(boutiquesFile);
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

    /**
     * List all the pipeline the user can access
     */
    public List<Pipeline> listPipelines(String studyIdentifier) throws ApiException {

        try {
            if (studyIdentifier != null) {
                logger.warn("Study identifier ({}) was ignored.", studyIdentifier);
            }
            ArrayList<Pipeline> pipelines = new ArrayList<>();

            List<String> classNames = classBusiness.getUserClassesName(currentUserProvider.get().getEmail(), false);

            List<Application> applications = applicationBusiness.getApplications(classNames);
            for (Application a : applications) {
                List<AppVersion> versions = applicationBusiness.getVersions(a.getName());
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
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    // Specific stuff that return in 'Application' class format and not 'Pipeline'
    // used for the VIP landing page
    public List<Application> listPublicPipelines() throws ApiException {
        try {
            return applicationBusiness.getPublicApplicationsWithGroups();
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

    // ********************* Basic stuff **************************************

    /**
     *  Get the pipeline parameters from the gwendia file
     *  Warning : this includes the results-directory parameter
     */
    private Pipeline getPipelineFromGwendiaDescriptor(String pipelineId) throws ApiException {
        try {
            Pipeline p = getPipelineWithoutParameters(pipelineId);

            // download the gwendia file (can be slow)
            Descriptor d = workflowBusiness.getApplicationDescriptor(
                    currentUserProvider.get(), p.getName(), p.getVersion());
            p.setDescription(d.getDescription());

            for (Source s : d.getSources()) {
                ParameterType sourceType = ParameterType.fromVipType(s.getType());
                if ("flag".equalsIgnoreCase(s.getVipTypeRestriction())) {
                    sourceType = ParameterType.Boolean;
                }
                PipelineParameter pp = new PipelineParameter(
                        s.getName(), sourceType, s.isOptional(),false,
                        s.getDefaultValue(), s.getDescription());
                p.getParameters().add(pp);
            }
            return p;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }


    /**
     *  Get the pipeline parameters from the boutiques file
     *  Warning : this does not include the results-directory parameter
     */
    private Pipeline getPipelineFromBoutiquesDescriptor(String pipelineId) throws ApiException {
        // download the boutiques file and parse it
        BoutiquesDescriptor boutiques = getBoutiquesDescriptor(pipelineId);
        Pipeline p = new Pipeline(pipelineId, boutiques.getName(), boutiques.getToolVersion());
        p.setDescription(boutiques.getDescription());

        for (Input input : boutiques.getInputs()) {
            ParameterType type = ParameterType.fromBoutiquesInput(input);
            PipelineParameter pp = new PipelineParameter(
                    input.getId(), type, input.getOptional() != null && input.getOptional(),false,
                    input.getDefaultValue(), input.getDescription());
            p.getParameters().add(pp);
        }
        return p;
    }

    // return basic pipeline without parameters
    private Pipeline getPipelineWithoutParameters(String pipelineId) throws ApiException {
        AppVersion appVersion = getAppVersionFromPipelineId(pipelineId);

        return new Pipeline(pipelineId, appVersion.getApplicationName(), appVersion.getVersion());
    }

    private AppVersion getAppVersionFromPipelineId(String pipelineId) throws ApiException {
        try {
            String applicationName = getApplicationName(pipelineId);
            String applicationVersion = getApplicationVersion(pipelineId);
            AppVersion appVersion = applicationBusiness.getVersion(applicationName, applicationVersion);
            if (appVersion == null) {
                logger.error("Cannot find pipeline {}/{}", applicationName, applicationVersion);
                throw new ApiException(PIPELINE_NOT_FOUND, pipelineId);
            }
            checkAppVersionAccess(appVersion);
            return appVersion;
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

    // ********************* VERIFICATION STUFF *******************************

    // should be called it all cases
    private void checkAppVersionAccess(AppVersion appVersion) throws ApiException {
        String appName = appVersion.getApplicationName();
        String version = appVersion.getVersion();

        // check it is visible or in white list
        if ( ! isApplicationVersionUsableInApi(appVersion)) {
            logger.error("Application {}/{} not visible or in api whitelist", appName, version);
            throw new ApiException(PIPELINE_NOT_FOUND, getPipelineIdentifier(appName, version));
        }

        // check the user can use it through its classes
        try {
            List<String> userClassNames = classBusiness.getUserClassesName(currentUserProvider.get().getEmail(), false);
            List<String> applicationClassNames = applicationBusiness.getApplication(appName).getApplicationClasses();

            for (String applicationClassName : applicationClassNames) {
                if (userClassNames.contains(applicationClassName)) {
                    return;
                }
            }
            logger.error("User {} not allowed to access application {}", currentUserProvider.get(), appName);
            throw new ApiException(NOT_ALLOWED_TO_USE_PIPELINE, getPipelineIdentifier(appName, version));
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

    private boolean isApplicationVersionUsableInApi(AppVersion appVersion) {
        if (appVersion.isVisible()) {
            return true;
        }
        List<String> whiteList = Arrays.asList(
                env.getRequiredProperty(CarminProperties.API_PIPELINE_WHITE_LIST, String[].class));
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
