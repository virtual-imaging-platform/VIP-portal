package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class InputRecord extends ListGridRecord {

    public InputRecord() {
    }

    public InputRecord(String application, String name, String values) {
        setAttribute("application", application);
        setAttribute("name", name);
        setAttribute("values", values);
    }
}
