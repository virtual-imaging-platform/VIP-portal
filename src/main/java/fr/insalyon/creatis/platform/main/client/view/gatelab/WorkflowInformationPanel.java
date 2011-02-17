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
package fr.insalyon.creatis.platform.main.client.view.gatelab;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import fr.insalyon.creatis.platform.main.client.rpc.GatelabService;
import fr.insalyon.creatis.platform.main.client.rpc.GatelabServiceAsync;
import fr.insalyon.creatis.platform.main.client.view.common.toolbar.LogsToolbarButton;
import fr.insalyon.creatis.platform.main.client.view.common.window.lfn.LFNBrowserWindow;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Ibrahim Kallel
 */
public class WorkflowInformationPanel extends Panel {

    private String workflowID;
    private String date;
    private String status;
    private Store store;
    private ToolbarTextItem lastUpdateItem;
    private Timer timer;
    private String clickedFileName;
    private String clickedDir;
    private ToolbarButton stopandmergeButton;
    private String[] states = {"Simulation name", "Simulation identifier", "Submitted Time", "Status", "Total Particles", "Total Particles simulated", "Input", "Output", "Simulation Type", "Gate version"};

    public WorkflowInformationPanel(final String workflowID, final String date, final String status) {
        this.workflowID = workflowID;
        this.status = status;
        this.date = date;
        this.setId(workflowID+"-information-panel");
        this.setTitle("General Information");
        this.setMargins(0, 0, 0, 0);
        this.clickedDir= "/" +workflowID;

        loadWorkflowInputs();

        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("information"),
                    new StringFieldDef("value")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy proxy = new MemoryProxy(new Object[][]{
                    {"Simulation Name",},
                    {"Simulation Identifier",},
                    {"Submitted Time",},
                    {"Status",},
                    {"Total Particles ",},
                    {"Total Particles simulated",},
                    {"Input",},
                    {"Output",},
                    {"Simulation Type",},
                    {"Gate Version",}
                });

        store = new Store(proxy, reader);
        store.load();

        ColumnConfig infoConfig = new ColumnConfig("Information", "information", 245, true);
        ColumnConfig valueConfig = new ColumnConfig("Value", "value", 453, true);

        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
                    infoConfig,
                    valueConfig
                });

        EditorGridPanel grid = new EditorGridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setWidth(702);
        grid.addGridCellListener(new GridCellListenerAdapter() {

            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
                if ((rowIndex == 6 || rowIndex == 7) && colIndex == 1) {
                    Record record = grid.getStore().getRecordAt(rowIndex);
                    String link = record.getAsString("value");
                    link = link.replaceFirst("lfn://lfc-biomed.in2p3.fr:5010", "");
                    new LFNBrowserWindow(link);
                }
            }
        });

        this.add(grid);

        Toolbar toolbar = new Toolbar();
        LogsToolbarButton logsbutton = new LogsToolbarButton(workflowID);
        toolbar.addButton(logsbutton);
        
        if (this.status.toLowerCase().equals("running")) {

            /*
            ToolbarButton killButton = new ToolbarButton("Kill Simulation", new ButtonListenerAdapter() {

                @Override
                public void onClick(Button button, EventObject e) {
                    //TODO
                }
            });
            toolbar.addButton(killButton);
            toolbar.addSeparator();
             *
             */

            stopandmergeButton = new ToolbarButton("Stop and Merge", new ButtonListenerAdapter() {

                @Override
                public void onClick(Button button, EventObject e) {
                    MessageBox.confirm("Confirm", "Do you really want to Stop and merge the simulation \"" + workflowID + "\"?",
                            new MessageBox.ConfirmCallback() {

                                public void execute(String btnID) {
                                    if (btnID.toLowerCase().equals("yes")) {
                                        stopSimulation();
                                        stopandmergeButton.setDisabled(true);
                                    }
                                }
                            });
                }
            });
            toolbar.addButton(stopandmergeButton);
            toolbar.addFill();
            lastUpdateItem = new ToolbarTextItem("Last updated on " + new Date());
            toolbar.addItem(lastUpdateItem);

            timer = new Timer() {

                public void run() {
                    loadWorkflowInputs();
                }
            };
            timer.scheduleRepeating(10000);

        } else {
            /*
            ToolbarButton cleanButton = new ToolbarButton("Clean", new ButtonListenerAdapter() {

                @Override
                public void onClick(Button button, EventObject e) {
                    //TODO
                }
            });
            toolbar.addButton(cleanButton);
             * 
             */
        }
        this.setTopToolbar(toolbar);
    }

    private void loadWorkflowInputs() {

        GatelabServiceAsync gatelabservice = GatelabService.Util.getInstance();
        final AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get workflow list\n" + caught.getMessage());
            }

            public void onSuccess(Map<String, String> result) {
                Object[][] data = new Object[states.length][2];
                for (int i = 0; i < states.length; i++) {
                    data[i][0] = states[i];
                }
                data[0][1] = result.get("application_name");
                data[1][1] = workflowID;
                data[2][1] = date;
                data[3][1] = status;
                data[4][1] = result.get("particles");
                data[5][1] = result.get("runnedparticles");
                data[6][1] = result.get("inputlink");
                data[7][1] = result.get("outputlink");
                data[8][1] = result.get("simulation");
                data[9][1] = result.get("gate_version");

                if (status.toLowerCase().equals("running")) {
                    lastUpdateItem.setText("Last updated on " + new Date());
                }

                MemoryProxy proxy = new MemoryProxy(data);
                store.setDataProxy(proxy);
                store.load();
                store.commitChanges();
            }
        };
        gatelabservice.getGatelabWorkflowInputs(workflowID, callback);
    }

    /*
    private void loadNbParticles() {

    WorkflowServiceAsync service = WorkflowService.Util.getInstance();
    final AsyncCallback<java.lang.Integer> callback = new AsyncCallback<java.lang.Integer>() {

    public void onFailure(Throwable caught) {
    MessageBox.alert("Error", "Error executing get workflow list\n" + caught.getMessage());
    }

    public void onSuccess(Integer result) {
    Object[][] data = new Object[states.length][2];
    for (int i = 0; i < states.length; i++) {
    data[i][0] = states[i];
    if (i == 0) {
    data[i][1] = workflowID;

    } else {
    data[i][1] = "input";

    }
    }
    data[1][1] = date;
    data[3][1] = result;

    MemoryProxy proxy = new MemoryProxy(data);
    store.setDataProxy(proxy);
    store.load();
    store.commitChanges();
    }
    };

    service.getNumberParticles(workflowID,callback);


    }
     * 
     */
    private void stopSimulation() {

        GatelabServiceAsync service = GatelabService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing killing simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                MessageBox.alert("The simulation was successfully Stopped.. Starting Merge in few minutes !");
            }
        };
        service.StopWorkflowSimulation(workflowID, callback);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
