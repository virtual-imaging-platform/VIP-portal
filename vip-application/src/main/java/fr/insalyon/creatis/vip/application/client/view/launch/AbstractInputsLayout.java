package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class AbstractInputsLayout extends VLayout {

    protected ModalWindow modal;
    protected String tabID;
    protected String applicationName;

    public AbstractInputsLayout(String tabID, String applicationName, String title, String icon) {

        this.tabID = tabID;
        this.applicationName = applicationName;

        this.setWidth100();
        this.setHeight100();
        this.setBorder("1px solid #C0C0C0");
        this.setBackgroundColor("#F5F5F5");
        this.setPadding(10);
        this.setMembersMargin(5);

        Label titleLabel = WidgetUtil.getLabel("<b>" + title + "</b>", icon, 15);
        titleLabel.setWidth100();
        titleLabel.setAlign(Alignment.LEFT);

        HLayout titleLayout = new HLayout(5);
        titleLayout.addMember(titleLabel);
        titleLayout.addMember(WidgetUtil.getIconLabel(CoreConstants.ICON_CLOSE, "Close", 15, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                destroy();
            }
        }));
        this.addMember(titleLayout);
    }

    public abstract void loadData();
}
