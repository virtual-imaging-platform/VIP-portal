package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Tag implements IsSerializable {

    private String name;
    private String application;
    private String version;
    private boolean visible;
    private boolean boutiques;

    public Tag() {}

    public Tag(Tag other) {
        this(other.getName(), other.getApplication(), other.getVersion(), other.isVisible(), other.isBoutiques());
    }

    public Tag(String name, String application, String version) {
        this(name, application, version, false, false);
    }

    public Tag(String name, boolean visible, boolean boutiques) {
        this(name, null, null, visible, boutiques);
    }

    public Tag(String name, String application, String version, boolean visible, boolean boutiques) {
        this.name = name;
        this.application = application;
        this.version = version;
        this.visible = visible;
        this.boutiques = boutiques;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
