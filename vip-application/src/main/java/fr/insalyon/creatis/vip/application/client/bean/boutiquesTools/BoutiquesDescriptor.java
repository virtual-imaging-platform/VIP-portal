package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

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
     * @param name String
     * @param description String
     * @param version String
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesDescriptor(String name, String description, String version) throws RuntimeException{
        this.name = name;
        this.description = description;
        this.version = version;
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

    public void addInput(BoutiquesInput input){
        this.inputs.add(input);
    }

    public void addGroup(BoutiquesGroup group){
        this.groups.add(group);
    }

    public void addDisablesInputs(String masterId, List<String> disabledIds){
        this.disablesInputsMap.put(masterId, disabledIds);
    }

    public void addRequiresInputs(String masterId, List<String> requiredIds){
        this.requiresInputsMap.put(masterId, requiredIds);
    }

    public void addValueDisablesInputs(String masterId, Map<String, List<String>> valueDisabledMap){
        this.valueDisablesInputsMap.put(masterId, valueDisabledMap);
    }

    public void addValueRequiresInputs(String masterId, Map<String, List<String>> valueRequiredMap){
        this.valueRequiresInputsMap.put(masterId, valueRequiredMap);
    }
}