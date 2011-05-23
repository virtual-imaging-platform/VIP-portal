/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.view.physicalParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.bean.PhysicalProperty;
import fr.insalyon.creatis.vip.portal.client.bean.Tissue;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueService;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueServiceAsync;
import java.util.List;

/**
 *
 * @author glatard
 */
public class PropertiesPanel extends Panel {

    public static PropertiesPanel instance = null;
    private GroupingStore propertyStore;
    private String tissueName;

    private int propId;
    private String propType;
    private String propAuthor;
    private String propDate;
    private String propComment;
    
    /**
     * Pattern Singleton: Gets an unique instance of ParamPanel
     *
     * @return Unique instance of ParamPanel
     */
    public static PropertiesPanel getInstance(String tissue) {
        if (instance == null) {

            instance = new PropertiesPanel("Tissues", tissue);
        } else {
            instance.setTissue(tissue);
            instance.reloadData();
        }

        return instance;
    }
    private Menu menu;

    public void setTissue(String tissue) {
        this.tissueName = tissue;
    }

    private PropertiesPanel(String title, String tissue) {

        setBorder(false);
        setPaddings(15);
        this.tissueName = tissue;



        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("property-id"),
                    new StringFieldDef("property-type"),
                    new StringFieldDef("author"),
                    new StringFieldDef("date"),
                    new StringFieldDef("comment")
                });
        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy proxy = new MemoryProxy(new Object[][]{new Object[]{"un", "deux", "trois", "quatre"}, new Object[]{"one", "two", "three", "four"}});

        propertyStore = new GroupingStore(proxy, reader);
        propertyStore.setSortInfo(new SortState("author", SortDir.ASC));
        propertyStore.setGroupField("property-type");
        propertyStore.load();


        ColumnConfig[] columns = new ColumnConfig[]{
            new ColumnConfig("Id", "property-id", 100),
            new ColumnConfig("Property", "property-type", 100, true, null, "author"),
            new ColumnConfig("Author", "author", 100, true, null, "author"),
            new ColumnConfig("Date", "date", 50),
            new ColumnConfig("Comment", "comment", 100)
        };

        ColumnModel model = new ColumnModel(columns);

        GridPanel propertyGrid = new GridPanel();
        propertyGrid.setStore(propertyStore);
        propertyGrid.setColumnModel(model);
        propertyGrid.setFrame(true);
        propertyGrid.setStripeRows(true);
        propertyGrid.setTitle("Physical properties");
        propertyGrid.setHeight(1000);
        propertyGrid.setWidth(800);



        GroupingView gridView = new GroupingView();
        gridView.setForceFit(true);
        gridView.setGroupTextTpl("{[values.rs[0].data[\"property-type\"]]}");

        propertyGrid.setView(gridView);
        propertyGrid.setCollapsible(true);
        propertyGrid.setAnimCollapse(false);

        loadPhysicalProperties();

        propertyGrid.addGridRowListener(new GridRowListener() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                propId = Integer.parseInt(record.getAsString("property-id"));
                propType = record.getAsString("property-type");
                propAuthor = record.getAsString("author");
                propComment = record.getAsString("comment");
                propDate = record.getAsString("date");
                
                showMenu(e);
            }

            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                onRowClick(grid, rowIndex, e);
            }

            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                onRowClick(grid, rowIndex, e);
            }
        });



        Toolbar toolbar = new Toolbar();
        ToolbarButton newProperty = new ToolbarButton("New physical property", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                PropertyPanel.getInstance().setTissueName(tissueName);
                //PropertyPanel.getInstance().setNewProperty(true);
//                Layout.getInstance().setCenterPanel(PropertyPanel.getInstance());
            }
        });
        toolbar.addButton(newProperty);
        this.setTopToolbar(toolbar);

        this.add(propertyGrid);


    }

    private void reloadData() {
        loadPhysicalProperties();
    }

    public void showMenu(EventObject e) {
        if (menu == null) {
            menu = new Menu();
            Item view = new Item("View property", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                         PropertyPanel.getInstance().setNewProperty(false);
                         PropertyPanel.getInstance().setTissueName(tissueName);
                         PropertyPanel.getInstance().setProperty(propId,propType,propAuthor,propComment,propDate);
//                         Layout.getInstance().setCenterPanel(PropertyPanel.getInstance());
                }
            });
            menu.addItem(view);

            Item delete = new Item("Delete property",new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                         TissueServiceAsync ts = TissueService.Util.getInstance();
                         final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            MessageBox.alert("Cannot delete property");
                        }

                        public void onSuccess(Void result) {
                            MessageBox.alert("Property was successfully deleted");
                            loadPhysicalProperties();
                        }
                    };
                    PhysicalProperty p = new PhysicalProperty(propId);
                    ts.deletePhysicalProperty(p, callback);
                }
            });
            menu.addItem(delete);
        }
        menu.showAt(e.getXY());
    }

    public void loadPhysicalProperties() {
        TissueServiceAsync ts = TissueService.Util.getInstance();
        final AsyncCallback<List<PhysicalProperty>> callback = new AsyncCallback<List<PhysicalProperty>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error physical property list for tissue " + tissueName + "\n" + caught.getMessage());
            }

            public void onSuccess(List<PhysicalProperty> result) {
                Object[][] data = new Object[result.size()][4];
                for (int i = 0; i < result.size(); i++) {

                    data[i][0] = "" + ((PhysicalProperty) result.get(i)).getId();
                    data[i][1] = ((PhysicalProperty) result.get(i)).getType();
                    data[i][2] = ((PhysicalProperty) result.get(i)).getAuthor();
                    data[i][3] = ((PhysicalProperty) result.get(i)).getDate().toString();
                    data[i][4] = ((PhysicalProperty) result.get(i)).getComment();
                }
                MemoryProxy proxy = new MemoryProxy(data);
                propertyStore.setDataProxy(proxy);
                propertyStore.load();

                propertyStore.commitChanges();
            }
        };
        ts.getPhysicalProperties(new Tissue(tissueName), callback);
    }
}
