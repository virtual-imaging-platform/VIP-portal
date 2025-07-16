package fr.insalyon.creatis.vip.core.client.view.property;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class PropertyRecord extends ListGridRecord {

    public PropertyRecord() {
    }

    public PropertyRecord(String property, String value) {
        
        setAttribute("property", property);
        setAttribute("value", value);
    }

    public String getProperty() {
        return getAttributeAsString("property");
    }

    public String getValue() {
        return getAttributeAsString("value");
    }
}
