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
