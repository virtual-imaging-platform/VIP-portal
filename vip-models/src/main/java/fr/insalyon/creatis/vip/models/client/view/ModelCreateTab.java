/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.DateChooser;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.models.client.ModelConstants;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.Date;

/**
 *
 * @author cervenansky
 */
public class ModelCreateTab extends VLayout {

    private SimulationObjectModel objModel = null;
    private ToolStrip toolstrip = null;
    private ModelTreeGrid modelTree = null;
    private ModelServiceAsync ms = null;
    private int selectTP = -1;

    public ModelCreateTab(SimulationObjectModel result) {
        super();

        objModel = result;
        init();
        ms = ModelService.Util.getInstance();

    }

    private void init() {

        toolstrip = new ToolStrip();
        toolstrip.setWidth100();

        toolstrip.addSeparator();
        ToolStripButton addTimepoint = new ToolStripButton("Add timepoint");
        addTimepoint.setIcon(ModelConstants.APP_IMG_TIMEPOINT);
        addTimepoint.setTooltip("It adds a TimePoint to current model");


        addTimepoint.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addTimePoint(new Date(System.currentTimeMillis()));
            }
        });

        toolstrip.addButton(addTimepoint);


        ToolStripButton addInstant = new ToolStripButton("Add instant");
        addInstant.setIcon(ModelConstants.APP_IMG_INSTANT);
        addInstant.setTooltip("It adds a Instant to current model");
        addInstant.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addInstant();
            }
        });
        toolstrip.addButton(addInstant);
        addMember(toolstrip);

    }

    private void addTimePoint(Date d) {

        AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cant add a timepoint");
            }

            public void onSuccess(SimulationObjectModel result) {
                objModel = result;
                updateTreeModel();
            }
        };
        ms.addTimePoint(objModel, d, callback);
    }

    private void addInstant() {
        if (selectTP >= 0) {
            AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Cant add an instant");
                }

                public void onSuccess(SimulationObjectModel result) {
                    objModel = result;
                    updateTreeModel();
                }
            };
            ms.addInstant(objModel, selectTP, callback);
        }

    }

    private void updateTreeModel() {
        if (modelTree == null) {
            modelTree = new ModelTreeGrid(objModel, false);
            modelTree.editable = true;
            addMember(modelTree);
        } else {
            modelTree.refreshFields();
        }
    }
}
