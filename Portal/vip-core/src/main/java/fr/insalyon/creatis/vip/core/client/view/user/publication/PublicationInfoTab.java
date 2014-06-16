/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.publication;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Nouha boujelben
 */
public class PublicationInfoTab extends AbstractFormLayout{
    public PublicationInfoTab() {

        super("100%", "50");
        addTitle("", CoreConstants.ICON_INFO);
        addMember(WidgetUtil.getLabel("<b>Please list here the references of the publications that you made using VIP. These references may"
                + " be used by the VIP team to justify the use of computing and storage resources to the European Grid Infrastructure.</b>", 20));
        
    }
    
}
