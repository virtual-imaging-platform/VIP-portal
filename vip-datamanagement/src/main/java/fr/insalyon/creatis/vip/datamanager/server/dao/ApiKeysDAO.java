package fr.insalyon.creatis.vip.datamanager.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.models.UserApiKey;

import java.util.List;

public interface ApiKeysDAO {
    void addKey(UserApiKey apiKey) throws DAOException;

    void updateKey(UserApiKey apiKey) throws DAOException;

    List<UserApiKey> getByUser(String userEmail) throws DAOException;

    void deleteKeyFor(String userEmail, String storageIdentifier)
        throws DAOException;
}
