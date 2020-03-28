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
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.List;

/**
 * Created by abonnet on 7/17/19.
 */
public class ExternalPlatformBusiness {

    private static final Logger logger = Logger.getLogger(ExternalPlatformBusiness.class);

    private GirderStorageBusiness girderStorageBusiness;

    public ExternalPlatformBusiness(GirderStorageBusiness girderStorageBusiness) {
        this.girderStorageBusiness = girderStorageBusiness;
    }

    public List<ExternalPlatform> listAll(Connection connection) throws BusinessException {
        try {
            return getExternalPlatformDAO(connection).getAll();
        } catch (DAOException e) {
            logger.error("Error listing all external platforms");
            throw new BusinessException(e);
        }
    }

    public static class ParseResult {
        final public Boolean isUri;
        final public String result;

        public ParseResult(Boolean isUri, String result) {
            this.isUri = isUri;
            this.result = result;
        }
    }

    public ParseResult parseParameter(
        String parameterName, String parameterValue,
        User user, Connection connection)
        throws BusinessException {
        if (!parameterValue.matches("^\\w+:.*")) {
             return new ParseResult(false, parameterValue);
        }
        int indexOfColon = parameterValue.indexOf(':');
        String platformIdentifier = parameterValue.substring(0, indexOfColon);
        String fileIdentifier = parameterValue.substring(indexOfColon + 1);
        ExternalPlatform externalPlatform =
                getById(platformIdentifier, connection);
        if (externalPlatform == null) {
            String error = "Cannot find external platform : " + platformIdentifier;
            logger.error(error);
            throw new BusinessException(error);
        }
        switch (externalPlatform.getType()) {
            case GIRDER:
                String girderUri = girderStorageBusiness.generateUri(
                    externalPlatform, parameterName,
                    fileIdentifier, user, connection);
                return new ParseResult(true, girderUri);
            default:
                String error = "Only girder external storage are supported. "
                        + " (found : " + externalPlatform.getType() + " )";
                logger.error(error);
                throw new BusinessException(error);
        }
    }

    private ExternalPlatform getById(String identifier, Connection connection) throws BusinessException {
        try {
            return getExternalPlatformDAO(connection).getById(identifier);
        } catch (DAOException e) {
            logger.error("Error getting external platform : " + identifier);
            throw new BusinessException(e);
        }
    }

    private ExternalPlatformsDAO getExternalPlatformDAO(Connection connection)
            throws BusinessException {
        try {
            return DataManagerDAOFactory.getInstance()
                    .getExternalPlatformsDAO(connection);
        } catch (DAOException e) {
            logger.error("Error on ExternalPlatformsDAO creation");
            throw new BusinessException(e);
        }
    }

}
