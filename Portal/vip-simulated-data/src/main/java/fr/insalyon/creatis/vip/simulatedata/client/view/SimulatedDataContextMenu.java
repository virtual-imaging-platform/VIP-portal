/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.simulatedata.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerModule;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.models.client.view.ModelDisplayTab;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataConstants;

/**
 *
 * @author Tristan Glatard
 */
public class SimulatedDataContextMenu extends Menu {

    private String model;
    private String simulation;

    public SimulatedDataContextMenu(final String modelURI, final String modelName, final String simulation) {

        this.model = modelURI;
        this.simulation = simulation;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem simuItem = new MenuItem("View simulation");
        simuItem.setIcon(SimulatedDataConstants.ICON_SIMU);
        simuItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new SimulationTab(simulation,
                        "Simulation " + simulation, SimulationStatus.Completed));
            }
        });


        MenuItem modelItem1 = new MenuItem("View model");
        modelItem1.setIcon(SimulatedDataConstants.ICON_MODEL);
        modelItem1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelDisplayTab(model, modelName, true, false));
            }
        });

        MenuItemSeparator separator = new MenuItemSeparator();

        if (this.model == null) {
            this.setItems(simuItem);
        } else {
            this.setItems(simuItem, modelItem1);
        }
        // this.setItems(simuItem, separator, fileItem,paramItem,modelItem);
    }

    private void downloadFile(String path) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                //   modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download file:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                // modal.hide();
                OperationLayout.getInstance().addOperation(result);
                DataManagerModule.dataManagerSection.expand();
            }
        };
        //  modal.show("Adding file to transfer queue...", true);
        service.downloadFile(path, callback);
    }
}
