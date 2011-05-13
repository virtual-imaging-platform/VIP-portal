/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.portal.client.view.simulationGUI;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 *
 * @author glatard
 */
class SimulationGUITab extends Tab{
    private final ToolStrip toolStrip;
    private final DefineSceneSection defineSceneSection;
    private final DefineParamsSection defineParamsSection;

    public SimulationGUITab() {
        this.setTitle("New Simulation");
        this.setID("new-simulation-tab");
        this.setCanClose(true);
        VLayout vLayout = new VLayout();

        toolStrip = new ToolStrip();
        vLayout.addMember(toolStrip);

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);

        defineSceneSection = new DefineSceneSection();
        defineParamsSection = new DefineParamsSection();

        sectionStack.setSections(defineSceneSection,defineParamsSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
    }

}
