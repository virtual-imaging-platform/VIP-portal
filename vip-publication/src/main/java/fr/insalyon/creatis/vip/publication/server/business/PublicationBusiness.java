package fr.insalyon.creatis.vip.publication.server.business;

import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.publication.models.Publication;
import fr.insalyon.creatis.vip.publication.models.PublicationType;
import fr.insalyon.creatis.vip.publication.server.dao.PublicationDAO;

@Service
@Transactional
public class PublicationBusiness extends CommonBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PublicationDAO publicationDAO;

    public PublicationBusiness(PublicationDAO publicationDAO) {
        this.publicationDAO = publicationDAO;
    }

    public List<Publication> parseBibtexText(String s, String vipAuthor) throws VipException {
        List<Publication> publications = new ArrayList<>();
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
                String doiv = (doi == null) ? "" : doi.toUserString();
                // TODO: handle vipApplication from bibtex entry if needed
                String vipApplication = "";
                publications.add(new Publication(
                        title == null ? "" : title.toUserString(),
                        date == null ? "" : date.toUserString(),
                        doiv,
                        authors == null ? "" : authors.toUserString(),
                        parseTypePublication(type),
                        getTypeName(entry, type),
                        vipAuthor,
                        vipApplication));
            }

        } catch (org.jbibtex.ParseException | org.jbibtex.TokenMgrException ex) {
            logger.error("Error parsing publication {}", s, ex);
            throw new VipException(ex);
        }
        return publications;
    }

    private PublicationType parseTypePublication(String type) {
        if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference")) {
            return PublicationType.ConferenceArticle;
        } else if (type.equalsIgnoreCase("article")) {
            return PublicationType.Journal;
        } else if (type.equalsIgnoreCase("inbook") || type.equalsIgnoreCase("incollection")) {
            return PublicationType.BookChapter;
        } else {
            return PublicationType.Other;
        }

    }

    private String getTypeName(org.jbibtex.BibTeXEntry entry, String type) {
        if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference") || type.equalsIgnoreCase("incollection")) {
            org.jbibtex.Value v = entry.getField(org.jbibtex.BibTeXEntry.KEY_BOOKTITLE);
            return v == null ? "" : v.toUserString();
        } else if (type.equalsIgnoreCase("article")) {
            org.jbibtex.Value v = entry.getField(org.jbibtex.BibTeXEntry.KEY_JOURNAL);
            return v == null ? "" : v.toUserString();
        } else {
            return "";
        }

    }


    /**
     *
     * @return @throws VipException
     */
    public List<Publication> getPublications()
            throws VipException {
        logger.debug("*******************PublicationBusiness getPublications*******************");
        try {
            return publicationDAO.getList();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Publication> getPublicPublications() throws VipException {
        try {
            return publicationDAO.getList().stream()
                    .sorted(Comparator.comparing((Publication p) -> parseDate(p.getDate())).reversed())
                    .toList();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr.trim());
        } catch (java.text.ParseException e) {
            try {
                return new SimpleDateFormat("yyyy").parse(dateStr.trim());
            } catch (java.text.ParseException e2) {
                return new Date(0);
            }
        }
    }

    public void removePublication(Long id)
            throws VipException {
        try {
            Publication existing = publicationDAO.getPublication(id);
            if (existing == null) {
                throw new VipException(DefaultError.NOT_FOUND, Publication.class.getSimpleName(), id.toString());
            }

            ensureAdminOrAuthor(existing);
            publicationDAO.remove(id);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void addPublication(Publication pub)
            throws VipException {
        try {
            pub.setVipAuthor(getUser().getEmail());
            logger.info("Create publication request: title='{}', vipAuthor='{}', vipApplication='{}'",
                pub.getTitle(), pub.getVipAuthor(), pub.getVipApplication());
            assertDataIsOK(pub);
            publicationDAO.add(pub);
            logger.debug("Publication created: title='{}', vipAuthor='{}', vipApplication='{}'",
                    pub.getTitle(), pub.getVipAuthor(), pub.getVipApplication());
        } catch (DAOException ex) {
            throw new VipException(ex);
        } catch (VipException ex) {
            throw ex;
        }
    }

    public void updatePublication(Publication pub)
            throws VipException {
        try {
            Publication existing = publicationDAO.getPublication(pub.getId());
            if (existing == null) {
                throw new VipException(DefaultError.NOT_FOUND, Publication.class.getSimpleName(), pub.getId().toString());
            }

            ensureAdminOrAuthor(existing);
            // Keep creator immutable to avoid ownership spoofing from request payload.
            pub.setVipAuthor(existing.getVipAuthor());
            assertDataIsOK(pub);
            publicationDAO.update(pub);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    private void assertDataIsOK(Publication publication) throws VipException {
        logger.debug("Publication payload values before validation: title='{}' (len={}), authors='{}' (len={}), date='{}', doi='{}', type='{}', typeName='{}', vipApplication='{}'",
                publication.getTitle(),
                publication.getTitle() == null ? -1 : publication.getTitle().length(),
                publication.getAuthors(),
                publication.getAuthors() == null ? -1 : publication.getAuthors().length(),
                publication.getDate(),
                publication.getDoi(),
                publication.getType(),
                publication.getTypeName(),
                publication.getVipApplication());

        CoreUtil.assertOnlyLatin1Characters(publication.getTitle());
        CoreUtil.assertOnlyLatin1Characters(publication.getDate());
        CoreUtil.assertOnlyLatin1Characters(publication.getAuthors());
        if (publication.getDoi() != null && !publication.getDoi().trim().isEmpty()) {
            CoreUtil.assertOnlyLatin1Characters(publication.getDoi());
        }
        CoreUtil.assertOnlyLatin1Characters(publication.getTypeName());
        if (publication.getVipApplication() == null || publication.getVipApplication().isBlank()) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "vipApplication", "VIP application is required!");
        }
        
    }
    

    public Publication getPublication(Long id)
            throws VipException {
        try {
            return publicationDAO.getPublication(id);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    private void ensureAdminOrAuthor(Publication publication) throws VipException {
        User currentUser = getUser();
        if (currentUser == null) {
            throw new VipException(DefaultError.ACCESS_DENIED);
        }

        if (!currentUser.isSystemAdministrator() && !isAuthor(currentUser, publication)) {
            throw new VipException(DefaultError.ACCESS_DENIED);
        }
    }

    private boolean isAuthor(User currentUser, Publication publication) {
        return currentUser.getEmail() != null
                && publication.getVipAuthor() != null
                && currentUser.getEmail().equalsIgnoreCase(publication.getVipAuthor());
    }


}
