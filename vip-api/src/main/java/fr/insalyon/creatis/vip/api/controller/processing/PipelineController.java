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
package fr.insalyon.creatis.vip.api.controller.processing;

import fr.insalyon.creatis.vip.api.business.PipelineBusiness;
import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.function.Supplier;

/**
 * Created by abonnet on 7/28/16.
 *
 */
@RestController
@RequestMapping("pipelines")
public class PipelineController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PipelineBusiness pipelineBusiness;

    @Autowired
    protected PipelineController(
            Supplier<User> currentUserSupplier, PipelineBusiness pipelineBusiness) {
        super(currentUserSupplier);
        this.pipelineBusiness = pipelineBusiness;
    }

    @RequestMapping
    public Pipeline[] listPipelines(
            @RequestParam(required = false) String studyIdentifier) throws ApiException {
        logMethodInvocation(logger, "listPipelines", studyIdentifier);
        return pipelineBusiness.listPipelines(studyIdentifier);
    }

    @RequestMapping("{pipelineId}")
    public Pipeline getPipeline(@PathVariable String pipelineId) throws ApiException {
        logMethodInvocation(logger, "getPipeline", pipelineId);
        try {
            pipelineId = URLDecoder.decode(pipelineId, "UTF8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error decoding pipelineid {}", pipelineId, e);
            throw new ApiException("cannot decode pipelineId : " + pipelineId);
        }
        pipelineBusiness.checkIfUserCanAccessPipeline(pipelineId);
        return pipelineBusiness.getPipeline(pipelineId);
    }

    @RequestMapping("{pipelineIdFirstPart}/{pipelineIdSecondPart}")
    public Pipeline getPipeline(@PathVariable String pipelineIdFirstPart,
                                @PathVariable String pipelineIdSecondPart) throws ApiException {
        return getPipeline(pipelineIdFirstPart + "/" + pipelineIdSecondPart);
    }

    @RequestMapping(params = "pipelineId")
    public Pipeline getPipelineWithRequestParam(@RequestParam String pipelineId) throws ApiException {
        return getPipeline(pipelineId);
    }
}
