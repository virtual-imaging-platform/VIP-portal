package fr.insalyon.creatis.vip.application.models;

public enum ResourceType {
    LOCAL,
    BATCH,
    KUBERNETES,
    DIRAC;

    public static ResourceType fromString(String groupeType) {
        for (ResourceType possibility : values()) {
            if (possibility.toString().equalsIgnoreCase(groupeType)) {
                return possibility;
            }
        }
        return getDefault();
    }

    public static String[] getValues() {
        String[] valuesAsString = new String[values().length];

        for (int i = 0; i < values().length; i++) {
            valuesAsString[i] = values()[i].toString();
        }
        return valuesAsString;
    }

    public static ResourceType getDefault() {
        return LOCAL;
    }
}