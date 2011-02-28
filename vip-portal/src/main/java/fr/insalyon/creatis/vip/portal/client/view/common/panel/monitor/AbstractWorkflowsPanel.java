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
package fr.insalyon.creatis.vip.portal.client.view.common.panel.monitor;

import com.gwtext.client.widgets.TabPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.bean.Workflow;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.StatsPanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractWorkflowsPanel extends Panel {

    protected enum Status {

        Completed, Running, Killed
    };
    protected GroupingStore store = null;
    private String user;
    private String app;
    private String status;
    private Date sDate;
    private Date eDate;
    private StatsPanel statsPanel;
    protected TabPanel tabPanel;
    protected String applicationClass;
    protected String appendID;

    public AbstractWorkflowsPanel(String title, String appendID) {
        this.setTitle(title);
        this.appendID = appendID;
        this.setId(appendID + "-workflows-panel");
        this.setLayout(new FitLayout());
        this.add(getGridPanel());


        tabPanel = new TabPanel();
        tabPanel.setResizeTabs(true);
        tabPanel.setMinTabWidth(80);
        this.add(tabPanel);



    }

    protected abstract GridPanel getGridPanel();

    /**
     *
     */
    public void loadWorkflowData() {
        this.loadWorkflowData(null, null, null, null, null);
    }

    /**
     *
     * @param user User name
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     */
    public void loadWorkflowData(String user, String app, String status, Date sDate, Date eDate) {

        this.user = user;
        this.app = app;
        this.status = status;
        this.sDate = sDate;
        this.eDate = eDate;

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<Workflow>> callback = new AsyncCallback<List<Workflow>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get workflow list\n" + caught.getMessage());
            }

            public void onSuccess(List<Workflow> result) {
                parseResult(result);
                //reloadStats(result);
                setStatsPanel(result);
                Ext.get(appendID + "-workflows-grid").unmask();
            }
        };
        service.getWorkflows(user, app, status, sDate, eDate, callback);
        Layout.getInstance().setActiveCenterPanel(id);
    }

    public void loadWorkflowStats(String user, String app, String status, Date sDate, Date eDate) {

        this.user = user;
        this.app = app;
        this.status = status;
        this.sDate = sDate;
        this.eDate = eDate;

        final WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<Workflow>> callback = new AsyncCallback<List<Workflow>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get workflow list\n" + caught.getMessage());
            }

            public void onSuccess(List<Workflow> result) {
                //parseResult(result);
                //reloadStats(result);
                setStatsPanel(result);
                Ext.get(appendID + "-workflows-grid").unmask();
            }
        };
        service.getWorkflows(user, app, status, sDate, eDate, callback);



        Layout.getInstance().setActiveCenterPanel(id);
    }

    /**
     * Reload the data of the grid panel.
     */
    public void reloadData() {
        if (user != null || app != null || status != null || sDate != null || eDate != null) {
            loadWorkflowData(user, app, status, sDate, eDate);
        } else {
            loadWorkflowData();
        }
    }

    protected abstract void parseResult(List<Workflow> result);

    //protected abstract void reloadStats(List<Workflow> result);
    /**
     * Set/Reload the status panel.
     */
    protected void setStatsPanel(List<Workflow> result) {
        Layout layout = Layout.getInstance();
        if (this.statsPanel == null) {
            this.statsPanel = new StatsPanel();
            layout.addCenterPanel(statsPanel);
            layout.setActiveCenterPanel("app-workflows-panel");
        }

        statsPanel.reloadStats(result);

    }

    /**
     * Sets the application class to be displayed in the grid
     *
     * @param applicationClass
     */
    public void setApplicationClass(String applicationClass) {
        this.applicationClass = applicationClass;
    }

    /**
     * 
     * @return
     */
    protected ColumnConfig getIcoStatusColumnConfig() {
        ColumnConfig icoColumn = new ColumnConfig("", "statusico", 25);
        icoColumn.setRenderer(new Renderer() {

            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
                String status = ((String) value);
                String image = "ico_running.png";
                if (status.equals("Completed")) {
                    image = "ico_completed.png";
                } else if (status.equals("Killed")) {
                    image = "ico_killed.png";
                }
                return "<img src=\"./images/" + image + "\" style=\"border: 0\"/>";
            }
        });
        return icoColumn;
    }

    /**
     * Creates a new tab panel for the corresponding workflow ID. If the panel
     * was already created, it is activated.
     *
     * @param workflowID Workflow Identification
     * @param status Workflow Status
     * @param submissionDate Workflow submission date
     */
    protected abstract void openTab(String workflowID, String status, String submissionDate);

    //protected abstract void setStatsPanel(List<Workflow> result);
    /**
     * Displays a context menu for a specific workflow
     *
     * @param e
     * @param workflowID Workflow identification
     * @param status Workflow status
     * @param submissionDate Workflow submission date
     */
    protected void showContextMenu(EventObject e, final String workflowID, final String status, final String submissionDate) {

        Menu contextMenu = new Menu();
        Item viewItem = new Item("View Simulation", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                openTab(workflowID, status, submissionDate);
            }
        });
        contextMenu.addItem(viewItem);
        contextMenu.addSeparator();

        if (status.equals(Status.Running.toString())) {
            Item killItem = new Item("Kill Simulation", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    MessageBox.confirm("Kill Simulation", "Do you really want to kill "
                            + "this simulation (" + workflowID + ")?",
                            new MessageBox.ConfirmCallback() {

                                public void execute(String btnID) {
                                    if (btnID.toLowerCase().equals("yes")) {
                                        killWorkflow(workflowID);
                                    }
                                }
                            });
                }
            });
            killItem.setDisabled(true);
            contextMenu.addItem(killItem);
        } else {
            Item cleanItem = new Item("Clean Simulation", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    MessageBox.confirm("Clean Simulation", "Do you really want to clean "
                            + "this simulation (" + workflowID + ")?",
                            new MessageBox.ConfirmCallback() {

                                public void execute(String btnID) {
                                    if (btnID.toLowerCase().equals("yes")) {
                                        //cleanWorkflow(workflowID);
                                    }
                                }
                            });
                }
            });
            cleanItem.setDisabled(true);
            contextMenu.addItem(cleanItem);
        }
        contextMenu.showAt(e.getXY());
    }

    /**
     * Sends a request to kill the workflow
     *
     * @param workflowID Workflow identification
     */
    private void killWorkflow(String workflowID) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing kill simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                reloadData();
            }
        };
        service.killWorkflow(workflowID, callback);
        Ext.get(appendID + "-workflows-grid").mask("Sending killing signal to " + workflowID + "...");
    }
}
