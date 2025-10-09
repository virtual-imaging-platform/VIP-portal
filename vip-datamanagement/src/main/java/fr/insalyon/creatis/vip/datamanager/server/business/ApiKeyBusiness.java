package fr.insalyon.creatis.vip.datamanager.server.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.UserApiKey;
import fr.insalyon.creatis.vip.datamanager.server.dao.ApiKeysDAO;

@Service
@Transactional
public class ApiKeyBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApiKeysDAO apiKeysDAO;

    @Autowired
    public ApiKeyBusiness(ApiKeysDAO apiKeysDAO) {
        this.apiKeysDAO = apiKeysDAO;
    }


    public List<UserApiKey> apiKeysFor(String userEmail) throws VipException {

        try {
            return apiKeysDAO.getByUser(userEmail);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void addOrUpdateApiKey(
            String storageIdentifier, String userEmail, String apiKey)
            throws VipException {

        try {

            List<UserApiKey> keys = apiKeysDAO.getByUser(userEmail);
            UserApiKey newKey =
                new UserApiKey(storageIdentifier, userEmail, apiKey);

            if (keys.stream().anyMatch(
                    k ->
                    k.getStorageIdentifier().equals(storageIdentifier)
                    && k.getUserEmail().equals(userEmail))) {
                apiKeysDAO.updateKey(newKey);
            } else {
                apiKeysDAO.addKey(newKey);
            }
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void deleteApiKey(String storageIdentifier, String userEmail)
            throws VipException {

        try {
            apiKeysDAO.deleteKeyFor(userEmail, storageIdentifier);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }
}
