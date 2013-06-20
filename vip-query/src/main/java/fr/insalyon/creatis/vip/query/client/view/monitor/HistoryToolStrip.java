/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

//import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryHistoryTab;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public class HistoryToolStrip  extends ToolStrip {


    private ModalWindow modal;

    public HistoryToolStrip(ModalWindow modal) {

        this.modal = modal;
        this.setWidth100();

        // Refresh Button
        this.addButton(WidgetUtil.getToolStripButton("Refresh",
                CoreConstants.ICON_REFRESH, null, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // getQueryHistoryTab().loadData();
            }
        }));

        // Search Button
        this.addButton(WidgetUtil.getToolStripButton("Search Query",
                QueryConstants.ICON_SEARCH, null, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
               //: getQueryHistoryTab().expandSearchSection();
            }
        }));

        //Kill Simulations Button
        this.addButton(WidgetUtil.getToolStripButton("Kill Query Execution",
                QueryConstants.ICON_KILL, null, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to kill the selected running query?", new BooleanCallback() {

                   @Override
                   public void execute(Boolean value) {
                       if (value) {
                        //    killSimulations();
                       }
                   }
                   
                });
            }
        }));

        // Clean Simulations Button
        this.addButton(WidgetUtil.getToolStripButton("Clean Query",
                QueryConstants.ICON_CLEAN, null, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.confirm("Do you really want to clean the selected completed/killed Query?", new BooleanCallback() {

                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            //cleanSimulations();
                        }
                    }
                });
            }
        }));

       // if (CoreModule.user.isSystemAdministrator()) {

            // Purge Simulations
            this.addButton(WidgetUtil.getToolStripButton("Purge Query",
                    CoreConstants.ICON_CLEAR, null, new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    SC.ask("Do you really want to purge the selected cleaned Query?", new BooleanCallback() {

                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                //purgeSimulations();
                            }
                        }
                    });
                }
            }));

          
    /**
     * Sends a request to kill the selected running simulations
     *
     */
    /*private void killSimulations() {

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Running) {
                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to kill simulations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.killSimulations(simulationIDs, callback);
        modal.show("Sending killing signal to selected simulations...", true);
    }

    /**
     * Sends a request to clean the selected completed/killed simulations
     *
     */
            /*
    private void cleanSimulations() {

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Completed
                    || status == SimulationStatus.Killed) {

                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to clean simulations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.cleanSimulations(simulationIDs, callback);
        modal.show("Cleaning selected simulations...", true);
    }

    /**
     * Sends a request to purge the selected cleaned simulations
     *
     */
    /*private void purgeSimulations() {

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Cleaned) {
                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to purge simulations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.purgeSimulations(simulationIDs, callback);
        modal.show("Purging selected simulations...", true);
    }
    * */
    }
    private QueryHistoryTab getQueryHistoryTab() {
        return (QueryHistoryTab) Layout.getInstance().getTab(QueryConstants.TAB_QUERYHISTORY);
    }
}

    




