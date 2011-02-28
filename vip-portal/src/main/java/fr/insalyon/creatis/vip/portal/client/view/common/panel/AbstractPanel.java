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
package fr.insalyon.creatis.vip.portal.client.view.common.panel;

import com.gwtext.client.widgets.Panel;
import fr.insalyon.creatis.vip.portal.client.view.common.Context;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractPanel extends Panel {

    private String userGroup = null;
    protected String title;

    public AbstractPanel(String[] groups) {
        this(groups, null);
    }

    public AbstractPanel(String[] groups, String title) {
        if (title != null) {
            this.title = title;
        }

        if (groups == null || groups.length == 0) {
            userGroup = "Administrator";
            buildPanel();
        } else {
            for (String groupName : groups) {
                if (Context.getInstance().getAuthentication().hasGroupAccess(groupName)) {
                    if (Context.getInstance().getAuthentication().isAdmin(groupName)) {
                        userGroup = groupName;
                        break;
                    }
                    if (userGroup == null) {
                        userGroup = groupName;
                    }
                }
            }
            if (userGroup != null) {
                buildPanel();
            } else {
                authorizationError();
            }
        }
    }

    private void authorizationError() {
        this.setHtml("<p style=\"font-size: 11px\"><strong>Authorization Error</strong></p>"
                + "<p style=\"font-size: 11px\">You are not allowed to access this content.</p>");
    }

    protected boolean isSystemAdmin() {
        return Context.getInstance().getAuthentication().isAdmin(userGroup);
    }

    protected boolean hasGroupAccess(String... groups) {
        for (String groupName : groups) {
            if (Context.getInstance().getAuthentication().hasGroupAccess(groupName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isGroupAdmin(String... groups) {
        for (String groupName : groups) {
            if (Context.getInstance().getAuthentication().isAdmin(groupName)) {
                return true;
            }
        }
        return false;
    }

    protected abstract void buildPanel();
}
