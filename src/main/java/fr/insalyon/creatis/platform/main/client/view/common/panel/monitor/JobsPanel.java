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
package fr.insalyon.creatis.platform.main.client.view.common.panel.monitor;

import com.google.gwt.user.client.DOM;
import fr.insalyon.creatis.platform.main.client.view.common.window.fileviewer.FileViewerWindow;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.platform.main.client.rpc.JobService;
import fr.insalyon.creatis.platform.main.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.platform.main.client.bean.Job;
import fr.insalyon.creatis.platform.main.client.bean.Node;
import fr.insalyon.creatis.platform.main.client.view.application.monitor.NodeWindow;
import fr.insalyon.creatis.platform.main.client.view.common.RowNumberingColumnConfigAdapter;
import fr.insalyon.creatis.platform.main.client.view.common.toolbar.LogsToolbarButton;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class JobsPanel extends Panel {

    private String workflowID;
    private GroupingStore store;
    private ToolbarTextItem lastUpdateItem;
    private Menu menu;
    private String fileName;
    private String status;
    private String jobID;
    private Timer timer;

    public JobsPanel(String workflowID, boolean completed) {

        this.setId(workflowID + "-jobs-panel");
        this.workflowID = workflowID;
        this.setTitle("Jobs");
        this.setLayout(new FitLayout());

        loadJobsData();
        PagingMemoryProxy proxy = new PagingMemoryProxy(new Object[][]{new Object[]{}});
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("jobid"),
                    new StringFieldDef("status"),
                    new StringFieldDef("exitCode"),
                    new StringFieldDef("command"),
                    new StringFieldDef("filename")
                });

        ArrayReader reader = new ArrayReader(recordDef);

        store = new GroupingStore(proxy, reader);
        store.setSortInfo(new SortState("jobid", SortDir.ASC));
        store.setGroupField("command");
        store.load();

        ColumnConfig jobIDColumn = new ColumnConfig("Job ID", "jobid", 100, true, null, "jobid");
        ColumnConfig statusColumn = new ColumnConfig("Status", "status", 180, true);
        ColumnConfig exitCodeColumn = new ColumnConfig("Minor Status", "exitCode", 200);
        ColumnConfig commandColumn = new ColumnConfig("Command", "command", 60);
        ColumnConfig fileNameColumn = new ColumnConfig("File Name", "filename", 60);
        commandColumn.setHidden(true);
        fileNameColumn.setHidden(true);

        BaseColumnConfig[] columns = new BaseColumnConfig[]{
            new RowNumberingColumnConfigAdapter(30),
            jobIDColumn,
            statusColumn,
            exitCodeColumn,
            commandColumn,
            fileNameColumn
        };

        ColumnModel columnModel = new ColumnModel(columns);

        GridPanel grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setFrame(true);
        grid.setStripeRows(true);

        GroupingView gridView = new GroupingView();
        gridView.setGroupTextTpl("{[values.rs[0].data[\"command\"]]}");
        grid.setView(gridView);
        grid.setAnimCollapse(true);

        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                jobID = record.getAsString("jobid");
                fileName = record.getAsString("filename");
                status = record.getAsString("status");
                showWorkflowMenu(e);
            }

            @Override
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                DOM.eventPreventDefault(e.getBrowserEvent());
                onRowClick(grid, rowIndex, e);
            }
        });

        PagingToolbar pagingToolbar = new PagingToolbar(store);
        pagingToolbar.setPageSize(50);
        pagingToolbar.setDisplayInfo(true);
        pagingToolbar.setDisplayMsg("Displaying jobs {0} - {1} of {2}");
        pagingToolbar.setEmptyMsg("No records to display");

        grid.setBottomToolbar(pagingToolbar);
        store.load();
        
        this.add(grid);

        Toolbar topToolbar = new Toolbar();

        LogsToolbarButton logsbutton = new LogsToolbarButton(workflowID);
        topToolbar.addButton(logsbutton);
        topToolbar.addFill();
        lastUpdateItem = new ToolbarTextItem("Last updated on " + new Date());
        topToolbar.addItem(lastUpdateItem);
        this.setTopToolbar(topToolbar);

        if (!completed) {
            timer = new Timer() {

                public void run() {
                    loadJobsData();
                }
            };
            timer.scheduleRepeating(15000);
        }
    }

    private void showWorkflowMenu(EventObject e) {

        if (menu == null) {
            menu = new Menu();
            menu.setId(workflowID + "-jobs-menu");

            Item appOutputItem = new Item("View Application Output", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadFileWindow("out", ".sh.out", "application_execution");
                }
            });
            appOutputItem.setId(workflowID + "-jobs-app-out");
            menu.addItem(appOutputItem);

            Item appErrorItem = new Item("View Application Error", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadFileWindow("err", ".sh.err", "application_execution");
                }
            });
            appErrorItem.setId(workflowID + "-jobs-app-err");
            menu.addItem(appErrorItem);
            menu.addSeparator();

            Item outputItem = new Item("View Output File", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadFileWindow("out", ".sh.out", null);
                }
            });
            outputItem.setId(workflowID + "-jobs-view-out");
            menu.addItem(outputItem);

            Item errorItem = new Item("View Error File", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadFileWindow("err", ".sh.err", null);
                }
            });
            errorItem.setId(workflowID + "-jobs-view-err");
            menu.addItem(errorItem);
            menu.addSeparator();

            Item scriptItem = new Item("View Script File", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadFileWindow("sh", ".sh", null);
                }
            });
            menu.addItem(scriptItem);
            menu.addSeparator();

            Item nodeItem = new Item("Node Information", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadNodeInformation();
                }
            });
            nodeItem.setId(workflowID + "-jobs-node-info");
            menu.addItem(nodeItem);
        }
        if (!status.equals("ERROR") && !status.equals("COMPLETED")) {
            menu.getItem(workflowID + "-jobs-app-out").setDisabled(true);
            menu.getItem(workflowID + "-jobs-app-err").setDisabled(true);
            menu.getItem(workflowID + "-jobs-view-out").setDisabled(true);
            menu.getItem(workflowID + "-jobs-view-err").setDisabled(true);
            menu.getItem(workflowID + "-jobs-node-info").setDisabled(true);
        } else {
            menu.getItem(workflowID + "-jobs-app-out").setDisabled(false);
            menu.getItem(workflowID + "-jobs-app-err").setDisabled(false);
            menu.getItem(workflowID + "-jobs-view-out").setDisabled(false);
            menu.getItem(workflowID + "-jobs-view-err").setDisabled(false);
            menu.getItem(workflowID + "-jobs-node-info").setDisabled(false);
        }
        menu.showAt(e.getXY());
    }

    private void loadJobsData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<Job>> callback = new AsyncCallback<List<Job>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get jobs list: " + caught.getMessage());
            }

            public void onSuccess(List<Job> result) {
                Object[][] data = new Object[result.size()][4];
                for (int i = 0; i < result.size(); i++) {
                    Job j = result.get(i);
                    data[i][0] = j.getId();
                    data[i][1] = j.getStatus();
                    data[i][2] = parseExitCode(j.getExitCode(), j.getStatus());
                    data[i][3] = j.getCommand();
                    data[i][4] = j.getFileName();
                }
                lastUpdateItem.setText("Last updated on " + new Date());

                PagingMemoryProxy proxy = new PagingMemoryProxy(data);
                store.setDataProxy(proxy);
                store.load(0, 50);
                store.commitChanges();
            }
        };
        service.getJobsList(workflowID, callback);
    }

    private void loadFileWindow(String dir, String ext, String filter) {
        new FileViewerWindow(workflowID, dir, fileName, ext, workflowID + "-jobs-menu", filter);
    }

    private void loadNodeInformation() {
        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Node> callback = new AsyncCallback<Node>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get node: " + caught.getMessage());
            }

            public void onSuccess(Node result) {
                new NodeWindow(result, jobID);
            }
        };
        service.getNode(workflowID, jobID, callback);
    }

    private String parseExitCode(int exitCode, String status) {
        if (status.equals("COMPLETED") || status.equals("ERROR")) {
            switch (exitCode) {
                case 0:
                    return "Execution Completed";
                case 1:
                    return "Inputs Download Failed";
                case 2:
                    return "Outputs Upload Failed";
                case 6:
                    return "Application Execution Failed";
                case 7:
                    return "Directories Creation Failed";
                default:
                    return "Retrieving Status";
            }
        } else {
            return "";
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        menu = null;
    }
}
