package fr.insalyon.creatis.vip.visualization.client.view;

import com.smartgwt.client.widgets.HTMLPane;

import fr.insalyon.creatis.vip.visualization.models.VisualizationItem;

/** @author glatard */
public class BrainBrowserViewTab extends AbstractViewTab {

    private final HTMLPane htmlPane;

    public static final String ID = "brain_browser_tab";

    public BrainBrowserViewTab(String lfn) {
        super(lfn);
        htmlPane = new HTMLPane();
        htmlPane.setShowEdges(false);
        htmlPane.setContents("<div id=\"brain-browser\"></div>");
        htmlPane.setWidth100();
        htmlPane.setHeight100();
        this.setID(ID);
        this.getPane().addChild(htmlPane);
    }

    public static boolean isFileSupported(String fileName) {
        return fileName.endsWith(".obj") || fileName.endsWith(".asc");
    }

    public static String fileTypeName(){
        return "surface";
    }

    @Override
    public void displayFile(VisualizationItem item) {
        String url = getFileUrl(item.getLfn());
        if (item.getLfn().endsWith(".asc")) {
            showBrainBrowserForAsc(url);
        } else {
            showBrainBrowserForObj(url);
        }
    }

    public native void showBrainBrowserForObj(String fileUrl) /*-{
           $wnd.$("#brain-browser").load("https://brainbrowser.cbrain.mcgill.ca/surface-viewer-widget?version=2.5.2&model="+fileUrl);
    }-*/;

    public native void showBrainBrowserForAsc(String fileUrl) /*-{
           $wnd.$("#brain-browser").load("https://brainbrowser.cbrain.mcgill.ca/surface-viewer-widget?version=2.5.2&model="+fileUrl+"&format=freesurferasc");
    }-*/;
}
