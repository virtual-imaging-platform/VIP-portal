package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.fields.*;
import fr.insalyon.creatis.vip.core.client.*;
import fr.insalyon.creatis.vip.core.client.rpc.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.*;
import fr.insalyon.creatis.vip.core.models.User;

/**
 *
 * @author Axel Bonnet
 */
public class RequestNewEmailLayout extends AbstractFormLayout {

    private TextItem emailField;
    private IButton saveButton;

    public RequestNewEmailLayout() {

        super("100%", "135");
        addTitle("Email", CoreConstants.ICON_PERSONAL);

        configure();
    }

    private void configure() {

        Label emailLabel = WidgetUtil.getLabel(CoreModule.user.getEmail(), 15);
        emailField = FieldUtil.getTextItem(200, "[a-zA-Z0-9_.\\-+@]");
        emailField.setName("email");

        emailField.setValidators(ValidatorUtil.getEmailValidator());

        saveButton = WidgetUtil.getIButton("Change email", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        if (emailField.validate()) {

                            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                            final AsyncCallback<User> callback = new AsyncCallback<User>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    WidgetUtil.resetIButton(saveButton, "Change email", CoreConstants.ICON_SAVED);
                                    Layout.getInstance().setWarningMessage("Unable to change email:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(User result) {
                                    Modules.getInstance().userUpdated(CoreModule.user, result);
                                    CoreModule.user = result;

                                    WidgetUtil.resetIButton(saveButton, "Change email", CoreConstants.ICON_SAVED);
                                    Layout.getInstance().setNoticeMessage(
                                            "Your email change has been taken into account. "
                                                    +" Please use the validation code sent to your new email address in order to validate it. <br />"
                                                    + "Note that your login email will not change until you validate the new address");
                                }
                            };
                            WidgetUtil.setLoadingIButton(saveButton, "Saving...");
                            service.requestNewEmail(
                                    emailField.getValueAsString().trim(), callback);
                        }
                    }
                });
        saveButton.setWidth(150);
        this.addMember(emailLabel);
        addField("New email", emailField);
        this.addMember(saveButton);
    }
}
