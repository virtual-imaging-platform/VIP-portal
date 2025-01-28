package fr.insalyon.creatis.vip.application.server.dao;

import java.util.List;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

public interface TagDAO {

    public void add(Tag tag) throws DAOException;

    public void update(Tag tag, String newName) throws DAOException;

    public void remove(Tag tag) throws DAOException;

    public List<Tag> getAll() throws DAOException;

    public void associate(Tag tag, AppVersion appVersion) throws DAOException;

    public void dissociate(Tag tag, AppVersion appVersion) throws DAOException;

    public List<Tag> getTags(AppVersion appVersion) throws DAOException;

    public List<AppVersion> getAssociated(Tag tag) throws DAOException;
}
