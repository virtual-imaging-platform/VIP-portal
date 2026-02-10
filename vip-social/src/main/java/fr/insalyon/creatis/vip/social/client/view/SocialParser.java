package fr.insalyon.creatis.vip.social.client.view;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.social.client.SocialConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SocialParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        addApplication(SocialConstants.APP_SOCIAL, SocialConstants.APP_IMG_SOCIAL);
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(SocialConstants.APP_SOCIAL)) {
            Layout.getInstance().addTab(
                SocialConstants.TAB_SOCIAL, SocialTab::new);
            return true;
        }
        return false;
    }
}
