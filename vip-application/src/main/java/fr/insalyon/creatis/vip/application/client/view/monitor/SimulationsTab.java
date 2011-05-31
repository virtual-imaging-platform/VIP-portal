/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class SimulationsTab extends Tab {

    protected ModalWindow modal;
    protected ListGrid grid;
    protected DynamicForm form;
    protected SelectItem userItem;
    protected SelectItem simulationItem;
    protected SelectItem statusItem;
    protected DateItem startDateItem;
    protected DateItem endDateItem;
    protected String user = null;
    protected String app = null;
    protected String status = null;
    protected Date startDate = null;
    protected Date endDate = null;
    protected HandlerRegistration rowMouseDownHandler;
    protected HandlerRegistration rowContextClickHandler;
    private SectionStackSection searchSection;
    private List<Workflow> simulationsList;

    public SimulationsTab(String application) {
        this.app = application;
        this.setID(application + "-simulations-tab");
        configure();
        simulationItem.setDisabled(true);
    }

    public SimulationsTab() {
        this.setID("simulations-tab");
        configure();
        simulationItem.setDisabled(true);
    }

    private void configure() {

        this.setTitle("Simulations");
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        configureGrid();
        configureForm();

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

        searchSection = new SectionStackSection("Search");
        searchSection.setExpanded(false);
        searchSection.addItem(form);

        sectionStack.setSections(gridSection, searchSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);

        if (!Context.getInstance().isSystemAdmin()) {
            this.user = Context.getInstance().getUser();
        }

        loadData();
        loadCombosData();
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
        ListGridField dateField = new ListGridField("date", "Date");

        grid.setFields(statusIcoField, applicationField, statusField,
                simulationIdField, userField, dateField);

        rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {

            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String simulationId = event.getRecord().getAttribute("simulationId");
                String status = event.getRecord().getAttribute("status");
                new SimulationsContextMenu(modal, simulationId, status).showContextMenu();
            }
        });
        rowMouseDownHandler = grid.addRowMouseDownHandler(new RowMouseDownHandler() {

            public void onRowMouseDown(RowMouseDownEvent event) {
                if (event.getColNum() != 1) {
                    String simulationID = event.getRecord().getAttribute("simulationId");
                    String status = event.getRecord().getAttribute("status");
                    Layout.getInstance().addTab(new SimulationTab(simulationID, status));
                }
            }
        });
    }

    private void configureForm() {
        form = new DynamicForm();
        form.setWidth(500);
        form.setNumCols(4);

        userItem = new SelectItem("userFilter", "User");
        simulationItem = new SelectItem("simualtionFilter", "Simulation");
        statusItem = new SelectItem("statusFilter", "Status");

        startDateItem = new DateItem("startDateFilter", "Start Date");
        startDateItem.setUseTextField(true);

        endDateItem = new DateItem("endDateFilter", "End Date");
        endDateItem.setUseTextField(true);

        ButtonItem submitItem = new ButtonItem("submitFilter", " Submit ");
        submitItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (Context.getInstance().isSystemAdmin()) {
                    String userText = userItem.getValueAsString();
                    user = userText == null || userText.isEmpty() || userText.equals("All") ? null : userText;
                }

                if (!simulationItem.isDisabled()) {
                    String simuText = simulationItem.getValueAsString();
                    app = simuText == null || simuText.isEmpty() || simuText.equals("All") ? null : simuText;
                }

                String statusText = statusItem.getValueAsString();
                status = statusText == null || statusText.isEmpty() || statusText.equals("All") ? null : statusText;

                Date startDateValue = startDateItem.getValueAsDate();
                startDate = startDateValue == null ? null : startDateValue;

                Date endDateValue = endDateItem.getValueAsDate();
                endDate = endDateValue == null ? null : endDateValue;

                loadData();
            }
        });

        ButtonItem resetItem = new ButtonItem("resetFilter", " Reset ");
        resetItem.setStartRow(false);
        resetItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                userItem.setValue("All");
                simulationItem.setValue("All");
                statusItem.setValue("All");
                startDateItem.setValue("");
                endDateItem.setValue("");
            }
        });

        if (Context.getInstance().isSystemAdmin()) {
            form.setFields(userItem, startDateItem, simulationItem,
                    endDateItem, statusItem, submitItem, resetItem);
        } else {
            form.setFields(simulationItem, startDateItem,
                    statusItem, endDateItem, submitItem, resetItem);
        }
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

    private void loadCombosData() {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>[]> callback = new AsyncCallback<List<String>[]>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get users and applications lists\n" + caught.getMessage());
            }

            public void onSuccess(List<String>[] result) {
                LinkedHashMap<String, String> usersMap = new LinkedHashMap<String, String>();
                usersMap.put("All", "All");
                for (String s : result[0]) {
                    usersMap.put(s, s);
                }
                userItem.setValueMap(usersMap);
                userItem.setValue("All");

                LinkedHashMap<String, String> simulationMap = new LinkedHashMap<String, String>();
                simulationMap.put("All", "All");
                for (String s : result[1]) {
                    simulationMap.put(s, s);
                }
                simulationItem.setValueMap(simulationMap);
                simulationItem.setValue("All");

                LinkedHashMap<String, String> statusMap = new LinkedHashMap<String, String>();
                statusMap.put("All", "All");
                statusMap.put("Completed", "Completed");
                statusMap.put("Running", "Running");
                statusMap.put("Killed", "Killed");
                if (Context.getInstance().isSystemAdmin()) {
                    statusMap.put("Cleaned", "Cleaned");
                }
                statusItem.setValueMap(statusMap);
                statusItem.setValue("All");
            }
        };
        service.getApplicationsAndUsersList(null, callback);
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
}
