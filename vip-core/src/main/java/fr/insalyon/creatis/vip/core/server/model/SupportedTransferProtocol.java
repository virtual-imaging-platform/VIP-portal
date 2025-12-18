package fr.insalyon.creatis.vip.core.server.model;

import com.fasterxml.jackson.annotation.*;

public enum SupportedTransferProtocol {

    HTTP,
    HTTPS,
    FTP,
    SFTP,
    FTPS,
    SCP,
    WEBDAV;

    @JsonCreator
    public static SupportedTransferProtocol forValue(String value) {
        return SupportedTransferProtocol.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
