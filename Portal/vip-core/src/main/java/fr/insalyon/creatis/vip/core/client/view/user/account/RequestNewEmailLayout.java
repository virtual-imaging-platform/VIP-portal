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
import com.smartgwt.client.widgets.form.fields.*;
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
                                                    +" You will receive a mail with a code to validate it.<br />"
                                                    + "Please refresh VIP to enter the code in your account");
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
