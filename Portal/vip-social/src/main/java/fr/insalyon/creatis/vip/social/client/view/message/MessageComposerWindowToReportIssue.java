/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.social.client.view.message;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.common.AbstractComposeWindow;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class MessageComposerWindowToReportIssue extends AbstractComposeWindow {

    private TextItem vipEmail;
    private TextItem subjectItem;
    List<String> recepients;

    public MessageComposerWindowToReportIssue(List<String> workflowID, List<String> simulationNames) {

        super("Compose New Message");
        loadVIPAdmin();
        configureForm(workflowID, simulationNames);

    }

    private void configureForm(final List<String> workflowID, final List<String> simulationNames) {

        HLayout buttonsLayout = new HLayout(5);
        Label sendLabel = WidgetUtil.getLabel("Send Message", SocialConstants.ICON_SEND, 15, Cursor.HAND);
        sendLabel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    sendMessage(recepients.toArray(new String[0]),
                            subjectItem.getValueAsString().trim(),
                            richTextEditor.getValue(), workflowID, simulationNames);
                }
            }
        });
        buttonsLayout.addMember(sendLabel);
        vLayout.addMember(buttonsLayout);

        vipEmail = FieldUtil.getTextItem(350, true, "To", "[0-9.,A-Za-z-@+/_(): ]");
        vipEmail.setValue("vip-support@creatis.insa-lyon.fr");
        vipEmail.setCanEdit(false);

        subjectItem = FieldUtil.getTextItem(350, true, "Subject", "[0-9.,A-Za-z-+/_(): ]");

        form = FieldUtil.getForm(vipEmail, subjectItem);
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

    private void loadVIPAdmin() {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get users list:<br />" + caught.getMessage());
            }

            public void onSuccess(List<User> result) {

                recepients = new ArrayList<String>();

                for (User user : result) {
                    if (user.isSystemAdministrator()) {
                        recepients.add(user.getEmail());
                    }
                }
                modal.hide();

            }
        };
        service.getUsers(callback);
        modal.show("Loading users list...", true);
    }

    private void sendMessage(String[] recipients, String subject, String message, List<String> workflowID, List<String> simulationNames) {

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
        service.sendMessageToVip(recipients, subject, message, workflowID, simulationNames, callback);
        modal.show("Sending message...", true);
    }

    public void setSubjectValue(String value) {
        subjectItem.setValue(value);
    }

    public void setTextMessage(String message) {
        richTextEditor.setValue(message);

    }
}
