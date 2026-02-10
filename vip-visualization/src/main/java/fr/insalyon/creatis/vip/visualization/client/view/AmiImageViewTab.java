package fr.insalyon.creatis.vip.visualization.client.view;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.HTMLPane;

import fr.insalyon.creatis.vip.visualization.models.VisualizationItem;

import java.util.logging.Logger;

public class AmiImageViewTab extends AbstractViewTab {

    private static Logger logger =
        Logger.getLogger(AmiImageViewTab.class.getName());

    private final HTMLPane htmlPane;
    private final String divId;
    private JavaScriptObject amiJsViewer;

    public AmiImageViewTab(String lfn) {
        super(lfn);
        htmlPane = new HTMLPane();
        htmlPane.setShowEdges(false);
        this.divId = "id_" + sanitize(filename);
        htmlPane.setContents(
            "<div id=\"" + divId + "\" class=\"ami-container\"></div>" +
            "<div id=\"gui_" + divId + "\" class=\"ami-gui-container\"></div>");
        htmlPane.setWidth100();
        htmlPane.setHeight100();
        htmlPane.addResizedHandler((event) -> {
                // The check for null is needed because some resize events are
                // received before the image has been loaded.
                if (amiJsViewer != null) {
                    resizeCanvas(amiJsViewer);
                }
            });
        this.setID(tabIdFrom(filename));
        this.getPane().addChild(htmlPane);
    }

    public static boolean isFileSupported(String filename) {
        String[] allowedExtensions =
            {"nii", "nii_", "dcm", "dic", "dicom", "ima", "mhd", "nrrd", "mgh"};
        String n = filename.toLowerCase();
        return n.endsWith(".mgz")
            || java.util.Arrays.stream(allowedExtensions).anyMatch(
                s -> n.endsWith("." + s) || n.endsWith("." + s + ".gz"));
    }

    public static String fileTypeName(){
        return "image";
    }

    public static String tabIdFrom(String filename) {
        int i = filename.lastIndexOf('/');
        String name = filename.substring(i + 1);
        return "ami_view_" + sanitize(name) + "_tab";
    }

    @Override
    public void displayFile(VisualizationItem item) {
        String url = getFileUrl(item.getLfn());
        amiJsViewer = showAmiImage(url, item.getExtension(), divId);
    }

    public native JavaScriptObject
        showAmiImage(String url, String extension, String divId) /*-{
        return $wnd.amiViewer(url, extension, divId);
    }-*/;

    public native void resizeCanvas(JavaScriptObject amiViewer) /*-{
        $wnd.amiViewerResizeCanvas(amiViewer);
    }-*/;

    private static String sanitize(String filename) {
        return filename.replaceAll("[ -./]", "_").toLowerCase();
    }
}
