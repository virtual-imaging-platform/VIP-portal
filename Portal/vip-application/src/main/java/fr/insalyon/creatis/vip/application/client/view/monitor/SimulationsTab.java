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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
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
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.SimulationsContextMenu;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SimulationRecord;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SimulationsTab extends Tab {

    protected ModalWindow modal;
    protected ListGrid grid;
    protected String user = null;
    protected String app = null;
    protected String status = null;
    protected String appClass = null;
    protected Date startDate = null;
    protected Date endDate = null;
    protected HandlerRegistration rowMouseDownHandler;
    protected HandlerRegistration rowContextClickHandler;
    private SectionStackSection searchSection;
    private List<Simulation> simulationsList;

    public SimulationsTab() {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_MONITOR) + " Executions");
        this.setID(ApplicationConstants.TAB_MONITOR);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        configureGrid();
        modal = new ModalWindow(grid);

        VLayout vLayout = new VLayout();
        vLayout.addMember(new SimulationsToolStrip(modal));

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        SectionStackSection gridSection = new SectionStackSection();
        gridSection.setCanCollapse(false);
        gridSection.setShowHeader(false);
        gridSection.addItem(grid);

        searchSection = new SearchStackSection();

        sectionStack.setSections(gridSection, searchSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);

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
        grid.setFields(
                FieldUtil.getIconGridField("statusIco"),
                new ListGridField("simulationName", "Execution Name"),
                new ListGridField("status", "Status"),
                new ListGridField("application", "Application"),
                new ListGridField("applicationVersion", "Version"),
                new ListGridField("user", "User"),
                FieldUtil.getDateField());

        rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {
            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                new SimulationsContextMenu(modal,
                        event.getRecord().getAttribute("simulationId"),
                        event.getRecord().getAttribute("simulationName"),
                        SimulationStatus.valueOf(event.getRecord().getAttribute("status")),
                        event.getRecord().getAttribute("application"),
                        event.getRecord().getAttribute("applicationVersion"),
                        event.getRecord().getAttribute("applicationClass"),
                        event.getRecord().getAttribute("user")).showContextMenu();
            }
        });
        rowMouseDownHandler = grid.addRowMouseDownHandler(new RowMouseDownHandler() {
            @Override
            public void onRowMouseDown(RowMouseDownEvent event) {
                if (event.getColNum() != 1) {
                    Layout.getInstance().addTab(MonitorParser.getInstance().parse(
                            event.getRecord().getAttribute("simulationId"),
                            event.getRecord().getAttribute("simulationName"),
                            event.getRecord().getAttribute("application"),
                            SimulationStatus.valueOf(event.getRecord().getAttribute("status")),
                            event.getRecord().getAttributeAsDate("date")));
                }
            }
        });
    }

    public void loadData() {

        final AsyncCallback<List<Simulation>> callback = new AsyncCallback<List<Simulation>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get simulations list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Simulation> result) {

                List<SimulationRecord> dataList = new ArrayList<SimulationRecord>();

                for (Simulation simulation : result) {
                    if (simulation.getStatus() != SimulationStatus.Cleaned
                            || CoreModule.user.isSystemAdministrator()
                            || CoreModule.user.isGroupAdmin()) {

                        dataList.add(new SimulationRecord(
                                simulation.getSimulationName(),
                                simulation.getApplicationName(),
                                simulation.getApplicationVersion(),
                                simulation.getApplicationClass(),
                                simulation.getStatus(),
                                simulation.getID(),
                                simulation.getUserName(),
                                simulation.getDate()));
                    }
                }
                grid.setData(dataList.toArray(new SimulationRecord[]{}));
                StatsTab statsTab = (StatsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_STATS);
                if (statsTab != null) {
                    statsTab.setSimulationsList(result);
                }
                simulationsList = result;
                modal.hide();
            }
        };
        modal.show("Loading Executions...", true);
        WorkflowService.Util.getInstance().getSimulations(user, app, status, appClass, startDate, endDate, callback);
        Layout.getInstance().setActiveCenterTab(ApplicationConstants.TAB_MONITOR);
    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelectedRecords();
    }

    public void expandSearchSection() {
        this.searchSection.setExpanded(true);
    }

    public List<Simulation> getSimulationsList() {
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
    
     public void setAppClass(String appClass) {
        this.appClass = appClass;
    }
}
