package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupLayout extends AbstractFormLayout {

    private VLayout vLayout;
    private Label messageLabel;

    public GroupLayout() {

        super("350", "501");
        addTitle("Groups", CoreConstants.ICON_GROUP);

        messageLabel = WidgetUtil.getLabel("", 15);
        messageLabel.setBackgroundColor("#F79191");
        this.addMember(messageLabel);
        
        vLayout = new VLayout(5);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        this.addMember(vLayout);

        this.addMember(WidgetUtil.getLabel("<font color=\"#666666\"><b>Note</b>: "
                + "modifications will only take effect once you reload the portal.</font>", 30));

        loadData();
    }

    private void loadData() {

        final ArrayList<String> groups = new ArrayList<String>();
        
        final ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        
        final AsyncCallback<List<Group>> cbPublicGroups = new AsyncCallback<List<Group>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load public groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {
                for (Group group : result) {
                    if( ! groups.contains(group.getName()) && ! group.isAuto())
                        vLayout.addMember(new GroupBoxLayout(group.getName(),
                                group.isPublicGroup(), GROUP_ROLE.None));
                }
            }
        };
        final AsyncCallback<Map<Group, GROUP_ROLE>> callback = new AsyncCallback<Map<Group, GROUP_ROLE>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Group, GROUP_ROLE> result) {
                
                for (Group group : result.keySet()) {
                    if ( ! group.isAuto()) {
                        vLayout.addMember(new GroupBoxLayout(group.getName(),
                        group.isPublicGroup(), result.get(group)));
                        groups.add(group.getName());
                    }
                } if (result.isEmpty()) {
                    messageLabel.setVisible(true);
                    messageLabel.setContents("You are not a member of any group. The following public groups will grant you access to applications. Please choose a group to be able to access the home tab directly at your next login.");
                } else {
                    messageLabel.setVisible(false);
                }
                service.getPublicGroups(cbPublicGroups);
            }
        };
        service.getUserGroups(null, callback);
    }
}
