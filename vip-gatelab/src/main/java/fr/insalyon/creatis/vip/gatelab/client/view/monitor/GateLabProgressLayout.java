package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.bean.Activity;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProgressLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabProgressLayout extends ProgressLayout {

    private final int gateFactor = 25;
    private double particles;
    private double runnedParticles;

    public GateLabProgressLayout(String simulationID, SimulationStatus status) {

        super(simulationID, status);
        particles = 0;
        runnedParticles = 0;
    }

    @Override
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
                boolean singleGateCompleted = false;
                StringBuilder sb = new StringBuilder();

                for (Activity processor : result) {

                    if (processor.getName().matches("gate")) {

                        if (processor.getStatus() == ProcessorStatus.Completed || runnedParticles >= particles) {
                            completed += gateFactor;
                        } else {
                            sb.append(sb.length() == 0 ? "<font color=\"#666666\">Running gate" : ", gate");
                            sb.append(" (").append(runnedParticles).append(" out of ").append(particles).append(" particles)");
                            completed += gateFactor * (runnedParticles / particles);
                            active += gateFactor * ((particles - runnedParticles) / particles);
                        }

                    } else if (processor.getName().matches("singleGate")) {
                        if (processor.getStatus() == ProcessorStatus.Completed) {
                            singleGateCompleted = true;
                        }
                        
                    } else if (processor.getStatus() == ProcessorStatus.Completed) {
                        completed++;

                    } else if (processor.getStatus() == ProcessorStatus.Unstarted) {
                        active++;

                    } else if (processor.getStatus() == ProcessorStatus.Active) {

                        sb.append(sb.length() == 0 ? "<font color=\"#666666\">Running " : ", ");
                        sb.append(processor.getName());

                        int factor = processor.getName().toLowerCase().contains("merge") ? 2 : 1;
                        double total = processor.getCompleted() + processor.getQueued() + processor.getFailed();
                        completed += factor * ((processor.getCompleted() + processor.getFailed()) / total);
                        active += factor * (processor.getQueued() / total);
                    }
                }
                if (completed > 0 || active > 0) {
                    int progress = (int) ((completed * 100) / (active + completed));
                    if (progress == 100) {
                        barLayout.setBackgroundColor(JobStatus.Completed.getColor());
                        barLayout.setWidth100();
                        statusLabel.setContents("<font color=\"#666666\">Simulation completed! ("
                                + runnedParticles + " out of " + particles + " particles)</font>");
                    } else if (singleGateCompleted) {
                        progress = 100;
                        barLayout.setBackgroundColor(JobStatus.Completed.getColor());
                        barLayout.setWidth100();
                        statusLabel.setContents("<font color=\"#666666\">Simulation completed!</font>");
                    }
                    else {
                        if (status == SimulationStatus.Killed) {
                            barLayout.setBackgroundColor(JobStatus.Failed.getColor());
                            sb.append(". <b>Simulation killed!</b>");
                        } else if (status == SimulationStatus.Failed) {
                            barLayout.setBackgroundColor(JobStatus.Failed.getColor());
                            sb.append(". <b>Simulation Failed!</b>");
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

    public void update(double particles, double runnedParticles) {

        this.runnedParticles = runnedParticles;
        this.particles = particles;
        loadData();
    }
}
