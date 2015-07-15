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
package fr.insalyon.creatis.vip.core.server.auth;

import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.SamlTokenValidator;
import fr.insalyon.creatis.vip.core.server.business.Server;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
public class SamlAuthenticationService extends AbstractAuthenticationService {

    private static final Logger logger = Logger.getLogger(SamlAuthenticationService.class);

    @Override
    protected String getEmailIfValidRequest(HttpServletRequest request) throws BusinessException {
        logger.info("SAML authentication request");

        String token = request.getParameter("_saml_token");
        if (token == null) {
            throw new BusinessException("SAML token is null");
        }

        String email;
        try {
            email = SamlTokenValidator.getEmailIfValid(token, Server.getInstance().getSamlTrustedCertificate(), request.getRequestURL().toString());
        } catch (CoreException ex) {
            logger.info(ex.getMessage());
            throw new BusinessException(ex);
        }
        return email;
    }

    @Override
    public String getDefaultAccountType() {
        return Server.getInstance().getSAMLDefaultAccountType();
    }
}
