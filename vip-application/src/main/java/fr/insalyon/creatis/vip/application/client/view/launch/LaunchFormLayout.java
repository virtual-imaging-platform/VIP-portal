package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.*;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fr.insalyon.creatis.vip.core.client.view.CoreConstants.RESULTS_DIRECTORY_PARAM_NAME;

/**
 * Launch form automatically generated from a Boutiques descriptor.
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class LaunchFormLayout extends AbstractFormLayout {
    public static final String EXECUTION_NAME_ID = "Execution-name";
    // Application information
    protected String applicationName;
    protected String applicationVersion;
    protected String applicationClass;
    // Maps from input/group IDs to corresponding object
    private final Map<String, InputLayout> inputsMap = new HashMap<>();
    private final Map<String, GroupValidator> groups = new HashMap<>();
    // Buttons
    private final HLayout buttonsLayout;
    private  IButton launchButton = null;
    private  IButton saveInputsButton = null;
    private  IButton saveAsExampleButton = null;
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
     * Assertion that given expression is true, else an IllegalStateException with given message is thrown
     *
     * @param expression boolean: expression to verify, should evaluate to true in normal execution
     * @param message    String message of thrown exception when expression is false
     * @throws IllegalStateException if expression is false
     */
    public static void assertCondition (boolean expression, String message) throws IllegalStateException{
        if(!expression){
            throw new IllegalStateException(message);
        }
    }

    /**
     * @param text  String
     * @return      String with content from text enclosed by HTML bold tag
     */
    public static String bold(String text) {
        return "<b>" + text + "</b>";
    }

    /**
     * @param  memberNames List of Strings containing a group's member names
     * @return             String message describing the group as mutually exclusive
     */
    public static String mutuallyExclusiveMessage(List<String> memberNames) {
        return groupMessage(memberNames, names -> "At most one of " + names + " can be non-empty.");
    }

    /**
     * @param  memberNames List of Strings containing a group's member names
     * @return             String message describing the group as all-or-none
     */
    public static String allOrNoneMessage(List<String> memberNames) {
        return groupMessage(memberNames, names -> "Either all or none of " + names + " must be empty.");
    }

    /**
     * @param  memberNames List of Strings containing a group's member names
     * @return             String message describing the group as one-is-required
     */
    public static String oneIsRequiredMessage(List<String> memberNames) {
        return groupMessage(memberNames, names -> "At least one of " + names + " must be non-empty.");
    }

    /**
     * Generate a String description of a group from its members and a message formatter function
     *
     * @param memberNames       List of Strings containing names of group member inputs
     * @param messageFormatter  Function returning a group description as String from a String argument that lists
     *                          the group members
     * @return                  String description of the group
     */
    private static String groupMessage(List<String> memberNames, Function<String, String> messageFormatter){
        return messageFormatter.apply(bold(memberNames.toString()));
    }

    /**
     * Initialize all visual elements
     *
     * @param applicationDescriptor     BoutiquesApplication generated from application .json descriptor file
     */
    public LaunchFormLayout(final BoutiquesApplication applicationDescriptor) {
        super("600", "*");
        this.setWidth(600);
        // Documentation
        Label docLabel = WidgetUtil.getLabel("Documentation and Terms of Use",
                CoreConstants.ICON_INFORMATION, 30, Cursor.HAND);
        docLabel.addClickHandler(event -> new DocumentationLayout(event.getX(), event.getY(),
                applicationDescriptor.getDescription()).show());
        this.addMember(docLabel);
        // Buttons
        this.buttonsLayout = new HLayout(5);
        this.buttonsLayout.setAlign(VerticalAlignment.CENTER);
        this.buttonsLayout.setMargin(20);
        // Error message
        this.errorLabel = WidgetUtil.getLabel("", CoreConstants.ICON_WARNING, 30);
        this.errorLabel.setWidth(430);
        this.errorLabel.hide();
        // Warning message
        this.warningLabel = WidgetUtil.getLabel("", CoreConstants.ICON_HELP, 30);
        this.warningLabel.setWidth(430);
        this.warningLabel.hide();
        // verify there are extensions
        assertCondition(applicationDescriptor.getBoutiquesExtensions() != null,
                "The boutiques descriptor must have extensions");
        // Add inputs, then buttons and warning/error labels below
        this.configureInputs(applicationDescriptor);
        this.addMember(buttonsLayout);
        this.addMember(this.errorLabel);
        this.addMember(this.warningLabel);
        // Groups
        for(BoutiquesGroup group : applicationDescriptor.getGroups()){
            this.groups.put(group.getId(), new GroupValidator(group, this));
        }
        this.validateGroups();
        // Dependencies
        this.configureDependencies(applicationDescriptor);
    }

    public void hideInputs() {
        setInputsVisible(false);
    }

    public void showInputs() {
        setInputsVisible(true);
    }

    private void setInputsVisible(Boolean visible) {
        for (InputLayout inputLayout : inputsMap.values()) {
            if (inputLayout.getInputId().equals(EXECUTION_NAME_ID)) {
                continue;
            }
            inputLayout.setVisible(visible);
        }
    }

    public void hideInput(String inputId) {
        for (InputLayout inputLayout : inputsMap.values()) {
            if (inputLayout.getInputId().equals(inputId)) {
                inputLayout.setVisible(false);
            }
        }
    }

    public void makeInputUnmodifiable(String inputId) {
        for (InputLayout inputLayout : inputsMap.values()) {
            if (inputLayout.getInputId().equals(inputId)) {
                inputLayout.makeUnmodifiablePermanently();
            }
        }
    }

    public void hideErrorsAndWarningLabels() {
        this.errorLabel.hide();
        this.warningLabel.hide();
    }

    public void showErrorsAndWarningLabels() {
        this.errorLabel.show();
        this.warningLabel.show();
    }

    /**
     * Add given launch, save inputs and save as example buttons to this
     *
     * @param launchButton          IButton
     * @param saveInputsButton      IButton
     * @param saveAsExampleButton   IButton, or null
     */
    public void setButtons(IButton launchButton, IButton saveInputsButton, IButton saveAsExampleButton) {
        this.launchButton = launchButton;
        this.saveInputsButton = saveInputsButton;
        HLayout layout;
        if(saveAsExampleButton == null){
            layout = getButtonLayout(20, launchButton, saveInputsButton);
        } else {
            this.saveAsExampleButton = saveAsExampleButton;
            layout = getButtonLayout(20, launchButton, saveInputsButton, saveAsExampleButton);
        }
        this.setButtonsLayout(layout);
        this.updateErrorMessages();
    }

    /**
     * Add given buttons to this
     *
     *  @param margin   int margin before added buttons
     * @param buttons   IButton... to add to this
     */
    public void setButtons(int margin, IButton... buttons) {
        this.setButtonsLayout(getButtonLayout(margin, buttons));
    }

    protected HLayout getButtonLayout(int margin, IButton... buttons) {
        HLayout newButtonLayout = new HLayout(5);
        newButtonLayout.setAlign(VerticalAlignment.CENTER);
        newButtonLayout.setMargin(margin);

        for (IButton button : buttons) {
            newButtonLayout.addMember(button);
        }
        return newButtonLayout;
    }

    private void setButtonsLayout(Layout newButtonsLayout) {
        for (Canvas child : this.buttonsLayout.getChildren()) {
            this.buttonsLayout.removeChild(child);
        }
        this.buttonsLayout.addMember(newButtonsLayout);
    }


    public void configureCitation(String applicationName) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                fr.insalyon.creatis.vip.core.client.view.layout.Layout.getInstance().setWarningMessage("Unable to load citation:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                if (result != null && !result.isEmpty()) {
                    VLayout citationLayout = new VLayout(5);
                    citationLayout.addMember(WidgetUtil.getLabel("<b>Please refer to the following publication:</b>", 20));

                    Label citation = new Label(result);
                    citation.setWidth100();
                    citation.setAutoHeight();
                    citation.setCanSelectText(true);
                    citation.setPadding(5);
                    citation.setBackgroundColor("#FFFFFF");
                    citation.setBorder("1px solid #CCCCCC");
                    citationLayout.addMember(citation);

                    addMember(citationLayout);
                }
            }
        };
        ApplicationService.Util.getInstance().getCitation(applicationName, callback);
    }

    /**
     * Generate forms for results directory inputs as well as all inputs described in applicationDescriptor
     *
     * @param applicationDescriptor BoutiquesDescriptor generated from application .json descriptor file
     */
    private void configureInputs(BoutiquesApplication applicationDescriptor) {
        // Execution name and results directory inputs

        try {
            this.createArtificialStringInput("Execution name", EXECUTION_NAME_ID, false, null,
                    false, "[" + ApplicationConstants.EXEC_NAME_VALID_CHARS + "]");
            if(applicationDescriptor.getBoutiquesExtensions().getAddResultsDirectoryInput()) {
                this.createArtificialStringInput("Results directory", RESULTS_DIRECTORY_PARAM_NAME, true,
                        DataManagerConstants.ROOT + "/" + DataManagerConstants.USERS_HOME,
                        true, "[" + ApplicationConstants.INPUT_VALID_CHARS + "]");
            }
        } catch (InvalidBoutiquesDescriptorException exception) {
            // This should not happen as parameters provided to createArtificialStringInput should be valid.
            throw new RuntimeException("Could not create 'Execution name' and 'Results directory' input layouts.");
        }
        // Application descriptor inputs
        for (BoutiquesInput input : applicationDescriptor.getInputs()) {
            InputLayout inputLayout;
            if(input.getPossibleValues() != null){
                Map<String, String> labels =
                        applicationDescriptor.getBoutiquesExtensions().getValueChoicesLabelsForInput(input.getId());
                inputLayout = new ValueChoiceInputLayout(input, this, labels);
            } else {
                switch (input.getType()) {
                    case STRING:
                    case FILE:
                        String allowedChar = "[" + ApplicationConstants.INPUT_VALID_CHARS + "]";
                        inputLayout = new StringInputLayout((BoutiquesStringInput) input, this,
                                true, allowedChar);
                        break;
                    case NUMBER:
                        inputLayout = new NumberInputLayout((BoutiquesNumberInput) input, this);
                        break;
                    case FLAG:
                        inputLayout = new FlagInputLayout((BoutiquesFlagInput) input, this);
                        break;
                    default:
                        throw new RuntimeException("Unknown input type: " + input.getType());
                }
            }
            // handle extensions
            if (applicationDescriptor.getBoutiquesExtensions().getNonListInputs().contains(input.getId())) {
                inputLayout.disableAddingValue();
            }
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
     * @throws InvalidBoutiquesDescriptorException if provided properties are invalid
     */
    private void createArtificialStringInput(String name, String id, boolean isFile,
                                             String defaultValue, boolean hasAddValueButton, String allowedChar)
            throws InvalidBoutiquesDescriptorException {
        BoutiquesInput.InputType type = isFile ? BoutiquesInput.InputType.FILE : BoutiquesInput.InputType.STRING;
        BoutiquesStringInput input = new BoutiquesStringInput(id, name, null, type, false,
                null, null, null, null,null,
                defaultValue);
        InputLayout inputLayout = new StringInputLayout(input, this, hasAddValueButton, allowedChar);
        this.inputsMap.put(id, inputLayout);
        this.addMember(inputLayout);
        inputLayout.onValueChanged();
    }

    /**
     * Add inter-inputs dependencies.
     * Preconditions: this.inputsMap has already been populated with correct InputLayouts (using this.configureInputs)
     *                this.groups has already been populated with correct GroupValidators (by constructor)
     *
     * @param applicationDescriptor BoutiquesDescriptor generated from application .json descriptor file
     */
    private void configureDependencies(BoutiquesApplication applicationDescriptor) {
        // Disables-inputs
        applicationDescriptor.getDisablesInputsMap().forEach(this::addDisablesInputs);
        // Requires-inputs
        applicationDescriptor.getRequiresInputsMap().forEach(this::addRequiresInputs);
        // Value-disables
        applicationDescriptor.getValueDisablesInputsMap().forEach(this::addValueDisables);
        // Value-requires
        applicationDescriptor.getValueRequiresInputsMap().forEach(this::addValueRequires);
    }

    /**
     * Make input with ID masterID disable all optional inputs with IDs in disabledIds when it is not empty
     *  @param masterId      String representing disabling input ID
     * @param disabledIds    Set of String IDs of dependent inputs
     */
    private void addDisablesInputs(String masterId, Set<String> disabledIds) {
        assertCondition(this.inputsMap.containsKey(masterId),
                "Invalid master input ID: " + masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        for (String disabledInputId : disabledIds) {
            assertCondition(this.inputsMap.containsKey(disabledInputId),
                    "Invalid disabled input ID: " + disabledInputId);
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
     *  @param masterId     String representing dependent input ID
     * @param requiredIds   Set of String IDs of required inputs or groups of inputs
     */
    private void addRequiresInputs(String masterId, Set<String> requiredIds) {
        assertCondition(this.inputsMap.containsKey(masterId), "Invalid master input ID: " + masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        // Ignore dependency if master input is not optional (thus cannot be disabled)
        if(!masterInput.isOptional()) {
            return;
        }
        for (String requiredInputId : requiredIds) {
            if (this.inputsMap.containsKey(requiredInputId)) {
                InputLayout requiredInput = this.inputsMap.get(requiredInputId);
                masterInput.addRequires(requiredInput);
            } else {
                // A whole group is required
                LaunchFormLayout.assertCondition(this.groups.containsKey(requiredInputId),
                        "Invalid group ID: " + requiredInputId);
                for (InputLayout requiredMember : this.groups.get(requiredInputId).getMembers()) {
                    masterInput.addRequires(requiredMember);
                }
            }
        }
    }

    /**
     * Make input with ID masterId values disable certain inputs. valueDisablesIds maps values to IDs of inputs disabled
     * when these values are selected.
     *  @param masterId          String representing disabling input ID.
     * @param valueDisablesIds   Map from String input values to Sets of String IDs of inputs disabled by those values
     */
    private void addValueDisables(String masterId, Map<String, Set<String>> valueDisablesIds) {
        LaunchFormLayout.assertCondition(this.inputsMap.containsKey(masterId),
                "Invalid master input ID: " + masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        LaunchFormLayout.assertCondition(masterInput instanceof ValueChoiceInputLayout,
                "Invalid state: can't add value-disables to non value choice input: " + masterId);
        for (String iValue : valueDisablesIds.keySet()) {
            Set<InputLayout> disabledInputSet = new HashSet<>();
            for (String disabledInputId : valueDisablesIds.get(iValue)) {
                LaunchFormLayout.assertCondition(this.inputsMap.containsKey(disabledInputId),
                        "Invalid disabled input ID: " + disabledInputId);
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
     *  @param masterId          String representing dependent input ID.
     * @param valueRequiresIds   Map from input values as Strings to Set of String IDs of inputs required by those
     *                          values
     */
    private void addValueRequires(String masterId, Map<String, Set<String>> valueRequiresIds) {
        LaunchFormLayout.assertCondition(this.inputsMap.containsKey(masterId),
                "Invalid master input ID: " + masterId);
        InputLayout masterInput = this.inputsMap.get(masterId);
        LaunchFormLayout.assertCondition(masterInput instanceof ValueChoiceInputLayout,
                "Can't add value-requires to non value choice input: " + masterId);
        Map<String, Set<InputLayout>> valueRequiresLayouts = new HashMap<>();
        for (String iValue : valueRequiresIds.keySet()) {
            Set<InputLayout> requiredInputs = valueRequiresIds.get(iValue).stream()
                    .map(this.inputsMap::get)
                    .collect(Collectors.toSet());
            valueRequiresLayouts.put(iValue, requiredInputs);
        }
        ((ValueChoiceInputLayout) masterInput).addValueRequires(valueRequiresLayouts);
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
        List<String> memberNames = unsupportedGroup.getMemberNames();
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
    public void updateErrorMessages() {
        if ((this.invalidInputIds.size() + this.errorMessages.size()) == 0){
            // No errors: enable buttons and hide error messages label
            if((this.launchButton != null) && (this.saveInputsButton != null)) {
                this.launchButton.enable();
                this.launchButton.setPrompt("");
                this.saveInputsButton.enable();
                this.saveInputsButton.setPrompt("");
            }
            if(this.saveAsExampleButton != null) {
                this.saveAsExampleButton.enable();
                this.saveAsExampleButton.setPrompt("Save the inputs as a featured example that will "
                        + "be available for all users.");
            }
            this.errorLabel.hide();
        } else {
            // Errors: disable buttons and show error messages
            if((this.launchButton != null) && (this.saveInputsButton != null)) {
                this.launchButton.disable();
                this.launchButton.setPrompt("Cannot launch. Some inputs are not valid (see below).");
                this.saveInputsButton.disable();
                this.saveInputsButton.setPrompt("Cannot save Inputs. Some inputs are not valid (see below).");
            }
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
        this.inputsMap.forEach((inputId, inputLayout) -> inputValuesMap.put(inputId, inputLayout.getValueList()));
        return inputValuesMap;
    }

    /**
     * Returns a map of input IDs to corresponding input values represented as one String
     *
     * @return Map with String containing input IDs as keys and String representing input values as values
     */
    public Map<String, String> getParametersMap() {
        Map<String, String> parameterMap = new HashMap<>();
        this.getInputValueMap().forEach((inputId, valueSet) -> {
            if(!inputId.equals(EXECUTION_NAME_ID)) {
                String inputValue;
                if (valueSet instanceof ValueList) {
                    inputValue = String.join(ApplicationConstants.SEPARATOR_LIST, valueSet.getValuesAsStrings());
                } else {
                    List<Float> startStepStop = ((ValueRange) valueSet).getRangeLimits();
                    inputValue = startStepStop.get(0) + ApplicationConstants.SEPARATOR_INPUT
                            + startStepStop.get(2) + ApplicationConstants.SEPARATOR_INPUT
                            + startStepStop.get(1);
                }
                parameterMap.put(inputId, inputValue);
            }
        });
        return parameterMap;
    }

    /**
     * Load saved values after confirming with user if some non default values are to be overwritten
     *
     * @param valueMap Map of String representing input IDs to ValueSet representing corresponding values to load
     */
    private void loadValueMap(final Map<String, ValueSet> valueMap) {
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
                    if( ! ((uniqueValue == null) && (defaultValue == null)) ){
                        if((uniqueValue == null) || (defaultValue == null)){
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
                    .collect(Collectors.toSet());
            SC.confirm("The following fields already have a value.<br />"
                            + "Do you want to replace them?<br />"
                            + "Fields: " + overwrittenNames,
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
     * Load inputs from a parameters map
     *
     * @param simulationName Execution name to load
     * @param valuesMap      Map of String input IDs to String representation of corresponding input values
     */
    public void loadInputs( String simulationName, Map<String, String> valuesMap) {
        Map<String, ValueSet> valueSetMap = new HashMap<>();
        valueSetMap.put(EXECUTION_NAME_ID, ValueSet.valueSetFactory(simulationName));
        valuesMap.forEach((inputId, valueString) -> valueSetMap.put(inputId, ValueSet.valueSetFactory(valueString)));
        this.loadValueMap(valueSetMap);
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

    /**
     * @return boolean: true if all inputs and groups are valid, false otherwise
     */
    public boolean validate() {
        return (this.invalidInputIds.size() + this.errorMessages.size()) == 0;
    }
}
