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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.auth.RecoveryTab;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
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
    private IButton recoverButton;

    public PasswordLayout() {

        super("100%", "240");
        addTitle("Password", CoreConstants.ICON_PASSWORD);

        configure();
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
                                Layout.getInstance().setWarningMessage("Passwords do not match. Please verify the entered password.");
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

        recoverButton = WidgetUtil.getIButton("Create/Forgot Password ?", CoreConstants.ICON_HELP, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                    CoreConstants.TAB_RECOVERY, RecoveryTab::new);
            }
        });

        recoverButton.setWidth(200);
        currentPasswordField.setTooltip("Note: you may not know your VIP password in case your account was automatically generated. If you need it, you can still recover it using the button below.");
        addField("Current", currentPasswordField);
        addField("New", newPasswordField);
        addField("Re-type new", confirmPasswordField);
        this.addMember(saveButton);
        this.addMember(recoverButton);
    }
}
