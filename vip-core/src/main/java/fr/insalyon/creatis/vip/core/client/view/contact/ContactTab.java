package fr.insalyon.creatis.vip.core.client.view.contact;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.LinkedHashMap;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ContactTab extends Tab {

    private VLayout contactLayout;
    private TextItem nameField;
    private TextItem emailField;
    private SelectItem categoryItem;
    private TextItem subjectField;
    private RichTextEditor commentEditor;
    private IButton submitButton;

    public ContactTab() {

        this.setID(CoreConstants.TAB_CONTACT);
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_HELP) + " Contact");
        this.setCanClose(true);

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        configure();
        vLayout.addMember(contactLayout);

        this.setPane(vLayout);
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(300, null, true);
        nameField.setValue(CoreModule.user.getFullName());

        emailField = FieldUtil.getTextItem(300, null, true);
        emailField.setValue(CoreModule.user.getEmail());

        categoryItem = new SelectItem("category", "Category");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("General contact", "General contact");
        map.put("General question", "General question");
        map.put("Report bug", "Report bug");
        map.put("Report missing term in ontologies", "Report missing term in ontologies");
        categoryItem.setValueMap(map);
        categoryItem.setValue("General contact");
        categoryItem.setShowTitle(false);

        subjectField = FieldUtil.getTextItem(300, null);

        commentEditor = new RichTextEditor();
        commentEditor.setWidth(300);
        commentEditor.setHeight(200);
        commentEditor.setOverflow(Overflow.HIDDEN);
        commentEditor.setShowEdges(true);
        commentEditor.setControlGroups("styleControls", "editControls", 
                "colorControls", "insertControls");        

        submitButton = new IButton("Submit");
        submitButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (subjectField.validate()) {

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            WidgetUtil.resetIButton(submitButton, "Submit", null);
                            Layout.getInstance().setWarningMessage("Unable to send contact email:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            WidgetUtil.resetIButton(submitButton, "Submit", null);
                            Layout.getInstance().setNoticeMessage("Contact successfully sent.");
                            subjectField.clearValue();
                            commentEditor.setValue("");
                        }
                    };
                    service.sendContactMail(
                            categoryItem.getValueAsString(),
                            subjectField.getValueAsString().trim(),
                            commentEditor.getValue(), callback);
                    WidgetUtil.setLoadingIButton(submitButton, "Sending messsage...");
                }
            }
        });

        contactLayout = WidgetUtil.getVIPLayout(330, 370);
        addField("Name", nameField);
        addField("E-mail", emailField);
        addField("Category", categoryItem);
        addField("Subject", subjectField);
        contactLayout.addMember(WidgetUtil.getLabel("<b>Comments</b>", 15));
        contactLayout.addMember(commentEditor);
        contactLayout.addMember(submitButton);
    }

    private void addField(String title, FormItem item) {

        contactLayout.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        contactLayout.addMember(FieldUtil.getForm(item));
    }
}
