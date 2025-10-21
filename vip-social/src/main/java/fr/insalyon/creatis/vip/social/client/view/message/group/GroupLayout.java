package fr.insalyon.creatis.vip.social.client.view.message.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.common.MoreDataBoxLayout;
import fr.insalyon.creatis.vip.social.models.GroupMessage;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupLayout extends VLayout {

    private String groupName;
    private VLayout messagesLayout;
    private Date lastDate;
    private MoreDataBoxLayout moreDataBoxLayout;

    public GroupLayout(String groupName) {

        this.groupName = groupName;

        this.setWidth100();
        this.setHeight100();
        this.setPadding(5);
        this.setMembersMargin(10);
        this.setOverflow(Overflow.AUTO);
        this.setAlign(VerticalAlignment.TOP);
        this.setBackgroundColor("#F2F2F2");

        this.addMember(WidgetUtil.getLabel("<p style=\"font-size: 13pt\">Group: "
                + "<strong>" + groupName + "</strong></p>", null, 15));

        configureButtons();

        messagesLayout = new VLayout(5);
        messagesLayout.setWidth100();
        messagesLayout.setHeight100();
        messagesLayout.setPadding(5);
        messagesLayout.setOverflow(Overflow.AUTO);
        messagesLayout.setAlign(VerticalAlignment.TOP);
        messagesLayout.setBackgroundColor("#F2F2F2");

        moreDataBoxLayout = new MoreDataBoxLayout("messages");
        moreDataBoxLayout.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                messagesLayout.removeMember(moreDataBoxLayout);
                loadData(lastDate);
            }
        });

        loadData();

        this.addMember(messagesLayout);
    }

    public void loadData() {

        messagesLayout.removeMembers(messagesLayout.getMembers());
        loadData(new Date());
    }

    private void loadData(Date date) {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<List<GroupMessage>> callback = new AsyncCallback<List<GroupMessage>>() {
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of messages:<br />" + caught.getMessage());
            }

            public void onSuccess(List<GroupMessage> result) {

                if (!result.isEmpty()) {

                    for (GroupMessage message : result) {
                        messagesLayout.addMember(new GroupMessageBoxLayout(message));
                        lastDate = message.getPostedDate();
                    }
                    if (result.size() == SocialConstants.MESSAGE_MAX_DISPLAY) {
                        messagesLayout.addMember(moreDataBoxLayout);
                    }
                }
            }
        };
        service.getGroupMessages(groupName, date, callback);
    }

    private void configureButtons() {

        HLayout buttonsLayout = new HLayout(5);
        if (CoreModule.user.getLevel() != UserLevel.Beginner) {

            buttonsLayout.setWidth100();

            Label composeLabel = WidgetUtil.getLabel("New Message", SocialConstants.ICON_COMPOSE, 15, Cursor.HAND);
            composeLabel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    new GroupComposerWindow(groupName).show();
                }
            });
            buttonsLayout.addMember(composeLabel);
        }

        Label refreshLabel = WidgetUtil.getLabel("Refresh", SocialConstants.ICON_REFRESH, 15, Cursor.HAND);
        refreshLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        buttonsLayout.addMember(refreshLabel);

        this.addMember(buttonsLayout);
    }
}
