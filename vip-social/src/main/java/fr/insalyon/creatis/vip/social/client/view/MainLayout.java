package fr.insalyon.creatis.vip.social.client.view;

import fr.insalyon.creatis.vip.social.client.view.common.AbstractMainLayout;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.social.client.view.message.MessageLayout;

/**
 *
 * @author Rafael Silva
 */
public class MainLayout extends VLayout {

    private VLayout mainLayout;
    private AbstractMainLayout layout;

    public MainLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        mainLayout = new VLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        mainLayout.setOverflow(Overflow.AUTO);

        layout = new MessageLayout();
        mainLayout.addMember(layout);
        
        this.addMember(mainLayout);
    }
    
    public void setLayout(AbstractMainLayout layout) {
        
        this.layout = layout;
        this.mainLayout.removeMembers(mainLayout.getChildren());
        this.mainLayout.addMember(layout);
    }
    
    public void loadData() {
        
        this.layout.loadData();
    }
}
