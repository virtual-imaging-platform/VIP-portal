package fr.insalyon.creatis.vip.publication.server.rpc;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.publication.client.rpc.PublicationService;
import fr.insalyon.creatis.vip.publication.client.view.PublicationTypes;
import fr.insalyon.creatis.vip.publication.models.Publication;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;
import jakarta.servlet.ServletException;

public class PublicationServiceImpl extends AbstractRemoteServiceServlet implements PublicationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private PublicationBusiness publicationBusiness;

    public PublicationServiceImpl() {
        logger.info("PublicationServiceImpl: Creating PublicationBusiness.");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        publicationBusiness = getBean(PublicationBusiness.class);
    }

    @Override
    public List<Publication> getPublications() throws CoreException {
        trace(logger, "Getting publication list.");
        try {
            return publicationBusiness.getPublications();
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void removePublication(Long id) throws CoreException {
        trace(logger, "Removing publication.");

        try {
            User user = getSessionUser();
            if (user.isSystemAdministrator() ||
                    publicationBusiness.getPublication(id).getVipAuthor()
                            .equals(user.getEmail())) {
                publicationBusiness.removePublication(id);
            } else {
                logger.error("{} cannot remove publication {} because it's not his",
                        user, id);
                throw new CoreException("you can't remove a publication that is not yours");
            }
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void addPublication(Publication pub) throws CoreException {
        trace(logger, "Adding publication.");

        try {
            User user = getSessionUser();
            pub.setVipAuthor(user.getEmail());
            publicationBusiness.addPublication(pub);
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public void updatePublication(Publication pub) throws CoreException {
        trace(logger, "Updating publication.");

        try {
            User user = getSessionUser();
            if (user.isSystemAdministrator() ||
                    publicationBusiness.getPublication(pub.getId()).getVipAuthor()
                            .equals(user.getEmail())) {
                pub.setVipAuthor(user.getEmail());
                publicationBusiness.updatePublication(pub);
            } else {
                logger.error("{} cannot modify publication {} because its not his",
                        user.getEmail(), pub.getId());
                throw new CoreException("you can't modify a publication that is not yours");
            }
        } catch (VipException ex) {
            throw new CoreException(ex);
        }
    }

    @Override
    public List<Publication> parseBibtexText(String s) throws CoreException {
        List<Publication> publications = new ArrayList<Publication>();
        try {
            Reader reader = new StringReader(s);
            org.jbibtex.BibTeXParser bibtexParser = new org.jbibtex.BibTeXParser();
            org.jbibtex.BibTeXDatabase database = bibtexParser.parseFully(reader);
            Map<org.jbibtex.Key, org.jbibtex.BibTeXEntry> entryMap = database.getEntries();
            Collection<org.jbibtex.BibTeXEntry> entries = entryMap.values();
            for (org.jbibtex.BibTeXEntry entry : entries) {
                String type = entry.getType().toString();
                org.jbibtex.Value title = entry.getField(org.jbibtex.BibTeXEntry.KEY_TITLE);
                org.jbibtex.Value date = entry.getField(org.jbibtex.BibTeXEntry.KEY_YEAR);
                org.jbibtex.Value doi = entry.getField(org.jbibtex.BibTeXEntry.KEY_DOI);
                org.jbibtex.Value authors = entry.getField(org.jbibtex.BibTeXEntry.KEY_AUTHOR);
                org.jbibtex.Value typeName = entry.getField(org.jbibtex.BibTeXEntry.KEY_BOOKTITLE);
                String doiv;
                if (doi == null) {
                    doiv = "";
                } else {
                    doiv = doi.toUserString();
                }
                //TODO Sorina : handle VIPApplication in this case
                String VipApplication = "";
                publications.add(new Publication(title.toUserString(), date.toUserString(), doiv, authors.toUserString(), parseTypePublication(type), getTypeName(entry, type), getSessionUser().getEmail(), VipApplication));

            }

        } catch (ParseException | TokenMgrException ex) {
            logger.error("Error parsing publication {}", s, ex);
            throw new CoreException(ex);
        }
        return publications;
    }

    private String parseTypePublication(String type) {
        if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference")) {
            return PublicationTypes.ConferenceArticle.toString();
        } else if (type.equalsIgnoreCase("article")) {
            return PublicationTypes.Journal.toString();
        } else if (type.equalsIgnoreCase("inbook") || type.equalsIgnoreCase("incollection")) {
            return PublicationTypes.BookChapter.toString();
        } else {
            return PublicationTypes.Other.toString();
        }

    }

    private String getTypeName(org.jbibtex.BibTeXEntry entry, String type) {
        if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference") || type.equalsIgnoreCase("incollection")) {
            return entry.getField(org.jbibtex.BibTeXEntry.KEY_BOOKTITLE).toUserString();
        } else if (type.equalsIgnoreCase("article")) {
            return entry.getField(org.jbibtex.BibTeXEntry.KEY_JOURNAL).toUserString();
        } else {
            return "";
        }

    }


}
