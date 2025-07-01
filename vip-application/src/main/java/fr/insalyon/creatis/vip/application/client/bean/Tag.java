package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Tag implements IsSerializable {

    public enum ValueType {
        STRING,
        BOOLEAN;
    }

    private String key;
    private String value;
    private ValueType type;
    private String application;
    private String version;
    private boolean visible;
    private boolean boutiques;

    public Tag() {}

    public Tag(Tag other) {
        this(
            other.getKey(), 
            other.getValue(), 
            other.getType(), 
            other.getApplication(), 
            other.getVersion(), 
            other.isVisible(), 
            other.isBoutiques());
    }

    public Tag(String key, String value, ValueType type, String application, String version, boolean visible, boolean boutiques) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.application = application;
        this.version = version;
        this.visible = visible;
        this.boutiques = boutiques;
    }

    public Tag(String key, String value, ValueType type, AppVersion appVersion, boolean visible, boolean boutiques) {
        this(key, value, type, appVersion.getApplicationName(), appVersion.getVersion(), visible, boutiques);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tag tag = (Tag) obj;
        
        return 
            key.equals(tag.getKey()) &&
            value.equals(tag.getValue()) &&
            type.equals(tag.getType()) &&
            application.equals(tag.getApplication()) &&
            version.equals(tag.getVersion()) &&
            visible == tag.isVisible() &&
            boutiques == tag.isBoutiques();
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }


    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isBoutiques() {
        return boutiques;
    }

    public void setBoutiques(boolean boutiques) {
        this.boutiques = boutiques;
    }
}
