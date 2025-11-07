package fr.insalyon.creatis.vip.application.client.view.system.tags;

import java.util.Set;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.bean.Tag;

public class TagRecord extends ListGridRecord {

    public TagRecord(Tag tag, Set<String> appVersions) {
        this(tag.getKey(), tag.getValue(), tag.getType().toString(), appVersions, tag.isVisible(), tag.isBoutiques());
    }

    public TagRecord(String key, String value, String type, Set<String> appVersions, boolean visible, boolean boutiques) {
        setAttribute("key", key);
        setAttribute("value", value);
        setAttribute("type", type);
        setAttribute("appVersions", appVersions);
        setAttribute("visible", visible);
        setAttribute("boutiques", boutiques);
    }
}
