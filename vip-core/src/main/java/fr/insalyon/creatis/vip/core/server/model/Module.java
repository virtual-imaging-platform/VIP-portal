package fr.insalyon.creatis.vip.core.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
