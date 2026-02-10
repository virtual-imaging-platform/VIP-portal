package fr.insalyon.creatis.vip.datamanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author abonnet
 */
public class ExternalPlatform {

    public enum Type {
        GIRDER,
        SHANOIR,
        SRM
    }

    private String identifier;
    private Type type;
    private String description;
    private String url;
    private String uploadUrl;
    private String keycloakClientId; // TODO : will not be necessary after VIP version 2.7
    private String refreshTokenUrl;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonIgnore
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getKeycloakClientId() {
        return keycloakClientId;
    }

    public void setKeycloakClientId(String keycloakClientId) {
        this.keycloakClientId = keycloakClientId;
    }

    public String getRefreshTokenUrl() {
        return refreshTokenUrl;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }
}
