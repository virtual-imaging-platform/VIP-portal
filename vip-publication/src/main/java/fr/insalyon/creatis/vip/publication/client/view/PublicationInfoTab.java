package fr.insalyon.creatis.vip.publication.client.view;

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
        addMember(WidgetUtil.getLabel("<b>Please list here the references " +
                "of the publications that you made using VIP. These references " +
                "may be used by the VIP team to justify the use of computing " +
                "and storage resources. <br/>" +
                "\uD83C\uDDEB\uD83C\uDDF7 french users: " +
                "France-Grilles also requires that the " +
                "publications made using their infrastructure are " +
                "registered in <a href=\"http://hal.archives-ouvertes.fr\">HAL</a> " +
                "(only authors can do that, we cannot do it for you).</b>", 20));
        
    }
    
}
