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
package fr.insalyon.creatis.vip.application.client.view.common;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractLaunchStackSection extends SectionStackSection {

    protected ModalWindow modal;
    protected String applicationName;
    protected TextItem simulationNameItem;
    protected VLayout vLayout;

    public AbstractLaunchStackSection(String applicationName) {

        this.applicationName = applicationName;
        this.setShowHeader(false);

        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setPadding(10);
        vLayout.setOverflow(Overflow.AUTO);

        modal = new ModalWindow(vLayout);

        this.addItem(vLayout);
    }

    public abstract void loadInput(String name, String values);

    protected abstract Map<String, String> getParametersMap();

    protected HLayout getSimulatioNameLayout() {

        HLayout simulationLayout = new HLayout(3);

        Label label = new Label("Simulation Name:");
        label.setWidth(150);
        label.setAlign(Alignment.RIGHT);
        simulationLayout.addMember(label);

        simulationNameItem = FieldUtil.getTextItem(400, false, "", "[0-9A-Za-z-_ ]");

        simulationLayout.addMember(FieldUtil.getForm(simulationNameItem));

        return simulationLayout;
    }

    protected IButton getLaunchButton() {

        IButton launchButton = new IButton("Launch");
        launchButton.setIcon(ApplicationConstants.ICON_LAUNCH);
        launchButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validate()) {
                    launch();
                } else {
                    SC.warn("Cannot launch. Some inputs are not valid.");
                }
            }
        });
        return launchButton;
    }

    protected IButton getSaveInputsButton() {

        IButton saveButton = new IButton("Save Inputs");
        saveButton.setIcon(CoreConstants.ICON_SAVED);
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validate()) {
                    verifySimulationName();
                }
            }
        });
        return saveButton;
    }

    protected IButton getSaveAsExampleButton() {

        IButton saveButton = new IButton("Save as Example");
        saveButton.setIcon(CoreConstants.ICON_EXAMPLE);
        saveButton.setWidth(120);
        saveButton.setPrompt("Save the inputs as a featured example that will "
                + "be available for all users.");
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validate()) {
                    saveInputsAsExample();
                }
            }
        });
        return saveButton;
    }

    protected String getSimulationName() {

        return simulationNameItem.getValueAsString().trim();
    }

    /**
     * Validates the form before launch a simulation.
     *
     * @return Result of the validation
     */
    protected abstract boolean validate();

    /**
     * Launches a simulation.
     */
    protected abstract void launch();

    /**
     * Verifies if the simulation name already exists.
     */
    private void verifySimulationName() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<SimulationInput> callback = new AsyncCallback<SimulationInput>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                if (!caught.getMessage().contains("No data is available")) {
                    SC.warn("Unable to verify simulation name:<br />" + caught.getMessage());
                } else {
                    saveInputs(false);
                }
            }

            @Override
            public void onSuccess(SimulationInput result) {
                modal.hide();
                SC.ask("A simulation entitled \"" + getSimulationName() + "\" "
                        + "already exists. <br />Do you want to ovewrite the input data?", new BooleanCallback() {

                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            saveInputs(true);
                        }
                    }
                });
            }
        };
        modal.show("Verifying simulation name...", true);
        service.getInputByNameUserApp(getSimulationName(), applicationName, callback);
    }

    private void saveInputs(boolean update) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to save simulation inputs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().
                        getTab(ApplicationConstants.getLaunchTabID(applicationName));
                launchTab.loadInputsList();
                modal.hide();
                SC.say("Input values were succesfully saved!");
            }
        };
        modal.show("Saving inputs...", true);
        if (update) {
            service.updateSimulationInput(getSimulationInput(), callback);

        } else {
            service.addSimulationInput(getSimulationInput(), callback);
        }
    }

    private void saveInputsAsExample() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to save simulation inputs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().
                        getTab(ApplicationConstants.getLaunchTabID(applicationName));
                launchTab.loadInputsList();
                modal.hide();
                SC.say("Input values were succesfully saved!");
            }
        };
        modal.show("Saving example inputs...", true);
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

        return new SimulationInput(applicationName,
                getSimulationName(), sb.toString());
    }
}
