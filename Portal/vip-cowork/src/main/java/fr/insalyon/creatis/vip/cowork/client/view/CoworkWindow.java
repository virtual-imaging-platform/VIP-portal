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
