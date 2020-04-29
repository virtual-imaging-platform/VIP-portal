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
import fr.insalyon.creatis.vip.api.bean.ParameterType;
import fr.insalyon.creatis.vip.api.bean.ParameterTypedValue;
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.PipelineParameter;
import fr.insalyon.creatis.vip.api.business.ApiException.ApiError;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.sql.Connection;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import static fr.insalyon.creatis.vip.api.business.ApiException.ApiError.*;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class PipelineBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiContext apiContext;
    private Environment env;

    private final WorkflowBusiness workflowBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final ClassBusiness classBusiness;

    public PipelineBusiness(ApiContext apiContext, Environment env) {
        this(apiContext, env, new WorkflowBusiness(), new ApplicationBusiness(), new ClassBusiness());
    }

    @Autowired
    public PipelineBusiness(ApiContext apiContext, Environment env, WorkflowBusiness workflowBusiness, ApplicationBusiness applicationBusiness, ClassBusiness classBusiness) {
        this.apiContext = apiContext;
        this.env = env;
        this.workflowBusiness = workflowBusiness;
        this.applicationBusiness = applicationBusiness;
        this.classBusiness = classBusiness;
    }

    public Pipeline getPipeline(String pipelineId, Connection connection)
        throws ApiException {
        Pipeline p = getPipelineWithResultsDirectory(pipelineId, connection);

        p.getParameters().removeIf(
            param ->
            param.getName().equals(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME));

        return p;
    }

    public Pipeline getPipelineWithResultsDirectory(
        String pipelineId, Connection connection)
        throws ApiException {
        try {
            String applicationName = ApiUtils.getApplicationName(pipelineId);
            String applicationVersion = ApiUtils.getApplicationVersion(pipelineId);
            Pipeline p = getPipelineWithPermissions(
                applicationName, applicationVersion, connection);

            Descriptor d = workflowBusiness.getApplicationDescriptor(apiContext.getUser(), p.getName(), p.getVersion(), connection); // Be careful, this copies the Gwendia file from LFC.
            p.setDescription(d.getDescription());

            for (Source s : d.getSources()) {
                ParameterType sourceType = ApiUtils.getCarminType(s.getType());
                ParameterTypedValue defaultValue = s.getDefaultValue() == null ? null : new ParameterTypedValue(sourceType, s.getDefaultValue());
                PipelineParameter pp = new PipelineParameter(s.getName(),
                                                             sourceType,
                                                             s.isOptional(),
                                                             false,
                                                             defaultValue,
                                                             s.getDefaultValue(),
                                                             s.getDescription());
                p.getParameters().add(pp);
            }
            return p;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public Pipeline[] listPipelines(
        String studyIdentifier, Connection connection)
        throws ApiException {

        try {
            if (studyIdentifier != null) {
                apiContext.getWarnings().add("Study identifier was ignored.");
            }
            ArrayList<Pipeline> pipelines = new ArrayList<>();

            List<AppClass> classes = classBusiness.getUserClasses(apiContext.getUser().getEmail(), false, connection);
            List<String> classNames = new ArrayList<>();
            for (AppClass c : classes) {
                classNames.add(c.getName());
            }

            List<Application> applications =
                applicationBusiness.getApplications(classNames, connection);
            for (Application a : applications) {
                List<AppVersion> versions =
                    applicationBusiness.getVersions(a.getName(), connection);
                for (AppVersion av : versions) {
                    if (isApplicationVersionUsableInApi(av)) {
                        pipelines.add(
                                new Pipeline(ApiUtils.getPipelineIdentifier(
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

    private boolean isApplicationVersionUsableInApi(AppVersion appVersion) {
        if (appVersion.isVisible()) {
            return true;
        }
        if (env == null) {
            // spring not present, so we're in soap context and the white list
            // is not supported
            return false;
        }
        List<String> whiteList = Arrays.asList(
                env.getProperty(CarminProperties.API_PIPELINE_WHITE_LIST, String[].class));
        return whiteList.stream().anyMatch(appString -> {
            String[] splitAppString = appString.split("/");
            if (splitAppString.length != 2) {
                return false;
            }
            return splitAppString[0].equals(appVersion.getApplicationName())
                    && splitAppString[1].equals(appVersion.getVersion());
        });
    }

    public void checkIfUserCanAccessPipeline(
        String pipelineId, Connection connection)
        throws ApiException {
        try {

            String applicationName = ApiUtils.getApplicationName(pipelineId);
            List<String> userClassNames = classBusiness.getUserClassesName(
                apiContext.getUser().getEmail(), false, connection);

            Application a = applicationBusiness.getApplication(
                applicationName, connection);
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
                    apiContext.getUser(), applicationName);
            throw new ApiException(NOT_ALLOWED_TO_USE_APPLICATION, applicationName);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    private Pipeline getPipelineWithPermissions(
        String applicationName, String applicationVersion, Connection connection)
        throws ApiException {
        Pipeline[] pipelines = listPipelines("", connection);
        for (Pipeline p : pipelines) {
            if (p.getName().equals(applicationName) && p.getVersion().equals(applicationVersion)) {
                return p;
            }
        }
        logger.error("Pipeline {}/{} doesn't exist or user {} cannot access it",
                applicationName, applicationVersion , apiContext.getUser());
        throw new ApiException(PIPELINE_NOT_FOUND, applicationName + "/" + applicationVersion);
    }
}
