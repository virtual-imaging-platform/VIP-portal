package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.Map;

public class BoutiquesApplicationExtensions implements IsSerializable {

    private Boolean addResultsDirectoryInput;
    private Map<String, Map<String, String>> valueChoicesLabels = new HashMap<>();


    public BoutiquesApplicationExtensions(Boolean addResultsDirectoryInput) {
        this.addResultsDirectoryInput = addResultsDirectoryInput;
    }

    public void addValueChoiceLabels(String inputId, Map<String, String> valueChoiceLabels) {
        valueChoicesLabels.put(inputId, valueChoiceLabels);
    }

    public Boolean getAddResultsDirectoryInput() {
        return addResultsDirectoryInput;
    }

    public Map<String, String> getValueChoicesLabelsForInput(String inputId) {
        return valueChoicesLabels.get(inputId);
    }
}
