package fr.insalyon.creatis.vip.core.client.view.application;

import com.smartgwt.client.widgets.tile.TileRecord;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationTileRecord extends TileRecord {

    public ApplicationTileRecord() {}

    public ApplicationTileRecord(String name, String icon) {
        this(name, null, icon);
    }

    public ApplicationTileRecord(String name, String version, String icon) {
        if (version != null && !version.isEmpty()) {
            setAttribute("commonName", name + " " + version);
        } else {
            setAttribute("commonName", name);
        }
        setAttribute("picture", icon);
        setAttribute("applicationName", name);
        setAttribute("applicationVersion", version);
    }

    public String getName() {
        return getAttributeAsString("commonName");
    }

    public String getIcon() {
        return getAttributeAsString("picture");
    }

    public String getApplicationName() {
        return getAttributeAsString("applicationName");
    }

    public String getApplicationVersion() {
        return getAttributeAsString("applicationVersion");
    }
}
