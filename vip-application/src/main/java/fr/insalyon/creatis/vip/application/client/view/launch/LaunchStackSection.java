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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LaunchStackSection extends SectionStackSection {

    private ModalWindow modal;
    private String applicationClass;
    private String simulationName;
    private VLayout layout;
    private VLayout formLayout;
    private DynamicForm form;

    public LaunchStackSection(String applicationClass) {

        this.applicationClass = applicationClass;

        this.setShowHeader(false);
        layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setPadding(10);
        layout.setOverflow(Overflow.AUTO);

        formLayout = new VLayout(3);
        formLayout.setWidth100();
        formLayout.setAutoHeight();
        layout.addMember(formLayout);

        modal = new ModalWindow(layout);
        this.addItem(layout);
    }

    /**
     * Loads the descriptor file of an application
     * 
     * @param simulationName Name of the simulation
     */
    public void load(String simulationName) {
        this.simulationName = simulationName;
        loadData();
    }

    /**
     * Loads input values from string.
     * 
     * @param values Input values
     */
    public void loadInput(String values) {

        Map<String, String> valuesMap = new HashMap<String, String>();

        for (String input : values.split("<br />")) {
            String[] s = input.split(" = ");
            valuesMap.put(s[0], s[1] != null ? s[1] : "");
        }

        StringBuilder sb = new StringBuilder();
        for (Canvas canvas : formLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                String value = valuesMap.get(input.getName());

                if (value != null) {
                    input.setValue(value);
                } else {
                    sb.append("Could not find value for parameter \""
                            + input.getName() + "\"<br />");
                }
            }
        }
        if (sb.length() > 0) {
            SC.warn(sb.toString());
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
    private void loadData() {

        formLayout.removeMembers(formLayout.getMembers());

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing get application sources list: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                if (result != null) {
                    for (String source : result) {
                        formLayout.addMember(new InputHLayout(source));
                    }

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

                    IButton saveButton = new IButton("Save Inputs");
                    saveButton.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            new SaveInputWindow(applicationClass, simulationName,
                                    getParametersMap()).show();
                        }
                    });
                    buttonsLayout.addMember(saveButton);
                    modal.hide();

                } else {
                    modal.hide();
                    SC.warn("Unable to download application source file.");
                }
            }
        };
        modal.show("Loading Launch Panel...", true);
        Context context = Context.getInstance();
        service.getWorkflowSources(context.getUser(),
                context.getProxyFileName(), simulationName, callback);
    }

    /**
     * Validates the form before launch a simulation.
     * 
     * @return Result of the validation
     */
    private boolean validate() {
        boolean valid = true;
        for (Canvas canvas : formLayout.getMembers()) {
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
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error while launching simulation: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                String simulationID = result.substring(result.lastIndexOf("/") + 1, result.lastIndexOf("."));
                modal.hide();
                SC.say("Simulation successfully launched with ID: " + simulationID);
            }
        };
        modal.show("Launching simulation...", true);
        Context context = Context.getInstance();
        service.launchWorkflow(context.getUser(), getParametersMap(),
                simulationName, context.getProxyFileName(), callback);
    }

    /**
     * Gets a map of parameters.
     * 
     * @return Map of parameters
     */
    public Map<String, String> getParametersMap() {

        Map<String, String> paramsMap = new HashMap<String, String>();

        for (Canvas canvas : formLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                paramsMap.put(input.getName(), input.getValue());
            }
        }

        return paramsMap;
    }

    public String getApplicationClass() {
        return applicationClass;
    }

    public void setApplicationClass(String applicationClass) {
        this.applicationClass = applicationClass;
    }

    public DynamicForm getForm() {
        return form;
    }

    public void setForm(DynamicForm form) {
        this.form = form;
    }

    public ModalWindow getModal() {
        return modal;
    }

    public void setModal(ModalWindow modal) {
        this.modal = modal;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }
}
