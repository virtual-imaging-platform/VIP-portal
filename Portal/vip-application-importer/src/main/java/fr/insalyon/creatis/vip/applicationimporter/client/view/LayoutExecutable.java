/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.view;

import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutExecutable extends AbstractFormLayout {
/**
 * 
 * @param width
 * @param height 
 */
    public LayoutExecutable(String width, String height) {
        super(width, height);
        this.addTitle("Executable", ApplicationImporterConstants.ICON_EXECUTABLE);
    }
}
