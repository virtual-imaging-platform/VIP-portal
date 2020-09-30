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
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.types.BackgroundRepeat;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobLayout extends VLayout {

    private int jobID;
    private String simuID;
    private JobStatus status;
    private HLayout statusLayout;

    private Set<DebugLayout> debugLayouts;

    public JobLayout(final int jobID, String simulationID, JobStatus status) {

        this.jobID = jobID;
        this.simuID = simulationID;
        this.status = status;
        this.debugLayouts = new HashSet<>();

        this.setWidth(40);
        this.setHeight(40);
        this.setBorder("1px solid #F2F2F2");
        this.setPadding(3);
        this.setMembersMargin(3);
        this.setBackgroundImage(ApplicationConstants.ICON_MONITOR_JOB);
        this.setBackgroundRepeat(BackgroundRepeat.NO_REPEAT);
        this.setBackgroundPosition("center center");
        this.setCursor(Cursor.HAND);

        this.addClickHandler(event -> {
            DebugLayout debugLayout = new DebugLayout(simuID, jobID);
            debugLayout.show();
            debugLayouts.add(debugLayout);
        });
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                setBackgroundColor("#F7F7F7");
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                setBackgroundColor("#FFFFFF");
            }
        });

        Label jobIDLabel = WidgetUtil.getLabel("#" + jobID, 25, Cursor.HAND);
        this.addMember(jobIDLabel);

        statusLayout = new HLayout();
        statusLayout.setWidth100();
        statusLayout.setHeight(2);
        statusLayout.setBackgroundColor(status.getColor());

        this.addMember(statusLayout);
    }

    public void updateStatus(JobStatus jobStatus) {

        if (status != jobStatus) {
            status = jobStatus;
            statusLayout.setBackgroundColor(status.getColor());
        }
    }

    public int getJobID() {
        return jobID;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (DebugLayout debugLayout : debugLayouts) {
            if ( debugLayout.isCreated()) {
                  debugLayout.destroy();
            }
        }
        debugLayouts.clear();
    }
}
