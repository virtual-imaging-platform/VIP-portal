package fr.insalyon.creatis.vip.datamanager.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class DMCachedFile implements IsSerializable {

    private String path;
    private String name;
    private String size;
    private int frequency;
    private Date lastUsage;

    public DMCachedFile() {
    }

    public DMCachedFile(String path, String name, String size, int frequency, Date lastUsage) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.frequency = frequency;
        this.lastUsage = lastUsage;
    }

    public int getFrequency() {
        return frequency;
    }

    public Date getLastUsage() {
        return lastUsage;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getSize() {
        return size;
    }
}
