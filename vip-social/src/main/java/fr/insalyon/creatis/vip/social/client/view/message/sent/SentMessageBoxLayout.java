package fr.insalyon.creatis.vip.social.client.view.message.sent;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SentMessageBoxLayout extends HLayout {

    private Message message;

    public SentMessageBoxLayout(Message message) {

        this.message = message;

        this.setMembersMargin(2);
        this.setWidth100();
        this.setHeight(50);
        if (message.isRead()) {
            this.setBackgroundColor("#FFFFFF");
        } else {
            this.setBackgroundColor("#D8E8EB");
        }

        configureImageLayout();
        configureMainLayout();
        configureDateLayout();
        configureActionLayout();
    }

    /**
     * Configures the image layout.
     */
    private void configureImageLayout() {

        VLayout imgLayout = new VLayout();
        imgLayout.setPadding(2);
        imgLayout.setWidth(50);
        imgLayout.setHeight(50);
        imgLayout.setAlign(Alignment.CENTER);

        imgLayout.addMember(new Img(SocialConstants.ICON_USER, 48, 48));
        this.addMember(imgLayout);
    }

    /**
     * Configures the main layout of a message.
     */
    private void configureMainLayout() {

        VLayout mainLayout = new VLayout(2);
        mainLayout.setWidth("*");
        mainLayout.setHeight(50);
        mainLayout.setAlign(Alignment.CENTER);

        StringBuilder sb = new StringBuilder();
        int count = 0;
        int numOfReceivers = message.getReceivers().length;
        for (User user : message.getReceivers()) {
            if (count == SocialConstants.MESSAGE_MAX_RECEIVERS_DISPLAY) {
                sb.append(" and ").append(numOfReceivers - count).append(" other users");
                break;
            } else {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(user.getFullName());
                count++;
            }
        }

        mainLayout.addMember(WidgetUtil.getLabel("<strong>" + sb.toString() + "</strong>: " 
                + message.getTitle(), 15, Cursor.HAND));

        String messageSummary = message.getMessage().substring(0, Math.min(50, message.getMessage().length()));
        mainLayout.addMember(WidgetUtil.getLabel("<font color=\"#666666\">"
                + messageSummary + "...</font>", 15, Cursor.HAND));

        mainLayout.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                new SentMessageViewerWindow(message).show();
            }
        });

        this.addMember(mainLayout);
    }

    /**
     * Configures message posted date.
     */
    private void configureDateLayout() {

        VLayout dateLayout = new VLayout(5);
        dateLayout.setHeight(50);
        dateLayout.setWidth(150);
        dateLayout.setAlign(VerticalAlignment.TOP);

        dateLayout.addMember(WidgetUtil.getLabel("<font color=\"#666666\">"
                + message.getPosted() + "</font>", 15));

        this.addMember(dateLayout);
    }

    /**
     * Configures actions related to a message.
     */
    private void configureActionLayout() {

        VLayout actionLayout = new VLayout(5);
        actionLayout.setHeight(50);
        actionLayout.setWidth(30);
        actionLayout.setAlign(VerticalAlignment.TOP);

        Img removeImg = new Img(SocialConstants.ICON_REMOVE, 16, 16);
        removeImg.setCursor(Cursor.HAND);
        removeImg.setPrompt("Remove");
        removeImg.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                remove();
            }
        });
        actionLayout.addMember(removeImg);

        this.addMember(actionLayout);
    }

    private void remove() {

        SC.confirm("Do you really want to remove this message?", new BooleanCallback() {

            public void execute(Boolean value) {

                if (value != null && value) {
                    SocialServiceAsync service = SocialService.Util.getInstance();
                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            Layout.getInstance().setWarningMessage("Unable to remove message:<br />" + caught.getMessage());
                        }

                        public void onSuccess(Void result) {
                            destroy();
                        }
                    };
                    service.removeMessage(message.getId(), callback);
                }
            }
        });
    }
}
