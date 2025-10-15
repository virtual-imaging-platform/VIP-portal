package fr.insalyon.creatis.vip.api.controller.processing;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.vip.api.business.PipelineBusiness;
import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.Pipeline;

@RestController
@RequestMapping("pipelines")
public class PipelineController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PipelineBusiness pipelineBusiness;

    @Autowired
    protected PipelineController(PipelineBusiness pipelineBusiness) {
        this.pipelineBusiness = pipelineBusiness;
    }

    @RequestMapping
    public List<Pipeline> listPipelines(
            @RequestParam(required = false) String studyIdentifier) throws ApiException {
        logMethodInvocation(logger, "listPipelines", studyIdentifier);
        return pipelineBusiness.listPipelines(studyIdentifier);
    }

    @RequestMapping(params = "public")
    public List<Pipeline> listPublicPipelines() throws ApiException {
        logMethodInvocation(logger, "listPublicPipelines");
        return pipelineBusiness.listPublicPipelines();
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
        return pipelineBusiness.getPipelineWithoutResultsDirectory(pipelineId);
    }

    @RequestMapping(value = "{pipelineId}", params = {"format=boutiques"})
    public BoutiquesDescriptor getBoutiquesDescriptor(@PathVariable String pipelineId) throws ApiException {
        logMethodInvocation(logger, "getBoutiquesDescriptor", pipelineId);
        try {
            pipelineId = URLDecoder.decode(pipelineId, "UTF8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error decoding pipelineid {}", pipelineId, e);
            throw new ApiException("cannot decode pipelineId : " + pipelineId);
        }
        return pipelineBusiness.getBoutiquesDescriptor(pipelineId);
    }

    @RequestMapping("{pipelineIdFirstPart}/{pipelineIdSecondPart}")
    public Pipeline getPipeline(@PathVariable String pipelineIdFirstPart,
                                @PathVariable String pipelineIdSecondPart) throws ApiException {
        return getPipeline(pipelineIdFirstPart + "/" + pipelineIdSecondPart);
    }

    @RequestMapping(value = "{pipelineIdFirstPart}/{pipelineIdSecondPart}", params = {"format=boutiques"})
    public BoutiquesDescriptor getBoutiquesDescriptor(@PathVariable String pipelineIdFirstPart,
                                @PathVariable String pipelineIdSecondPart) throws ApiException {
        return getBoutiquesDescriptor(pipelineIdFirstPart + "/" + pipelineIdSecondPart);
    }

    @RequestMapping(params = "pipelineId")
    public Pipeline getPipelineWithRequestParam(@RequestParam String pipelineId) throws ApiException {
        return getPipeline(pipelineId);
    }
}
