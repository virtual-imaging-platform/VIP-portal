/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.view;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutInputs extends AbstractFormLayout {

    LabelButton addButton;

    /**
     *
     * @param width
     * @param height
     */
    public LayoutInputs(String width, String height) {
        super(width, height);
        this.addTitle("Application Inputs", ApplicationImporterConstants.ICON_INPUT);
        HLayout toolstrip = new HLayout();
        toolstrip.setWidth100();
        toolstrip.setHeight(30);
        toolstrip.setBackgroundColor("#F7F7F7");
        toolstrip.setPadding(3);
        toolstrip.setMembersMargin(10);
        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));
        addButton = new LabelButton("Add Input", CoreConstants.ICON_ADD);
        addButton.setWidth(200);
        toolstrip.addMember(addButton);
        this.addMember(toolstrip);
    }

    /**
     *
     * @return
     */
    public LabelButton getAddButton() {
        return addButton;
    }
}
