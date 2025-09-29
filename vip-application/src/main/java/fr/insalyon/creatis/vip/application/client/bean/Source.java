package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Sorina Camarasu
 */
public class Source implements IsSerializable {

    private String name, type, userLevel, description, defaultValue, vipTypeRestriction, prettyName ;

    private boolean optional;

    public Source() {
    }

    public Source(String name, String type, String userLevel, String description, String optional, String defaultValue, String vipTypeRestriction, String prettyName) {
        this.name = name;
        this.type = type;
        this.userLevel = userLevel;
        this.description = description;
        this.optional = Boolean.parseBoolean(optional);
        this.defaultValue = defaultValue;
        this.vipTypeRestriction = vipTypeRestriction;
        this.prettyName = prettyName;
    }

    public Source(String name, String type, String userLevel, String description) {
        this(name, type, userLevel, description, "false","","","");
    }
    
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getType() {
        return type;
    }
    
    public String getVipTypeRestriction() {
        return vipTypeRestriction;
    }

    public void setVipTypeRestriction(String vipTypeRestriction) {
        this.vipTypeRestriction = vipTypeRestriction;
    }
    
    public String getPrettyName() {
        return prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }
    
}
