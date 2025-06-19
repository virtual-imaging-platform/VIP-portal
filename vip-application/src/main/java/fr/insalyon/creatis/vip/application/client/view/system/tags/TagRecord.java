package fr.insalyon.creatis.vip.application.client.view.system.tags;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.bean.Tag;

public class TagRecord extends ListGridRecord {

    public TagRecord(Tag tag) {
        this(tag.getKey(), tag.getValue(), tag.getType().toString(), tag.getApplication(), tag.getVersion(), tag.isVisible(), tag.isBoutiques());
    }

    public TagRecord(String key, String value, String type, String application, String version, boolean visible, boolean boutiques) {
        setAttribute("key", key);
        setAttribute("value", value);
        setAttribute("type", type);
        setAttribute("application", application);
        setAttribute("version", version);
        setAttribute("visible", visible);
        setAttribute("boutiques", boutiques);
    }
}
