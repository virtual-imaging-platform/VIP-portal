package fr.insalyon.creatis.vip.application.client.view.system.applications.app;

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
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.CoreModule;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApplicationsLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    private boolean onlyPublicApps;

    public ApplicationsLayout(boolean onlyPublicApps) {

        setWidth100();
        setHeight100();
        this.setOverflow(Overflow.AUTO);

        this.onlyPublicApps = onlyPublicApps;

        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);
        loadData();
    }

    private void configureActions() {

        ToolstripLayout toolstrip = new ToolstripLayout();

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        if( ! onlyPublicApps) {
            LabelButton addButton = new LabelButton("Add Application", CoreConstants.ICON_ADD);
            addButton.setWidth(150);
            addButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                            getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                    appsTab.setApplication(null, null, null, null, null);
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
    }

    private void configureGrid() {
        ListGridField nameField = new ListGridField("name", "Application Name");
        ListGridField groupsField = new ListGridField("groupsLabel", "Groups");
        ListGridField resourceField = new ListGridField("resources", "Resources");
        ListGridField noteField = new ListGridField("note", "Note");
        noteField.setEscapeHTML(true);
        grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if ( ! onlyPublicApps && rollOverCanvas == null) {
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
                            SC.ask("Do you really want to remove the application \""
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
                    if (! CoreModule.user.isDeveloper()) {
                        rollOverCanvas.addMember(deleteImg);
                    }
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
        grid.setWrapCells(true);
        grid.setFixedRecordHeights(false);
        if (onlyPublicApps){
            grid.setFields(nameField);
        } else {
            grid.setFields(
                nameField,
                groupsField,
                resourceField,
                noteField);
        }
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                edit(event.getRecord());
            }
        });
        this.addMember(grid);
    }

    public void loadData() {
        final AsyncCallback<List<Application>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of applications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Application> result) {
                modal.hide();
                List<ApplicationRecord> dataList = new ArrayList<>();

                for (Application app : result) {
                    dataList.add(new ApplicationRecord(app));
                }
                grid.setData(dataList.toArray(new ApplicationRecord[]{}));
            }
        };
        modal.show("Loading applications...", true);
        if (onlyPublicApps) {
            ApplicationService.Util.getInstance().getPublicApplications(callback);
        } else {
            loadDataWithResources();
        }
    }

    public void loadDataWithResources() {
        final AsyncCallback<Map<Application, Set<Resource>>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of applications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Application, Set<Resource>> result) {
                modal.hide();
                List<ApplicationRecord> dataList = new ArrayList<>();

                result.forEach((app, resources) -> {
                    dataList.add(new ApplicationRecord(
                        app,
                        resources.stream().map(Resource::getName).collect(Collectors.toList())
                    ));
                });
                grid.setData(dataList.toArray(new ApplicationRecord[]{}));
            }
        };
        modal.show("Loading applications...", true);

        ApplicationService.Util.getInstance().getManageableApplications(callback);
    }

    private void remove(String name) {
        final AsyncCallback<String> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The application was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing application '" + name + "'...", true);
        ApplicationService.Util.getInstance().remove(name, callback);
    }

    private void edit(ListGridRecord record) {
        ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
        List<String> keys = Arrays.asList(record.getAttributeAsStringArray("groupsLabel"));
        List<String> values = Arrays.asList(record.getAttributeAsStringArray("groups"));

        Map<String, String> groups = IntStream.range(0, Math.min(keys.size(), values.size()))
            .boxed()
            .collect(Collectors.toMap(keys::get, values::get));

        appsTab.loadVersions(record.getAttribute("name"));
        appsTab.setApplication(
            record.getAttribute("name"), 
            record.getAttribute("owner"), 
            record.getAttribute("citation"), 
            record.getAttribute("note"),
            groups
        );
    }
}
