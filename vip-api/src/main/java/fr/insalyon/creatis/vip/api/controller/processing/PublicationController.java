package fr.insalyon.creatis.vip.api.controller.processing;

import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.business.PipelineBusiness;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("publication")
public class PublicationController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PipelineBusiness pipelineBusiness;

    @Autowired
    protected PublicationController (PipelineBusiness pipelineBusiness) {
        this.pipelineBusiness = pipelineBusiness;
    }


    @RequestMapping
    public List<Publication> listPublication() throws ApiException {
        logMethodInvocation(logger, "listPublication");
        return pipelineBusiness.listPublication();
    }





}
