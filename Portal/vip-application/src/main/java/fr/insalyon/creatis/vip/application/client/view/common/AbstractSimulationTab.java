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
package fr.insalyon.creatis.vip.application.client.view.common;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class AbstractSimulationTab extends Tab {

    protected TabSet tabSet;
    protected boolean completed;
    private Timer timer;

    public AbstractSimulationTab(String simulationID, String simulationName, SimulationStatus status) {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_APPLICATION_MONITOR) + " " + simulationName);
        this.setID(simulationID + "_tab");
        this.setCanClose(true);

        this.completed = status == SimulationStatus.Running ? false : true;

        VLayout vLayout = new VLayout();
        tabSet = new TabSet();
        tabSet.setTabBarPosition(Side.LEFT);
        tabSet.setWidth100();
        tabSet.setHeight100();

        vLayout.addMember(tabSet);

        this.setPane(vLayout);

        if (!completed) {
            timer = new Timer() {

                @Override
                public void run() {
                    updateData();
                }
            };
            timer.scheduleRepeating(30000);
        }

        this.addTabDeselectedHandler(new TabDeselectedHandler() {

            @Override
            public void onTabDeselected(TabDeselectedEvent event) {
                if (!completed) {
                    timer.cancel();
                }
            }
        });
        this.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!completed) {
                    updateData();
                    timer.scheduleRepeating(30000);
                }
            }
        });

        tabSet.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                if (!completed) {
                    ((AbstractCornerTab) tabSet.getSelectedTab()).update();
                }
            }
        });
    }

    protected void updateData() {
        ((AbstractCornerTab) tabSet.getSelectedTab()).update();
    }

    public void destroy() {
        if (!completed) {
            timer.cancel();
        }
    }
}
