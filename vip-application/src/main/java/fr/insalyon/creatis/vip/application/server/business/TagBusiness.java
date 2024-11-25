package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.server.dao.TagDAO;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class TagBusiness {
    
    private TagDAO tagDAO;

    @Autowired
    public TagBusiness(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public void add(Tag tag) throws BusinessException {
        try {
            tagDAO.add(tag);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void update(Tag tag, String newName) throws BusinessException {
        try {
            tagDAO.update(tag, newName);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void remove(Tag tag) throws BusinessException {
        try {
            tagDAO.remove(tag);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<Tag> getAll() throws BusinessException {
        try {
            return tagDAO.getAll();
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void associate(Tag tag, AppVersion appVersion) throws BusinessException {
        try {
            tagDAO.associate(tag, appVersion);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void dissociate(Tag tag, AppVersion appVersion) throws BusinessException {
        try {
            tagDAO.dissociate(tag, appVersion);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<Tag> getTags(AppVersion appVersion) throws BusinessException {
        try {
            return tagDAO.getTags(appVersion);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<AppVersion> getAssociated(Tag tag) throws BusinessException {
        try {
            return tagDAO.getAssociated(tag);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }
}
