package fr.insalyon.creatis.vip.application.client.view.monitor.progress;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.bean.Activity;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobStatus;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ProgressLayout extends VLayout {

    protected String simulationID;
    protected SimulationStatus status;
    protected VLayout barLayout;
    protected Label progressLabel;
    protected Label statusLabel;

    public ProgressLayout(String simulationID, SimulationStatus status) {

        this.simulationID = simulationID;
        this.status = status;
        this.setWidth100();
        this.setHeight(60);
        this.setMembersMargin(5);

        VLayout barBorderLayout = new VLayout();
        barBorderLayout.setWidth100();
        barBorderLayout.setHeight(30);
        barBorderLayout.setBorder("1px solid #CCCCCC");
        barBorderLayout.setBackgroundColor("#FFFFFF");

        barLayout = new VLayout();
        barLayout.setWidth(1);
        barLayout.setHeight100();
        barLayout.setMargin(3);
        barLayout.setOpacity(80);
        barLayout.setBackgroundColor(JobStatus.Running.getColor());

        progressLabel = new Label("<b>0%</b>");
        progressLabel.setWidth(30);
        progressLabel.setHeight(25);
        progressLabel.setLayoutAlign(Alignment.CENTER);

        barLayout.addMember(progressLabel);
        barBorderLayout.addMember(barLayout);
        this.addMember(barBorderLayout);

        statusLabel = new Label("Loading information...");
        statusLabel.setWidth100();
        statusLabel.setHeight(22);
        statusLabel.setBorder("1px solid #CCCCCC");
        statusLabel.setBackgroundColor("#FFFFFF");
        statusLabel.setPadding(3);
        statusLabel.setOverflow(Overflow.AUTO);
        statusLabel.setValign(VerticalAlignment.TOP);
        this.addMember(statusLabel);

        loadData();
    }

    protected void loadData() {

        AsyncCallback<List<Activity>> callback = new AsyncCallback<List<Activity>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load progress:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Activity> result) {

                double completed = 0;
                double active = 0;
                StringBuilder sb = new StringBuilder();

                for (Activity processor : result) {
                    
                    if (processor.getStatus() == ProcessorStatus.Completed) {
                        completed++;
                    
                    } else if (processor.getStatus() == ProcessorStatus.Unstarted) {
                        active++;
                    
                    } else if (processor.getStatus() == ProcessorStatus.Active) {
                        
                        completed += ((double) processor.getCompleted()) / (processor.getCompleted() + processor.getQueued());
                        active += ((double) processor.getQueued()) / (processor.getCompleted() + processor.getQueued());
                        
                        if (sb.length() == 0) {
                            sb.append("<font color=\"#666666\">Running ");
                        } else {
                            sb.append(", ");
                        }
                        sb.append(processor.getName());
                    }
                }
                if (completed > 0 || active > 0) {
                    int progress = (int) ((completed * 100) / (active + completed));
                    
                    if (progress == 100) {
                        barLayout.setBackgroundColor(JobStatus.Completed.getColor());
                        barLayout.setWidth100();
                        statusLabel.setContents("<font color=\"#666666\">Execution completed!</font>");
                    } else {
                        if (status == SimulationStatus.Killed) {
                            barLayout.setBackgroundColor(JobStatus.Failed.getColor());
                            sb.append(". <b>Execution killed!</b>");
                        } else if (status == SimulationStatus.Failed) {
                            barLayout.setBackgroundColor(JobStatus.Failed.getColor());
                            sb.append(". <b>Execution Failed!</b>");
                        }
                        sb.append("</font>");
                        barLayout.setWidth(progress + "%");
                        statusLabel.setContents(sb.toString());
                    }
                    progressLabel.setContents("<b>" + progress + "%</b>");
                }
            }
        };
        WorkflowService.Util.getInstance().getProcessors(simulationID, callback);
    }

    public void update() {
        loadData();
    }
}
