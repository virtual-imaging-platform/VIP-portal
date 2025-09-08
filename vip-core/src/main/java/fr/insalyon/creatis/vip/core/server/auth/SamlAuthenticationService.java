package fr.insalyon.creatis.vip.core.server.auth;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.SamlTokenValidator;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

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
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Tristan Glatard
 */
public class SamlAuthenticationService extends AbstractAuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Assertion assertion;
    private Issuer issuer;

    private Server server;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext applicationContext = WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        server = applicationContext.getBean(Server.class);
    }

    @Override
    protected void checkValidRequest(HttpServletRequest request) throws BusinessException {
        logger.info("SAML authentication request");

        String token = request.getParameter("_saml_token");
        if (token == null) {
            logger.error("Error with SAML assertion : SAML token is null");
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
            logger.error("Error getting SAML assertion {}", new String(xmlAssertion));
            throw new BusinessException("Cannot get assertion!");
        }

        // Find public key certificate to use from issuer
        issuer = assertion.getIssuer();
        if (issuer == null) {
            logger.error("Error with SAML assertion {} : Cannot find issuer", new String(xmlAssertion));
            throw new BusinessException("Cannot find assertion issuer!");
        }
        logger.info("Received SAML assertion from issuer " + issuer.getValue());
        String certFile = server.getSamlTrustedCertificate(issuer.getValue());

        // Check validity of assertion
        try {
            SamlTokenValidator.isSignatureValid(certFile, assertion);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | InvalidKeySpecException | ValidationException ex) {
            logger.error("Error with SAML assertion {} : signature is not valid", new String(xmlAssertion), ex);
            throw new BusinessException("Assertion signature is not valid!", ex);
        }
        if (!SamlTokenValidator.isTimeValid(assertion)) {
            logger.error("Error with SAML assertion {} : time not valid", new String(xmlAssertion));
            throw new BusinessException("Assertion is not time valid!");
        }
        try {
            if (!SamlTokenValidator.isAudienceValid(request.getRequestURL().toString(), assertion)) {
                logger.error("Error with SAML assertion {} : audience is not valid", new String(xmlAssertion));
                throw new BusinessException("Assertion audience is not valid!");
            }
        } catch (MalformedURLException | URISyntaxException ex) {
            logger.error("Error with SAML assertion {}", new String(xmlAssertion), ex);
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getDefaultGroup() {
        return server.getSAMLDefaultGroup(issuer.getValue());
    }

    @Override
    protected String getEmail() throws BusinessException {
        return SamlTokenValidator.getEmail(assertion);
    }

}
