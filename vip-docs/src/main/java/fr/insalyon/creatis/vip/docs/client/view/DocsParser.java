package fr.insalyon.creatis.vip.docs.client.view;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DocsParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        addApplication(DocsConstants.APP_DOCUMENTATION, DocsConstants.APP_IMG_DOCUMENTATION);
        addApplication(DocsConstants.APP_GALLERY, DocsConstants.APP_IMG_GALLERY);
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(DocsConstants.APP_DOCUMENTATION)) {
            Layout.getInstance().addTab(
                DocsConstants.TAB_DOCUMENTATION, DocumentationTab::new);
            return true;

        } else if (applicationName.equals(DocsConstants.APP_GALLERY)) {
            Layout.getInstance().addTab(
                DocsConstants.TAB_GALLERY, GalleryTab::new);
            return true;
        }
        return false;
    }
}
