package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobInfoLayout extends VLayout {

    private JobInfoButton infoButton;

    public JobInfoLayout() {

        this.setWidth100();
        this.setHeight100();

        infoButton = new JobInfoButton();
        this.addMember(infoButton);
    }

    public void setStatus(int status) {
        infoButton.setStatus(status);
    }

    public JobInfoButton getInfoButton() {
        return infoButton;
    }
}
