package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Tristan Glatard tristan.glatard@creatis.insa-lyon.fr
 */
public enum Module {
    PROCESSING("Processing"),
    DATA("Data"),
    ADVANCED_DATA("AdvancedData"),
    MANAGEMENT("Management"),
    COMMERCIAL("Commercial");

    private String label;

    Module(String label) {
        this.label = label;
    }

    @JsonCreator
    public static Module fromLabel(String label) {
        for (Module module : values()) {
            if (label.equals(module.label)) {
                return module;
            }
        }
        throw new IllegalArgumentException("Unknown module : " + label);
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
