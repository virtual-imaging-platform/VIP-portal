package fr.insalyon.creatis.vip.applicationimporter.client;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.applicationimporter.client.view.HomeParser;

import java.util.Set;

/**
 *
 * @author Nouha Boujelben
 */
public class ApplicationImporterModule extends Module {

    @Override
    public void load() {
        CoreModule.addSystemApplicationParser(new HomeParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate(Set<Tab> removedTabs) {
    }
}
