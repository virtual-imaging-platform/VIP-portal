package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

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
