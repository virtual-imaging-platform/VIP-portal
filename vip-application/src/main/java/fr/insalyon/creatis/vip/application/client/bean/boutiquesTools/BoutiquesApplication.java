package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

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
public class BoutiquesApplication implements IsSerializable {

    private String name;
    private String description;
    private String version;
    private List<BoutiquesInput> inputs = new ArrayList<>();
    // Input dependencies
    private List<BoutiquesGroup> groups = new ArrayList<>();
    private Map<String, List<String>> disablesInputsMap= new HashMap<>();
    private Map<String, List<String>> requiresInputsMap= new HashMap<>();
    private Map<String, Map<String, List<String>>> valueDisablesInputsMap= new HashMap<>();
    private Map<String, Map<String, List<String>>> valueRequiresInputsMap= new HashMap<>();
    // Other properties not used for launch form generation
    private String applicationLFN;
    private String author;
    private String commandLine;
    private String containerType;
    private String containerImage;
    private String containerIndex;
    private String schemaVersion;
    private String challengerEmail;
    private String challengerTeam;
    private List<BoutiquesOutputFile> outputFiles = new ArrayList<>();
    private Map<String,String> tags = new HashMap<>();
    private String jsonFile;

    private BoutiquesApplication(){}

    /**
     * @param name String
     * @param description String
     * @param version String
     */
    public BoutiquesApplication(String name, String description, String version){
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

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getToolVersion() {
        return version;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public String getContainerType() {
        return containerType;
    }

    public String getContainerImage() {
        return containerImage;
    }

    public String getContainerIndex() {
        return containerIndex;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getChallengerEmail() {
        return challengerEmail;
    }

    public String getChallengerTeam() {
        return challengerTeam;
    }

    public List<BoutiquesOutputFile> getOutputFiles() {
        return outputFiles;
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public String getApplicationLFN() {
        return applicationLFN;
    }

    public String getWrapperLFN() {
        return this.applicationLFN + "/bin/" + getName() + ".sh";
    }

    public String getWrapperName() {
        return getName() + ".sh";
    }

    public String getGASWLFN() {
        return this.applicationLFN + "/gasw/" + getName() + ".xml";
    }

    public String getGwendiaLFN() {
        return this.applicationLFN + "/workflow/" + getName() + ".gwendia";
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String getJsonLFN() {
        return this.applicationLFN + "/json/" + getName() + ".json";
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public void setContainerImage(String containerImage) {
        this.containerImage = containerImage;
    }

    public void setContainerIndex(String containerIndex) {
        this.containerIndex = containerIndex;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public void setChallengerEmail(String challengerEmail) {
        this.challengerEmail = challengerEmail;
    }

    public void setChallengerTeam(String challengerTeam) {
        this.challengerTeam = challengerTeam;
    }

    public void setApplicationLFN(String applicationLFN) {
        this.applicationLFN = applicationLFN;
    }

    public void addTag(String key, String value) {
        tags.put(key, value);
    }

}