package fr.insalyon.creatis.vip.publication.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;

import java.util.List;

public interface PublicationDAO {

    public void add(Publication publication) throws DAOException;

    public void update(Publication publication) throws DAOException;

    public void remove(Long publicationID) throws DAOException;
    
    public Publication getPublication(Long publicationID) throws DAOException;

    public List<Publication> getList() throws DAOException;
}
