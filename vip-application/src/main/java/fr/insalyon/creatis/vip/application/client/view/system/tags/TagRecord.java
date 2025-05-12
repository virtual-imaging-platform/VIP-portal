package fr.insalyon.creatis.vip.application.client.view.system.tags;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.bean.Tag;

public class TagRecord extends ListGridRecord {

    public TagRecord(Tag tag) {
        this(tag.getName(), tag.getApplication(), tag.getVersion(), tag.isVisible(), tag.isBoutiques());
    }

    public TagRecord(String name, String application, String version, boolean visible, boolean boutiques) {
        setAttribute("name", name);
        setAttribute("application", application);
        setAttribute("version", version);
        setAttribute("visible", visible);
        setAttribute("boutiques", boutiques);
    }
}
