package fr.insalyon.creatis.vip.application.models;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Silva
 */
public class InOutData implements IsSerializable {

    private String path;
    private String processor;
    private String type;

    public InOutData() {
    }

    public InOutData(String path, String processor, String type) {
        this.path = path;
        this.processor = processor;
        this.type = type;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public String getProcessor() {
        return processor;
    }
}
