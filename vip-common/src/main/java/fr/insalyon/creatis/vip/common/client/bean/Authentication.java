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
package fr.insalyon.creatis.vip.common.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class Authentication implements IsSerializable {

    private String user;
    private String organization;
    private String userDN;
    private Map<String, String> groups;
    private String proxyFileName;
    private boolean proxyValid;

    public Authentication() {
    }

    public Authentication(String user, String organization, String userDN, Map<String, String> groups, String proxyFileName, boolean proxyValid) {
        this.user = user;
        this.organization = organization;
        this.userDN = userDN;
        this.groups = groups;
        this.proxyFileName = proxyFileName;
        this.proxyValid = proxyValid;
    }

    public String getUserDN() {
        return userDN;
    }

    public String getUser() {
        return user;
    }

    public String getOrganization() {
        return organization;
    }

    public boolean hasGroupAccess(String groupName) {
        return groups.containsKey(groupName);
    }

    public boolean isAdmin(String groupName) {
        if (groups.containsKey(groupName)) {
            return groups.get(groupName).equals("admin") ? true : false;
        }
        return false;
    }

    public String getProxyFileName() {
        return proxyFileName;
    }

    public boolean isProxyValid() {
        return proxyValid;
    }
}
