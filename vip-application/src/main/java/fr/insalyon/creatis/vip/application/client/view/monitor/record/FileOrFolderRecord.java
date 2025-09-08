package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class FileOrFolderRecord extends ListGridRecord {
    
    public FileOrFolderRecord() {
    }
    
    public FileOrFolderRecord(String name, String type, String baseDir) {
        this(name, type, baseDir, "", "");
    }
    
    public FileOrFolderRecord(String name, String type, String baseDir, String size, String date) {
        
        setAttribute("icon", "icon-" + type);
        setAttribute("name", name);
        setAttribute("size", size);
        setAttribute("lastModified", date);
        setAttribute("baseDir", baseDir);
    }
}
