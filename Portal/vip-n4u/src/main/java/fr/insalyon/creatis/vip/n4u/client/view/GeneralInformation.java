/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

/**
 *
 * @author Nouha Boujelben
 */
public class GeneralInformation extends AbstractFormLayout {

    /**
     *
     * @param width the GeneralInformation layout's width 
     * @param height the GeneralInformation layout's height 
     */
    public GeneralInformation(String width, String height) {
        super(width, height);
        this.addTitle("General Information", N4uConstants.ICON_INFORMATION);
    }
}
