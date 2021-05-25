/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.visualization.client.view;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.HTMLPane;
import fr.insalyon.creatis.vip.visualization.client.bean.VisualizationItem;

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
