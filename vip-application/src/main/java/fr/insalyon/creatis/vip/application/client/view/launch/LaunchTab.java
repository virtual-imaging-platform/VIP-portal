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
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesParser;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
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

    protected LaunchFormLayout launchFormLayout;
    
    public LaunchTab(String applicationName, String applicationVersion, String applicationClass) {
        this(applicationName, applicationVersion, applicationClass, null, null);
    }

    public LaunchTab(String applicationName, String applicationVersion,
            String applicationClass, String simulationName, Map<String, String> inputs) {

        super(applicationName, applicationVersion, applicationClass);
        layout.clear();
        loadDescriptor(simulationName, inputs);
    }

    public boolean hasID() {
        return this.getAttributeAsString("ID") != null;
    }

    /**
     * Loads simulation descriptor content as String.
     */
    private void loadDescriptor(String simulationName,
                                Map<String, String> inputs) {
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download application descriptor:<br />"
                                                       + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(String boutiquesDescriptorString) {
                BoutiquesApplication applicationTool =
                        null;
                try {
                    applicationTool = new BoutiquesParser().parseApplication(boutiquesDescriptorString);
                } catch (InvalidBoutiquesDescriptorException exception) {
                    Layout.getInstance().setWarningMessage("Unable to parse application descriptor:<br />"
                            + exception.getMessage(), 10);
                }
                launchFormLayout = new LaunchFormLayout(applicationTool);
                layout.addMember(launchFormLayout);
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
                if ((simulationName != null) && (inputs != null)) {
                    launchFormLayout.loadInputs(simulationName, inputs);
                }
            }
        };
        modal.show("Loading launch panel...", true);
        WorkflowService.Util.getInstance().getApplicationDescriptorString(applicationName, applicationVersion,
                                                                          callback);
    }

    /**
     * Launches a simulation.
     */
    @Override
    protected void launch() {
        WidgetUtil.setLoadingIButton(launchButton, "Launching...");

        // Input data verification
        List<String> inputData = new ArrayList<>();
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
     * Resets the launch button to its initial state.
     */
    protected void resetLaunchButton() {
        super.resetLaunchButton();
        this.launchFormLayout.updateErrorMessages();
    }

    /**
     * Resets the save inputs button to its initial state.
     */
    protected void resetSaveInputsButton() {
        super.resetSaveInputsButton();
        this.launchFormLayout.updateErrorMessages();
    }

    /**
     * Resets the save as example button to its initial state.
     */
    protected void resetSaveAsExampleButton() {
        super.resetSaveAsExampleButton();
        this.launchFormLayout.updateErrorMessages();
    }

    /**
     * @return LaunchFormLayout representing this tab's launch form
     */
    @Override
    protected LaunchFormLayout getLaunchFormLayout() {
        return this.launchFormLayout;
    }

    /**
     * Submits a simulation to the workflow engine.
     */
    private void submit() {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                resetLaunchButton();
                Layout.getInstance().setWarningMessage("Unable to launch the execution:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Void result) {
                resetLaunchButton();
                Layout.getInstance().setNoticeMessage("Execution '<b>" + getSimulationName() + "</b>' successfully launched.", 10);
                TimelineLayout.getInstance().update();
            }
        };
        WorkflowService.Util.getInstance().launchSimulation(getParametersMap(),
                applicationName, applicationVersion, applicationClass, 
                getSimulationName(), callback);
    }
}
