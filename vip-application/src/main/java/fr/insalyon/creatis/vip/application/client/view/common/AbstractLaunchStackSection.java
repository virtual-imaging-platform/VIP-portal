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
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractLaunchStackSection extends SectionStackSection {

    protected String applicationClass;
    protected String launchTabID;
    protected String simulationName;
    protected TextItem simulationNameItem;
    protected VLayout layout;

    public AbstractLaunchStackSection(String applicationClass, String launchTabID) {

        this.applicationClass = applicationClass;
        this.launchTabID = launchTabID;
        this.setShowHeader(false);

        layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setPadding(10);
        layout.setOverflow(Overflow.AUTO);
    }

    public void load(String simulationName) {
        this.simulationName = simulationName;
        loadData();
    }

    public abstract void loadInput(String values);

    protected abstract void loadData();

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

    protected IButton getSaveInputsButton() {

        IButton saveButton = new IButton("Save Inputs");
        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (simulationNameItem.validate()) {
                    verifySimulationName();
                } else {
                    SC.warn("You should provide a Simulation Name.");
                }
            }
        });
        return saveButton;
    }

    private void verifySimulationName() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<SimulationInput> callback = new AsyncCallback<SimulationInput>() {

            public void onFailure(Throwable caught) {
                if (!caught.getMessage().contains("No data is available")) {
                    SC.warn("Unable to perform save operation: " + caught.getMessage());
                } else {
                    saveInputs(false);
                }
            }

            public void onSuccess(SimulationInput result) {
                SC.ask("A simulation entitled \"" + simulationNameItem.getValueAsString() + "\" "
                        + "already exists. <br />Do you want to ovewrite the input data?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            saveInputs(true);
                        }
                    }
                });
            }
        };
        service.getInputByNameUserApp(Context.getInstance().getUserDN(),
                simulationNameItem.getValueAsString(), simulationName, callback);
    }

    private void saveInputs(boolean update) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error while saving simulation inputs: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().
                        getTab(launchTabID);
                launchTab.loadInputsList();
                SC.say("Input values were succesfully saved!");
            }
        };
        
        if (update) {
            service.updateSimulationInput(Context.getInstance().getUserDN(),
                    getSimulationInput(), callback);

        } else {
            service.addSimulationInput(Context.getInstance().getUserDN(),
                    getSimulationInput(), callback);
        }
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

        return new SimulationInput(simulationName,
                simulationNameItem.getValueAsString(), sb.toString());
    }
}
