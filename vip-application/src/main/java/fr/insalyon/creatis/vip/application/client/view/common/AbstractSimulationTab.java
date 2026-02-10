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
        this.setID(tabIdFrom(simulationID));
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

    public static String tabIdFrom(String simulationId) {
        return simulationId.replaceAll("[ -]", "_").toLowerCase() + "_tab";
    }
}
