/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
