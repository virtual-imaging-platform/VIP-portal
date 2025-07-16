package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractCornerTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.LogsLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabGeneralTab extends AbstractCornerTab {

    private GateLabGeneralLayout generalLayout;
    private GateLabDownloadsLayout downloadLayout;
    
    public GateLabGeneralTab(String simulationID, SimulationStatus status, String date) {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_GENERAL));
        this.setPrompt("General Information");

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        
        generalLayout = new GateLabGeneralLayout(simulationID, status, date);
        vLayout.addMember(generalLayout);
        
        downloadLayout = new GateLabDownloadsLayout(simulationID);
        vLayout.addMember(downloadLayout);
        
        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.isGroupAdmin()) {
            vLayout.addMember(new LogsLayout(simulationID));
        }
        
        //TODO: Stop and Merge

        this.setPane(vLayout);
    }

    @Override
    public void update() {
        generalLayout.update();
        downloadLayout.update();
    }
}
