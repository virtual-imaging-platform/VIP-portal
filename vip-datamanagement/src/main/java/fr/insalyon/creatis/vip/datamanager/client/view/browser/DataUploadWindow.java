package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
 */
public class DataUploadWindow extends Window {

    public DataUploadWindow(ModalWindow modal, String baseDir, String target) {

        this.setTitle(Canvas.imgHTML(DataManagerConstants.ICON_UPLOAD_MULTIPLE) + " Upload folder to: " + baseDir);
        this.setWidth(450);
        this.setHeight(200);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();
        this.setPadding(5);

        this.addItem(new DataUploadHTMLPane(
                "DataUpload", 
                GWT.getModuleBaseURL() + "/fileuploadservice", 
                "folderToUpload", baseDir, target, true, true));
    }
}
