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

import fr.insalyon.creatis.vip.api.bean.ParameterType;
import fr.insalyon.creatis.vip.api.bean.ParameterTypedValue;
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.PipelineParameter;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class PipelineBusiness {

    private final static Logger logger = Logger.getLogger(PipelineBusiness.class);

    private final ApiContext apiContext;

    private final WorkflowBusiness workflowBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final ClassBusiness classBusiness;

    public PipelineBusiness(ApiContext apiContext) {
        this(apiContext, new WorkflowBusiness(), new ApplicationBusiness(), new ClassBusiness());
    }

    @Autowired
    public PipelineBusiness(ApiContext apiContext, WorkflowBusiness workflowBusiness, ApplicationBusiness applicationBusiness, ClassBusiness classBusiness) {
        this.apiContext = apiContext;
        this.workflowBusiness = workflowBusiness;
        this.applicationBusiness = applicationBusiness;
        this.classBusiness = classBusiness;
    }

    public Pipeline getPipeline(String pipelineId) throws ApiException {
        try {

            String applicationName = ApiUtils.getApplicationName(pipelineId);
            String applicationVersion = ApiUtils.getApplicationVersion(pipelineId);
            Pipeline p = getPipelineWithPermissions(applicationName, applicationVersion);

            Descriptor d = workflowBusiness.getApplicationDescriptor(apiContext.getUser(), p.getName(), p.getVersion()); // Be careful, this copies the Gwendia file from LFC.
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

    public Pipeline[] listPipelines(String studyIdentifier) throws ApiException {

        try {
            if (studyIdentifier != null) {
                apiContext.getWarnings().add("Study identifier was ignored.");
            }
            ArrayList<Pipeline> pipelines = new ArrayList<>();

            List<AppClass> classes = classBusiness.getUserClasses(apiContext.getUser().getEmail(), false);
            List<String> classNames = new ArrayList<>();
            for (AppClass c : classes) {
                classNames.add(c.getName());
            }

            List<Application> applications = applicationBusiness.getApplications(classNames);
            for (Application a : applications) {
                List<AppVersion> versions = applicationBusiness.getVersions(a.getName());
                for (AppVersion av : versions) {
                    pipelines.add(
                            new Pipeline(ApiUtils.getPipelineIdentifier(a.getName(), av.getVersion()), a.getName(), av.getVersion(), true)
                    );
                }
            }
            Pipeline[] array_pipelines = new Pipeline[pipelines.size()];
            return pipelines.toArray(array_pipelines);
        } catch (BusinessException ex) {
            logger.error("Error listing pipelines" + ex);
            throw new ApiException(ex);
        }
    }

    public void checkIfUserCanAccessPipeline(String pipelineId) throws ApiException {
        try {

            String applicationName = ApiUtils.getApplicationName(pipelineId);
            List<String> userClassNames = classBusiness.getUserClassesName(apiContext.getUser().getEmail(), false);

            Application a = applicationBusiness.getApplication(applicationName);
            if (a == null) {
                throw new ApiException("Cannot find application " + applicationName);
            }
            for (String applicationClassName : a.getApplicationClasses()) {
                if (userClassNames.contains(applicationClassName)) {
                    return;
                }
            }
            throw new ApiException("User " + apiContext.getUser().getEmail() + " not allowed to access application " + a.getName());
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    private Pipeline getPipelineWithPermissions(String applicationName, String applicationVersion) throws ApiException {
        Pipeline[] pipelines = listPipelines("");
        for (Pipeline p : pipelines) {
            if (p.getName().equals(applicationName) && p.getVersion().equals(applicationVersion)) {
                return p;
            }
        }
        throw new ApiException("Pipeline '" + applicationName + "' (version '" + applicationVersion + "') doesn't exist or user '"
                + apiContext.getUser().getEmail() + "' cannot access it");
    }
}
