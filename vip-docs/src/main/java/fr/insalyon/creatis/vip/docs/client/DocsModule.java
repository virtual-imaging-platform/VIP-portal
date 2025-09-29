package fr.insalyon.creatis.vip.docs.client;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.docs.client.view.DocsParser;

import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DocsModule extends Module {

    @Override
    public void load() {
        
        CoreModule.addGeneralApplicationParser(new DocsParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate(Set<Tab> removedTabs) {
    }
}
