package fr.insalyon.creatis.vip.application.client.view.system.resources;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResourceLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public ResourceLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureToolStrip();
        configureGrid();
        modal = new ModalWindow(grid);

        loadData();
    }

    private void configureToolStrip() {

        ToolstripLayout toolstrip = new ToolstripLayout();

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        LabelButton addButton = new LabelButton("Add Resource", CoreConstants.ICON_ADD);
        addButton.setWidth(150);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageResourcesTab resourceTab = (ManageResourcesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_RESOURCE);
                        resourceTab.setResource(null, false, null, null, null, null);
            }
        });
        toolstrip.addMember(addButton);

        LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
        refreshButton.setWidth(150);
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        toolstrip.addMember(refreshButton);

        this.addMember(toolstrip);
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

                    ImgButton loadImg = WidgetUtil.getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord);
                        }
                    });
                    ImgButton deleteImg = WidgetUtil.getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String name = rollOverRecord.getAttribute("name");
                            SC.ask("Do you really want to remove this resource \""
                                    + name + "\"?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value) {
                                        remove(name);
                                    }
                                }
                            });
                        }
                    });
                    rollOverCanvas.addMember(loadImg);
                    rollOverCanvas.addMember(deleteImg);
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
        grid.setFields(
                new ListGridField("name", "Name"),
                new ListGridField("status", "Active"),
                new ListGridField("type", "Type"),
                new ListGridField("configuration", "Configuration"),
                new ListGridField("engines", "Engines"),
                new ListGridField("groupsLabel", "Groups Resources"));
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                edit(event.getRecord());
            }
        });
        this.addMember(grid);
    }

    public void loadData() {
        final AsyncCallback<List<Resource>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of resources:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Resource> result) {
                modal.hide();
                List<ResourceRecord> dataList = new ArrayList<>();

                for (Resource resource : result) {
                    dataList.add(new ResourceRecord(resource));
                }
                grid.setData(dataList.toArray(new ResourceRecord[]{}));
            }
        };
        modal.show("Loading resources...", true);
        ApplicationService.Util.getInstance().getResources(callback);
    }

    private void remove(String name) {
        final Resource resourceToDelete = new Resource(name);
        final AsyncCallback<String> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove engine:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The engine was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing resource '" + name + "'...", true);
        ApplicationService.Util.getInstance().removeResource(resourceToDelete, callback);
    }

    private void edit(ListGridRecord record) {
        ManageResourcesTab appsTab = (ManageResourcesTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MANAGE_RESOURCE);
        List<String> keys = Arrays.asList(record.getAttributeAsStringArray("groupsLabel"));
        List<String> values = Arrays.asList(record.getAttributeAsStringArray("groups"));

        Map<String, String> groups = IntStream.range(0, Math.min(keys.size(), values.size()))
            .boxed()
            .collect(Collectors.toMap(keys::get, values::get));

        appsTab.setResource(
            record.getAttribute("name"), 
            record.getAttributeAsBoolean("status"), 
            record.getAttribute("type"), 
            record.getAttribute("configuration"),
            record.getAttributeAsStringArray("engines"),
            groups
        );
    }
}
