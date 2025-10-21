package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import fr.insalyon.creatis.vip.core.client.view.property.AbstractPropertyWindow;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import fr.insalyon.creatis.vip.datamanager.models.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class DataPropertiesWindow extends AbstractPropertyWindow {

    public DataPropertiesWindow(String baseDir, DataRecord record) {

        super("Properties for " + baseDir + "/" + record.getName(), 550, 240);

        List<PropertyRecord> data = new ArrayList<PropertyRecord>();
        data.add(new PropertyRecord("Folder", baseDir));
        data.add(new PropertyRecord("Name", record.getName()));
        data.add(new PropertyRecord("Type", record.getType().name()));
        
        if (record.getType() == Data.Type.file) {
            data.add(new PropertyRecord("Size", record.getLength()));
            data.add(new PropertyRecord("Modification Date", record.getModificationDate()));
        }
        
        data.add(new PropertyRecord("Permissions", record.getPermissions()));
        
        if (record.getType() == Data.Type.file) {
            data.add(new PropertyRecord("Replicas", record.getReplicas()));
        }

        grid.setData(data.toArray(new PropertyRecord[]{}));
    }
}
