/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.visualization;

import com.smartgwt.client.widgets.HTMLPane;
import fr.insalyon.creatis.vip.datamanager.client.bean.VisualizationItem;

/**
 *
 * @author glatard
 */
public class BrainBrowserViewTab extends AbstractViewTab {

    private final HTMLPane htmlPane;

    public BrainBrowserViewTab(String lfn) {
        super(lfn);
        htmlPane = new HTMLPane();
        htmlPane.setShowEdges(false);
        htmlPane.setContents("<div id=\"brain-browser\"></div>");
        htmlPane.setWidth100();
        htmlPane.setHeight100();
        this.getPane().addChild(htmlPane);
    }

    @Override
    public boolean isFileSupported(String fileName) {
        return fileName.endsWith(".obj") || fileName.endsWith(".asc");
    }
    
    @Override
    public String fileTypeName(){
        return "surface";
    }
    
    @Override
    public void displayFile(VisualizationItem item) {
        showBrainBrowser(item.getURL());
    }

    
    public native void showBrainBrowser(String fileName) /*-{
           $wnd.$("#brain-browser").load("https://brainbrowser.cbrain.mcgill.ca/surface-viewer-widget?version=2.1.1&nothreejs=true&model="+fileName);         
    }-*/;
    
}
