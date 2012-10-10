/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
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
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class SavedInputsLayout extends AbstractInputsLayout {

    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public SavedInputsLayout(String tabID) {

        super(tabID, "Saved Inputs", CoreConstants.ICON_SAVED);

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

                    ImgButton loadImg = getImgButton("icon-load.png", "Load Input");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            String values = rollOverRecord.getAttribute("values");
                            AbstractLaunchTab launchTab = (AbstractLaunchTab) Layout.getInstance().getTab(tabID);
                            launchTab.loadInput(rollOverRecord.getAttribute("name"), values);
                        }
                    });

                    ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
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
                    rollOverCanvas.addMember(deleteImg);
                }
                return rollOverCanvas;
            }

            private ImgButton getImgButton(String imgSrc, String prompt) {
                ImgButton button = new ImgButton();
                button.setShowDown(false);
                button.setShowRollOver(false);
                button.setLayoutAlign(Alignment.CENTER);
                button.setSrc(imgSrc);
                button.setPrompt(prompt);
                button.setHeight(16);
                button.setWidth(16);
                return button;
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

        ListGridField applicationField = new ListGridField("application", "Application");
        ListGridField nameField = new ListGridField("name", "Name");

        grid.setFields(applicationField, nameField);
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

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
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
        service.getSimulationInputByUser(callback);
    }

    private void remove(String name, String applicationName) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
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
        service.removeSimulationInput(name, applicationName, callback);
    }
}
