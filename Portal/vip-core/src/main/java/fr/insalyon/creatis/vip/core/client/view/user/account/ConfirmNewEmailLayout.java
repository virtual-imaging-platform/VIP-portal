/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.*;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.*;

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
                                Layout.getInstance().setWarningMessage("Unable to validate new email. Your code may be wrong :<br />" + caught.getMessage());
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
