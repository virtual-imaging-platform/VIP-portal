package fr.insalyon.creatis.vip.core.client.view.user;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.ExecutionDataPublic;
import org.springframework.beans.factory.annotation.Autowired;

public class MakeExecutionPublicTab extends Tab {
    private VLayout makeExecutionPublicLayout;
    private TextItem nameOfTheExecutionField;
    private TextItem versionExecutionField;
    private TextItem statusExecutionField;
    private TextItem authorNameField;
    private TextAreaItem commentsItem;
    private IButton makeExecutionblicButton;
    private ConfigurationServiceAsync configurationService = ConfigurationService.Util.getInstance();

    public MakeExecutionPublicTab() {

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
                configurationService.addExecution(newExecution, new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        SC.warn("Failed to add execution: " + caught.getMessage());
                    }
                    public void onSuccess(Void result) {
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
}
