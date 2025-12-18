package fr.insalyon.creatis.vip.datamanager.server.dao;

import java.util.List;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.models.ExternalPlatform;

public interface ExternalPlatformsDAO {

    ExternalPlatform getById(String identifier) throws DAOException;

    List<ExternalPlatform> getAll() throws DAOException;
}
