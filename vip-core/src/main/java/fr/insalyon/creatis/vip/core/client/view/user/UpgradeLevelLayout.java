package fr.insalyon.creatis.vip.core.client.view.user;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Silva
 */
public class UpgradeLevelLayout extends VLayout {

    public UpgradeLevelLayout(int x, int y) {
        
        this.setWidth(500);
        this.setHeight(300);
        this.setMembersMargin(5);
        this.setPadding(5);
        this.setBackgroundColor("#FFFFFF");
        this.setBorder("1px solid #CCCCCC");
        this.setOpacity(85);
        this.moveTo(x, y + 20);
        
        Label titleLabel = new Label("<b>User Levels:</b>");
        titleLabel.setIcon(CoreConstants.ICON_USER_INFO);
        titleLabel.setHeight(30);
        this.addMember(titleLabel);
        
        HTMLPane pane = new HTMLPane();
        pane.setWidth100();
        pane.setHeight100();
        pane.setOverflow(Overflow.AUTO);
        pane.setContentsURL("./documentation/user/user_levels.html");  
        pane.setContentsType(ContentsType.PAGE);        
        this.addMember(pane);
        
        Label closeLabel = new Label("Close");
        closeLabel.setIcon(CoreConstants.ICON_CLOSE);
        closeLabel.setHeight(25);
        closeLabel.setWidth100();
        closeLabel.setAlign(Alignment.RIGHT);
        closeLabel.setCursor(Cursor.HAND);
        closeLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                destroy();
            }
        });

        this.addMember(closeLabel);
    }
}
