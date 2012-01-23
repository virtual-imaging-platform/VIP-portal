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
 */
package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Silva
 */
public class ManageUsersTab extends Tab {

    private ToolStrip toolStrip;
    private UsersStackSection usersStackSection;
    private EditUserStackSection editStackSection;

    public ManageUsersTab() {

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_USER) + " " + CoreConstants.APP_USER);
        this.setID(CoreConstants.TAB_MANAGE_USERS);
        this.setCanClose(true);
        this.setIcon(CoreConstants.ICON_USER);

        VLayout vLayout = new VLayout();

        toolStrip = new ManageUsersToolStrip();
        vLayout.addMember(toolStrip);

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);

        usersStackSection = new UsersStackSection();
        editStackSection = new EditUserStackSection();

        sectionStack.setSections(usersStackSection, editStackSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
    }

    public void loadUsers() {
        usersStackSection.loadData();
    }

    public void setUser(String email, boolean confirmed, String level) {
        editStackSection.setUser(email, confirmed, level);
    }
}
