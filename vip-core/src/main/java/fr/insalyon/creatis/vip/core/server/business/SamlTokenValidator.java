package fr.insalyon.creatis.vip.core.server.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SamlTokenValidator {

    private static final Logger logger = LoggerFactory.getLogger(SamlTokenValidator.class);

    public SamlTokenValidator() {
    }

    public static XMLObject getSAMLObject(byte[] xmlAssertion) throws UnsupportedEncodingException, ConfigurationException, XMLParserException, UnmarshallingException {

        //See https://wiki.shibboleth.net/confluence/display/OpenSAML/OSTwoUsrManJavaCreateFromXML
        

        // Initialize the library
        DefaultBootstrap.bootstrap();

        // Get parser pool manager
        BasicParserPool ppMgr = new BasicParserPool();
        ppMgr.setNamespaceAware(true);

        // Parse metadata file
        Document inCommonMDDoc = null;
        try{
            InputStream in = new ByteArrayInputStream(xmlAssertion);
            inCommonMDDoc = ppMgr.parse(in);
        }catch (XMLParserException ex){
            // xml Assertion may be encoded in URL
            logger.info(ex.getMessage());
            xmlAssertion = URLDecoder.decode(new String(xmlAssertion), Charset.defaultCharset().name()).getBytes();
            InputStream in = new ByteArrayInputStream(xmlAssertion);
            inCommonMDDoc = ppMgr.parse(in);
        }
        Element metadataRoot = inCommonMDDoc.getDocumentElement();
        
        // Get apropriate unmarshaller
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);

        // Unmarshall using the document root element, an EntitiesDescriptor in this case
        return unmarshaller.unmarshall(metadataRoot);
        
    }

    public static boolean isSignatureValid(String certFile, Assertion assertion) throws FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, ValidationException {
        // see http://kevnls.blogspot.ca/2009/07/processing-saml-in-java-using-opensaml.html

        File certificateFile = new File(certFile);

        //get the certificate from the file
        InputStream inputStream2 = new FileInputStream(certificateFile);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream2);
        inputStream2.close();

        //pull out the public key part of the certificate into a KeySpec
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(certificate.getPublicKey().getEncoded());

        //get KeyFactory object that creates key objects, specifying RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        System.out.println("Security Provider: " + keyFactory.getProvider().toString());

        //generate public key to validate signatures
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        //we have the public key
        System.out.println("Public Key created");

        //create credentials
        BasicX509Credential publicCredential = new BasicX509Credential();

        //add public key value
        publicCredential.setPublicKey(publicKey);

        //get the signature to validate from the response object
        Signature signature = assertion.getSignature();

        SignatureValidator signatureValidator = new SignatureValidator(publicCredential);

        //try to validate
        signatureValidator.validate(signature);

        //no validation exception was thrown
        return true;
    }

    public static boolean isTimeValid(Assertion assertion) {
        DateTime date = new DateTime();
        if (assertion.getConditions() == null) {
            return true;
        }
        if (assertion.getConditions().getNotBefore() != null && !assertion.getConditions().getNotBefore().isBefore(date)) {
            return false;
        }
        if (assertion.getConditions().getNotOnOrAfter() != null && !assertion.getConditions().getNotOnOrAfter().isAfter(date)) {
            return false;
        }
        return true;
    }

    public static boolean isAudienceValid(String server, Assertion assertion) throws MalformedURLException, URISyntaxException {
        String serverHost = getHost(server);
        if (assertion.getConditions() == null) {
            return true;
        }
        for (AudienceRestriction ar : assertion.getConditions().getAudienceRestrictions()) {
            for (Audience a : ar.getAudiences()) {
                if (getHost(a.getAudienceURI()).equals(serverHost)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getEmail(Assertion assertion) {
        return assertion.getSubject().getNameID().getValue();
    }

   /// Private methods
    private static String getHost(String serverURL) throws MalformedURLException, URISyntaxException {
        return new URI(serverURL).toURL().getHost();
    }
}
