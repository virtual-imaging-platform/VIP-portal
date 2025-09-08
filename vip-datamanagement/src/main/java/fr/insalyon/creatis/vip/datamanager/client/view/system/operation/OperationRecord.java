package fr.insalyon.creatis.vip.datamanager.client.view.system.operation;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class OperationRecord extends ListGridRecord {

    public OperationRecord() {
    }
    
    public OperationRecord(String id, String type, String status, String source, 
            String destination, Date date, String owner) {
        
        if (type.equals("Download_Files")) {
            type = "Download";
        }
        setAttribute("typeIcon", "datamanager/icon-" + type.toLowerCase());
        setAttribute("statusIcon", "datamanager/operation/icon-" + status.toLowerCase());
        setAttribute("operationId", id);
        setAttribute("type", type);
        setAttribute("status", status);
        setAttribute("name", source);
        setAttribute("destination", destination);
        setAttribute("date", date);
        setAttribute("owner", owner);
    }
    
    public String getId() {
        return getAttributeAsString("operationId");
    }
    
    public String getType() {
        return getAttributeAsString("type");
    }
    
    public String getStatus() {
        return getAttributeAsString("status");
    }
    
    public String getSource() {
        return getAttributeAsString("name");
    }
    
    public String getDestination() {
        return getAttributeAsString("destination");
    }
    
    public String getDate() {
        return getAttributeAsString("date");
    }
}
