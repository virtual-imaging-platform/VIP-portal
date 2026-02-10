package fr.insalyon.creatis.vip.visualization.client;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserContextMenu;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserContextMenu.Visualizer;
import fr.insalyon.creatis.vip.visualization.client.view.AbstractViewTab;
import fr.insalyon.creatis.vip.visualization.client.view.AmiImageViewTab;
import fr.insalyon.creatis.vip.visualization.client.view.BrainBrowserViewTab;

import java.util.Set;
import java.util.function.Consumer;

public class VisualizationModule extends Module {

    @Override
    public void load() {
        BrowserContextMenu.addVisualizer(amiImageVisualizer);
        BrowserContextMenu.addVisualizer(brainBrowserVisualizer);
    }

    @Override
    public void postLoading() {}

    @Override
    public void terminate(Set<Tab> removedTabs) {}

    private Visualizer brainBrowserVisualizer =
        new Visualizer() {
            @Override
            public boolean isFileSupported(String filename) {
                return BrainBrowserViewTab.isFileSupported(filename);
            }

            @Override
            public String fileTypeName() {
                return BrainBrowserViewTab.fileTypeName();
            }

            @Override
            public Consumer<String> viewStarter() {
                return new Consumer<String>() {
                    @Override
                    public void accept(String filename) {
                        AbstractViewTab tab =
                            (AbstractViewTab) Layout.getInstance().addTab(
                                BrainBrowserViewTab.ID,
                                () -> new BrainBrowserViewTab(filename));
                        tab.load();
                    }
                };
            }
        };

    private Visualizer amiImageVisualizer =
        new Visualizer() {
            @Override
            public boolean isFileSupported(String filename) {
                return AmiImageViewTab.isFileSupported(filename);
            }

            @Override
            public String fileTypeName() {
                return AmiImageViewTab.fileTypeName();
            }

            @Override
            public Consumer<String> viewStarter() {
                return new Consumer<String>() {
                    @Override
                    public void accept(String filename) {
                        AbstractViewTab tab =
                            (AbstractViewTab) Layout.getInstance().addTab(
                                AmiImageViewTab.tabIdFrom(filename),
                                () -> new AmiImageViewTab(filename));
                        tab.load();
                    }
                };
            }
        };
}
