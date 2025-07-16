package fr.insalyon.creatis.vip.application.client.view.system.engines;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EngineRecord extends ListGridRecord {

    public EngineRecord(String name, String endpoint, String status) {

        setAttribute("name", name);
        setAttribute("endpoint", endpoint);
        setAttribute("status", status);
    }
}
