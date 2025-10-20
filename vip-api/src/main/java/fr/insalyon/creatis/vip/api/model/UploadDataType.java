package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.*;

public enum UploadDataType {

    FILE,
    ARCHIVE;

    @JsonCreator
    public static UploadDataType forValue(String value) {
        return UploadDataType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
