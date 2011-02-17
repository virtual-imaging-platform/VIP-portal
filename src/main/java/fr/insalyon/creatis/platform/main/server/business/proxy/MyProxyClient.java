/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 *
 * Copyright 2007 The Board of Trustees of the University of Illinois.
 * All rights reserved.
 *
 * --
 * Developed by:
 *
 *   MyProxy Team
 *   National Center for Supercomputing Applications
 *   University of Illinois
 *   http://myproxy.ncsa.uiuc.edu/
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *   Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimers.
 *
 *   Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimers in the
 *   documentation and/or other materials provided with the distribution.
 *
 *   Neither the names of the National Center for Supercomputing
 *   Applications, the University of Illinois, nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */
package fr.insalyon.creatis.platform.main.server.business.proxy;

import fr.insalyon.creatis.platform.main.server.ServerConfiguration;
import fr.insalyon.creatis.platform.main.server.business.BusinessException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author Rafael Silva
 */
public class MyProxyClient {

    private SSLSocket socket;
    private BufferedInputStream socketIn;
    private BufferedOutputStream socketOut;
    private Collection certificateChain;
    private KeyPair keyPair;
    // CONSTANTS
    private final String VERSION = "VERSION=MYPROXYv2";
    private final String GETCOMMAND = "COMMAND=0";
    private final String INFOCOMMAND = "COMMAND=2";
    private final String USERNAME = "USERNAME=";
    private final String PASSPHRASE = "PASSPHRASE=";
    private final String LIFETIME = "LIFETIME=31536000";
    private final String RESPONSE = "RESPONSE=";
    private final String ERROR = "ERROR=";

    /**
     * The MyProxyClient class provides an interface for retrieving credentials
     * from a MyProxy server.
     */
    public MyProxyClient() {
    }

    /**
     *
     * @param userDN User distinguished name
     * @param proxyName Name of the proxy to be stored
     * @return Proxy file name
     * @throws BusinessException
     */
    public String getProxy(String userCN, String userDN) throws BusinessException {

        try {
            String proxyFileName = ServerConfiguration.getInstance().getProxiesDir()
                    + "x509up_" + userCN.toLowerCase().replace(" ", "_");

            connect();
            logon(userDN);
            getCredentials(userCN);
            saveCredentials(proxyFileName);
            disconnect();

            return proxyFileName;

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                disconnect();
            } catch (IOException ex1) {
                throw new BusinessException(ex1.getMessage());
            }
            throw new BusinessException(ex.getMessage());
        }
    }

    public static void main(String[] s) throws Exception {
        MyProxyClient client = new MyProxyClient();
        client.connect();
        client.info("/O=GRID-FR/C=FR/O=CNRS/OU=CREATIS/CN=Tristan Glatard");
    }

    /**
     * Connects to the MyProxy server at the desired host and port. Requires
     * host authentication via SSL. The host's certificate subject must
     * match the requested hostname. If CA certificates are found in the
     * standard GSI locations, they will be used to verify the server's
     * certificate. If trust roots are requested and no CA certificates are
     * found, the server's certificate will still be accepted.
     */
    private void connect() throws Exception {

        SSLContext sc = SSLContext.getInstance("SSL");
        KeyManager[] keyManagers = createKeyManagers(
                ServerConfiguration.getInstance().getKeystoreFile(),
                ServerConfiguration.getInstance().getKeystorePass());

        sc.init(keyManagers, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
        SSLSocketFactory sf = sc.getSocketFactory();

        this.socket = (SSLSocket) sf.createSocket(
                ServerConfiguration.getInstance().getMyProxyHost(),
                ServerConfiguration.getInstance().getMyProxyPort());

        this.socket.setEnabledProtocols(new String[]{"SSLv3"});
        this.socket.startHandshake();
        this.socketIn = new BufferedInputStream(this.socket.getInputStream());
        this.socketOut = new BufferedOutputStream(this.socket.getOutputStream());
    }

    /**
     * Logs on to the MyProxy server by issuing the MyProxy GET command.
     */
    private void logon(String userDN) throws Exception {

        this.socketOut.write('0');
        this.socketOut.flush();
        this.socketOut.write(VERSION.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(GETCOMMAND.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write((USERNAME + userDN).getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(PASSPHRASE.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(LIFETIME.getBytes());
        this.socketOut.write('\n');
        this.socketOut.flush();

        String line = readLine(this.socketIn);
        if (line == null) {
            throw new EOFException();
        }
        if (!line.equals(VERSION)) {
            throw new ProtocolException("bad MyProxy protocol VERSION string: " + line);
        }
        line = readLine(this.socketIn);
        if (line == null) {
            throw new EOFException();
        }
        if (!line.startsWith(RESPONSE)
                || line.length() != RESPONSE.length() + 1) {
            throw new ProtocolException(
                    "bad MyProxy protocol RESPONSE string: " + line);
        }
        char response = line.charAt(RESPONSE.length());
        if (response == '1') {
            StringBuilder errString = new StringBuilder("MyProxy logon failed");
            while ((line = readLine(this.socketIn)) != null) {
                if (line.startsWith(ERROR)) {
                    errString.append('\n');
                    errString.append(line.substring(ERROR.length()));
                }
            }
            throw new FailedLoginException(errString.toString());
        } else if (response == '2') {
            throw new ProtocolException("MyProxy authorization RESPONSE not implemented");
        } else if (response != '0') {
            throw new ProtocolException("Unknown MyProxy protocol RESPONSE string: " + line);
        }
        while ((line = readLine(this.socketIn)) != null) {
        }
    }


    private void info(String userDN) throws Exception {
        this.socketOut.write('0');
        this.socketOut.flush();
        this.socketOut.write(VERSION.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(INFOCOMMAND.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write((USERNAME + userDN).getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(PASSPHRASE.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(LIFETIME.getBytes());
        this.socketOut.write('\n');
        this.socketOut.flush();

        String line = readLine(this.socketIn);
        if (line == null) {
            throw new EOFException();
        }
        if (!line.equals(VERSION)) {
            throw new ProtocolException("bad MyProxy protocol VERSION string: " + line);
        }
        line = readLine(this.socketIn);
        if (line == null) {
            throw new EOFException();
        }
        if (!line.startsWith(RESPONSE)
                || line.length() != RESPONSE.length() + 1) {
            throw new ProtocolException(
                    "bad MyProxy protocol RESPONSE string: " + line);
        }
        System.out.println(line);
    }

    /**
     * Retrieves credentials from the MyProxy server.
     */
    private void getCredentials(String userCN) throws Exception {

        InputStream inputStream = new FileInputStream(
                ServerConfiguration.getInstance().getKeystoreFile());
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, ServerConfiguration.getInstance().getKeystorePass().toCharArray());

        PrivateKey key = (PrivateKey) keyStore.getKey("1",
                ServerConfiguration.getInstance().getKeystorePass().toCharArray());
        Certificate cert = keyStore.getCertificate("1");
        PublicKey publicKey = cert.getPublicKey();

        keyPair = new KeyPair(publicKey, (PrivateKey) key);

        PKCS10CertificationRequest pkcs10 = new PKCS10CertificationRequest(
                "SHA1withRSA",
                new X509Principal(new X500Principal("CN=" + userCN).getEncoded()),
                keyPair.getPublic(),
                null,
                keyPair.getPrivate(),
                "SunRsaSign");

        this.socketOut.write(pkcs10.getEncoded());
        this.socketOut.flush();

        int numCertificates = this.socketIn.read();
        if (numCertificates == -1) {
            throw new Exception("connection aborted");
        } else if (numCertificates == 0 || numCertificates < 0) {
            throw new Exception("bad number of certificates sent by server: " + numCertificates);
        }

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        certificateChain = certFactory.generateCertificates(this.socketIn);
    }

    /**
     * Writes the retrieved credentials to the specified filename.
     */
    private void saveCredentials(String proxyName) throws Exception {

        Iterator iter;
        X509Certificate certificate;
        PrintStream printStream;

        iter = certificateChain.iterator();
        certificate = (X509Certificate) iter.next();
        File outFile = new File(proxyName);
        outFile.delete();
        outFile.createNewFile();
        // set permission
        String command = "chmod 0600 " + proxyName;
        Runtime.getRuntime().exec(command);

        printStream = new PrintStream(new FileOutputStream(outFile));
        printCert(certificate, printStream);
        printKey(keyPair.getPrivate(), printStream);
        while (iter.hasNext()) {
            certificate = (X509Certificate) iter.next();
            printCert(certificate, printStream);
        }
    }

    /**
     * Disconnects from the MyProxy server.
     */
    private void disconnect() throws IOException {
        this.socket.close();
        this.socket = null;
        this.socketIn = null;
        this.socketOut = null;
    }

    private void printB64(byte[] data, PrintStream out) {
        byte[] b64data;

        b64data = Base64.encode(data);
        for (int i = 0; i < b64data.length; i += 64) {
            if ((b64data.length - i) > 64) {
                out.write(b64data, i, 64);
            } else {
                out.write(b64data, i, b64data.length - i);
            }
            out.println();
        }
    }

    private void printCert(X509Certificate certificate, PrintStream out)
            throws CertificateEncodingException {
        out.println("-----BEGIN CERTIFICATE-----");
        printB64(certificate.getEncoded(), out);
        out.println("-----END CERTIFICATE-----");
    }

    private void printKey(PrivateKey key, PrintStream out)
            throws IOException {
        out.println("-----BEGIN RSA PRIVATE KEY-----");
        ByteArrayInputStream inStream = new ByteArrayInputStream(key.getEncoded());
        ASN1InputStream derInputStream = new ASN1InputStream(inStream);
        DERObject keyInfo = derInputStream.readObject();
        PrivateKeyInfo pkey = new PrivateKeyInfo((ASN1Sequence) keyInfo);
        DERObject derKey = pkey.getPrivateKey();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DEROutputStream der = new DEROutputStream(bout);
        der.writeObject(derKey);
        printB64(bout.toByteArray(), out);
        out.println("-----END RSA PRIVATE KEY-----");
    }

    private KeyManager[] createKeyManagers(String keyStoreFileName, String keyStorePassword) throws Exception {

        InputStream inputStream = new FileInputStream(keyStoreFileName);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
        KeyManager[] managers = keyManagerFactory.getKeyManagers();

        return managers;
    }

    private String readLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int c = is.read(); c > 0 && c != '\n'; c = is.read()) {
            sb.append((char) c);
        }
        if (sb.length() > 0) {
            return new String(sb);
        }
        return null;
    }

    private class MyTrustManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] issuers = null;
            String certDirPath = getExistingTrustRootPath();
            if (certDirPath == null) {
                return null;
            }
            File dir = new File(certDirPath);
            if (!dir.isDirectory()) {
                return null;
            }
            String[] certFilenames = dir.list();
            String[] certData = new String[certFilenames.length];
            for (int i = 0; i < certFilenames.length; i++) {
                try {
                    FileInputStream fileStream = new FileInputStream(
                            certDirPath + File.separator + certFilenames[i]);
                    byte[] buffer = new byte[fileStream.available()];
                    fileStream.read(buffer);
                    certData[i] = new String(buffer);
                    fileStream.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            try {
                issuers = getX509CertsFromStringList(certData);
            } catch (Exception e) {
                // ignore
            }
            return issuers;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            throw new CertificateException(
                    "checkClientTrusted not implemented by edu.uiuc.ncsa.MyProxy.MyProxyLogon.MyTrustManager");
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            checkServerCertPath(certs);
            checkServerDN(certs[0]);
        }

        private void checkServerCertPath(X509Certificate[] certs)
                throws CertificateException {
            try {
                CertPathValidator validator = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                CertPath certPath = certFactory.generateCertPath(Arrays.asList(certs));
                X509Certificate[] acceptedIssuers = getAcceptedIssuers();
                if (acceptedIssuers == null) {
                    String certDir = getExistingTrustRootPath();
                    if (certDir != null) {
                        throw new CertificateException(
                                "no CA certificates found in " + certDir);
                    }
                    acceptedIssuers = new X509Certificate[1];
                    acceptedIssuers[0] = certs[certs.length - 1];
                }
                Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>(
                        acceptedIssuers.length);
                for (int i = 0; i < acceptedIssuers.length; i++) {
                    TrustAnchor ta = new TrustAnchor(acceptedIssuers[i], null);
                    trustAnchors.add(ta);
                }
                PKIXParameters pkixParameters = new PKIXParameters(trustAnchors);
                pkixParameters.setRevocationEnabled(false);
                validator.validate(certPath, pkixParameters);
            } catch (CertificateException e) {
                throw e;
            } catch (GeneralSecurityException e) {
                throw new CertificateException(e);
            }
        }

        private void checkServerDN(X509Certificate cert)
                throws CertificateException {
            String subject = cert.getSubjectX500Principal().getName();
            int index = subject.indexOf("CN=");
            if (index == -1) {
                throw new CertificateException("Server certificate subject ("
                        + subject + "does not contain a CN component.");
            }
            String CN = subject.substring(index + 3);
            index = CN.indexOf(',');
            if (index >= 0) {
                CN = CN.substring(0, index);
            }
            if ((index = CN.indexOf('/')) >= 0) {
                String service = CN.substring(0, index);
                CN = CN.substring(index + 1);
                if (!service.equals("host") && !service.equals("myproxy")) {
                    throw new CertificateException(
                            "Server certificate subject CN contains unknown service element: "
                            + subject);
                }
            }
            String myHostname = ServerConfiguration.getInstance().getMyProxyHost();
            if (myHostname.equals("localhost")) {
                try {
                    myHostname = InetAddress.getLocalHost().getHostName();
                } catch (Exception e) {
                    // ignore
                }
            }
            if (!CN.equals(myHostname)) {
                throw new CertificateException(
                        "Server certificate subject CN (" + CN
                        + ") does not match server hostname ("
                        + ServerConfiguration.getInstance().getMyProxyHost()
                        + ").");
            }
        }
    }

    private X509Certificate[] getX509CertsFromStringList(String[] certList) throws CertificateException {

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        Collection<X509Certificate> c = new ArrayList<X509Certificate>(
                certList.length);
        for (int i = 0; i < certList.length; i++) {
            int index = -1;
            String certData = certList[i];
            if (certData != null) {
                index = certData.indexOf("-----BEGIN CERTIFICATE-----");
            }
            if (index >= 0) {
                certData = certData.substring(index);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(
                        certData.getBytes());
                try {
                    X509Certificate cert = (X509Certificate) certFactory.generateCertificate(inputStream);
                    c.add(cert);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (c.isEmpty()) {
            return null;
        }
        return c.toArray(new X509Certificate[0]);
    }

    /**
     * Gets the existing trusted CA certificates directory.
     * @return directory path string or null if none found
     */
    public String getExistingTrustRootPath() {
        String path, GL;

        GL = System.getenv("GLOBUS_LOCATION");
        if (GL == null) {
            GL = System.getProperty("GLOBUS_LOCATION");
        }

        path = System.getenv("X509_CERT_DIR");
        if (path == null) {
            path = System.getProperty("X509_CERT_DIR");
        }
        if (path == null) {
            path = getDir("/etc/grid-security/certificates");
        }
        if (path == null) {
            path = getDir(GL + File.separator + "share" + File.separator
                    + "certificates");
        }

        return path;
    }

    private String getDir(String path) {
        if (path == null) {
            return null;
        }
        File f = new File(path);
        if (f.isDirectory() && f.canRead()) {
            return f.getAbsolutePath();
        }
        return null;
    }
}
