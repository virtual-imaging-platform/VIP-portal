package fr.insalyon.creatis.vip.datamanager.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.*;

import java.util.List;

public interface ExternalPlatformsDAO {

    ExternalPlatform getById(String identifier) throws DAOException;

    List<ExternalPlatform> getAll() throws DAOException;
}
