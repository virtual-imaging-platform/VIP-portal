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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataConstants;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataService;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataServiceAsync;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class SimulatedDataTab extends Tab {

    private VLayout vLayout;
    protected ModalWindow modal;
    SimulatedDataGrid pet, us, ct, mri;

    public SimulatedDataTab() {

        this.setTitle(Canvas.imgHTML(SimulatedDataConstants.ICON_SD) + " " + SimulatedDataConstants.APP_SD);
        this.setID(SimulatedDataConstants.TAB_SD);
        this.setCanClose(true);

        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);

        this.setPane(vLayout);

        modal = new ModalWindow(vLayout);

        pet = new SimulatedDataGrid("PET");
        us = new SimulatedDataGrid("Ultrasound");
        ct = new SimulatedDataGrid("CT");
        mri = new SimulatedDataGrid("MRI");

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.setTitle("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        toolStrip.addButton(refreshButton);

        ToolStripButton dumpRdf = new ToolStripButton();
        dumpRdf.setIcon(SimulatedDataConstants.ICON_DUMP);
        dumpRdf.setTitle("Get RDF dump");
        if (!CoreModule.user.isSystemAdministrator() && !CoreModule.user.isGroupAdmin()) {
            dumpRdf.hide();
        }
        dumpRdf.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal.show("Creating RDF dump of simulated data repository", true);
                SimulatedDataServiceAsync msa = SimulatedDataService.Util.getInstance();
                AsyncCallback<String> ac = new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        modal.hide();
                        Layout.getInstance().setWarningMessage("Cannot get RDF dump file");
                    }

                    @Override
                    public void onSuccess(String result) {
                        modal.hide();
                        Window.open(
                                GWT.getModuleBaseURL()
                                + "/getfileservice?filepath=" + result
                                + "&" + CoreConstants.COOKIES_SESSION
                                + "=" + Cookies.getCookie(CoreConstants.COOKIES_SESSION), "", "");
                    }
                };
                msa.getRdfDump(ac);
            }
        });
        toolStrip.addButton(dumpRdf);

        vLayout.addMember(toolStrip);
        vLayout.addMember(pet);
        vLayout.addMember(us);
        vLayout.addMember(ct);
        vLayout.addMember(mri);

        loadData();
    }

    private void loadData() {

        modal.show("Loading data...", true);
        AsyncCallback<List<SimulatedData>> callback = new AsyncCallback<List<SimulatedData>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get simulated data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<SimulatedData> result) {
                modal.hide();
                pet.loadData(result);
                us.loadData(result);
                ct.loadData(result);
                mri.loadData(result);
            }
        };
        SimulatedDataService.Util.getInstance().getSimulatedData(callback);
    }
}
