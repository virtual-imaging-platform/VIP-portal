/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.gatelab.client.view.launch;

//import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
//import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
//import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author camarasu
 */
public class GateLabLaunchTab extends AbstractLaunchTab {



    //private GateLabLaunchStackSection launchSection;

    public GateLabLaunchTab (String applicationName) {

        super(applicationName);

        this.launchSection = new GateLabLaunchStackSection(applicationName);
        sectionStack.setSections(launchSection);

        addInputsSection();
    }

    /**
     * Sets a value to an input name. The value should be in the following forms:
     *
     * For single list field: a string
     * For multiple list fields: strings separated by '; '
     * For ranges: an string like 'Start: 0 - Stop: 0 - Step: 0'
     *
     * @param inputName
     * @param value
     */
    public void setInputValue(String inputName, String value) {
        //launchSection.setInputValue(inputName, value);
    }


}
