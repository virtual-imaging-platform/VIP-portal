package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.RecoveryTab;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class PasswordLayout extends AbstractFormLayout {

    private PasswordItem currentPasswordField;
    private PasswordItem newPasswordField;
    private PasswordItem confirmPasswordField;
    private IButton saveButton;

    public PasswordLayout() {

        super("100%", "240");
        addTitle("Password", CoreConstants.ICON_PASSWORD);

        configure();

        if (CoreModule.user.getPassword() == null){
            this.addMember(WidgetUtil.getLabel("<font color=\"#666666\"><b>Note</b>: "
                    + "You do not have a password yet, if you wish you can create one by clicking on the button above.</font>", 30));}
    }

    private void configure() {


        currentPasswordField = FieldUtil.getPasswordItem(200, 32);
        newPasswordField = FieldUtil.getPasswordItem(200, 32);
        confirmPasswordField = FieldUtil.getPasswordItem(200, 32);


        saveButton = WidgetUtil.getIButton("Save Changes", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {

                        if (currentPasswordField.validate() & newPasswordField.validate()
                                & confirmPasswordField.validate()) {

                            if (!newPasswordField.getValueAsString().equals(confirmPasswordField.getValueAsString())) {
                                Layout.getInstance().setWarningMessage("Passwords do not match. Please verify the entered password.", 10);
                                newPasswordField.focusInItem();
                                return;
                            }

                            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    WidgetUtil.resetIButton(saveButton, "Save Changes", CoreConstants.ICON_SAVED);
                                    Layout.getInstance().setWarningMessage("Unable to update password:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    currentPasswordField.setValue("");
                                    newPasswordField.setValue("");
                                    confirmPasswordField.setValue("");
                                    WidgetUtil.resetIButton(saveButton, "Save Changes", CoreConstants.ICON_SAVED);
                                    Layout.getInstance().setNoticeMessage("Password successfully updated.");
                                }
                            };
                            WidgetUtil.setLoadingIButton(saveButton, "Saving...");
                    service.updateUserPassword(
                            currentPasswordField.getValueAsString(),
                            newPasswordField.getValueAsString(), callback);
                }
            }
        });

        saveButton.setWidth(200);

        IButton recoverButton = WidgetUtil.getIButton("Forgot Password ?", CoreConstants.ICON_HELP, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                        CoreConstants.TAB_RECOVERY, RecoveryTab::new);
            }
        });

        IButton createPassButton = WidgetUtil.getIButton("Create a Password ?", CoreConstants.ICON_HELP, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                        CoreConstants.TAB_RECOVERY, RecoveryTab::new);
            }
        });

        recoverButton.setWidth(200);
        createPassButton.setWidth(200);
        if (CoreModule.user.getPassword() == null) {
            this.addMember(createPassButton);
        } else {
            addField("Current", currentPasswordField);
            addField("New", newPasswordField);
            addField("Re-type new", confirmPasswordField);
            this.addMember(saveButton);
            this.addMember(recoverButton);

        }
    }
}
