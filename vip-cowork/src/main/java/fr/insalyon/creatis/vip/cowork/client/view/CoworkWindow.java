/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cowork.client.view;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import fr.insalyon.creatis.vip.cowork.client.CoworkConstants;

/**
 *
 * @author glatard
 */
public class CoworkWindow extends Window {
    
     public CoworkWindow() {
    
         this.setTitle(Canvas.imgHTML(CoworkConstants.ICON_SD) + " Cowork ");
     
        this.setWidth(400);
        this.setHeight(200);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();
       // this.setPadding(5);

        this.addItem(new Label("<center><p>Launching Cowork...</p><img src=\"images/"+CoworkConstants.APP_IMG_SD+"\"/></center>"));    
        AppletHTMLPane ahp = new AppletHTMLPane(
                "Cowork", 
                "fr.insalyon.creatis.vip.vipcoworkapplet.Cowork", 
                "vip-cowork-applet.jar", 1, 1
                );
        ahp.setBorder("0px ");
        this.addItem(ahp);
    }
}
