package fr.insalyon.creatis.vip.application.client.view.system.engines;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageEnginesTab extends AbstractManageTab {

    private EnginesLayout enginesLayout;
    private EditEngineLayout editLayout;
    
    public ManageEnginesTab() {

        super(ApplicationConstants.ICON_ENGINE, ApplicationConstants.APP_ENGINE, ApplicationConstants.TAB_MANAGE_ENGINE);
        
        enginesLayout = new EnginesLayout();
        editLayout = new EditEngineLayout();
        
        HLayout hLayout = new HLayout(5);
        hLayout.addMember(enginesLayout);
        hLayout.addMember(editLayout);
        
        vLayout.addMember(hLayout);
    }
    
    public void loadEngines() {
        enginesLayout.loadData();
    }

    public void setEngine(String name, String endpoint, String status) {
        editLayout.setEngine(name, endpoint, status);
    }
}
