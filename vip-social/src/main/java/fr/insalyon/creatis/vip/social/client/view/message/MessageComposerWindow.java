package fr.insalyon.creatis.vip.social.client.view.message;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.common.AbstractComposeWindow;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class MessageComposerWindow extends AbstractComposeWindow {

    private SelectItem usersPickList;
    private TextItem subjectItem;

    private Boolean sendCopyToSupport = false;

    public MessageComposerWindow() {

        super("Compose New Message");

        configureForm();
        loadUsers();
    }

    private void configureForm() {

        HLayout buttonsLayout = new HLayout(5);
        Label sendLabel = WidgetUtil.getLabel("Send Message", SocialConstants.ICON_SEND, 15, Cursor.HAND);
        sendLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    sendMessage(usersPickList.getValues(),
                            subjectItem.getValueAsString().trim(),
                            richTextEditor.getValue());
                }
            }
        });
        buttonsLayout.addMember(sendLabel);
        vLayout.addMember(buttonsLayout);

        usersPickList = new SelectItem();
        usersPickList.setTitle("To");
        usersPickList.setMultiple(true);
        usersPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        usersPickList.setWidth(350);
        usersPickList.setRequired(true);

        subjectItem = FieldUtil.getTextItem(350, true, "Subject", "[0-9.,A-Za-z-+/_(): ]");

        form = FieldUtil.getForm(usersPickList, subjectItem);
        form.setWidth(500);
        vLayout.addMember(form);
        richTextEditor = new RichTextEditor();
        richTextEditor.setHeight100();
        richTextEditor.setOverflow(Overflow.HIDDEN);
        richTextEditor.setCanDragResize(true);
        richTextEditor.setShowEdges(true);
        richTextEditor.setControlGroups("fontControls", "formatControls",
                "styleControls", "editControls", "colorControls", "insertControls");
        vLayout.addMember(richTextEditor);
    }

    private void loadUsers() {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get users list:<br />" + caught.getMessage());
            }

            public void onSuccess(List<User> result) {

                LinkedHashMap<String, String> usersMap = new LinkedHashMap<String, String>();
                usersMap.put("All", "All");
                for (User user : result) {
                    usersMap.put(user.getEmail(), user.getFullName());

                }
                usersPickList.setValueMap(usersMap);
                modal.hide();
            }
        };
        service.getUsers(callback);
        modal.show("Loading users list...", true);
    }

    public void setUsersPickerListValue(final String userFullName) {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get users list:<br />" + caught.getMessage());
            }

            public void onSuccess(List<User> result) {
                for (User user : result) {
                    if (user.getFullName().equals(userFullName)) {
                        usersPickList.setValue(user.getEmail());
                    }

                }
                modal.hide();
            }
        };
        service.getUsers(callback);

    }

    private void sendMessage(String[] recipients, String subject, String message) {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to send message:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                destroy();
                Layout.getInstance().setNoticeMessage("Message successfully sent.");
            }
        };
        if (sendCopyToSupport) {
            service.sendMessageWithSupportCopy(recipients, subject, message, callback);
        } else {
            service.sendMessage(recipients, subject, message, callback);
        }
        modal.show("Sending message...", true);
    }

    public void setSubjectValue(String value) {
        subjectItem.setValue(value);
    }

    public void setTextMessage(String message) {
        richTextEditor.setValue(message);
    }

    public void setSendCopyToSupport(Boolean sendCopyToSupport) {
        this.sendCopyToSupport = sendCopyToSupport;
    }
}
