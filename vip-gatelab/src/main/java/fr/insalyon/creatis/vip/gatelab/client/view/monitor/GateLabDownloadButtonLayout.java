package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabDownloadButtonLayout extends HLayout {

    private Img img;
    private VLayout vLayout;
    
    public GateLabDownloadButtonLayout(String text, String icon, Label label) {
        
        this.setMembersMargin(10);
        this.setWidth(400);
        this.setHeight(70);
        this.setBorder("1px solid #CCCCCC");
        this.setPadding(4);

        img = new Img(icon);
        img.setWidth(60);
        img.setHeight(60);
        this.addMember(img);

        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight(60);
        this.addMember(vLayout);

        vLayout.addMember(WidgetUtil.getLabel("<b>Download " + text + " File</b>", 25));
        vLayout.addMember(label);
    }
    
    @Override
    public Canvas setDisabled(boolean disabled) {
        
        if (disabled) {
            this.setCursor(Cursor.ARROW);
            img.setCursor(Cursor.ARROW);
            vLayout.setCursor(Cursor.ARROW);
        } else {
            this.setCursor(Cursor.HAND);
            img.setCursor(Cursor.HAND);
            vLayout.setCursor(Cursor.HAND);
        }
        return (this);
    }
}
