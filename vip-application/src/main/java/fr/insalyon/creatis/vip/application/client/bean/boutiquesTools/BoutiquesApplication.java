package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput.InputType;

public class BoutiquesApplication implements IsSerializable {

    private String name;
    private String description;
    private String version;
    private String originalDescriptor;
    private Set<BoutiquesInput> inputs = new HashSet<>();
    // Input dependencies
    private Set<BoutiquesGroup> groups = new HashSet<>();
    // Other properties not used for launch form generation
    private String author;
    private String commandLine;
    private String containerType;
    private String containerImage;
    private String containerIndex;
    private String schemaVersion;
    private String challengerEmail;
    private String challengerTeam;
    private String vipResultsDirectoryDefault;
    private String vipResultsDirectoryDescription;
    private Set<BoutiquesOutputFile> outputFiles = new HashSet<>();
    private Map<String, String> tags = new HashMap<>();
    private Set<String> vipDotInputIds;
    private Map<String, String> vipOverriddenInputs;

    private BoutiquesApplicationExtensions boutiquesExtensions;

    public BoutiquesApplication(){}

    /**
     * @param name String
     * @param description String
     * @param version String
     */
    public BoutiquesApplication(String name, String description, String version, String originalDescriptor) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.originalDescriptor = originalDescriptor;
    }

    public void setBoutiquesExtensions(BoutiquesApplicationExtensions boutiquesExtensions) {
        if (this.boutiquesExtensions != null) {
            throw new IllegalStateException("A boutiques application can only be extended once");
        }
        this.boutiquesExtensions = boutiquesExtensions;
    }

    public BoutiquesApplicationExtensions getBoutiquesExtensions() {
        return boutiquesExtensions;
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

    public String getOriginalDescriptor() { return this.originalDescriptor; }

    /**
     * @return Array of BoutiquesInputs representing application inputs
     */
    public Set<BoutiquesInput> getInputs() {
        return this.inputs;
    }

    public Optional<BoutiquesInput> getInput(String inputId) {
        return this.inputs.stream().filter(input -> input.getId().equals(inputId)).findAny();
    }

    /**
     * @return Array of BoutiquesGroups representing application input groups
     */
    public Set<BoutiquesGroup> getGroups() {
        return groups;
    }

    /**
     * Generic method for getters of input attribute maps.
     *
     * @param inputGetter Function: getter implemented in BoutiquesInput to return an input attribute.
     * @param <T> Type of the input attribute
     * @return  Map from String input IDs to the attribute of those inputs
     */
    private <T> Map<String, T> getMap(Function<BoutiquesInput, T> inputGetter){
        return this.inputs.stream()
                .filter(input -> inputGetter.apply(input) != null)
                .collect(Collectors.toMap(BoutiquesInput::getId, inputGetter));
    }

    /**
     * @return Map representing input dependencies of type 'disables-inputs'. Keys are disabling input IDs as Strings,
     * and values are arrays of Strings representing IDs of inputs disabled when disabling input is non empty
     */
    public Map<String, Set<String>> getDisablesInputsMap() {
        return this.getMap(BoutiquesInput::getDisablesInputsId);
    }

    /**
     * @return Map representing input dependencies of type 'requires-inputs'. Keys are dependant input IDs as Strings,
     * and values are arrays of Strings representing IDs of inputs that need to be non-empty for dependant input to be
     * enabled
     */
    public Map<String, Set<String>> getRequiresInputsMap() {
        return this.getMap(BoutiquesInput::getRequiresInputsId);
    }

    /**
     * @return Map representing input dependencies of type 'value-disables'. Keys are disabling input IDs as Strings,
     * and values are Maps of String values to arrays of Strings representing IDs of inputs disabled when
     * corresponding value of disabling input is selected
     */
    public Map<String, Map<String, Set<String>>> getValueDisablesInputsMap() {
        return this.getMap(BoutiquesInput::getValueDisablesInputsId);
    }

    /**
     * @return Map representing input dependencies of type 'value-requires'. Keys are dependant input IDs as Strings,
     * and values are Maps of String values to arrays of Strings representing IDs of inputs that need to be non empty
     * when corresponding value of dependant input is selected
     */
    public Map<String, Map<String, Set<String>>> getValueRequiresInputsMap() {
        return this.getMap(BoutiquesInput::getValueRequiresInputsId);
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

    public Set<BoutiquesOutputFile> getOutputFiles() {
        return outputFiles;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String getVipResultsDirectoryDefault() {
        return vipResultsDirectoryDefault;
    }

    public String getVipResultsDirectoryDescription() {
        return vipResultsDirectoryDescription;
    }

    public Set<String> getVipDotInputIds() {
        if (vipDotInputIds == null) {
            return Collections.emptySet();
        }
        return vipDotInputIds;
    }

    public Set<String> getCommandLineFlag() {
        return inputs.stream()
                .filter(i -> InputType.FLAG.equals(i.getType()))
                .map(BoutiquesInput::getId)
                .collect(Collectors.toSet());
    }

    public Set<String> getinputIds() {
        return this.getInputs().stream()
                .map(BoutiquesInput::getId)
                .collect(Collectors.toSet());
    }

    public Map<String, String> getVipOverriddenInputs() {
        return vipOverriddenInputs;
    }

    public void addInput(BoutiquesInput input){
        this.inputs.add(input);
    }

    public void addGroup(BoutiquesGroup group){
        this.groups.add(group);
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

    public void addTag(String key, String value) {
        tags.put(key, value);
    }

    public void setVipResultsDirectoryDefault(String vipResultsDirectoryDefault) {
        this.vipResultsDirectoryDefault = vipResultsDirectoryDefault;
    }

    public void setVipResultsDirectoryDescription(String vipResultsDirectoryDescription) {
        this.vipResultsDirectoryDescription = vipResultsDirectoryDescription;
    }

    public void setVipDotInputIds(Set<String> inputIds) {
        this.vipDotInputIds = inputIds;
    }

    public void setVipOverriddenInputs(Map<String, String> vipOverriddenInputs) {
        this.vipOverriddenInputs = vipOverriddenInputs;
    }
}
