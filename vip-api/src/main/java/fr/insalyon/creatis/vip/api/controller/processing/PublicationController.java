package fr.insalyon.creatis.vip.api.controller.processing;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.publication.models.Publication;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;

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
    public List<Publication> listPublication() throws VipException {
        logMethodInvocation(logger, "listPublication");
        return publicationBusiness.getPublications();
    }
}
