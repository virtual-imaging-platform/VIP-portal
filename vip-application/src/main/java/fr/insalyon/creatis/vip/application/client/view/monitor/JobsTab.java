package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractCornerTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobsLayout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobsTab extends AbstractCornerTab {

    private JobsLayout jobsLayout;

    public JobsTab(String simulationID, boolean completed) {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_SUMMARY));
        this.setPrompt("Jobs Summary");

        VLayout layout = new VLayout(10);
        layout.setWidth100();
        layout.setHeight100();
        layout.setOverflow(Overflow.AUTO);

        jobsLayout = new JobsLayout(simulationID);
        layout.addMember(jobsLayout);

        setPane(layout);
        
        if (completed) {
            jobsLayout.update();
        }
    }

    @Override
    public void destroy() {
        jobsLayout.destroy();
    }

    @Override
    public void update() {
        jobsLayout.update();
    }
}
