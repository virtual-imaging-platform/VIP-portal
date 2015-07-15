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
                StringBuilder sb = new StringBuilder();

                for (Activity processor : result) {

                    if (processor.getName().toLowerCase().contains("gate")) {

                        if (processor.getStatus() == ProcessorStatus.Completed || runnedParticles >= particles) {
                            completed += gateFactor;
                        } else {
                            sb.append(sb.length() == 0 ? "<font color=\"#666666\">Running gate" : ", gate");
                            sb.append(" (").append(runnedParticles).append(" out of ").append(particles).append(" particles)");
                            completed += gateFactor * (runnedParticles / particles);
                            active += gateFactor * ((particles - runnedParticles) / particles);
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
                    } else {
                        if (status == SimulationStatus.Killed) {
                            barLayout.setBackgroundColor(JobStatus.Failed.getColor());
                            sb.append(". <b>Simulation killed!</b>");
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
