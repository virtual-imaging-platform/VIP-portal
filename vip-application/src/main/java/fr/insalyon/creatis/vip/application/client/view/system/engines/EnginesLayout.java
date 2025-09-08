package fr.insalyon.creatis.vip.application.client.view.system.engines;

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
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EnginesLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public EnginesLayout() {

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

        LabelButton addButton = new LabelButton("Add Engine", CoreConstants.ICON_ADD);
        addButton.setWidth(150);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageEnginesTab enginesTab = (ManageEnginesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_ENGINE);
                enginesTab.setEngine(null, null, null);
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
                            edit(rollOverRecord.getAttribute("name"),
                                    rollOverRecord.getAttribute("endpoint"),
                                    rollOverRecord.getAttribute("status"));
                        }
                    });
                    ImgButton deleteImg = WidgetUtil.getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String name = rollOverRecord.getAttribute("name");
                            SC.ask("Do you really want to remove the engine \""
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
        grid.setFields(new ListGridField("name", "Engine Name"),
                new ListGridField("endpoint", "End-Point"),
                new ListGridField("status", "Status"));
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                edit(event.getRecord().getAttribute("name"),
                        event.getRecord().getAttribute("endpoint"),
                        event.getRecord().getAttribute("status"));
            }
        });
        this.addMember(grid);
    }

    public void loadData() {

        final AsyncCallback<List<Engine>> callback = new AsyncCallback<List<Engine>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of engines:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Engine> result) {
                modal.hide();
                List<EngineRecord> dataList = new ArrayList<EngineRecord>();

                for (Engine engine : result) {
                    dataList.add(new EngineRecord(engine.getName(), engine.getEndpoint(), engine.getStatus()));
                }
                grid.setData(dataList.toArray(new EngineRecord[]{}));
            }
        };
        modal.show("Loading engines...", true);
        ApplicationService.Util.getInstance().getEngines(callback);
    }

    private void remove(String name) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove engine:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The engine was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing engine '" + name + "'...", true);
        ApplicationService.Util.getInstance().removeEngine(name, callback);
    }

    private void edit(String name, String endpoint, String status) {

        ManageEnginesTab appsTab = (ManageEnginesTab) Layout.getInstance().
                getTab(ApplicationConstants.TAB_MANAGE_ENGINE);
        appsTab.setEngine(name, endpoint, status);
    }
}
