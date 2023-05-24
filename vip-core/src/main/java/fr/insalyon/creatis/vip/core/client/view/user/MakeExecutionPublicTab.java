package fr.insalyon.creatis.vip.core.client.view.user;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
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
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class MakeExecutionPublicTab extends Tab {
    private VLayout makeExecutionPublicLayout;
    private TextItem nameOfTheExecutionField;
    private TextItem authorNameField;
    private TextAreaItem commentsItem;
    private IButton makeExecutionblicButton;

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

        configureSignupLayout();
        vLayout.addMember(makeExecutionPublicLayout);

        this.setPane(vLayout);
    }

    private void configureSignupLayout() {

        nameOfTheExecutionField = FieldUtil.getTextItem(300, false, "", null);
        authorNameField = FieldUtil.getTextItem(300, false, "", null);

        commentsItem = new TextAreaItem("comment", "");
        commentsItem.setHeight(80);
        commentsItem.setWidth(300);
        commentsItem.setShowTitle(false);

        makeExecutionblicButton = new IButton("Sign Up");

        makeExecutionPublicLayout = WidgetUtil.getVIPLayout(320);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Name of the execution", nameOfTheExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Author name", authorNameField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Comments", commentsItem);
        makeExecutionPublicLayout.addMember(makeExecutionblicButton);
    }
}
