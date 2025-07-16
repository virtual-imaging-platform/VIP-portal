package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class Descriptor implements IsSerializable {

    private List<Source> sources;
    private String description;

    public Descriptor(List<Source> sources, String description) {
        this.sources = sources;
        this.description = description;
    }

    public Descriptor() {
        
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public List<Source> getSources() {
        return sources;
    }
    
      public String getDescription() {
        return description;
    }

}
