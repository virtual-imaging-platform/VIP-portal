/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

/**
 *
 * @author nouha
 */
public class LayoutOutput extends AbstractFormLayout {

    public LayoutOutput(String width, String height) {
        super(width, height);
         this.addTitle("Outputs",  N4uConstants.ICON_OUTPUT);
    }  
    
}
