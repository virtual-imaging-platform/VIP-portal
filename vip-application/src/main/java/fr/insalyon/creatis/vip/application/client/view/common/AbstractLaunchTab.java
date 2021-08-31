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
package fr.insalyon.creatis.vip.application.client.view.common;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.launch.AbstractLaunchFormLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.InputsLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class AbstractLaunchTab extends Tab {

    protected HLayout layout;
    protected String applicationName;
    protected String applicationVersion;
    protected String applicationClass;
    protected ModalWindow modal;
    protected InputsLayout inputsLayout;
    protected IButton launchButton;
    protected IButton saveInputsButton;
    protected IButton saveAsExampleButton;

    public AbstractLaunchTab(String applicationName, String applicationVersion, 
            String applicationClass) {

        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationClass = applicationClass;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_APPLICATION) + " "
                + applicationName + " " + applicationVersion);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        this.setID(ApplicationConstants.getLaunchTabID(applicationName));

        layout = new HLayout(10);
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(5);

        modal = new ModalWindow(layout);

        this.setPane(layout);
    }

    /**
     * Launches a simulation.
     */
    protected abstract void launch();

    /**
     *
     */
    public void loadInputsList() {

        inputsLayout.loadData();
    }

    /**
     * Loads input values from string.
     *
     * @param simulationName Simulation name
     * @param values Input values
     */
    public void loadInput(String simulationName, String values) {

        Map<String, String> valuesMap = new HashMap<>();

        for (String input : values.split("<br />")) {
            String[] s = input.split(" = ");
            valuesMap.put(s[0], s[1] != null ? s[1] : "");
        }
        loadInput(simulationName, valuesMap);
    }

    /**
     * Loads input values from map.
     *
     * @param simulationName Simulation name
     * @param values Input values map
     */
    public void loadInput(String simulationName, Map<String, String> values) {
        getLaunchFormLayout().loadInputs(simulationName, values);
    }

    /**
     *
     */
    protected void configureInputsLayout(boolean showExamples) {

        inputsLayout = new InputsLayout(getAttribute("ID"), applicationName, showExamples);
        layout.addMember(inputsLayout);
    }

    /**
     *
     * @return Map of String input IDs to String representing corresponding input values
     */
    protected Map<String, String> getParametersMap() {

        return getLaunchFormLayout().getParametersMap();
    }

    /**
     * Configures the launch button.
     */
    protected void configureLaunchButton() {

        launchButton = WidgetUtil.getIButton("Launch", ApplicationConstants.ICON_LAUNCH,
                event -> {
                    if (validate()) {
                        launch();
                    } else {
                        Layout.getInstance().setWarningMessage("Cannot launch. Some inputs are not valid.");
                    }
                });
    }
    
    /**
     * Resets the launch button to its initial state.
     */
    protected void resetLaunchButton() {
        WidgetUtil.resetIButton(launchButton, "Launch", ApplicationConstants.ICON_LAUNCH);
    }

    /**
     * Configures the save inputs button.
     */
    protected void configureSaveInputsButton() {

        saveInputsButton = WidgetUtil.getIButton("Save Inputs", CoreConstants.ICON_SAVED,
                event -> {
                    if (validate()) {
                        saveInputs();
                    } else {
                        Layout.getInstance().setWarningMessage("Cannot save inputs. Some inputs are not valid.");
                    }
                });
    }

    /**
     * Resets the save inputs button to its initial state.
     */
    protected void resetSaveInputsButton() {

        WidgetUtil.resetIButton(saveInputsButton, "Save Inputs", CoreConstants.ICON_SAVED);
    }

    /**
     * Configures the save as example button.
     */
    protected void configureSaveAsExampleButton() {

        saveAsExampleButton = WidgetUtil.getIButton("Save as Example", CoreConstants.ICON_EXAMPLE,
                event -> {
                    if (validate()) {
                        saveInputsAsExample();
                    }
                });
        saveAsExampleButton.setPrompt("Save the inputs as a featured example that will "
                + "be available for all users.");
        saveAsExampleButton.setWidth(120);
    }

    /**
     * Resets the save as example button to its initial state.
     */
    protected void resetSaveAsExampleButton() {

        WidgetUtil.resetIButton(saveAsExampleButton, "Save as Example", CoreConstants.ICON_EXAMPLE);
    }

    /**
     * Gets the name of the simulation.
     *
     * @return String
     */
    protected String getSimulationName() {
        return getLaunchFormLayout().getSimulationName();
    }

    /**
     * Validates the form before launch a simulation.
     *
     * @return Result of the validation
     */
    protected boolean validate() {
        return getLaunchFormLayout().validate();
    }

    /**
     * Verifies if the simulation name already exists and save the inputs.
     */
    private void saveInputs() {

        WidgetUtil.setLoadingIButton(saveInputsButton, "Saving...");
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<SimulationInput> callback = new AsyncCallback<SimulationInput>() {
            @Override
            public void onFailure(Throwable caught) {
                if (!caught.getMessage().contains("No data is available")
                        && !caught.getMessage().contains("empty result set")) {
                    resetSaveInputsButton();
                    Layout.getInstance().setWarningMessage("Unable to verify execution name:<br />" + caught.getMessage(), 10);
                } else {
                    saveInputs(false);
                }
            }

            @Override
            public void onSuccess(SimulationInput result) {
                SC.ask("A simulation entitled \"" + getSimulationName() + "\" "
                        + "already exists. <br />Do you want to overwrite the input data?", value -> {
                            if (value) {
                                saveInputs(true);
                            } else {
                                resetSaveInputsButton();
                            }
                        });
            }
        };
        service.getInputByNameUserApp(getSimulationName(), applicationName, callback);
    }

    private void saveInputs(boolean update) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                resetSaveInputsButton();
                Layout.getInstance().setWarningMessage("Unable to save simulation inputs:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Void result) {
                AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().
                        getTab(ApplicationConstants.getLaunchTabID(applicationName));
                launchTab.loadInputsList();
                resetSaveInputsButton();
                Layout.getInstance().setNoticeMessage("Input values were successfully saved!", 10);
            }
        };
        if (update) {
            service.updateSimulationInput(getSimulationInput(), callback);
        } else {
            service.addSimulationInput(getSimulationInput(), callback);
        }
    }

    private void saveInputsAsExample() {

        WidgetUtil.setLoadingIButton(saveAsExampleButton, "Saving...");
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                resetSaveAsExampleButton();
                Layout.getInstance().setWarningMessage("Unable to save example inputs:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Void result) {
                AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().
                        getTab(ApplicationConstants.getLaunchTabID(applicationName));
                launchTab.loadInputsList();
                resetSaveAsExampleButton();
                Layout.getInstance().setNoticeMessage("Examples input values were successfully saved!", 10);
            }
        };
        service.saveInputsAsExamples(getSimulationInput(), callback);
    }

    private SimulationInput getSimulationInput() {

        StringBuilder sb = new StringBuilder();
        for (String k : getParametersMap().keySet()) {
            sb.append(k);
            sb.append("=");
            String value = getParametersMap().get(k);
            if (value == null) {
                value = "";
            } else if (value.contains("null")) {
                value = value.replaceAll("null", "");
            }
            sb.append(value);
            sb.append("--");
        }

        return new SimulationInput(applicationName, getSimulationName(), sb.toString());
    }

    /**
     * @return AbstractLaunchFormLayout representing this tab's launch form
     */
    protected abstract AbstractLaunchFormLayout getLaunchFormLayout();
}
