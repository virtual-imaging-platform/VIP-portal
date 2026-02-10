package fr.insalyon.creatis.vip.social.client.view.message.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.view.common.AbstractMainLayout;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupsLayout extends AbstractMainLayout {

    private DynamicForm form;
    private SelectItem groupsItem;
    private VLayout groupLayout;
    
    public GroupsLayout() {

        super(SocialConstants.MENU_GROUP, SocialConstants.ICON_GROUP);

        configureGroupsList();
        
        groupLayout = new VLayout();
        groupLayout.setWidth100();
        groupLayout.setHeight100();
        this.addMember(groupLayout);
        
        loadData();
    }

    @Override
    public void loadData() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        AsyncCallback<List<Group>> callback = new AsyncCallback<>() {

            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get user's groups:<br />" + caught.getMessage());
            }

            public void onSuccess(List<Group> result) {
                groupsItem.setValueMap(result.stream().map(Group::getName).toArray(String[]::new));
            }
        };
        service.getUserGroups(callback);
    }

    private void configureGroupsList() {

        form = new DynamicForm();
        form.setMargin(5);
        form.setNumCols(2);
        form.setHeight(20);
        form.setWidth(400);
        
        groupsItem = new SelectItem("groupsItem", "Your Groups");
        groupsItem.setEmptyPickListMessage("You have no groups");
        groupsItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                
                groupLayout.removeMembers(groupLayout.getMembers());
                groupLayout.addMember(new GroupLayout(groupsItem.getValueAsString()));
            }
        });
        
        form.setFields(groupsItem);
        
        HLayout groupsLayout = new HLayout(5);       
        groupsLayout.addMember(form);
        this.addMember(groupsLayout);
    }
}
