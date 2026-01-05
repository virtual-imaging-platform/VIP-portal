package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ExpansionMode;
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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.models.SimulationInput;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ExampleInputsLayout extends AbstractInputsLayout {

    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public ExampleInputsLayout(String tabID, String applicationName) {

        super(tabID, applicationName, "Examples", CoreConstants.ICON_EXAMPLE);

        configureGrid();
        modal = new ModalWindow(grid);
        this.addMember(grid);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid() {
            @Override
            protected Canvas getExpansionComponent(ListGridRecord record) {
                Canvas canvas = super.getExpansionComponent(record);
                canvas.setMargin(5);
                return canvas;
            }

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = WidgetUtil.getImgButton("icon-load.png", "Load Input");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            String values = rollOverRecord.getAttribute("values");
                            LaunchTab launchTab = (LaunchTab) Layout.getInstance().getTab(tabID);
                            launchTab.loadInput(rollOverRecord.getAttribute("name"), values);
                        }
                    });

                    ImgButton deleteImg = WidgetUtil.getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String name = rollOverRecord.getAttribute("name");
                            final String applicationName = rollOverRecord.getAttribute("application");
                            SC.confirm("Do you really want to remove the entry \"" + name + "\"?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value) {
                                        remove(name, applicationName);
                                    }
                                }
                            });
                        }
                    });

                    rollOverCanvas.addMember(loadImg);
                    if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin()) {
                        rollOverCanvas.addMember(deleteImg);
                    }
                }
                return rollOverCanvas;
            }
        };

        grid.setWidth100();
        grid.setHeight100();
        grid.setCanExpandRecords(true);
        grid.setExpansionMode(ExpansionMode.DETAIL_FIELD);
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFields(new ListGridField("application", "Application"), new ListGridField("name", "Name"));
        grid.setDetailField("values");
        grid.setSortField("application");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                grid.expandRecord(event.getRecord());
            }
        });
    }

    @Override
    public void loadData() {

        AsyncCallback<List<SimulationInput>> callback = new AsyncCallback<List<SimulationInput>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get simulations inputs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<SimulationInput> result) {

                List<InputRecord> dataList = new ArrayList<InputRecord>();

                for (SimulationInput wi : result) {
                    String[] inputs = wi.getInputs().split("--");
                    StringBuilder values = new StringBuilder();

                    for (String in : inputs) {

                        if (!in.contains(ApplicationConstants.SEPARATOR_INPUT)) {
                            in = in.replace("=", " = ");
                        } else {
                            in = in.replace("=", " = Start: ");
                        }
                        in = in.replace(ApplicationConstants.SEPARATOR_LIST, "; ");
                        in = in.replaceFirst(ApplicationConstants.SEPARATOR_INPUT, " - Stop: ");
                        in = in.replaceFirst(ApplicationConstants.SEPARATOR_INPUT, " - Step: ");
                        values.append(in);
                        values.append("<br />");
                    }
                    dataList.add(new InputRecord(wi.getApplication(),
                            wi.getName(), values.toString()));
                }
                grid.setData(dataList.toArray(new InputRecord[]{}));
                modal.hide();
            }
        };
        modal.show("Loading inputs...", true);
        WorkflowService.Util.getInstance().getSimulationInputExamples(applicationName, callback);
    }

    private void remove(String name, String applicationName) {

        AsyncCallback<Void> callback;
        callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove simulation input:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void v) {
                modal.hide();
                loadData();
            }
        };
        modal.show("Removing simulation input...", true);
        WorkflowService.Util.getInstance().removeSimulationInputExample(name, applicationName, callback);
    }
}
