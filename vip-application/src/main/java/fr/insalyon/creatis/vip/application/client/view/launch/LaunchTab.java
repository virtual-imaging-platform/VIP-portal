/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

/**
 *
 * @author Rafael Silva
 */
public class LaunchTab extends Tab {

    private String applicationClass;
    private SectionStack sectionStack;
    private LaunchStackSection launchSection;
    private LaunchToolStrip launchToolStrip;
    private InputsStackSection inputsSection;

    public LaunchTab(String applicationClass, String applicationName) {
        initTab("Launch " + applicationName, applicationClass, false, 
                "launch-" + applicationName.toLowerCase() + "-tab");
        createSimulation(applicationName);
    }

    public LaunchTab(String applicationClass) {
        initTab("Launch " + applicationClass, applicationClass, true, 
                "launch-" + applicationClass.toLowerCase() + "-tab");
    }

    private void initTab(String title, String applicationClass, boolean showToolStrip, String id) {
        this.applicationClass = applicationClass;
        this.setTitle(title);
        this.setID(id);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        launchToolStrip = new LaunchToolStrip(applicationClass);

        VLayout vLayout = new VLayout();
        if (showToolStrip) {
            vLayout.addMember(launchToolStrip);
        }
        vLayout.setWidth100();
        vLayout.setHeight100();

        sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        launchSection = new LaunchStackSection(applicationClass);

        sectionStack.setSections(launchSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
    }

    public void createSimulation(String simulationName) {
        launchSection.load(simulationName);
    }

    public void loadInput(String values) {
        launchSection.loadInput(values);
    }

    public void loadInputsList() {
        inputsSection.loadData();
    }

    public void addInputsSection() {
        if (inputsSection == null) {
            inputsSection = new InputsStackSection(applicationClass);
            sectionStack.addSection(inputsSection);
        }
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
        launchSection.setInputValue(inputName, value);
    }
}
