package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Tristan Glatard
 */
public class SemanticRecord extends ListGridRecord {
    
    public SemanticRecord() {
    }
    
    public SemanticRecord(String subject, String predicate, String object) {
        setAttribute("icon", "icon-file");
        setAttribute("subject", subject);
        setAttribute("predicate", predicate);
        setAttribute("object", object);
    }
 
}
