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
package fr.insalyon.creatis.vip.application.client.view.monitor.menu;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationActionsUtil;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationTab;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class SimulationsContextMenu extends Menu {

    public SimulationsContextMenu(final ModalWindow modal, final String simulationID,
            final String status, final boolean groupAdmin) {

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem viewItem = new MenuItem("View Simulation");
        viewItem.setIcon("icon-simulation-view.png");
        viewItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new SimulationTab(simulationID, 
                        status, groupAdmin));
            }
        });

        MenuItem killItem = new MenuItem("Kill Simulation");
        killItem.setIcon("icon-kill.png");
        killItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to kill this simulation ("
                        + simulationID + ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            SimulationActionsUtil.killSimulation(modal, simulationID);
                        }
                    }
                });
            }
        });

        MenuItem cleanItem = new MenuItem("Clean Simulation");
        cleanItem.setIcon("icon-clean.png");
        cleanItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to clean this simulation ("
                        + simulationID + ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            SimulationActionsUtil.cleanSimulation(modal, simulationID);
                        }
                    }
                });

            }
        });

        MenuItem purgeItem = new MenuItem("Purge Simulation");
        purgeItem.setIcon("icon-clear.png");
        purgeItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to purge this simulation ("
                        + simulationID + ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            SimulationActionsUtil.purgeSimulation(modal, simulationID);
                        }
                    }
                });

            }
        });

        if (status.equals("Running")) {
            this.setItems(viewItem, killItem);
        } else if (status.equals("Completed") || status.equals("Killed")) {
            this.setItems(viewItem, cleanItem);
        } else if (status.equals("Cleaned")) {
            this.setItems(viewItem, purgeItem);
        }
    }
}
