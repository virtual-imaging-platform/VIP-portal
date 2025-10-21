package fr.insalyon.creatis.vip.social.client.view.message.sent;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.bean.Message;

/**
 *
 * @author Rafael Silva
 */
public class SentMessageViewerWindow extends Window {

    private VLayout vLayout;
    private Message message;

    public SentMessageViewerWindow(Message message) {

        this.message = message;

        this.setTitle(Canvas.imgHTML(SocialConstants.ICON_MESSAGE_SENT) + " "
                + message.getTitle());
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

        StringBuilder sb = new StringBuilder();
        int count = 0;
        int numOfReceivers = message.getReceivers().length;
        for (User user : message.getReceivers()) {
            if (count == SocialConstants.MESSAGE_MAX_RECEIVERS_DISPLAY) {
                sb.append(" and ").append(numOfReceivers - count).append(" other users");
                break;
            } else {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(user.getFullName());
                count++;
            }
        }

        mainLayout.addMember(WidgetUtil.getLabel("<strong>" + sb.toString() 
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
