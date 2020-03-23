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

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.SamlTokenValidator;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.validation.ValidationException;

/**
 *
 * @author Tristan Glatard
 */
public class SamlAuthenticationService extends AbstractAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(SamlAuthenticationService.class);
    private Assertion assertion;
    private Issuer issuer;

    @Override
    protected void checkValidRequest(HttpServletRequest request) throws BusinessException {
        logger.info("SAML authentication request");

        String token = request.getParameter("_saml_token");
        if (token == null) {
            throw new BusinessException("SAML token is null");
        }

        // Get the SAML assertion in XML form
        // SAML assertion might be encoded in base64 (happens in FLI)
        byte[] xmlAssertion = token.getBytes();
        if (Base64.isBase64(xmlAssertion)) {
            xmlAssertion = Base64.decodeBase64(xmlAssertion);
        }

        // Get an Assertion object from XML assertion
        assertion = null;
        try {
            try {
                // XML object might be a saml response. In this case, take the first assertion found
                // (happens in FLI)
                Response samlResponse = (Response) SamlTokenValidator.getSAMLObject(xmlAssertion);
                List<Assertion> assertions = samlResponse.getAssertions();
                assertion = assertions.get(0);
            } catch (ClassCastException ex) {
                // Otherwise, try to cast directly the XML object to Assertion (happens in Neugrid).
                assertion = (Assertion) SamlTokenValidator.getSAMLObject(xmlAssertion);
            }
        } catch (UnsupportedEncodingException | ConfigurationException | XMLParserException | UnmarshallingException ex) {
            logger.error("Error getting SAML assertion {}", new String(xmlAssertion), ex);
        }
        if (assertion == null) {
            throw new BusinessException("Cannot get assertion!");
        }

        // Find public key certificate to use from issuer
        issuer = assertion.getIssuer();
        if (issuer == null) {
            throw new BusinessException("Cannot find assertion issuer!");
        }
        logger.info("Received SAML assertion from issuer " + issuer.getValue());
        String certFile = Server.getInstance().getSamlTrustedCertificate(issuer.getValue());

        // Check validity of assertion
        try {
            SamlTokenValidator.isSignatureValid(certFile, assertion);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | InvalidKeySpecException | ValidationException ex) {
            throw new BusinessException("Assertion signature is not valid!", ex);
        }
        if (!SamlTokenValidator.isTimeValid(assertion)) {
            throw new BusinessException("Assertion is not time valid!");
        }
        try {
            if (!SamlTokenValidator.isAudienceValid(request.getRequestURL().toString(), assertion)) {
                throw new BusinessException("Assertion audience is not valid!");
            }
        } catch (MalformedURLException ex) {
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getDefaultAccountType() {
        return Server.getInstance().getSAMLAccountType(issuer.getValue());
    }

    @Override
    protected String getEmail() throws BusinessException {
        return SamlTokenValidator.getEmail(assertion);
    }

}
