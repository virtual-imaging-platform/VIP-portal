package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobInfoButton extends HLayout {

    private boolean isSelected;
    private Img img;
    private Label messageLabel;

    public JobInfoButton() {

        this.isSelected = false;

        this.setMembersMargin(10);
        this.setWidth(300);
        this.setHeight(50);
        this.setBorder("1px solid #CCCCCC");
        this.setPadding(10);
        this.setCursor(Cursor.HAND);
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                setBackgroundColor("#F7F7F7");
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                if (isSelected) {
                    setBackgroundColor("#F7F7F7");
                } else {
                    setBackgroundColor("#FFFFFF");
                }
            }
        });

        img = new Img(ApplicationConstants.ICON_MONITOR_TASK_OK);
        img.setWidth(32);
        img.setHeight(32);
        this.addMember(img);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight(40);
        vLayout.setCursor(Cursor.HAND);
        this.addMember(vLayout);

        messageLabel = WidgetUtil.getLabel("<strong>Loading...</strong>", 40);
        vLayout.addMember(messageLabel);
    }

    public void setStatus(int status) {

        switch (status) {
            case 1:
                messageLabel.setContents("<strong>Non-critical errors detected.</strong><br />"
                        + "<font color=\"#666666\"><em>Click to debug.</em></font>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_NONCRITICAL);
                break;
            case 2:
                messageLabel.setContents("<strong>Critical errors detected.</strong><br />"
                        + "<font color=\"#666666\"><em>Click to debug.</em></font>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_CRITICAL);
                break;
            case 3:
                messageLabel.setContents("<strong>Held jobs detected.</strong><br />"
                        + "<font color=\"#666666\"><em>Click to debug.</em></font>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_NONCRITICAL);
                break;
            default:
                messageLabel.setContents("<strong>No errors detected.</strong>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_OK);
                break;
        }
    }

    public void setSelected(boolean selected) {

        this.isSelected = selected;
        if (selected) {
            this.setBackgroundColor("#F7F7F7");
        } else {
            this.setBackgroundColor("#FFFFFF");
        }
    }
}
