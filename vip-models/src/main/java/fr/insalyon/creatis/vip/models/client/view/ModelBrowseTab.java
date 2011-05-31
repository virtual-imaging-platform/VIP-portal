/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glatard
 */
public class ModelBrowseTab extends Tab {

    protected ListGrid grid;
    protected ModalWindow modal;
    protected HandlerRegistration rowContextClickHandler;
    protected HandlerRegistration rowMouseDownHandler;

    public ModelBrowseTab() {

        this.setTitle("List models");
        this.setID("model-browse-tab");
        this.setCanClose(true);

        configureGrid();
        modal = new ModalWindow(grid);

        VLayout layout = new VLayout();

        loadModels();
        layout.addMember(grid);

        //this will be triggered from the context menu
        //TODO: call model.storageURL when Germain implements it
        // String lfnModel = "/grid/biomed/creatis/vip/data/groups/VIP/Models/adam.zip";
        //  downloadModel(lfnModel) ;

        this.setPane(layout);

    }

    public void resetTab() {
        return;
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        // grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        grid.setEmptyMessage("<br>No data available.");

        //    ListGridField statusIcoField = FieldUtil.getIconGridField("statusIco");
        ListGridField modelNameField = new ListGridField("name", "Name");
        ListGridField typeField = new ListGridField("types", "Type(s)");
        ListGridField longitudinalField = new ListGridField("longitudinal", "Longitudinal");
        ListGridField movementField = new ListGridField("movement", "Movement");
        ListGridField URIField = new ListGridField("uri", "URI");

        grid.setFields(modelNameField, typeField, longitudinalField, movementField, URIField);

        rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {

        public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                //call download model method below to download the model zip file.
                
                //SC.say("context click");
//                String simulationId = event.getRecord().getAttribute("simulationId");
//                String status = event.getRecord().getAttribute("status");
//                new SimulationsContextMenu(modal, simulationId, status).showContextMenu();
            }
        });
        rowMouseDownHandler = grid.addRowMouseDownHandler(new RowMouseDownHandler() {

            public void onRowMouseDown(RowMouseDownEvent event) {
                Layout.getInstance().addTab(new ModelTab(event.getRecord().getAttribute("uri")));
            }
        });
    }

    private void loadModels() {
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<List<SimulationObjectModelLight>> callback = new AsyncCallback<List<SimulationObjectModelLight>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot list models");
                modal.hide();
            }

            public void onSuccess(List<SimulationObjectModelLight> result) {
                List<SimulationObjectModelLightRecord> dataList = new ArrayList<SimulationObjectModelLightRecord>();
                for (SimulationObjectModelLight s : result) {
                    String type = "";
                    boolean[] saxes = s.getSemanticAxes();
                    boolean init = false;
                    if (saxes[0]) {
                        type += "anatomical";
                        init = true;
                    }
                    if (saxes[1]) {
                        if (init) {
                            type += ", ";
                        }
                        init = true;
                        type += "pathological";
                    }
                    if (saxes[2]) {
                        if (init) {
                            type += ", ";
                        }
                        init = true;
                        type += "geometrical";
                    }
                    if (saxes[3]) {
                        if (init) {
                            type += ", ";
                        }
                        init = true;
                        type += "foreign object";
                    }
                    if (saxes[4]) {
                        if (init) {
                            type += ", ";
                        }
                        init = true;
                        type += "external agent";
                    }
                    dataList.add(new SimulationObjectModelLightRecord(s.getModelName(), type, "" + s.isLongitudinal(), "" + s.isMoving(), s.getURI()));
                }
                grid.setData(dataList.toArray(new SimulationObjectModelLightRecord[]{}));
                modal.hide();
            }
        };
        ms.listAllModels(callback);
        modal.show("Loading Models...", true);

    }

   
}
