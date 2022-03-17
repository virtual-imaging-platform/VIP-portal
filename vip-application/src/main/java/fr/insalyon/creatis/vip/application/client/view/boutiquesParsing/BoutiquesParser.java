package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.*;

import java.util.Map;
import java.util.Set;

/**
 * Helper class for parsing JSON objects
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesParser extends AbstractJsonParser{

    /**
     * Parse JSON Boutiques descriptor
     *
     * @param descriptor        String representing application JSON descriptor
     * @throws InvalidBoutiquesDescriptorException if descriptor is invalid
     */
    public BoutiquesApplication parseApplication(String descriptor) throws InvalidBoutiquesDescriptorException{
        JSONObject parsedDescriptor = JSONParser.parseStrict(descriptor).isObject();
        if (parsedDescriptor == null) {
            throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: not a JSON object.");
        }
        String name = getStringValue(parsedDescriptor, "name");
        String description = getStringValue(parsedDescriptor, "description");
        String version = getStringValue(parsedDescriptor, "tool-version");
        BoutiquesApplication application = new BoutiquesApplication(name, description, version);
        // Inputs
        JSONArray inputsArray = getArrayValue(parsedDescriptor, "inputs", false);
        for(int inputNo = 0; inputNo < inputsArray.size(); inputNo++){
            BoutiquesInput input;
            try {
                input = parseInput(inputsArray.get(inputNo).isObject());
            } catch (InvalidBoutiquesDescriptorException exception){
                throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: input " + inputNo
                        + " is invalid.", exception);
            }
            application.addInput(input);
        }
        // Groups
        JSONArray groupsArray = getArrayValue(parsedDescriptor, "groups", true);
        if (groupsArray != null) {
            for(int groupNo = 0; groupNo < groupsArray.size(); groupNo++) {
                JSONObject currentGroupDescriptor = groupsArray.get(groupNo).isObject();
                if (currentGroupDescriptor == null) {
                    throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: group " + groupNo
                            + " is not a JSON object");
                }
                try {
                    application.addGroup(parseGroup(currentGroupDescriptor));
                } catch (InvalidBoutiquesDescriptorException exception){
                    throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: group " + groupNo
                            + " is invalid.", exception);
                }
            }
        }
        // Other properties
        application.setAuthor(getStringValue(parsedDescriptor, "author", true));
        application.setCommandLine(getStringValue(parsedDescriptor, "command-line"));
        application.setSchemaVersion(getStringValue(parsedDescriptor, "schema-version"));
        application.setChallengerEmail(getStringValue(parsedDescriptor, "vip:miccai-challenger-email", true));
        application.setChallengerTeam(getStringValue(parsedDescriptor, "vip:miccai-challenge-team-name", true));
        // Output files
        JSONArray outputJSONArray = getArrayValue(parsedDescriptor, "output-files", true);
        if (outputJSONArray != null) {
            for (int i = 0; i < outputJSONArray.size(); i++) {
                try{
                    application.getOutputFiles().add(parseBoutiquesOutputFile(outputJSONArray.get(i).isObject()));
                } catch (InvalidBoutiquesDescriptorException exception){
                    throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: output file " + i
                            + " is invalid.", exception);
                }
            }
        }
        // Tags
        JSONObject tagsJSONObject = getObjectValue(parsedDescriptor, "tags", true);
        if (tagsJSONObject != null) {
            for (String key : tagsJSONObject.keySet()) {
                String value;
                try {
                     value = getStringValue(tagsJSONObject, key);
                } catch (InvalidBoutiquesDescriptorException exception){
                    throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: tag with key '" + key
                            + "' is not a valid String.", exception);
                }
                application.addTag(key, value);
            }
        }
        // Container image
        JSONObject containerObject = getObjectValue(parsedDescriptor, "container-image", true);
        if (containerObject != null) {
            try {
                application.setContainerType(getStringValue(containerObject, "type"));
                application.setContainerImage(getStringValue(containerObject, "image"));
                application.setContainerIndex(getStringValue(containerObject, "index", true));
            } catch (InvalidBoutiquesDescriptorException exception){
                throw new InvalidBoutiquesDescriptorException("Invalid Boutiques descriptor: invalid container-image.",
                        exception);
            }
        }
        // Json descriptor
        application.setJsonFile(parsedDescriptor.toString());
        return application;
    }

    /**
     * Parse a JSONObject corresponding to an input inside a Boutiques descriptor and return corresponding BoutiquesInput
     *
     * @param inputJson JSONObject containing input information
     * @return          Parsed BoutiquesInput
     * @throws InvalidBoutiquesDescriptorException if the JSONObject is not a valid representation of a Boutiques input
     */
    public BoutiquesInput parseInput(JSONObject inputJson) throws InvalidBoutiquesDescriptorException{
        if (inputJson == null){
            throw new InvalidBoutiquesDescriptorException("Invalid input descriptor: not a JSON object");
        }
        // General attributes
        String id = getStringValue(inputJson, "id");
        String name = getStringValue(inputJson, "name");
        String description = getStringValue(inputJson, "description", true);
        boolean isOptional = getBooleanValue(inputJson, "optional", true);
        Set<String> disablesInputsId = getArrayValueAsStringSet(inputJson, "disables-inputs", true);
        Set<String> requiresInputsId = getArrayValueAsStringSet(inputJson, "requires-inputs", true);
        String typeString = getStringValue(inputJson, "type");
        BoutiquesInput.InputType inputType = BoutiquesInput.InputType.valueOf(typeString.toUpperCase());
        BoutiquesInput input;
        // Flag is treated separately as it does not accept value-disables, value-requires or value-choice properties
        if (inputType == BoutiquesInput.InputType.FLAG){
            boolean defaultValue = getBooleanValue(inputJson, "default-value", true);
            input = new BoutiquesFlagInput(id, name, description, isOptional, disablesInputsId, requiresInputsId,
                    defaultValue);
        } else {
            // Non flag inputs (Number, String or File)
            Map<String, Set<String>> valueDisablesInputsId = getStringSetMapValue(inputJson, "value-disables",
                    true);
            Map<String, Set<String>> valueRequiresInputsId = getStringSetMapValue(inputJson, "value-requires",
                    true);
            JSONArray possibleValuesArray = getArrayValue(inputJson, "value-choices", true);
            Set<String> possibleValues;
            switch (inputType) {
                case NUMBER:
                    try {
                        possibleValues = jsonArrayToStringSet(isOptional, possibleValuesArray, this::jsonValueToDouble);
                    } catch (InvalidBoutiquesDescriptorException exception){
                        throw new InvalidBoutiquesDescriptorException("Invalid input descriptor: "
                                + "input of type 'Number' but value-choice contains non-double value(s).",
                                exception);
                    }
                    Double defaultValueDouble = getDoubleValue(inputJson, "default-value", true);
                    boolean isInteger = getBooleanValue(inputJson, "integer", true);
                    Double maximum = getDoubleValue(inputJson, "maximum", true);
                    Double minimum = getDoubleValue(inputJson, "minimum", true);
                    boolean isExclusiveMaximum = getBooleanValue(inputJson, "exclusive-maximum", true);
                    boolean isExclusiveMinimum = getBooleanValue(inputJson, "exclusive-minimum", true);
                    input = new BoutiquesNumberInput(id, name, description, isOptional, disablesInputsId,
                            requiresInputsId, possibleValues, valueDisablesInputsId, valueRequiresInputsId,
                            defaultValueDouble, isInteger, isExclusiveMaximum, isExclusiveMinimum, maximum, minimum);
                    break;
                case STRING:
                case FILE:
                    try {
                        possibleValues = jsonArrayToStringSet(isOptional, possibleValuesArray, this::jsonValueToString);
                    } catch (InvalidBoutiquesDescriptorException exception) {
                        throw new InvalidBoutiquesDescriptorException("Invalid input descriptor: input of type '"
                                + inputType.getCamelName() + "'but value-choice contains non-String value(s).",
                                exception);
                    }
                    String defaultValueString = getStringValue(inputJson, "default-value", true);
                    input = new BoutiquesStringInput(id, name, description, inputType, isOptional, disablesInputsId,
                            requiresInputsId,  possibleValues, valueDisablesInputsId, valueRequiresInputsId,
                            defaultValueString);
                    break;
                default:
                    throw new InvalidBoutiquesDescriptorException("Invalid input descriptor: invalid type '"
                            + inputType + "'. Only allowed types are 'String', 'File', 'Number' and 'Flag'.");
            }
        }
        input.setValueKey(getStringValue(inputJson, "value-key", true));
        input.setList(getBooleanValue(inputJson, "list", true));
        String commandLineFlag = getStringValue(inputJson, "command-line-flag", true);
        input.setCommandLineFlag(commandLineFlag == null ? "" : commandLineFlag);
        return input;
    }

    /**
     * Parse a JSONObject representing an input group.
     *
     * @param groupJson JSONObject to parse
     * @return BoutiquesGroup representing parsed group
     * @throws InvalidBoutiquesDescriptorException if JSON Object is not a valid representation of a group
     */
    public BoutiquesGroup parseGroup(JSONObject groupJson) throws InvalidBoutiquesDescriptorException{
        String id = getStringValue(groupJson, "id");
        Set<String> members = getArrayValueAsStringSet(groupJson, "members", false);
        boolean allOrNone = getBooleanValue(groupJson, "all-or-none", true);
        boolean mutuallyExclusive = getBooleanValue(groupJson, "mutually-exclusive", true);
        boolean oneIsRequired = getBooleanValue(groupJson, "one-is-required", true);
        return new BoutiquesGroup(id, allOrNone, mutuallyExclusive, oneIsRequired, members);
    }

    /**
     * Parse a JSONObject representing an output file
     *
     * @param outputFile JSONObject to parse
     * @return BoutiquesOutputFile representing parsed output file
     * @throws InvalidBoutiquesDescriptorException if outputFile is not a valid representation of an output file
     */
    private BoutiquesOutputFile parseBoutiquesOutputFile(JSONObject outputFile)
            throws InvalidBoutiquesDescriptorException {
        BoutiquesOutputFile bof = new BoutiquesOutputFile();
        bof.setId(getStringValue(outputFile, "id"));
        bof.setName(getStringValue(outputFile, "name"));
        bof.setDescription(getStringValue(outputFile, "description", true));
        bof.setValueKey(getStringValue(outputFile, "value-key", true));
        bof.setPathTemplate(getStringValue(outputFile, "path-template", true));
        bof.setList(getBooleanValue(outputFile, "list", true));
        bof.setOptional(getBooleanValue(outputFile, "optional", true));
        String commandLineFlag = getStringValue(outputFile, "command-line-flag", true);
        commandLineFlag = commandLineFlag == null ? "" : commandLineFlag;
        bof.setCommandLineFlag(commandLineFlag);
        return bof;
    }
}