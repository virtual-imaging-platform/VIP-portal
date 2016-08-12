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
package fr.insalyon.creatis.vip.api.rest.controller;

import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.business.*;
import fr.insalyon.creatis.vip.api.rest.RestApiBusiness;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.application.server.business.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static fr.insalyon.creatis.vip.core.client.view.util.CountryCode.th;

/**
 * Created by abonnet on 7/28/16.
 */
@RestController
@RequestMapping("pipelines")
public class PipelineController {

    public static final Logger logger = Logger.getLogger(PipelineController.class);

    @Autowired
    private WorkflowBusiness workflowBusiness;
    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private ClassBusiness classBusiness;

    // although the controller is a singleton, this is a proxy that always point on the current request
    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping
    public Pipeline[] listPipelines(@RequestParam(required = false) String studyIdentifier) throws ApiException {
        ApiUtils.methodInvocationLog("listPipelines");
        ApiContext apiContext = new RestApiBusiness().getApiContext(httpServletRequest, true);
        PipelineBusiness pb = new PipelineBusiness(apiContext, workflowBusiness, applicationBusiness, classBusiness);
        return pb.listPipelines(studyIdentifier);
    }

    @RequestMapping("{pipelineId}")
    public Pipeline getPipeline(@PathVariable String pipelineId) throws ApiException {
        // TODO : correct that !
        try {
            pipelineId = URLDecoder.decode(pipelineId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApiException("Cannot decode input " + pipelineId);
        }
        ApiUtils.methodInvocationLog("getPipeline", pipelineId);
        ApiContext apiContext = new RestApiBusiness().getApiContext(httpServletRequest, true);
        PipelineBusiness pb = new PipelineBusiness(apiContext, workflowBusiness, applicationBusiness, classBusiness);
        pb.checkIfUserCanAccessPipeline(pipelineId);
        return pb.getPipeline(pipelineId);
    }

    @RequestMapping(params = "pipelineId")
    public Pipeline getPipelineWithRequestParam(@RequestParam String pipelineId) throws ApiException {
        ApiUtils.methodInvocationLog("getPipeline", pipelineId);
        ApiContext apiContext = new RestApiBusiness().getApiContext(httpServletRequest, true);
        PipelineBusiness pb = new PipelineBusiness(apiContext, workflowBusiness, applicationBusiness, classBusiness);
        pb.checkIfUserCanAccessPipeline(pipelineId);
        return pb.getPipeline(pipelineId);
    }
}
