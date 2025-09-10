package fr.insalyon.creatis.vip.social.client.view;

import fr.insalyon.creatis.vip.social.client.view.common.AbstractMainLayout;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.social.client.SocialConstants;

/**
 *
 * @author Rafael Silva
 */
public class SocialTab extends Tab {

    private MainLayout mainLayout;
    
    public SocialTab() {
        
        this.setID(SocialConstants.TAB_SOCIAL);
        this.setTitle(Canvas.imgHTML(SocialConstants.ICON_SOCIAL) + " " + SocialConstants.APP_SOCIAL);
        this.setCanClose(true);
        
        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.HIDDEN);
        
        hLayout.addMember(new MenuLayout());
        
        mainLayout = new MainLayout();
        hLayout.addMember(mainLayout);
        
        this.setPane(hLayout);
    }
    
    public void setLayout(AbstractMainLayout layout) {
        
        mainLayout.setLayout(layout);
    }
    
    public void loadData() {
        
        mainLayout.loadData();
    }
}
