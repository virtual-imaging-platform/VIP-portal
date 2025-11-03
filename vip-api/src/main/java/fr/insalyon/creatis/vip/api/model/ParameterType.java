package fr.insalyon.creatis.vip.api.model;

import fr.insalyon.creatis.boutiques.model.Input;

public enum ParameterType {
    // TODO improve (de)serialisation (enums should be in capital)
    File, String, Boolean, Int64, Double, List;

    public static ParameterType fromVipType(String vipType) {
        return vipType.equalsIgnoreCase("URI") ? File : String;
    }

    public static ParameterType fromBoutiquesInput(Input boutiquesInput) {
        if (boutiquesInput.getList() != null && boutiquesInput.getList()) {
            return List;
        }
        return switch (boutiquesInput.getType()) {
            case FILE -> File;
            case STRING -> String;
            case FLAG -> Boolean;
            case NUMBER -> (boutiquesInput.getInteger() != null && boutiquesInput.getInteger()) ? Int64 : Double;
        };
    }
}
