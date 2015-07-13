/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.core.client.view.user;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class UserMenuButton extends ToolStripMenuButton {

    public UserMenuButton(User user) {

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_USER) + " "
                + user.getFullName() + " (" + user.getLevel().name() + ")");

        Menu menu = new Menu();
        menu.setWidth(150);
        menu.setShowShadow(true);
        menu.setShadowDepth(3);

        // Upgrade level
        MenuItem upgradeItem = new MenuItem("Upgrade your Account");
        upgradeItem.setIcon(CoreConstants.ICON_USER_INFO);
        upgradeItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                new UpgradeLevelLayout(200, 25).show();
            }
        });

        // Account settings
        MenuItem accountItem = new MenuItem("Account Settings");
        accountItem.setIcon(CoreConstants.ICON_ACCOUNT);
        accountItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new AccountTab());
            }
        });

        // Sign out
        MenuItem signoutItem = new MenuItem("Sign Out");
        signoutItem.setIcon(CoreConstants.ICON_SIGNOUT);
        signoutItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().signout();
            }
        });

        if (user.getLevel() == UserLevel.Beginner) {
            menu.setItems(upgradeItem, accountItem, signoutItem);
        } else {
            menu.setItems(accountItem, signoutItem);
        }

        this.setMenu(menu);
    }
}
