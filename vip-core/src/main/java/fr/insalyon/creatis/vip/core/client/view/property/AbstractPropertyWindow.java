package fr.insalyon.creatis.vip.core.client.view.property;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractPropertyWindow extends Window {

    protected ListGrid grid;

    public AbstractPropertyWindow(String title, int width, int height) {

        this.setTitle(title);
        this.setCanDragReposition(true);
        this.setCanDragResize(true);
        this.setWidth(width);
        this.setHeight(height);
        this.centerInPage();

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");

        grid.setFields(propertyField, valueField);
        this.addItem(grid);
    }
}
