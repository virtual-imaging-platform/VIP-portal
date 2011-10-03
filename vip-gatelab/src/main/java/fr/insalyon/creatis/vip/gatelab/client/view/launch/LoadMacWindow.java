/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.gatelab.client.view.launch;

import com.smartgwt.client.widgets.Window;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.view.common.AppletHTMLPane;
/**
 *
 * @author camarasu
 */
public class LoadMacWindow extends Window {

    public LoadMacWindow(ModalWindow modal, String baseDir) {

        this.setTitle("Load Main Mac File");
        this.setWidth(750);
        this.setHeight(550);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();
        this.setPadding(5);

        this.addItem(new AppletHTMLPane(
                        "DataUpload",
                        "fr.insalyon.creatis.vip.gatelab.applet.loadmac.LoadMac",
                        "vip-gatelab-applet.jar", 710, 525,
                         baseDir, false, false));
    }
    

}