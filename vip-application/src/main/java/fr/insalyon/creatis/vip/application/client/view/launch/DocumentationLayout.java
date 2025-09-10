package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.Alignment;
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
public class DocumentationLayout extends VLayout {

    public DocumentationLayout(int x, int y, String contents) {

        this.setWidth(650);
        this.setHeight(350);
        this.setMembersMargin(5);
        this.setPadding(5);
        this.setBackgroundColor("#FFFFFF");
        this.setBorder("1px solid #CCCCCC");
        this.setOpacity(90);
        this.moveTo(x, y + 20);

        Label titleLabel = new Label("<b>Documentation and Terms of Use</b>");
        titleLabel.setIcon(CoreConstants.ICON_INFORMATION);
        titleLabel.setHeight(30);
        this.addMember(titleLabel);

        HTMLPane pane = new HTMLPane();
        pane.setWidth100();
        pane.setHeight100();
        pane.setOverflow(Overflow.AUTO);
        pane.setContents(contents);
        this.addMember(pane);

        Label closeLabel = new Label("Close");
        closeLabel.setIcon(CoreConstants.ICON_CLOSE);
        closeLabel.setHeight(25);
        closeLabel.setWidth100();
        closeLabel.setAlign(Alignment.RIGHT);
        closeLabel.setCursor(Cursor.HAND);
        closeLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                destroy();
            }
        });

        this.addMember(closeLabel);
    }
}
