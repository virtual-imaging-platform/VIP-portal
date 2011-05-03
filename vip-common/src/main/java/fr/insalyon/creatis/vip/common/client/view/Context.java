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
package fr.insalyon.creatis.vip.common.client.view;

import fr.insalyon.creatis.vip.common.client.bean.Authentication;

/**
 *
 * @author Rafael Silva
 */
public class Context {

    private static Context instance;
    private Authentication authentication;
    private String quickstartURL;
    private String moteurServerHost;
    private String lastGridFolderBrowsed;
    private String lfcHost;
    private int lfcPort;

    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    private Context() {
    }
    
    public boolean isSystemAdmin() {
        return authentication.isAdmin("Administrator");
    }
    
    public String getUser() {
        return authentication.getUser();
    }
    
    public String getUserDN() {
        return authentication.getUserDN();
    }
    
    public String getProxyFileName() {
        return authentication.getProxyFileName();
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getLastGridFolderBrowsed() {
        return lastGridFolderBrowsed;
    }

    public void setLastGridFolderBrowsed(String lastGridFolderBrowsed) {
        this.lastGridFolderBrowsed = lastGridFolderBrowsed;
    }

    public String getMoteurServerHost() {
        return moteurServerHost;
    }

    public void setMoteurServerHost(String moteurServerHost) {
        this.moteurServerHost = moteurServerHost;
    }

    public String getQuickstartURL() {
        return quickstartURL;
    }

    public void setQuickstartURL(String quickstartURL) {
        this.quickstartURL = quickstartURL;
    }

    public String getLfcHost() {
        return lfcHost;
    }

    public void setLfcHost(String lfcHost) {
        this.lfcHost = lfcHost;
    }

    public int getLfcPort() {
        return lfcPort;
    }

    public void setLfcPort(int lfcPort) {
        this.lfcPort = lfcPort;
    }
}
