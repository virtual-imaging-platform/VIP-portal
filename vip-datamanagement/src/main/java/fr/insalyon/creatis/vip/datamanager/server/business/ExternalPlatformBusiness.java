/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;

/**
 * Created by abonnet on 7/17/19.
 */
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

    public List<ExternalPlatform> listAll() throws BusinessException {
        try {
            return externalPlatformsDAO.getAll();
        } catch (DAOException e) {
            throw new BusinessException(e);
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
            throws BusinessException {
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
            throw new BusinessException(error);
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
                throw new BusinessException(error);
        }
    }

    private ExternalPlatform getById(String identifier) throws BusinessException {
        try {
            return externalPlatformsDAO.getById(identifier);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

}
