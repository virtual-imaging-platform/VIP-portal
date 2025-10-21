package fr.insalyon.creatis.vip.application.models.boutiquesTools;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BoutiquesApplicationExtensions implements IsSerializable {

    private Boolean addResultsDirectoryInput;
    private Map<String, Map<String, String>> valueChoicesLabels = new HashMap<>();
    private Set<String> nonListInputs = new HashSet<>();

    public BoutiquesApplicationExtensions() {
        this.addResultsDirectoryInput = true;
    }

    public BoutiquesApplicationExtensions(Boolean addResultsDirectoryInput) {
        this.addResultsDirectoryInput = addResultsDirectoryInput;
    }

    public void addValueChoiceLabels(String inputId, Map<String, String> valueChoiceLabels) {
        valueChoicesLabels.put(inputId, valueChoiceLabels);
    }

    public void addNonListInputs(String... inputIds){
        Collections.addAll(nonListInputs, inputIds);
    }

    public Boolean getAddResultsDirectoryInput() {
        return addResultsDirectoryInput;
    }

    public Map<String, String> getValueChoicesLabelsForInput(String inputId) {
        return valueChoicesLabels.get(inputId);
    }

    public Set<String> getNonListInputs() {
        return nonListInputs;
    }
}
