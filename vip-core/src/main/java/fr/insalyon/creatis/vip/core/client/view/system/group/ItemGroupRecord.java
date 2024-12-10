package fr.insalyon.creatis.vip.core.client.view.system.group;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ItemGroupRecord extends ListGridRecord {

    public ItemGroupRecord (String name) {
        setAttribute("item", name);
    }
}
