package fr.insalyon.creatis.vip.core.client.bean;

public enum GroupType {
    APPLICATION,
    RESOURCE;

    public static GroupType fromString(String groupeType) {
        for (GroupType possibility : values()) {
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

    public static GroupType getDefault() {
        return APPLICATION;
    }
}
