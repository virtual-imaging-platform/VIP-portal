package fr.insalyon.creatis.vip.social.client.view.common;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractMainLayout extends VLayout {

    public AbstractMainLayout(String title, String icon) {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        this.setMembersMargin(10);
        this.setPadding(5);

        this.addMember(WidgetUtil.getLabel("<p style=\"font-size: 13pt\"><strong>"
                + title + "</strong></p>", icon, 17));
    }

    public abstract void loadData();
}
