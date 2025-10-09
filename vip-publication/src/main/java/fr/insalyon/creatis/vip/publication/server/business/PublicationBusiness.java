package fr.insalyon.creatis.vip.publication.server.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.publication.server.dao.PublicationDAO;

@Service
@Transactional
public class PublicationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PublicationDAO publicationDAO;

    public PublicationBusiness(PublicationDAO publicationDAO) {
        this.publicationDAO = publicationDAO;
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

    public void removePublication(Long id)
            throws VipException {
        try {
            publicationDAO.remove(id);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void addPublication(Publication pub)
            throws VipException {
        try {
            publicationDAO.add(pub);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void updatePublication(Publication pub)
            throws VipException {
        try {
            publicationDAO.update(pub);
        } catch (DAOException ex) {
            throw new VipException(ex);
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


}
