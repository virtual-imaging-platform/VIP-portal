package fr.insalyon.creatis.vip.application.client.view.system.tags;

import com.smartgwt.client.widgets.layout.HLayout;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

public class ManageTagsTab extends AbstractManageTab {

    private TagLayout tagLayout;
    private EditTagLayout editLayout;
    
    public ManageTagsTab() {
        
        super(ApplicationConstants.ICON_TAG, ApplicationConstants.APP_TAG, ApplicationConstants.TAB_MANAGE_TAG);
        
        tagLayout = new TagLayout();
        editLayout = new EditTagLayout();
        
        HLayout hLayout = new HLayout(5);
        hLayout.addMember(tagLayout);
        hLayout.addMember(editLayout);
        
        vLayout.addMember(hLayout);
    }
    
    public void loadTags() {
        tagLayout.loadData();
    }

    public void setTag(Tag tag, String[] appVersions) {
        editLayout.setTag(tag, appVersions);
    }
}
