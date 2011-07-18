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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Workflow;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.SimulationsContextMenu;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SimulationRecord;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class SimulationsTab extends Tab {

    protected ModalWindow modal;
    protected ListGrid grid;
    protected String user = null;
    protected String app = null;
    protected String status = null;
    protected Date startDate = null;
    protected Date endDate = null;
    protected HandlerRegistration rowMouseDownHandler;
    protected HandlerRegistration rowContextClickHandler;
    private SectionStackSection searchSection;
    private List<Workflow> simulationsList;
    private boolean groupAdmin;

    public SimulationsTab(String application, boolean groupAdmin) {
        this.app = application;
        this.groupAdmin = groupAdmin;
        this.setID(application + "-simulations-tab");
        configure();
    }

    public SimulationsTab(boolean groupAdmin) {
        this.groupAdmin = groupAdmin;
        this.setID("simulations-tab");
        configure();
    }

    private void configure() {

        this.setTitle("Simulations");
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        configureGrid();
        modal = new ModalWindow(grid);

        VLayout vLayout = new VLayout();
        vLayout.addMember(new SimulationsToolStrip(modal, this.getID()));

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        SectionStackSection gridSection = new SectionStackSection();
        gridSection.setCanCollapse(false);
        gridSection.setShowHeader(false);
        gridSection.addItem(grid);

        searchSection = new SearchStackSection(this.getID(), groupAdmin);

        sectionStack.setSections(gridSection, searchSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);

        if (!Context.getInstance().isSystemAdmin() && !groupAdmin) {
            this.user = Context.getInstance().getUser();
        }

        loadData();
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField statusIcoField = FieldUtil.getIconGridField("statusIco");
        ListGridField applicationField = new ListGridField("application", "Application");
        ListGridField statusField = new ListGridField("status", "Status");
        ListGridField simulationIdField = new ListGridField("simulationId", "Simulation ID");
        ListGridField userField = new ListGridField("user", "User");
        ListGridField dateField = FieldUtil.getDateField();

        grid.setFields(statusIcoField, applicationField, statusField,
                simulationIdField, userField, dateField);

        rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {

            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String simulationId = event.getRecord().getAttribute("simulationId");
                String status = event.getRecord().getAttribute("status");
                new SimulationsContextMenu(modal, simulationId, status,
                        groupAdmin).showContextMenu();
            }
        });
        rowMouseDownHandler = grid.addRowMouseDownHandler(new RowMouseDownHandler() {

            public void onRowMouseDown(RowMouseDownEvent event) {
                if (event.getColNum() != 1) {
                    String simulationID = event.getRecord().getAttribute("simulationId");
                    String status = event.getRecord().getAttribute("status");
                    Layout.getInstance().addTab(new SimulationTab(simulationID,
                            status, groupAdmin));
                }
            }
        });
    }

    public void loadData() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<Workflow>> callback = new AsyncCallback<List<Workflow>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get workflow list\n" + caught.getMessage());
            }

            public void onSuccess(List<Workflow> result) {

                List<SimulationRecord> dataList = new ArrayList<SimulationRecord>();

                for (Workflow w : result) {
                    if (!w.getMajorStatus().equals(ApplicationConstants.WorkflowStatus.Cleaned.name())
                            || Context.getInstance().isSystemAdmin()) {

                        dataList.add(new SimulationRecord(w.getApplication(),
                                w.getMajorStatus(), w.getWorkflowID(),
                                w.getUserName(), w.getDate()));
                    }
                }
                grid.setData(dataList.toArray(new SimulationRecord[]{}));
                StatsTab statsTab = (StatsTab) Layout.getInstance().getTab("stats-tab");
                if (statsTab != null) {
                    statsTab.setSimulationsList(result);
                }
                simulationsList = result;
                modal.hide();
            }
        };
        modal.show("Loading Simulations...", true);
        service.getWorkflows(user, app, status, startDate, endDate, callback);
        Layout.getInstance().setActiveCenterTab(this.getID());
    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelection();
    }

    public void expandSearchSection() {
        this.searchSection.setExpanded(true);
    }

    public List<Workflow> getSimulationsList() {
        return simulationsList;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
