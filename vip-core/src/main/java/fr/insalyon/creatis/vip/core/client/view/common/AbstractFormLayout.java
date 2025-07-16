package fr.insalyon.creatis.vip.core.client.view.common;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public abstract class AbstractFormLayout extends VLayout {

    public AbstractFormLayout(int width, int height) {
        this(Integer.toString(width), Integer.toString(height));
    }

    public AbstractFormLayout(int width, String height) {
        this(Integer.toString(width), height);
    }
    
    public AbstractFormLayout() {
        this.setBorder("1px solid #C0C0C0");
        this.setBackgroundColor("#F5F5F5");
        this.setPadding(10);
        this.setMembersMargin(5);
    }

    public AbstractFormLayout(String width, String height) {
        this.setWidth(width);
        this.setHeight(height);
        this.setBorder("1px solid #C0C0C0");
        this.setBackgroundColor("#F5F5F5");
        this.setPadding(10);
        this.setMembersMargin(5);
    }

    /**
     * Adds a title to the form.
     */
    protected void addTitle(String title, String icon) {

        Label label = WidgetUtil.getLabel("<b>" + title + "</b>",
                icon, 15);
        label.setWidth100();
        label.setAlign(Alignment.LEFT);
        this.addMember(label);
    }

    /**
     * Adds a field to the form.
     */
    public void addField(String title, FormItem item) {
        this.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        this.addMember(FieldUtil.getForm(item));
    }
    
    public void addField100(String title, FormItem item) {

        this.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        this.addMember(FieldUtil.getFormOneColumn(item));
    }
     
    public void addFieldResponsiveHeight(String title, FormItem item) {

        this.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        this.addMember(FieldUtil.getFormOneColumnResponsiveHeight(item));
    }

    public void addInline(Canvas... canvas) {
        HLayout hLayout = new HLayout(5);

        for (Canvas cv : canvas) {
            hLayout.addMember(cv);
        }
        addMember(hLayout);
    }

    /**
     * Adds a set of buttons displayed in line.
     */
    protected void addButtons(IButton... buttons) {

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        
        for (IButton button : buttons) {
            hLayout.addMember(button);
        }
        this.addMember(hLayout);
    }
}
