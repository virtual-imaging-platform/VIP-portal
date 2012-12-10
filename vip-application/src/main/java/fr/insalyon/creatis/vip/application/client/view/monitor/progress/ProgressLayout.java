/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor.progress;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
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

        AsyncCallback<List<Processor>> callback = new AsyncCallback<List<Processor>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load progress:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Processor> result) {

                double completed = 0;
                double active = 0;
                StringBuilder sb = new StringBuilder();

                for (Processor processor : result) {
                    
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
                        statusLabel.setContents("<font color=\"#666666\">Simulation completed!</font>");
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

    public void update() {
        loadData();
    }
}
