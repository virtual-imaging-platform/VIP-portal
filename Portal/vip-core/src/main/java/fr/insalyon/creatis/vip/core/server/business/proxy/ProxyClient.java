/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
 */
package fr.insalyon.creatis.vip.core.server.business.proxy;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.*;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.nio.channels.FileChannel;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import javax.net.ssl.*;
import javax.security.auth.login.FailedLoginException;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author Rafael Silva
 */
public class ProxyClient {

    private final static Logger logger = Logger.getLogger(ProxyClient.class);
    private SSLSocket socket;
    private BufferedInputStream socketIn;
    private BufferedOutputStream socketOut;
    private Collection certificateChain;
    private KeyPair keyPair;
    // CONSTANTS
    private final String VERSION = "VERSION=MYPROXYv2";
    private final String GETCOMMAND = "COMMAND=0";
    private final String USERNAME = "USERNAME=";
    private final String PASSPHRASE = "PASSPHRASE=";
    private final String LIFETIME = "LIFETIME=";
    private final String RESPONSE = "RESPONSE=";
    private final String ERROR = "ERROR=";

    /**
     * The ProxyClient class provides an interface for retrieving credentials
     * from a MyProxy server.
     */
    public ProxyClient() {
    }

    /**
     *
     * @param userDN User distinguished name
     * @param proxyName Name of the proxy to be stored
     * @return Proxy file name
     * @throws BusinessException
     */
    public Proxy getProxy() throws BusinessException {

        try {

            String proxyFileName = Server.getInstance().getServerProxy();
            if (new File(proxyFileName).exists()) {

                FileInputStream is = new FileInputStream(proxyFileName);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                boolean valid = true;
                Date endDate = null;

                while (is.available() > 0) {
                    X509Certificate certificate = (X509Certificate) cf.generateCertificate(is);
                    Calendar currentDate = Calendar.getInstance();
                    currentDate.setTime(new Date());
                    currentDate.add(Calendar.HOUR, Server.getInstance().getMyProxyMinHours());
                    try {
                        certificate.checkValidity(currentDate.getTime());
                        if (endDate != null && certificate.getNotAfter().getTime() < endDate.getTime()) {
                            endDate = certificate.getNotAfter();
                        } else {
                            endDate = certificate.getNotAfter();
                        }
                    } catch (Exception ex1) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    logger.info("Server proxy still valid until: " + endDate);
                    return new Proxy(proxyFileName, endDate);
                } else {
                    new File(proxyFileName).delete();
                }
            }
            logger.info("Fetching server proxy from MyProxy server.");
            connect();
            logon();
            getCredentials();
            Date endDate = saveCredentials(proxyFileName);
            disconnect();
            //copy the proxy file and add extenstion
            copyFile(Server.getInstance().getServerProxy(), Server.getInstance().getServerProxyFolder(CoreConstants.VO_BIOMED));
            copyFile(Server.getInstance().getServerProxy(), Server.getInstance().getServerProxyFolder(CoreConstants.VO_NEUGRID));
            addVomsExtension(CoreConstants.VO_BIOMED);
            addVomsExtension(CoreConstants.VO_NEUGRID);

            return new Proxy(proxyFileName, endDate);

        } catch (Exception ex) {
            logger.error(ex);
            try {
                disconnect();
            } catch (IOException ex1) {
                logger.error(ex);
                throw new BusinessException(ex1.getMessage());
            }
            throw new BusinessException(ex.getMessage());
        }
    }

    private void addVomsExtension(String vo) throws Exception {

        logger.info("Adding" + vo + "Extension to server proxy.");
        // Voms Extension
        Server serverConf = Server.getInstance();
        long hours = Long.parseLong(serverConf.getMyProxyLifeTime()) / 3600;
        String command = "voms-proxy-init -voms " + vo
                + " -cert " + serverConf.getServerProxy()
                + " -key " + serverConf.getServerProxy()
                + " -out " + serverConf.getServerProxy(vo)
                + " -noregen -valid " + hours + ":00";
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s = null;
        String cout = "";

        while ((s = r.readLine()) != null) {
            cout += s + "\n";
        }
        process.waitFor();

        logger.info(cout);

        process.getOutputStream().close();
        process.getInputStream().close();
        process.getErrorStream().close();
        r.close();

        process = null;
    }

    private void connect() throws Exception {

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(new KeyManager[]{}, new TrustManager[]{new ProxyClient.MyTrustManager()}, new SecureRandom());
        SSLSocketFactory sf = sc.getSocketFactory();

        this.socket = (SSLSocket) sf.createSocket(
                Server.getInstance().getMyProxyHost(),
                Server.getInstance().getMyProxyPort());

        this.socket.setEnabledProtocols(new String[]{"SSLv3"});
        this.socket.startHandshake();
        this.socketIn = new BufferedInputStream(this.socket.getInputStream());
        this.socketOut = new BufferedOutputStream(this.socket.getOutputStream());
    }

    /**
     * Logs on to the MyProxy server by issuing the MyProxy GET command.
     */
    private void logon() throws Exception {

        this.socketOut.write('0');
        this.socketOut.flush();
        this.socketOut.write(VERSION.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write(GETCOMMAND.getBytes());
        this.socketOut.write('\n');
        this.socketOut.write((USERNAME + Server.getInstance().getMyProxyUser()).getBytes());
        this.socketOut.write('\n');
        this.socketOut.write((PASSPHRASE + Server.getInstance().getMyProxyPass()).getBytes());
        this.socketOut.write('\n');
        this.socketOut.write((LIFETIME + Server.getInstance().getMyProxyLifeTime()).getBytes());
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

    /**
     * Retrieves credentials from the MyProxy server.
     */
    private void getCredentials() throws Exception {

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyPair = keyGenerator.generateKeyPair();

        PKCS10CertificationRequest cert = new PKCS10CertificationRequest(
                "MD5WITHRSA",
                new X509Name("CN=irrelevant"),
                keyPair.getPublic(),
                null,
                keyPair.getPrivate(),
                "BC");

        socketOut.write(cert.getEncoded());
        socketOut.write(0x00);
        socketOut.flush();

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
    private Date saveCredentials(String proxyName) throws Exception {

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
        Date endDate = null;
        while (iter.hasNext()) {
            certificate = (X509Certificate) iter.next();
            if (endDate != null && certificate.getNotAfter().getTime() < endDate.getTime()) {
                endDate = certificate.getNotAfter();
            } else {
                endDate = certificate.getNotAfter();
            }
            printCert(certificate, printStream);
        }
        return endDate;
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

        @Override
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

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            throw new CertificateException(
                    "checkClientTrusted not implemented by edu.uiuc.ncsa.MyProxy.MyProxyLogon.MyTrustManager");
        }

        @Override
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
            String myHostname = Server.getInstance().getMyProxyHost();
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
                        + Server.getInstance().getMyProxyHost()
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
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(inputStream);
                c.add(cert);
            }
        }
        if (c.isEmpty()) {
            return null;
        }
        return c.toArray(new X509Certificate[0]);
    }

    /**
     * Gets the existing trusted CA certificates directory.
     *
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

    public void copyFile(String source, String dest) {
        FileChannel in = null;
        FileChannel out = null;

        try {
            // Init
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

    }
}
