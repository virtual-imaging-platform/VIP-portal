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
import fr.insalyon.creatis.vip.api.model.ParameterType;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.api.model.PipelineParameter;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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

    private Environment env;

    private Supplier<User> currentUserProvider;
    private final WorkflowBusiness workflowBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final ClassBusiness classBusiness;

    @Autowired
    public PipelineBusiness(
            Supplier<User> currentUserProvider, Environment env,
            WorkflowBusiness workflowBusiness,
            ApplicationBusiness applicationBusiness, PublicationBusiness publicationBusiness, ClassBusiness classBusiness) {
        this.currentUserProvider = currentUserProvider;
        this.env = env;
        this.workflowBusiness = workflowBusiness;
        this.applicationBusiness = applicationBusiness;
        this.classBusiness = classBusiness;
    }

    public Pipeline getPipeline(String pipelineId)
            throws ApiException {
        Pipeline p = getPipelineWithResultsDirectory(pipelineId);

        p.getParameters().removeIf(
            param ->
            param.getName().equals(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME));

        return p;
    }

    public Pipeline getPipelineWithResultsDirectory(String pipelineId)
            throws ApiException {
        try {
            String applicationName = getApplicationName(pipelineId);
            String applicationVersion = getApplicationVersion(pipelineId);
            Pipeline p = getPipelineWithPermissions(
                    applicationName, applicationVersion);

            Descriptor d = workflowBusiness.getApplicationDescriptor(
                    currentUserProvider.get(), p.getName(), p.getVersion()); // Be careful, this copies the Gwendia file from LFC.
            p.setDescription(d.getDescription());

            for (Source s : d.getSources()) {
                ParameterType sourceType = ParameterType.fromVipType(s.getType());
                if ("flag".equalsIgnoreCase(s.getVipTypeRestriction())) {
                    sourceType = ParameterType.Boolean;
                }
                PipelineParameter pp = new PipelineParameter(s.getName(),
                                                             sourceType,
                                                             s.isOptional(),
                                                             false,
                                                             s.getDefaultValue(),
                                                             s.getDescription());
                p.getParameters().add(pp);
            }
            return p;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public Pipeline[] listPipelines(String studyIdentifier) throws ApiException {

        try {
            if (studyIdentifier != null) {
                logger.warn("Study identifier ({}) was ignored.", studyIdentifier);
            }
            ArrayList<Pipeline> pipelines = new ArrayList<>();

            List<AppClass> classes = classBusiness.getUserClasses(currentUserProvider.get().getEmail(), false);
            List<String> classNames = new ArrayList<>();
            for (AppClass c : classes) {
                classNames.add(c.getName());
            }

            List<Application> applications =
                applicationBusiness.getApplications(classNames);
            for (Application a : applications) {
                List<AppVersion> versions =
                    applicationBusiness.getVersions(a.getName());
                for (AppVersion av : versions) {
                    if (isApplicationVersionUsableInApi(av)) {
                        pipelines.add(
                                new Pipeline(getPipelineIdentifier(
                                        a.getName(), av.getVersion()),
                                        a.getName(), av.getVersion(), true)
                        );
                    }
                }
            }
            Pipeline[] array_pipelines = new Pipeline[pipelines.size()];
            return pipelines.toArray(array_pipelines);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public List<Application> listPublicPipelines() throws ApiException {
        try {
             return applicationBusiness.getPublicApplicationsWithGroups();
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

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

    public void checkIfUserCanAccessPipeline(String pipelineId)
            throws ApiException {
        try {

            String applicationName = getApplicationName(pipelineId);
            List<String> userClassNames = classBusiness.getUserClassesName(
                currentUserProvider.get().getEmail(), false);

            Application a = applicationBusiness.getApplication(applicationName);
            if (a == null) {
                logger.error("Cannot find application {}", applicationName);
                throw new ApiException(APPLICATION_NOT_FOUND, applicationName);
            }
            for (String applicationClassName : a.getApplicationClasses()) {
                if (userClassNames.contains(applicationClassName)) {
                    return;
                }
            }
            logger.error("User {} not allowed to access application {}",
                    currentUserProvider.get(), applicationName);
            throw new ApiException(NOT_ALLOWED_TO_USE_APPLICATION, applicationName);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    private Pipeline getPipelineWithPermissions(
            String applicationName, String applicationVersion)
        throws ApiException {
        Pipeline[] pipelines = listPipelines("");
        for (Pipeline p : pipelines) {
            if (p.getName().equals(applicationName) && p.getVersion().equals(applicationVersion)) {
                return p;
            }
        }
        logger.error("Pipeline {}/{} doesn't exist or user {} cannot access it",
                applicationName, applicationVersion , currentUserProvider.get());
        throw new ApiException(PIPELINE_NOT_FOUND, applicationName + "/" + applicationVersion);
    }
}
