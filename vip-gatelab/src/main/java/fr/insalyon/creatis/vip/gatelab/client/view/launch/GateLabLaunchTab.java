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

    public GateLabLaunchTab () {

        super("Launch GateLab", "GATE", true,  "launch-gate-tab");
        initTab();
        //done in LaunchToolsSTrip inst by the asbtract class
        //createSimulation("GATE");
        //addInputsSection();
    }

    private void initTab() {

        this.launchSection = new GateLabLaunchStackSection(this.getID());
        sectionStack.setSections(launchSection);
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

/*
        private void initTab(String title, String applicationClass, boolean showToolStrip, String id) {

        this.setTitle(title);
        this.setID(id);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);


        VLayout vLayout = new VLayout();

        vLayout.setWidth100();
        vLayout.setHeight100();

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        launchSection = new GateLabLaunchStackSection();

        sectionStack.setSections(launchSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
    }
*/
}
