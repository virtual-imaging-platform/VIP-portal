package fr.insalyon.creatis.vip.core.client.view.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.core.client.rpc.ReproVipServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class MakeExecutionPublicTab extends Tab {
    private VLayout makeExecutionPublicLayout;
    private TextItem nameOfTheExecutionField;
    private TextItem versionExecutionField;
    private TextItem statusExecutionField;
    private TextItem authorNameField;
    private TextAreaItem commentsItem;
    private IButton makeExecutionblicButton;
    private ReproVipServiceAsync reproVipServiceAsync = ReproVipService.Util.getInstance();

    public MakeExecutionPublicTab(Execution execution) {

        this.setID(CoreConstants.TAB_MAKE_EXECUTION_PUBLIC);
        this.setTitle("Make execution public");
        this.setCanClose(true);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        configureExecutionPublicLayout();
        nameOfTheExecutionField.setValue(execution.getName());
        versionExecutionField.setValue(execution.getVersion());
        statusExecutionField.setValue(execution.getStatus());
        authorNameField.setValue(execution.getAuthor());
        commentsItem.setValue(execution.getComments());
        vLayout.addMember(makeExecutionPublicLayout);

        this.setPane(vLayout);
    }

    private void configureExecutionPublicLayout() {

        nameOfTheExecutionField = FieldUtil.getTextItem(300, false, "", null);
        authorNameField = FieldUtil.getTextItem(300, false, "", null);
        versionExecutionField= FieldUtil.getTextItem(300, false, "", null);
        statusExecutionField = FieldUtil.getTextItem(300, false, "", null);

        commentsItem = new TextAreaItem("comment", "");
        commentsItem.setHeight(80);
        commentsItem.setWidth(300);
        commentsItem.setShowTitle(false);

        makeExecutionblicButton = new IButton("Make execution public");

        makeExecutionblicButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                String name = nameOfTheExecutionField.getValueAsString();
                String version = versionExecutionField.getValueAsString();
                String status = statusExecutionField.getValueAsString();
                String author = authorNameField.getValueAsString();
                String comments = commentsItem.getValueAsString();

                Execution newExecution = new Execution(name, version, status, author, comments);
                reproVipServiceAsync.addExecution(newExecution, new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        SC.warn("Failed to add execution: " + caught.getMessage());
                    }
                    public void onSuccess(Void result) {
                        sendExecutionAdminEmail(newExecution);
                        SC.say("Execution added successfully!");
                    }
                });
            }
        });

        makeExecutionPublicLayout = WidgetUtil.getVIPLayout(320);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Name of the execution", nameOfTheExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Version of the execution", versionExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Status of the execution", statusExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Author name", authorNameField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Comments", commentsItem);
        makeExecutionPublicLayout.addMember(makeExecutionblicButton);
    }

    private void sendExecutionAdminEmail(Execution execution) {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to send email to admins:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Email has been sent to admins.");
            }
        };
        reproVipServiceAsync.executionAdminEmail(execution, callback);
    }

}
