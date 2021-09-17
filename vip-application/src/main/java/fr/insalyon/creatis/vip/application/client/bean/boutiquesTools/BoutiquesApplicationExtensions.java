package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

public class BoutiquesApplicationExtensions implements IsSerializable {

    private final Boolean addResultsDirectoryInput;
    private final Map<String, Map<String, String>> valueChoicesLabels = new HashMap<>();

    private final Set<String> unmodifiableInputs = new HashSet<>();
    private final Set<String> hiddenInputs = new HashSet<>();
    private final Map<String,Set<String>> unmodifiableInputsByValue = new HashMap<>();


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
}
