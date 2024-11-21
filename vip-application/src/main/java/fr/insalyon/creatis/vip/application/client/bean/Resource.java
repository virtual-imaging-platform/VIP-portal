package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Resource implements IsSerializable {

    private String name;
    private boolean visible;
    private boolean status;
    private ResourceType type;
    private String configuration;

    public Resource(String name, boolean visible, boolean status, ResourceType type, String configuration) {
        this.name = name;
        this.visible = visible;
        this.status = status;
        this.type = type;
        this.configuration = configuration;
    }

    public Resource(String name, boolean visible, boolean status, String type, String configuration) {
        this(name, visible, status, ResourceType.fromString(type), configuration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
