package fr.insalyon.creatis.vip.api.controller.processing;

import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("publications")
public class PublicationController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PublicationBusiness publicationBusiness;

    @Autowired
    protected PublicationController (PublicationBusiness publicationBusiness) {
        this.publicationBusiness = publicationBusiness;
    }

    @RequestMapping
    public List<Publication> listPublication() throws ApiException {
        logMethodInvocation(logger, "listPublication");
        try {
            return publicationBusiness.getPublications();
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }
}
