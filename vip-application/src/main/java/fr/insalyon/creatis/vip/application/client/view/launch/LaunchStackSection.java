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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchStackSection;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import java.util.*;

/**
 *
 * @author Rafael Silva
 */
public class LaunchStackSection extends AbstractLaunchStackSection {

    private String tabID;
    private HLayout hLayout;
    private LaunchFormLayout launchFormLayout;
    private InputsLayout inputsLayout;

    public LaunchStackSection(String applicationName, String tabID,
            String simulationName, Map<String, String> inputs) {

        super(applicationName);

        this.tabID = tabID;

        hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();

        vLayout.addMember(hLayout);

        loadData(simulationName, inputs);
    }

    /**
     * Loads input values from string.
     *
     * @param values Input values
     */
    @Override
    public void loadInput(String name, String values) {

        Map<String, String> valuesMap = new HashMap<String, String>();

        for (String input : values.split("<br />")) {
            String[] s = input.split(" = ");
            valuesMap.put(s[0], s[1] != null ? s[1] : "");
        }

        launchFormLayout.loadInputs(name, valuesMap);
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

        launchFormLayout.setInputValue(inputName, value);
    }

    /**
     * Loads simulation sources list.
     */
    protected void loadData(final String simulationName, final Map<String, String> inputs) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Descriptor> callback = new AsyncCallback<Descriptor>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to download application source file:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Descriptor descriptor) {

                launchFormLayout = new LaunchFormLayout(applicationName, null, descriptor.getDescription());
                hLayout.addMember(launchFormLayout);

                for (Source source : descriptor.getSources()) {
                    launchFormLayout.addSource(new InputHLayout(source.getName(), source.getDescription()));
                }

                if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin()) {
                    launchFormLayout.addButtons(getLaunchButton(), getSaveInputsButton(),
                            getSaveAsExampleButton());
                } else {
                    launchFormLayout.addButtons(getLaunchButton(), getSaveInputsButton());
                }

                modal.hide();
                modal = launchFormLayout.getModal();

                inputsLayout = new InputsLayout(tabID);
                hLayout.addMember(inputsLayout);
                
                if (simulationName != null) {
                    launchFormLayout.loadInputs(simulationName, inputs);
                }
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

        return launchFormLayout.validate();
    }

    /**
     * Launches a simulation.
     */
    @Override
    protected void launch() {

        modal.show("Launching simulation '" + getSimulationName() + "'...", true);

        // Input data verification
        List<String> inputData = new ArrayList<String>();
        for (String input : getParametersMap().values()) {
            if (input.startsWith(DataManagerConstants.ROOT)) {
                if (input.contains(ApplicationConstants.SEPARATOR_LIST)) {
                    inputData.addAll(Arrays.asList(input.split(ApplicationConstants.SEPARATOR_LIST)));
                } else {
                    inputData.add(input);
                }
            }
        }
        if (!inputData.isEmpty()) {
            WorkflowServiceAsync service = WorkflowService.Util.getInstance();
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Error on input data:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    submit();
                }
            };
            service.validateInputs(inputData, callback);
        } else {
            submit();
        }
    }

    /**
     * Submits a simulation to the workflow engine.
     */
    private void submit() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to launch the simulation:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                SC.say("Simulation '" + getSimulationName() + "' successfully launched.");
            }
        };
        service.launchSimulation(getParametersMap(), applicationName,
                getSimulationName(), callback);
    }

    @Override
    protected Map<String, String> getParametersMap() {

        return launchFormLayout.getParametersMap();
    }

    @Override
    protected String getSimulationName() {

        return launchFormLayout.getSimulationName();
    }

    public void loadInputsList() {

        inputsLayout.loadData();
    }
}
