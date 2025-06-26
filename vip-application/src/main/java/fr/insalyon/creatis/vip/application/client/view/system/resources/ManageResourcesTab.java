package fr.insalyon.creatis.vip.application.client.view.system.resources;

import java.util.Map;

import com.smartgwt.client.widgets.layout.HLayout;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

public class ManageResourcesTab extends AbstractManageTab {

    private ResourceLayout resourceLayout;
    private EditResourceLayout editLayout;
    
    public ManageResourcesTab() {
        
        super(ApplicationConstants.ICON_RESOURCE, ApplicationConstants.APP_RESOURCE, ApplicationConstants.TAB_MANAGE_RESOURCE);
        
        resourceLayout = new ResourceLayout();
        editLayout = new EditResourceLayout();
        
        HLayout hLayout = new HLayout(5);
        hLayout.addMember(resourceLayout);
        hLayout.addMember(editLayout);
        
        vLayout.addMember(hLayout);
        setResource(null, false, null, null, null, null);
    }
    
    public void loadResources() {
        resourceLayout.loadData();
    }

    public void setResource(String name, boolean status, String type, String configuration, String[] engines, Map<String, String> groups) {
        editLayout.setResource(name, status, type, configuration, engines, groups);
    }
}
