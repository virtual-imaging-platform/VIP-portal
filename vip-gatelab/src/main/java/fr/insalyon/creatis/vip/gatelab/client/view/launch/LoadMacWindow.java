package fr.insalyon.creatis.vip.gatelab.client.view.launch;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;

/**
 *
 * @author Sorina Camarasu, Rafael Ferreira da Silva
 */
public class LoadMacWindow extends Window {

    public LoadMacWindow(ModalWindow modal, String baseDir) {

        this.setTitle("Load Main Mac File");
        this.setWidth(590);
        this.setHeight(320);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();
        this.setPadding(5);
        
        this.addItem(new LoadMacHTMLPane(
                "MacUpload", 
                GWT.getModuleBaseURL() + "/fileuploadservice", 
                "gateParentFolderID", "macFileID", baseDir, "dataManagerUploadComplete",
                false, false));
    }
}