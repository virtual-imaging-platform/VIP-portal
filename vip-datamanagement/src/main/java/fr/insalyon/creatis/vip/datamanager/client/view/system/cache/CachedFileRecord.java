package fr.insalyon.creatis.vip.datamanager.client.view.system.cache;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class CachedFileRecord extends ListGridRecord {

    public CachedFileRecord() {
    }
 
    public CachedFileRecord(String path, String name, String size, 
            int frequency, Date lastUsage) {
        
        setAttribute("path", path);
        setAttribute("name", name);
        setAttribute("size", size);
        setAttribute("frequency", frequency);
        setAttribute("date", lastUsage);
    }
    
    public String getPath() {
        return getAttributeAsString("path");
    }
    
    public String getName() {
        return getAttributeAsString("name");
    }
    
    public String getSize() {
        return getAttributeAsString("size");
    }
    
    public int getFrequency() {
        return getAttributeAsInt("frequency");
    }
    
    public Date getLastUsage() {
        return getAttributeAsDate("date");
    }
}
