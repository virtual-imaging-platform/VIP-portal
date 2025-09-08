package fr.insalyon.creatis.vip.application.client.view.monitor.timeline;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.rpc.WorkflowServiceImpl;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class TimelineLayout extends VLayout {

    private static TimelineLayout instance;
    private VLayout simulationsLayout;
    private Timer timer;
    private Label loadMoreLabel;

    public static TimelineLayout getInstance() {

        if (instance == null) {
            instance = new TimelineLayout();
        }
        return instance;
    }

    private TimelineLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setMembersMargin(10);

        this.addMember(WidgetUtil.getLabel("<font size=\"3\"><b>Executions Timeline</b></font>",
                ApplicationConstants.ICON_MONITOR_TIMELINE, 20));

        simulationsLayout = new VLayout(5);
        simulationsLayout.setWidth100();
        simulationsLayout.setHeight100();
        simulationsLayout.setPadding(5);
        simulationsLayout.setBackgroundColor("#F2F2F2");
        simulationsLayout.setOverflow(Overflow.AUTO);
        simulationsLayout.setAlign(VerticalAlignment.TOP);
        this.addMember(simulationsLayout);

        loadMoreLabel = WidgetUtil.getLabel("<font color=\"#666666\">Load more Execution</font>", 18, Cursor.HAND);
        loadMoreLabel.setAlign(Alignment.CENTER);
        loadMoreLabel.setBorder("1px solid #CCCCCC");
        loadMoreLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadMoreData();
            }
        });

        loadData();

        timer = new Timer() {
            @Override
            public void run() {
                loadData();
            }
        };
        timer.scheduleRepeating(60000);
    }

    private void loadData() {

        AsyncCallback<List<Simulation>> callback = new AsyncCallback<List<Simulation>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Simulation> result) {
                if (!result.isEmpty()) {
                    for (Simulation simulation : result) {
                        boolean exists = false;
                        int position = 0;
                        for (Canvas canvas : simulationsLayout.getChildren()) {
                            if (canvas instanceof SimulationBoxLayout) {
                                SimulationBoxLayout sbl = (SimulationBoxLayout) canvas;
                                if (sbl.getSimulationID().equals(simulation.getID())) {
                                    exists = true;
                                    sbl.updateStatus(simulation.getStatus());
                                    break;
                                } else if (simulation.getDate().before(sbl.getLaunchedDate())) {
                                    position = simulationsLayout.getMemberNumber(canvas) + 1;
                                }
                            }
                        }
                        if (!exists) {
                            if(simulation.getStatus()!=SimulationStatus.Cleaned ||  CoreModule.user.isSystemAdministrator()) {
                                simulationsLayout.addMember(TimelineParser.getInstance().parse(
                                        simulation.getID(),
                                        simulation.getSimulationName(),
                                        simulation.getApplicationName(),
                                        simulation.getApplicationVersion(),
                                        simulation.getApplicationClass(),
                                        simulation.getUserName(),
                                        simulation.getStatus(),
                                        simulation.getDate()), position);
                            }
                        }
                    }
                    simulationsLayout.addMember(loadMoreLabel);
                }
            }
        };
        WorkflowServiceImpl.Util.getInstance().getSimulations(callback);
    }

    private void loadMoreData() {

        AsyncCallback<List<Simulation>> callback = new AsyncCallback<List<Simulation>>() {
            @Override
            public void onFailure(Throwable caught) {
                setLoading(false);
                Layout.getInstance().setWarningMessage("Unable to load executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Simulation> result) {

                setLoading(false);
                if (!result.isEmpty()) {    
                    simulationsLayout.removeChild(loadMoreLabel);
                    for (Simulation simulation : result) {
                        if(simulation.getStatus()!=SimulationStatus.Cleaned ||  CoreModule.user.isSystemAdministrator()) {
                            simulationsLayout.addMember(TimelineParser.getInstance().parse(
                                    simulation.getID(),
                                    simulation.getSimulationName(),
                                    simulation.getApplicationName(),
                                    simulation.getApplicationVersion(),
                                    simulation.getApplicationClass(),
                                    simulation.getUserName(),
                                    simulation.getStatus(),
                                    simulation.getDate()));
                        }
                    }
                    simulationsLayout.addMember(loadMoreLabel);
                } else {
                    simulationsLayout.removeChild(loadMoreLabel);
                }
            }
        };
        Date lastDate = ((SimulationBoxLayout) simulationsLayout.getChildren()[simulationsLayout.getChildren().length - 2]).getLaunchedDate();
        WorkflowServiceImpl.Util.getInstance().getSimulations(lastDate, callback);
        setLoading(true);
    }

    private void setLoading(boolean loading) {

        if (loading) {
            loadMoreLabel.setContents("<font color=\"#666666\">Loading executions...</font>");
            loadMoreLabel.setIcon(CoreConstants.ICON_LOADING);
            loadMoreLabel.setDisabled(true);
        } else {
            loadMoreLabel.setContents("<font color=\"#666666\">Load more Executions</font>");
            loadMoreLabel.setIcon(null);
            loadMoreLabel.setDisabled(false);
        }
    }
   
    public void update() {
        loadData();
    }
    
    public void terminate() {
        timer.cancel();
        destroy();
        instance = null;
    }
}
