package fr.insalyon.creatis.vip.application.client.view.system.tags;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TagRecord extends ListGridRecord {

    public TagRecord(String name) {
        setAttribute("name", name);
    }
}
