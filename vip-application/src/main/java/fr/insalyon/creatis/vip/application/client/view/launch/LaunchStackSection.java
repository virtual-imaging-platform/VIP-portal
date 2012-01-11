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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchStackSection;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LaunchStackSection extends AbstractLaunchStackSection {

    private VLayout formLayout, inputs;
    private String tabID;

    public LaunchStackSection(String applicationName, String tabId) {

        super(applicationName);

        this.tabID = tabId;
        formLayout = new VLayout(3);
        formLayout.setAutoHeight();
        vLayout.addMember(formLayout);

        loadData();
    }

    /**
     * Loads input values from string.
     *
     * @param values Input values
     */
    public void loadInput(String name, String values) {

        simulationNameItem.setValue(name);
        Map<String, String> valuesMap = new HashMap<String, String>();
        final Map<String, String> conflictMap = new HashMap<String, String>();

        for (String input : values.split("<br />")) {
            String[] s = input.split(" = ");
            valuesMap.put(s[0], s[1] != null ? s[1] : "");
        }

        StringBuilder sb = new StringBuilder();
        for (Canvas canvas : inputs.getMembers()) {
            if (canvas instanceof InputHLayout) {
                final InputHLayout input = (InputHLayout) canvas;
                final String inputValue = valuesMap.get(input.getName());

                if (inputValue != null) {
                    if (input.getValue() == null || input.getValue().isEmpty()) {
                        input.setValue(inputValue);

                    } else {
                        conflictMap.put(input.getName(), inputValue);
                    }
                } else {
                    sb.append("Could not find value for parameter \"");
                    sb.append(input.getName()).append("\".<br />");
                }
            }
        }
        if (!conflictMap.isEmpty()) {
            SC.ask("The following fields already have a value.<br />"
                    + "Do you want to replace them?<br />"
                    + "Fields: " + conflictMap.keySet(), new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value) {
                        for (Canvas canvas : inputs.getMembers()) {
                            if (canvas instanceof InputHLayout) {
                                final InputHLayout input = (InputHLayout) canvas;
                                final String inputValue = conflictMap.get(input.getName());
                                if (inputValue != null) {
                                    input.setValue(inputValue);
                                }
                            }
                        }
                    }
                }
            });
        }
        if (sb.length() > 0) {
            SC.warn(sb.toString());
        }
    }

    /**
     * Sets a value to an input name. The value should be in the following
     * forms:
     *
     * For single list field: a string For multiple list fields: strings
     * separated by '; ' For ranges: an string like 'Start: 0 - Stop: 0 - Step:
     * 0'
     *
     * @param inputName
     * @param value
     */
    public void setInputValue(String inputName, String value) {

        for (Canvas canvas : formLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                if (input.getName().equals(inputName)) {
                    input.setValue(value);
                }
            }
        }
    }

    /**
     * Loads simulation sources list.
     */
    protected void loadData() {

        formLayout.removeMembers(formLayout.getMembers());

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Descriptor> callback = new AsyncCallback<Descriptor>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to download application source file:<br />" + caught.getMessage());
            }

            public void onSuccess(Descriptor d) {
                AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().getTab(tabID);
                launchTab.getDescriptionSection().setContents(d.getDescription());
                List<Source> sources = d.getSources();


                formLayout.addMember(getSimulatioNameLayout());

                HLayout inputLayout = new HLayout(5);
                inputLayout.setMargin(20);

                inputs = new VLayout(3);
                inputs.setAutoHeight();
                for (Source source : sources) {
                    inputs.addMember(new InputHLayout(source.getName(), source.getDescription()));
                }
                HLayout inputsLayout = new HLayout(5);
                inputsLayout.setAlign(VerticalAlignment.CENTER);
                inputsLayout.setMargin(20);
                inputsLayout.addMember(inputs);
                formLayout.addMember(inputsLayout);

                HLayout buttonsLayout = new HLayout(5);
                buttonsLayout.setAlign(VerticalAlignment.CENTER);
                buttonsLayout.setMargin(20);
                formLayout.addMember(buttonsLayout);

                IButton launchButton = new IButton("Launch");
                launchButton.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        if (validate()) {
                            launch();
                        }
                    }
                });
                buttonsLayout.addMember(launchButton);
                buttonsLayout.addMember(getSaveInputsButton());
                modal.hide();
            }
        };
        modal.show("Loading launch panel...", true);
        service.getApplicationDescriptor(applicationName, callback);
    }

    /**
     * Validates the form before launch a simulation.
     *
     * @return Result of the validation
     */
    @Override
    protected boolean validate() {

        boolean valid = simulationNameItem.validate();
        for (Canvas canvas : inputs.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                if (!input.validate()) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    /**
     * Launches a simulation.
     */
    private void launch() {

        modal.show("Launching simulation '" + simulationNameItem.getValueAsString()
                + "'...", true);

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error on input data:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                submitWorkflow();
            }
        };
        List<String> inputData = new ArrayList<String>();
        for (String input : getParametersMap().values()) {
            if (input.startsWith(DataManagerConstants.ROOT)) {
                if (input.contains(ApplicationConstants.SEPARATOR_LIST)) {
                    for (String i : input.split(ApplicationConstants.SEPARATOR_LIST)) {
                        inputData.add(i);
                    }
                } else {
                    inputData.add(input);
                }
            }
        }
        if (!inputData.isEmpty()) {
            service.validateInputs(inputData, callback);
        } else {
            submitWorkflow();
        }
    }

    private void submitWorkflow() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to launch the simulation:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SC.say("Simulation '" + simulationNameItem.getValueAsString()
                        + "' successfully launched.");
            }
        };
        service.launchSimulation(getParametersMap(), applicationName,
                simulationNameItem.getValueAsString().trim(), callback);
    }

    /**
     * Gets a map of parameters.
     *
     * @return Map of parameters
     */
    public Map<String, String> getParametersMap() {

        Map<String, String> paramsMap = new HashMap<String, String>();

        for (Canvas canvas : inputs.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                paramsMap.put(input.getName(), input.getValue());
            }
        }
        return paramsMap;
    }
}
