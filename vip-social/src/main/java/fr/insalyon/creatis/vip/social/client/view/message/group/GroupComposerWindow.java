package fr.insalyon.creatis.vip.social.client.view.message.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.common.AbstractComposeWindow;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupComposerWindow extends AbstractComposeWindow {

    private String groupName;
    private TextItem groupItem;
    private TextItem subjectItem;
    
    public GroupComposerWindow(String groupName) {

        super("Compose New Message to '" + groupName + "'");
        
        this.groupName = groupName;

        configureForm();
    }

    private void configureForm() {

        HLayout buttonsLayout = new HLayout(5);
        Label sendLabel = WidgetUtil.getLabel("Send Message", SocialConstants.ICON_SEND, 15, Cursor.HAND);
        sendLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    sendMessage(subjectItem.getValueAsString().trim(),
                            richTextEditor.getValue());
                }
            }
        });
        buttonsLayout.addMember(sendLabel);
        vLayout.addMember(buttonsLayout);

        groupItem = FieldUtil.getTextItem(350, true, "Group", null);
        groupItem.setValue(groupName);
        groupItem.setDisabled(true);
        
        subjectItem = FieldUtil.getTextItem(350, true, "Subject", "[0-9.,A-Za-z-+/_(): ]");
        
        form = FieldUtil.getForm(groupItem, subjectItem);
        form.setWidth(500);
        vLayout.addMember(form);
        
        vLayout.addMember(richTextEditor);
    }
    
    private void sendMessage(String subject, String message) {
        
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
        service.sendGroupMessage(groupName, subject, message, callback);
        modal.show("Sending message to '" + groupName + "'...", true);
    }
}
