package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
 */
public class MoreOperationsBoxLayout extends HLayout {

    public MoreOperationsBoxLayout() {

        this.setMembersMargin(2);
        this.setWidth100();
        this.setHeight(25);
        this.setBackgroundColor("#E2E2E2");
        this.setBorder("1px solid #D2D2D2");
        this.setAlign(Alignment.CENTER);

        configureImageLayout();
        configureMainLayout();
    }

    /**
     * Configures the image layout.
     */
    private void configureImageLayout() {

        VLayout imgLayout = new VLayout();
        imgLayout.setPadding(2);
        imgLayout.setWidth(20);
        imgLayout.setHeight(25);
        imgLayout.setAlign(Alignment.CENTER);

        Img icon = new Img(DataManagerConstants.ICON_RELOAD, 16, 16);
        icon.setPrompt("More operations");
        icon.setCursor(Cursor.HAND);

        imgLayout.addMember(icon);
        this.addMember(imgLayout);
    }

    /**
     * Configures the main layout of an operation.
     */
    private void configureMainLayout() {

        VLayout mainLayout = new VLayout(2);
        mainLayout.setWidth(120);
        mainLayout.setHeight(25);
        mainLayout.setAlign(Alignment.CENTER);

        Label messageLabel = new Label("<font color=\"#666666\">More operations</font>");
        messageLabel.setHeight(15);
        messageLabel.setCursor(Cursor.HAND);
        mainLayout.addMember(messageLabel);

        this.addMember(mainLayout);
    }
}
