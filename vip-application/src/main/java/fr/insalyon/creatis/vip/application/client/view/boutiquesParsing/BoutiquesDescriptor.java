package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final List<BoutiquesInput> inputs = new ArrayList<>();
    // Input dependencies
    private final List<BoutiquesGroup> groups = new ArrayList<>();
    private final Map<String, List<String>> disablesInputsMap= new HashMap<>();
    private final Map<String, List<String>> requiresInputsMap= new HashMap<>();
    private final Map<String, Map<String, List<String>>> valueDisablesInputsMap= new HashMap<>();
    private final Map<String, Map<String, List<String>>> valueRequiresInputsMap= new HashMap<>();

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
        for(int inputNo = 0; inputNo < inputsArray.size(); inputNo++){
            parseInput(inputsArray.get(inputNo).isObject());
        }
        // Groups
        JSONArray groupsArray = BoutiquesUtil.getArrayValue(parsedDescriptor, "groups", true);
        for(int groupNo = 0; groupNo < groupsArray.size(); groupNo++) {
            JSONObject currentGroupDescriptor = groupsArray.get(groupNo).isObject();
            if (currentGroupDescriptor == null) {
                throw new RuntimeException("Invalid Boutiques descriptor: group " + groupNo
                        + " is not a JSON object");
            }
            this.groups.set(groupNo, new BoutiquesGroup(currentGroupDescriptor));
        }
    }

    private void parseInput(JSONObject inputDescriptor) {
        if (inputDescriptor == null){
            throw new RuntimeException("Invalid Boutiques descriptor: not a JSON object");
        }
        String inputType = BoutiquesUtil.getStringValue(inputDescriptor, "type");
        BoutiquesInput parsedInput;
        switch(inputType) {
            case "String":
                parsedInput = new BoutiquesInputString(inputDescriptor, BoutiquesInput.InputType.STRING);
            case "File":
                parsedInput = new BoutiquesInputString(inputDescriptor, BoutiquesInput.InputType.FILE);
                break;
            case "Number":
                parsedInput = new BoutiquesInputNumber(inputDescriptor);
                break;
            case "Flag":
                parsedInput = new BoutiquesInputFlag(inputDescriptor);
                break;
            default:
                throw new RuntimeException("Invalid Boutiques descriptor: invalid type '"
                        + inputType + "'. Only allowed types are 'String', 'File', 'Number' and 'Flag'.");
        }
        this.inputs.add(parsedInput);
        // Dependencies
        String inputId = parsedInput.getId();
        // disables-inputs
        if(parsedInput.getDisablesInputsId() != null){
            this.disablesInputsMap.put(inputId, parsedInput.getDisablesInputsId());
        }
        // requires-inputs
        if(parsedInput.getRequiresInputsId() != null){
            this.requiresInputsMap.put(inputId, parsedInput.getRequiresInputsId());
        }
        // value-disables
        if(parsedInput.getValueDisablesInputsId() != null){
            this.valueDisablesInputsMap.put(inputId, parsedInput.getValueDisablesInputsId());
        }
        // value-requires
        if(parsedInput.getValueRequiresInputsId() != null){
            this.valueRequiresInputsMap.put(inputId, parsedInput.getValueRequiresInputsId());
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
    public List<BoutiquesInput> getInputs() {
        return this.inputs;
    }

    /**
     * @return Array of BoutiquesGroups representing application input groups
     */
    public List<BoutiquesGroup> getGroups() {
        return groups;
    }

    /**
     * @return Map representing input dependencies of type 'disables-inputs'. Keys are disabling input IDs as Strings,
     * and values are arrays of Strings representing IDs of inputs disabled when disabling input is non empty
     */
    public Map<String, List<String>> getDisablesInputsMap() {
        return this.disablesInputsMap;
    }

    /**
     * @return Map representing input dependencies of type 'requires-inputs'. Keys are dependant input IDs as Strings,
     * and values are arrays of Strings representing IDs of inputs that need to be non-empty for dependant input to be
     * enabled
     */
    public Map<String, List<String>> getRequiresInputsMap() {
        return this.requiresInputsMap;
    }

    /**
     * @return Map representing input dependencies of type 'value-disables'. Keys are disabling input IDs as Strings,
     * and values are Maps of String values to arrays of Strings representing IDs of inputs disabled when
     * corresponding value of disabling input is selected
     */
    public Map<String, Map<String, List<String>>> getValueDisablesInputsMap() {
        return valueDisablesInputsMap;
    }

    /**
     * @return Map representing input dependencies of type 'value-requires'. Keys are dependant input IDs as Strings,
     * and values are Maps of String values to arrays of Strings representing IDs of inputs that need to be non empty
     * when corresponding value of dependant input is selected
     */
    public Map<String, Map<String, List<String>>> getValueRequiresInputsMap() {
        return valueRequiresInputsMap;
    }
}