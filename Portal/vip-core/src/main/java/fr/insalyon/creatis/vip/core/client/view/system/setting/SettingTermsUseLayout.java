/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.system.setting;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.Arrays;

/**
 *
 * @author Nouha Boujelben
 */
public class SettingTermsUseLayout extends AbstractFormLayout {

    private IButton updateTermsUse;
    
    public SettingTermsUseLayout() {
        
        
        super("350", "100");
        addTitle("Terms Of Use", CoreConstants.ICON_TERMS_USE);
        configure();
    }
    
    private void configure() {
        updateTermsUse = WidgetUtil.getIButton("Terms of Use have been modified", CoreConstants.ICON_EDIT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                
            }
        });
        updateTermsUse.setWidth(200);
        addButtons(updateTermsUse);        
    }
}
