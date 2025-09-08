package fr.insalyon.creatis.vip.core.client.view.layout;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 *
 * @author Rafael Silva
 */
public class CenterTabSet extends TabSet {

    private static CenterTabSet instance;

    public static CenterTabSet getInstance() {
        if (instance == null) {
            instance = new CenterTabSet();
        }
        return instance;
    }

    private CenterTabSet() {
        
        this.setTabBarPosition(Side.TOP);
        this.setWidth100();
        this.setHeight100();
   }
}