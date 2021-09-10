package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.*;

import java.util.*;
import java.util.function.Function;

/**
 * Helper class for parsing JSON objects
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class AbstractJsonParser {

    /**
     * Helper method to get value associated to given key in given JSON object. Provided converter function allows
     * conversion from JSONValue to awaited type. Converter returns null if obtained value is not of expected type
     * (in which case applyToValue throws an InvalidBoutiquesDescriptorException)
     *
     * @param jsonObject    JSONObject to parse
     * @param key           String representing the key in jsonObject associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return value
     *                      instead of a RuntimeException
     * @param converter     Function<JSONValue, T> converting found JSONValue to expected type, or null if value is
     *                      invalid
     * @param <T>           Type of expected value after conversion
     * @return              Value associated to key after conversion, or null if key is absent and optional is true
     * @throws InvalidBoutiquesDescriptorException if value is invalid or if key is absent and optional is false
     * @see #getStringValue(JSONObject, String, boolean)
     * @see #getDoubleValue(JSONObject, String, boolean)
     * @see #getBooleanValue(JSONObject, String, boolean)
     * @see #getArrayValue(JSONObject, String, boolean)
     */
    protected  <T> T applyToValue(JSONObject jsonObject, String key, boolean optional,
                               Function<JSONValue, T> converter)
            throws InvalidBoutiquesDescriptorException {
        if (jsonObject.containsKey(key)) {
            T value = converter.apply(jsonObject.get(key));
            if (value == null) {
                throw new InvalidBoutiquesDescriptorException("Invalid JSON object: '" + key + "' value is not valid.");
            }
            return value;
        } else {
            if(optional){
                return null;
            } else {
                throw new InvalidBoutiquesDescriptorException("Invalid JSON object: does not contain mandatory key '"
                        + key + "'.");
            }
        }
    }

    /**
     * Convert given JSONValue to a Double if possible, otherwise return null
     *
     * @param value JSONValue to convert
     * @return      Double representation of value, or null if it is not a valid number
     */
    protected Double jsonValueToDouble(JSONValue value) {
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
    protected String jsonValueToString(JSONValue value) {
        JSONString stringValue = value.isString();
        if(stringValue != null){
            return stringValue.stringValue();
        }
        return null;
    }

    /**
     * Converts a JSONArray to Set of Strings. Each value is checked using valueConverter which converts them to any
     * Object or returns null if the value is invalid.
     *
     * @param addEmptyValue  boolean: true if null should be added as first value of returned List
     * @param jsonArray      JSONArray from which values are taken
     * @param valueConverter Function converting a JSONValue to any Object, or returning null if the JSONValue is
     *                       not of expected type. It is used to ensure all values from jsonArray are valid.
     * @return Set of String. Always null if jsonArray was null
     * @throws InvalidBoutiquesDescriptorException if a value is not valid (valueConverter returned null on one of
     *                                             jsonArray's values)
     */
    protected Set<String> jsonArrayToStringSet(boolean addEmptyValue, JSONArray jsonArray,
                                                Function<JSONValue, Object> valueConverter)
            throws InvalidBoutiquesDescriptorException {
        if(jsonArray == null) {
            return null;
        }
        Set<String> stringList = new HashSet<>();
        if (addEmptyValue) {
            stringList.add(null);
        }
        for (int valueNo = 0; valueNo < jsonArray.size(); valueNo++) {
            Object iValue = valueConverter.apply(jsonArray.get(valueNo));
            if (iValue == null) {
                throw new InvalidBoutiquesDescriptorException("Invalid JSON array: value " + valueNo
                        + " is not of expected type.");
            }
            stringList.add(iValue.toString());
        }
        return stringList;
    }

    /**
     * Get numeric value associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return value
     *                      instead of a RuntimeException
     * @return              Double value associated to key in descriptor, or null if key is absent and optional is true
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid number or if key is absent and
     *                                             optional is false
     */
    protected Double getDoubleValue(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        try {
            return applyToValue(descriptor, key, optional, this::jsonValueToDouble);
        } catch (InvalidBoutiquesDescriptorException exception){
            throw new InvalidBoutiquesDescriptorException("Invalid JSON object: no valid double value with key '"
                    + key + "'.", exception);
        }
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
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid boolean or if key is absent and
     *                                             optional is false
     */
    protected boolean getBooleanValue(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        try {
            JSONBoolean value = applyToValue(descriptor, key, optional, JSONValue::isBoolean);
            return (value != null) && value.booleanValue();
        } catch (InvalidBoutiquesDescriptorException exception){
            throw new InvalidBoutiquesDescriptorException("Invalid JSON object: no valid boolean value with key '"
                    + key + "'.", exception);
        }
    }

    /**
     * Get String value associated to given mandatory key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @return              String value associated to key in descriptor
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid String or if key is absent
     * @see #getStringValue(JSONObject, String, boolean)
     */
    protected String getStringValue(JSONObject descriptor, String key) throws InvalidBoutiquesDescriptorException{
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
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid String or if key is absent and
     *                                             optional is false
     */
    protected String getStringValue(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        try{
            JSONString value = applyToValue(descriptor, key, optional, JSONValue::isString);
            return value == null ? null : value.stringValue();
        } catch (InvalidBoutiquesDescriptorException exception){
            throw new InvalidBoutiquesDescriptorException("Invalid JSON object: no valid String value with key '"
                    + key + "'.", exception);
        }
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
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid array or if key is absent and
     *                                             optional is false
     */
    protected JSONArray getArrayValue(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        try {
            return applyToValue(descriptor, key, optional, JSONValue::isArray);
        } catch (InvalidBoutiquesDescriptorException exception){
            throw new InvalidBoutiquesDescriptorException("Invalid JSON object: no valid Array value with key '"
                    + key + "'.", exception);
        }
    }

    /**
     * Get a Set of String associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return
     *                      value instead of a RuntimeException
     * @return              Set of Strings associated to key in descriptor, or null if key is absent and optional is
     *                      true
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid String array or if key is absent
     *                                             and optional is false
     */
    protected Set<String> getArrayValueAsStringSet(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        JSONArray array = getArrayValue(descriptor, key, optional);
        if (array == null){
            return null;
        } else {
            try {
                return jsonArrayToStringSet(false, array, this::jsonValueToString);
            } catch (InvalidBoutiquesDescriptorException exception) {
                throw new InvalidBoutiquesDescriptorException("Invalid JSON object: '" + key
                        + "' array contains non-String element(s).", exception);
            }
        }
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
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid object or if key is absent and
     *                                             optional is false
     */
    protected Map<String, Set<String>> getStringSetMapValue(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        JSONObject object = getObjectValue(descriptor, key, optional);
        if(object == null){
            return null;
        }
        // Converts obtained object to a Map of Strings to String arrays
        Map<String, Set<String>> convertedObject = new HashMap<>();
        for(String objectKey : object.keySet()){
            JSONArray objectValue = object.get(objectKey).isArray();
            if (objectValue == null){
                throw new InvalidBoutiquesDescriptorException("Invalid JSON object: '" + objectKey
                        + "' value in " + key + "object is not a JSON Array.");
            }
            if(objectValue.size() > 0){
                convertedObject.put(objectKey, jsonArrayToStringSet(false, objectValue,
                        this::jsonValueToString));
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
     * @throws InvalidBoutiquesDescriptorException if expected value is not a valid JSONObjector if key is absent and
     *                                             optional is false
     */
    protected JSONObject getObjectValue(JSONObject descriptor, String key, boolean optional)
            throws InvalidBoutiquesDescriptorException {
        try{
            return applyToValue(descriptor, key, optional, JSONValue::isObject);
        } catch (InvalidBoutiquesDescriptorException exception){
            throw new InvalidBoutiquesDescriptorException("Invalid JSON object: no valid JSON object value with key '"
                    + key + "'.", exception);
        }
    }
}
