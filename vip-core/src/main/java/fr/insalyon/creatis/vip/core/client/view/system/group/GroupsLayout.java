package fr.insalyon.creatis.vip.core.client.view.system.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class GroupsLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public GroupsLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        
        configureGrid();
        this.addMember(new GroupsToolStrip());
        this.addMember(grid);

        modal = new ModalWindow(grid);

        loadData();
    }

    private void configureGrid() {
        grid = new ListGrid() {

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    rollOverCanvas.addMember(FieldUtil.getImgButton(
                            CoreConstants.ICON_EDIT, "Edit", new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord.getAttribute("name"),
                                    rollOverRecord.getAttributeAsBoolean("isPublic"),
                                    rollOverRecord.getAttributeAsString("type"),
                                    rollOverRecord.getAttributeAsBoolean("auto"));
                        }
                    }));

                    rollOverCanvas.addMember(FieldUtil.getImgButton(
                            CoreConstants.ICON_DELETE, "Delete", new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            remove(rollOverRecord.getAttribute("name"));
                        }
                    }));
                }
                return rollOverCanvas;
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField isPublicField = new ListGridField("isPublic", "Public");
        isPublicField.setType(ListGridFieldType.BOOLEAN);

        ListGridField typeField = new ListGridField("type", "GroupType");
        typeField.setType(ListGridFieldType.TEXT);

        ListGridField autoField = new ListGridField("auto", "Auto");
        autoField.setType(ListGridFieldType.BOOLEAN);
        
        grid.setFields(isPublicField, typeField, autoField, new ListGridField("name", "Group Name"));
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                edit(event.getRecord().getAttribute("name"),
                        event.getRecord().getAttributeAsBoolean("isPublic"),
                        event.getRecord().getAttributeAsString("type"),
                        event.getRecord().getAttributeAsBoolean("auto"));
            }
        });
    }

    public void loadData() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<List<Group>>() {

            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Unable to get groups list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {
                List<GroupRecord> dataList = new ArrayList<GroupRecord>();

                for (Group group : result) {
                    dataList.add(new GroupRecord(group));
                }
                grid.setData(dataList.toArray(new GroupRecord[]{}));
            }
        };
        service.getGroups(callback);
    }

    private void remove(final String name) {
        SC.ask("Do you really want to remove \"" + name + "\" group?", new BooleanCallback() {
            @Override
            public void execute(Boolean value) {
                if (value) {
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            SC.warn("Unable to remove group:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            SC.say("The group was successfully removed!");
                            loadData();
                        }
                    };
                    service.removeGroup(name, callback);
                }
            }
        });
    }

    private void edit(String name, boolean isPublic, String type, boolean auto) {

        ((ManageGroupsTab) Layout.getInstance().getTab(
                CoreConstants.TAB_MANAGE_GROUPS)).setGroup(name, isPublic, type, auto);
    }
}
