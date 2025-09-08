package fr.insalyon.creatis.vip.datamanager.client;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerHomeParser;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSystemParser;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DataManagerModule extends Module {

    public static DataManagerSection dataManagerSection;

    @Override
    public void load() {

        CoreModule.addGeneralApplicationParser(new DataManagerHomeParser());
        CoreModule.addSystemApplicationParser(new DataManagerSystemParser());

        dataManagerSection = new DataManagerSection();
        Layout.getInstance().addMainSection(dataManagerSection);
    }

    @Override
    public void terminate(Set<Tab> removedTabs) {
        Layout.getInstance().removeMainSection(DataManagerConstants.SECTION_FILE_TRANSFER);
        BrowserLayout.terminate();
        OperationLayout.terminate();
    }

    @Override
    public void postLoading() {
    }
}
