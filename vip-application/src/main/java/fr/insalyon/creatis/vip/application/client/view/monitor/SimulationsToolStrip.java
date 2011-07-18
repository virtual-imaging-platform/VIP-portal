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

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SimulationRecord;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class SimulationsToolStrip extends ToolStrip {

    private ModalWindow modal;
    private String tabID;

    public SimulationsToolStrip(ModalWindow modal, final String tabID) {

        this.modal = modal;
        this.tabID = tabID;
        this.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon("icon-refresh.png");
        refreshButton.setTitle("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
                simulationsTab.loadData();
            }
        });
        this.addButton(refreshButton);

        ToolStripButton searchButton = new ToolStripButton();
        searchButton.setIcon("icon-search.png");
        searchButton.setTitle("Search");
        searchButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
                simulationsTab.expandSearchSection();
            }
        });
        this.addButton(searchButton);

        ToolStripButton killButton = new ToolStripButton();
        killButton.setIcon("icon-kill.png");
        killButton.setTitle("Kill Simulations");
        killButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SC.confirm("Do you really want to kill the selected running simulations?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            killSimulations();
                        }
                    }
                });
            }
        });
        this.addButton(killButton);

        ToolStripButton cleanButton = new ToolStripButton();
        cleanButton.setIcon("icon-clean.png");
        cleanButton.setTitle("Clean Simulations");
        cleanButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SC.confirm("Do you really want to clean the selected completed/killed simulations?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            cleanSimulations();
                        }
                    }
                });
            }
        });
        this.addButton(cleanButton);

        if (Context.getInstance().isSystemAdmin()) {
            ToolStripButton purgeButton = new ToolStripButton();
            purgeButton.setIcon("icon-clear.png");
            purgeButton.setTitle("Purge Simulations");
            purgeButton.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    SC.confirm("Do you really want to purge the selected cleaned simulations?", new BooleanCallback() {

                        public void execute(Boolean value) {
                            if (value != null && value) {
                                purgeSimulations();
                            }
                        }
                    });
                }
            });
            this.addButton(purgeButton);
        }

        if (Context.getInstance().isSystemAdmin()) {
            ToolStripButton statsButton = new ToolStripButton();
            statsButton.setIcon("icon-chart.png");
            statsButton.setTitle("Detailed Stats");
            statsButton.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    Layout.getInstance().addTab(new StatsTab());
                    StatsTab statsTab = (StatsTab) Layout.getInstance().getTab("stats-tab");
                    SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
                    statsTab.setSimulationsList(simulationsTab.getSimulationsList());
                }
            });

            this.addSeparator();
            this.addButton(statsButton);
        }
    }

    /**
     * Sends a request to kill the selected running simulations
     * 
     */
    private void killSimulations() {
        SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
        ListGridRecord[] records = simulationsTab.getGridSelection();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            if (data.getStatus().equals("Running")) {
                SimulationActionsUtil.killSimulation(modal, data.getSimulationId());
            }
        }
    }

    /**
     * Sends a request to clean the selected completed/killed simulations
     * 
     */
    private void cleanSimulations() {
        SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
        ListGridRecord[] records = simulationsTab.getGridSelection();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            if (data.getStatus().equals("Completed") || data.getStatus().equals("Killed")) {
                SimulationActionsUtil.cleanSimulation(modal, data.getSimulationId());
            }
        }
    }

    /**
     * Sends a request to purge the selected cleaned simulations
     * 
     */
    private void purgeSimulations() {
        SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
        ListGridRecord[] records = simulationsTab.getGridSelection();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            if (data.getStatus().equals("Cleaned")) {
                SimulationActionsUtil.purgeSimulation(modal, data.getSimulationId());
            }
        }
    }
}
