package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;

/**
 *
 * @author Rafael Silva
 */
public class DataRecord extends ListGridRecord {

    public DataRecord() {
    }

    public DataRecord(Data.Type type, String name) {
        this(type, name, 0, "", "", "");
    }

    public DataRecord(Data.Type type, String name, long length, String date,
            String replicas, String permissions) {
        if (name.equals("Trash")) {
            setAttribute("icon", "datamanager/icon-trash");
        } else {
            setAttribute("icon", "datamanager/icon-" + type.name());
        }
        setAttribute("name", name);
        setAttribute("length", length);
        setAttribute("modificationDate", date);
        setAttribute("type", type.name());
        setAttribute("replicas", replicas);
        setAttribute("permissions", permissions);
    }

    public Data.Type getType() {
        return Data.Type.valueOf(getAttributeAsString("type"));
    }

    public String getName() {
        return getAttributeAsString("name");
    }

    public String getLength() {
        return getAttributeAsString("length");
    }

    public String getModificationDate() {
        return getAttributeAsString("modificationDate");
    }

    public String getReplicas() {
        return getAttributeAsString("replicas");
    }

    public String getPermissions() {
        return getAttributeAsString("permissions");
    }
}
