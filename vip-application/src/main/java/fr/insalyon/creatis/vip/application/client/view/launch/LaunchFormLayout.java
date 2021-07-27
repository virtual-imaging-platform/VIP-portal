package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesDescriptor;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesGroup;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesInputString;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Launch form automatically generated from a Boutiques descriptor.
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class LaunchFormLayout extends AbstractFormLayout {
    public static final String EXECUTION_NAME_ID = "Execution-name";
    public static final String RESULTS_DIR_ID = "Results-directory";
    private final String SAVE_AS_EXAMPLE_PROMPT =
            "Save the inputs as a featured example that will be available for all users.";
    // Application information
    protected String applicationName;
    protected String applicationVersion;
    protected String applicationClass;
    // Maps from input/group IDs to corresponding object
    private final Map<String, InputLayout> inputsMap = new HashMap<>();
    private final Map<String, GroupValidator> groups = new HashMap<>();
    private final IButton launchButton;
    private final IButton saveInputsButton;
    private final IButton saveAsExampleButton;
    // Label warning user about unsupported dependencies (due to one of the inputs having multiple values)
    private final Label warningLabel;
    // Maps the problematic input ID to the set of dependencies that can't be supported because of it
    private final Map<String, Set<String>> unsupportedDependencies = new HashMap<>();
    // Label pointing out errors in the form
    private final Label errorLabel;
    // Names of the inputs with invalid values
    private final Set<String> invalidInputIds = new TreeSet<>();
    // Other error messages (unsatisfied groups)
    private final Set<String> errorMessages = new TreeSet<>();

    /**
     * @param text  String
     * @return      String with content from text enclosed by HTML bold tag
     */
    public static String bold(String text) {
        return "<b>" + text + "</b>";
    }

    /**
     * @param  memberNames String[] containing a group's member names
     * @return             String message describing the group as mutually exclusive
     */
    public static String mutuallyExclusiveMessage(String[] memberNames) {
        return groupMessage(memberNames, names -> "At most one of " + names + " can be non-empty.");
    }

    /**
     * @param  memberNames String[] containing a group's member names
     * @return             String message describing the group as all-or-none
     */
    public static String allOrNoneMessage(String[] memberNames) {
        return groupMessage(memberNames, names -> "Either all or none of " + names + " must be empty.");
    }

    /**
     * @param  memberNames String[] containing a group's member names
     * @return             String message describing the group as one-is-required
     */
    public static String oneIsRequiredMessage(String[] memberNames) {
        return groupMessage(memberNames, names -> "At least one of " + names + " must be non-empty.");
    }

    /**
     * Generate a String description of a group from its members and a message formatter function
     *
     * @param memberNames       String[] containing names of group member inputs
     * @param messageFormatter  Function returning a group description as String from a String argument that lists
     *                          the group members
     * @return                  String description of the group
     */
    private static String groupMessage(String[] memberNames, Function<String, String> messageFormatter){
        return messageFormatter.apply(bold(Arrays.toString(memberNames)));
    }

    /**
     * Initialize all visual elements
     *
     * @param applicationDescriptor BoutiquesDescriptor generated from application .json descriptor file
     */
    public LaunchFormLayout(final BoutiquesDescriptor applicationDescriptor, String applicationName,
                            String applicationVersion, String applicationClass) {
        super("600", "*");
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationClass = applicationClass;
        this.setWidth(600);
        // Documentation
        Label docLabel = WidgetUtil.getLabel("Documentation and Terms of Use",
                CoreConstants.ICON_INFORMATION, 30, Cursor.HAND);
        docLabel.addClickHandler(event -> new DocumentationLayout(event.getX(), event.getY(),
                applicationDescriptor.getDescription()).show());
        this.addMember(docLabel);
        // Buttons
        HLayout buttonsLayout = new HLayout();
        // Launch button
        this.launchButton = WidgetUtil.getIButton("Launch", ApplicationConstants.ICON_LAUNCH,
                event -> {
            if (event.getFiringCanvas().isDisabled()) {
                Layout.getInstance().setWarningMessage("Cannot launch. Some inputs are not valid.");
            } else {
                this.launchSimulation();
            }
        });
        buttonsLayout.addMember(this.launchButton);
        // Save inputs button
        this.saveInputsButton = WidgetUtil.getIButton("Save Inputs", CoreConstants.ICON_SAVED,
                event -> {
                    if (event.getFiringCanvas().isDisabled()) {
                        Layout.getInstance().setWarningMessage("Cannot save inputs. Some inputs are not valid.");
                    } else {
                        this.saveInputs();
                    }//this.saveInputs()
                });
        buttonsLayout.addMember(this.saveInputsButton);
        // Save as example button
        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin()) {
            saveAsExampleButton = WidgetUtil.getIButton("Save as Example", CoreConstants.ICON_EXAMPLE,
                    event -> {
                        if (!event.getFiringCanvas().isDisabled()) {
                            saveInputsAsExample();
                        }
                    });
            saveAsExampleButton.setPrompt(SAVE_AS_EXAMPLE_PROMPT);
            saveAsExampleButton.setWidth(120);
            buttonsLayout.addMember(this.saveAsExampleButton);
        } else {
            saveAsExampleButton = null;
        }
        buttonsLayout.setMembersMargin(50);
        buttonsLayout.setLayoutLeftMargin(50);
        // Error message
        this.errorLabel = WidgetUtil.getLabel("", CoreConstants.ICON_WARNING, 30);
        this.errorLabel.setWidth(430);
        this.errorLabel.hide();
        // Warning message
        this.warningLabel = WidgetUtil.getLabel("", CoreConstants.ICON_HELP, 30);
        this.warningLabel.setWidth(430);
        this.warningLabel.hide();
        // Add inputs, then buttons and warning/error labels below
        this.configureInputs(applicationDescriptor);
        this.addMember(buttonsLayout);
        this.addMember(this.errorLabel);
        this.addMember(this.warningLabel);
        // Groups
        GWT.log("Taking groups  into account");
        for(BoutiquesGroup group : applicationDescriptor.getGroups()){
            this.groups.put(group.getId(), new GroupValidator(group, this));
        }
        this.validateGroups();
        // Dependencies
        GWT.log("Add dependencies");
        this.configureDependencies(applicationDescriptor);
        GWT.log("Launch form layout created");
    }

    /**
     * Generate forms for results directory inputs as well as all inputs described in applicationDescriptor
     *
     * @param applicationDescriptor BoutiquesDescriptor generated from application .json descriptor file
     */
    private void configureInputs(BoutiquesDescriptor applicationDescriptor) {
        // Execution name input
        GWT.log("Creating execution name input");
        this.createArtificialStringInput("Execution name", EXECUTION_NAME_ID, false, null,
                          false, "[" + ApplicationConstants.EXEC_NAME_VALID_CHARS + "]");
        GWT.log("Creating results directory input");
        this.createArtificialStringInput("Results directory", RESULTS_DIR_ID, true,
                DataManagerConstants.ROOT + "/" + DataManagerConstants.USERS_HOME, true,
                null);
        // Application descriptor inputs
        GWT.log("Creating application inputs");
        for (BoutiquesInput input : applicationDescriptor.getInputs()) {
            GWT.log(input.getName());
            InputLayout inputLayout = input.getLayout(this);
            this.inputsMap.put(input.getId(), inputLayout);
            this.addMember(inputLayout);
            // Validate input value and dependencies
            inputLayout.onValueChanged();
        }
    }

    /**
     * Create and displays an artificial String input from its attributes (used for execution name and results
     * directory)
     *
     * @param name              String name of the input
     * @param id                String input ID
     * @param isFile            boolean: true if input is a File, false if it is a free String
     * @param defaultValue      input default value ("" for empty default)
     * @param hasAddValueButton boolean: true if input should have a "add value" button, false otherwise
     * @param allowedChar       String representing a regexp of allowed characters (ex: "[a-zA-z0-9]" for alphanumeric).
     *                          null with use default value "[" + ApplicationConstants.INPUT_VALID_CHARS + "]".
     */
    private void createArtificialStringInput(String name, String id, boolean isFile,
                                             String defaultValue, boolean hasAddValueButton, String allowedChar) {
        String type = isFile ? "File" : "String";
        // Generate an artificial input descriptor
        JSONObject descriptor = new JSONObject();
        descriptor.put("name", new JSONString(name));
        descriptor.put("id", new JSONString(id));
        descriptor.put("type", new JSONString(type));
        if(defaultValue != null) {
            descriptor.put("default-value", new JSONString(defaultValue));
        }
        // Create execution name input from the descriptor
        InputLayout artificialInput = new BoutiquesInputString(descriptor, type)
                                           .getLayout(this, hasAddValueButton, allowedChar);
        assert artificialInput instanceof StringInputLayout;
        this.inputsMap.put(id, artificialInput);
        this.addMember(artificialInput);
        artificialInput.onValueChanged();
    }

    /**
     * Add inter-inputs dependencies.
     * Preconditions: this.inputsMap has already been populated with correct InputLayouts (using this.configureInputs)
     *                this.groups has already been populated with correct GroupValidators (by constructor)
     *
     * @param applicationDescriptor BoutiquesDescriptor generated from application .json descriptor file
     */
    private void configureDependencies(BoutiquesDescriptor applicationDescriptor) {
        // Disables-inputs
        GWT.log("  Disables-inputs");
        applicationDescriptor.getDisablesInputsMap().forEach(this::addDisablesInputs);
        // Requires-inputs
        GWT.log("  Requires-inputs");
        applicationDescriptor.getRequiresInputsMap().forEach(this::addRequiresInputs);
        // Value-disables
        GWT.log("  Value-disables");
        applicationDescriptor.getValueDisablesInputsMap().forEach(this::addValueDisables);
        // Value-requires
        GWT.log("  Value-requires");
        applicationDescriptor.getValueRequiresInputsMap().forEach(this::addValueRequires);
    }

    /**
     * Make input with ID masterID disable all optional inputs with IDs in disabledIds when it is not empty
     *
     * @param masterId      String representing disabling input ID
     * @param disabledIds   String[] containing IDs of dependent inputs
     */
    private void addDisablesInputs(String masterId, String[] disabledIds) {
        assert this.inputsMap.containsKey(masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        for (String disabledInputId : disabledIds) {
            assert this.inputsMap.containsKey(disabledInputId);
            InputLayout disabledInput = this.inputsMap.get(disabledInputId);
            // Add dependency only if disabledInput can indeed be disabled, which means it is optional
            if(disabledInput.isOptional()) {
                masterInput.addDisables(disabledInput);
            }
        }
    }

    /**
     * Make input with ID masterID require all inputs and groups with IDs in requiredIds. This means it will be disabled
     * if one of them is left empty
     *
     * @param masterId      String representing dependent input ID
     * @param requiredIds   String[] containing IDs of required inputs or groups of inputs
     */
    private void addRequiresInputs(String masterId, String[] requiredIds) {
        assert this.inputsMap.containsKey(masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        // Ignore dependency if master input is not optional (thus cannot be disabled)
        if(masterInput.isOptional()) {
            for (String requiredInputId : requiredIds) {
                if (this.inputsMap.containsKey(requiredInputId)) {
                    InputLayout requiredInput = this.inputsMap.get(requiredInputId);
                    masterInput.addRequires(requiredInput);
                } else {
                    // A whole group is required
                    assert this.groups.containsKey(requiredInputId);
                    for (InputLayout requiredMember : this.groups.get(requiredInputId).getMembers()) {
                        masterInput.addRequires(requiredMember);
                    }
                }
            }
        }
    }

    /**
     * Make input with ID masterId values disable certain inputs. valueDisablesIds maps values to IDs of inputs disabled
     * when these values are selected.
     *
     * @param masterId          String representing disabling input ID.
     * @param valueDisablesIds  Map with String representation of disabling input values as keys, and String[]
     *                          containing disabled input IDs as values.
     */
    private void addValueDisables(String masterId, Map<String, String[]> valueDisablesIds) {
        assert this.inputsMap.containsKey(masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        assert masterInput instanceof ValueChoiceInputLayout;
        for (String iValue : valueDisablesIds.keySet()) {
            Set<InputLayout> disabledInputSet = new HashSet<>();
            for (String disabledInputId : valueDisablesIds.get(iValue)) {
                assert this.inputsMap.containsKey(disabledInputId);
                InputLayout disabledInput = this.inputsMap.get(disabledInputId);
                // Only optional inputs can be disabled
                if(disabledInput.isOptional()) {
                    disabledInputSet.add(disabledInput);
                }
            }
            ((ValueChoiceInputLayout) masterInput).addValueDisables(iValue, disabledInputSet);
        }
    }

    /**
     * Make input with ID masterId values require certain inputs. valueRequiresIds maps values to IDs of inputs that
     * cannot be empty when these values are selected.
     *
     * @param masterId          String representing dependent input ID.
     * @param valueRequiresIds  Map with String representation of dependent input values as keys, and String[]
     *                          containing required input IDs as values.
     */
    private void addValueRequires(String masterId, Map<String, String[]> valueRequiresIds) {
        assert this.inputsMap.containsKey(masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        assert masterInput instanceof ValueChoiceInputLayout;
        for (String iValue : valueRequiresIds.keySet()) {
            Set<InputLayout> requiredInputs = new HashSet<>();
            for (String requiredInputId : valueRequiresIds.get(iValue)) {
                assert this.inputsMap.containsKey(requiredInputId);
                requiredInputs.add(this.inputsMap.get(requiredInputId));
            }
            ((ValueChoiceInputLayout) masterInput).addValueRequires(iValue, requiredInputs);
        }
    }

    /**
     * Validate groups using corresponding GroupValidators validate method. This adds
     */
    public void validateGroups() {
        this.groups.values().forEach(GroupValidator::validate);
    }

    /**
     * Add dependencies involving masterInput to unsupported dependencies, usually because the input has
     * multiple values. A warning message will be displayed to inform user
     *
     * @param masterInput   InputLayout representing problematic input
     * @see #removeUnsupportedDependencies(String)
     * @see #updateWarningMessage()
     */
    public void addUnsupportedDependencies(InputLayout masterInput){
        final String masterName = bold(masterInput.getInputName());
        Set<String> newUnsupportedDependencies; // Warning messages to display
        // Requires-inputs
        newUnsupportedDependencies = this.formatDependencies(masterInput.getRequires(),
                inputName -> masterName + " requires " + inputName);
        newUnsupportedDependencies.addAll(this.formatDependencies(masterInput.getRequiredBy(),
                inputName -> inputName + " requires " + masterName));
        // Disables-inputs
        newUnsupportedDependencies.addAll(this.formatDependencies(masterInput.getDisables(),
                inputName -> masterName + " disables " + inputName));
        newUnsupportedDependencies.addAll(this.formatDependencies(masterInput.getDisabledBy(),
                inputName -> inputName + " disables " + masterName));
        // Value-disables
        newUnsupportedDependencies.addAll(this.formatDependencies(masterInput.getRequiredByValue(),
                (inputName, values) -> inputName + " values " + values + " require " + masterName));
        // Value-requires
        newUnsupportedDependencies.addAll(this.formatDependencies(masterInput.getDisabledByValue(),
                (inputName, values) -> inputName + " values " + values + " disable " + masterName));
        if(masterInput instanceof ValueChoiceInputLayout){
            ValueChoiceInputLayout masterChoiceInput = (ValueChoiceInputLayout) masterInput;
            // Value-disables
            newUnsupportedDependencies.addAll(this.formatDependencies(masterChoiceInput.getValueDisables(),
                    (inputName, values) -> masterName + " values " + values + " disable " + inputName));
            // Value-requires
            newUnsupportedDependencies.addAll(this.formatDependencies(masterChoiceInput.getValueRequires(),
                    (inputName, values) -> masterName + " values " + values + " require " + inputName));
        }
        // Update warning message
        if(newUnsupportedDependencies.size() > 0){
            this.unsupportedDependencies.put(masterInput.getInputId(), newUnsupportedDependencies);
        }
        this.updateWarningMessage();
    }

    /**
     * Generate a set of dependency messages from a set of InputLayouts involved in the dependencies. A formatter
     * Function generates one dependency message given involved input's name
     *
     * @param inputSet  Set of InputLayout involved in dependencies to describe
     * @param formatter Function taking a String containing an input name as argument and returning a String
     *                  representing the dependency its involved in
     * @return          Set of Strings containing dependency messages
     * @see #addUnsupportedDependencies(InputLayout)
     */
    private Set<String> formatDependencies(Set<InputLayout> inputSet,
                                           Function<String, String> formatter) {
        return inputSet.stream()
                .map(input -> bold(input.getInputName()))
                .map(formatter)
                .collect(Collectors.toSet());
    }

    /**
     * Generate a set of dependency messages from a Map containing InputLayouts and input values involved in the
     * dependencies. A formatter BiFunction generates one dependency message given involved input's name and
     * involved Set of values.
     *
     * @param valueChoices  Map containing InputLayouts as keys and Sets of Strings as values. These respectively
     *                      represent inputs and input values involved together in a dependency
     * @param formatter     BiFunction taking two Strings as arguments and returning a dependency message as String.
     *                      Arguments are respectively involved input's name and the list of involved input values
     * @return              Set of Strings containing dependency messages
     * @see #addUnsupportedDependencies(InputLayout)
     */
    private Set<String> formatDependencies(Map<InputLayout, Set<String>> valueChoices,
                                           BiFunction<String, String, String> formatter) {
        return valueChoices.entrySet().stream()
                .map(entry -> formatter.apply(bold(entry.getKey().getInputName()), entry.getValue().toString()))
                .collect(Collectors.toSet());
    }

    /**
     * Remove dependencies involving input with ID masterId from unsupported dependencies, then update corresponding
     * warning message
     *
     * @param masterId  String containing involved input's ID
     * @see #addUnsupportedDependencies(InputLayout)
     * @see #updateWarningMessage()
     */
    public void removeUnsupportedDependencies(String masterId){
        this.unsupportedDependencies.remove(masterId);
        this.updateWarningMessage();
    }

    /**
     * Add dependencies involving unsupportedGroup to unsupported dependencies, usually because the group input has
     * members with multiple values. A warning message will be displayed to inform user
     *
     * @param unsupportedGroup  GroupValidator representing problematic group
     * @see #addUnsupportedDependencies(InputLayout)
     * @see #updateWarningMessage()
     */
    public void addUnsupportedGroup(GroupValidator unsupportedGroup) {
        HashSet<String> newUnsupportedDependencies = new HashSet<>();
        String[] memberNames = unsupportedGroup.getMemberNames();
        if(unsupportedGroup.isMutuallyExclusive()){
            newUnsupportedDependencies.add(mutuallyExclusiveMessage(memberNames));
        }
        if(unsupportedGroup.isAllOrNone()){
            newUnsupportedDependencies.add(allOrNoneMessage(memberNames));
        }
        if(unsupportedGroup.isOneIsRequired()){
            newUnsupportedDependencies.add(oneIsRequiredMessage(memberNames));
        }
        if(newUnsupportedDependencies.size() > 0){
            this.unsupportedDependencies.put(unsupportedGroup.getGroupId(), newUnsupportedDependencies);
            newUnsupportedDependencies.forEach(message -> this.groupErrorMessage(message, false));
        }
        this.updateWarningMessage();
    }

    /**
     * Remove dependencies involving group with ID nowSupportedGroup from unsupported dependencies, then update
     * corresponding warning message
     *
     * @param nowSupportedGroup  GroupValidator representing the group
     * @see #addUnsupportedGroup(GroupValidator) 
     * @see #removeUnsupportedDependencies(String)
     * @see #updateWarningMessage()
     */
    public void removeUnsupportedGroup(GroupValidator nowSupportedGroup) {
        this.unsupportedDependencies.remove(nowSupportedGroup.getGroupId());
        this.updateWarningMessage();
    }

    /**
     * Update this.warningLabel to show current user warnings. Hide it if there is no warning to show
     */
    private void updateWarningMessage(){
        Set<String> warningDependencies = new TreeSet<>();
        this.unsupportedDependencies.forEach((keyId, warnings) -> warningDependencies.addAll(warnings));
        if(warningDependencies.size() == 0){
            this.warningLabel.hide();
        } else {
            String message = warningDependencies.stream()
                    .map(dependency -> "<li> " + dependency + "</li>")
                    .collect(Collectors.joining("", "<font color=\"orange\">Following input "
                            + "dependencies can't be checked because some inputs have multiple values. Please ensure "
                            + "they are met to avoid execution errors: <ul>", "</ul>"));
            this.warningLabel.setContents(message);
            this.warningLabel.show();
        }
    }

    /**
     * Add an InputLayout input name to the list of inputs with invalid value, then update user error messages
     *
     * @param input InputLayout representing problematic input
     * @see #removeValidationErrorOnInput(InputLayout)
     * @see #updateErrorMessages()
     * @see #addUnsupportedDependencies(InputLayout)
     */
    public void addValidationErrorOnInput(InputLayout input) {
        this.invalidInputIds.add(input.getInputId());
        this.updateErrorMessages();
    }

    /**
     * Remove an InputLayout input name from the list of inputs with invalid value, then update user error messages
     *
     * @param input InputLayout representing concerned input
     * @see #addValidationErrorOnInput(InputLayout)
     * @see #updateErrorMessages()
     * @see #removeUnsupportedDependencies(String)
     */
    public void removeValidationErrorOnInput(InputLayout input) {
        this.invalidInputIds.remove(input.getInputId());
        this.updateErrorMessages();
    }
    
    /**
     * Add or remove a group from unsatisfied group error messages, then update visible error messages
     *
     * @param message   String containing the group error message to add or remove
     * @param addError  boolean: true if the error message should be added, false if it should be removed
     * @see #addValidationErrorOnInput(InputLayout)
     * @see #removeValidationErrorOnInput(InputLayout)
     * @see #updateErrorMessages()
     */
    public void groupErrorMessage(String message, boolean addError) {
        if(addError) {
            this.errorMessages.add(message);
        } else {
            this.errorMessages.remove(message);
        }
        this.updateErrorMessages();
    }

    /**
     * Update displayed user error messages to match field invalidInputNames and errorMessages. If there is no error
     * to display, also enable launch simulation, save inputs and save as example buttons, else disable them
     * 
     * @see #addValidationErrorOnInput(InputLayout)
     * @see #removeValidationErrorOnInput(InputLayout)
     * @see #groupErrorMessage(String, boolean) 
     */
    private void updateErrorMessages() {
        if ((this.invalidInputIds.size() + this.errorMessages.size()) == 0){
            // No errors: enable buttons and hide error messages label
            this.launchButton.enable();
            this.launchButton.setPrompt("");
            this.saveInputsButton.enable();
            this.saveInputsButton.setPrompt("");
            if(this.saveAsExampleButton != null) {
                this.saveAsExampleButton.enable();
                this.saveAsExampleButton.setPrompt(SAVE_AS_EXAMPLE_PROMPT);
            }
            this.errorLabel.hide();
        } else {
            // Errors: disable buttons and show error messages
            this.launchButton.disable();
            this.launchButton.setPrompt("Cannot launch. Some inputs are not valid (see below).");
            this.saveInputsButton.disable();
            this.saveInputsButton.setPrompt("Cannot save Inputs. Some inputs are not valid (see below).");
            if(this.saveAsExampleButton != null) {
                this.saveAsExampleButton.disable();
                this.saveAsExampleButton.setPrompt("Cannot save Inputs. Some inputs are not valid (see below).");
            }
            this.errorLabel.show();
            // Error messages
            StringBuilder errorContent = new StringBuilder("<font color=\"red\"><ul>");
            if(this.invalidInputIds.size() > 0) {
                Set<String> invalidInputNames = this.invalidInputIds.stream()
                        .map(id -> this.inputsMap.get(id).getInputName())
                        .collect(Collectors.toSet());
                errorContent.append("<li>Following inputs are invalid: <b>")
                        .append(invalidInputNames)
                        .append("</b>.</li>");
            }
            this.errorMessages.forEach(iMessage -> errorContent.append("<li>").append(iMessage).append("</li>"));
            this.errorLabel.setContents(errorContent + "</ul>");
        }
    }

    /**
     * Returns a map of input IDs to corresponding input values represented as ValueSets
     *
     * @return Map with String containing input IDs as keys and ValuesSets representing input values as values
     */
    private Map<String, ValueSet> getInputValueMap() {
        Map<String, ValueSet> inputValuesMap = new HashMap<>();
        this.inputsMap.forEach((inputId, values) -> inputValuesMap.put(inputId, values.getValueList()));
        return inputValuesMap;
    }

    /**
     * Returns a map of input IDs to corresponding input values represented as one String
     *
     * @return Map with String containing input IDs as keys and String representing input values as values
     */
    private Map<String, String> getParametersMap() {
        Map<String, String> parameterMap = new HashMap<>();
        this.getInputValueMap().forEach((inputId, valueSet) -> {
            if((!inputId.equals(EXECUTION_NAME_ID)) & (!inputId.equals(RESULTS_DIR_ID))) {
                String inputValue;
                if (valueSet instanceof ValueList) {
                    inputValue = String.join(ApplicationConstants.SEPARATOR_LIST, valueSet.getValuesAsStrings());
                } else {
                    Float[] startStepStop = ((ValueRange) valueSet).getRangeLimits();
                    inputValue = startStepStop[0] + ApplicationConstants.SEPARATOR_INPUT
                            + startStepStop[2] + ApplicationConstants.SEPARATOR_INPUT
                            + startStepStop[1];
                }
                parameterMap.put(inputId, inputValue);
            }
        });
        return parameterMap;
    }

    /**
     * Save current input values as example
     */
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
                Layout.getInstance().setNoticeMessage("Examples input values were succesfully saved!", 10);
            }
        };
        service.saveInputsAsExamples(getSimulationInput(), callback);
    }

    /**
     * Create a button to later re-load current set of input values
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
                        + "already exists. <br />Do you want to ovewrite the input data?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            saveInputs(true);
                        } else {
                            resetSaveInputsButton();
                        }
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

    /**
     * @return SimulationInput corresponding to current input values
     */
    private SimulationInput getSimulationInput() {
        StringBuilder sb = new StringBuilder();
        this.getParametersMap().forEach(
                (inputId, inputValue) -> sb.append(inputId)
                        .append("=")
                        .append(inputValue.replaceAll(ApplicationConstants.INPUT_WITHOUT_VALUE, ""))
                        .append("--"));
        return new SimulationInput(applicationName, getSimulationName(), sb.toString());
    }


    /**
     * Load saved values after confirming with user if some non default values are to be overwritten
     *
     * @param valueMap Map of String representing input IDs to ValueSet representing corresponding values to load
     */
    private void loadInputs(final Map<String, ValueSet> valueMap) {
        // Check if some inputs have non default values that will be overwritten with different values,
        // in order to alert the user
        Set<String> overwrittenInputs = new TreeSet<>();
        for(String inputId : valueMap.keySet()) {
            InputLayout input = this.inputsMap.get(inputId);
            boolean overwriteInput = false;
            ValueSet currentValueList = input.getValueList();
            ValueSet loadedValueList = valueMap.get(inputId);
            if (!currentValueList.isEqualTo(loadedValueList)) {
                if (!input.hasUniqueValue()) {
                    overwriteInput = true;
                } else {
                    // Overwrite only if current value is different from default value.
                    Object uniqueValue = currentValueList.getValueNo(0);
                    Object defaultValue = input.getDefaultValue();
                    if(!((uniqueValue == null) & (defaultValue == null))){
                        if((uniqueValue == null) | (defaultValue == null)){
                            // Current value or default value is null while the other is not
                            overwriteInput = true;
                        } else {
                            // String comparison in case defaultValue is not the same type as uniqueValue.
                            // For instance if descriptor has default value "1" for an input of type "Number", we want
                            // the Float value 1 to be considered equal to default.
                            if(!defaultValue.toString().equals(uniqueValue.toString())){
                                overwriteInput = true;
                            }
                        }
                    }
                }
            }
            if (overwriteInput) {
                overwrittenInputs.add(input.getInputId());
            }
        }
        // If some values are to be overwritten, ask user to confirm
        if (overwrittenInputs.size() == 0) {
            this.overwriteValues(valueMap);
        } else {
            Set<String> overwrittenNames =  overwrittenInputs.stream()
                    .map(id -> this.inputsMap.get(id).getInputName())
                    .collect(Collectors.toSet());;
            SC.confirm("Inputs " + overwrittenNames + " already have values. Do you want to overwrite them ?",
                    confirmed -> {
                        if(confirmed){
                            this.overwriteValues(valueMap);
                        }
                    });
        }
    }

    /**
     * Overwrite current in put values with provided ones
     *
     * @param valueMap  Map of String representing input IDs to ValueSet representing corresponding values to write
     */
    private void overwriteValues(Map<String, ValueSet> valueMap) {
        valueMap.forEach((inputId, values) -> this.inputsMap.get(inputId).overwriteValues(values));
    }

    /**
     * Check if file inputs are valid, then launch simulation
     */
    private void launchSimulation() {
        WidgetUtil.setLoadingIButton(this.launchButton, "Launching...");
        // Input data verification
        Map<String, ValueSet> inputValueMap = this.getInputValueMap();
        List<String> inputData = new ArrayList<>(); // Will contain all input file paths
        this.inputsMap.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof StringInputLayout)
                .filter(entry -> ((StringInputLayout) entry.getValue()).isFile()) // All file inputs
                .map(entry -> inputValueMap.get(entry.getKey()).getValuesAsStrings()) // Values list for each file input
                .forEach(valueList -> {
                    for(String value : valueList) {
                        if (value.startsWith(DataManagerConstants.ROOT)) {
                            inputData.add(value);
                        }
                    }
                });
        if (inputData.isEmpty()) {
            submit();
        } else {
            WorkflowServiceAsync service = WorkflowService.Util.getInstance();
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    resetLaunchButton();
                    Layout.getInstance().setWarningMessage("Error on input data:<br />" + caught.getMessage(),
                                                      10);
                }

                @Override
                public void onSuccess(Void result) {
                    submit();
                }
            };
            service.validateInputs(inputData, callback);
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
                Layout.getInstance().setWarningMessage("Unable to launch the execution:<br />"
                                                       + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Void result) {
                resetLaunchButton();
                Layout.getInstance().setNoticeMessage("Execution '<b>" + getSimulationName()
                                                      + "</b>' successfully launched.", 10);
                TimelineLayout.getInstance().update();
            }
        };

        WorkflowService.Util.getInstance().launchSimulation(this.getParametersMap(),
                applicationName, applicationVersion, applicationClass,
                getSimulationName(), callback);
    }

    /**
     * Reset launch button (used to exit the "loading" display mode after simulation is launched or launch failed)
     */
    private void resetLaunchButton(){
        WidgetUtil.resetIButton(launchButton, "Launch", ApplicationConstants.ICON_LAUNCH);
        updateErrorMessages();
    }

    protected void  resetSaveAsExampleButton(){
        if(this.saveAsExampleButton != null) {
            WidgetUtil.resetIButton(this.saveAsExampleButton, "Save as Example", CoreConstants.ICON_EXAMPLE);
            updateErrorMessages();
        }
    }
    /**
     * Resets the save inputs button to its initial state.
     */
    protected void resetSaveInputsButton() {
        WidgetUtil.resetIButton(saveInputsButton, "Save Inputs", CoreConstants.ICON_SAVED);
        updateErrorMessages();
    }

    /**
     * @return Map of String representing IDs of inputs contained in this form to corresponding InputLayouts
     */
    public Map<String, InputLayout> getInputsMap() {
        return this.inputsMap;
    }

    /**
     * @return Simulation name
     */
    public String getSimulationName() {
        return this.getInputValueMap().get(EXECUTION_NAME_ID).getStringValueNo(0);
    }
}
