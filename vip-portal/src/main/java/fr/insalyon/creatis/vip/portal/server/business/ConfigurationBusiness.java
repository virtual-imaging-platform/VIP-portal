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
 */
package fr.insalyon.creatis.vip.portal.server.business;

import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.portal.client.bean.Configuration;
import fr.insalyon.creatis.vip.portal.client.bean.User;
import fr.insalyon.creatis.vip.portal.server.business.proxy.MyProxyClient;
import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import fr.insalyon.creatis.voms.client.VomsClientConf;
import fr.insalyon.creatis.voms.client.VomsClientException;
import fr.insalyon.creatis.voms.client.VomsProxyCredential;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import org.globus.gsi.GlobusCredential;

/**
 *
 * @author Rafael Silva
 */
public class ConfigurationBusiness {

    /**
     * 
     * @param object
     * @return
     * @throws BusinessException
     */
    public Configuration loadConfiguration(Object object) throws BusinessException {

        Authentication authentication = null;
        ServerConfiguration conf = ServerConfiguration.getInstance();

        if (object != null) {
            X509Certificate certs[] = (X509Certificate[]) object;
            X509Certificate cert = certs[0];

            String[] subjectDN = cert.getSubjectDN().getName().split(", ");
            String userDN = "";

            for (int i = subjectDN.length - 1; i >= 0; i--) {
                userDN += "/" + subjectDN[i];
            }

            try {
                if (DAOFactory.getDAOFactory().getUserDAO().exists(userDN)) {
                    User user = DAOFactory.getDAOFactory().getUserDAO().getUser(userDN);
                    String proxyFileName = "";
                    try {
                        // MyProxy
                        MyProxyClient myproxy = new MyProxyClient();
                        proxyFileName = myproxy.getProxy(user.getCanonicalName(), userDN);

                        // Voms
//                        String command = "voms-proxy-init --voms biomed -cert "
//                                + proxyFileName + " -key " + proxyFileName
//                                + " -out " + proxyFileName + " -noregen";
//                        Process process = Runtime.getRuntime().exec(command);
//                        process.waitFor();
//                        VomsClientConf.getInstance().setUserKeyPath(proxyFileName);
//                        VomsClientConf.getInstance().setUserCertPath(proxyFileName);
//                        VomsProxyCredential vpc = new VomsProxyCredential();
//                        GlobusCredential proxy = vpc.getCredential(
//                                "", proxyFileName, "biomed", 31536000, 86400);
//                        OutputStream out = new FileOutputStream(new File(proxyFileName));
//                        proxy.save(out);

                        // Authentication
                        authentication = new Authentication(
                                user.getCanonicalName(), user.getOrganizationUnit(),
                                userDN, user.getGroups(), proxyFileName, true);

                        // User's folder
//                        VletAgentClient client = new VletAgentClient(
//                                ServerConfiguration.getInstance().getVletagentHost(),
//                                ServerConfiguration.getInstance().getVletagentPort(),
//                                proxyFileName);
//
//                        client.createDirectory(conf.getDataManagerUsersHome(),
//                                user.getCanonicalName().replace(" ", "_").toLowerCase());
//                        client.createDirectory(conf.getDataManagerUsersHome(),
//                                DataManagerConstants.PUBLIC_HOME);

                    } catch (BusinessException ex) {
                        authentication = new Authentication(
                                user.getCanonicalName(), user.getOrganizationUnit(),
                                userDN, new HashMap(), "", false);

//                    } catch (VletAgentClientException ex) {
//                        if (!ex.getMessage().contains("ERROR: File/Directory exists or Directory is not empty")) {
//                            throw new BusinessException(ex);
//                        }
//                    } catch (IOException ex) {
//                        throw new BusinessException(ex);
                    } catch (Exception ex) {
                        throw new BusinessException(ex);
                    }
                } else {
                    authentication = getAnonymousAuth();
                }
            } catch (DAOException ex) {
                authentication = getAnonymousAuth();
            }
        } else {
            authentication = getAnonymousAuth();
        }

        URI uri = null;
        try {
            uri = new URI(conf.getMoteurServer());
        } catch (URISyntaxException ex) {
            throw new BusinessException(ex);
        }
        return new Configuration(authentication,
                conf.getQuickStartURL(),
                uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort(),
                conf.getDataManagerLFCHost(), conf.getDataManagerLFCPort());
    }

    private Authentication getAnonymousAuth() {
        return new Authentication("Anonymous", "", "Anonymous", new HashMap(), "", false);
    }

    /**
     * 
     * @param proxy
     * @param groupName
     * @return
     * @throws BusinessException
     */
    public String addGroup(String proxy, String groupName) throws BusinessException {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxy);
            client.createDirectory(ServerConfiguration.getInstance().getDataManagerGroupsHome(),
                    groupName);

            return DAOFactory.getDAOFactory().getGroupDAO().add(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (VletAgentClientException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param proxy
     * @param groupName
     * @throws BusinessException
     */
    public void removeGroup(String proxy, String groupName) throws BusinessException {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxy);
            client.delete(
                    ServerConfiguration.getInstance().getDataManagerGroupsHome()
                    + "/" + groupName);

            DAOFactory.getDAOFactory().getGroupDAO().remove(groupName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (VletAgentClientException ex) {
            throw new BusinessException(ex);
        }
    }
}
