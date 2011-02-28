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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import com.google.gwt.user.client.DOM;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.portal.client.bean.Workflow;
import fr.insalyon.creatis.vip.portal.client.view.common.panel.monitor.AbstractWorkflowsPanel;
import fr.insalyon.creatis.vip.portal.client.view.common.RowNumberingColumnConfigAdapter;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowsPanel extends AbstractWorkflowsPanel {

    private static WorkflowsPanel instance;

    /**
     * Pattern Singleton: Gets an unique instance of WorkflowsPanel.
     *
     * @return Unique instance of WorkflowsPanel
     */
    public static WorkflowsPanel getInstance() {
        if (instance == null) {
            instance = new WorkflowsPanel();
        }
        return instance;
    }

    /**
     * Creates an instance of WorkflowsPanel
     */
    private WorkflowsPanel() {
        super("Simulations", "app");
    }

    /**
     * Sets a grid panel with a list of workflows submitted to the grid.
     *
     * @return The workflows grid panel
     */
    protected GridPanel getGridPanel() {
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("statusico"),
                    new StringFieldDef("application"),
                    new StringFieldDef("majorstatus"),
                    new StringFieldDef("workflowID"),
                    new StringFieldDef("user"),
                    new StringFieldDef("subdate")});

        ArrayReader reader = new ArrayReader(recordDef);
        PagingMemoryProxy proxy = new PagingMemoryProxy(new Object[][]{new Object[]{}});

        store = new GroupingStore(proxy, reader);
        store.setSortInfo(new SortState("majorstatus", SortDir.DESC));
        store.setGroupField("majorstatus");
        store.load();
        final CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();

        BaseColumnConfig[] columns = new BaseColumnConfig[]{
            new RowNumberingColumnConfigAdapter(30),
            getIcoStatusColumnConfig(), 
            new ColumnConfig("Application", "application", 180, true, null, "application"),
            new ColumnConfig("Status", "majorstatus", 70, true),
            new ColumnConfig("Simulation ID", "workflowID", 120, true),
            new ColumnConfig("User", "user", 120, true),
            new ColumnConfig("Date", "subdate", 150, true)};

        ColumnModel columnModel = new ColumnModel(columns);

        GridPanel grid = new GridPanel();
        grid.setStore(store);
        grid.setId("app-workflows-grid");
        grid.setColumnModel(columnModel);
        grid.setSelectionModel(cbSelectionModel);
        grid.setFrame(true);
        grid.setStripeRows(true);

        GroupingView gridView = new GroupingView();
        gridView.setGroupTextTpl("{[values.rs[0].data[\"majorstatus\"]]}");
        grid.setView(gridView);
        grid.setAnimCollapse(true);
        
        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                openTab(record.getAsString("workflowID"), 
                        record.getAsString("majorstatus"),
                        record.getAsString("subdate"));
            }

            @Override
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                DOM.eventPreventDefault(e.getBrowserEvent());
                Record record = grid.getStore().getRecordAt(rowIndex);
                showContextMenu(e,
                        record.getAsString("workflowID"),
                        record.getAsString("majorstatus"),
                        record.getAsString("subdate"));
            }
        });

        PagingToolbar pagingToolbar = new PagingToolbar(store);
        pagingToolbar.setPageSize(25);
        pagingToolbar.setDisplayInfo(true);
        pagingToolbar.setDisplayMsg("Displaying simulations {0} - {1} of {2}");
        pagingToolbar.setEmptyMsg("No records to display");

        grid.setBottomToolbar(pagingToolbar);
        store.load();

        return grid;
    }

    /**
     *
     * @param result List of Workflows
     */
    protected void parseResult(List<Workflow> result) {
        Object[][] data = new Object[result.size()][6];
        for (int i = 0; i < result.size(); i++) {
            Workflow w = result.get(i);
            data[i][0] = w.getMajorStatus();
            data[i][1] = w.getApplication();
            data[i][2] = w.getMajorStatus();
            data[i][3] = w.getWorkflowID();
            data[i][4] = w.getUserName();
            data[i][5] = w.getDate();
        }
        PagingMemoryProxy proxy = new PagingMemoryProxy(data);
        store.setDataProxy(proxy);
        store.load(0, 25);
        store.commitChanges();
    }

    /**
     * Creates a new tab panel for the corresponding workflow ID. If the panel
     * was already created, it is activated.
     *
     * @param workflowID Workflow Identification
     * @param status Workflow Status
     * @param submissionDate Workflow submission date
     */
    protected void openTab(String workflowID, String status, String submissionDate) {
        Layout layout = Layout.getInstance();
        if (layout.hasCenterPanelTab(workflowID)) {
            layout.setActiveCenterPanel(workflowID);
        } else {
            layout.addCenterPanel(new WorkflowPanel(workflowID, status));
        }
    }
}
