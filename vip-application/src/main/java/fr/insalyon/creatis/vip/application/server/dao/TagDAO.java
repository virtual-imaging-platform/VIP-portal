package fr.insalyon.creatis.vip.application.server.dao;

import java.util.List;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

public interface TagDAO {

    public void add(Tag tag) throws DAOException;

    public void update(Tag oldTag, Tag newTag) throws DAOException;

    public void remove(Tag tag) throws DAOException;

    public Tag get(String key, String value, String application, String version) throws DAOException;

    public List<Tag> getAll() throws DAOException;

    public List<Tag> getTags(AppVersion appVersion) throws DAOException;
}
