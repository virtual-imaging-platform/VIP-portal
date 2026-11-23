package fr.insalyon.creatis.vip.core.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.server.inter.DataViews;

@JsonView(DataViews.User.class)
public class Group implements IsSerializable {

    @JsonKey
    private String name;
    private boolean publicGroup;
    private GroupType type;
    private boolean auto;

    public Group() { }

    public Group(String name, boolean publicGroup, String type, boolean auto) {
        this(name, publicGroup, GroupType.fromString(type), auto);
    }

    public Group(String name, boolean publicGroup, GroupType type) {
        this(name, publicGroup, type, false);
    }

    public Group(String name, boolean publicGroup, GroupType type, boolean auto) {
        this.name = name;
        this.publicGroup = publicGroup;
        this.type = type;
        this.auto = auto;
    }

    public Group(String name) {
        this(name, true, GroupType.getDefault(), false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublicGroup() {
        return publicGroup;
    }

    public void setPublicGroup(boolean publicGroup) {
        this.publicGroup = publicGroup;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public void setAuto(boolean isAuto) {
        this.auto = isAuto;
    }

    public boolean isAuto() {
        return auto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Group other = (Group) obj;
        return publicGroup == other.publicGroup &&
               auto == other.auto &&
               type == other.type &&
               Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publicGroup, type, auto);
    }
}
