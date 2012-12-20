/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva, Tristan Glatard
 */
public class LaunchTab extends AbstractLaunchTab {

    private ArrayList<String> disabledSources;

    public LaunchTab(String applicationName, String applicationVersion) {
        this(applicationName, applicationVersion, null, null, null);
    }

    public LaunchTab(String applicationName, String applicationVersion,
            String simulationName, Map<String, String> inputs) {

        this(applicationName, applicationVersion, simulationName, inputs, null);
    }

    public LaunchTab(String applicationName, String applicationVersion,
            String simulationName, Map<String, String> inputs, String[] disabled) {

        super(applicationName, applicationVersion);
        layout.clear();
        disabledSources = new ArrayList<String>();
        if (disabled != null) {
            disabledSources.addAll(Arrays.asList(disabled));
        }
        loadData(simulationName, inputs);
    }

    public boolean hasID() {
        return this.getAttributeAsString("ID") != null;
    }

    /**
     * Loads simulation sources list.
     */
    private void loadData(final String simulationName, final Map<String, String> inputs) {

        final AsyncCallback<Descriptor> callback = new AsyncCallback<Descriptor>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download application source file:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Descriptor descriptor) {

                launchFormLayout = new LaunchFormLayout(applicationName + " " + applicationVersion, null, descriptor.getDescription());
                layout.addMember(launchFormLayout);

                for (Source source : descriptor.getSources()) {
                    boolean disabled = false;
                    for (String name : disabledSources) {
                        if (source.getName().equals(name)) {
                            disabled = true;
                        }
                    }
                    launchFormLayout.addSource(new InputHLayout(source.getName(), source.getDescription()), disabled);
                }

                configureLaunchButton();
                configureSaveInputsButton();

                if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin()) {
                    configureSaveAsExampleButton();
                    launchFormLayout.addButtons(launchButton, saveInputsButton,
                            saveAsExampleButton);
                } else {
                    launchFormLayout.addButtons(launchButton, saveInputsButton);
                }

                launchFormLayout.configureCitation(applicationName);

                modal.hide();

                configureInputsLayout(true);

                if (simulationName != null) {
                    launchFormLayout.loadInputs(simulationName, inputs);
                }
            }
        };
        modal.show("Loading launch panel...", true);
        WorkflowService.Util.getInstance().getApplicationDescriptor(applicationName, applicationVersion, callback);
    }

    /**
     * Launches a simulation.
     */
    @Override
    protected void launch() {

        WidgetUtil.setLoadingIButton(launchButton, "Launching...");

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
                    resetLaunchButton();
                    Layout.getInstance().setWarningMessage("Error on input data:<br />" + caught.getMessage(), 10);
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

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                resetLaunchButton();
                Layout.getInstance().setWarningMessage("Unable to launch the simulation:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Void result) {
                resetLaunchButton();
                Layout.getInstance().setNoticeMessage("Simulation '<b>" + getSimulationName() + "</b>' successfully launched.", 10);
                TimelineLayout.getInstance().update();
            }
        };
        WorkflowService.Util.getInstance().launchSimulation(getParametersMap(),
                applicationName, applicationVersion, getSimulationName(), callback);
    }
}
