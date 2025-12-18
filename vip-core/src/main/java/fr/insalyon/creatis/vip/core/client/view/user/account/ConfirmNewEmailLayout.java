package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;

import fr.insalyon.creatis.vip.core.client.CoreModule;
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
 * @author Axel Bonnet
 */
public class ConfirmNewEmailLayout extends AbstractFormLayout {

    private TextItem codeField;
    private IButton cancelButton;
    private IButton validateButton;

    public ConfirmNewEmailLayout() {

        super("100%", "135");
        addTitle("Email", CoreConstants.ICON_PERSONAL);

        configure();
    }

    private void configure() {

        Label newEmailLabel = WidgetUtil.getLabel("<b>New email : </b>" + CoreModule.user.getNextEmail(), 15);
        codeField = FieldUtil.getTextItem(150, "[a-zA-Z0-9\\-]");
        codeField.setName("Validation code");

        cancelButton = WidgetUtil.getIButton("Cancel", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                        final AsyncCallback<User> callback = new AsyncCallback<User>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                WidgetUtil.resetIButton(validateButton, "Cancel", CoreConstants.ICON_SAVED);
                                Layout.getInstance().setWarningMessage("Unable to validate new email:<br />" + caught.getMessage());
                            }

                            @Override
                            public void onSuccess(User result) {
                                Modules.getInstance().userUpdated(CoreModule.user, result);
                                CoreModule.user = result;

                                WidgetUtil.resetIButton(validateButton, "Cancel", CoreConstants.ICON_SAVED);
                                validateButton.setDisabled(true);
                                cancelButton.setDisabled(true);
                                Layout.getInstance().setNoticeMessage(
                                        "Your email change has been canceled");
                            }
                        };
                        WidgetUtil.setLoadingIButton(validateButton, "Canceling...");
                        service.cancelNewEmail(callback);
                    }
                });
        cancelButton.setWidth(150);

        validateButton = WidgetUtil.getIButton("Validate", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                        final AsyncCallback<User> callback = new AsyncCallback<User>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                WidgetUtil.resetIButton(validateButton, "Validate", CoreConstants.ICON_SAVED);
                                Layout.getInstance().setWarningMessage("Unable to validate new email.<br />" + caught.getMessage());
                            }

                            @Override
                            public void onSuccess(User result) {
                                Modules.getInstance().userUpdated(CoreModule.user, result);
                                CoreModule.user = result;

                                // the user mail has changed, need to update it in the cookie
                                if (Cookies.isCookieEnabled()) {
                                    Cookies.setCookie(CoreConstants.COOKIES_USER, result.getEmail(),
                                            CoreConstants.COOKIES_EXPIRATION_DATE, null, "/", false);
                                }

                                WidgetUtil.resetIButton(validateButton, "Validate", CoreConstants.ICON_SAVED);
                                validateButton.setDisabled(true);
                                cancelButton.setDisabled(true);
                                Layout.getInstance().setNoticeMessage(
                                        "Your email change has been validated");
                            }
                        };
                        WidgetUtil.setLoadingIButton(validateButton, "Saving...");
                        service.confirmNewEmail(codeField.getValueAsString().trim(), callback);
                    }
                });
        validateButton.setWidth(150);

        this.addMember(newEmailLabel);
        addField("Validation code", codeField);

        addButtons(cancelButton, validateButton);
    }
}
