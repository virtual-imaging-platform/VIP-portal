/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
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
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
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
        addTimepoint.setTooltip("Adds a time-point to the current model");


        addTimepoint.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addTimePoint(new Date(System.currentTimeMillis()));
            }
        });

        toolstrip.addButton(addTimepoint);


        ToolStripButton addInstant = new ToolStripButton("Add instant");
        addInstant.setIcon(ModelConstants.APP_IMG_INSTANT);
        addInstant.setTooltip("Adds an instant to the current model");
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
                Layout.getInstance().setWarningMessage("Cant add a timepoint");
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
                    Layout.getInstance().setWarningMessage("Cant add an instant");
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
            modelTree = new ModelTreeGrid(objModel, false,true);
            modelTree.editable = true;
            addMember(modelTree);
        } else {
            modelTree.refreshFields();
        }
    }
}
