package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.*;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.*;

import java.util.*;
import java.util.function.Function;

/**
 * Helper class for parsing JSON objects
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesParser {

    /**
     * Parse JSON Boutiques descriptor
     *
     * @param descriptor        String representing application JSON descriptor
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesApplication parseApplication(String descriptor) throws RuntimeException{
        JSONObject parsedDescriptor = JSONParser.parseStrict(descriptor).isObject();
        if (parsedDescriptor == null) {
            throw new RuntimeException("Invalid Boutiques descriptor: not a JSON object.");
        }
        String name = getStringValue(parsedDescriptor, "name");
        String description = getStringValue(parsedDescriptor, "description");
        String version = getStringValue(parsedDescriptor, "tool-version");
        BoutiquesApplication application = new BoutiquesApplication(name, description, version);
        // Inputs
        JSONArray inputsArray = getArrayValue(parsedDescriptor, "inputs", false);
        for(int inputNo = 0; inputNo < inputsArray.size(); inputNo++){
            BoutiquesInput input = parseInput(inputsArray.get(inputNo).isObject());
            application.addInput(input);
            // Dependencies
            String inputId = input.getId();
            // disables-inputs
            if(input.getDisablesInputsId() != null){
                application.addDisablesInputs(inputId, input.getDisablesInputsId());
            }
            // requires-inputs
            if(input.getRequiresInputsId() != null){
                application.addRequiresInputs(inputId, input.getRequiresInputsId());
            }
            // value-disables
            if(input.getValueDisablesInputsId() != null){
                application.addValueDisablesInputs(inputId, input.getValueDisablesInputsId());
            }
            // value-requires
            if(input.getValueRequiresInputsId() != null){
                application.addValueRequiresInputs(inputId, input.getValueRequiresInputsId());
            }
        }
        // Groups
        JSONArray groupsArray = getArrayValue(parsedDescriptor, "groups", true);
        for(int groupNo = 0; groupNo < groupsArray.size(); groupNo++) {
            JSONObject currentGroupDescriptor = groupsArray.get(groupNo).isObject();
            if (currentGroupDescriptor == null) {
                throw new RuntimeException("Invalid Boutiques descriptor: group " + groupNo
                        + " is not a JSON object");
            }
            application.addGroup(parseGroup(currentGroupDescriptor));
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
                application.getOutputFiles().add(parseBoutiquesOutputFile(outputJSONArray.get(i).isObject()));
            }
        }
        // Tags
        JSONObject tagsJSONObject = getObjectValue(parsedDescriptor, "tags", true);
        if (tagsJSONObject != null) {
            for (String key : tagsJSONObject.keySet()) {
                String value = getStringValue(tagsJSONObject, key);
                application.addTag(key, value);
            }
        }
        // Container image
        JSONObject containerObject = getObjectValue(parsedDescriptor, "container-image", true);
        if (containerObject != null) {
            application.setContainerType(getStringValue(containerObject, "type"));
            application.setContainerImage(getStringValue(containerObject, "image"));
            application.setContainerIndex(getStringValue(containerObject, "index", true));
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
     * @throws RuntimeException if the JSONObject is not a valid representation of a Boutiques input
     */
    public BoutiquesInput parseInput(JSONObject inputJson) throws RuntimeException{
        if (inputJson == null){
            throw new RuntimeException("Invalid Boutiques descriptor: not a JSON object");
        }
        // General attributes
        String id = getStringValue(inputJson, "id");
        String name = getStringValue(inputJson, "name");
        String description = getStringValue(inputJson, "description", true);
        boolean isOptional = getBooleanValue(inputJson, "optional", true);
        List<String> disablesInputsId = getArrayValueAsStringList(inputJson, "disables-inputs", true);
        List<String> requiresInputsId = getArrayValueAsStringList(inputJson, "requires-inputs", true);
        String typeString = getStringValue(inputJson, "type");
        BoutiquesInput.InputType inputType = BoutiquesInput.InputType.valueOf(typeString.toUpperCase());
        BoutiquesInput input;
        // Flag is treated separately as it does not accept value-disables, value-requires or value-choice properties
        if (inputType == BoutiquesInput.InputType.FLAG){
            boolean defaultValue = getBooleanValue(inputJson, "default-value", true);
            input = new BoutiquesInputFlag(id, name, description, isOptional, disablesInputsId, requiresInputsId,
                    defaultValue);
        } else {
            // Non flag inputs (Number, String or File)
            Map<String, List<String>> valueDisablesInputsId = getStringMapValue(inputJson, "value-disables",
                    true);
            Map<String, List<String>> valueRequiresInputsId = getStringMapValue(inputJson, "value-requires",
                    true);
            JSONArray possibleValuesArray = getArrayValue(inputJson, "value-choices", true);
            List<String> possibleValues;
            switch (inputType) {
                case NUMBER:
                    possibleValues = jsonArrayToStringList(isOptional, possibleValuesArray, this::jsonValueToDouble);
                    Double defaultValueDouble = getDoubleValue(inputJson, "default-value", true);
                    boolean isInteger = getBooleanValue(inputJson, "integer", true);
                    Double maximum = getDoubleValue(inputJson, "maximum", true);
                    Double minimum = getDoubleValue(inputJson, "minimum", true);
                    boolean isExclusiveMaximum = getBooleanValue(inputJson, "exclusive-maximum", true);
                    boolean isExclusiveMinimum = getBooleanValue(inputJson, "exclusive-minimum", true);
                    input = new BoutiquesInputNumber(id, name, description, isOptional, disablesInputsId,
                            requiresInputsId, possibleValues, valueDisablesInputsId, valueRequiresInputsId,
                            defaultValueDouble, isInteger, isExclusiveMinimum, isExclusiveMaximum, maximum, minimum);
                    break;
                case STRING:
                case FILE:
                    possibleValues = jsonArrayToStringList(isOptional, possibleValuesArray, this::jsonValueToString);
                    String defaultValueString = getStringValue(inputJson, "default-value", true);
                    input = new BoutiquesInputString(id, name, description, inputType, isOptional, disablesInputsId,
                            requiresInputsId,  possibleValues, valueDisablesInputsId, valueRequiresInputsId,
                            defaultValueString);
                    break;
                default:
                    throw new RuntimeException("Invalid Boutiques descriptor: invalid type '"
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
     */
    public BoutiquesGroup parseGroup(JSONObject groupJson){
        String id = getStringValue(groupJson, "id");
        List<String> members = getArrayValueAsStringList(groupJson, "members", false);
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
     * @throws RuntimeException if outputFile is not a valid representation of an output file
     */
    private BoutiquesOutputFile parseBoutiquesOutputFile(JSONObject outputFile) throws RuntimeException {
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


    /**
     * Converts a JSONArray to List of Strings. Each value is checked using valueConverter which converts them to any
     * Object or returns null if the value is invalid.
     *
     * @param addEmptyValue  boolean: true if null should be added as first value of returned List
     * @param jsonArray      JSONArray from which values are taken
     * @param valueConverter Function converting a JSONValue to any Object, or returning null if the JSONValue is
     *                       not of expected type. It is used to ensure all values from jsonArray are valid.
     * @return List of String. Always null if jsonArray was null
     * @throws RuntimeException if a value is not valid (valueConverter returned null on one of jsonArray's values)
     */
    private List<String> jsonArrayToStringList(boolean addEmptyValue, JSONArray jsonArray,
                                       Function<JSONValue, Object> valueConverter) throws RuntimeException {
        if(jsonArray == null) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        if (addEmptyValue) {
            stringList.add(null);
        }
        for (int valueNo = 0; valueNo < jsonArray.size(); valueNo++) {
            Object iValue = valueConverter.apply(jsonArray.get(valueNo));
            if (iValue == null) {
                throw new RuntimeException("Invalid Boutiques descriptor: input has invalid value-choices.");
            }
            stringList.add(iValue.toString());
        }
        return stringList;
    }



    /**
     * Helper method to get value associated to given key in given JSON object. Provided converter function allows
     * conversion from JSONValue to awaited type. Converter returns null if obtained value is not a valid
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return value
     *                      instead of a RuntimeException
     * @param converter     Function<JSONValue, T> converting found JSONValue to expected type, or null if value is
     *                      invalid
     * @param awaitedType   String representing expected type, used in error message if found value is invalid
     * @param <T>           Type of expected value after conversion
     * @return              Value associated to key after conversion, or null if key is absent and optional is true
     * @throws RuntimeException if expected value is invalid or if key is absent and optional is false
     * @see #getStringValue(JSONObject, String, boolean)
     * @see #getDoubleValue(JSONObject, String, boolean)
     * @see #getBooleanValue(JSONObject, String, boolean)
     * @see #getArrayValue(JSONObject, String, boolean)
     */
    private <T> T applyToValue(JSONObject descriptor, String key, boolean optional,
                                     Function<JSONValue, T> converter, String awaitedType)
            throws RuntimeException {
        if (descriptor.containsKey(key)) {
            T value = converter.apply(descriptor.get(key));
            if (value == null) {
                throw new RuntimeException("Invalid Boutiques descriptor: '" + key + "' value is not a valid "
                        + awaitedType);
            }
            return value;
        } else {
            if(optional){
                return null;
            } else {
                throw new RuntimeException("Invalid Boutiques descriptor: does not contain key '" + key + "'.");
            }
        }
    }

    /**
     * Convert given JSONValue to a Double if possible, otherwise return null
     *
     * @param value JSONValue to convert
     * @return      Double representation of value, or null if it is not a valid number
     */
    private Double jsonValueToDouble(JSONValue value) {
        JSONNumber numberValue = value.isNumber();
        if(numberValue != null){
            return numberValue.doubleValue();
        }
        // If value is not a number, it can be a String containing a number
        JSONString stringValue = value.isString();
        if(stringValue == null){
            return null;
        }
        try{
            return Double.parseDouble(stringValue.stringValue());
        } catch (NumberFormatException exception){
            return null;
        }
    }

    /**
     * Convert given JSONValue to a String if possible, otherwise return null
     *
     * @param value JSONValue to convert
     * @return      String representation of value, or null if it is not a valid String
     */
    public String jsonValueToString(JSONValue value) {
        JSONString stringValue = value.isString();
        if(stringValue != null){
            return stringValue.stringValue();
        }
        return null;
    }

    /**
     * Get numeric value associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return value
     *                      instead of a RuntimeException
     * @return              Double value associated to key in descriptor, or null if key is absent and optional is true
     * @throws RuntimeException if expected value is not a valid number or if key is absent and optional is false
     */
    private Double getDoubleValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        return applyToValue(descriptor, key, optional, this::jsonValueToDouble, "number");
    }

    /**
     * Get boolean value associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a false return
     *                      value instead of a RuntimeException
     * @return              boolean value associated to key in descriptor, or false if key is absent and optional is
     *                      true
     * @throws RuntimeException if expected value is not a valid boolean or if key is absent and optional is false
     */
    private boolean getBooleanValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        JSONBoolean value = applyToValue(descriptor, key, optional, JSONValue::isBoolean, "boolean");
        return (value != null) && value.booleanValue();
    }

    /**
     * Get String value associated to given mandatory key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @return              String value associated to key in descriptor
     * @throws RuntimeException if expected value is not a valid String or if key is absent
     * @see #getStringValue(JSONObject, String, boolean)
     */
    private String getStringValue(JSONObject descriptor, String key) throws RuntimeException{
        String stringValue = getStringValue(descriptor, key, false);
        assert stringValue != null;
        return stringValue;
    }

    /**
     * Get String value associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return value
     *                      instead of a RuntimeException
     * @return              String value associated to key in descriptor, or null if key is absent and optional is true
     * @throws RuntimeException if expected value is not a valid String or if key is absent and optional is false
     */
    private String getStringValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        JSONString value = applyToValue(descriptor, key, optional, JSONValue::isString, "String");
        return value == null ? null : value.stringValue();
    }

    /**
     * Get JSONArray value associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return
     *                      value instead of a RuntimeException
     * @return              JSONArray value associated to key in descriptor, or null if key is absent and optional is
     *                      true
     * @throws RuntimeException if expected value is not a valid array or if key is absent and optional is false
     */
    private JSONArray getArrayValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        return applyToValue(descriptor, key, optional, JSONValue::isArray, "array");
    }

    /**
     * Get a List of String associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return
     *                      value instead of a RuntimeException
     * @return              Array of Strings associated to key in descriptor, or null if key is absent and optional is
     *                      true
     * @throws RuntimeException if expected value is not a valid String array or if key is absent and optional is false
     */
    private List<String> getArrayValueAsStringList(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        JSONArray array = getArrayValue(descriptor, key, optional);
        try {
            return array == null ? null : JSONArrayToStrings(array);
        } catch(RuntimeException exception){
            throw new RuntimeException("Invalid Boutiques descriptor: '" + key
                    + "' array contains non-String element(s).");
        }
    }

    /**
     * Convert a JSONArray to a String array
     *
     * @param array JSONArray to convert
     * @return      Array of Strings representing array
     * @throws RuntimeException if some elements of array are not valid Strings
     */
    private List<String> JSONArrayToStrings(JSONArray array) throws RuntimeException {
        List<String> stringArray = new ArrayList<>();
        for (int valueNo = 0; valueNo < array.size(); valueNo++){
            JSONString valueString = array.get(valueNo).isString();
            if(valueString == null){
                throw new RuntimeException("Invalid Array: value " + valueNo + " is not a String.");
            }
            stringArray.add(valueString.stringValue());
        }
        return stringArray;
    }

    /**
     * Get a Map representing the JSON object associated to given key in given parent JSON object. Obtained JSON
     * object must have String keys and String arrays as values
     *
     * @param descriptor    Parent JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return
     *                      value instead of a RuntimeException
     * @return              Map representing JSON object associated to key in descriptor, or null if key is absent and
     *                      optional is true. Map's keys are obtained JSON object keys and values are String arrays
     *                      representing its values
     * @throws RuntimeException if expected value is not a valid object or if key is absent and optional is false
     */
    private Map<String, List<String>> getStringMapValue(JSONObject descriptor, String key,
                                                              boolean optional) throws RuntimeException {
        JSONObject object = applyToValue(descriptor, key, optional, JSONValue::isObject, "JSON object");
        if(object == null){
            return null;
        }
        // Converts obtained object to a Map of Strings to String arrays
        Map<String, List<String>> convertedObject = new HashMap<>();
        for(String objectKey : object.keySet()){
            JSONArray objectValue = object.get(objectKey).isArray();
            if (objectValue == null){
                throw new RuntimeException("Invalid Boutiques descriptor: '" + objectKey + "' value in " + key
                        + "object is not a JSON Array.");
            }
            if(objectValue.size() > 0){
                convertedObject.put(objectKey, JSONArrayToStrings(objectValue));
            }
        }
        return convertedObject;
    }

    /**
     * Return the JSONObject associated to given key in given descriptor
     *
     * @param descriptor JSONObject containing key
     * @param key        String key associated to returned value
     * @param optional   boolean: true if key is optional, in which case its absence will lead to a null return
     *                   value instead of a RuntimeException
     * @return           JSONObject associated to key in descriptor
     * @throws RuntimeException if expected value is not a valid JSONObjector if key is absent and optional is false
     */
    private JSONObject getObjectValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        return applyToValue(descriptor, key, optional, JSONValue::isObject, "JSON object");
    }
}
