/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.visualization;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;

/**
 *
 * @author glatard
 */
public class BrainBrowserViewTab extends Tab {

    private final HTMLPane htmlPane;
    private final ModalWindow modal;

    public BrainBrowserViewTab(final String lfn) {
        String fileName = lfn.substring(lfn.lastIndexOf('/') + 1);
        this.setTitle(fileName);
        this.setCanClose(true);

        htmlPane = new HTMLPane();
        htmlPane.setShowEdges(false);
        htmlPane.setContents("<div id=\"brain-browser\"></div>");
        this.setPane(htmlPane);
        modal = new ModalWindow(htmlPane);
        
        loadSurface(lfn);
    }

    public static boolean isSupported(String fileName) {
        return fileName.endsWith(".obj") || fileName.endsWith(".asc");
    }

    private void loadSurface(String lfn) {
        DataManagerServiceAsync dmsa = DataManagerService.Util.getInstance();
        modal.show("Loading surface file...", true);
        dmsa.getSurfaceFileURL(lfn, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Cannot load surface: " + caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                modal.hide();
                showBrainBrowser(result);
            }
        });
    }
    
    public native void showBrainBrowser(String fileName) /*-{
           $wnd.$("#brain-browser").load("https://brainbrowser.cbrain.mcgill.ca/surface-viewer-widget?version=1.5.2&nothreejs=true&model="+fileName);         
    }-*/;
}
