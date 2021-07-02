package fr.insalyon.creatis.vip.application.client.view.launch.boutiquesParsing;

import com.google.gwt.json.client.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Helper class for parsing JSON objects
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesUtil {

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
    public static <T> T applyToValue(JSONObject descriptor, String key, boolean optional,
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
    public static Double JSONToDouble(JSONValue value) {
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
     * Get numeric value associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return value
     *                      instead of a RuntimeException
     * @return              Double value associated to key in descriptor, or null if key is absent and optional is true
     * @throws RuntimeException if expected value is not a valid number or if key is absent and optional is false
     */
    public static Double getDoubleValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        return applyToValue(descriptor, key, optional, BoutiquesUtil::JSONToDouble, "number");
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
    public static boolean getBooleanValue(JSONObject descriptor, String key, boolean optional)
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
    public static String getStringValue(JSONObject descriptor, String key) throws RuntimeException{
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
    public static String getStringValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        JSONString value = applyToValue(descriptor, key, optional, JSONValue::isString, "String");
        return value == null ? null : value.stringValue();
    }

    /**
     * Get JSONArray value associated to given mandatory key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @return              JSONArray value associated to key in descriptor
     * @throws RuntimeException if expected value is not a valid array or if key is absent
     */
    public static JSONArray getArrayValue(JSONObject descriptor, String key) throws RuntimeException {
        JSONArray value = getArrayValue(descriptor, key, false);
        assert value != null;
        return value;
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
    public static JSONArray getArrayValue(JSONObject descriptor, String key, boolean optional)
            throws RuntimeException {
        return applyToValue(descriptor, key, optional, JSONValue::isArray, "array");
    }

    /**
     * Get a String array associated to given key in given JSON object
     *
     * @param descriptor    JSONObject to parse
     * @param key           String representing the key in descriptor associated to searched value
     * @param optional      boolean: true if key is optional, in which case its absence will lead to a null return
     *                      value instead of a RuntimeException
     * @return              Array of Strings associated to key in descriptor, or null if key is absent and optional is
     *                      true
     * @throws RuntimeException if expected value is not a valid String array or if key is absent and optional is false
     */
    public static String[] getArrayValueAsStrings(JSONObject descriptor, String key, boolean optional)
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
    public static String[] JSONArrayToStrings(JSONArray array) throws RuntimeException {
        String[] stringArray = new String[array.size()];
        for (int valueNo = 0; valueNo < array.size(); valueNo++){
            JSONString valueString = array.get(valueNo).isString();
            if(valueString == null){
                throw new RuntimeException("Invalid Array: value " + valueNo + " is not a String.");
            }
            stringArray[valueNo] = valueString.stringValue();
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
    public static Map<String, String[]> getStringMapValue(JSONObject descriptor, String key,
                                                          boolean optional) throws RuntimeException {
        JSONObject object = applyToValue(descriptor, key, optional, JSONValue::isObject, "JSON object");
        if(object == null){
            return null;
        }
        // Converts obtained object to a Map of Strings to String arrays
        Map<String, String[]> convertedObject = new HashMap<>();
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
}
