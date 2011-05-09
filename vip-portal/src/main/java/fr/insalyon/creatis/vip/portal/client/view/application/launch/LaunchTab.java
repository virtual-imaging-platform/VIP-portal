/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.portal.client.view.application.launch;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LaunchTab extends Tab {

    private LaunchStackSection launchSection;
    private LaunchToolStrip launchToolStrip;
    private InputsStackSection inputsSection;

    public LaunchTab(String applicationClass) {

        this.setTitle("Launch " + applicationClass);
        this.setID("launch-" + applicationClass.toLowerCase() + "-tab");
//        this.setIcon("icon-launch.png");
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        launchToolStrip = new LaunchToolStrip(applicationClass);
                
        VLayout vLayout = new VLayout();
        vLayout.addMember(launchToolStrip);
        vLayout.setWidth100();
        vLayout.setHeight100();

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        launchSection = new LaunchStackSection(applicationClass);
        inputsSection = new InputsStackSection(applicationClass);

        sectionStack.setSections(launchSection, inputsSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
    }
    
    public void createSimulation(String simulationName) {
        launchSection.load(simulationName);
    }
    
    public void loadInput(String values) {
        launchSection.loadInput(values);
    }
    
    public Map<String, String> getParametersMap() {
        return launchSection.getParametersMap();
    }
    
    public void enableSaveButton() {
        launchToolStrip.enableSaveInputButton();
    }
    
    public void loadInputsList() {
        inputsSection.loadData();
    }
}
