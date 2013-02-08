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
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.FlowLayout;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class CommandLayout extends VLayout {

    private CommandLayout instance;
    private FlowLayout layout;
    private int jobsID;
    private Map<String, JobLayout> jobsMap;

    public CommandLayout(String command) {

        this.instance = this;
        this.jobsID = 1;
        this.jobsMap = new HashMap<String, JobLayout>();

        this.setWidth100();
        this.setHeight100();
        this.setMembersMargin(3);

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight(20);
        hLayout.setBorder("1px solid #CCCCCC");
        hLayout.setBackgroundColor("#F7F7F7");
        hLayout.setPadding(5);

        Label titleLabel = WidgetUtil.getLabel("<b>" + command.replace(".sh", "") + "</b>", 18);
        titleLabel.setWidth100();
        hLayout.addMember(titleLabel);
        hLayout.addMember(WidgetUtil.getSpaceLabel(16));
        hLayout.addMember(WidgetUtil.getIconLabel(ApplicationConstants.ICON_MONITOR_SEARCH, "Filter", 16, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                FilterJobsLayout.getInstance(event.getX(), event.getY(), instance).show();
            }
        }));

        this.addMember(hLayout);

        this.layout = new FlowLayout();
        this.layout.setWidth100();
        this.layout.setHeight100();
        this.layout.setOverflow(Overflow.VISIBLE);
        this.addMember(layout);
    }

    /**
     * Adds a job to the tile.
     *
     * @param simulationID Simulation identification
     * @param job Job object
     */
    public void addJob(String simulationID, Job job) {

        String params = job.getParameters();
        if (jobsMap.containsKey(job.getParameters())) {
            jobsMap.get(params).updateStatus(job.getStatus());
        } else {
            JobLayout jobLayout = new JobLayout(jobsID++, simulationID, job);
            jobsMap.put(params, jobLayout);
            layout.addTile(jobLayout);
        }
        
//        boolean exist = false;
//        for (Canvas canvas : layout.getChildren()) {
//            if (canvas instanceof JobLayout) {
//                JobLayout jobLayout = (JobLayout) canvas;
//                if (job.getParameters().equals(jobLayout.getParameters())) {
//                    exist = true;
//                    jobLayout.updateStatus(job.getStatus());
//                    break;
//                }
//            }
//        }
//        if (!exist) {
//            layout.addTile(new JobLayout(jobsID++, simulationID, job));
//        }
    }

    /**
     * Filters jobs according to a status.
     * 
     * @param status Status to be filtered by. Value <b>All</b> will display all jobs.
     */
    public void filter(String status) {

        if (status.equals("All")) {
            for (Canvas canvas : layout.getChildren()) {
                if (canvas instanceof JobLayout) {
                    canvas.show();
                }
            }
        } else {
            JobStatus jobStatus = JobStatus.valueOf(status);
            for (Canvas canvas : layout.getChildren()) {
                if (canvas instanceof JobLayout) {
                    JobLayout jobLayout = (JobLayout) canvas;
                    if (jobLayout.getStatus() == jobStatus) {
                        canvas.show();
                    } else {
                        canvas.hide();
                    }
                }
            }
        }
    }
}
