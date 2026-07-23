package fr.insalyon.creatis.vip.publication.server.controller;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.publication.models.Publication;
import fr.insalyon.creatis.vip.publication.models.PublicationType;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;

@RestController
@RequestMapping("/publications")
public class PublicationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PublicationBusiness publicationBusiness;
    private final Supplier<User> userProvider;

    @Autowired
    public PublicationController(PublicationBusiness publicationBusiness, Supplier<User> userProvider) {
        this.publicationBusiness = publicationBusiness;
        this.userProvider = userProvider;
    }

    @GetMapping("/public")
    public List<Publication> listPublic() throws VipException {
        return publicationBusiness.getPublicPublications();
    }

    @GetMapping
    public List<Publication> list() throws VipException {
        return publicationBusiness.getPublications();
    }

    @GetMapping("/types")
    public PublicationType[] types() {
        return PublicationType.values();
    }

    @GetMapping("{id}")
    public Publication get(@PathVariable Long id) throws VipException {
        Publication publication = publicationBusiness.getPublication(id);

        if (publication == null) {
            throw new VipException(DefaultError.NOT_FOUND, Publication.class.getSimpleName(), id.toString());
        }
        return publication;
    }

    @PostMapping
    public void create(@Valid @RequestBody Publication publication) throws VipException {
        publicationBusiness.addPublication(publication);
    }

    @PutMapping("{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody Publication publication) throws VipException {
        if (publication.getId() == null || !id.equals(publication.getId())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "id", "Publication id do not match!");
        }

        logger.info("Update publication request: id='{}', title='{}', vipApplication='{}'",
            id, publication.getTitle(), publication.getVipApplication());

        publicationBusiness.updatePublication(publication);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws VipException {
        publicationBusiness.removePublication(id);
    }

    @PostMapping("/import/bibtex")
    public List<Publication> importBibtex(@RequestBody String bibtex) throws VipException {
        User currentUser = userProvider.get();
        String vipEmail = currentUser == null ? null : currentUser.getEmail();
        List<Publication> publications = publicationBusiness.parseBibtexText(bibtex, vipEmail);
        // Persist parsed publications
        for (Publication p : publications) {
            publicationBusiness.addPublication(p);
        }
        return publications;
    }
}
