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
import fr.insalyon.creatis.vip.api.bean.pairs.PairOfPipelineAndBooleanLists;
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
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
public class PipelineBusiness extends ApiBusiness {

    private final static Logger logger = Logger.getLogger(PipelineBusiness.class);

    public PipelineBusiness(WebServiceContext wsContext) throws ApiException {
        super(wsContext,true);
    }

    public Pipeline getPipeline(String pipelineId) throws ApiException {
        try {
            logger.info("Calling API method getPipeline(" + pipelineId + ")");
            String applicationName = getApplicationName(pipelineId);
            String applicationVersion = getApplicationVersion(pipelineId);
            Pipeline p = getPipelineWithPermissions(applicationName,applicationVersion);
            
            
            WorkflowBusiness wb = new WorkflowBusiness();
            Descriptor d = wb.getApplicationDescriptor(getUser(), p.getName(), p.getVersion()); // Be careful, this copies the Gwendia file from LFC. 
            p.setDescription(d.getDescription());
            
            for(Source s : d.getSources()){
                ParameterType sourceType = getCarminType(s.getType());
                ParameterTypedValue defaultValue = s.getDefaultValue() == null ? null : new ParameterTypedValue(sourceType,s.getDefaultValue());
                PipelineParameter pp = new PipelineParameter(s.getName(),
                        sourceType,
                        s.isOptional(),
                        false,
                        defaultValue,
                        s.getDescription());
                p.getParameters().add(pp);
            }
            return p;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

    public PairOfPipelineAndBooleanLists listPipelines(String studyIdentifier) throws ApiException {

        try {
            logger.info("Calling API method listPipelines(" + studyIdentifier + ")");
            ApplicationBusiness ab = new ApplicationBusiness();
            PairOfPipelineAndBooleanLists response = new PairOfPipelineAndBooleanLists();

            ClassBusiness classBusiness = new ClassBusiness();
            List<AppClass> classes = classBusiness.getUserClasses(getUser().getEmail(), false);
            List<String> classNames = new ArrayList<>();
            for (AppClass c : classes) {
                classNames.add(c.getName());
            }

            List<Application> applications = ab.getApplications(classNames);
            for (Application a : applications) {
                List<AppVersion> versions = ab.getVersions(a.getName());
                for (AppVersion av : versions) {
                    Pipeline p = new Pipeline(getPipelineIdentifier(a.getName(), av.getVersion()), a.getName(), av.getVersion());
                    response.getPipelines().add(p);
                    response.getCanExecute().add(true);
                }
            }
            return response;
        } catch (BusinessException ex) {
            logger.error(ex);
            throw new ApiException(ex);
        }
    }

    // static methods
    
    /**
     * Returns a unique identifier from the VIP application name and version. 
     * @param applicationName
     * @param applicationVersion
     * @return 
     */
    public static String getPipelineIdentifier(String applicationName, String applicationVersion) {
        return applicationName + "/" + applicationVersion;
    }
    
    /**
     * Returns the Carmin type from Gwendia type
     * @param vipType
     * @return 
     */
    public static ParameterType getCarminType(String vipType){
        return vipType.equals("URI") ? ParameterType.File : ParameterType.String;
    }
    
    public static String getApplicationName(String identifier) {
        return identifier.substring(0, identifier.lastIndexOf("/"));
    }

    public static String getApplicationVersion(String identifier) {
        return identifier.substring(identifier.lastIndexOf("/")+1);
    }
    
    // private methods
    
    private void checkIfValidIdentifier(String identifier) throws ApiException {
        if (!identifier.contains("/")) {
            throw new ApiException("Invalid pipeline identifier: " + identifier);
        }
    }

    private Pipeline getPipelineWithPermissions(String applicationName, String applicationVersion) throws ApiException {
        PairOfPipelineAndBooleanLists popabl = listPipelines("");
        for (Pipeline p : popabl.pipelines) {
            if (p.getName().equals(applicationName) && p.getVersion().equals(applicationVersion)) {
                return p;
            }
        }
        throw new ApiException("Pipeline '" + applicationName + "' (version '"+applicationVersion+"') doesn't exist or user '" + getUser().getEmail() + "' cannot access it");
    }
}
