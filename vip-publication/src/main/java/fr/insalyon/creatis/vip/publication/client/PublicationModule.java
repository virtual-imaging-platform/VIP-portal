package fr.insalyon.creatis.vip.publication.client;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.publication.client.view.PublicationConstants;
import fr.insalyon.creatis.vip.publication.client.view.PublicationParser;
import fr.insalyon.creatis.vip.publication.client.view.PublicationTab;

import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva, Sorina Pop
 */
public class PublicationModule extends Module {

    public PublicationModule() {
        CoreModule.getHomePageActions().put(CoreConstants.HOME_ACTION_SHOW_PUBLICATIONS, new Runnable() {
            @Override
            public void run() {
                Layout.getInstance().addTab(
                        PublicationConstants.TAB_PUBLICATION, PublicationTab::new);
            }
        });
    }

    @Override
    public void load() {
        Layout.getInstance().removeTab(PublicationConstants.TAB_PUBLICATION);
        CoreModule.addGeneralApplicationParser(new PublicationParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate(Set<Tab> removedTabs) {
    }
}
