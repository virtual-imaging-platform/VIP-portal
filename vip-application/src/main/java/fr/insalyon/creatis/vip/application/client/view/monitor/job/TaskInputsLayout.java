/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author glatard
 */
class TaskInputsLayout  extends HLayout {
    
    private String param;

    public TaskInputsLayout(String param) {
        
        this.setWidth100();
        this.setHeight(16);
        this.setBorder("1px solid #F2F2F2");
        this.setPadding(1);
        this.setMembersMargin(3);
        
//        Label nameLabel = WidgetUtil.getLabel("<b>Parameter: </b>", 16, Cursor.AUTO);
//        nameLabel.setWidth100();
//        this.addMember(nameLabel);
        
        Label valueLabel = WidgetUtil.getLabel(param, 16, Cursor.TEXT);
        valueLabel.setCanSelectText(true);
        valueLabel.setWidth100();
        this.addMember(valueLabel);
    }
    
}
