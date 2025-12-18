package fr.insalyon.creatis.vip.application.client.view.system.applications.version;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
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
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;

public class VersionsLayout extends VLayout {

    private String applicationName;
    private Label applicationLabel;
    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public VersionsLayout() {
        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);
    }

    private void configureActions() {

        ToolstripLayout toolstrip = new ToolstripLayout();

        applicationLabel = WidgetUtil.getLabel("", 24);
        applicationLabel.setWidth(250);
        toolstrip.addMember(applicationLabel);
        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        LabelButton addButton = new LabelButton("Add Version", CoreConstants.ICON_ADD);
        addButton.setWidth(150);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                appsTab.setVersion(null, null, null, null, true, null, null);
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
                            final String version = rollOverRecord.getAttribute("version");
                            SC.ask("Do you really want to remove the application version \""
                                    + version + "\"?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value) {
                                        remove(version);
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

        ListGridField isVisibleField = new ListGridField("visible", "Visible");
        isVisibleField.setType(ListGridFieldType.BOOLEAN);

        grid.setFields(
                isVisibleField,
                new ListGridField("version", "Version"),
                new ListGridField("resources", "Resources"));
        grid.setSortField("version");
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
        final AsyncCallback<List<AppVersion>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of applications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<AppVersion> result) {
                modal.hide();
                List<VersionRecord> dataList = new ArrayList<VersionRecord>();

                for (AppVersion version : result) {
                    dataList.add(new VersionRecord(
                        version.getVersion(), version.getDescriptor(),
                        version.getDoi(), version.isVisible(), version.getSource(),
                        version.getSettings(), version.getResourcesNames()));
                }
                grid.setData(dataList.toArray(new VersionRecord[]{}));
            }
        };
        modal.show("Loading versions...", true);
        ApplicationService.Util.getInstance().getVersions(applicationName, callback);
    }

    public void setApplication(String applicationName) {
        this.applicationName = applicationName;
        this.applicationLabel.setContents("<b>Application:</b> " + applicationName);
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void edit(ListGridRecord record) {
        ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
            getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);

        appsTab.setVersion(
            record.getAttribute("version"),
            record.getAttribute("descriptor"),
            record.getAttribute("doi"),
            record.getAttributeAsMap("settings"),
            record.getAttributeAsBoolean("visible"),
            record.getAttribute("source"),
            record.getAttributeAsStringArray("resources"));
    }

    private void remove(String version) {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove version:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The version was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing version '" + version + "'...", true);
        ApplicationService.Util.getInstance().removeVersion(applicationName, version, callback);
    }
}
