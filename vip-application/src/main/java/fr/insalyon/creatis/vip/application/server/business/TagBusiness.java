package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.server.dao.TagDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class TagBusiness {
    
    private TagDAO tagDAO;

    @Autowired
    public TagBusiness(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public void add(Tag tag) throws VipException {
        try {
            tagDAO.add(tag);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void addOrUpdate(Tag tag) throws VipException {
        if ( ! exist(tag)) {
            add(tag);
        } else {
            update(tag, tag);
        }
    }

    public void update(Tag oldTag, Tag newTag) throws VipException {
        try {
            tagDAO.update(oldTag, newTag);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void remove(Tag tag) throws VipException {
        try {
            tagDAO.remove(tag);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public boolean exist(Tag tag) throws VipException {
        try {
            return tagDAO.get(tag.getKey(), tag.getValue(), tag.getApplication(), tag.getVersion()) != null;
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Tag> getAll() throws VipException {
        try {
            return tagDAO.getAll();
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<Tag> getTags(AppVersion appVersion) throws VipException {
        try {
            return tagDAO.getTags(appVersion);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }
}
