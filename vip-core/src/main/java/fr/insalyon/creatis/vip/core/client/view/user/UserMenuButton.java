package fr.insalyon.creatis.vip.core.client.view.user;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.models.User;

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
                Layout.getInstance().addTab(
                    CoreConstants.TAB_ACCOUNT, AccountTab::new);
            }
        });

        // Sign out
        final MenuItem signoutItem = new MenuItem("Sign Out");
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
