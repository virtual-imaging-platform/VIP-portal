package fr.insalyon.creatis.vip.social.client.view.message.sent;

import java.util.Date;
import java.util.List;

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
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.common.AbstractMainLayout;
import fr.insalyon.creatis.vip.social.client.view.common.MoreDataBoxLayout;
import fr.insalyon.creatis.vip.social.models.Message;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SentMessageLayout extends AbstractMainLayout {

    private VLayout messagesLayout;
    private Date lastDate;
    private MoreDataBoxLayout moreDataBoxLayout;

    public SentMessageLayout() {

        super(SocialConstants.MENU_MESSAGE_SENT, SocialConstants.ICON_MESSAGE_SENT);

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

    public void loadData(Date date) {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<List<Message>> callback = new AsyncCallback<List<Message>>() {

            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of messages:<br />" + caught.getMessage());
            }

            public void onSuccess(List<Message> result) {

                if (!result.isEmpty()) {

                    for (Message message : result) {
                        messagesLayout.addMember(new SentMessageBoxLayout(message));
                        lastDate = message.getPostedDate();
                    }
                    if (result.size() == SocialConstants.MESSAGE_MAX_DISPLAY) {
                        messagesLayout.addMember(moreDataBoxLayout);
                    }
                }
            }
        };
        service.getSentMessagesByUser(date, callback);
    }

    private void configureButtons() {

        if (CoreModule.user.isSystemAdministrator()) {
            HLayout buttonsLayout = new HLayout(5);
            buttonsLayout.setWidth100();

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
}
