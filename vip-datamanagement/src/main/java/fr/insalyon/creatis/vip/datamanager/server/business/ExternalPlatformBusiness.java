package fr.insalyon.creatis.vip.datamanager.server.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.dao.ExternalPlatformsDAO;

@Service
@Transactional
public class ExternalPlatformBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GirderStorageBusiness girderStorageBusiness;
    private ShanoirStorageBusiness shanoirStorageBusiness;
    private SrmStorageBusiness srmStorageBusiness;
    private ExternalPlatformsDAO externalPlatformsDAO;

    @Autowired
    public ExternalPlatformBusiness(
            GirderStorageBusiness girderStorageBusiness,
            ShanoirStorageBusiness shanoirStorageBusiness,
            SrmStorageBusiness srmStorageBusiness,
            ExternalPlatformsDAO externalPlatformsDAO) {
        this.girderStorageBusiness = girderStorageBusiness;
        this.shanoirStorageBusiness = shanoirStorageBusiness;
        this.srmStorageBusiness = srmStorageBusiness;
        this.externalPlatformsDAO = externalPlatformsDAO;
    }

    public List<ExternalPlatform> listAll() throws VipException {
        try {
            return externalPlatformsDAO.getAll();
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public static class ParseResult {
        final public Boolean isUri;
        final public String result;

        private ParseResult(Boolean isUri, String result) {
            this.isUri = isUri;
            this.result = result;
        }
    }

    public ParseResult parseParameter(
            String parameterName, String parameterValue, User user)
            throws VipException {
        if ( ! parameterValue.matches("^\\w+:.*")) {
             return new ParseResult(false, parameterValue);
        }
        int indexOfColon = parameterValue.indexOf(':');
        String platformIdentifier = parameterValue.substring(0, indexOfColon);

        if ("file".equals(platformIdentifier)) {
            // its a local file keep it as it is
            return new ParseResult(true, parameterValue);
        }

        String fileIdentifier = parameterValue.substring(indexOfColon + 1);
        ExternalPlatform externalPlatform = getById(platformIdentifier);
        if (externalPlatform == null) {
            String error = "Cannot find external platform : " + platformIdentifier;
            logger.error(error);
            throw new VipException(error);
        }
        switch (externalPlatform.getType()) {
            case GIRDER:
                String girderUri = girderStorageBusiness.generateUri(
                    externalPlatform, parameterName,
                    fileIdentifier, user);
                return new ParseResult(true, girderUri);
            case SHANOIR:
                String shanoirUri = shanoirStorageBusiness.generateUri(
                        externalPlatform, parameterName, parameterValue
                );
                return new ParseResult(true, shanoirUri);
            case SRM:
	        String srmUri = srmStorageBusiness.generateUri(externalPlatform,
			fileIdentifier, user);
	        return new ParseResult(true, srmUri);
            default:
                String error = "Only girder, shanoir and srm external storages are supported. "
                        + " (found : " + externalPlatform.getType() + " )";
                logger.error(error);
                throw new VipException(error);
        }
    }

    private ExternalPlatform getById(String identifier) throws VipException {
        try {
            return externalPlatformsDAO.getById(identifier);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

}
