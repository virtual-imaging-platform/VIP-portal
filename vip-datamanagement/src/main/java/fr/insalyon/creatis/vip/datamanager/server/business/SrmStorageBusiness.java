package fr.insalyon.creatis.vip.datamanager.server.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform.Type;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;

@Service
@Transactional
public class SrmStorageBusiness {
	private final Logger logger = LoggerFactory.getLogger(getClass());
		
	private LfcPathsBusiness lfcPathsBusiness;
	
    	@Autowired
    	public SrmStorageBusiness(LfcPathsBusiness lfcPathsBusiness) {
		this.lfcPathsBusiness = lfcPathsBusiness; 
	}
	
	public String generateUri(ExternalPlatform externalPlatform, String fileIdentifier, User user) throws VipException {
		verifyExternalPlatform(externalPlatform);
		String userFolderPath = "";
		try {
			userFolderPath = lfcPathsBusiness.parseBaseDir(user, fileIdentifier);	
		} catch (DataManagerException e) {
			throw new VipException(e);	
		}
		return buildUri(externalPlatform.getUrl(), userFolderPath);
	}

	private void verifyExternalPlatform(ExternalPlatform externalPlatform) throws VipException {
		if (!externalPlatform.getType().equals(Type.SRM)) {
			logger.error("Trying to generate a srm URI for a non srm storage {}", externalPlatform.getType());
			throw new VipException("Cannot generate srm uri");
		}
		if (externalPlatform.getUrl() == null) {
			logger.error("A srm external storage must have an URL to generate an URI");
			throw new VipException("Cannot generate srm uri");
		}
	}
	private String buildUri(String apiUrl, String userFolderPath) {
		return apiUrl + "/" + (DataManagerConstants.USERS_HOME).toLowerCase() + userFolderPath;
	}
}
