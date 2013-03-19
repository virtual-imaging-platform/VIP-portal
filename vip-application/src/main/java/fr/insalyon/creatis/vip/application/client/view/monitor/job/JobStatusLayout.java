/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.FlowLayout;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobStatusLayout extends VLayout {

    private String simulationID;
    private JobStatus status;
    private Set<Integer> jobsList;
    private Label titleLabel;
    private FlowLayout layout;
    private int count;

    public JobStatusLayout(String simulationID, JobStatus status, Set<Integer> jobsList) {

        this.simulationID = simulationID;
        this.status = status;
        this.jobsList = jobsList;

        this.setWidth100();

        HLayout hLayout = new HLayout();
        hLayout.setWidth100();
        hLayout.setHeight(20);
        hLayout.setBorder("1px solid #CCCCCC");
        hLayout.setBackgroundColor("#F7F7F7");
        hLayout.setPadding(5);

        titleLabel = WidgetUtil.getLabel("<b>" + status.getDescription()
                + "</b> (" + jobsList.size() + ")", 18, Cursor.HAND);
        titleLabel.setWidth100();
        titleLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        hLayout.addMember(titleLabel);
        this.addMember(hLayout);
    }

    private void loadData() {

        if (layout == null) {
            layout = new FlowLayout();
            layout.setWidth100();
            layout.setHeight100();
            layout.setOverflow(Overflow.VISIBLE);
            this.addMember(layout);

            count = 0;
            final Iterator<Integer> iterator = jobsList.iterator();
            new Timer() {
                @Override
                public void run() {

                    if (count >= jobsList.size()) {
                        cancel();
                    } else {
                        layout.addTile(new JobLayout(iterator.next(), simulationID, status));
                        count++;
                    }
                }
            }.scheduleRepeating(20);

        } else {
            layout.destroy();
            layout = null;
        }
    }

    public void add(int jobID) {

        jobsList.add(jobID);
        titleLabel.setContents("<b>" + status.getDescription() + "</b> (" + jobsList.size() + ")");

        if (layout != null) {
            layout.addTile(new JobLayout(jobID, simulationID, status));
        }
    }

    public void remove(int jobID, JobStatus status) {

        jobsList.remove(jobID);
        titleLabel.setContents("<b>" + status.getDescription() + "</b> (" + jobsList.size() + ")");

        if (layout != null) {
            for (Canvas canvas : layout.getChildren()) {
                if (canvas instanceof JobLayout) {
                    JobLayout jl = (JobLayout) canvas;
                    if (jl.getJobID() == jobID) {
                        jl.destroy();
                        break;
                    }
                }
            }
        }
    }
}
