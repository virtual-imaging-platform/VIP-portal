package fr.insalyon.creatis.vip.core.client.view.main;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 *
 * @author Rafael Silva
 */
public class HomeMenuButton extends ToolStripMenuButton {

    public HomeMenuButton() {

        this.setTitle("VIP");
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(3);
                
        this.setMenu(menu);
    }
}
