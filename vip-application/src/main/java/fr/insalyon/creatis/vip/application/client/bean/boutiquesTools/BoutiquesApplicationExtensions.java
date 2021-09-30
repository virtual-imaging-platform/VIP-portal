package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

public class BoutiquesApplicationExtensions implements IsSerializable {

    private Boolean addResultsDirectoryInput;
    private Map<String, Map<String, String>> valueChoicesLabels = new HashMap<>();

    private Set<String> unmodifiableInputs = new HashSet<>();
    private Set<String> hiddenInputs = new HashSet<>();
    private Set<String> nonListInputs = new HashSet<>();

    private Map<String,Set<String>> unmodifiableInputsByValue = new HashMap<>();

    public BoutiquesApplicationExtensions() {
        this.addResultsDirectoryInput = true;
    }

    public BoutiquesApplicationExtensions(Boolean addResultsDirectoryInput) {
        this.addResultsDirectoryInput = addResultsDirectoryInput;
    }

    public void addValueChoiceLabels(String inputId, Map<String, String> valueChoiceLabels) {
        valueChoicesLabels.put(inputId, valueChoiceLabels);
    }

    public void addUnmodifiableInput(String... inputIds){
        Collections.addAll(unmodifiableInputs, inputIds);
    }

    public void addHiddenInputs(String... inputIds){
        Collections.addAll(hiddenInputs, inputIds);
    }

    public void addNonListInputs(String... inputIds){
        Collections.addAll(nonListInputs, inputIds);
    }

    public void addUnmodifiableInputByValue(String inputId, Set<String> unmodifiableValues) {
        unmodifiableInputsByValue.put(inputId, unmodifiableValues);
    }

    public Boolean getAddResultsDirectoryInput() {
        return addResultsDirectoryInput;
    }

    public Map<String, String> getValueChoicesLabelsForInput(String inputId) {
        return valueChoicesLabels.get(inputId);
    }

    public Set<String> getUnmodifiableInputs() {
        return unmodifiableInputs;
    }

    public Map<String, Set<String>> getUnmodifiableInputsByValue() {
        return unmodifiableInputsByValue;
    }

    public Set<String> getHiddenInputs() {
        return hiddenInputs;
    }

    public Set<String> getNonListInputs() {
        return nonListInputs;
    }
}
