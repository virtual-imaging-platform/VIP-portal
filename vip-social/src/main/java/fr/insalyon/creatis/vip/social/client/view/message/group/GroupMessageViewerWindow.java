package fr.insalyon.creatis.vip.social.client.view.message.group;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.models.GroupMessage;

/**
 *
 * @author Rafael Silva
 */
public class GroupMessageViewerWindow extends Window {

    private VLayout vLayout;
    private GroupMessage message;

    public GroupMessageViewerWindow(GroupMessage message) {

        this.message = message;

        this.setTitle(Canvas.imgHTML(SocialConstants.ICON_MESSAGE) + " "
                + message.getGroupName() + ": " + message.getTitle());
        this.setCanDragReposition(true);
        this.setCanDragResize(true);
        this.setWidth(700);
        this.setHeight(450);
        this.centerInPage();
        this.setBackgroundColor("#F2F2F2");
        this.setPadding(5);

        vLayout = new VLayout(5);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setPadding(5);

        configureHeader();
        configureBody();

        this.addItem(vLayout);
    }

    private void configureHeader() {

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight(50);
        hLayout.setBackgroundColor("#FFFFFF");

        // Image Layout
        VLayout imgLayout = new VLayout();
        imgLayout.setPadding(2);
        imgLayout.setWidth(50);
        imgLayout.setHeight(50);
        imgLayout.setAlign(Alignment.CENTER);
        imgLayout.addMember(new Img(SocialConstants.ICON_USER, 48, 48));

        hLayout.addMember(imgLayout);

        // Main Layout
        VLayout mainLayout = new VLayout(2);
        mainLayout.setWidth("*");
        mainLayout.setHeight(50);
        mainLayout.setAlign(Alignment.CENTER);

        mainLayout.addMember(WidgetUtil.getLabel("<strong>" + message.getSender().getFullName()
                + "</strong>: " + message.getTitle(), 15));

        mainLayout.addMember(WidgetUtil.getLabel("<font color=\"#666666\">"
                + message.getPosted() + "</font>", 15));

        hLayout.addMember(mainLayout);

        vLayout.addMember(hLayout);
    }

    private void configureBody() {

        HTMLPane pane = new HTMLPane();
        pane.setPadding(10);
        pane.setOverflow(Overflow.AUTO);
        pane.setStyleName("defaultBorder");
        pane.setBackgroundColor("#FFFFFF");

        String contents = "<b>Message: </b><br /><br />" + message.getMessage();

        pane.setContents(contents);
        vLayout.addMember(pane);
    }
}
