package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesDescriptor {

    private final String name;
    private final String description;
    private final String version;
    private final BoutiquesInput[] inputs;
    // Input dependencies
    private final BoutiquesGroup[] groups;
    private final Map<String, String[]> disablesInputsMap= new HashMap<>();
    private final Map<String, String[]> requiresInputsMap= new HashMap<>();
    private final Map<String, Map<String, String[]>> valueDisablesInputsMap= new HashMap<>();
    private final Map<String, Map<String, String[]>> valueRequiresInputsMap= new HashMap<>();

    /**
     * Parse JSON Boutiques descriptor
     *
     * @param descriptor        String representing application JSON descriptor
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesDescriptor(String descriptor) throws RuntimeException{
        JSONObject parsedDescriptor = JSONParser.parseStrict(descriptor).isObject();
        if (parsedDescriptor == null) {
            throw new RuntimeException("Invalid Boutiques descriptor: not a JSON object.");
        }
        this.name = BoutiquesUtil.getStringValue(parsedDescriptor, "name");
        this.description = BoutiquesUtil.getStringValue(parsedDescriptor, "description");
        this.version = BoutiquesUtil.getStringValue(parsedDescriptor, "tool-version");
        JSONArray inputsArray = BoutiquesUtil.getArrayValue(parsedDescriptor, "inputs");
        this.inputs = new BoutiquesInput[inputsArray.size()];
        for(int inputNo = 0; inputNo < inputsArray.size(); inputNo++){
            JSONObject currentInputDescriptor = inputsArray.get(inputNo).isObject();
            if (currentInputDescriptor == null){
                throw new RuntimeException("Invalid Boutiques descriptor: input " + inputNo + " is not a JSON object");
            }
            String inputType = BoutiquesUtil.getStringValue(currentInputDescriptor, "type");
            BoutiquesInput parsedInput;
            switch(inputType) {
                case "String":
                case "File":
                    parsedInput = new BoutiquesInputString(currentInputDescriptor, inputType);
                    break;
                case "Number":
                    parsedInput = new BoutiquesInputNumber(currentInputDescriptor);
                    break;
                case "Flag":
                    parsedInput = new BoutiquesInputFlag(currentInputDescriptor);
                    break;
                default:
                    throw new RuntimeException("Invalid Boutiques descriptor: invalid type for input " + inputNo
                            + ": '" + inputType + "'. Only allowed types are 'String', 'File', 'Number' and 'Flag'.");
            }
            this.inputs[inputNo] = parsedInput;
            // Dependencies
            String inputId = parsedInput.getId();
            // disables-inputs
            String[] disableInputsIds = parsedInput.getDisablesInputsId();
            if(disableInputsIds != null){
                this.disablesInputsMap.put(inputId, disableInputsIds);
            }
            // requires-inputs
            String[] requireInputsIds = parsedInput.getRequiresInputsId();
            if(requireInputsIds != null){
                this.requiresInputsMap.put(inputId, requireInputsIds);
            }
            // value-disables
            Map<String, String[]> valueDisablesMap = parsedInput.getValueDisablesInputsId();
            if(valueDisablesMap != null){
                this.valueDisablesInputsMap.put(inputId, valueDisablesMap);
            }
            // value-requires
            Map<String, String[]> valueRequiresMap = parsedInput.getValueRequiresInputsId();
            if(valueRequiresMap != null){
                this.valueRequiresInputsMap.put(inputId, valueRequiresMap);
            }
        }
        // Groups
        JSONArray groupsArray = BoutiquesUtil.getArrayValue(parsedDescriptor, "groups", true);
        this.groups = new BoutiquesGroup[groupsArray.size()];
        for(int groupNo = 0; groupNo < groupsArray.size(); groupNo++) {
            JSONObject currentGroupDescriptor = groupsArray.get(groupNo).isObject();
            if (currentGroupDescriptor == null) {
                throw new RuntimeException("Invalid Boutiques descriptor: group " + String.valueOf(groupNo)
                        + " is not a JSON object");
            }
            this.groups[groupNo] = new BoutiquesGroup(currentGroupDescriptor);
        }
    }

    /**
     * @return String of format 'applicationName applicationVersion'
     */
    public String getFullName(){
        return this.name + " " + this.version;
    }

    /**
     * @return Application description as String
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * @return Array of BoutiquesInputs representing application inputs
     */
    public BoutiquesInput[] getInputs() {
        return this.inputs;
    }

    /**
     * @return Array of BoutiquesGroups representing application input groups
     */
    public BoutiquesGroup[] getGroups() {
        return groups;
    }

    /**
     * @return Map representing input dependencies of type 'disables-inputs'. Keys are disabling input IDs as Strings,
     * and values are arrays of Strings representing IDs of inputs disabled when disabling input is non empty
     */
    public Map<String, String[]> getDisablesInputsMap() {
        return this.disablesInputsMap;
    }

    /**
     * @return Map representing input dependencies of type 'requires-inputs'. Keys are dependant input IDs as Strings,
     * and values are arrays of Strings representing IDs of inputs that need to be non-empty for dependant input to be
     * enabled
     */
    public Map<String, String[]> getRequiresInputsMap() {
        return this.requiresInputsMap;
    }

    /**
     * @return Map representing input dependencies of type 'value-disables'. Keys are disabling input IDs as Strings,
     * and values are Maps of String values to arrays of Strings representing IDs of inputs disabled when
     * corresponding value of disabling input is selected
     */
    public Map<String, Map<String, String[]>> getValueDisablesInputsMap() {
        return valueDisablesInputsMap;
    }

    /**
     * @return Map representing input dependencies of type 'value-requires'. Keys are dependant input IDs as Strings,
     * and values are Maps of String values to arrays of Strings representing IDs of inputs that need to be non empty
     * when corresponding value of dependant input is selected
     */
    public Map<String, Map<String, String[]>> getValueRequiresInputsMap() {
        return valueRequiresInputsMap;
    }
}