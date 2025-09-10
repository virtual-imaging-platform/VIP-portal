package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractCornerTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.GeneralLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.InputTreeGrid;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.LogsLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.OutputTreeGrid;
import fr.insalyon.creatis.vip.core.client.CoreModule;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GeneralTab extends AbstractCornerTab {

    private GeneralLayout generalLayout;
    private InputTreeGrid inputTreeGrid;
    private OutputTreeGrid outputTreeGrid;
    
    public GeneralTab(String simulationID, SimulationStatus status) {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_GENERAL));
        this.setPrompt("General Information");

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);

        generalLayout = new GeneralLayout(simulationID, status);
        vLayout.addMember(generalLayout);

        HLayout hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);

        // Left column
        VLayout leftLayout = new VLayout(10);
        leftLayout.setWidth("50%");
        leftLayout.setHeight100();
        leftLayout.setOverflow(Overflow.AUTO);

        inputTreeGrid = new InputTreeGrid(simulationID);
        leftLayout.addMember(inputTreeGrid);
        
        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.isGroupAdmin() || CoreModule.user.isDeveloper()) {
            leftLayout.addMember(new LogsLayout(simulationID));
        }

        // Right column
        VLayout rightLayout = new VLayout(10);
        rightLayout.setWidth("50%");
        rightLayout.setHeight100();
        rightLayout.setOverflow(Overflow.AUTO);
        
        outputTreeGrid = new OutputTreeGrid(simulationID);
        rightLayout.addMember(outputTreeGrid);

        hLayout.addMember(leftLayout);
        hLayout.addMember(rightLayout);
        vLayout.addMember(hLayout);

        this.setPane(vLayout);
    }

    @Override
    public void update() {

        generalLayout.update();
        inputTreeGrid.update();
        outputTreeGrid.update();
    }
}
