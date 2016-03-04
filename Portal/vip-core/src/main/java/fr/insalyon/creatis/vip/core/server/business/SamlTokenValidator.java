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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.view.CoreException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.impl.AssertionImpl;
import org.opensaml.saml2.core.impl.IssuerImpl;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author glatard
 */
public class SamlTokenValidator {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SamlTokenValidator.class);

    public SamlTokenValidator() {
    }

    public static String getEmailIfValid(AssertionImpl assertion, String trustedCertificate, String server) throws CoreException {
        try {
            //Check time validity
            if (!isTimeValid(assertion)) {
                throw new CoreException("SAML token is not time valid");
            } else {
                logger.info("SAML token is time valid");
            }

            //Check audience validity
            if (!isAudienceValid(server, assertion)) {
                throw new CoreException("SAML token audience is not valid");
            } else {
                logger.info("SAML token audience is valid");
            }

            //Check signature validity
            isSignatureValid(trustedCertificate, assertion);
            logger.info("SAML token signature is valid");
        } catch (IOException | CertificateException | NoSuchAlgorithmException | InvalidKeySpecException | ValidationException ex) {
            throw new CoreException(ex);
        }

        //Get the user email
        String email = getUserEmail(assertion);
        return email;
    }

    public static AssertionImpl getSAMLAssertion(String token) throws CoreException {

        try {
            String decodedToken;
            decodedToken = URLDecoder.decode(token, "UTF-8");

            //See https://wiki.shibboleth.net/confluence/display/OpenSAML/OSTwoUsrManJavaCreateFromXML
            InputStream in = new ByteArrayInputStream(decodedToken.getBytes("UTF-8"));

            // Initialize the library
            DefaultBootstrap.bootstrap();

            // Get parser pool manager
            BasicParserPool ppMgr = new BasicParserPool();
            ppMgr.setNamespaceAware(true);

            // Parse metadata file
            Document inCommonMDDoc = ppMgr.parse(in);
            Element metadataRoot = inCommonMDDoc.getDocumentElement();

            // Get apropriate unmarshaller
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);

            // Unmarshall using the document root element, an EntitiesDescriptor in this case
            AssertionImpl assertion = null;
            IssuerImpl issuer = null;
            for (Object o : unmarshaller.unmarshall(metadataRoot).getOrderedChildren()) {
                try {
                    assertion = (AssertionImpl) o;
                    issuer = (IssuerImpl) o;
                } catch (ClassCastException ex) {
                }
            }
            return assertion;
        } catch (UnsupportedEncodingException | ConfigurationException | XMLParserException | UnmarshallingException ex) {
            throw new CoreException(ex);
        }
    }

    private static String getUserEmail(AssertionImpl assertion) {
        return assertion.getSubject().getNameID().getValue();
    }

    private static boolean isTimeValid(AssertionImpl assertion) {
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

    private static boolean isAudienceValid(String server, AssertionImpl assertion) throws MalformedURLException {
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

    private static String getHost(String serverURL) throws MalformedURLException {
        return new URL(serverURL).getHost();
    }

    private static boolean isSignatureValid(String certFile, AssertionImpl assertion) throws FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, ValidationException {
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

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

}
