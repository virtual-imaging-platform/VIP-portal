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

    public void addOrUpdate(Tag tag) throws BusinessException {
        if ( ! exist(tag)) {
            add(tag);
        } else {
            update(tag, tag);
        }
    }

    public void update(Tag oldTag, Tag newTag) throws BusinessException {
        try {
            tagDAO.update(oldTag, newTag);
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

    public boolean exist(Tag tag) throws BusinessException {
        try {
            return tagDAO.get(tag.getName(), tag.getApplication(), tag.getVersion()) != null;
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

    public List<Tag> getTags(AppVersion appVersion) throws BusinessException {
        try {
            return tagDAO.getTags(appVersion);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }
}
