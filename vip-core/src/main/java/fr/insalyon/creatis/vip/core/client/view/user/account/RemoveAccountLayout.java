package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.models.User;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class RemoveAccountLayout extends AbstractFormLayout {

    private CheckboxItem confirmField;
    private IButton removeButton;

    public RemoveAccountLayout() {

        super("350", "113");
        addTitle("Delete Account", CoreConstants.ICON_ACCOUNT_REMOVE);

        configure();
    }

    private void configure() {

        this.addMember(WidgetUtil.getLabel("Removing your account will remove "
                + "all your personal data and simulations", 20));

        confirmField = new CheckboxItem("confirm", "<font color=\"#808080\">Yes, I want to delete my account</font>");
        confirmField.setRequired(true);
        confirmField.setWidth(320);
        confirmField.setAlign(Alignment.LEFT);
        confirmField.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {

                if (confirmField.getValueAsBoolean()) {
                    removeButton.setDisabled(false);
                } else {
                    removeButton.setDisabled(true);
                }
            }
        });

        this.addMember(FieldUtil.getForm(confirmField));

        removeButton = new IButton("Delete VIP Account");
        removeButton.setWidth(150);
        removeButton.setDisabled(true);
        removeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (confirmField.validate() && confirmField.getValueAsBoolean()) {
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<User> callback = new AsyncCallback<User>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            WidgetUtil.resetIButton(removeButton, "Delete VIP Account", null);
                            Layout.getInstance().setWarningMessage("Unable to delete account:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(User result) {
                            Modules.getInstance().userRemoved(result);
                            Layout.getInstance().signout();
                            Layout.getInstance().setNoticeMessage("Your account was successfully deleted.");
                        }
                    };
                    service.removeUser(null, callback);
                    WidgetUtil.setLoadingIButton(removeButton, "Removing account...");
                }
            }
        });

        this.addMember(removeButton);
    }
}
