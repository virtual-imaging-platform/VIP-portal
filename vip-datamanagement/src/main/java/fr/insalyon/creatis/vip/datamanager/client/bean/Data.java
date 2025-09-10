package fr.insalyon.creatis.vip.datamanager.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class Data implements IsSerializable {

    public enum Type {

        folder, folderSync, file, fileSync
    };
    private String name;
    private Type type;
    private long length;
    private String modificationDate;
    private List<String> replicas;
    private String permissions;

    public Data() {
    }

    public Data(String name, Type type, String permissions) {
        this(name, type, 0, "", new ArrayList<String>(), permissions);
    }

    public Data(String name, Type type, long length, String modificationDate,
            List<String> replicas, String permissions) {

        this.name = name;
        this.type = type;
        this.length = length;
        this.modificationDate = modificationDate;
        this.replicas = replicas;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public long getLength() {
        return length;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public String getPermissions() {
        return permissions;
    }

    public List<String> getReplicas() {
        return replicas;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
