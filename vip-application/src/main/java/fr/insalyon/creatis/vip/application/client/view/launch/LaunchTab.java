package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplicationExtensions;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesParser;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

import java.util.*;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class LaunchTab extends Tab {

    protected String applicationName;
    protected String applicationVersion;
    protected String applicationClass;
    protected String simulationName;
    protected Map<String, String> inputs;

    protected Boolean showExamples = true;
    protected boolean showSeparators = true;

    protected HLayout layout;
    protected LaunchFormLayout launchFormLayout;
    protected InputsLayout inputsLayout;
    protected ModalWindow modal;
    protected IButton launchButton;
    protected IButton saveInputsButton;
    protected IButton saveAsExampleButton;


    public LaunchTab(String applicationName, String applicationVersion, String applicationClass) {
        this(applicationName, applicationVersion, applicationClass, null, null);
    }

    public LaunchTab(
            String applicationName, String applicationVersion, String applicationClass,
            String simulationName, Map<String, String> inputs) {

        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationClass = applicationClass;
        this.simulationName = simulationName;
        this.inputs = inputs;

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

        this.configureButtons();
        this.setPane(layout);

        this.init();
        this.loadDescriptor();
    }

    public boolean hasID() {
        return this.getAttributeAsString("ID") != null;
    }

    protected void init() {}

    protected void loadDescriptor() {
        modal.show("Loading launch panel...", true);
        ApplicationService.Util.getInstance().getVersion(applicationName, applicationVersion, new AsyncCallback<AppVersion>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download application info:<br />"
                        + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(AppVersion appVersion) {
                loadBoutiquesDescriptor();
            }
        });
    }

    /**
     * Loads simulation descriptor content as String.
     */
    protected void loadBoutiquesDescriptor() {
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download application descriptor:<br />"
                        + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(String boutiquesDescriptorString) {
                try {
                    BoutiquesApplication applicationTool = new BoutiquesParser().parseApplication(boutiquesDescriptorString);
                    Map<String, String> overriddenInputs = applicationTool.getVipOverriddenInputs();
                    // when we have both predefined inputs (i.e. on relaunch) and overriddenInputs,
                    // remove overriddenInputs from the inputs map, so that it matches the form content
                    if (inputs != null && overriddenInputs != null) {
                        for (String key: overriddenInputs.keySet()) {
                            inputs.remove(key);
                        }
                    }
                    addExtensionAndCreateForm(applicationTool, true, () -> createForm(applicationTool));
                } catch (InvalidBoutiquesDescriptorException exception) {
                    Layout.getInstance().setWarningMessage("Unable to parse application descriptor:<br />"
                            + exception.getMessage(), 10);
                }
            }
        };
        WorkflowService.Util.getInstance().getApplicationDescriptorString(applicationName, applicationVersion,
                callback);
    }

    protected void addExtensionAndCreateForm(
            BoutiquesApplication applicationTool, Boolean addResultsDirectoryInput, Runnable launchFormCreator) {
        applicationTool.setBoutiquesExtensions(new BoutiquesApplicationExtensions(addResultsDirectoryInput));
        launchFormCreator.run();
    }

    protected void createForm(BoutiquesApplication applicationTool) {
        launchFormLayout = new LaunchFormLayout(applicationTool, showSeparators);
        this.createButtons();
        onLaunchFormCreated();
        layout.addMember(launchFormLayout);
        launchFormLayout.configureCitation(applicationName);
        modal.hide();
        configureInputsLayout(this.showExamples);
        onLaunchFormReady();
    }

    protected void createButtons() {
        if (this.showExamples &&
                (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin()) ) {
            launchFormLayout.setButtons(launchButton, saveInputsButton,
                    saveAsExampleButton);
        } else {
            launchFormLayout.setButtons(launchButton, saveInputsButton, null);
        }
    }

    protected void onLaunchFormCreated() {}

    protected void onLaunchFormReady() {
        if ((simulationName != null) && (inputs != null)) {
            launchFormLayout.loadInputs(simulationName, inputs, true);
        }
    }

    /**
     * Launches a simulation.
     */
    private void launch() {
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
        launchFormLayout.loadInputs(simulationName, values, true);
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

        return launchFormLayout.getParametersMap();
    }

    /**
     * Configures the buttons
     */
    protected void configureButtons() {

        launchButton = WidgetUtil.getIButton("Launch", ApplicationConstants.ICON_LAUNCH,
                event -> {
                    if (validate()) {
                        launch();
                    } else {
                        Layout.getInstance().setWarningMessage("Cannot launch. Some inputs are not valid.");
                    }
                });

        saveInputsButton = WidgetUtil.getIButton("Save Inputs", CoreConstants.ICON_SAVED,
                event -> {
                    if (validate()) {
                        saveInputs();
                    } else {
                        Layout.getInstance().setWarningMessage("Cannot save inputs. Some inputs are not valid.");
                    }
                });

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
     * Resets the launch button to its initial state.
     */
    protected void resetLaunchButton() {
        WidgetUtil.resetIButton(launchButton, "Launch", ApplicationConstants.ICON_LAUNCH);
        this.launchFormLayout.updateErrorMessages();
    }

    /**
     * Resets the save inputs button to its initial state.
     */
    protected void resetSaveInputsButton() {

        WidgetUtil.resetIButton(saveInputsButton, "Save Inputs", CoreConstants.ICON_SAVED);
        this.launchFormLayout.updateErrorMessages();
    }

    /**
     * Resets the save as example button to its initial state.
     */
    protected void resetSaveAsExampleButton() {
        WidgetUtil.resetIButton(saveAsExampleButton, "Save as Example", CoreConstants.ICON_EXAMPLE);
        this.launchFormLayout.updateErrorMessages();
    }

    /**
     * Gets the name of the simulation.
     *
     * @return String
     */
    protected String getSimulationName() {
        return launchFormLayout.getSimulationName();
    }

    /**
     * Validates the form before launch a simulation.
     *
     * @return Result of the validation
     */
    protected boolean validate() {
        return launchFormLayout.validate();
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
                resetSaveInputsButton();
                Layout.getInstance().setWarningMessage("Unable to verify execution name:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(SimulationInput result) {
                if (result != null) {
                    SC.ask("A simulation entitled \"" + getSimulationName() + "\" "
                            + "already exists. <br />Do you want to overwrite the input data?", value -> {
                        if (value) {
                            saveInputs(true);
                        } else {
                            resetSaveInputsButton();
                        }
                    });
                } else{
                    saveInputs(false);
                }
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
                LaunchTab launchTab = (LaunchTab) Layout.getInstance().
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
                LaunchTab launchTab = (LaunchTab) Layout.getInstance().
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
}
